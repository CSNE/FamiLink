package com.chancorp.tabactivity;

/**
 * Created by Chan on 2015-09-30.
 */
public class ToDo {
    long dueTime;
    String title,description;
    int creator;

    public ToDo(){
        this(0,"","",0);
    }


    public ToDo(int creator, String title, String description, long dueTime){
        this.title=title;
        this.description=description;
        this.creator=creator;
        this.dueTime=dueTime;
    }




}
