package com.android_developer.jaipal.sim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

class AboutDeveloper extends AppCompatActivity {

    public void onCreate (Bundle savedInstanceState) {
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.about_developer_activity );
    }
}
