package com.android_developer.jaipal.sim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by hp on 2018-04-02.
 */

public class BlockInstrumentsActivity extends AppCompatActivity {

    private Button addBlockBtn, removeBlockBtn;
    int blockCount = 2, axleCount = 12, defaultMinBlockCount = 2, defaultMaxBlockCount =4,id;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_instruments_activity);

        //addGateBtn onclick
        addBlockBtn = (Button)findViewById(R.id.addBlockButton);
        removeBlockBtn = (Button)findViewById(R.id.removeBlockButton);
        addBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockCount++;
                axleCount++;
                if(blockCount<=defaultMinBlockCount){
                    removeBlockBtn.setVisibility( view.INVISIBLE );
                }
                else{
                    removeBlockBtn.setVisibility( view.VISIBLE );
                }

                if(blockCount>=defaultMaxBlockCount){
                    addBlockBtn.setVisibility( view.INVISIBLE );
                }
                else{
                    addBlockBtn.setVisibility( view.VISIBLE );
                }
                // get a reference to the already created main layout
                LinearLayout mainLayout1 = (LinearLayout) findViewById(R.id.blockInstrumentsLayout2);
                LinearLayout mainLayout2 = (LinearLayout) findViewById(R.id.axleCountersLayout1);
                // inflate (create) another copy of our custom layout
                LayoutInflater inflater = getLayoutInflater();
                View myLayout1 = inflater.inflate(R.layout.block_instruments_bi_replicate_activity, mainLayout1, false);
                View myLayout2 = inflater.inflate(R.layout.block_instruments_ac_replicate_activity, mainLayout2, false);

                TextView txtview = myLayout1.findViewById(R.id.bi3TextView  );
                txtview.setText( "BI "+blockCount );
                myLayout1.setId( blockCount );

                txtview = myLayout2.findViewById(R.id.axleCounter3TextView  );
                id = axleCount-10;
                txtview.setText( "Axle Counter "+id );
                myLayout2.setId( axleCount);
                // add our custom layout to the main layout
                mainLayout1.addView(myLayout1);
                mainLayout2.addView(myLayout2);


//                TextView txtview = (TextView) findViewById( R.id.bi3TextView );
//                txtview.setText( "BI "+blockCount );

//                myLayout2.setId( blockCount);
//                txtview = (TextView) findViewById( R.id.axleCounter3TextView );
//                id = blockCount -1;
//                txtview.setText( "Axle Counter "+id );
            }
        });

        removeBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View myLayout2 = findViewById( axleCount );
                View myLayout1 = findViewById( blockCount );


                LinearLayout mainLayout1 = (LinearLayout) myLayout1.getParent();
                LinearLayout mainLayout2 = (LinearLayout) myLayout2.getParent();

                mainLayout1.removeView(myLayout1);
                mainLayout2.removeView(myLayout2);

                blockCount--;
                axleCount--;
                if(blockCount<=defaultMinBlockCount){
                    removeBlockBtn.setVisibility( view.INVISIBLE );
                }
                else{
                    removeBlockBtn.setVisibility( view.VISIBLE );
                }
                if(blockCount>=defaultMaxBlockCount){
                    addBlockBtn.setVisibility( view.INVISIBLE );
                }
                else{
                    addBlockBtn.setVisibility( view.VISIBLE );
                }
            }
        });

    }
}
