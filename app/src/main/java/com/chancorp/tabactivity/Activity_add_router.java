package com.chancorp.tabactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_add_router extends Activity implements View.OnClickListener {
    TextView textview;
    Button btn;
    Handler handler;
    Timer timer = null;
    TimerTask timertask = null;
    boolean ReadytoAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrouter);
        textview = (TextView) findViewById(R.id.addroutertext);
        btn = (Button) findViewById(R.id.addrouterconfirmbtn);
        btn.setOnClickListener(this);
        ((ImageView) findViewById(R.id.help1)).setImageResource(R.drawable.wifi_icon);
        ((ImageView) findViewById(R.id.help2)).setImageResource(R.drawable.smartphone_icon);
        makehandler();
        maketimer();
    }

    private void makehandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ReadytoAdd = false;
                switch (msg.what) {
                    case 0:
                        textview.setText("Wifi is off now.");
                        break;
                    case 1:
                        textview.setText("You are connected to 3G now.");
                        break;
                    case 3:
                        textview.setText("Wifi connection is unstable.");
                        break;
                    case 2:
                        WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifinfo = wifimanager.getConnectionInfo();
                        textview.setText("Router Address : " + wifinfo.getBSSID());
                        ReadytoAdd = true;
                        break;
                }
            }
        };
    }

    private void maketimer() {
        timertask = new TimerTask() {
            @Override
            public void run() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if(info == null) {
                    handler.sendEmptyMessage(0);
                } else {
                    if(info.getType() == 0) {
                        handler.sendEmptyMessage(1);
                    } else {
                        WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        if(wifimanager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                            handler.sendEmptyMessage(2);
                        } else {
                            handler.sendEmptyMessage(3);
                        }
                    }
                }
            }
        };
        timer = new Timer();
        timer.schedule(timertask, 1000, 1000);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addrouterconfirmbtn) {
            if(ReadytoAdd == false) {
                Toast.makeText(this, "Not Ready to Add Router", Toast.LENGTH_LONG).show();
                return;
            }
            Intent it = getIntent();
            try {
                WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifinfo = wifimanager.getConnectionInfo();
                it.putExtra("new_mac", wifinfo.getBSSID());
                it.putExtra("new_name", wifinfo.getSSID());
                timer.cancel();
                setResult(RESULT_OK, it);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}