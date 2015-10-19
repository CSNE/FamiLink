package com.chancorp.tabactivity;

import android.util.Log;

import java.io.Serializable;

//Class for storing router information.
public class RouterInformation implements Serializable{



    public String name;
    public String macAddr;

    public int getID() {
        return iD;
    }

    public void setID(int iD) {
        this.iD = iD;
    }

    public int iD;

    public RouterInformation() {
        iD=-1;
    }
    public RouterInformation(String name, String mac){
        this.name=name;
        this.macAddr=mac;
        if (name!=null) stripQuotes();
    }

    public void stripQuotes(){
        this.name=this.name.replace("\"","");
    }

    public String summary() { return this.getName()+" ("+this.getMacAddr()+")"; }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; stripQuotes(); }

    public String toString(){
        return "RouterInformation. SSID: "+this.getName()+" | MAC: "+this.getMacAddr();
    }

    public boolean match(RouterInformation r){
        Log.d("OhmyGod", this.getName() + "   " + this.getMacAddr());
        if(this.getMacAddr().equals(r.getMacAddr())&&this.getName().equals(r.getName())){
            return true;
        }
        else return false;
    }
}
