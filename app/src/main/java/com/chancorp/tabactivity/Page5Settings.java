package com.chancorp.tabactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clickedcount = 0;
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
                if(preference.getKey().toString().equals("ListofRouter")) {
                    sendActivity();
                } else if(preference.getKey().toString().equals("DeveloperInformation")) {
                    clickedcount ++;
                    if(clickedcount == 4) {
                        clickedcount = 0;
                        sendActivity_Second();
                    }
                } else if(preference.getKey().toString().equals("familink_LogOut")) {
                    sendActivity_Third();
                }
                return true;
            }
        };
        addPreferencesFromResource(R.xml.pref_settings);
        setOnPreferenceChange(findPreference("familink_ServiceRunning"));
        setOnPreferenceChange(findPreference("familink_Electronics"));
        setOnPreferenceClick(findPreference("ListofRouter"));
        setOnPreferenceClick(findPreference("DeveloperInformation"));
        return;
    }

    private void sendActivity_Third()   { startActivity(new Intent(this, Activity_Log_Out.class));}
    private void sendActivity()   {startActivity(new Intent(this, Activity_List_of_router.class));}
    private void sendActivity_Second() {startActivity(new Intent(this, Activity_EasterEgg.class));}

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

    private void setOnPreferenceClick(Preference mPreference) {
        mPreference.setOnPreferenceClickListener(onPreferenceClickListener);;
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
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}
