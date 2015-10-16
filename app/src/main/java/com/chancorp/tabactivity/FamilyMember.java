package com.chancorp.tabactivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

//가족 구성원 1명에 대한 데이터 저장.
public class FamilyMember implements Serializable, CredReturnListener{



    String name;
    String phoneNumber;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    String nickname;

    public static final int[] avatarIdToDrawable={R.drawable.icon_father,
                              R.drawable.icon_mother,
                              R.drawable.icon_sister,
                              R.drawable.icon_son,
                              R.drawable.icon_grandfather,
                              R.drawable.icon_grandmother};
    int defaultDrawable=R.drawable.ic_mood_black_48dp;


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
        try {
            return avatarIdToDrawable[this.avatar];
        }catch(ArrayIndexOutOfBoundsException e){
            Log.v("Familink", "Avaratar ID to Drawable index out of bounds.");
        }
        return defaultDrawable;
    }

    public void openMessenger(FragmentActivity a) {
        String uriStr = "sms:" + this.phoneNumber;
        Intent itt = new Intent(Intent.ACTION_VIEW);
        itt.setData(Uri.parse(uriStr));

        a.startActivity(itt);
    }

    public void openDialer(FragmentActivity a) {
        String uriStr = "tel:" + this.phoneNumber;
        Intent itt = new Intent(Intent.ACTION_CALL, Uri.parse(uriStr));
        a.startActivity(itt);
    }
    public void openSettings(FragmentActivity a) {
        Log.d("Familink", "Opening settings for member" + getName());
        CredentialsGetter cg=new CredentialsGetter(a,CredentialsGetter.NICKNAME_AND_AVATAR);
        cg.setTitle("Set Family Info");
        cg.setHint("Nickname");
        cg.init();
        cg.setOnReturnListener(this);
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

    @Override
    public void onReturn(Credentials c) {
        this.setAvatar(c.getAvatar());
        this.setNickname(c.getNickname());
    }

    public void salvageData(ArrayList<FamilyMember> origData) {
        for (FamilyMember fm:origData){
            if(fm.getPersonID()==this.getPersonID()){
                this.setAvatar(fm.getAvatar());
                this.setNickname(fm.getNickname());
            }
        }
    }
}
