package com.chancorp.tabactivity;

//Class for storing router information.
public class RouterInformation {


    public String name;
    public String macAddr;

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
