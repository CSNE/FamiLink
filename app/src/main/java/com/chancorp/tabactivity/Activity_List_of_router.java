package com.chancorp.tabactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_List_of_router extends Activity implements View.OnClickListener {
    Button btn;
    ListView listview;
    ArrayList<String> arraylist;
    CustomAdapter01 adapter = null;
    final int REQUEST_CODE = 100131;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listofrouter);

        listview = (ListView) findViewById(R.id.listofrouterlist);
        arraylist = new ArrayList<String>();
        adapter = new CustomAdapter01(this, R.layout.customadapter01design, arraylist);
        listview.setAdapter(adapter);
        btn = (Button) findViewById(R.id.addrouterbtn);
        btn.setOnClickListener(this);
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
                arraylist.add(data.getStringExtra("new_address")+"   ("+data.getStringExtra("new_address_date")+")");
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void datastechanged(int position) {
        arraylist.remove(position);
        adapter.notifyDataSetChanged();
    }













    class CustomAdapter01 extends BaseAdapter implements View.OnClickListener {

        int mResource;
        ArrayList<String> mObject;
        LayoutInflater minflater;
        private Context context;

        public CustomAdapter01(Context context,int layoutId, ArrayList<String> object) {
            this.context = context;
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
        public View getView(int position, View convertview, ViewGroup parent) {
            TextView text1;
            ImageView img1, img2;
            final int position_ = position;
            final Context context = parent.getContext();

            // 아이템 뷰 생성 및 재사용
            if(convertview == null) {
                convertview = minflater.inflate(mResource, parent, false);

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
                            .setCancelable(true)
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

            }
            return convertview;
        }
        @Override
        public void onClick(View v) {}
    }

}
