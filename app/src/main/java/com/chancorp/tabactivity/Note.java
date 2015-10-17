package com.chancorp.tabactivity;

import java.io.Serializable;

/**
 * Created by Chan on 2015-10-17.
 */
public class Note implements Serializable{
    String title,body;



    int iD;


    public Note(){

    }
    public Note(String title, String body, int id){
        this.title=title;
        this.body=body;
        this.iD=id;
    }

    public boolean match(Note n){
        if(n.body.equals(this.body)&&n.title.equals(this.title)) return true;
        else return false;
    }


    public int getID() {
        return iD;
    }

    public void setID(int iD) {
        this.iD = iD;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
