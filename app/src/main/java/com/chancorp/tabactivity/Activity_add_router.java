package com.chancorp.tabactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
        setContentView(R.layout.activity_addrouter);
        textview = (TextView) findViewById(R.id.addroutertext);
        btn = (Button) findViewById(R.id.addrouterconfirmbtn);
        btn.setOnClickListener(this);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_router);
        Bitmap resizebmp = Bitmap.createScaledBitmap(bmp, width/2, width/2, true);
        ((ImageView) findViewById(R.id.help1)).setImageBitmap(resizebmp);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_smartphone);
        resizebmp = Bitmap.createScaledBitmap(bmp, width / 2, width / 2, true);
        ((ImageView) findViewById(R.id.help2)).setImageBitmap(resizebmp);
        ((TextView) findViewById(R.id.doum)).setText("집의 공유기에 핸드폰의 와이파이를 연결해주세요.\n" +
                "연결이 되었으면, 위의 버튼을 눌러 가족 공유기 추가를 진행해주세요." +
                "\n가족 공유기 정보는 자동으로 서버에 저장되어, 전 가족이 공유하게 됩니다." +
                "\n공유기 리스트를 바탕으로 가족 구성원이 집에 있는지 여부를 판단하게 됩니다.");

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
                        textview.setText("Wi-Fi가 해제되어 있습니다.\n"+"Wi-Fi를 사용해 주세요.");
                        break;
                    case 1:
                        textview.setText("모바일 데이터에 연결되어 있습니다.\n"+"Wi-Fi를 사용해 주세요.");
                        break;
                    case 3:
                        textview.setText("현재 인터넷 연결중이거나, 연결이 불안정합니다...");
                        break;
                    case 2:
                        WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifinfo = wifimanager.getConnectionInfo();
                        textview.setText("현재 연결된 Wi-Fi 주소는,\n" + wifinfo.getSSID() + "입니다.");
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
        timer.schedule(timertask, 500, 500);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addrouterconfirmbtn) {
            if(ReadytoAdd == false) {
                Toast.makeText(this, "아직 공유기를 추가할 수 없습니다.", Toast.LENGTH_LONG).show();
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
        try {
            timer.cancel();
        } catch(Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}