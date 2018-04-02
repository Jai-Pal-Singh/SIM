package com.android_developer.jaipal.sim;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by hp on 2018-04-01.
 */

class MainGearsActivity extends AppCompatActivity {
    private String[] name;
    private Drawable[] picture;

    private Context mContext;

    RelativeLayout mRelativeLayout;
    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request window feature action bar
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main_gears );

        // Get the application context
        mContext = getApplicationContext();

        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rm);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Define a layout for RecyclerView
        mLayoutManager = new GridLayoutManager(mContext,2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Initialize a new instance of RecyclerView Adapter instance
        Resources resources = mContext.getResources();
        name = resources.getStringArray(R.array.gear);
        TypedArray a = resources.obtainTypedArray(R.array.gear_picture);
        Log.d( "debug",""+a.length() );
        picture = new Drawable[a.length()];
        Log.d( "debug",""+picture.length );
        for (int i = 0; i < picture.length; i++) {
            Log.d("debug",""+i);
            picture[i] = a.getDrawable(i);
        }
        a.recycle();

        mAdapter = new GearsAdapter(mContext,name,picture);

        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter(mAdapter);
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
