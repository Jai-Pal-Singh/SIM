package com.android_developer.jaipal.sim;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

public class LevelCrossingActivity extends AppCompatActivity {

    private Button addGateBtn, removeGateBtn, levelCrossingGenerateInspectionNoteButton;
    int gateCount = 0, maxGateCount = 9, minGateCount = 0;
    private String selectedDivision="", stationCode="";
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;
    LCInspectionNotes lcInspectionNotes;
    setUpStorageVariablesForDefaultGate defaultGateSetUp;
    setUpStorageVariablesForAddedGate[] addedGateSetUp = new setUpStorageVariablesForAddedGate[10];
    Boolean isActivityComplete;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_crossing_activity);
        getDivisionFromSharedPreferences();
        isActivityComplete = false;
        addGateBtn = (Button)findViewById(R.id.addGateButton);
        removeGateBtn = (Button)findViewById(R.id.removeGateButton);
        levelCrossingGenerateInspectionNoteButton = (Button)findViewById(R.id.levelCrossingGenerateInspectionNoteButton);
        initialGates(addGateBtn);

        defaultGateSetUp = new setUpStorageVariablesForDefaultGate();
        defaultGateSetUp.getSavedDataFromSharedPreferences();
        //addGateBtn onclick
        addGateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gateCount++;

                if(gateCount<=minGateCount){
                    removeGateBtn.setVisibility( view.INVISIBLE );
                }
                else{
                    removeGateBtn.setVisibility( view.VISIBLE );
                }

                if(gateCount>=maxGateCount){
                    addGateBtn.setVisibility( view.INVISIBLE );
                }
                else{
                    addGateBtn.setVisibility( view.VISIBLE );
                }
                // get a reference to the already created main layout
                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.levelCrossingLayout11);
                // inflate (create) another copy of our custom layout
                LayoutInflater inflater = getLayoutInflater();
                View myLayout = inflater.inflate(R.layout.level_crossing_replicate_activity, mainLayout, false);
                // add our custom layout to the main layout
                mainLayout.addView(myLayout);
                myLayout.setId( gateCount );
                addedGateSetUp[gateCount] = new setUpStorageVariablesForAddedGate( myLayout , gateCount);
                addedGateSetUp[gateCount].getSavedDataFromSharedPreferences();
            }
        });

        removeGateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View myLayout = findViewById( gateCount );
                LinearLayout mainLayout = (LinearLayout) myLayout.getParent();
                mainLayout.removeView(myLayout);
                removeSharedPreferencesForRemovedGate( gateCount );
                gateCount--;

                if(gateCount<=minGateCount){
                    removeGateBtn.setVisibility( view.INVISIBLE );
                }
                else{
                    removeGateBtn.setVisibility( view.VISIBLE );
                }
                if(gateCount>=maxGateCount){
                    addGateBtn.setVisibility( view.INVISIBLE );
                }
                else{
                    addGateBtn.setVisibility( view.VISIBLE );
                }
            }
        });

    }

    private void initialGates(View view) {
        gateCount = sharedpreferences.getInt( "levelCrossingGateCount",0 );
        for(int gate = 1;gate<=gateCount;gate++){
            if(gate<=minGateCount){
                removeGateBtn.setVisibility( view.INVISIBLE );
            }
            else{
                removeGateBtn.setVisibility( view.VISIBLE );
            }

            if(gate>=maxGateCount){
                addGateBtn.setVisibility( view.INVISIBLE );
            }
            else{
                addGateBtn.setVisibility( view.VISIBLE );
            }
            // get a reference to the already created main layout
            LinearLayout mainLayout = (LinearLayout) findViewById(R.id.levelCrossingLayout11);
            // inflate (create) another copy of our custom layout
            LayoutInflater inflater = getLayoutInflater();
            View myLayout = inflater.inflate(R.layout.level_crossing_replicate_activity, mainLayout, false);
            // add our custom layout to the main layout
            mainLayout.addView(myLayout);
            myLayout.setId( gate );
            addedGateSetUp[gate] = new setUpStorageVariablesForAddedGate( myLayout , gate);
            addedGateSetUp[gate].getSavedDataFromSharedPreferences();
        }
    }

    private void removeSharedPreferencesForRemovedGate(int gateNumber){
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove( "gateNo"+gateNumber );
        editor.remove( "nameOfGateman"+gateNumber );
        editor.remove( "gateTypeSpnr"+gateNumber );
        editor.remove( "gateTypeSpnrForReplicatePosition"+gateNumber );
        editor.remove( getResources().getString( R.string.positive_boom_locking_tested )+gateNumber );
        editor.remove( getResources().getString( R.string.booms_were_painted )+gateNumber );
        editor.remove( getResources().getString( R.string.gateman_having_adequate_safety_knowledge )+gateNumber );
        editor.remove( getResources().getString( R.string.gate_telephone_s_found_in_working_order )+gateNumber );
        editor.remove( getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule )+gateNumber );
        editor.remove( getResources().getString( R.string.inspection_maintenance_records_were_maintained )+gateNumber );
        editor.remove( getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked )+gateNumber );
        editor.remove( "anyDeficiencyFound"+gateNumber );
        editor.remove( "levelCrossingActionBySpr"+gateNumber );
        editor.remove( "levelCrossingActionBySprForReplicate"+gateNumber );
        editor.remove( "levelCrossingActionByEditTxt"+gateNumber );
        editor.putInt( "levelCrossingGateCount",gateCount );
        editor.apply();
    }

    private void getDivisionFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        selectedDivision = sharedpreferences.getString("division", null);
        stationCode = sharedpreferences.getString( "stationCode",null );
    }

    private void setActionBySpinner(String selectedDivision, Spinner ActionBySpr, int position) {
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
                ActionBySpr.setSelection( position );
                break;
            case "JU":
                actionBy = createActionByList(stationCode, "SSE/Sig/","SSE/Tele/",getResources().getStringArray( R.array.jodhpurActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<String>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                ActionBySpr.setSelection( position );
                break;
            case "AII":
                actionBy = createActionByList(stationCode, "SSE/Sig/","SSE/Tele/",getResources().getStringArray( R.array.ajmerActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<String>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                ActionBySpr.setSelection( position );
                break;
            case "BKN":
                actionBy = createActionByList(stationCode, "SSE/Sig/","SSE/Tele/",getResources().getStringArray( R.array.bikanerActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<String>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                ActionBySpr.setSelection( position );
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

    public void saveLevelCrossingData(View view) {
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
        editor.putBoolean( "levelActivityComplete",isActivityComplete );
        editor.apply();
    }

    private void saveDataInSharedPreferences() {
        defaultGateSetUp.saveDataForDefaultGateInSharedPreferences();
        for(int gateNumber = 1; gateNumber <= gateCount ; gateNumber++ ){
            addedGateSetUp[gateNumber].saveDataForAddedGateInSharedPreferences();
        }
    }

    public Boolean validateUserInput() {
        Boolean isAnyAddedGateEmpty = false;
        for(int gateNumber = 1; gateNumber <= gateCount ; gateNumber++ ){
            if(addedGateSetUp[gateNumber].validateAddGate() )
                isAnyAddedGateEmpty = true;
        }

        return (defaultGateSetUp.validateDefaultGate() && !isAnyAddedGateEmpty );
    }

    public void submitLevelCrossingData(View view) {
        saveDataInSharedPreferences();
        if( validateUserInput() ) {
            isActivityComplete = true;
            updateIsActivityInSharedPreferences();
            sendDataToDBTask task = new sendDataToDBTask();
            task.execute();
//            FileUploadHandler fileUploadHandler = new FileUploadHandler();
//            Toast.makeText(getApplicationContext(), "Entries Saved", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Please fill all entries", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeSharedPreference() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        //Level Crossing
        editor.remove( "gateNo" );
        editor.remove( "nameOfGateman" );
        editor.remove( "gateTypeSpnr" );
        editor.remove( "gateTypeSpnrPosition" );
        editor.remove( getResources().getString( R.string.positive_boom_locking_tested ) );
        editor.remove( getResources().getString( R.string.booms_were_painted ) );
        editor.remove( getResources().getString( R.string.gateman_having_adequate_safety_knowledge ) );
        editor.remove( getResources().getString( R.string.gate_telephone_s_found_in_working_order ) );
        editor.remove( getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule ) );
        editor.remove( getResources().getString( R.string.inspection_maintenance_records_were_maintained ) );
        editor.remove( getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked ) );
        editor.remove( "anyDeficiencyFound" );
        editor.remove( "levelCrossingActionBySpr" );
        editor.remove( "levelCrossingActionBySprPosition" );
        editor.remove( "levelCrossingActionByEditTxt" );
        removeLevelCrossingReplicatedGates(sharedpreferences.getInt("levelCrossingGateCount",0));
        editor.remove( "levelActivityComplete" );
        editor.apply();
    }

    private void removeLevelCrossingReplicatedGates(int levelCrossingGateCount) {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        for(int gateNumber = 1; gateNumber <= levelCrossingGateCount ; gateNumber++ ){
            editor.remove( "gateNo"+gateNumber );
            editor.remove( "nameOfGateman"+gateNumber );
            editor.remove( "gateTypeSpnr"+gateNumber );
            editor.remove( "gateTypeSpnrPosition"+gateNumber );
            editor.remove( getResources().getString( R.string.positive_boom_locking_tested )+gateNumber );
            editor.remove( getResources().getString( R.string.booms_were_painted )+gateNumber );
            editor.remove( getResources().getString( R.string.gateman_having_adequate_safety_knowledge )+gateNumber );
            editor.remove( getResources().getString( R.string.gate_telephone_s_found_in_working_order )+gateNumber );
            editor.remove( getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule )+gateNumber );
            editor.remove( getResources().getString( R.string.inspection_maintenance_records_were_maintained )+gateNumber );
            editor.remove( getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked )+gateNumber );
            editor.remove( "anyDeficiencyFound"+gateNumber );
            editor.remove( "levelCrossingActionBySpr"+gateNumber );
            editor.remove( "levelCrossingActionBySprForReplicate"+gateNumber );
            editor.remove( "levelCrossingActionByEditTxt"+gateNumber );
            editor.apply();
        }
    }

    private class setUpStorageVariablesForDefaultGate{
        EditText gateNo, nameOfGateman,anyDeficiencyFound,levelCrossingActionByEditTxt;
        String levelCrossingActionBySpinnerVal, gateTypeSpinnerVal;
        Spinner gateTypeSpnr, levelCrossingActionBySpr;
        CheckBox PositiveBoomLocking,BoomsPainted,GatemanSafetyKnowledge, GateTelephone, SSEInspections, InspectionRecords, OtherElectricalMechanicalParameter;

        private setUpStorageVariablesForDefaultGate(){
            initialUISetup();
            getSavedDataFromSharedPreferences();
            ////////////////////////////////////////////////////////////////////////////////////////////////////
            //Item Selected Validation
            gateNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        if(TextUtils.isEmpty(gateNo.getText().toString())){
                            gateNo.setError( "Invalid Input" );
                        }
                        else {
//                            Data is entered
                        }
                    }
                    else
                        gateNo.setError( null );
                }
            });

            nameOfGateman.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        if(!TextUtils.isEmpty(nameOfGateman.getText().toString())){
//                            Data is entered
                        }
                        else {
                            nameOfGateman.setError( "Invalid Input" );
                        }
                    }
                    else
                        nameOfGateman.setError( null );
                }
            });

            levelCrossingActionByEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        if(!TextUtils.isEmpty(levelCrossingActionByEditTxt.getText().toString())){
//                            Data is entered
                        }
                        else {
                            levelCrossingActionByEditTxt.setError( "Invalid Input" );
                        }
                    }
                    else
                        levelCrossingActionByEditTxt.setError( null );
                }
            });
            //////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////
            //Add EditText Box on selecting Other
            setActionBySpinner(selectedDivision,levelCrossingActionBySpr,sharedpreferences.getInt( "levelCrossingActionBySprPosition",0 ));
            levelCrossingActionBySpr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    levelCrossingActionBySpinnerVal = levelCrossingActionBySpr.getSelectedItem().toString();
                    if(levelCrossingActionBySpinnerVal.equals( "Other" )){
                        levelCrossingActionByEditTxt.setVisibility( View.VISIBLE );
                    }
                    else{
                        levelCrossingActionByEditTxt.setVisibility( View.GONE );
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            } );
            //////////////////////////////////////////////////////////////////////////////////////////////////
        }

        private void initialUISetup(){
            gateNo = (EditText) findViewById( R.id.gateNoEditText );
            nameOfGateman =(EditText) findViewById( R.id.SMeditText );
            gateTypeSpnr = (Spinner) findViewById( R.id.gateTypeSpinner );
            PositiveBoomLocking = (CheckBox) findViewById( R.id.positive_boomCheckBox );
            BoomsPainted = (CheckBox) findViewById( R.id.booms_were_paintedCheckBox );
            GatemanSafetyKnowledge = (CheckBox) findViewById( R.id.gatemanCheckBox );
            GateTelephone = (CheckBox) findViewById( R.id.gateTelephoneCheckBox );
            SSEInspections = (CheckBox) findViewById( R.id.sse_je_inspectionsCheckBox );
            InspectionRecords = (CheckBox) findViewById( R.id.inspection_maintenanceCheckBox );
            OtherElectricalMechanicalParameter = (CheckBox) findViewById( R.id.other_electrical_and_mechanicalCheckBox );
            anyDeficiencyFound = (EditText) findViewById( R.id.levelCrossingDeficiencyEditText );
            levelCrossingActionBySpr = (Spinner) findViewById( R.id.levelCrossingActionBySpinner );
            levelCrossingActionByEditTxt = (EditText) findViewById( R.id.levelCrossingActionByEditText );
        }

        public void saveDataForDefaultGateInSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean( "levelActivityComplete",isActivityComplete );
            editor.putString( "gateNo",gateNo.getText().toString() );
            editor.putString( "nameOfGateman",nameOfGateman.getText().toString() );
            editor.putString( "gateTypeSpnr",gateTypeSpnr.getSelectedItem().toString() );
            editor.putInt( "gateTypeSpnrPosition",gateTypeSpnr.getSelectedItemPosition() );
            editor.putBoolean(getResources().getString( R.string.positive_boom_locking_tested ), PositiveBoomLocking.isChecked());
            editor.putBoolean(getResources().getString( R.string.booms_were_painted ), BoomsPainted.isChecked());
            editor.putBoolean(getResources().getString( R.string.gateman_having_adequate_safety_knowledge ), GatemanSafetyKnowledge.isChecked());
            editor.putBoolean(getResources().getString( R.string.gate_telephone_s_found_in_working_order ), GateTelephone.isChecked());
            editor.putBoolean(getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule ), SSEInspections.isChecked());
            editor.putBoolean(getResources().getString( R.string.inspection_maintenance_records_were_maintained ), InspectionRecords.isChecked());
            editor.putBoolean(getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked ), OtherElectricalMechanicalParameter.isChecked());
            editor.putString( "anyDeficiencyFound",anyDeficiencyFound.getText().toString() );
            editor.putString( "levelCrossingActionBySpr",levelCrossingActionBySpr.getSelectedItem().toString() );
            editor.putInt( "levelCrossingActionBySprPosition",levelCrossingActionBySpr.getSelectedItemPosition() );
            editor.putString( "levelCrossingActionByEditTxt",levelCrossingActionByEditTxt.getText().toString() );
            editor.putInt( "levelCrossingGateCount",gateCount );
            editor.apply();
        }

        private void getSavedDataFromSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            gateNo.setText( sharedpreferences.getString( "gateNo",null ) );
            nameOfGateman.setText( sharedpreferences.getString( "nameOfGateman",null ) );
            gateTypeSpnr.setSelection( sharedpreferences.getInt( "gateTypeSpnrPosition",0 )  );
            PositiveBoomLocking.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.positive_boom_locking_tested ), false ) );
            BoomsPainted.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.booms_were_painted ), false ) );
            GatemanSafetyKnowledge.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.gateman_having_adequate_safety_knowledge ), false ) );
            GateTelephone.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.gate_telephone_s_found_in_working_order ), false ) );
            SSEInspections.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule ), false ) );
            InspectionRecords.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.inspection_maintenance_records_were_maintained ), false ) );
            OtherElectricalMechanicalParameter.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked ), false ) );
            anyDeficiencyFound.setText( sharedpreferences.getString( "anyDeficiencyFound",null ) );
            levelCrossingActionByEditTxt.setText( sharedpreferences.getString( "levelCrossingActionByEditTxt",null ) );
        }

        private  boolean validateDefaultGate(){
            Boolean isAnyFieldsEmpty = false;
            if(gateNo.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                gateNo.setError( "Invalid Input" );
            }
            if(nameOfGateman.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                nameOfGateman.setError( "Invalid Input" );
            }
            if(levelCrossingActionBySpinnerVal.equals( "Other" )) {
                if (levelCrossingActionByEditTxt.getText().toString().isEmpty()) {
                    isAnyFieldsEmpty = true;
                    levelCrossingActionByEditTxt.setError( "Invalid Input" );
                }
            }
            return !isAnyFieldsEmpty;
        }
    }

    private class setUpStorageVariablesForAddedGate{
        int countOfGate;
        EditText gateNoForReplicate, nameOfGatemanForReplicate,anyDeficiencyFoundForReplicate,levelCrossingActionByEditTxtForReplicate;
        String levelCrossingActionBySpinnerValForReplicate, gateTypeSpinnerValForReplicate;
        Spinner gateTypeSpnrForReplicate, levelCrossingActionBySprForReplicate;
        CheckBox PositiveBoomLockingForReplicate,BoomsPaintedForReplicate,GatemanSafetyKnowledgeForReplicate, GateTelephoneForReplicate, SSEInspectionsForReplicate, InspectionRecordsForReplicate, OtherElectricalMechanicalParameterForReplicate;

        public setUpStorageVariablesForAddedGate( View myLayout, int countGate ){
            initialUISetup(myLayout);
            countOfGate = countGate;
            getSavedDataFromSharedPreferences();
            ////////////////////////////////////////////////////////////////////////////////////////////////////
            //Item Selected Validation
            gateNoForReplicate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        if(TextUtils.isEmpty(gateNoForReplicate.getText().toString())){
                            gateNoForReplicate.setError( "Invalid Input" );
                        }
                        else {
//                            Data is entered
                        }
                    }
                    else
                        gateNoForReplicate.setError( null );
                }
            });

            nameOfGatemanForReplicate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        if(!TextUtils.isEmpty(nameOfGatemanForReplicate.getText().toString())){
//                            Data is entered
                        }
                        else {
                            nameOfGatemanForReplicate.setError( "Invalid Input" );
                        }
                    }
                    else
                        nameOfGatemanForReplicate.setError( null );
                }
            });

            levelCrossingActionByEditTxtForReplicate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        if(!TextUtils.isEmpty(levelCrossingActionByEditTxtForReplicate.getText().toString())){
//                            Data is entered
                        }
                        else {
                            levelCrossingActionByEditTxtForReplicate.setError( "Invalid Input" );
                        }
                    }
                    else
                        levelCrossingActionByEditTxtForReplicate.setError( null );
                }
            });
            //////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////
            //Add EditText Box on selecting Other
            setActionBySpinner(selectedDivision,levelCrossingActionBySprForReplicate,sharedpreferences.getInt( "levelCrossingActionBySprForReplicate"+countOfGate,0 ));
            levelCrossingActionBySprForReplicate.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    levelCrossingActionBySpinnerValForReplicate = levelCrossingActionBySprForReplicate.getSelectedItem().toString();
                    if(levelCrossingActionBySpinnerValForReplicate.equals( "Other" )){
                        levelCrossingActionByEditTxtForReplicate.setVisibility( View.VISIBLE );
                    }
                    else{
                        levelCrossingActionByEditTxtForReplicate.setVisibility( View.GONE );
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            } );
            //////////////////////////////////////////////////////////////////////////////////////////////////
        }

        private void initialUISetup(View myLayout){
            gateNoForReplicate = (EditText) myLayout.findViewById( R.id.gateNoEditTextForReplicate );
            nameOfGatemanForReplicate =(EditText) myLayout.findViewById( R.id.SMeditTextForReplicate );
            gateTypeSpnrForReplicate = (Spinner) myLayout.findViewById( R.id.gateTypeSpinnerForReplicate );
            PositiveBoomLockingForReplicate = (CheckBox) myLayout.findViewById( R.id.positive_boomCheckBoxForReplicate );
            BoomsPaintedForReplicate = (CheckBox) myLayout.findViewById( R.id.booms_were_paintedCheckBoxForReplicate );
            GatemanSafetyKnowledgeForReplicate = (CheckBox) myLayout.findViewById( R.id.gatemanCheckBoxForReplicate );
            GateTelephoneForReplicate = (CheckBox) myLayout.findViewById( R.id.gateTelephoneCheckBoxForReplicate );
            SSEInspectionsForReplicate = (CheckBox) myLayout.findViewById( R.id.sse_je_inspectionsCheckBoxForReplicate );
            InspectionRecordsForReplicate = (CheckBox) myLayout.findViewById( R.id.inspection_maintenanceCheckBoxForReplicate );
            OtherElectricalMechanicalParameterForReplicate = (CheckBox) myLayout.findViewById( R.id.other_electrical_and_mechanicalCheckBoxForReplicate );
            anyDeficiencyFoundForReplicate = (EditText) myLayout.findViewById( R.id.levelCrossingDeficiencyEditTextForReplicate );
            levelCrossingActionBySprForReplicate = (Spinner) myLayout.findViewById( R.id.levelCrossingActionBySpinnerForReplicate );
            levelCrossingActionByEditTxtForReplicate = (EditText) myLayout.findViewById( R.id.levelCrossingActionByEditTextForReplicate );
        }

        private  boolean validateAddGate(){
            Boolean isAnyFieldsEmpty = false;                                           //no field is empty. All are filled
            if(gateNoForReplicate.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                gateNoForReplicate.setError( "Invalid Input" );
            }
            if(nameOfGatemanForReplicate.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                nameOfGatemanForReplicate.setError( "Invalid Input" );
            }
            if(levelCrossingActionBySpinnerValForReplicate.equals( "Other" )) {
                if (levelCrossingActionByEditTxtForReplicate.getText().toString().isEmpty()) {
                    isAnyFieldsEmpty = true;
                    levelCrossingActionByEditTxtForReplicate.setError( "Invalid Input" );
                }
            }
            return isAnyFieldsEmpty;
        }

        public void saveDataForAddedGateInSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString( "gateNo"+countOfGate,gateNoForReplicate.getText().toString() );
            editor.putString( "nameOfGateman"+countOfGate,nameOfGatemanForReplicate.getText().toString() );
            editor.putString( "gateTypeSpnr"+countOfGate,gateTypeSpnrForReplicate.getSelectedItem().toString() );
            editor.putInt( "gateTypeSpnrForReplicatePosition"+countOfGate,gateTypeSpnrForReplicate.getSelectedItemPosition() );
            editor.putBoolean(getResources().getString( R.string.positive_boom_locking_tested )+countOfGate, PositiveBoomLockingForReplicate.isChecked());
            editor.putBoolean(getResources().getString( R.string.booms_were_painted )+countOfGate, BoomsPaintedForReplicate.isChecked());
            editor.putBoolean(getResources().getString( R.string.gateman_having_adequate_safety_knowledge )+countOfGate, GatemanSafetyKnowledgeForReplicate.isChecked());
            editor.putBoolean(getResources().getString( R.string.gate_telephone_s_found_in_working_order )+countOfGate, GateTelephoneForReplicate.isChecked());
            editor.putBoolean(getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule )+countOfGate, SSEInspectionsForReplicate.isChecked());
            editor.putBoolean(getResources().getString( R.string.inspection_maintenance_records_were_maintained )+countOfGate, InspectionRecordsForReplicate.isChecked());
            editor.putBoolean(getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked )+countOfGate, OtherElectricalMechanicalParameterForReplicate.isChecked());
            editor.putString( "anyDeficiencyFound"+countOfGate,anyDeficiencyFoundForReplicate.getText().toString() );
            editor.putString( "levelCrossingActionBySpr"+countOfGate,levelCrossingActionBySprForReplicate.getSelectedItem().toString() );
            editor.putInt( "levelCrossingActionBySprForReplicate"+countOfGate,levelCrossingActionBySprForReplicate.getSelectedItemPosition() );
            editor.putString( "levelCrossingActionByEditTxt"+countOfGate,levelCrossingActionByEditTxtForReplicate.getText().toString() );
            editor.apply();
        }

        private void getSavedDataFromSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            gateNoForReplicate.setText( sharedpreferences.getString( "gateNo"+countOfGate,null ) );
            nameOfGatemanForReplicate.setText( sharedpreferences.getString( "nameOfGateman"+countOfGate,null ) );
            gateTypeSpnrForReplicate.setSelection( sharedpreferences.getInt( "gateTypeSpnrForReplicatePosition"+countOfGate,0 )  );
            PositiveBoomLockingForReplicate.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.positive_boom_locking_tested )+countOfGate, false ) );
            BoomsPaintedForReplicate.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.booms_were_painted )+countOfGate, false ) );
            GatemanSafetyKnowledgeForReplicate.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.gateman_having_adequate_safety_knowledge )+countOfGate, false ) );
            GateTelephoneForReplicate.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.gate_telephone_s_found_in_working_order )+countOfGate, false ) );
            SSEInspectionsForReplicate.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule )+countOfGate, false ) );
            InspectionRecordsForReplicate.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.inspection_maintenance_records_were_maintained )+countOfGate, false ) );
            OtherElectricalMechanicalParameterForReplicate.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked )+countOfGate, false ) );
            anyDeficiencyFoundForReplicate.setText( sharedpreferences.getString( "anyDeficiencyFound"+countOfGate,null ) );
            levelCrossingActionByEditTxtForReplicate.setText( sharedpreferences.getString( "levelCrossingActionByEditTxt"+countOfGate,null ) );
        }
    }

    private class sendDataToDBTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        int success = 0;
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog( LevelCrossingActivity.this );
            pd.setMessage( "Submitting... " );
            pd.setCancelable( false );
            pd.setIndeterminate( true );
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Boolean dataSent = null;
            try {
                dataSent = sendLevelCrossingData();
//                SmsHandler smsHandler = new SmsHandler( LevelCrossingActivity.this );
//                FileUploadHandler fileUploadHandler = new FileUploadHandler(LevelCrossingActivity.this);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dataSent) {
                success =1;
            } else {
                success = 0;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pd != null) {
                pd.dismiss();
                levelCrossingGenerateInspectionNoteButton.setEnabled( true );
                if(success==0)
                    showAlertDialog( "Couldn't send the data. Please check your internet connection!" );
                else{
                    generatePdf generate = new generatePdf();
                    generate.execute(  );
                }
            }
        }
    }

    private boolean sendLevelCrossingData() throws SQLException {
        GetData getData = new GetData();
        int lcid;
        String ActionBy = "";
        ActionBy = getActionBy( sharedpreferences.getString( "levelCrossingActionBySpr","" ), sharedpreferences.getString( "levelCrossingActionByEditTxt","" ) );

        lcid = getData.levelCrossingInsertionQuery(
                sharedpreferences.getString( "gateNo","" ),
                sharedpreferences.getString( "nameOfGateman","" ),
                sharedpreferences.getString( "gateTypeSpnr","" ),
                sharedpreferences.getBoolean( getResources().getString( R.string.positive_boom_locking_tested ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.booms_were_painted ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.gateman_having_adequate_safety_knowledge ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.gate_telephone_s_found_in_working_order ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.inspection_maintenance_records_were_maintained ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked ),false ),
                sharedpreferences.getString( "anyDeficiencyFound","" ),
                ActionBy
        );
//        lc +=lcid;
        Log.e("lcid", "lcid = "+lcid);
        for(int gateNumber = 1; gateNumber <= sharedpreferences.getInt("levelCrossingGateCount", 0) ; gateNumber++ ){
            ActionBy = getActionBy( sharedpreferences.getString( "levelCrossingActionBySpr"+gateNumber,"" ), sharedpreferences.getString( "levelCrossingActionByEditTxt"+gateNumber,"" ) );

            lcid = getData.levelCrossingInsertionQuery(
                    sharedpreferences.getString( "gateNo"+gateNumber,"" ),
                    sharedpreferences.getString( "nameOfGateman"+gateNumber,"" ),
                    sharedpreferences.getString( "gateTypeSpnr"+gateNumber,"" ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.positive_boom_locking_tested )+gateNumber,false ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.booms_were_painted )+gateNumber,false ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.gateman_having_adequate_safety_knowledge )+gateNumber,false ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.gate_telephone_s_found_in_working_order )+gateNumber,false ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule )+gateNumber,false ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.inspection_maintenance_records_were_maintained )+gateNumber,false ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked )+gateNumber,false ),
                    sharedpreferences.getString( "anyDeficiencyFound"+gateNumber,"" ),
                    ActionBy
            );
//            lc +=","+lcid;
            Log.e("lcid", "lcid = "+lcid);
        }
//        Log.e("lc", "lc = "+lc);
        return lcid > 0;
    }

    private String getActionBy (String s1, String s2){
        if(s1.equals( "Other" ))
            return s2;
        else
            return s1;
    }

    private void showAlertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( LevelCrossingActivity.this );
        alertDialogBuilder.setMessage( msg );
        alertDialogBuilder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
//                Toast.makeText( LoginActivity.this, "You clicked ok button", Toast.LENGTH_LONG ).show();
            }
        } );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class generatePdf extends AsyncTask<Void, Void, Void> {
        int success = 0;
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( LevelCrossingActivity.this );
            progressDialog.setMessage( "Generating Inspection Note... " );
            progressDialog.setCancelable( false );
            progressDialog.setIndeterminate( true );
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (isStoragePermissionGranted() ) {
                Looper.prepare();
                lcInspectionNotes = new LCInspectionNotes( LevelCrossingActivity.this );
//                lcInspectionNotes.uploadPdf();
//                lcInspectionNotes.previewPdf();
//                removeSharedPreference();
                success =1;
            } else {
                success = 0;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                levelCrossingGenerateInspectionNoteButton.setEnabled( true );
                if(success==0)
                    showAlertDialog( "Couldn't generate Inspection Note. Please review Storage Permission!" );
                else{
                    UploadGeneratedPdf uploadGeneratedPdf = new UploadGeneratedPdf();
                    uploadGeneratedPdf.execute(  );
                }
            }
        }
    }

    private class UploadGeneratedPdf extends AsyncTask<Void, Void, Void> {
        int success = 0;
        int dataUpload = -3;
        private ProgressDialog progressDialog;
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( LevelCrossingActivity.this );
            progressDialog.setMessage( "Uploading Inspection Note... " );
            progressDialog.setCancelable( false );
            progressDialog.setIndeterminate( true );
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            GetData getData = new GetData();
            if (isStoragePermissionGranted() ) {
                Looper.prepare();
                lcInspectionNotes.uploadPdf();
                dataUpload = getData.inspectionNoteInsertionQuery(lcInspectionNotes.returnFileName(),sharedpreferences.getString("authDesignation", ""),sharedpreferences.getString("stationCode", ""),lcInspectionNotes.returnDateTime(),sharedpreferences.getString("division", ""));
                success =1;
            } else {
                success = 0;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                levelCrossingGenerateInspectionNoteButton.setEnabled( true );
                if(dataUpload==-2 || success==0)
                    showAlertDialog( "Couldn't upload the Inspection Note. Please check your internet connection!" );
                else if(dataUpload==-1)
                    showAlertDialog( "Could not Connect to server. Try again after sometime" );
                else{
                    lcInspectionNotes.previewPdf();
                    removeSharedPreference();
                }
            }
        }
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(LevelCrossingActivity.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(LevelCrossingActivity.this,android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG,"Permission is granted");
                return true;
            } else {

                Log.e(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(LevelCrossingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(LevelCrossingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.e(TAG,"Permission is granted");
            return true;
        }
    }


}
