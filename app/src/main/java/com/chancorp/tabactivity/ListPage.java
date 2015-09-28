package com.chancorp.tabactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//첫번째 탭 fragment. 가족 리스트.
public class ListPage extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, RedrawableFragment{

    TextView tv1;
    ListView lv1;

    FamilyData fd;

    FamilyMemberAdapter adapter;

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

        if (savedInstanceState!=null) this.fd=savedInstanceState.getParcelable("data");

        View rootView = inflater.inflate(R.layout.page_1_list, container, false);

        tv1=(TextView)rootView.findViewById(R.id.page1_TopMsg);
        tv1.setText("Family Member List");




        adapter = new FamilyMemberAdapter(getContext(), R.layout.single_family_member_list_element, fd.getMembersInArray(),(AppCompatActivity)getActivity());


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
    public void onClick(View view) {
        Toast.makeText(getContext(), "Clicked! btn", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getContext(), "Clicked."+i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void redraw() {
        System.out.println("Redraw on ListPage");
        adapter.notifyDataSetChanged();
        lv1.invalidate();

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}