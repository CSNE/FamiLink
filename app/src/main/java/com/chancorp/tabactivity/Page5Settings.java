package com.chancorp.tabactivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Baranium on 2015. 10. 14..
 */
public class Page5Settings extends PreferenceActivity implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    int clickedcount;
    FamilyData fd;
    private SharedPreferences mPref;
    private Preference.OnPreferenceChangeListener onPreferenceChangeListener;
    private OnPreferenceClickListener onPreferenceClickListener;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("menu","oncreateoptionsmenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_page5settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.page5settingsbackarrow) {
            finish();
        } else if(id == R.id.page5settingsquestion) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("환경 설정 및 다양한 개발자 정보를 확인할 수 있습니다.");
            builder.setTitle("이 페이지는?")
                    .setCancelable(true)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog_ = builder.create();
            dialog_.setCanceledOnTouchOutside(true);
            dialog_.show();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_settings);
        getActionBar().setDisplayShowHomeEnabled(false);

        clickedcount = 0;
        this.fd=ServerComms.getStaticFamilyData();
        this.mPref = PreferenceManager.getDefaultSharedPreferences(this);
        onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(preference instanceof SwitchPreference) {
                    String Tag = preference.getKey().toString();
                    if(Tag.equals("familink_ServiceRunning")) BuildAlertDialog_familink_ServiceRunning(preference);
                    else if(Tag.equals("familink_Electronics"))  BuildAlertDialog_Electornics(preference);
                }
                return true;
            }
        };
        onPreferenceClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String comparison = preference.getKey().toString();
                if(comparison.equals("ListofRouter")) {
                    sendActivity();
                } else if(comparison.equals("DeveloperInformation")) {
                    clickedcount ++;
                    if(clickedcount == 4) {
                        clickedcount = 0;
                        sendActivity_Second();
                    }
                } else if(comparison.equals("familink_Logout")) {
                    logoutProcess();
                } else if(comparison.equals("familink_DelAccount")) {
                    delAccountProcess();
                } else if(comparison.equals("familink_getAccount")) {
                    sendActivity_Third();
                }
                return true;
            }
        };
        setOnPreferenceChange(findPreference("familink_ServiceRunning"));
        setOnPreferenceChange(findPreference("familink_Electronics"));
        setOnPreferenceClick(findPreference("familink_Logout"));
        setOnPreferenceClick(findPreference("familink_DelAccount"));
        setOnPreferenceClick(findPreference("ListofRouter"));
        setOnPreferenceClick(findPreference("DeveloperInformation"));
        setOnPreferenceClick(findPreference("familink_getAccount"));
        return;
    }


    private void logoutProcess() {
        BuildAlertDialog_Logout_or_DeleteAccount(true);
    }

    private void delAccountProcess(){
        BuildAlertDialog_Logout_or_DeleteAccount(false);
    }

    private void sendActivity()   {startActivity(new Intent(this, Activity_List_of_router.class));}

    private void sendActivity_Second() {startActivity(new Intent(this, Activity_EasterEgg.class));}

    private void sendActivity_Third() {startActivity(new Intent(this, Activity_getAccount.class));}

    private void BuildAlertDialog_familink_ServiceRunning(Preference preference) {
        if(mPref.getBoolean(preference.getKey(),true) == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("알림");
            builder.setMessage("앱의 기능을 위해,\n본 서비스는 항상 켜두는 걸 권장합니다.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog_ = builder.create();
            dialog_.show();
            startService(new Intent(this, Service_WifiStateChange.class));
            preference.setSummary("사용 중");
        } else {
            stopService(new Intent(this, Service_WifiStateChange.class));
            preference.setSummary("사용하지 않음");
        }
        return;
    }

    private void BuildAlertDialog_Electornics(Preference preference) {
        if(mPref.getBoolean(preference.getKey(),true) == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("이 기능은?")
                    .setMessage("집을 마지막으로 나갈 때, 알람을 전달합니다.\n실시간 위치제공 서비스가 꺼져 있다면, 작동하지 않습니다.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog_ = builder.create();
            dialog_.show();
            preference.setSummary("사용 중");
        } else {
            preference.setSummary("사용하지 않음");
        }
        return;
    }

    private void BuildAlertDialog_Logout_or_DeleteAccount(boolean isLogout) {
        final boolean isLogout_ = isLogout;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(isLogout_) builder.setMessage("확인 버튼을 누르시면, 가족을 나가게 됩니다.\n 가족을 나간 후 다시 가입할 수 있습니다.");
        else builder.setMessage("확인 버튼을 누르시면, 가족을 나가게 됩니다. \n 서버에 저장된 나에 대한 정보는 완전히 삭제됩니다.");
        builder.setTitle("경고")
                .setCancelable(true)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (isLogout_) HereComesDeleteTiming(true);
                        else HereComesDeleteTiming(false);
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog_ = builder.create();
        dialog_.setCanceledOnTouchOutside(true);
        dialog_.show();
    }

    private void HereComesDeleteTiming(boolean isLogout) {
        if(isLogout) new ServerComms(this).logOut();
        else new ServerComms(this).deleteMe();
        SharedPreferences.Editor edit = mPref.edit();
        edit.clear();
        if(mPref.getBoolean("familink_ServiceRunning",false)) {
            stopService(new Intent(this, Service_WifiStateChange.class));
        }
        edit.commit();
        return;
    }


    // lister attaching methods.
    private void setOnPreferenceClick(Preference mPreference) {
        mPreference.setOnPreferenceClickListener(onPreferenceClickListener);
    }

    private void setOnPreferenceChange(Preference mPreference) {
        mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        if(mPreference.getKey().toString().equals("familink_ServiceRunning")) {
            if(mPref.getBoolean("familink_ServiceRunning",true) == false) {
                mPreference.setSummary("사용 안함");
            } else {
                mPreference.setSummary("사용 중");
            }
        } else if(mPreference.getKey().toString().equals("familink_Electronics")) {
            if(mPref.getBoolean("familink_Electronics", true) == false) {
                mPreference.setSummary("사용 안함");
            } else {
                mPreference.setSummary("사용 중");
            }
        }
    }


    // not using these listener methods.
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}
