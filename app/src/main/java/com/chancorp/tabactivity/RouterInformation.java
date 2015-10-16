package com.chancorp.tabactivity;

import java.io.Serializable;

//Class for storing router information.
public class RouterInformation implements Serializable{



    public String name;
    public String macAddr;

    public RouterInformation() {
        this.name="";
        this.macAddr="";
    }
    public RouterInformation(String name, String mac){
        this.name=name;
        this.macAddr=mac;
    }


    public String summary(){
        return this.getName()+" ("+this.getMacAddr()+")";
    }
    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return "RouterInformation. SSID: "+this.getName()+" | MAC: "+this.getMacAddr();
    }

    public boolean match(RouterInformation r){
        if(this.getMacAddr().equals(r.getMacAddr())&&this.getName().equals(r.getName())){
            return true;
        }
        else return false;
    }
}
