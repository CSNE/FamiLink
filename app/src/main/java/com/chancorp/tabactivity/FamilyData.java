package com.chancorp.tabactivity;

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
    public void addToDo(String title, String description, long due){
        addToDo(new ToDo(this.myID,title,description,due));
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


    public void parseData(String s) throws FamilyDataException{

        clearMembers();

        System.out.println("parsing data...");

        String[] memberSplit=s.split(";");
        for (String ms:memberSplit){

            String[] memberDatas=ms.split("\\&");

            FamilyMember f=new FamilyMember();

            for (String md:memberDatas){

                String[] memberDatasSplit=md.split(":");
                if (memberDatasSplit.length<2) continue;

                String dataName=memberDatasSplit[0];
                String dataBody=memberDatasSplit[1];
                //System.out.println("name - " + dataName);
                //System.out.println("dat - " + dataBody);

                if (dataName.equals("personID")) f.setPersonID(Integer.parseInt(dataBody));
                else if (dataName.equals("name")) f.setName(dataBody);
                else if (dataName.equals("isInside")) f.setIsInside(Boolean.parseBoolean(dataBody));
                else if (dataName.equals("phoneNumber")) f.setPhoneNumber(dataBody);
                else throw new FamilyDataException("A String has fallen through!");


            }

            addMembers(f);

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

class FamilyDataException extends Exception{
    public FamilyDataException(){}

    public FamilyDataException(String msg){
        super(msg);
    }
}
