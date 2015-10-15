package com.chancorp.tabactivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class Receiver_WifiStateChange extends BroadcastReceiver  {

    static boolean AlreadyRunning = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String WIFI1 = new String();
        WIFI1 = context.getResources().getString(R.string.Wifi_change1);
        String WIFI2 = new String();
        WIFI2 = context.getResources().getString(R.string.Wifi_change2);
        if(intent.getAction().equals(WIFI1) || intent.getAction().equals(WIFI2)) {
            WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifinfo = wifimanager.getConnectionInfo();
            Log.d("FamiLink", "Info:"+wifinfo.getSSID()+wifinfo.getBSSID());
            ServerComms sc=new ServerComms();
            sc.updateStatus(new RouterInformation(wifinfo.getSSID(),wifinfo.getBSSID()));
        }
    }

    private void start_the_activity(Context context) {
        if(AlreadyRunning) return;
        AlreadyRunning = true;
        Intent i = new Intent(context, Activity_Lockscreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}