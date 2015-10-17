package com.chancorp.tabactivity;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;

//이 클래스는 가족 데이터를 저장하고 관리하는 클래스입니다.
public class FamilyData implements Serializable {
    transient public static boolean lock=false;
    int saveIdentifier=1;

    ArrayList<FamilyMember> data;
    ArrayList<RouterInformation> routers;
    ArrayList<ToDo> todos;
    int familyID=-1, myID=-1;
    UserInformation cred;
    transient Context c;

    public void exactCopy(FamilyData fd){

        this.data=fd.data;
        this.routers=fd.routers;
        this.todos=fd.todos;
        this.familyID=fd.familyID;
        this.myID=fd.myID;
        this.cred=fd.cred;

    }

    public void shallowCopyData(FamilyData fd){
        ArrayList<FamilyMember> origData=this.data;

        this.data=fd.data;
        this.routers=fd.routers;
        this.todos=fd.todos;

        for(int i=0;i<this.data.size();i++){
            this.data.get(i).salvageData(origData);
        }
    }

    public void reset(){
        exactCopy(new FamilyData(null));
    }

    public int numInside() {
        int num=0;
        for(FamilyMember fm:data){
            if (fm.isInside()) num++;
        }
        return num;
    }

    public boolean imInside() {
        for(FamilyMember fm:data){
            if (fm.getPersonID()==this.myID){
                return fm.isInside();
            }
        }
        Log.e("Familink", "WHAT THE FUCK");
        return false;
    }



    //Some other data should go here.

    public FamilyData(Context c){
        data=new ArrayList<FamilyMember>();
        todos=new ArrayList<ToDo>();
        routers=new ArrayList<RouterInformation>();
        this.c=c;
    }

    public boolean isRegistered(){
        if (familyID==-1) return false;
        else return true;
    }

    public String personIDToName(int id){
        for (FamilyMember fm:this.data){
            if (fm.getPersonID()==id){
                return fm.getName();
            }
        }
        Log.e("Familink","FamilyData.personIDToName > ID not matched. (ID: "+id+")");
        return null;
    }

    public ArrayList<RouterInformation> getRouters(){
        return this.routers;
    }
    public ArrayList<ToDo> getToDos(){
        return this.todos;
    }
    public ArrayList<FamilyMember> getFamilyMembers(){
        return this.data;
    }

    public void setCredentials(UserInformation c){
        this.cred=c;
    }
    public UserInformation getCredentials(){
        return this.cred;
    }

    public void setFamilyID(int id){
        this.familyID=id;
    }
    public void setMyID(int id){
        this.myID=id;
    }

    public void addMembers(FamilyMember fm){
        data.add(fm);
    }

    public void clearMembers(){
        data=new ArrayList<FamilyMember>();
    }

    public void addRouter(RouterInformation r){
        routers.add(r);
    }
    public void deleteRouter(RouterInformation ri){
        for (int i=0;i<this.routers.size();i++) {
            if (routers.get(i).match(ri)) {
                routers.remove(i);
                break;
            }
        }
    }

    public boolean matchRouter(RouterInformation r) {
        boolean res=false;
        for (RouterInformation ri:this.routers){
            if (ri.match(r)) res=true;
        }
        return res;
    }

    public void addToDo(ToDo td){
        todos.add(td);
    }
    public void addToDo(String title, String description, long due,int icon){
        addToDo(new ToDo(this.myID, title, description, due, icon));
    }
    public ToDo getToDoAt(int pos){
        return todos.get(pos);
    }
    public ToDo[] getToDosInArray(){
        ToDo[] arr;
        arr=new ToDo[todos.size()];
        for (int i=0;i<arr.length;i++){
            arr[i]=this.todos.get(i);
        }
        return arr;
    }


    public void parseData(String s){

        FamilyData parsedData=new FamilyData(null);

        Log.d("FamiLink", "FamilyData.parseData() input:" + s);

        Log.d("Familink","Converted: "+StringUtils.unescapeHtml3(s));

        s=StringUtils.unescapeHtml3(s);

        s=s.replace("<ul class=entries>","").replace("</ul>","");

        String[] parts=s.split("\\[");

        for (String part:parts){

            String[] subparts = part.split("\\]");

            try {
                String partTitle = subparts[0];
                String partBody = subparts[1];
                Log.v("Familink", "Part: " + partTitle);
                String[] members = partBody.split(";");

                for (String member : members) {
                    Log.v("Familink", "   New member");

                    FamilyMember fm=new FamilyMember();
                    ToDo td=new ToDo();
                    RouterInformation ri=new RouterInformation();

                    String[] lines = member.split("&");

                    for (String line : lines) {
                        String[] elements = line.split("=");
                        try {
                            String title = elements[0].trim();
                            String data = elements[1].trim();
                            Log.v("Familink", "      Title:" + title + " | Data:" + data);
                            if (partTitle.equals("PersonInfo")){
                                if (title.equals("name")) fm.setName(data);
                                else if (title.equals("personID")) fm.setPersonID(Integer.parseInt(data));
                                else if (title.equals("phoneNumber")) fm.setPhoneNumber(data);
                                else if (title.equals("isInside")){
                                    if (data.equals("0")) fm.setIsInside(false);
                                    else if (data.equals("1")) fm.setIsInside(true);
                                    else Log.e("Familink","IsInside data is not 0 nor 1.");
                                }else{
                                    Log.e("Familink", "PersonInfo does not match any of its parameters! Line: "+line);
                                }
                            }else if (partTitle.equals("TaskInfo")){
                                if (title.equals("taskID")) td.setID(Integer.parseInt(data));
                                else if (title.equals("name")) td.setTitle(data);
                                else if (title.equals("text")) td.setDescription(data);
                                else if (title.equals("personID")) td.setCreator(Integer.parseInt(data));
                                else if (title.equals("due")) td.parseDue(data,true);
                                else Log.e("Familink", "TaskInfo does not match any of its parameters! Line: "+line);
                            }else if (partTitle.equals("WifiInfo")){
                                if (title.equals("wifiID")) ri.setID(Integer.parseInt(data));
                                else if (title.equals("name")) ri.setName(data);
                                else if (title.equals("address")) ri.setMacAddr(data);
                                else Log.e("Familink", "WiFiInfo does not match any of its parameters! Line: "+line);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.v("Familink", "Array out of bounds caught in line: " + line);
                        }

                    }

                    if (partTitle.equals("PersonInfo")&&(fm.getName()!=null)){
                        parsedData.addMembers(fm);
                    }else if (partTitle.equals("TaskInfo")&&(td.getTitle()!=null)){
                        parsedData.addToDo(td);
                    }else if (partTitle.equals("WifiInfo")&&(ri.getName()!=null)){
                        parsedData.addRouter(ri);
                    }
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                Log.v("Familink", "Array out of bounds caught in part: " + part);
                continue;
            }


        }
        Log.d("Familink","Parsed routers:"+parsedData.getRouterListAsString());
        this.shallowCopyData(parsedData);


    }

    public FamilyMember[] getMembersInArray(){
        FamilyMember[] arr;
        arr=new FamilyMember[data.size()];
        for (int i=0;i<arr.length;i++){
            arr[i]=this.data.get(i);
        }
        return arr;
    }

    public int getMyID(){
        return this.myID;
    }
    public int getFamilyID(){
        return this.familyID;
    }

    public void saveToFile(){
        lock=false;
        try {
            saveIdentifier=new Random().nextInt();
            Log.d("Familink","Data saving... identifier: "+saveIdentifier);
            FileOutputStream fos = c.openFileOutput("familink_family_data", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
            Log.i("Familink", "FamilyData saved.");
        }catch(Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Log.e("Familink","File write error!\n"+errors.toString());
        }
    }

    public void loadFromFile(){
        if (lock==true) Log.e("Familink","FamilyData>loadFromFile() called but it is locked! Are two instances running at the same time?");
        lock=true;

        try {
            FileInputStream fis = c.openFileInput("familink_family_data");
            ObjectInputStream is = new ObjectInputStream(fis);
            FamilyData readFD = (FamilyData) is.readObject();
            is.close();
            fis.close();

            Log.i("Familink", "FamilyData Loaded. Identifier: " + readFD.saveIdentifier);
            this.exactCopy(readFD);
            Log.d("Familink", "FamilyData overwritten.");

        }catch(Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Log.e("Familink","File read error!\n"+errors.toString());
        }
    }

    public String getRouterListAsString(){
        String res=new String();
        for(int i=0;i<routers.size();i++){
            res=res+routers.get(i).toString()+"\n";
        }
        return res;
    }

}

