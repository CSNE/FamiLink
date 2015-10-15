package com.chancorp.tabactivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_Lockscreen extends AppCompatActivity implements Runnable {

    Handler hand = null;
    ImageView img;
    Button btn;
    TextView textview;
    Timer timer;
    TimerTask timertask = null;
    static boolean AlreadyTimer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen);
        img = (ImageView)findViewById(R.id.imgview);
        img.setImageResource(R.drawable.flower1);
        btn = (Button) findViewById(R.id.dislock);
        textview = (TextView) findViewById(R.id.MacAddressText);
        hand = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1) {
                    WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifinfo = wifimanager.getConnectionInfo();
                    textview.setText("Router Address : " + wifinfo.getBSSID());
                } else if(msg.what == 2) {
                    textview.setText("Wifi is off, now.");
                } else if(msg.what == 3) {
                    textview.setText("Your are Runing 3G DATA!");
                }
            }
        };
        textStart();
    }

    public void textStart() {
        if(AlreadyTimer) return;
        AlreadyTimer = true;
        timertask = new TimerTask() {
            @Override
            public void run() {
                Log.d("MainActivity_1_1", "Timer is Running");
                UpdateText();
            }
        };
        timer = new Timer();
        timer.schedule(timertask, 1000, 2000);
    }

    private void UpdateText() {
        ConnectivityManager cntmng = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwi = cntmng.getActiveNetworkInfo();
        if(nwi == null) {
            hand.sendEmptyMessage(2);
        }
        else {
            if (nwi.getType() == 0) {
                hand.sendEmptyMessage(3);
            } else {
                WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                if (wifimanager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                    hand.sendEmptyMessage(1);
                } else {
                    hand.sendEmptyMessage(2);
                }
            }
        }
        return;
    }

    public void dislockonClick(View v) {
        Receiver_WifiStateChange.AlreadyRunning = false;
        if(timertask != null) {
            timer.cancel();
            AlreadyTimer = false;
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {}
}
