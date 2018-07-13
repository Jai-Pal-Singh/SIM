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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hp on 2018-04-02.
 */

public class SignalsActivity extends AppCompatActivity {

    CheckBox signalLampCheckBox,VECRDropsCheckBox, signalCascadedCheckBox,redLampCheckBox;
    private String selectedDivision="", stationCode="";
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;
    Spinner signalsActionBySpinner;
    EditText signalsActionByEditText;
    Button addRowBtn, removeRowBtn;
    int rowCount = 0, maxRowCount = 100, minRowCount =0;
    SignalTableRow [] addedRowSetUp = new SignalTableRow[100];
    Boolean isActivityComplete;
    EditText signalNoEditText0,aspectEditText0,voltageEditText0,currentEditText0;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signals_activity);
        initialUISetup();
        getDivisionFromSharedPreferences();
        getSavedDataFromSharedPreferences();
        isActivityComplete=false;
        initialRows(addRowBtn);

        applyOnFocusChangeListener( signalsActionByEditText );
        setActionBySpinner(selectedDivision,signalsActionBySpinner,sharedpreferences.getInt( "signalsActionBySpinnerPosition",0));
        applyOnItemSelectedListener(signalsActionBySpinner,signalsActionByEditText);

        addRowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRow(view);
            }
        });
        removeRowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRow(view);
            }
        });
    }

    private void removeRow(View view) {
        View myLayout = findViewById( rowCount );
        LinearLayout mainLayout = (LinearLayout) myLayout.getParent();
        mainLayout.removeView(myLayout);
        removeSharedPreferencesForRemovedRows( rowCount );
        rowCount--;

        if(rowCount<=minRowCount){
            removeRowBtn.setVisibility( view.INVISIBLE );
        }
        else{
            removeRowBtn.setVisibility( view.VISIBLE );
        }
        if(rowCount>=maxRowCount){
            addRowBtn.setVisibility( view.INVISIBLE );
        }
        else{
            addRowBtn.setVisibility( view.VISIBLE );
        }
    }

    private void addRow(View view) {
        rowCount++;

        if(rowCount<=minRowCount){
            removeRowBtn.setVisibility( view.INVISIBLE );
        }
        else{
            removeRowBtn.setVisibility( view.VISIBLE );
        }

        if(rowCount>=maxRowCount){
            addRowBtn.setVisibility( view.INVISIBLE );
        }
        else{
            addRowBtn.setVisibility( view.VISIBLE );
        }
        // get a reference to the already created main layout
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.signalsTableAdditionalRows);
        // inflate (create) another copy of our custom layout
        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.signals_row_replicate_activity, mainLayout, false);
        // add our custom layout to the main layout
        mainLayout.addView(myLayout);
        myLayout.setId( rowCount );
        addedRowSetUp[rowCount] = new SignalTableRow( myLayout , rowCount);
        addedRowSetUp[rowCount].getSavedRowDataFromSharedPreferences();
    }

    private void initialUISetup(){
        signalLampCheckBox = findViewById( R.id.signalLampCheckBox );
        VECRDropsCheckBox = findViewById( R.id.VECRDropsCheckBox );
        signalCascadedCheckBox = findViewById( R.id.signalCascadedCheckBox );
        redLampCheckBox = findViewById( R.id.redLampCheckBox );
        signalNoEditText0 = findViewById( R.id.signalNoEditText0 );
        aspectEditText0 = findViewById( R.id.aspectEditText0 );
        voltageEditText0 = findViewById( R.id.voltageEditText0 );
        currentEditText0 = findViewById( R.id.currentEditText0 );
        addRowBtn  = findViewById( R.id.addSignalTableRowButton );
        removeRowBtn  = findViewById( R.id.removeSignalTableRowButton );
        signalsActionBySpinner  = findViewById( R.id.signalsActionBySpinner );
        signalsActionByEditText  = findViewById( R.id.signalsActionByEditText );
    }

    private void initialRows(View view) {
        rowCount = sharedpreferences.getInt( "signalTableRowCount", 0 );
        for (int gate = 1; gate <= rowCount; gate++) {
            if(gate<=minRowCount){
                removeRowBtn.setVisibility( view.INVISIBLE );
            }
            else{
                removeRowBtn.setVisibility( view.VISIBLE );
            }

            if(gate>=maxRowCount){
                addRowBtn.setVisibility( view.INVISIBLE );
            }
            else{
                addRowBtn.setVisibility( view.VISIBLE );
            }
            // get a reference to the already created main layout
            LinearLayout mainLayout = (LinearLayout) findViewById(R.id.signalsTableAdditionalRows);
            // inflate (create) another copy of our custom layout
            LayoutInflater inflater = getLayoutInflater();
            View myLayout = inflater.inflate(R.layout.signals_row_replicate_activity, mainLayout, false);
            // add our custom layout to the main layout
            mainLayout.addView(myLayout);
            myLayout.setId( rowCount );
            addedRowSetUp[rowCount] = new SignalTableRow( myLayout , rowCount);
            addedRowSetUp[rowCount].getSavedRowDataFromSharedPreferences();
        }
    }

    private void removeSharedPreferencesForRemovedRows(int gateNumber) {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.remove( "signalNoEditText"+gateNumber );
        editor.remove( "aspectEditText"+gateNumber );
        editor.remove( "voltageEditText"+gateNumber );
        editor.remove( "currentEditText"+gateNumber );
        editor.putInt( "signalTableRowCount",rowCount );

        editor.apply();
    }

    private void getDivisionFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        selectedDivision = sharedpreferences.getString("division", null);
        stationCode = sharedpreferences.getString( "stationCode",null );
    }

    public void saveSignalsData(View view) {
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
        editor.putBoolean( "signalsActivityComplete",isActivityComplete );
        editor.apply();
    }

    private void saveDataInSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean( "signalsActivityComplete",isActivityComplete );
        editor.putBoolean(getResources().getString( R.string.signal_lamp_voltae_90_of_rated_value ), signalLampCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.vecr_drops_with_fusing_of_minimum_3_route_leds ), VECRDropsCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.signals_are_cascaded_for_e_g_fusing_of_a_signal_s_particular_aspect_other_than_red_illuminates_a_more_restrictive_aspect ), signalCascadedCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.red_lamp_protection_working_e_g_fusing_of_red_aspect_of_signal_should_illuminate_signal_in_rear_with_most_restrictive_aspect_red ), redLampCheckBox.isChecked());
        editor.putString( "signalNoEditText0",signalNoEditText0.getText().toString() );
        editor.putString( "aspectEditText0",aspectEditText0.getText().toString() );
        editor.putString( "voltageEditText0",voltageEditText0.getText().toString() );
        editor.putString( "currentEditText0",currentEditText0.getText().toString() );
        for(int gateNumber = 1; gateNumber <= rowCount ; gateNumber++ ){
            addedRowSetUp[gateNumber].saveDataForAddedRowInSharedPreferences();
        }
        editor.putString( "signalsActionBySpinner",signalsActionBySpinner.getSelectedItem().toString() );
        editor.putInt( "signalsActionBySpinnerPosition",signalsActionBySpinner.getSelectedItemPosition() );
        editor.putString( "signalsActionByEditText",signalsActionByEditText.getText().toString() );
        editor.putInt( "signalTableRowCount",rowCount );
        editor.apply();
    }

    private void getSavedDataFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        signalLampCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.signal_lamp_voltae_90_of_rated_value ), false ) );
        VECRDropsCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.vecr_drops_with_fusing_of_minimum_3_route_leds ), false ) );
        signalCascadedCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.signals_are_cascaded_for_e_g_fusing_of_a_signal_s_particular_aspect_other_than_red_illuminates_a_more_restrictive_aspect ), false ) );
        redLampCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.red_lamp_protection_working_e_g_fusing_of_red_aspect_of_signal_should_illuminate_signal_in_rear_with_most_restrictive_aspect_red ), false ) );
        signalNoEditText0.setText( sharedpreferences.getString("signalNoEditText0", null) );
        aspectEditText0.setText( sharedpreferences.getString("aspectEditText0", null) );
        voltageEditText0.setText( sharedpreferences.getString("voltageEditText0", null) );
        currentEditText0.setText( sharedpreferences.getString("currentEditText0", null) );
        signalsActionByEditText.setText( sharedpreferences.getString( "signalsActionByEditText",null ) );
    }


    private boolean validateUserInput() {
        Boolean isAnyFieldsEmpty = false;
        if(signalsActionBySpinner.getSelectedItem().toString().equals( "Other" )) {
            if (signalsActionByEditText.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                signalsActionByEditText.setError( "Invalid Input" );
            }
        }
        if(signalNoEditText0.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalNoEditText0.setError( "Invalid Input" );
        }
        if(aspectEditText0.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            aspectEditText0.setError( "Invalid Input" );
        }
        if(voltageEditText0.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            voltageEditText0.setError( "Invalid Input" );
        }
        if(currentEditText0.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            currentEditText0.setError( "Invalid Input" );
        }
        Boolean isAnyAddedRowEmpty = false;
        for(int gateNumber = 1; gateNumber <= rowCount ; gateNumber++ ){
            if(addedRowSetUp[gateNumber].validateAddGate() )
                isAnyAddedRowEmpty = true;
        }
        return !isAnyFieldsEmpty&& !isAnyAddedRowEmpty;
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
        String[] tempArray = new String[ stringArray.length + 3 ];
        tempArray[0] = "None";
        tempArray[1] = firstElement;
        tempArray[2] = secondElement;
        System.arraycopy( stringArray, 0, tempArray, 3, stringArray.length );
        return tempArray;
    }

    private void applyOnItemSelectedListener(final Spinner signalsActionBySpr, final EditText signalsActionByEditTxt) {
        signalsActionBySpr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = signalsActionBySpr.getSelectedItem().toString();
                if(s.equals( "Other" )){
                    signalsActionByEditTxt.setVisibility( View.VISIBLE );
                }
                else{
                    signalsActionByEditTxt.setVisibility( View.GONE );
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
                }
                else
                    inputEditTxt.setError( null );
            }
        });
    }

    public class SignalTableRow{
        int rowCount;
        EditText signalNoEditText,aspectEditText,voltageEditText,currentEditText;

        public SignalTableRow( View myLayout, int countRow ) {
            initialUISetupForAddedRow( myLayout );
            rowCount = countRow;
            getSavedRowDataFromSharedPreferences();

            applyOnFocusChangeListener( signalNoEditText );
            applyOnFocusChangeListener( aspectEditText );
            applyOnFocusChangeListener( voltageEditText );
            applyOnFocusChangeListener( currentEditText );
        }

        private void initialUISetupForAddedRow(View myLayout) {
            signalNoEditText = myLayout.findViewById( R.id.signalNoEditText );
            aspectEditText = myLayout.findViewById( R.id.aspectEditText );
            voltageEditText = myLayout.findViewById( R.id.voltageEditText );
            currentEditText = myLayout.findViewById( R.id.currentEditText );
        }

        public void saveDataForAddedRowInSharedPreferences() {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString( "signalNoEditText"+rowCount,signalNoEditText.getText().toString() );
            editor.putString( "aspectEditText"+rowCount,aspectEditText.getText().toString() );
            editor.putString( "voltageEditText"+rowCount,voltageEditText.getText().toString() );
            editor.putString( "currentEditText"+rowCount,currentEditText.getText().toString() );

            editor.apply();
        }

        public void getSavedRowDataFromSharedPreferences(){
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            signalNoEditText.setText( sharedpreferences.getString("signalNoEditText"+rowCount, null) );
            aspectEditText.setText( sharedpreferences.getString("aspectEditText"+rowCount, null) );
            voltageEditText.setText( sharedpreferences.getString("voltageEditText"+rowCount, null) );
            currentEditText.setText( sharedpreferences.getString("currentEditText"+rowCount, null) );
        }

        private  boolean validateAddGate(){
            Boolean isAnyFieldsEmpty = false;                          //no field is empty. All are filled
            if(signalNoEditText.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                signalNoEditText.setError( "Invalid Input" );
            }
            if(aspectEditText.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                aspectEditText.setError( "Invalid Input" );
            }
            if(voltageEditText.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                voltageEditText.setError( "Invalid Input" );
            }
            if(currentEditText.getText().toString().isEmpty()){
                isAnyFieldsEmpty = true;
                currentEditText.setError( "Invalid Input" );
            }
            return isAnyFieldsEmpty;
        }
    }
}
