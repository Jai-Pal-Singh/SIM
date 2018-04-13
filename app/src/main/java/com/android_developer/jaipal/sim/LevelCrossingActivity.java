package com.android_developer.jaipal.sim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class LevelCrossingActivity extends AppCompatActivity {

    private Button addGateBtn;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_crossing_activity);

        //addGateBtn onclick
        addGateBtn = (Button)findViewById(R.id.addGateButton);
        addGateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get a reference to the already created main layout
                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.levelCrossingLayout11);
                // inflate (create) another copy of our custom layout
                LayoutInflater inflater = getLayoutInflater();
                View myLayout = inflater.inflate(R.layout.level_crossing_replicate_activity, mainLayout, false);
                // add our custom layout to the main layout
                mainLayout.addView(myLayout);
            }
        });

    }
}
