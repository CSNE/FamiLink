package com.chancorp.tabactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Activity_List_of_router extends Activity implements View.OnClickListener {
    Button btn;
    ListView listview;
    ArrayList<String> arraylist;
    CustomAdapter01 adapter = null;
    final int REQUEST_CODE = 100131, MaxRouterSize = 10;
    SharedPreferences mRef;
    SharedPreferences.Editor mRefEdit;

    /*
    when getting Router Size from SharedPreferences : mRef.getInt("RouterSz",0);
    RouterTag = "Router"+String.ValueOf(i);
    when getting Router name from SharedPreferences : mRef.getString(RouterTag+"_First");
    when getting Router macAddr from SharedPreferences : mRef.getString("RouterTag"+"_Second");
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listofrouter);

        listview = (ListView) findViewById(R.id.listofrouterlist);
        FillArrayListAsPreferences();
        adapter = new CustomAdapter01(this, R.layout.customadapter01design, arraylist);
        listview.setAdapter(adapter);
        btn = (Button) findViewById(R.id.addrouterbtn);
        btn.setOnClickListener(this);
    }

    private void FillArrayListAsPreferences() {
        mRef = getSharedPreferences("RouterList",0);
        mRefEdit = mRef.edit();
        arraylist = new ArrayList<String>();
        int RouterSz = mRef.getInt("RouterSz",0);
        String RouterTag = new String();
        RouterInformation RouterList[] = new RouterInformation[10];
        for(int i=0;i<RouterSz;i++) {
            RouterTag = "Router"+String.valueOf(i);
            RouterList[i] = new RouterInformation();
            RouterList[i].name = mRef.getString(RouterTag+"_First","");
            RouterList[i].macAddr = mRef.getString(RouterTag+"_Second","");
            arraylist.add(RouterList[i].name+"   ("+RouterList[i].macAddr+")");
        }
        return;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addrouterbtn) {
            startActivityForResult(new Intent(this, Activity_add_router.class), REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                boolean exists = false;
                String newstring = data.getStringExtra("new_address")+"   ("+data.getStringExtra("new_address_date")+")";
                for(String arraydata : arraylist) {
                    if(arraydata.equals(newstring)) {
                        exists = true;
                        break;
                    }
                }
                if(!exists) {
                    arraylist.add(newstring);
                    adapter.notifyDataSetChanged();
                    int Resizedsz = mRef.getInt("RouterSz",0);
                    String RouterTag = "Router"+String.valueOf(Resizedsz);
                    mRefEdit.putInt("RouterSz",Resizedsz+1);
                    mRefEdit.putString("Router"+String.valueOf(Resizedsz)+"_First",data.getStringExtra("new_address"));
                    mRefEdit.putString("Router"+String.valueOf(Resizedsz)+"_Second",data.getStringExtra("neww_address_date"));
                    mRefEdit.commit();
                } else {
                    Toast.makeText(this, "This Wi-fi already exists in a list!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void datastechanged(int position) {
        arraylist.remove(position);
        String RouterTag = "Router"+String.valueOf(position);
        mRefEdit.remove(RouterTag+"_First");
        mRefEdit.remove(RouterTag+"_Second");
        int Resizedsz = mRef.getInt("RouterSz",0);
        mRefEdit.putInt("RouterSz",Resizedsz-1);
        mRefEdit.commit();
        adapter.notifyDataSetChanged();
        return;
    }













    class CustomAdapter01 extends BaseAdapter implements View.OnClickListener {

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

        private class CustomHolder {
            TextView mtextview;
            ImageView imgview01, imgview02;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup parent) {
            final int position_ = position;
            final Context context = parent.getContext();
            CustomHolder holder = null;
            TextView text1 = null;
            ImageView img1 = null, img2 = null;

            // 아이템 뷰 생성 및 재사용
            if(convertview == null) {
                convertview = minflater.inflate(mResource, parent, false);
                holder = new CustomHolder();
                holder.mtextview = text1;
                holder.imgview01 = img1;
                holder.imgview02 = img2;
                convertview.setTag(holder);
            } else {
                holder = (CustomHolder) convertview.getTag();
                holder.mtextview = text1;
                holder.imgview01 = img1;
                holder.imgview02 = img2;
            }

            //UI 레퍼런스 가져오기
            text1 = (TextView) convertview.findViewById(R.id.customadapter01text01);
            img1 = (ImageView) convertview.findViewById(R.id.customadapter01img01);
            img2 = (ImageView) convertview.findViewById(R.id.customadapter01img02);
            text1.setText((String) getItem(position));
            img1.setImageResource(R.mipmap.ic_launcher);

            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.delete);
            Bitmap resized = Bitmap.createScaledBitmap(bmp, 150, 150, true);
            img2.setImageBitmap(resized);
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public  void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Caution");
                    Log.d("Activity_List_of_router", String.valueOf(position_));
                            builder.setMessage("Erase wi-fi " + arraylist.get(position_) + "?")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    datastechanged(position_);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog dialog_ = builder.create();
                    dialog_.show();
                }
            });
            return convertview;
        }
        @Override
        public void onClick(View v) {}
    }

}
