package com.chancorp.tabactivity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//List page의 가족 데이터르르 표시할 때 쓰는 어댑터.
public class FamilyMemberAdapter extends BaseAdapter {

    // LayoutInflater : 레이아웃 xml을 view객체로 만드는 클래스
    int mResource;
    FamilyMember[] familyMembers;
    LayoutInflater minflater;

    AppCompatActivity ac;
    Redrawable parentPage;

    //FamilyMemberAdapter 생성자 정의
    public FamilyMemberAdapter(Context context, int layoutId, FamilyMember[] fm, AppCompatActivity ac, Redrawable rdf){
        mResource=layoutId;
        this.familyMembers=fm;
        minflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.ac=ac;
        this.parentPage=rdf;
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
        Button msgBtn, callBtn, setBtn;
        final FamilyMember currentMember;

        if (convertedView==null){
            view=minflater.inflate(mResource,parent, false);
        }
        else view=convertedView;

        tv1=(TextView) view.findViewById(R.id.memberList_Name);
        tv2=(TextView) view.findViewById(R.id.memberList_Data);

        iv1=(ImageView) view.findViewById(R.id.memberList_Profile);

        msgBtn=(Button) view.findViewById(R.id.memberList_MsgBtn);
        callBtn=(Button) view.findViewById(R.id.memberList_CallBtn);
        setBtn=(Button) view.findViewById(R.id.memberList_SettingBtn);
        //Log.v("Familink", "adapter debug:"+familyMembers[i].getName()+" "+familyMembers[i].getNickname());
        if(familyMembers[i].getNickname()==null) {

            tv1.setText(familyMembers[i].getName());
        }else{
            tv1.setText(familyMembers[i].getNameAndNickname());
        }
        tv2.setText(familyMembers[i].getDataString());

        iv1.setImageResource(familyMembers[i].getAvatarDrawable());

        callBtn.setBackgroundResource(R.drawable.ic_call_black_48dp);
        setBtn.setBackgroundResource(R.drawable.ic_settings_black_48dp);
        msgBtn.setBackgroundResource(R.drawable.ic_message_black_48dp);

        familyMembers[i].setParentPage(parentPage);

        currentMember=familyMembers[i];

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMember.openDialer(ac);
            }
        });
        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMember.openMessenger(ac);
            }
        });
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMember.openSettings(ac);
            }
        });



        return view;
    }
}