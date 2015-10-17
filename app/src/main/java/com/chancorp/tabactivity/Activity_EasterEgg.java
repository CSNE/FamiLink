package com.chancorp.tabactivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Baranium on 2015. 10. 15..
 */
public class Activity_EasterEgg extends Activity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easteregg);
        image = (ImageView) findViewById(R.id.Aimer);
        image.setImageResource(R.drawable.easteregg);
        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
