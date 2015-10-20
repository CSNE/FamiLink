package com.chancorp.tabactivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Receiver_WifiStateChange extends BroadcastReceiver {

    private static final int TIME_DELAY_FOR_SLOW_CONNECTION = 500;
    SharedPreferences mPref = null;
    SharedPreferences.Editor edit = null;
    WifiManager wifimanager;
    WifiInfo wifinfo;
    boolean electronics = false;
    String lastestssid = null, lastestbssid = null;;

    @Override
    public void onReceive(Context context, Intent intent) {

        String WIFI1 = new String();
        WIFI1 = context.getResources().getString(R.string.Wifi_change1);
        String WIFI2 = new String();
        WIFI2 = context.getResources().getString(R.string.Wifi_change2);

        if (intent.getAction().equals(WIFI1) || intent.getAction().equals(WIFI2)) {

            if (mPref == null) mPref = PreferenceManager.getDefaultSharedPreferences(context);
            if(edit == null) edit = mPref.edit();
            electronics = mPref.getBoolean("familink_Electronics", false);
            if(lastestssid ==  null) lastestssid = "undefined";
            if(lastestbssid == null) lastestbssid = "undefined";

            wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifinfo = wifimanager.getConnectionInfo();

            String nowssid = wifinfo.getSSID();
            String nowbssid = wifinfo.getBSSID();
            if(nowbssid == null) {
                if(lastestbssid != null) {
                    boolean decision = ServerComms.fd.matchRouter(new RouterInformation(lastestssid, lastestbssid));
                    ServerComms sc = new ServerComms(context);
                    sc.updateStatus(new RouterInformation("",""), electronics, decision, false, context);
                }
            } else {
                if(lastestbssid == null) {
                    ServerComms sc = new ServerComms(context);
                    sc.updateStatus(new RouterInformation(nowssid,nowbssid), electronics, false, true, context);
                } else {
                    ServerComms sc = new ServerComms(context);
                    boolean decision = ServerComms.fd.matchRouter(new RouterInformation(lastestssid, lastestbssid));
                    sc.updateStatus(new RouterInformation(nowssid,nowbssid), electronics, decision, true, context);
                }
            }
            lastestbssid = nowbssid;
            lastestssid = nowssid;

            /*
            if(wifimanager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) { // disabled
                try {
                    if(lastestssid.equals("undefined") == false) {
                        Log.w("Familink","Latest router info: "+new RouterInformation(lastestssid,lastestbssid).summary());
                        boolean decision = ServerComms.fd.matchRouter(new RouterInformation(lastestssid,lastestbssid));
                        Log.d("Decision  :  ", String.valueOf(decision) + "  " + lastestssid + "  " + lastestbssid);

                        ServerComms sc = new ServerComms(context);
                        sc.updateStatus(new RouterInformation("",""), electronics, decision, false, context);
                        lastestssid = "undefined";
                        lastestbssid = "undefined";
                    }
                } catch(NullPointerException e) {
                    Log.d("Android Developers_: ", "Such an idiot!_");
                }
            }

            else if(wifimanager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) { // enabled
                try {
                    if(lastestbssid.equals("undefined") == true) {
                        if(wifinfo.getBSSID() == null) return;
                        lastestssid = wifinfo.getSSID();
                        lastestbssid = wifinfo.getBSSID();

                        Log.d("enabled", "is there server delay?  " + wifinfo.getBSSID());
                        ServerComms sc = new ServerComms(context);
                        sc.updateStatus(new RouterInformation(wifinfo.getSSID(), wifinfo.getBSSID()), electronics, false, true, context);
                    }
                } catch(NullPointerException e) {
                    Log.d("Android Developers: ", "Such an idiot!");
                    e.printStackTrace();
                }
            }
            */
        }
        return;
    }

}