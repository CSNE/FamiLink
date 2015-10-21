package com.chancorp.tabactivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
        imgview.setImageResource(R.mipmap.logo_rev5);
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
        final double EPS = 1000.0;
        double dist = Math.sqrt(Math.pow(sx-sy,2)+Math.pow(ex-ey,2));
        Log.d("Distance", String.valueOf(dist));
        if(dist > EPS) {
            finish();
        }
    }

    public static Bitmap blur(Context context, Bitmap sentBitmap, int radius) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius); //0.0f ~ 25.0f
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        } else {
            return sentBitmap;
        }
    }

    public class mView extends View {

        Context context;
        public mView(Context context) {
            super(context);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.d("Canvas", "Working");
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.background_lockscreen);
            //Bitmap blurred = blur(getContext(), bmp, 10);
            canvas.drawBitmap(bmp, 0,0,paint);
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
