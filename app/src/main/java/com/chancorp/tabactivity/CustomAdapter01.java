package com.chancorp.tabactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter01 extends BaseAdapter {

    int mResource;
    ArrayList<String> mObject;
    LayoutInflater minflater;

    public CustomAdapter01(Context context,int layoutId, ArrayList<String> object) {
        mResource = layoutId;
        mObject = object;
        minflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        try {
            return mObject.size();
        } catch(NullPointerException e) {return 0;}
    }

    @Override
    public Object getItem(int position) {
        return mObject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView text1, text2;
        ImageView img;

        // 아이템 뷰 생성 및 재사용
        if(convertView == null) {
            view = minflater.inflate(mResource, parent, false);
        }
        else view = convertView;

        //UI 레퍼런스 가져오기
        text1 = (TextView)view.findViewById(R.id.customadapter01text01);
        text2 = (TextView)view.findViewById(R.id.customadapter01text02);
        img = (ImageView)view.findViewById(R.id.customadapter01img01);

        text1.setText((String)getItem(position));
        img.setImageResource(R.mipmap.ic_launcher);

        return view;
    }
}