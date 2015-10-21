package com.chancorp.tabactivity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//List page의 가족 데이터르르 표시할 때 쓰는 어댑터.
public class ToDoListAdapter extends BaseAdapter {

    // LayoutInflater : 레이아웃 xml을 view객체로 만드는 클래스
    int mResource;
    FamilyData fd;
    LayoutInflater minflater;


    AppCompatActivity ac;

    //FamilyMemberAdapter 생성자 정의
    public ToDoListAdapter(int layoutId, FamilyData fd, AppCompatActivity ac){
        mResource=layoutId;
        this.fd=fd;
        minflater=(LayoutInflater)ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.ac=ac;

    }

    @Override
    public int getCount() {
        return fd.getToDos().size();
    }

    @Override
    public Object getItem(int i) {
        return fd.getToDoAt(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertedView, ViewGroup parent) {

        View view;
        TextView nameV, dueV, byV, descV;
        ImageView iconV;
        Button btn, resolveBtn;
        final ToDo currentTD;


        if (convertedView==null){
            view=minflater.inflate(mResource,parent, false);
        }
        else view=convertedView;


        nameV=(TextView) view.findViewById(R.id.todoList_Name);
        dueV=(TextView) view.findViewById(R.id.todoList_Due);
        byV=(TextView) view.findViewById(R.id.todoList_made_by);
        descV=(TextView) view.findViewById(R.id.todoList_desc);

        iconV=(ImageView) view.findViewById(R.id.todoList_Icon);

        resolveBtn=(Button) view.findViewById(R.id.todoList_Resolve);
        btn=(Button) view.findViewById(R.id.todoList_Resolve);


        int urg=fd.getToDoAt(i).checkUrgency();
        if (fd.getToDoAt(i).getDueTime()==-1){
            dueV.setTextColor(ac.getResources().getColor(R.color.text_body));
            dueV.setText("-");
        }else {
            if (urg == ToDo.TOO_LATE)
                dueV.setTextColor(ac.getResources().getColor(R.color.text_redder));
            else if (urg == ToDo.URGENT)
                dueV.setTextColor(ac.getResources().getColor(R.color.text_red));
            else if (urg == ToDo.KINDA_URGENT)
                dueV.setTextColor(ac.getResources().getColor(R.color.text_orange));
            else if (urg == ToDo.NOT_URGENT)
                dueV.setTextColor(ac.getResources().getColor(R.color.text_blue));
            dueV.setText(fd.getToDoAt(i).getStringDue(false));
        }


        nameV.setText(fd.getToDoAt(i).getTitle());

        FamilyMember fm=fd.findFamilyMemberByID(fd.getToDoAt(i).getCreator());
        try {
            byV.setText(fm.getNameAndNickname());
        }catch (NullPointerException e){
            Log.d("Familink","Null returned from findFamilyMemberByID. Fallback to default creator name.");
            byV.setText("???");
        }

        descV.setText(fd.getToDoAt(i).getDescription());

        resolveBtn.setBackgroundResource(R.drawable.ic_done_black_48dp);

        iconV.setImageResource(fd.getToDoAt(i).getIconDrawable());



        currentTD=fd.getToDoAt(i);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "CLICK ON "+currentTD.getTitle(), Toast.LENGTH_SHORT).show();
                new ServerComms(ac).deleteToDo(currentTD);
            }
        });



        return view;
    }
}