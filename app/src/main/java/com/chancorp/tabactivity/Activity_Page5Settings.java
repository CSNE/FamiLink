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
import android.util.Log;
import android.widget.Switch;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

/**
 * Created by Baranium on 2015. 10. 14..
 */
public class Activity_Page5Settings extends PreferenceActivity implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private SharedPreferences mPref;
    private Preference.OnPreferenceChangeListener onPreferenceChangeListener;
    private OnPreferenceClickListener onPreferenceClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPref = PreferenceManager.getDefaultSharedPreferences(this);
        onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringvalue = newValue.toString();
                if(preference instanceof SwitchPreference) {
                    BuildAlertDialog(preference);
                }
                return true;
            }
        };
        onPreferenceClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(preference.getKey().toString().equals("ListofRouter")) {
                    sendActivity();
                }
                return true;
            }
        };
        addPreferencesFromResource(R.xml.pref_settings);
        setOnPreferenceChange(findPreference("ServiceRunning"));
        setOnPreferenceClick(findPreference("ListofRouter"));
    }

    private void sendActivity() {
        startActivity(new Intent(this, Activity_List_of_router.class));
    }

    private void BuildAlertDialog(Preference preference) {
        if(mPref.getBoolean("ServiceRunning",true) == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Caution");
            builder.setMessage("Service is Going to Start. By pressing the switch again, you can turn off switch.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog_ = builder.create();
            dialog_.show();
            startService(new Intent(this, Service_Lockscreen.class));
            preference.setSummary("사용 중");
        } else {
            stopService(new Intent(this, Service_Lockscreen.class));
            preference.setSummary("사용하지 않음");
        }
    }

    private void setOnPreferenceClick(Preference mPreference) {
        mPreference.setOnPreferenceClickListener(onPreferenceClickListener);
    }
    private void setOnPreferenceChange(Preference mPreference) {
        mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
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
