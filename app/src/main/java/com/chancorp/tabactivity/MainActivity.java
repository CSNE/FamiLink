/*
COPYRIGHT CONTROL

Family Logos By Snehal Patil (CC-BY-3.0-US)
Material Design Icons by Google (CC-BY-4.0)
StringUtils class by Nick Frolov
ServerComms POST code from http://www.xyzws.com/javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139

*/

package com.chancorp.tabactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


//프로그램 시작 시 보여지는 Activity..

public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener,Redrawable {

    //TODO images instead of avatars?
    //TODO Diary page?(instead of notes)
    //TODO Delete Family
    //TODO Delete Person


    FamilyData fd;
    ServerComms serverConnector;
    Redrawable[] rd =new Redrawable[5];
    View tab1View,tab2View,tab3View,tab4View;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Familink", "-----------New Session started.-----------");

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);

        fd=new FamilyData(getApplicationContext());

        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();

        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // 2. 탭 생성함 create new tabs and and set up the titles of the tabs
        ActionBar.Tab listPageTab = actionbar.newTab().setText("List");
        ActionBar.Tab todoPageTab = actionbar.newTab().setText("To Do");
        ActionBar.Tab notePageTab = actionbar.newTab().setText("Note");
        ActionBar.Tab chatPageTab = actionbar.newTab().setText("Chat");


        tab1View=getLayoutInflater().inflate(R.layout.tab_layout,null);
        listPageTab.setCustomView(tab1View);
        setupTab(tab1View, "List", null, 0);

        tab2View=getLayoutInflater().inflate(R.layout.tab_layout,null);
        todoPageTab.setCustomView(tab2View);
        setupTab(tab2View, "ToDo", null, 0);

        tab3View=getLayoutInflater().inflate(R.layout.tab_layout,null);
        notePageTab.setCustomView(tab3View);
        setupTab(tab3View, "Note", null, 0);

        tab4View=getLayoutInflater().inflate(R.layout.tab_layout,null);
        chatPageTab.setCustomView(tab4View);
        setupTab(tab4View,"Chat",null,0);


        Fragment listPage = (Fragment) new Page1List();
        Fragment todoPage = (Fragment) new Page2ToDo();
        Fragment notePage = (Fragment) new Page3Note();
        Fragment chatPage = (Fragment) new Page4Chat();


        listPageTab.setTabListener(new MyTabsListener(listPage, this));
        todoPageTab.setTabListener(new MyTabsListener(todoPage,this));
        notePageTab.setTabListener(new MyTabsListener(notePage,this));
        chatPageTab.setTabListener(new MyTabsListener(chatPage, this));


        actionbar.addTab(listPageTab);
        actionbar.addTab(todoPageTab);
        actionbar.addTab(notePageTab);
        actionbar.addTab(chatPageTab);

        rd[0]=(Redrawable)listPage;
        rd[1]=(Redrawable)todoPage;
        rd[2]=(Redrawable)notePage;
        rd[3]=(Redrawable)chatPage;
        rd[4]=(Redrawable)this;




        //ServerComms.setup("http://172.30.86.177:5000",this.fd,rd);
        //ServerComms.setup("http://10.0.2.2:8301",this.fd,rd);
        ServerComms.setup("http://122.203.53.110:8071",this.fd, rd);
        serverConnector = new ServerComms(getApplicationContext());

        fd.loadFromFile();

        redraw();
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("Familink", "MainActivity started.");
        if (serverConnector!=null) serverConnector.redrawFragments();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("Familink", "MainActivity resumed.");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("Familink", "MainActivity paused.");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("Familink", "MainActivity stopped.");
        fd.saveToFile();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("Familink", "MainActivity killed.");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        this.menu=menu;

        return true;

    }

    public Menu getMenu(){
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
        if (id == R.id.debug_del_save) {
            BuildAlertDialog_Logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void HereComesDeleteTiming() {
        new ServerComms(this).logOut();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = mPref.edit();
        edit.clear();
        if(mPref.getBoolean("familink_ServiceRunning",false)) {
            stopService(new Intent(this, Service_WifiStateChange.class));
        }
        edit.commit();
        return;
    }

    private void BuildAlertDialog_Logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경고")
                .setMessage("확인 버튼을 누르시면, 가족을 나가게 됩니다.\n 가족을 나간 후 다시 가입할 수 있습니다.")
                .setCancelable(true)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        HereComesDeleteTiming();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog_ = builder.create();
        dialog_.setCanceledOnTouchOutside(true);
        dialog_.show();
    }

    public ServerComms provideServerComms(){
        return this.serverConnector;
    }

    @Override
    public void redraw() {
        setupTab(tab2View,"To Do",Integer.toString(fd.getToDos().size()),getResources().getColor(R.color.text_orange));
    }

    public static void setupTab(View v, String name, String message, int color){
        TextView tText=(TextView)v.findViewById(R.id.tab_text);
        TextView tNum=(TextView)v.findViewById(R.id.tab_num);
        tText.setText(name);
        if (message==null || message.equals("") || message.equals("0")) {
            tNum.setText("");
        }else{
            tNum.setText("["+message+"] ");
            tNum.setTextColor(color);
        }
    }


    class MyTabsListener implements ActionBar.TabListener {
    public Fragment fragment;
    public Context context;

    public MyTabsListener(Fragment fragment, Context context) {
        this.fragment = fragment;
        this.context = context;

    }


    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        ft.replace(R.id.tab_area, fragment);
    }



    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        ft.remove(fragment);
    }

}


}
