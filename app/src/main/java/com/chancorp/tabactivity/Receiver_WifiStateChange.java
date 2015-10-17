package com.chancorp.tabactivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Receiver_WifiStateChange extends BroadcastReceiver {

    SharedPreferences mPref = null;
    SharedPreferences.Editor edit = null;
    boolean electronics = false;
    Timer mTimer = null;
    TimerTask mTimerTask = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mPref == null) mPref = PreferenceManager.getDefaultSharedPreferences(context);
        if(edit == null) edit = mPref.edit();
        electronics = mPref.getBoolean("familink_Electronics", false);
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        String WIFI1 = new String();
        WIFI1 = context.getResources().getString(R.string.Wifi_change1);
        String WIFI2 = new String();
        WIFI2 = context.getResources().getString(R.string.Wifi_change2);

        if (intent.getAction().equals(WIFI1) || intent.getAction().equals(WIFI2)) {

            if(wifimanager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) { // disabled
                WifiInfo wifinfo = wifimanager.getConnectionInfo();
                edit.putString("familink_lastest", "undefined");
                edit.commit();
                Log.d("disabled", "is there server delay?");
                ServerComms sc = new ServerComms();
                sc.updateStatus(new RouterInformation(wifinfo.getSSID(), wifinfo.getBSSID()), electronics, wifimanager.getWifiState(), context);
            }

            else if(wifimanager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) { // enabled
                WifiInfo wifinfo = wifimanager.getConnectionInfo();
                try {
                    if(wifinfo.getBSSID().equals(mPref.getString("familink_lastest","undefined")) == false) {
                        edit.putString("familink_lastest", wifinfo.getBSSID());
                        edit.commit();
                        Log.d("enabled", "is there server delay?  " + wifinfo.getBSSID());
                        ServerComms sc = new ServerComms();
                        sc.updateStatus(new RouterInformation(wifinfo.getSSID(), wifinfo.getBSSID()), electronics, wifimanager.getWifiState(), context);
                        // sending boolean electronics : check whether gonna do alarm.
                    }
                } catch(NullPointerException e) {
                    Log.d("Android Developers: ", "Such an idiot!");
                    e.printStackTrace();
                }
            }

        }
    }
}