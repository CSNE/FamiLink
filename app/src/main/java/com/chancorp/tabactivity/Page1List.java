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
public class Page1List extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, Redrawable {

    Button addFamilyBtn,addUserBtn;
    LinearLayout notRegisteredMenu;
    ListView lv1;

    FamilyData fd;


    FamilyMemberAdapter adapter;

    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        System.out.println("Attached.");
        this.fd=ServerComms.getStaticFamilyData();

    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState!=null) this.fd=savedInstanceState.getParcelable("data");

        View rootView = inflater.inflate(R.layout.page_1_list, container, false);

        addFamilyBtn=(Button) rootView.findViewById(R.id.page1_btn_add_family);
        addUserBtn=(Button) rootView.findViewById(R.id.page1_btn_add_user);


        notRegisteredMenu=(LinearLayout) rootView.findViewById(R.id.page1_not_registered_menu);
        adapter = new FamilyMemberAdapter(getContext(), R.layout.single_family_member_list_element, fd.getMembersInArray(),(AppCompatActivity)getActivity(),this);

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
            UserInformationGetter cg=new UserInformationGetter(getContext(), UserInformationGetter.NAME_AND_PHONE);
            cg.setTitle("Log in to existing family");
            cg.setHint("Family Name","Password");
            cg.setOnReturnListener(new UserInfoReturnListener() {
                public void onReturn(UserInformation c) {
                    final UserInformation cf=c;
                    Log.d("Familink", "Cred: " + c.getID() + " | " + c.getPassword());
                    fd.setCredentials(c);
                    final ServerComms svc=new ServerComms();
                    svc.getID(c.getID());
                    svc.setDataReturnListener(new DataReturnListener() {
                        @Override
                        public void onReturn(String data) {
                            svc.addMe(cf.getName(), cf.getPhone());
                            svc.clearDataReturnListener();
                        }
                    });


                }
            });
            cg.init();
        }else if (view.getId()==R.id.page1_btn_add_family){
            UserInformationGetter cg=new UserInformationGetter(getContext(), UserInformationGetter.NAME_AND_PHONE);
            cg.setTitle("Make new family");
            cg.setHint("New Family Name","Password");
            cg.setOnReturnListener(new UserInfoReturnListener() {
                public void onReturn(final UserInformation c) {
                    Log.d("Familink", "Cred: " + c.getID() + " | " + c.getPassword());
                    fd.setCredentials(c);
                    final ServerComms svc=new ServerComms();
                    svc.addFamily(c);
                    svc.setDataReturnListener(new DataReturnListener() {
                        @Override
                        public void onReturn(String data) {
                            svc.addMe(c.getName(), c.getPhone());
                            svc.clearDataReturnListener();
                        }
                    });
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
            Log.v("Familink", "IllegalStateException on Page 1>redraw() - Maybe page wasn't visible?");
        }catch(NullPointerException e){
            Log.v("Familink", "NullPointerException on Page 1>redraw() - Maybe page wasn't active?");
        }
    }
}