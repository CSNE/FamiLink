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
import android.widget.Toast;

//List page의 가족 데이터르르 표시할 때 쓰는 어댑터.
public class NoteListAdapter extends BaseAdapter {

    // LayoutInflater : 레이아웃 xml을 view객체로 만드는 클래스
    int mResource;
    FamilyData fd;
    LayoutInflater minflater;


    AppCompatActivity ac;

    //FamilyMemberAdapter 생성자 정의
    public NoteListAdapter(int layoutId, FamilyData fd, AppCompatActivity ac){
        mResource=layoutId;
        this.fd=fd;
        minflater=(LayoutInflater)ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.ac=ac;

    }

    @Override
    public int getCount() {
        return fd.getNotes().size();
    }

    @Override
    public Object getItem(int i) {
        return fd.getNoteAt(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertedView, ViewGroup parent) {

        View view;
        TextView titleV, bodyV;
        Button delBtn;
        final Note currentNote;


        if (convertedView==null){
            view=minflater.inflate(mResource,parent, false);
        }
        else view=convertedView;


        titleV=(TextView) view.findViewById(R.id.notelist_title);
        bodyV=(TextView) view.findViewById(R.id.notelist_body);

        delBtn=(Button) view.findViewById(R.id.notelist_delete);

        titleV.setText(fd.getNoteAt(i).getTitle());
        bodyV.setText(fd.getNoteAt(i).getBody());


        delBtn.setBackgroundResource(R.drawable.ic_close_black_48dp);

        currentNote=fd.getNoteAt(i);

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "CLICK ON "+currentNote.getTitle(), Toast.LENGTH_SHORT).show();
                new ServerComms(ac).deleteNote(currentNote);
            }
        });



        return view;
    }
}