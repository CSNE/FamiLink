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
    transient public static final int NOT_URGENT=24613, KINDA_URGENT=12353473, URGENT=843621, TOO_LATE=75465;
    long dueTime;
    String title,description;
    int creator;
    int icon;
    int iD;

    public ToDo(){

    }


    public ToDo(int creator, String title, String description, long dueTime, int icon){
        this.title=title;
        this.description=description;
        this.creator=creator;
        this.dueTime=dueTime;
        this.icon=icon;
    }


    public String getStringDue(boolean seconds){
        Log.v("Familink","Converting UNIX time: "+this.dueTime);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date time=new java.util.Date((long)this.dueTime*1000);

        String reportDate = df.format(time);

        Log.v("Familink","Converted to: "+reportDate);
        if (seconds) Log.v("Familink","deleting last 3 chars.");

        if (seconds) return reportDate;
        else return reportDate.substring(0, reportDate.length()-3);
    }

    public void parseDue(String s, boolean seconds){
        Log.v("Familink", "ToDo parsing date from string: " + s);
        if (!seconds) Log.v("Familink",":00 added to string.");
        s=s+":00";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt= null;
        try {
            dt = sdf.parse(s);
            Calendar calendar= GregorianCalendar.getInstance();
            calendar.setTime(dt);
            this.dueTime=(calendar.getTimeInMillis() / 1000);
        } catch (ParseException e) {
            Log.e("Familink", "ParseException while parsing date.");
            this.dueTime=0;
        }

        Log.v("Familink", "Parsed UNIX time: " + this.dueTime);
    }

    public long timeLeft(){
        long currentTime = System.currentTimeMillis() / 1000L;
        return (dueTime-currentTime);
    }

    public int checkUrgency(){

        long left=this.timeLeft();

        Log.v("Familink","Checking urgency.... time left: "+left);

        if (left<0) return TOO_LATE;
        else if (left<DUE_CRITICAL) return URGENT;
        else if (left<DUE_WARNING) return KINDA_URGENT;
        else return NOT_URGENT;
    }

    public int getIconDrawable(){
        try {
            return icons[this.icon];
        }catch(ArrayIndexOutOfBoundsException e){
            Log.v("Familink", "ArrayIndexOutOfBoundsException on ToDo "+this.getTitle());
            return defaultDrawable;
        }
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
