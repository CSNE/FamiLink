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

package com.chancorp.tabactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


//프로그램 시작 시 보여지는 Activity..

public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener, Redrawable, View.OnTouchListener {

    //TODO images instead of avatars?
    //TODO Diary page?(instead of notes)
    //TODO Delete Family
    //TODO Delete Person


    FamilyData fd;
    ServerComms serverConnector;
    Redrawable[] rd = new Redrawable[5];
    View tab1View, tab2View, tab3View, tab4View;
    Menu menu;
    int nowid;
    float sx,sy,dist;
    final double EPS = 500f;
    ActionBar.Tab listPageTab;
    ActionBar.Tab todoPageTab;
    ActionBar.Tab notePageTab;
    ActionBar.Tab chatPageTab;
    ActionBar actionbar;
    ViewPager viewpager;

    private double dist(float sx,float sy,float ex,float ey) {
        return Math.sqrt(Math.pow(sx-ex,2.0)+Math.pow(sy-ey,2.0));
    }

    final private View.OnTouchListener MyListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN :
                    sx = event.getX();
                    sy = event.getY();
                    break;
                case MotionEvent.ACTION_UP :
                    if(dist(event.getX(),event.getY(),sx,sy) < EPS) break;
                    if(sx<event.getX()) { // to right.
                        if(nowid == 4) break;
                        if(nowid == 1) {

                        } else if(nowid == 2) {

                        } else if(nowid == 3) {

                        }
                    }
                    else break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Familink", "-----------New Session started.-----------");
        nowid = 1;

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);
        fd = new FamilyData(getApplicationContext());
        setContentView(R.layout.activity_main);

        actionbar = getSupportActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        viewpager = (ViewPager) findViewById(R.id.pager);
        CustomAdapter02 customadapter = new CustomAdapter02(getLayoutInflater());
        viewpager.setAdapter(customadapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {actionbar.setSelectedNavigationItem(position);}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        listPageTab = actionbar.newTab();
        todoPageTab = actionbar.newTab();
        notePageTab = actionbar.newTab();
        chatPageTab = actionbar.newTab();

        tab1View = getLayoutInflater().inflate(R.layout.tab_layout, null);
        listPageTab.setCustomView(tab1View);
        setupTab(tab1View, null, 0, R.drawable.ic_view_list_white);

        tab2View = getLayoutInflater().inflate(R.layout.tab_layout, null);
        todoPageTab.setCustomView(tab2View);
        setupTab(tab2View, String.valueOf(fd.getToDos().size()), 0, R.drawable.ic_workingman_white);

        tab3View = getLayoutInflater().inflate(R.layout.tab_layout, null);
        notePageTab.setCustomView(tab3View);
        setupTab(tab3View, null, 0, R.drawable.ic_event_note_white);

        tab4View = getLayoutInflater().inflate(R.layout.tab_layout, null);
        chatPageTab.setCustomView(tab4View);
        setupTab(tab4View, null, 0, R.drawable.ic_chat_white);


        Fragment listPage = (Fragment) new Page1List();
        Fragment todoPage = (Fragment) new Page2ToDo();
        Fragment notePage = (Fragment) new Page3Note();
        Fragment chatPage = (Fragment) new Page4Chat();


        listPageTab.setTabListener(new MyTabsListener(listPage, 1));
        todoPageTab.setTabListener(new MyTabsListener(todoPage, 2));
        notePageTab.setTabListener(new MyTabsListener(notePage, 3));
        chatPageTab.setTabListener(new MyTabsListener(chatPage, 4));


        actionbar.addTab(listPageTab);
        actionbar.addTab(todoPageTab);
        actionbar.addTab(notePageTab);
        actionbar.addTab(chatPageTab);

        rd[0] = (Redrawable) listPage;
        rd[1] = (Redrawable) todoPage;
        rd[2] = (Redrawable) notePage;
        rd[3] = (Redrawable) chatPage;
        rd[4] = (Redrawable) this;


        //ServerComms.setup("http://172.30.86.177:5000",this.fd,rd);
        //ServerComms.setup("http://10.0.2.2:8301",this.fd,rd);
        ServerComms.setup("http://122.203.53.110:8071", this.fd, rd);
        serverConnector = new ServerComms(getApplicationContext());

        fd.loadFromFile();

        redraw();
    }







    public void showInitialPage() {
        startActivity(new Intent(this,InitialActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Familink", "MainActivity started.");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Familink", "MainActivity resumed.");
        if (serverConnector != null) {
            serverConnector.redrawFragments();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Familink", "MainActivity paused.");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Familink", "MainActivity stopped.");
        fd.saveToFile();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Familink", "MainActivity killed.");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }
    public Menu getMenu() {
        return this.menu;
    }
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //fd.saveToFile();
            startActivity(new Intent(this, Page5Settings.class));
            return true;
        }
        if (id == R.id.global_refresh) {
            Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
            serverConnector.refreshData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void HereComesDeleteTiming() {
        new ServerComms(this).logOut();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = mPref.edit();
        edit.clear();
        if (mPref.getBoolean("familink_ServiceRunning", false)) {
            stopService(new Intent(this, Service_WifiStateChange.class));
        }
        edit.commit();
        return;
    }


    public ServerComms provideServerComms() {
        return this.serverConnector;
    }

    @Override
    public void redraw() {
        setupTab(tab2View, String.valueOf(fd.getToDos().size()),
                getResources().getColor(R.color.text_red),
                (nowid==2)?R.drawable.ic_workingman_orange:R.drawable.ic_workingman_white);
        if (!fd.isRegistered()) showInitialPage();
    }

    public static void setupTab(View v, String message, int numberColor, int imageResource) {
        //TextView tText = (TextView) v.findViewById(R.id.tab_text);
        TextView tNum = (TextView) v.findViewById(R.id.tab_num);
        ImageView imgV=(ImageView) v.findViewById(R.id.tab_icon);

        //tText.setText("");
        if (message == null || message.equals("") || message.equals("0")) {
            tNum.setText("");
        } else {
            tNum.setText("[" + message + "] ");
            tNum.setTextColor(numberColor);
        }
        imgV.setImageResource(imageResource);
    }

    public static void resetupTab(View v, String message, int numberColor, int imageResource) {
        ImageView imgV = (ImageView) v.findViewById(R.id.tab_icon);
        TextView tNum = (TextView) v.findViewById(R.id.tab_num);

        if (message == null || message.equals("") || message.equals("0")) {
            tNum.setText("");
        } else {
            tNum.setText("[" + message + "] ");
            tNum.setTextColor(numberColor);
        }
        imgV.setImageResource(imageResource);
        return;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


    class MyTabsListener implements ActionBar.TabListener {
        public Fragment fragment;
        public int id;

        public MyTabsListener(Fragment fragment, int id) {
            this.fragment = fragment;
            this.id=id;
        }
        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            int position = tab.getPosition();
            viewpager.setCurrentItem(position, true);
            nowid = id;
        }
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    }
}
