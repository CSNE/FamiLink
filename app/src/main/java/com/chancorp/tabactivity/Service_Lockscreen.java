package com.chancorp.tabactivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Service_Lockscreen extends Service {
    String WIFI1 = new String();
    String WIFI2 = new String();
    NotificationManager ntm = null;
    Notification ntf;

    private Receiver_Lockscreen mreceiver = null;
    // here, we right all final Intent receivers and add intent.
    IntentFilter intent_filter;
    //
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        ntf = null;
        super.onDestroy();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if(intent != null) {
            if(intent.getAction() == null) {
                if(mreceiver == null) {
                    mreceiver = new Receiver_Lockscreen();
                    /*
                    try {
                        WifiManager mng = (WifiManager) getSystemService(WIFI_SERVICE);
                        WifiInfo wifi_info = mng.getConnectionInfo();
                        String mac = wifi_info.getMacAddress();
                    } catch(Exception e) {e.printStackTrace();}
                    */
                    intent_filter = new IntentFilter();
                    intent_filter.addAction(WIFI1);
                    intent_filter.addAction(WIFI2);
                    Log.d("tag", "Also this part is running!");
                    registerReceiver(mreceiver, intent_filter);
                }
            }
        }
        version_check_and_startForeground();
        return START_REDELIVER_INTENT;
    }

    public void version_check_and_startForeground() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            startForeground(1, new Notification());
        } else {
            if(ntf != null) return;
            startForeground(1,new Notification());
            onBtnNotification();
        }
    }

    public void onBtnNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, Activity_Lockscreen.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // 작은 아이콘 이미지.
        builder.setSmallIcon(R.mipmap.ic_launcher);
        // 알림이 출력될 때 상단에 나오는 문구.
        builder.setTicker("FamiLink 위치공유 실행 중...");
        // 알림 출력 시간.
        builder.setWhen(System.currentTimeMillis());
        // 알림 제목.
        builder.setContentTitle("FamiLink 위치공유");
        // 프로그래스 바.유유
        builder.setProgress(100, 50, true);
        // 알림 내용.
        builder.setContentText("기기의 위치를 주기적으로 송신합니다.");
        // 알림시 사운드, 진동, 불빛을 설정 가능.
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
        // 알림 터치시 반응.
        builder.setContentIntent(pendingIntent);
        // 알림 터치시 반응 후 알림 삭제 여부.
        builder.setAutoCancel(true);
        // 우선순위.
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        // 행동 최대3개 등록 가능.
        builder.addAction(R.mipmap.ic_launcher, "Show", pendingIntent);
        builder.addAction(R.mipmap.ic_launcher, "Hide", pendingIntent);
        // 고유ID로 알림을 생성.
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(123456, builder.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WIFI1 = getString(R.string.Wifi_change1);
        WIFI2 = getString(R.string.Wifi_change2);
        mreceiver = new Receiver_Lockscreen();
        intent_filter = new IntentFilter();
        intent_filter.addAction(WIFI1);
        intent_filter.addAction(WIFI2);
        registerReceiver(mreceiver, intent_filter);
    }
    private void registerreceiver(Receiver_Lockscreen myreceiver, IntentFilter filter ) {
        registerReceiver(myreceiver, filter);
    }

}
