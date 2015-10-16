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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//이 클래스는 가족 데이터를 저장하고 관리하는 클래스입니다.
public class FamilyData implements Serializable, Runnable {

    ArrayList<FamilyMember> data;
    ArrayList<RouterInformation> routers;
    ArrayList<ToDo> todos;
    int familyID=-1, myID=-1;
    Credentials cred;
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

    public ArrayList<RouterInformation> getRouters(){
        return this.routers;
    }

    public void setCredentials(Credentials c){
        this.cred=c;
    }
    public Credentials getCredentials(){
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
    public void deleteRouter(int idx){
        routers.remove(idx);
    }

    /*
    int trytime;
    boolean retvalue;
    final int trylimit = 5;
    Timer mtimer = null;
    TimerTask mtimertask = null;

    public boolean matchRouter(RouterInformation r){
        final RouterInformation r_ = r;
        trytime = 0;
        retvalue = false;
        mtimer = new Timer();
        TimerTask mtimertask = new TimerTask() {
            @Override
            public void run() {
                queryProcess(r_); // Todo : Test!
            }
        };
        mtimer.schedule(mtimertask, 1000, 1000);
        return retvalue;
    }
    private int queryProcess(RouterInformation r) {
        boolean ret = false;
        for(int i=0;i<routers.size();i++) {
            ret |= routers.get(i).match(r);
        }
        trytime ++;
        if(ret) {
            mtimer.cancel();
            retvalue = true;
            return; // success code
        }
        if(trytime > trylimit) {
            mtimer.cancel();
            return; // end code
        }
        return 3; // failed code
    }*/

    public boolean matchRouter(RouterInformation r) {
        //TODO fix
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

        //clearMembers();
        FamilyData parsedData=new FamilyData(null);

        Log.d("FamiLink", "FamilyData.parseData() input:" + s);

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

                    String[] lines = member.split("&");

                    for (String line : lines) {
                        String[] elements = line.split("=");
                        try {
                            String title = elements[0].trim();
                            String data = elements[1].trim();
                            Log.v("Familink", "      Title:" + title + " | Data:" + data);
                            if (partTitle.equals("PersonInfo")){
                                if (title.equals("name")) fm.setName(data);
                                if (title.equals("personID")) fm.setPersonID(Integer.parseInt(data));
                                if (title.equals("phoneNumber")) fm.setPhoneNumber(data);
                                if (title.equals("isInside")){
                                    if (data.equals("0")) fm.setIsInside(false);
                                    else if (data.equals("1")) fm.setIsInside(true);
                                    else Log.e("Familink","IsInside data is not 0 nor 1.");
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.v("Familink", "Array out of bounds caught in line: " + line);
                        }

                    }

                    if (partTitle.equals("PersonInfo")){
                        parsedData.addMembers(fm);
                    }
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                Log.v("Familink", "Array out of bounds caught in part: " + part);
                continue;
            }


        }

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
        try {
            FileOutputStream fos = c.openFileOutput("familink_family_data", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
            Log.d("Familink", "File written successfully.");
        }catch(Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Log.e("Familink","File write error!\n"+errors.toString());
        }
    }

    public void loadFromFile(){
        try {
            FileInputStream fis = c.openFileInput("familink_family_data");
            ObjectInputStream is = new ObjectInputStream(fis);
            FamilyData readFD = (FamilyData) is.readObject();
            is.close();
            fis.close();

            Log.d("Familink", "File read successfully.");
            this.exactCopy(readFD);
            Log.d("Familink", "FamilyData overwritten.");

        }catch(Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Log.e("Familink","File read error!\n"+errors.toString());
        }
    }


    @Override
    public void run() {

    }
}

