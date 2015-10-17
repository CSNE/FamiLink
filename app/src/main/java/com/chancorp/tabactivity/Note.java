package com.chancorp.tabactivity;

import java.io.Serializable;

/**
 * Created by Chan on 2015-10-17.
 */
public class Note implements Serializable{
    String title,body;
    long creationDate;



    public Note(){

    }
    public Note(String title, String body, long date){
        this.title=title;
        this.body=body;
        this.creationDate=date;
    }




    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
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
