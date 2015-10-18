package com.chancorp.tabactivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Baranium on 2015. 10. 18..
 */
public class Activity_Log_Out extends Activity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        btn = (Button) findViewById(R.id.logoutbtn);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.logoutbtn) {
            //fd.reset();
            //serverConnector.redrawFragments();
        }
    }
}
