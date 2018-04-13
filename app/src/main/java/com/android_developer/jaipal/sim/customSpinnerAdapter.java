package com.android_developer.jaipal.sim;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class customSpinnerAdapter extends ArrayAdapter {

    String hint;

    public customSpinnerAdapter(Activity act, int layoutResId, String [] dataArray, String hint) {
        super(act,layoutResId,dataArray);
        this.hint=hint;
    }

    @Override    public View getView(int position, View convertView, ViewGroup parent) {

        View v = null;
        if (position >= getCount()) { //logic to display hint
            v =super.getView(0, convertView, parent);
            ((TextView)v.findViewById(android.R.id.text1)).setText("");
            ((TextView)v.findViewById(android.R.id.text1)).setTextSize( 18 );
            ((TextView)v.findViewById(android.R.id.text1)).setHint(hint); //"Hint to be displayed"
             }
        else            v =super.getView(position, convertView, parent);
            return v;
        }

        @Override    public int getCount() {
            return super.getCount();
        }
}
