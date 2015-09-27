package com.chancorp.tabactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chan on 2015-09-20.
 */

public class ChatPage extends Fragment implements RedrawableFragment{

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
        View rootView = inflater.inflate(R.layout.page_2_todo, container, false);



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
