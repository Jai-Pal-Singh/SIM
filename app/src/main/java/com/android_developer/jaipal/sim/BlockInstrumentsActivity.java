package com.android_developer.jaipal.sim;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hp on 2018-04-02.
 */

public class BlockInstrumentsActivity extends AppCompatActivity {

    private Button addBlockBtn, removeBlockBtn;
    int blockCount = 2, axleCount = 12, defaultMinBlockCount = 2, defaultMaxBlockCount =4,id;
    private String selectedDivision="", stationCode="";
    private SharedPreferences sharedpreferences;
    private final String MyPREFERENCES = "MyPrefs" ;
    private setUpStorageVariablesForDefaultGate defaultBISetUp;
    private setUpStorageVariablesForAddedBI[] addedBISetUp = new setUpStorageVariablesForAddedBI[2];
    private setUpStorageVariablesForAddedAC[] addedACSetUp = new setUpStorageVariablesForAddedAC[2];
    Boolean isActivityComplete;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_instruments_activity);

        getDivisionFromSharedPreferences();
        defaultBISetUp = new setUpStorageVariablesForDefaultGate();
        isActivityComplete = false;

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

                TextView txtview = myLayout2.findViewById(R.id.axleCounter3TextView  );
                id = axleCount-10;
                txtview.setText( "Axle Counter "+id );
                myLayout2.setId( axleCount);
                addedACSetUp[axleCount-13] = new setUpStorageVariablesForAddedAC(myLayout2,axleCount-13);

                txtview = myLayout1.findViewById(R.id.bi3TextView  );
                txtview.setText( "BI "+blockCount );
                myLayout1.setId( blockCount );
                addedBISetUp[blockCount-3] = new setUpStorageVariablesForAddedBI(myLayout1,blockCount-3);
                // add our custom layout to the main layout
                mainLayout1.addView(myLayout1);
                mainLayout2.addView(myLayout2);
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

    private void getDivisionFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        selectedDivision = sharedpreferences.getString("division", null);
        stationCode = sharedpreferences.getString( "stationCode",null );
    }

    private void setActionBySpinner(String selectedDivision, Spinner ActionBySpr) {
        ArrayAdapter<String> adapter;
        String[] actionBy;
        final List<String> actionByList;
        switch (selectedDivision) {
            case "JP":
                actionBy = createActionByList(stationCode, "SSE/Sig/","SSE/Tele/",getResources().getStringArray( R.array.jaipurActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<String>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                break;
            case "JU":
                actionBy = createActionByList(stationCode, "SSE/Sig/","SSE/Tele/",getResources().getStringArray( R.array.jodhpurActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<String>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                break;
            case "AII":
                actionBy = createActionByList(stationCode, "SSE/Sig/","SSE/Tele/",getResources().getStringArray( R.array.ajmerActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<String>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                break;
            case "BKN":
                actionBy = createActionByList(stationCode, "SSE/Sig/","SSE/Tele/",getResources().getStringArray( R.array.bikanerActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<String>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                break;
        }
    }

    private String[] createActionByList(String stationCode, String firstString, String secondString, String[] stringArray) {
        String firstElement = firstString+stationCode;
        String secondElement = secondString+stationCode;
        String[] tempArray = new String[ stringArray.length + 2 ];
        tempArray[0] = firstElement;
        tempArray[1] = secondElement;
        System.arraycopy( stringArray, 0, tempArray, 2, stringArray.length );
        return tempArray;
    }

    public void saveBIdata(View view) {
        saveDataInSharedPreferences();
        if( validateUserInput() ) {
            isActivityComplete = true;
            updateIsActivityInSharedPreferences();
            Toast.makeText(getApplicationContext(), "Entries Saved", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Please fill all entries", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateIsActivityInSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean( "biAcActivityComplete",isActivityComplete );
        editor.apply();
    }

    private void saveDataInSharedPreferences() {
        defaultBISetUp.saveDataForBIAC1InSharedPreferences();
        defaultBISetUp.saveDataForBIAC2InSharedPreferences();
        for(int biCount =0, acCount = 0; biCount <= blockCount-3 ; biCount++, acCount++ ){
            addedBISetUp[biCount].saveDataForAddedBiInSharedPreferences();
            addedACSetUp[acCount].saveDataForAddedAcInSharedPreferences();
        }
    }

    private boolean validateUserInput() {
        Boolean isAnyAddedBiEmpty = false, isAnyAddedAcEmpty = false;
        for(int blockNumber = 0; blockNumber <= blockCount-3 ; blockNumber++ ){
            if(addedBISetUp[blockNumber].validateAddBI() ) {
                isAnyAddedBiEmpty = true;
            }
        }
        for(int axleNumber = 0; axleNumber <= axleCount-13 ; axleNumber++ ){
            if(addedACSetUp[axleNumber].validateAddAC() ) {
                isAnyAddedAcEmpty = true;
            }
        }
        return (!defaultBISetUp.validateDefaultBIAC() && !isAnyAddedBiEmpty  && !isAnyAddedAcEmpty);
    }

    private class setUpStorageVariablesForDefaultGate{
        Spinner bi1ActionBySpr,recordsBI1Spr,workingAC1Spr,electricalAC1Spr,ac1ActionBySpr;
        EditText bi1EditTxt,localBI1EditTxt,lineBI1EditTxt,voltageOutgoingBI1EditTxt,currentOutgoingBI1EditTxt,voltageIncomingBI1EditTxt,currentIncomingBI1EditTxt,bi1deficiencyEditTxt,lineClearBI1EditTxt,bi1ActionByEditTxt;
        EditText axleCounter1EditTxt,resetAC1EditTxt,ac1ActionByEditTxt ,ac1deficiencyEditText;

        Spinner bi2ActionBySpr,recordsBI2Spr,workingAC2Spr,electricalAC2Spr,ac2ActionBySpr;
        EditText bi2EditTxt,localBI2EditTxt,lineBI2EditTxt,voltageOutgoingBI2EditTxt,currentOutgoingBI2EditTxt,voltageIncomingBI2EditTxt,currentIncomingBI2EditTxt,bi2deficiencyEditTxt,lineClearBI2EditTxt,bi2ActionByEditTxt;
        EditText axleCounter2EditTxt,resetAC2EditTxt,ac2ActionByEditTxt, ac2deficiencyEditText;

        private setUpStorageVariablesForDefaultGate(){
            initialUISetup();
            getSavedDataForBIAC1FromSharedPreferences();
            getSavedDataForBIAC2FromSharedPreferences();

            applyOnFocusChangeListener(bi1EditTxt);
            applyOnFocusChangeListener(localBI1EditTxt);
            applyOnFocusChangeListener(lineBI1EditTxt);
            applyOnFocusChangeListener(voltageOutgoingBI1EditTxt);
            applyOnFocusChangeListener(currentOutgoingBI1EditTxt);
            applyOnFocusChangeListener(voltageIncomingBI1EditTxt);
            applyOnFocusChangeListener(currentIncomingBI1EditTxt);
            applyOnFocusChangeListener(lineClearBI1EditTxt);
            applyOnFocusChangeListener(bi1ActionByEditTxt);

            applyOnFocusChangeListener(bi2EditTxt);
            applyOnFocusChangeListener(localBI2EditTxt);
            applyOnFocusChangeListener(lineBI2EditTxt);
            applyOnFocusChangeListener(voltageOutgoingBI2EditTxt);
            applyOnFocusChangeListener(currentOutgoingBI2EditTxt);
            applyOnFocusChangeListener(voltageIncomingBI2EditTxt);
            applyOnFocusChangeListener(currentIncomingBI2EditTxt);
            applyOnFocusChangeListener(lineClearBI2EditTxt);
            applyOnFocusChangeListener(bi2ActionByEditTxt);

            applyOnFocusChangeListener( axleCounter1EditTxt );
            applyOnFocusChangeListener( resetAC1EditTxt );
            applyOnFocusChangeListener( ac1ActionByEditTxt );

            applyOnFocusChangeListener( axleCounter2EditTxt );
            applyOnFocusChangeListener( resetAC2EditTxt );
            applyOnFocusChangeListener( ac2ActionByEditTxt );

            setActionBySpinner(selectedDivision,bi1ActionBySpr);
            applyOnItemSelectedListener(bi1ActionBySpr ,bi1ActionByEditTxt);
            setActionBySpinner(selectedDivision,bi2ActionBySpr);
            applyOnItemSelectedListener(bi2ActionBySpr ,bi2ActionByEditTxt);

            setActionBySpinner(selectedDivision,ac1ActionBySpr);
            applyOnItemSelectedListener(ac1ActionBySpr,ac1ActionByEditTxt);
            setActionBySpinner(selectedDivision,ac2ActionBySpr);
            applyOnItemSelectedListener(ac2ActionBySpr ,ac2ActionByEditTxt);
        }

        private void initialUISetup() {
            bi1EditTxt = findViewById( R.id.bi1EditText );
            localBI1EditTxt = findViewById( R.id.localBI1EditText );
            lineBI1EditTxt = findViewById( R.id.lineBI1EditText );
            voltageOutgoingBI1EditTxt = findViewById( R.id.voltageOutgoingBI1EditText );
            currentOutgoingBI1EditTxt = findViewById( R.id.currentOutgoingBI1EditText );
            voltageIncomingBI1EditTxt = findViewById( R.id.voltageIncomingBI1EditText );
            currentIncomingBI1EditTxt = findViewById( R.id.currentIncomingBI1EditText );
            bi1deficiencyEditTxt = findViewById( R.id.bi1deficiencyEditText );
            lineClearBI1EditTxt = findViewById( R.id.lineClearBI1EditText );
            recordsBI1Spr = findViewById( R.id.recordsBI1Spinner );
            bi1ActionBySpr = findViewById( R.id.bi1ActionBySpinner );
            bi1ActionByEditTxt = findViewById( R.id.bi1ActionByEditText );

            axleCounter1EditTxt = findViewById( R.id.axleCounter1EditText );
            workingAC1Spr = findViewById( R.id.workingAC1Spinner );
            electricalAC1Spr = findViewById( R.id.electricalAC1Spinner );
            resetAC1EditTxt = findViewById( R.id.resetAC1EditText );
            ac1ActionBySpr = findViewById( R.id.ac1ActionBySpinner );
            ac1ActionByEditTxt = findViewById( R.id.ac1ActionByEditText );
            ac1deficiencyEditText = findViewById( R.id.ac1deficiencyEditText );

            bi2EditTxt = findViewById( R.id.bi2EditText );
            localBI2EditTxt = findViewById( R.id.localBI2EditText );
            lineBI2EditTxt = findViewById( R.id.lineBI2EditText );
            voltageOutgoingBI2EditTxt = findViewById( R.id.voltageOutgoingBI2EditText );
            currentOutgoingBI2EditTxt = findViewById( R.id.currentOutgoingBI2EditText );
            voltageIncomingBI2EditTxt = findViewById( R.id.voltageIncomingBI2EditText );
            currentIncomingBI2EditTxt = findViewById( R.id.currentIncomingBI2EditText );
            bi2deficiencyEditTxt = findViewById( R.id.bi2deficiencyEditText );
            lineClearBI2EditTxt = findViewById( R.id.lineClearBI2EditText );
            recordsBI2Spr = findViewById( R.id.recordsBI2Spinner );
            bi2ActionBySpr = findViewById( R.id.bi2ActionBySpinner );
            bi2ActionByEditTxt = findViewById( R.id.bi2ActionByEditText );

            axleCounter2EditTxt = findViewById( R.id.axleCounter2EditText );
            workingAC2Spr = findViewById( R.id.workingAC2Spinner );
            electricalAC2Spr = findViewById( R.id.electricalAC2Spinner );
            resetAC2EditTxt = findViewById( R.id.resetAC2EditText );
            ac2ActionBySpr = findViewById( R.id.ac2ActionBySpinner );
            ac2ActionByEditTxt = findViewById( R.id.ac2ActionByEditText );
            ac2deficiencyEditText = findViewById( R.id.ac2deficiencyEditText );
        }

        public void saveDataForBIAC1InSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean( "biAcActivityComplete",isActivityComplete );
            editor.putString( "bi1EditTxt",bi1EditTxt.getText().toString() );
            editor.putString( "localBI1EditTxt",localBI1EditTxt.getText().toString() );
            editor.putString( "lineBI1EditTxt",lineBI1EditTxt.getText().toString() );
            editor.putString( "voltageOutgoingBI1EditTxt",voltageOutgoingBI1EditTxt.getText().toString() );
            editor.putString( "currentOutgoingBI1EditTxt",currentOutgoingBI1EditTxt.getText().toString() );
            editor.putString( "voltageIncomingBI1EditTxt",voltageIncomingBI1EditTxt.getText().toString() );
            editor.putString( "currentIncomingBI1EditTxt",currentIncomingBI1EditTxt.getText().toString() );
            editor.putString( "bi1deficiencyEditTxt",bi1deficiencyEditTxt.getText().toString() );
            editor.putString( "lineClearBI1EditTxt",lineClearBI1EditTxt.getText().toString() );
            editor.putString( "recordsBI1Spr",recordsBI1Spr.getSelectedItem().toString() );
            editor.putString( "bi1ActionBySpr",bi1ActionBySpr.getSelectedItem().toString() );
            editor.putString( "bi1ActionByEditTxt",bi1ActionByEditTxt.getText().toString() );

            editor.putString( "axleCounter1EditTxt",axleCounter1EditTxt.getText().toString() );
            editor.putString( "workingAC1Spr",workingAC1Spr.getSelectedItem().toString() );
            editor.putString( "electricalAC1Spr",electricalAC1Spr.getSelectedItem().toString() );
            editor.putString( "resetAC1EditTxt",resetAC1EditTxt.getText().toString() );
            editor.putString( "ac1deficiencyEditText",ac1deficiencyEditText.getText().toString() );
            editor.putString( "ac1ActionBySpr",ac1ActionBySpr.getSelectedItem().toString() );
            editor.putString( "ac1ActionByEditTxt",ac1ActionByEditTxt.getText().toString() );

            editor.putInt( "biCount",blockCount );
            editor.putInt( "acCount",axleCount );

            editor.apply();
        }

        private void getSavedDataForBIAC1FromSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            bi1EditTxt.setText( sharedpreferences.getString( "bi1EditTxt",null ) );
            localBI1EditTxt.setText( sharedpreferences.getString( "localBI1EditTxt",null ) );
            lineBI1EditTxt.setText( sharedpreferences.getString( "lineBI1EditTxt",null ) );
            voltageOutgoingBI1EditTxt.setText( sharedpreferences.getString( "voltageOutgoingBI1EditTxt",null ) );
            currentOutgoingBI1EditTxt.setText( sharedpreferences.getString( "currentOutgoingBI1EditTxt",null ) );
            voltageIncomingBI1EditTxt.setText( sharedpreferences.getString( "voltageIncomingBI1EditTxt",null ) );
            currentIncomingBI1EditTxt.setText( sharedpreferences.getString( "currentIncomingBI1EditTxt",null ) );
            bi1deficiencyEditTxt.setText( sharedpreferences.getString( "bi1deficiencyEditTxt",null ) );
            lineClearBI1EditTxt.setText( sharedpreferences.getString( "lineClearBI1EditTxt",null ) );
            bi1ActionByEditTxt.setText( sharedpreferences.getString( "bi1ActionByEditTxt",null ) );

            axleCounter1EditTxt.setText( sharedpreferences.getString( "axleCounter1EditTxt",null ) );
            resetAC1EditTxt.setText( sharedpreferences.getString( "resetAC1EditTxt",null ) );
            ac1deficiencyEditText.setText( sharedpreferences.getString( "ac1deficiencyEditText",null ) );
            ac1ActionByEditTxt.setText( sharedpreferences.getString( "ac1ActionByEditTxt",null ) );
        }

        public void saveDataForBIAC2InSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString( "bi2EditTxt",bi2EditTxt.getText().toString() );
            editor.putString( "localBI2EditTxt",localBI2EditTxt.getText().toString() );
            editor.putString( "lineBI2EditTxt",lineBI2EditTxt.getText().toString() );
            editor.putString( "voltageOutgoingBI2EditTxt",voltageOutgoingBI2EditTxt.getText().toString() );
            editor.putString( "currentOutgoingBI2EditTxt",currentOutgoingBI2EditTxt.getText().toString() );
            editor.putString( "voltageIncomingBI2EditTxt",voltageIncomingBI2EditTxt.getText().toString() );
            editor.putString( "currentIncomingBI2EditTxt",currentIncomingBI2EditTxt.getText().toString() );
            editor.putString( "bi2deficiencyEditTxt",bi2deficiencyEditTxt.getText().toString() );
            editor.putString( "lineClearBI2EditTxt",lineClearBI2EditTxt.getText().toString() );
            editor.putString( "recordsBI2Spr",recordsBI2Spr.getSelectedItem().toString() );
            editor.putString( "bi2ActionBySpr",bi2ActionBySpr.getSelectedItem().toString() );
            editor.putString( "bi2ActionByEditTxt",bi2ActionByEditTxt.getText().toString() );

            editor.putString( "axleCounter2EditTxt",axleCounter2EditTxt.getText().toString() );
            editor.putString( "workingAC2Spr",workingAC2Spr.getSelectedItem().toString() );
            editor.putString( "electricalAC2Spr",electricalAC2Spr.getSelectedItem().toString() );
            editor.putString( "resetAC2EditTxt",resetAC2EditTxt.getText().toString() );
            editor.putString( "ac2deficiencyEditText",ac2deficiencyEditText.getText().toString() );
            editor.putString( "ac2ActionBySpr",ac2ActionBySpr.getSelectedItem().toString() );
            editor.putString( "ac2ActionByEditTxt",ac2ActionByEditTxt.getText().toString() );
            editor.apply();
        }

        private void getSavedDataForBIAC2FromSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            bi2EditTxt.setText( sharedpreferences.getString( "bi2EditTxt",null ) );
            localBI2EditTxt.setText( sharedpreferences.getString( "localBI2EditTxt",null ) );
            lineBI2EditTxt.setText( sharedpreferences.getString( "lineBI2EditTxt",null ) );
            voltageOutgoingBI2EditTxt.setText( sharedpreferences.getString( "voltageOutgoingBI2EditTxt",null ) );
            currentOutgoingBI2EditTxt.setText( sharedpreferences.getString( "currentOutgoingBI2EditTxt",null ) );
            voltageIncomingBI2EditTxt.setText( sharedpreferences.getString( "voltageIncomingBI2EditTxt",null ) );
            currentIncomingBI2EditTxt.setText( sharedpreferences.getString( "currentIncomingBI2EditTxt",null ) );
            bi2deficiencyEditTxt.setText( sharedpreferences.getString( "bi2deficiencyEditTxt",null ) );
            lineClearBI2EditTxt.setText( sharedpreferences.getString( "lineClearBI2EditTxt",null ) );
            bi2ActionByEditTxt.setText( sharedpreferences.getString( "bi2ActionByEditTxt",null ) );

            axleCounter2EditTxt.setText( sharedpreferences.getString( "axleCounter2EditTxt",null ) );
            resetAC2EditTxt.setText( sharedpreferences.getString( "resetAC2EditTxt",null ) );
            ac2deficiencyEditText.setText( sharedpreferences.getString( "ac2deficiencyEditText",null ) );
            ac2ActionByEditTxt.setText( sharedpreferences.getString( "ac2ActionByEditTxt",null ) );
        }

        private void applyOnItemSelectedListener(final Spinner biActionBySpr, final EditText biActionByEditTxt) {
            biActionBySpr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String s = biActionBySpr.getSelectedItem().toString();
                    if(s.equals( "Other" )){
                        biActionByEditTxt.setVisibility( View.VISIBLE );
                    }
                    else{
                        biActionByEditTxt.setVisibility( View.GONE );
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            } );
        }

        private void applyOnFocusChangeListener(final EditText inputEditTxt) {
            inputEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        if(TextUtils.isEmpty(inputEditTxt.getText().toString())){
                            inputEditTxt.setError( "Invalid Input" );
                        }
                        else {
                            if(inputEditTxt.equals( bi1EditTxt )) {
                                String s = inputEditTxt.getText().toString();
                                axleCounter1EditTxt.setText( s );
                            }
                            if(inputEditTxt.equals( bi2EditTxt )) {
                                String s = inputEditTxt.getText().toString();
                                axleCounter2EditTxt.setText( s );
                            }
                        }
                    }
                    else
                        inputEditTxt.setError( null );
                }
            });
        }

        private  boolean validateDefaultBIAC(){
            Boolean isAnyFieldsEmpty = false;
            if(bi1EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                bi1EditTxt.setError( "Invalid Input" );
            }
            if(localBI1EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                localBI1EditTxt.setError( "Invalid Input" );
            }
            if(lineBI1EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                lineBI1EditTxt.setError( "Invalid Input" );
            }
            if(voltageOutgoingBI1EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                voltageOutgoingBI1EditTxt.setError( "Invalid Input" );
            }
            if(currentOutgoingBI1EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                currentOutgoingBI1EditTxt.setError( "Invalid Input" );
            }
            if(voltageIncomingBI1EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                voltageIncomingBI1EditTxt.setError( "Invalid Input" );
            }
            if(currentIncomingBI1EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                currentIncomingBI1EditTxt.setError( "Invalid Input" );
            }
            if(lineClearBI1EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                lineClearBI1EditTxt.setError( "Invalid Input" );
            }
            if(bi1ActionBySpr.getSelectedItem().toString().equals( "Other" )) {
                if (bi1ActionByEditTxt.getText().toString().isEmpty()) {
                    isAnyFieldsEmpty = true;
                    bi1ActionByEditTxt.setError( "Invalid Input" );
                }
            }

            if(bi2EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                bi2EditTxt.setError( "Invalid Input" );
            }
            if(localBI2EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                localBI2EditTxt.setError( "Invalid Input" );
            }
            if(lineBI2EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                lineBI2EditTxt.setError( "Invalid Input" );
            }
            if(voltageOutgoingBI2EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                voltageOutgoingBI2EditTxt.setError( "Invalid Input" );
            }
            if(currentOutgoingBI2EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                currentOutgoingBI2EditTxt.setError( "Invalid Input" );
            }
            if(voltageIncomingBI2EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                voltageIncomingBI2EditTxt.setError( "Invalid Input" );
            }
            if(currentIncomingBI2EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                currentIncomingBI2EditTxt.setError( "Invalid Input" );
            }
            if(lineClearBI2EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                lineClearBI2EditTxt.setError( "Invalid Input" );
            }
            if(bi2ActionBySpr.getSelectedItem().toString().equals( "Other" )) {
                if (bi2ActionByEditTxt.getText().toString().isEmpty()) {
                    isAnyFieldsEmpty = true;
                    bi2ActionByEditTxt.setError( "Invalid Input" );
                }
            }

            if(axleCounter1EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                axleCounter1EditTxt.setError( "Invalid Input" );
            }
            if(resetAC1EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                resetAC1EditTxt.setError( "Invalid Input" );
            }
            if(ac1ActionBySpr.getSelectedItem().toString().equals( "Other" )) {
                if (ac1ActionByEditTxt.getText().toString().isEmpty()) {
                    isAnyFieldsEmpty = true;
                    ac1ActionByEditTxt.setError( "Invalid Input" );
                }
            }

            if(axleCounter2EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                axleCounter2EditTxt.setError( "Invalid Input" );
            }
            if(resetAC2EditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                resetAC2EditTxt.setError( "Invalid Input" );
            }
            if(ac2ActionBySpr.getSelectedItem().toString().equals( "Other" )) {
                if (ac2ActionByEditTxt.getText().toString().isEmpty()) {
                    isAnyFieldsEmpty = true;
                    ac2ActionByEditTxt.setError( "Invalid Input" );
                }
            }
            return isAnyFieldsEmpty;
        }
    }

    private class setUpStorageVariablesForAddedBI{
        Spinner biActionBySpr,recordsBISpr;
        EditText biEditTxt,localBIEditTxt,lineBIEditTxt,voltageOutgoingBIEditTxt,currentOutgoingBIEditTxt,voltageIncomingBIEditTxt,currentIncomingBIEditTxt,bideficiencyEditTxt,lineClearBIEditTxt,biActionByEditTxt;
        int relativeAxleCount;
        private setUpStorageVariablesForAddedBI(View myLayout, int axleCount) {
            relativeAxleCount = axleCount;
            initialUISetup(myLayout);
            getSavedDataForAddedBiFromSharedPreferences();
            applyOnFocusChangeListener(biEditTxt);
            applyOnFocusChangeListener(localBIEditTxt);
            applyOnFocusChangeListener(lineBIEditTxt);
            applyOnFocusChangeListener(voltageOutgoingBIEditTxt);
            applyOnFocusChangeListener(currentOutgoingBIEditTxt);
            applyOnFocusChangeListener(voltageIncomingBIEditTxt);
            applyOnFocusChangeListener(currentIncomingBIEditTxt);
            applyOnFocusChangeListener(lineClearBIEditTxt);
            applyOnFocusChangeListener(biActionByEditTxt);

            setActionBySpinner(selectedDivision,biActionBySpr);
            applyOnItemSelectedListener(biActionBySpr ,biActionByEditTxt);
        }

        private void initialUISetup(View myLayout) {
            biEditTxt = myLayout.findViewById( R.id.bi2EditTextForReplicate );
            localBIEditTxt = myLayout.findViewById( R.id.localBI1EditTextForReplicate );
            lineBIEditTxt = myLayout.findViewById( R.id.lineBI1EditTextForReplicate );
            voltageOutgoingBIEditTxt = myLayout.findViewById( R.id.voltageOutgoingBI1EditTextForReplicate );
            currentOutgoingBIEditTxt = myLayout.findViewById( R.id.currentOutgoingBI1EditTextForReplicate );
            voltageIncomingBIEditTxt = myLayout.findViewById( R.id.voltageIncomingBI1EditTextForReplicate );
            currentIncomingBIEditTxt = myLayout.findViewById( R.id.currentIncomingBI1EditTextForReplicate );
            bideficiencyEditTxt = myLayout.findViewById( R.id.bi1deficiencyEditTextForReplicate );
            lineClearBIEditTxt = myLayout.findViewById( R.id.lineClearBI1EditTextForReplicate );
            recordsBISpr = myLayout.findViewById( R.id.recordsBI1SpinnerForReplicate );
            biActionBySpr = myLayout.findViewById( R.id.biActionBySpinnerForReplicate );
            biActionByEditTxt = myLayout.findViewById( R.id.biActionByEditTextForReplicate );
        }

        private void applyOnItemSelectedListener(final Spinner biActionBySpr, final EditText biActionByEditTxt) {
            biActionBySpr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String s = biActionBySpr.getSelectedItem().toString();
                    if(s.equals( "Other" )){
                        biActionByEditTxt.setVisibility( View.VISIBLE );
                    }
                    else{
                        biActionByEditTxt.setVisibility( View.GONE );
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            } );
        }

        private void applyOnFocusChangeListener(final EditText inputEditTxt) {
            inputEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        if(TextUtils.isEmpty(inputEditTxt.getText().toString())){
                            inputEditTxt.setError( "Invalid Input" );
                        }
                        else {
                            if(inputEditTxt.equals( biEditTxt )) {
                                String s = inputEditTxt.getText().toString();
                                addedACSetUp[relativeAxleCount].axleCounterEditTxt.setText( s );
                            }
                        }
                    }
                    else
                        inputEditTxt.setError( null );
                }
            });
        }

        private  boolean validateAddBI(){
            Boolean isAnyFieldsEmpty = false;
            if(biEditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                biEditTxt.setError( "Invalid Input" );
            }
            if(localBIEditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                localBIEditTxt.setError( "Invalid Input" );
            }
            if(lineBIEditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                lineBIEditTxt.setError( "Invalid Input" );
            }
            if(voltageOutgoingBIEditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                voltageOutgoingBIEditTxt.setError( "Invalid Input" );
            }
            if(currentOutgoingBIEditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                currentOutgoingBIEditTxt.setError( "Invalid Input" );
            }
            if(voltageIncomingBIEditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                voltageIncomingBIEditTxt.setError( "Invalid Input" );
            }
            if(currentIncomingBIEditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                currentIncomingBIEditTxt.setError( "Invalid Input" );
            }
            if(lineClearBIEditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                lineClearBIEditTxt.setError( "Invalid Input" );
            }
            if(biActionBySpr.getSelectedItem().toString().equals( "Other" )) {
                if (biActionByEditTxt.getText().toString().isEmpty()) {
                    isAnyFieldsEmpty = true;
                    biActionByEditTxt.setError( "Invalid Input" );
                }
            }
            return isAnyFieldsEmpty;
        }

        public void saveDataForAddedBiInSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString( "biEditTxt"+relativeAxleCount,biEditTxt.getText().toString() );
            editor.putString( "localBIEditTxt"+relativeAxleCount,localBIEditTxt.getText().toString() );
            editor.putString( "lineBIEditTxt"+relativeAxleCount,lineBIEditTxt.getText().toString() );
            editor.putString( "voltageOutgoingBIEditTxt"+relativeAxleCount,voltageOutgoingBIEditTxt.getText().toString() );
            editor.putString( "currentOutgoingBIEditTxt"+relativeAxleCount,currentOutgoingBIEditTxt.getText().toString() );
            editor.putString( "voltageIncomingBIEditTxt"+relativeAxleCount,voltageIncomingBIEditTxt.getText().toString() );
            editor.putString( "currentIncomingBIEditTxt"+relativeAxleCount,currentIncomingBIEditTxt.getText().toString() );
            editor.putString( "bideficiencyEditTxt"+relativeAxleCount,bideficiencyEditTxt.getText().toString() );
            editor.putString( "lineClearBIEditTxt"+relativeAxleCount,lineClearBIEditTxt.getText().toString() );
            editor.putString( "recordsBISpr"+relativeAxleCount,recordsBISpr.getSelectedItem().toString() );
            editor.putString( "biActionBySpr"+relativeAxleCount,biActionBySpr.getSelectedItem().toString() );
            editor.putString( "biActionByEditTxt"+relativeAxleCount,biActionByEditTxt.getText().toString() );
            editor.apply();
        }

        private void getSavedDataForAddedBiFromSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            biEditTxt.setText( sharedpreferences.getString( "biEditTxt"+relativeAxleCount,null ) );
            localBIEditTxt.setText( sharedpreferences.getString( "localBIEditTxt"+relativeAxleCount,null ) );
            lineBIEditTxt.setText( sharedpreferences.getString( "lineBIEditTxt"+relativeAxleCount,null ) );
            voltageOutgoingBIEditTxt.setText( sharedpreferences.getString( "voltageOutgoingBIEditTxt"+relativeAxleCount,null ) );
            currentOutgoingBIEditTxt.setText( sharedpreferences.getString( "currentOutgoingBIEditTxt"+relativeAxleCount,null ) );
            voltageIncomingBIEditTxt.setText( sharedpreferences.getString( "voltageIncomingBIEditTxt"+relativeAxleCount,null ) );
            currentIncomingBIEditTxt.setText( sharedpreferences.getString( "currentIncomingBIEditTxt"+relativeAxleCount,null ) );
            bideficiencyEditTxt.setText( sharedpreferences.getString( "bideficiencyEditTxt"+relativeAxleCount,null ) );
            lineClearBIEditTxt.setText( sharedpreferences.getString( "lineClearBIEditTxt"+relativeAxleCount,null ) );
            biActionByEditTxt.setText( sharedpreferences.getString( "biActionByEditTxt"+relativeAxleCount,null ) );
        }

    }


    private class setUpStorageVariablesForAddedAC{
        Spinner workingACSpr,electricalACSpr,acActionBySpr;
        EditText axleCounterEditTxt,resetACEditTxt,acActionByEditTxt,acdeficiencyEditText;
        int countAxle;

        private setUpStorageVariablesForAddedAC(View myLayout, int count) {
            initialUISetup(myLayout);
            countAxle = count;
            getSavedDataForAddedAcFromSharedPreferences();
            applyOnFocusChangeListener( axleCounterEditTxt );
            applyOnFocusChangeListener( resetACEditTxt );
            applyOnFocusChangeListener( acActionByEditTxt );

            setActionBySpinner(selectedDivision,acActionBySpr);
            applyOnItemSelectedListener(acActionBySpr, acActionByEditTxt);
        }

        private void initialUISetup(View myLayout) {
            axleCounterEditTxt = myLayout.findViewById( R.id.axleCounter2EditTextForReplicate );
            workingACSpr = myLayout.findViewById( R.id.workingAC2SpinnerForReplicate );
            electricalACSpr = myLayout.findViewById( R.id.electricalAC2SpinnerForReplicate );
            resetACEditTxt = myLayout.findViewById( R.id.resetAC2EditTextForReplicate );
            acActionBySpr = myLayout.findViewById( R.id.acActionBySpinnerForReplicate );
            acActionByEditTxt = myLayout.findViewById( R.id.acActionByEditTextForReplicate );
            acdeficiencyEditText = myLayout.findViewById( R.id.acdeficiencyEditText );
        }

        public void saveDataForAddedAcInSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString( "axleCounterEditTxt"+countAxle,axleCounterEditTxt.getText().toString() );
            editor.putString( "workingACSpr"+countAxle,workingACSpr.getSelectedItem().toString() );
            editor.putString( "electricalACSpr"+countAxle,electricalACSpr.getSelectedItem().toString() );
            editor.putString( "resetACEditTxt"+countAxle,resetACEditTxt.getText().toString() );
            editor.putString( "acdeficiencyEditText"+countAxle,acdeficiencyEditText.getText().toString() );
            editor.putString( "acActionBySpr"+countAxle,acActionBySpr.getSelectedItem().toString() );
            editor.putString( "acActionByEditTxt"+countAxle,acActionByEditTxt.getText().toString() );
            editor.apply();
        }

        private void getSavedDataForAddedAcFromSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            axleCounterEditTxt.setText( sharedpreferences.getString( "axleCounterEditTxt"+countAxle,null ) );
            resetACEditTxt.setText( sharedpreferences.getString( "resetACEditTxt"+countAxle,null ) );
            acdeficiencyEditText.setText( sharedpreferences.getString( "acdeficiencyEditText"+countAxle,null ) );
            acActionByEditTxt.setText( sharedpreferences.getString( "acActionByEditTxt"+countAxle,null ) );
        }

        private void applyOnItemSelectedListener(final Spinner biActionBySpr, final EditText biActionByEditTxt) {
            biActionBySpr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String s = biActionBySpr.getSelectedItem().toString();
                    if(s.equals( "Other" )){
                        biActionByEditTxt.setVisibility( View.VISIBLE );
                    }
                    else{
                        biActionByEditTxt.setVisibility( View.GONE );
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            } );
        }

        private void applyOnFocusChangeListener(final EditText inputEditTxt) {
            inputEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        if(TextUtils.isEmpty(inputEditTxt.getText().toString())){
                            inputEditTxt.setError( "Invalid Input" );
                        }
                        else {
//                            Data is entered
                        }
                    }
                    else
                        inputEditTxt.setError( null );
                }
            });
        }

        private  boolean validateAddAC(){
            Boolean isAnyFieldsEmpty = false;
            if(axleCounterEditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                axleCounterEditTxt.setError( "Invalid Input" );
            }
            if(resetACEditTxt.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                resetACEditTxt.setError( "Invalid Input" );
            }
            if(acActionBySpr.getSelectedItem().toString().equals( "Other" )) {
                if (acActionByEditTxt.getText().toString().isEmpty()) {
                    isAnyFieldsEmpty = true;
                    acActionByEditTxt.setError( "Invalid Input" );
                }
            }
            return isAnyFieldsEmpty;
        }
    }
}
