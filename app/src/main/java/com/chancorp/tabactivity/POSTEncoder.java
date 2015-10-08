package com.chancorp.tabactivity;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Chan on 2015-10-08.
 */
public class POSTEncoder {
    ArrayList<String> keys, values;
    public POSTEncoder(){
        keys=new ArrayList<String>();
        values=new ArrayList<String>();
    }

    public void addDataSet(String key, String value){
        keys.add(key);
        values.add(value);
    }
    public String encode(){

        String res=new String();
        for (int i=0;i<keys.size();i++){
            if (i!=0) res=res+"&";
            try {
                res = res + URLEncoder.encode(keys.get(i), "UTF-8") + "=" + URLEncoder.encode(values.get(i), "UTF-8");
            }catch(UnsupportedEncodingException e){
                Log.e("Familink", "What the fuck");
            }

        }
        return res;
    }
}
