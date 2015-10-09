package com.chancorp.tabactivity;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

//이 클래스는 가족 데이터를 저장하고 관리하는 클래스입니다.
public class FamilyData{
    ArrayList<FamilyMember> data;
    ArrayList<RouterInformation> routers;
    ArrayList<ToDo> todos;
    int familyID=1, myID=1;

    //Some other data should go here.

    public FamilyData(){
        data=new ArrayList<FamilyMember>();
        todos=new ArrayList<ToDo>();
        routers=new ArrayList<RouterInformation>();
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
    public boolean matchRouter(RouterInformation r){
        for (int i=0;i<routers.size();i++){
            if (routers.get(i).equals(r)) return true;
        }
        return false;
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

        clearMembers();

        Log.d("FamiLink", "FamilyData.parseData() input:" + s);

        s=s.replace("<ul class=entries>","").replace("</ul>","");

        String[] parts=s.split("\\[");

        for (String part:parts){

            String[] subparts = part.split("\\]");

            try {
                String partTitle = subparts[0];
                String partBody = subparts[1];
                Log.d("Familink","Part: "+partTitle);
                String[] members = partBody.split(";");

                for (String member : members) {
                    Log.d("Familink","   New member");

                    FamilyMember fm=new FamilyMember();

                    String[] lines = member.split("&");

                    for (String line : lines) {
                        String[] elements = line.split(":");
                        try {
                            String title = elements[0].trim();
                            String data = elements[1].trim();
                            Log.d("Familink", "      Title:" + title + " | Data:" + data);
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
                        this.addMembers(fm);
                    }
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                Log.v("Familink", "Array out of bounds caught in part: " + part);
                continue;
            }


        }



    }

    public FamilyMember[] getMembersInArray(){
        FamilyMember[] arr;
        arr=new FamilyMember[data.size()];
        for (int i=0;i<arr.length;i++){
            arr[i]=this.data.get(i);
        }
        return arr;
    }

    public int getID(){
        return this.familyID;
    }

}

