package com.chancorp.tabactivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class Receiver_WifiStateChange extends BroadcastReceiver  {

    SharedPreferences mPref = null;
    Boolean electronics = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(mPref == null) mPref = PreferenceManager.getDefaultSharedPreferences(context);
        electronics = mPref.getBoolean("Electronics", false);

        String WIFI1 = new String();
        WIFI1 = context.getResources().getString(R.string.Wifi_change1);
        String WIFI2 = new String();
        WIFI2 = context.getResources().getString(R.string.Wifi_change2);

        if(intent.getAction().equals(WIFI1) || intent.getAction().equals(WIFI2)) {
            WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifinfo = wifimanager.getConnectionInfo();
            Log.d("FamiLink", "Info:"+wifinfo.getSSID()+wifinfo.getBSSID());
            if(electronics) {
                //implementation : telling electronics if electronics switch is on!
                Log.d("Familink", "INFOFOFOFOF");
            }
            ServerComms sc=new ServerComms();
            sc.updateStatus(new RouterInformation(wifinfo.getSSID(),wifinfo.getBSSID()));
        }
    }

}