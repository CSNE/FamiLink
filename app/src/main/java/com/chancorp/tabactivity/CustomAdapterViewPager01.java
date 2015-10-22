package com.chancorp.tabactivity;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomAdapterViewPager01 extends PagerAdapter {

    LayoutInflater inflater;
    final int PAGE_NUM = 4;

    public CustomAdapterViewPager01(LayoutInflater inflater) {
        // TODO Auto-generated constructor stub
        this.inflater=inflater;
    }

    //PagerAdapter가 가지고 잇는 View의 개수를 리턴
    //Tab에 따른 View를 보여줘야 하므로 Tab의 개수인 3을 리턴..
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return PAGE_NUM; //보여줄 View의 개수 리턴(Tab이 3개라서 3을 리턴)
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=null;
        //새로운 View 객체를 Layoutinflater를 이용해서 생성
        //position마다 다른 View를 생성
        //만들어질 View의 설계는 res폴더>>layout폴더안에 3개의 레이아웃 파일 사용
        switch( position ){
            case 0: //첫번째 Tab을 선택했을때 보여질 뷰
                view= inflater.inflate(R.layout.page_1_list, null);
                break;
            case 1: //두번째 Tab을 선택했을때 보여질 뷰
                view= inflater.inflate(R.layout.page_2_todo, null);
                break;
            case 2: //세번째 Tab을 선택했을때 보여질 뷰
                view= inflater.inflate(R.layout.page_3_notes, null);
                break;
            case 3:
                view = inflater.inflate(R.layout.page_4_chat, null);
                break;
        }
        if(view != null) container.addView(view);
        return view;
    }

    //화면에 보이지 않은 View는파쾨를 해서 메모리를 관리함.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : 파괴될 View의 인덱스(가장 처음부터 0,1,2,3...)
    //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v==obj;
    }

}
