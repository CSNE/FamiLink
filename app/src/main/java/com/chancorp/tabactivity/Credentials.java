package com.chancorp.tabactivity;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//계정 정보 조장 클래스

public class Credentials {

    String password, iD;

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHash(){
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(this.password.getBytes());
            byte[] digest = sha.digest();

            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest)
                sb.append(String.format("%02x", b & 0xff));
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            Log.e("Familink", "NoSuchAlgorithmException on ServerComms>setQueryHash");
            return new String();
        }

    }



}
