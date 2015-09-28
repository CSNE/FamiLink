package com.chancorp.tabactivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

//가족 구성원 1명에 대한 데이터 저장.
public class FamilyMember {
    String name, phoneNumber;


    int personID;
    boolean isInside;
    int avatar;

    public FamilyMember() {
        this("None", -1, false, -1, "None");
    }

    public FamilyMember(String name, int personID, boolean isInside, int avatar, String num) {
        this.name = name;
        this.personID = personID;
        this.isInside = isInside;
        this.avatar = avatar;
        this.phoneNumber = num;
    }

    public String getDataString() {
        if (this.isInside) return new String("Inside");
        else return new String("Not Inside");
    }

    public int getAvatarDrawable() {
        return this.avatar;
    }

    public void openMessenger(FragmentActivity a) {
        String uriStr = "tel:" + this.phoneNumber;
        Intent itt = new Intent(Intent.ACTION_CALL, Uri.parse(uriStr));
        a.startActivity(itt);
    }

    public void openDialer(FragmentActivity a) {
        String uriStr = "sms:" + this.phoneNumber;
        Intent itt = new Intent(Intent.ACTION_VIEW, Uri.parse(uriStr));
        a.startActivity(itt);
    }

    public void update() {

    }






    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public boolean isInside() {
        return isInside;
    }

    public void setIsInside(boolean isInside) {
        this.isInside = isInside;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

}
