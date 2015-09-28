package com.chancorp.tabactivity;

import java.util.ArrayList;

/**
 * Created by Chan on 2015-09-20.
 */
public class FamilyData{
    ArrayList<FamilyMember> data;
    int familyID=1;
    //Some other data should go here.

    public FamilyData(){
        data=new ArrayList<FamilyMember>();
    }

    public void addMembers(FamilyMember fm){
        data.add(fm);
    }

    public void clearMembers(){
        data=new ArrayList<FamilyMember>();
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
