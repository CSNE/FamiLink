package com.chancorp.tabactivity;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Chan on 2015-09-20.
 */
public class FamilyMemberAdapter extends BaseAdapter {

    // LayoutInflater : 레이아웃 xml을 view객체로 만드는 클래스
    int mResource;
    FamilyMember[] familyMembers;
    LayoutInflater minflater;

    AppCompatActivity ac;

    //FamilyMemberAdapter 생성자 정의
    public FamilyMemberAdapter(Context context, int layoutId, FamilyMember[] fm, AppCompatActivity ac){
        mResource=layoutId;
        this.familyMembers=fm;
        minflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.ac=ac;

        //setareAllItemsEnabled();
        //isEnabled(true);

    }

    @Override
    public int getCount() {
        return familyMembers.length;
    }

    @Override
    public Object getItem(int i) {
        return familyMembers[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertedView, ViewGroup parent) {

        View view;
        TextView tv1, tv2;
        ImageView iv1;
        Button btn;
        final FamilyMember currentMember;

        if (convertedView==null){
            view=minflater.inflate(mResource,parent, false);
        }
        else view=convertedView;

        tv1=(TextView) view.findViewById(R.id.memberList_Name);
        tv2=(TextView) view.findViewById(R.id.memberList_Data);

        iv1=(ImageView) view.findViewById(R.id.memberList_Profile);

        btn=(Button) view.findViewById(R.id.memberList_MsgBtn);

        tv1.setText(familyMembers[i].getName());
        tv2.setText(familyMembers[i].getDataString());

        iv1.setImageResource(familyMembers[i].getAvatarDrawable());

        btn.setBackgroundResource(android.R.drawable.sym_action_chat);

        currentMember=familyMembers[i];

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "CLICK ON"+currentMember.getName(), Toast.LENGTH_SHORT).show();
                currentMember.openMessenger(ac);
            }
        });



        return view;
    }
}