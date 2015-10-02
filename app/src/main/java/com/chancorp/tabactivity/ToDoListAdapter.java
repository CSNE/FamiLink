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
public class ToDoListAdapter extends BaseAdapter {

    // LayoutInflater : 레이아웃 xml을 view객체로 만드는 클래스
    int mResource;
    ToDo[] todos;
    LayoutInflater minflater;

    AppCompatActivity ac;

    //FamilyMemberAdapter 생성자 정의
    public ToDoListAdapter(Context context, int layoutId, ToDo[] td, AppCompatActivity ac){
        mResource=layoutId;
        this.todos=td;
        minflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.ac=ac;
    }

    @Override
    public int getCount() {
        return todos.length;
    }

    @Override
    public Object getItem(int i) {
        return todos[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertedView, ViewGroup parent) {

        View view;
        TextView nameV, dueV;
        ImageView imgV;
        Button btn;
        final ToDo currentTD;


        if (convertedView==null){
            view=minflater.inflate(mResource,parent, false);
        }
        else view=convertedView;


        nameV=(TextView) view.findViewById(R.id.todoList_Name);
        dueV=(TextView) view.findViewById(R.id.todoList_Due);

        imgV=(ImageView) view.findViewById(R.id.todoList_Icon);

        btn=(Button) view.findViewById(R.id.todoList_Resolve);

        nameV.setText(todos[i].getTitle());
        dueV.setText(todos[i].getStringDue());

        imgV.setImageResource(todos[i].getIconDrawable());



        currentTD=todos[i];

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "CLICK ON "+currentTD.getTitle(), Toast.LENGTH_SHORT).show();
                currentTD.accept();
            }
        });



        return view;
    }
}