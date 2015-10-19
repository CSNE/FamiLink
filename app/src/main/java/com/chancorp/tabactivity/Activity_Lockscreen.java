package com.chancorp.tabactivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class Activity_Lockscreen extends Activity implements View.OnDragListener, View.OnTouchListener {

    ImageView imgview;
    Vibrator vb = null;
    float sx,sy,ex,ey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN);


        imgview = (ImageView) findViewById(R.id.imageview_lockscreen);
        imgview.setImageResource(R.drawable.background_lockscreen);
        imgview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    sx = event.getX();
                    sy = event.getY();
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    ex = event.getX();
                    ey = event.getY();
                    Checkdistance(sx,sy,ex,ey);
                }
                return true;
            }
        });

        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(2000);
        return;
    }

    private void Checkdistance(float sx, float sy, float ex, float ey) {
        final double EPS = 10;
        double dist = Math.sqrt(Math.pow(sx-sy,2)+Math.pow(ex-ey,2));
        Log.d("Distance", String.valueOf(dist));
        if(dist > EPS) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }
}
