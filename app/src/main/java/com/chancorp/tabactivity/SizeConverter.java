package com.chancorp.tabactivity;

import android.content.Context;
import android.util.Size;

/**
 * Created by Chan on 2015-10-16.
 */
public class SizeConverter {
    Context c;
    public SizeConverter(Context c){
        this.c=c;
    }
    int dpToPixels(int dp){
        float density=c.getResources().getDisplayMetrics().density;
        return (int)(dp*density);
    }
}
