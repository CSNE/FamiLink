package com.chancorp.tabactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Activity_Module1 extends AppCompatActivity implements View.OnClickListener {

    Button onbtn, offbtn, listofrouterbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backend_main);

        Receiver_Lockscreen.AlreadyRunning = false;
        onbtn = (Button) findViewById(R.id.onbtn);
        offbtn = (Button) findViewById(R.id.offbtn);
        listofrouterbtn = (Button) findViewById(R.id.listofrouterbtn);
        onbtn.setOnClickListener(this);
        offbtn.setOnClickListener(this);
        listofrouterbtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onClick(View v) {
        Intent it = new Intent(this, Service_Lockscreen.class);
        if(v.getId() == R.id.onbtn) {
            startService(it);
        } else if(v.getId() == R.id.offbtn) {
            stopService(it);
        } else if(v.getId() == R.id.listofrouterbtn) {
            startActivity(new Intent(this, Activity_List_of_router.class));
        }
        return;
    }
}
