package com.chancorp.tabactivity;

import java.io.Serializable;

//Class for storing router information.
public class RouterInformation implements Serializable{

    static final long serialVersionUID = 1L;

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
}
