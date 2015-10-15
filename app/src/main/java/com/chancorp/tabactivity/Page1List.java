package com.chancorp.tabactivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;

//첫번째 탭 fragment. 가족 리스트.
public class Page1List extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, RedrawableFragment{

    Button addFamilyBtn,addUserBtn;
    LinearLayout notRegisteredMenu;
    ListView lv1;

    FamilyData fd;
    ServerComms sc;

    FamilyMemberAdapter adapter;

    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        System.out.println("Attached.");
        try{
            this.fd=((FamilyDataProvider) a).provideData();

        }catch(ClassCastException e){
            Log.e("Familink","Can't cast activity to FamilyDataProvider");
        }
        try{
            this.sc=((ServerCommsProvider) a).provideServerComms();

        }catch(ClassCastException e){
            Log.e("Familink","Can't cast activity to ServerCommsProvider");
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState!=null) this.fd=savedInstanceState.getParcelable("data");

        View rootView = inflater.inflate(R.layout.page_1_list, container, false);

        addFamilyBtn=(Button) rootView.findViewById(R.id.page1_btn_add_family);
        addUserBtn=(Button) rootView.findViewById(R.id.page1_btn_add_user);


        notRegisteredMenu=(LinearLayout) rootView.findViewById(R.id.page1_not_registered_menu);
        adapter = new FamilyMemberAdapter(getContext(), R.layout.single_family_member_list_element, fd.getMembersInArray(),(AppCompatActivity)getActivity());

        if (fd.isRegistered()) notRegisteredMenu.setVisibility(LinearLayout.GONE);

        lv1 = (ListView) rootView.findViewById(R.id.page1_FamilyList);
        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(this);
        addFamilyBtn.setOnClickListener(this);
        addUserBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        Log.d("Familink", "Clicked"+view.getId());
        if (view.getId()==R.id.page1_btn_add_user){
            CredentialsGetter cg=new CredentialsGetter(getContext());
            cg.setTitle("Existing family name");
            cg.setHint("Family Name","Password");
            cg.setOnReturnListener(new CredReturnListener() {
                public void onReturn(Credentials c) {
                    Log.d("Familink", "Cred: " + c.getID() + " | " + c.getPassword());
                    fd.setCredentials(c);
                    sc.getID(c.getID());
                    sc.setDataReturnListener(new DataReturnListener() {
                        @Override
                        public void onReturn(String data) {
                            //TODO Actual user input.
                            sc.addMe("asdf4362", "010-0000-1237");
                            sc.clearDataReturnListener();
                        }
                    });


                }
            });
            cg.init();
        }else if (view.getId()==R.id.page1_btn_add_family){
            CredentialsGetter cg=new CredentialsGetter(getContext());
            cg.setTitle("Make new family");
            cg.setHint("New Family Name","Password");
            cg.setOnReturnListener(new CredReturnListener() {
                public void onReturn(Credentials c) {
                    Log.d("Familink", "Cred: " + c.getID() + " | " + c.getPassword());
                    fd.setCredentials(c);
                    sc.addFamily(c);
                }
            });
            cg.init();
        }

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
            Log.e("Familink", "IllegalStateException on Page 1>redraw() - Maybe page wasn't visible?");
        }catch(NullPointerException e){
            Log.e("Familink", "NullPointerException on Page 1>redraw() - Maybe page wasn't active?");
        }
    }
}