package com.chancorp.tabactivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.view.PagerTabStrip;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        setContentView(R.layout.activity_main);

        //System.out.println("Start");


        //System.out.println("End");


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
        serverConnector = new ServerComms("http://10.0.2.2:8301",this.fd,rdfs);

        serverConnector.sendData();


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
            serverConnector.retrive_data();
            return true;
        }
        if (id == R.id.debug_1) {
            Toast.makeText(this, "SEND TO JISUNG'S PART", Toast.LENGTH_SHORT).show();
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

	/* FragmentTransaction : 프레그먼트를 추가, 교체, 삭제하는 등의 작업 처리하기 위한 클래스
	 *
	 */

    // Tab키 다시 눌렀을때
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {


    }


    // Tab 키 눌렀을때
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






