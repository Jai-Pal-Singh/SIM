package com.android_developer.jaipal.sim;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by hp on 2018-04-02.
 */

public class CCIPActivity extends AppCompatActivity implements View.OnClickListener{

    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    private String selectedDivision="", stationCode="";
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;
    public Spinner ccipActionBySpr, typeOfInterlockingSpinner;
    public EditText ccipDeficiencyEditText, ERRBEditText, RRBUEditText, COGGNEditText, COCYNEditText, EBPUEditText, ECHEditText, lastAnnualDateEditText, ccipActionByEditTxt;
    public CheckBox SIPCheckBox, countersCheckBox, sampleCheckingCheckBox, sampleCheckingCancellationCheckBox, testingEmergencyCheckBox, sampleCheckLockCheckBox;
    Boolean isActivityComplete ;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.ccip_activity );
        getDivisionFromSharedPreferences();

        //Date formatter
        dateFormatter = new SimpleDateFormat( "dd-MM-yyyy", Locale.US );
        findViewsById();
        setDateTimeField();
        getSavedDataFromSharedPreferences();
        isActivityComplete = false;

        applyOnFocusChangeListener( ERRBEditText );
        applyOnFocusChangeListener( RRBUEditText );
        applyOnFocusChangeListener( COGGNEditText );
        applyOnFocusChangeListener( COCYNEditText );
        applyOnFocusChangeListener( EBPUEditText );
        applyOnFocusChangeListener( ECHEditText );
        applyOnFocusChangeListenerForDate( lastAnnualDateEditText );
        applyOnFocusChangeListener( ccipActionByEditTxt );

        setActionBySpinner(selectedDivision,ccipActionBySpr,sharedpreferences.getInt( "ccipActionBySprPosition",0));
        applyOnItemSelectedListener(ccipActionBySpr,ccipActionByEditTxt);
    }

    private void getDivisionFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        selectedDivision = sharedpreferences.getString("division", null);
        stationCode = sharedpreferences.getString( "stationCode",null );
    }

    @Override
    public void onClick(View view) {
        datePicker.show();
    }

    private void findViewsById() {
        lastAnnualDateEditText = findViewById(R.id.lastAnnualDateEditText);
        lastAnnualDateEditText.setInputType( InputType.TYPE_NULL);

        ccipDeficiencyEditText = findViewById(R.id.ccipDeficiencyEditText);
        ERRBEditText = findViewById(R.id.ERRBEditText);
        RRBUEditText = findViewById(R.id.RRBUEditText);
        COGGNEditText = findViewById(R.id.COGGNEditText);
        COCYNEditText = findViewById(R.id.COCYNEditText);
        EBPUEditText = findViewById(R.id.EBPUEditText);
        ECHEditText = findViewById(R.id.ECHEditText);
        ccipActionByEditTxt = findViewById(R.id.ccipActionByEditText);
        SIPCheckBox = findViewById(R.id.SIPCheckBox);
        countersCheckBox = findViewById(R.id.countersCheckBox);
        sampleCheckingCheckBox = findViewById(R.id.sampleCheckingCheckBox);
        sampleCheckingCancellationCheckBox = findViewById(R.id.sampleCheckingCancellationCheckBox);
        testingEmergencyCheckBox = findViewById(R.id.testingEmergencyCheckBox);
        sampleCheckLockCheckBox = findViewById(R.id.sampleCheckLockCheckBox);
        ccipActionBySpr = findViewById( R.id.ccipActionBySpinner );
        typeOfInterlockingSpinner = findViewById( R.id.typeOfInterlockingSpinner );
    }

    private void saveDataInSharedPreferences() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean( "ccipActivityComplete",isActivityComplete );
        editor.putString( "typeOfInterlockingSpinner",typeOfInterlockingSpinner.getSelectedItem().toString() );
        editor.putInt( "typeOfInterlockingSpinnerPosition",typeOfInterlockingSpinner.getSelectedItemPosition() );
        editor.putBoolean(getResources().getString( R.string.sip_swrd_is_as_per_the_physical_yard_layout ), SIPCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.counters_on_panel_vdu_are_same_as_recorded_in_counter_register ), countersCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.sample_checking_of_calling_on_signal_coggb_counter_increment ), sampleCheckingCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.sample_checking_of_calling_on_signal_cancellation_cocyn_errb_counter_increment ), sampleCheckingCancellationCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.testing_of_emergency_classover_in_double_line_sections ), testingEmergencyCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.sample_check_of_approach_locking_dead_approach_locking ), sampleCheckLockCheckBox.isChecked());
        editor.putString( "ccipDeficiencyEditText", ccipDeficiencyEditText.getText().toString() );
        editor.putString( "ERRBEditText", ERRBEditText.getText().toString() );
        editor.putString( "RRBUEditText", RRBUEditText.getText().toString() );
        editor.putString( "COGGNEditText", COGGNEditText.getText().toString() );
        editor.putString( "COCYNEditText", COCYNEditText.getText().toString() );
        editor.putString( "EBPUEditText", EBPUEditText.getText().toString() );
        editor.putString( "ECHEditText", ECHEditText.getText().toString() );
        editor.putString( "lastAnnualDateEditText", lastAnnualDateEditText.getText().toString() );
        editor.putString( "ccipActionBySpr",ccipActionBySpr.getSelectedItem().toString() );
        editor.putInt( "ccipActionBySprPosition",ccipActionBySpr.getSelectedItemPosition() );
        editor.putString( "ccipActionByEditTxt",ccipActionByEditTxt.getText().toString() );
        editor.apply();
    }

    private void getSavedDataFromSharedPreferences() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        typeOfInterlockingSpinner.setSelection( sharedpreferences.getInt( "typeOfInterlockingSpinnerPosition",0) );
        SIPCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.sip_swrd_is_as_per_the_physical_yard_layout ), false ) );
        countersCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.counters_on_panel_vdu_are_same_as_recorded_in_counter_register ), false ) );
        sampleCheckingCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.sample_checking_of_calling_on_signal_coggb_counter_increment ), false ) );
        sampleCheckingCancellationCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.sample_checking_of_calling_on_signal_cancellation_cocyn_errb_counter_increment ), false ) );
        testingEmergencyCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.testing_of_emergency_classover_in_double_line_sections ), false ) );
        sampleCheckLockCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.sample_check_of_approach_locking_dead_approach_locking ), false ) );
        ccipDeficiencyEditText.setText( sharedpreferences.getString("ccipDeficiencyEditText", null) );
        ERRBEditText.setText( sharedpreferences.getString("ERRBEditText", null) );
        RRBUEditText.setText( sharedpreferences.getString("RRBUEditText", null) );
        COGGNEditText.setText( sharedpreferences.getString("COGGNEditText", null) );
        COCYNEditText.setText( sharedpreferences.getString("COCYNEditText", null) );
        EBPUEditText.setText( sharedpreferences.getString("EBPUEditText", null) );
        ECHEditText.setText( sharedpreferences.getString("ECHEditText", null) );
        lastAnnualDateEditText.setText( sharedpreferences.getString("lastAnnualDateEditText", null) );
        ccipActionByEditTxt.setText( sharedpreferences.getString("ccipActionByEditTxt", null) );
    }

    private void setDateTimeField() {
        lastAnnualDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });

        lastAnnualDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if(TextUtils.isEmpty(lastAnnualDateEditText.getText().toString())){
                        lastAnnualDateEditText.setError( "Invalid Input" );
                    }
                    else
                        lastAnnualDateEditText.setError( null );
                }
                else
                    lastAnnualDateEditText.setError( null );
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                lastAnnualDateEditText.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void applyOnItemSelectedListener(final Spinner ccipActionBySpr, final EditText ccipActionByEditTxt) {
        ccipActionBySpr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = ccipActionBySpr.getSelectedItem().toString();
                if(s.equals( "Other" )){
                    ccipActionByEditTxt.setVisibility( View.VISIBLE );
                }
                else{
                    ccipActionByEditTxt.setVisibility( View.GONE );
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
                    else
                        inputEditTxt.setError( null );
                }
                else
                    inputEditTxt.setError( null );
            }
        });
    }

    private void applyOnFocusChangeListenerForDate(final EditText inputEditTxt) {
        inputEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if(TextUtils.isEmpty(inputEditTxt.getText().toString())){
                        inputEditTxt.requestFocus();
                        inputEditTxt.setError( "Invalid Input" );
                    }
                    else {
                        inputEditTxt.requestFocus();
                        inputEditTxt.setError( null );
                    }
                }
                else{
                    inputEditTxt.requestFocus();
                    inputEditTxt.setError( null );
                }
            }
        });
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

    private  boolean validateUserInput() {
        Boolean isAnyFieldsEmpty = false;
        if(EBPUEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            EBPUEditText.setError( "Invalid Input" );
        }
        if(ERRBEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            ERRBEditText.setError( "Invalid Input" );
        }
        if(RRBUEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            RRBUEditText.setError( "Invalid Input" );
        }
        if(COGGNEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            COGGNEditText.setError( "Invalid Input" );
        }
        if(COCYNEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            COCYNEditText.setError( "Invalid Input" );
        }
        if(ECHEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            ECHEditText.setError( "Invalid Input" );
        }
        if(lastAnnualDateEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            lastAnnualDateEditText.setError( "Invalid Input" );
        }
        if(ccipActionBySpr.getSelectedItem().toString().equals( "Other" )) {
            if (ccipActionByEditTxt.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                ccipActionByEditTxt.setError( "Invalid Input" );
            }
        }
        return !isAnyFieldsEmpty;
    }

    public void saveCcipData(View view) {
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
        editor.putBoolean( "ccipActivityComplete",isActivityComplete );
        editor.apply();
    }
}
