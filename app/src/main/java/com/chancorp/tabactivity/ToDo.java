package com.chancorp.tabactivity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Chan on 2015-09-30.
 */
public class ToDo implements Serializable{

    static final long serialVersionUID = 1L;


    long dueTime;
    String title,description;
    int creator, icon;

    public ToDo(){
        this(0,"","",0,0);
    }


    public ToDo(int creator, String title, String description, long dueTime, int icon){
        this.title=title;
        this.description=description;
        this.creator=creator;
        this.dueTime=dueTime;
        this.icon=icon;
    }


    public String getStringDue(){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date today = Calendar.getInstance().getTime();

        String reportDate = df.format(today);


        return reportDate;
    }

    public int getIconDrawable(){
        return this.icon;
    }

    public void accept(){

    }








    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public long getDueTime() {
        return dueTime;
    }

    public void setDueTime(long dueTime) {
        this.dueTime = dueTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
