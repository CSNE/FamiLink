package com.chancorp.tabactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//4번째 탭. 채팅.
public class Page4Chat extends Fragment implements Redrawable {

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
        View rootView = inflater.inflate(R.layout.page_4_chat, container, false);



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
            Log.v("Familink", "IllegalStateException on Page 4>redraw() - Maybe page wasn't visible?");
        }catch(NullPointerException e){
            Log.v("Familink", "NullPointerException on Page 4>redraw() - Maybe page wasn't active?");
        }
    }
}
