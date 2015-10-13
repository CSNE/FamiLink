package com.chancorp.tabactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

//5번째 탭. 설정.
public class Page5Settings extends Fragment implements RedrawableFragment{

    FamilyData fd;
    Switch ServiceSwitch;
    SharedPreferences setting;
    SharedPreferences.Editor settingeditor;
    final int defValue = 1234567;

    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        System.out.println("Attached.");
        try{
            this.fd=((FamilyDataProvider) a).provideData();

        }catch(ClassCastException e){
            throw new ClassCastException("Can't cast.");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_5_settings, container, false);

        setting = this.getActivity().getSharedPreferences("setting", 0);
        settingeditor = setting.edit(); // Ready to edit sharedpreferences.

        ServiceSwitch = (Switch) rootView.findViewById(R.id.slidingtoggleswitch);
        ServiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if(isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                    getActivity().startService(new Intent(getContext(), Service_Lockscreen.class));
                    settingeditor.putBoolean("ServiceRunning", true);
                } else {
                    getActivity().stopService(new Intent(getContext(), Service_Lockscreen.class));
                    settingeditor.putBoolean("ServiceRunning", false);
                }
                settingeditor.commit();
            }
        });
        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void redraw() {

    }
}