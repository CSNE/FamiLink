package com.chancorp.tabactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Chan on 2015-10-14.
 */
public class CredentialsGetter {
    public static final int BASIC=10239;
    public static final int NAME_AND_PHONE=884252;

    EditText usernameInput,passwordInput,nameInput,phoneInput;
    CredReturnListener cr;
    Context c;
    String title,idHint,pwHint;
    int type;

    public void setTitle(String title){
        this.title=title;
    }
    public void setHint(String idHint, String pwHint){
        this.pwHint=pwHint;
        this.idHint=idHint;
    }

    public CredentialsGetter(Context c, int type){
        this.c=c;
        this.type=type;
    }

    public void setOnReturnListener(CredReturnListener cr){
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
        }else{
            Log.e("Familink", "What the fuck");
            view=null;
        }
        builder.setView(view);

        usernameInput=(EditText)view.findViewById(R.id.cred_getter_id);
        passwordInput=(EditText)view.findViewById(R.id.cred_getter_pw);
        if (type==NAME_AND_PHONE){
            nameInput=(EditText)view.findViewById(R.id.cred_getter_name);
            phoneInput=(EditText)view.findViewById(R.id.cred_getter_phone);
        }

        builder.setMessage("Enter")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Credentials cred=new Credentials();
                        cred.setID(usernameInput.getText().toString());
                        cred.setPassword(passwordInput.getText().toString());
                        if (type==NAME_AND_PHONE){
                            cred.setName(nameInput.getText().toString());
                            cred.setPhone(phoneInput.getText().toString());
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
