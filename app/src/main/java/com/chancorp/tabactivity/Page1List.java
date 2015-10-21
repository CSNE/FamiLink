package com.chancorp.tabactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

//첫번째 탭 fragment. 가족 리스트.
public class Page1List extends Fragment implements AdapterView.OnItemClickListener, Redrawable {

    ListView lv1;

    FamilyData fd;

    FamilyMemberAdapter adapter;

    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        this.fd=ServerComms.getStaticFamilyData();

    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState!=null) this.fd=savedInstanceState.getParcelable("data");

        View rootView = inflater.inflate(R.layout.page_1_list, container, false);





        adapter = new FamilyMemberAdapter(getContext(), R.layout.single_family_member_list_element, fd.getMembersInArray(),(AppCompatActivity)getActivity(),this);



        lv1 = (ListView) rootView.findViewById(R.id.page1_FamilyList);
        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(this);


        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("Familink","Clicked item "+i);
    }

    @Override
    public void redraw() {
        System.out.println("Redraw on Page1List");
        adapter.notifyDataSetChanged();
        lv1.invalidate();

        try {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }catch(IllegalStateException e){
            Log.v("Familink", "IllegalStateException on Page 1>redraw() - Maybe page wasn't visible?");
        }catch(NullPointerException e){
            Log.v("Familink", "NullPointerException on Page 1>redraw() - Maybe page wasn't active?");
        }
    }
}