package com.chancorp.tabactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Chan on 2015-10-14.
 */
public class CredentialsGetter {
    public final int NEW_USER=523169;
    public final int NEW_FAMILY=98135;

    EditText usernameInput,passwordInput;
    CredReturnListener cr;
    Context c;

    public CredentialsGetter(Context c){
        this.c=c;
    }

    public void setOnReturnListener(CredReturnListener cr){
        this.cr=cr;
    }

    public void init(){

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view=inflater.inflate(R.layout.credientials_getter, null);
        builder.setView(view);

        usernameInput=(EditText)view.findViewById(R.id.cred_getter_id);
        passwordInput=(EditText)view.findViewById(R.id.cred_getter_pw);

        builder.setMessage("Enter")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Credentials cred=new Credentials();
                        cred.setID(usernameInput.getText().toString());
                        cred.setPassword(passwordInput.getText().toString());
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
