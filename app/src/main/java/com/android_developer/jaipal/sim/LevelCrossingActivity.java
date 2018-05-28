package com.android_developer.jaipal.sim;

import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelCrossingActivity extends AppCompatActivity {

    private Button addGateBtn, removeGateBtn;
    int gateCount = 0, maxGateCount = 9, minGateCount = 0;
    private String selectedDivision="", stationCode="";
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;
    setUpStorageVariablesForDefaultGate defaultGateSetUp;
    setUpStorageVariablesForAddedGate[] addedGateSetUp = new setUpStorageVariablesForAddedGate[10];
    Boolean isActivityComplete;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_crossing_activity);
        getDivisionFromSharedPreferences();
        isActivityComplete = false;

        defaultGateSetUp = new setUpStorageVariablesForDefaultGate();
        defaultGateSetUp.getSavedDataFromSharedPreferences();
        //addGateBtn onclick
        addGateBtn = (Button)findViewById(R.id.addGateButton);
        removeGateBtn = (Button)findViewById(R.id.removeGateButton);
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
            setActionBySpinner(selectedDivision,levelCrossingActionBySpr);
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
            editor.putBoolean(getResources().getString( R.string.positive_boom_locking_tested ), PositiveBoomLocking.isChecked());
            editor.putBoolean(getResources().getString( R.string.booms_were_painted ), BoomsPainted.isChecked());
            editor.putBoolean(getResources().getString( R.string.gateman_having_adequate_safety_knowledge ), GatemanSafetyKnowledge.isChecked());
            editor.putBoolean(getResources().getString( R.string.gate_telephone_s_found_in_working_order ), GateTelephone.isChecked());
            editor.putBoolean(getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule ), SSEInspections.isChecked());
            editor.putBoolean(getResources().getString( R.string.inspection_maintenance_records_were_maintained ), InspectionRecords.isChecked());
            editor.putBoolean(getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked ), OtherElectricalMechanicalParameter.isChecked());
            editor.putString( "anyDeficiencyFound",anyDeficiencyFound.getText().toString() );
            editor.putString( "levelCrossingActionBySpr",levelCrossingActionBySpr.getSelectedItem().toString() );
            editor.putString( "levelCrossingActionByEditTxt",levelCrossingActionByEditTxt.getText().toString() );
            editor.putInt( "levelCrossingGateCount",gateCount );
            editor.apply();
        }

        private void getSavedDataFromSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            gateNo.setText( sharedpreferences.getString( "gateNo",null ) );
            nameOfGateman.setText( sharedpreferences.getString( "nameOfGateman",null ) );
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
            setActionBySpinner(selectedDivision,levelCrossingActionBySprForReplicate);
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
            editor.putBoolean(getResources().getString( R.string.positive_boom_locking_tested )+countOfGate, PositiveBoomLockingForReplicate.isChecked());
            editor.putBoolean(getResources().getString( R.string.booms_were_painted )+countOfGate, BoomsPaintedForReplicate.isChecked());
            editor.putBoolean(getResources().getString( R.string.gateman_having_adequate_safety_knowledge )+countOfGate, GatemanSafetyKnowledgeForReplicate.isChecked());
            editor.putBoolean(getResources().getString( R.string.gate_telephone_s_found_in_working_order )+countOfGate, GateTelephoneForReplicate.isChecked());
            editor.putBoolean(getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule )+countOfGate, SSEInspectionsForReplicate.isChecked());
            editor.putBoolean(getResources().getString( R.string.inspection_maintenance_records_were_maintained )+countOfGate, InspectionRecordsForReplicate.isChecked());
            editor.putBoolean(getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked )+countOfGate, OtherElectricalMechanicalParameterForReplicate.isChecked());
            editor.putString( "anyDeficiencyFound"+countOfGate,anyDeficiencyFoundForReplicate.getText().toString() );
            editor.putString( "levelCrossingActionBySpr"+countOfGate,levelCrossingActionBySprForReplicate.getSelectedItem().toString() );
            editor.putString( "levelCrossingActionByEditTxt"+countOfGate,levelCrossingActionByEditTxtForReplicate.getText().toString() );
            editor.apply();
        }

        private void getSavedDataFromSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            gateNoForReplicate.setText( sharedpreferences.getString( "gateNo"+countOfGate,null ) );
            nameOfGatemanForReplicate.setText( sharedpreferences.getString( "nameOfGateman"+countOfGate,null ) );
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
}
