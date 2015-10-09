package com.chancorp.tabactivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

//프로그램 시작 시 보여지는 Activity..

public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener,FamilyDataProvider {


    FamilyData fd;
    ServerComms serverConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);

        fd=new FamilyData();
        fd.addMembers(new FamilyMember("Father", 0, true, R.drawable.capture, "010-4944-7734"));
        fd.addMembers(new FamilyMember("Mother", 1, false, R.drawable.capture, "0"));
        fd.addMembers(new FamilyMember("Sister", 2, true, R.drawable.capture, "0"));
        fd.addToDo(new ToDo(1,"Test TODO","test.",1443769403L,R.drawable.capture));

        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();

        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // 2. 탭 생성함 create new tabs and and set up the titles of the tabs
        ActionBar.Tab listPageTab = actionbar.newTab().setText("List");
        ActionBar.Tab todoPageTab = actionbar.newTab().setText("To Do");
        ActionBar.Tab notePageTab = actionbar.newTab().setText("Notes");
        ActionBar.Tab chatPageTab = actionbar.newTab().setText("Chat");
        ActionBar.Tab settingsPageTab = actionbar.newTab().setText("Settings");

        Fragment listPage = (Fragment) new ListPage();
        Fragment todoPage = (Fragment) new ToDoPage();
        Fragment notePage = (Fragment) new NotePage();
        Fragment chatPage = (Fragment) new ChatPage();
        Fragment settingsPage = (Fragment) new SettingsPage();

        listPageTab.setTabListener(new MyTabsListener(listPage, this));
        todoPageTab.setTabListener(new MyTabsListener(todoPage,this));
        notePageTab.setTabListener(new MyTabsListener(notePage,this));
        chatPageTab.setTabListener(new MyTabsListener(chatPage,this));
        settingsPageTab.setTabListener(new MyTabsListener(settingsPage, this));

        actionbar.addTab(listPageTab);
        actionbar.addTab(todoPageTab);
        actionbar.addTab(notePageTab);
        actionbar.addTab(chatPageTab);
        actionbar.addTab(settingsPageTab);

        RedrawableFragment[] rdfs={(RedrawableFragment)listPage,
                (RedrawableFragment)todoPage,
                (RedrawableFragment)notePage,
                (RedrawableFragment)chatPage,
                (RedrawableFragment)settingsPage};



        //ServerComms.setup("http://172.30.86.177:5000",this.fd,rdfs);
        ServerComms.setup("http://10.0.2.2:8301",this.fd,rdfs);
        ServerComms.setup("http://122.203.53.110:8071",this.fd,rdfs);
        serverConnector = new ServerComms();

        startService(new Intent(this, Service_Lockscreen.class));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem mi = menu.findItem(R.id.debug_1);
        mi.setOnMenuItemClickListener(this);

        return true;

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
            return true;
        }
        if (id == R.id.global_refresh) {
            Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
            serverConnector.retriveData();
            return true;
        }
        if (id == R.id.debug_1) {
            Toast.makeText(this, "SEND TO JISUNG'S PART", Toast.LENGTH_SHORT).show();
            Intent itt=new Intent(this,Activity_Module1.class);
            startActivity(itt);
        }
        if (id == R.id.debug_2) {
            serverConnector.test();
        }
        if (id == R.id.debug_in) {
            serverConnector.gotInside();
        }
        if (id == R.id.debug_out) {
            serverConnector.gotOutside();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public FamilyData provideData() {
        return this.fd;
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
