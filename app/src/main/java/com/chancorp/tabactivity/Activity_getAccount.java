package com.chancorp.tabactivity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Baranium on 2015. 10. 21..
 */
public class Activity_getAccount extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.getaccountbackarrow) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_getaccount, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountinfo);
        TextView tv1 = (TextView) findViewById(R.id.getaccounttext1);
        TextView tv2 = (TextView) findViewById(R.id.getaccounttext2);
        String id = ServerComms.getStaticFamilyData().getCredentials().getID();
        String password = ServerComms.getStaticFamilyData().getCredentials().getPassword();
        try {
            tv1.setText("아이디 :  " + id);
        } catch(NullPointerException e) {
            tv1.setText("아이디를 불러올 수 없습니다.\n잠시 후 다시 시도해주세요.");
        }
        try {
            tv2.setText("비밀번호 :  " + password);
        } catch(NullPointerException e) {
            tv2.setText("비밀번호를 불러올 수 없습니다.\n잠시 후 다시 시도해주세요.");
        }
    }
}
