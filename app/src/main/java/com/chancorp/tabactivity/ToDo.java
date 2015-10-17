package com.chancorp.tabactivity;

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Chan on 2015-09-30.
 */
public class ToDo implements Serializable{

    transient public static final int[] icons={R.drawable.ic_event_black_48dp,R.drawable.ic_access_alarm_black_48dp};
    transient public static final int defaultDrawable=R.drawable.ic_event_black_48dp;
    transient public static final int DUE_WARNING=18000, DUE_CRITICAL=3600;
    long dueTime;
    String title,description;
    int creator;
    int icon;
    int iD;

    public ToDo(){
        this(0,"","",0,-1);
    }


    public ToDo(int creator, String title, String description, long dueTime, int icon){
        this.title=title;
        this.description=description;
        this.creator=creator;
        this.dueTime=dueTime;
        this.icon=icon;
    }


    public String getStringDue(){
        Log.d("Familink","Converting UNIX time: "+this.dueTime);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date time=new java.util.Date((long)this.dueTime*1000);

        String reportDate = df.format(time);

        Log.d("Familink","Converted to: "+reportDate);

        return reportDate;
    }

    public void parseDue(String s){
        Log.d("Familink","ToDo parsing date from string: "+s);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt= null;
        try {
            dt = sdf.parse(s);
        } catch (ParseException e) {
            Log.e("Familink","ParseException while parsing date.");
        }
        Calendar calendar= GregorianCalendar.getInstance();
        calendar.setTime(dt);
        this.setDueTime(calendar.getTimeInMillis() / 1000);
        Log.d("Familink","Parsed UNIX time: "+this.dueTime);
    }

    public long timeLeft(){
        long currentTime = System.currentTimeMillis() / 1000L;
        return (dueTime-currentTime);
    }

    public int getIconDrawable(){
        try {
            return icons[this.icon];
        }catch(ArrayIndexOutOfBoundsException e){
            Log.v("Familink", "ArrayIndexOutOfBoundsException on ToDo "+this.getTitle());
            return defaultDrawable;
        }
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

    public int getID() {
        return iD;
    }

    public void setID(int iD) {
        this.iD = iD;
    }

}
