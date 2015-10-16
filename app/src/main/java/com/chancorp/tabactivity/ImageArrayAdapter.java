package com.chancorp.tabactivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ImageArrayAdapter extends ArrayAdapter<Integer> {
    private Integer[] images;
    private int width,height;

    public ImageArrayAdapter(Context context, Integer[] images, int width, int height) {
        super(context, android.R.layout.simple_spinner_item, images);
        this.images = images;
        this.width=width;
        this.height=height;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }

    private View getImageForPosition(int position) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(images[position]);
        //imageView.requestLayout();
        //imageView.getLayoutParams().height=height;
        //imageView.getLayoutParams().width=width;
        //imageView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setLayoutParams(new AbsListView.LayoutParams(width, height));
        return imageView;
    }
}