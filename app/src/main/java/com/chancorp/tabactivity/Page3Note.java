package com.chancorp.tabactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.PrintWriter;
import java.io.StringWriter;

//3번째 탭. 가족 노트.
public class Page3Note extends Fragment implements RedrawableFragment{

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
        View rootView = inflater.inflate(R.layout.page_3_notes, container, false);



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
            Log.v("Familink", "IllegalStateException on Page 3>redraw() - Maybe page wasn't visible?");
        }catch(NullPointerException e){
            Log.v("Familink", "NullPointerException on Page 3>redraw() - Maybe page wasn't active?");
        }
    }
}