package com.chancorp.tabactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
/**
 * Created by Baranium on 2015. 10. 22..
 */
public class Activity_copyright extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copyright);
        TextView tv = (TextView) findViewById(R.id.copyrighttextview);
        tv.setText(
                "Family Logos By Snehal Patil (CC-BY-3.0-US)\n\n" +
                "Material Design Icons by Google (CC-BY-4.0)\n\n" +
                "StringUtils class by Nick Frolov\n\n" +
                "ServerComms POST code from http://www.xyzws.com/javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139\n\n" +
                "ic_chat.png by www.solarheatventi.cz\n\n" +
                "icon_working_man_white.png by Wheelbarrow by João Proença from the Noun Project");
        /*
COPYRIGHT CONTROL

Family Logos By Snehal Patil (CC-BY-3.0-US)
Material Design Icons by Google (CC-BY-4.0)
StringUtils class by Nick Frolov
ServerComms POST code from http://www.xyzws.com/javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139
ic_chat.png by www.solarheatventi.cz
ic_workingman.png by www.solarheatventi.cz
icon_working_man_white.png by Wheelbarrow by João Proença from the Noun Project
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_copyright, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.copyrightbackarrow) finish();
        return true;
    }
}
