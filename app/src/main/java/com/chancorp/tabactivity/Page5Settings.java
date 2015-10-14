package com.chancorp.tabactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;

//5번째 탭. 설정.
public class Page5Settings extends Fragment implements RedrawableFragment{

    FamilyData fd;
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
        startActivity(new Intent(this.getActivity(), Activity_Page5Settings.class));
        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void redraw() {
        try {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }catch(IllegalStateException e){
            Log.e("Familink", "IllegalStateException on Page5Settings>redraw() - Maybe page wasn't visible?");
        }catch(NullPointerException e){
            Log.e("Familink", "NullPointerException on Page5Settings>redraw() - Maybe page wasn't active?");
        }
    }
}