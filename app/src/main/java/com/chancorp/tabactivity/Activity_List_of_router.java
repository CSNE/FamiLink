package com.chancorp.tabactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Activity_List_of_router extends Activity implements View.OnClickListener {
    Button btn;
    ListView listview;
    ArrayList<String> arraylist;
    CustomAdapter01 adapter = null;
    final int REQUEST_CODE = 100100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listofrouter);

        listview = (ListView) findViewById(R.id.listofrouterlist);
        arraylist = new ArrayList<String>();
        adapter = new CustomAdapter01(this, R.layout.customadapter01design, arraylist);
        listview.setAdapter(adapter);
        btn = (Button) findViewById(R.id.addrouterbtn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addrouterbtn) {
            startActivityForResult(new Intent(this, Activity_add_router.class), REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                arraylist.add(data.getStringExtra("new_address")+"   ("+data.getStringExtra("new_address_date")+")");
                adapter.notifyDataSetChanged();
            }
        }
    }
}
