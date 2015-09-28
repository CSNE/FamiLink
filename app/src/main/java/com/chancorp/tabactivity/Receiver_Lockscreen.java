package com.chancorp.tabactivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver_Lockscreen extends BroadcastReceiver  {

    static boolean AlreadyRunning = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String WIFI1 = new String();
        WIFI1 = context.getResources().getString(R.string.Wifi_change1);
        String WIFI2 = new String();
        WIFI2 = context.getResources().getString(R.string.Wifi_change2);
        if(intent.getAction().equals(WIFI1) || intent.getAction().equals(WIFI2)) {
            start_the_activity(context);
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
