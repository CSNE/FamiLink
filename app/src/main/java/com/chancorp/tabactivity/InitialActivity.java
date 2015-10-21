package com.chancorp.tabactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Chan on 2015-10-19.
 */
public class InitialActivity extends AppCompatActivity implements View.OnClickListener{
    Button addFamilyBtn,addUserBtn;
    FamilyData fd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fd=ServerComms.getStaticFamilyData();


        setContentView(R.layout.activity_initial);

        addFamilyBtn=(Button)findViewById(R.id.tut_btn_add_family);
        addUserBtn=(Button)findViewById(R.id.tut_btn_add_user);

        addFamilyBtn.setOnClickListener(this);
        addUserBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final Context ctxt=this;
        if (view.getId()==R.id.tut_btn_add_user){
            UserInformationGetter cg=new UserInformationGetter(ctxt, UserInformationGetter.NAME_AND_PHONE);
            cg.setTitle("있는 가족에 로그인");
            cg.setHint("가족 ID","비밀 번호");
            cg.setOnReturnListener(new UserInfoReturnListener() {
                public void onReturn(UserInformation c) {
                    final UserInformation cf=c;
                    Log.d("Familink", "Cred: " + c.getID() + " | " + c.getPassword());
                    fd.setCredentials(c);
                    final ServerComms svc=new ServerComms(ctxt);

                    svc.setDataReturnListener(new DataReturnListener() {
                        @Override
                        public void onReturn(String data) {
                            final ServerComms svc2=new ServerComms(ctxt);
                            svc2.setDataReturnListener(new DataReturnListener() {
                                @Override
                                public void onReturn(String data) {
                                    svc2.clearDataReturnListener();
                                    Log.d("Familink", "DRL debug 593");
                                    close();
                                    return;
                                }
                            });
                            svc2.addMe(cf.getName(), cf.getPhone());
                            svc.clearDataReturnListener();

                        }
                    });
                    svc.getID(c.getID());


                }
            });
            cg.init();
        }else if (view.getId()==R.id.tut_btn_add_family){
            UserInformationGetter cg=new UserInformationGetter(ctxt, UserInformationGetter.NAME_AND_PHONE);
            cg.setTitle("새 가족 생성");
            cg.setHint("새 가족 ID","비밀 번호");
            cg.setOnReturnListener(new UserInfoReturnListener() {
                public void onReturn(final UserInformation c) {
                    Log.d("Familink", "Cred: " + c.getID() + " | " + c.getPassword());
                    fd.setCredentials(c);
                    final ServerComms svc=new ServerComms(ctxt);

                    svc.setDataReturnListener(new DataReturnListener() {
                        @Override
                        public void onReturn(String data) {
                            final ServerComms svc2=new ServerComms(ctxt);
                            svc2.setDataReturnListener(new DataReturnListener() {
                                @Override
                                public void onReturn(String data) {
                                    svc2.clearDataReturnListener();
                                    close();
                                    return;

                                }
                            });
                            svc2.addMe(c.getName(), c.getPhone());
                            svc.clearDataReturnListener();

                        }
                    });
                    svc.addFamily(c);
                }
            });
            cg.init();
        }

    }
    public void close(){
        finish();
        return;
        //TODO this doesn't work.
    }
}
