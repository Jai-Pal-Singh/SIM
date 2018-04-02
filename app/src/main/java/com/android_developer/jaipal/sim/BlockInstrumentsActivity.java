package com.android_developer.jaipal.sim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * Created by hp on 2018-04-02.
 */

class BlockInstrumentsActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_instruments_activity);
    }
}
