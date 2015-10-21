package com.chancorp.tabactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Chan on 2015-10-14.
 */
public class UserInformationGetter {
    public static final int BASIC=10239;
    public static final int NAME_AND_PHONE=884252;
    public static final int NICKNAME_AND_AVATAR=129744;
    public static final int TASK=385791;
    public static final int NOTE=189516;

    EditText usernameInput,passwordInput,nameInput,phoneInput, nickInput, timeInput, taskNameInput, taskDescInput, noteTitleInput, noteBodyInput;
    Spinner avatarSpinner;
    UserInfoReturnListener cr;
    Context c;
    String title, hint1, hint2;
    int type;

    public void setTitle(String title){
        this.title=title;
    }
    public void setHint(String hint){
        this.hint1 =hint;

    }
    public void setHint(String idHint, String pwHint){
        this.hint2 =pwHint;
        this.hint1 =idHint;
    }

    public UserInformationGetter(Context c, int type){
        this.c=c;
        this.type=type;
    }

    public void setOnReturnListener(UserInfoReturnListener cr){
        this.cr=cr;
    }

    public void init(){

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View view;
        if (type==BASIC) {
            view=inflater.inflate(R.layout.credientials_getter_basic, null);

        }else if (type==NAME_AND_PHONE){
            view=inflater.inflate(R.layout.credientials_getter_name_and_phone, null);
        }else if(type==NICKNAME_AND_AVATAR){
            view=inflater.inflate(R.layout.credientials_getter_nickname_and_avatar, null);
        }else if(type==TASK){
            view=inflater.inflate(R.layout.credientials_getter_task, null);
        }else if(type==NOTE){
            view=inflater.inflate(R.layout.credientials_getter_note, null);
        }
        else{
            view=null;
        }

        builder.setView(view);

        if (type==BASIC||type==NAME_AND_PHONE) {
            usernameInput = (EditText) view.findViewById(R.id.cred_getter_id);
            passwordInput = (EditText) view.findViewById(R.id.cred_getter_pw);
        }
        if (type==NAME_AND_PHONE){
            nameInput=(EditText)view.findViewById(R.id.cred_getter_name);
            phoneInput=(EditText)view.findViewById(R.id.cred_getter_phone);
        }
        if(type==NICKNAME_AND_AVATAR){
            nickInput=(EditText)view.findViewById(R.id.cred_getter_nickname);
            avatarSpinner=(Spinner)view.findViewById(R.id.cred_getter_avatar_spinner);
            Integer[] picArray=new Integer[FamilyMember.avatarIdToDrawable.length];
            for (int i=0;i<FamilyMember.avatarIdToDrawable.length;i++){
                picArray[i]=FamilyMember.avatarIdToDrawable[i];
            }
            SizeConverter szConv=new SizeConverter(c);
            ImageArrayAdapter adapter = new ImageArrayAdapter(c, picArray,szConv.dpToPixels(100),szConv.dpToPixels(100));
            avatarSpinner.setAdapter(adapter);
        }
        if (type==TASK){
            timeInput=(EditText)view.findViewById(R.id.cred_getter_task_time);
            taskNameInput=(EditText)view.findViewById(R.id.cred_getter_task_name);
            taskDescInput=(EditText)view.findViewById(R.id.cred_getter_task_desc);
        }
        if(type==NOTE){
            noteBodyInput=(EditText)view.findViewById(R.id.cred_getter_note_body);
            noteTitleInput=(EditText)view.findViewById(R.id.cred_getter_note_title);
        }

        builder.setMessage(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        UserInformation cred = new UserInformation();
                        if(type==NAME_AND_PHONE||type==BASIC) {
                            cred.setID(usernameInput.getText().toString());
                            cred.setPassword(passwordInput.getText().toString());
                        }
                        if (type==NAME_AND_PHONE){
                            cred.setName(nameInput.getText().toString());
                            cred.setPhone(phoneInput.getText().toString());
                        }
                        if(type==NICKNAME_AND_AVATAR){
                            cred.setNickname(nickInput.getText().toString());
                            cred.setAvatar(avatarSpinner.getSelectedItemPosition());
                        }
                        if(type==TASK){
                            cred.setTaskName(taskNameInput.getText().toString());
                            cred.setTaskTime(timeInput.getText().toString());
                            cred.setTaskDesc(taskDescInput.getText().toString());
                        }
                        if(type==NOTE){
                            cred.setNoteBody(noteBodyInput.getText().toString());
                            cred.setNoteTitle(noteTitleInput.getText().toString());
                        }
                        cr.onReturn(cred);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        builder.show();

    }

}
