package com.android_developer.jaipal.sim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class AboutITCenterActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.it_center_activity );
    }
}
