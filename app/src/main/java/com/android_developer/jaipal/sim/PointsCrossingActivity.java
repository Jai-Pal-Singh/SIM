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

public class PointsCrossingActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText selectDateEtxt;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    private String selectedDivision="", stationCode="";
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;
    CheckBox pointOperatedCheckBox, pointStopCheckBox, pointOpeningCheckBox, fillerGaugeCheckBox, pointMaintenanceCheckBox;
    EditText pointsDeficiencyEditText, pointsDetailEditText, obstructionCurrentEditText, lockedPointsEditText, lastJointDeficiencyEditText;
    Spinner pointsCrossingEnsureActionBySpr ,pointsCrossingDetailsActionBySpr,pointsCrossingLastJointActionBySpr;
    EditText pointsCrossingEnsureActionByEditTxt,pointsCrossingDetailsActionByEditTxt,pointsCrossingLastJointActionByEditTxt;
    Boolean isActivityComplete = false;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.points_crossing_activity);
        getDivisionFromSharedPreferences();
        isActivityComplete = false;

        //Date formatter
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        findViewsById();
        setDateTimeField();
        getSavedDataFromSharedPreferences();
        applyOnFocusChangeListener( selectDateEtxt );
        applyOnFocusChangeListener( pointsCrossingEnsureActionByEditTxt );
        applyOnFocusChangeListener( pointsDetailEditText );
        applyOnFocusChangeListener( obstructionCurrentEditText );
        applyOnFocusChangeListener( lockedPointsEditText );
        applyOnFocusChangeListener( pointsCrossingDetailsActionByEditTxt );
        applyOnFocusChangeListener( pointsCrossingLastJointActionByEditTxt );

        setActionBySpinner(selectedDivision,pointsCrossingEnsureActionBySpr,sharedpreferences.getInt( "pointsCrossingEnsureActionBySpinnerPosition",0));
        applyOnItemSelectedListener(pointsCrossingEnsureActionBySpr, pointsCrossingEnsureActionByEditTxt);
        setActionBySpinner(selectedDivision,pointsCrossingDetailsActionBySpr,sharedpreferences.getInt( "pointsCrossingDetailsActionBySprPosition",0));
        applyOnItemSelectedListener(pointsCrossingDetailsActionBySpr, pointsCrossingDetailsActionByEditTxt );
        setActionBySpinner(selectedDivision,pointsCrossingLastJointActionBySpr,sharedpreferences.getInt( "pointsCrossingLastJointActionBySprPosition",0));
        applyOnItemSelectedListener(pointsCrossingLastJointActionBySpr, pointsCrossingLastJointActionByEditTxt );
    }

    @Override
    public void onClick(View view) {
        datePicker.show();
    }


    private void findViewsById() {
        selectDateEtxt = findViewById(R.id.selectDateEditText);
        selectDateEtxt.setInputType( InputType.TYPE_NULL);

        pointOperatedCheckBox = findViewById(R.id.pointOperatedCheckBox);
        pointStopCheckBox = findViewById(R.id.pointStopCheckBox);
        pointOpeningCheckBox = findViewById(R.id.pointOpeningCheckBox);
        fillerGaugeCheckBox = findViewById(R.id.fillerGaugeCheckBox);
        pointMaintenanceCheckBox = findViewById(R.id.pointMaintenanceCheckBox);
        pointsDeficiencyEditText = findViewById(R.id.pointsDeficiencyEditText);
        pointsDetailEditText = findViewById(R.id.pointsDetailEditText);
        obstructionCurrentEditText = findViewById(R.id.obstructionCurrentEditText);
        lockedPointsEditText = findViewById(R.id.lockedPointsEditText);
        lastJointDeficiencyEditText = findViewById(R.id.lastJointDeficiencyEditText);
        pointsCrossingEnsureActionBySpr = findViewById( R.id.pointsCrossingEnsureActionBySpinner ) ;
        pointsCrossingEnsureActionByEditTxt = findViewById( R.id.pointsCrossingEnsureActionByEditText );
        pointsCrossingDetailsActionBySpr = findViewById( R.id.pointsCrossingDetailsActionBySpinner ) ;
        pointsCrossingDetailsActionByEditTxt = findViewById( R.id.pointsCrossingDetailsActionByEditText );
        pointsCrossingLastJointActionBySpr = findViewById( R.id.pointsCrossingLastJointActionBySpinner ) ;
        pointsCrossingLastJointActionByEditTxt = findViewById( R.id.pointsCrossingLastJointActionByEditText );
    }

    private void getDivisionFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        selectedDivision = sharedpreferences.getString("division", null);
        stationCode = sharedpreferences.getString( "stationCode",null );
    }

    private void setDateTimeField() {
        selectDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                selectDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void applyOnItemSelectedListener(final Spinner pointsCrossingActionBySpr, final EditText pointsCrossingActionByEditTxt) {
        pointsCrossingActionBySpr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = pointsCrossingActionBySpr.getSelectedItem().toString();
                if(s.equals( "Other" )){
                    pointsCrossingActionByEditTxt.setVisibility( View.VISIBLE );
                }
                else{
                    pointsCrossingActionByEditTxt.setVisibility( View.GONE );
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
                    else{
                        if(inputEditTxt.equals( selectDateEtxt )) {
                            inputEditTxt.setError( null );
                        }
                    }
                }
                else
                    inputEditTxt.setError( null );
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
        String[] tempArray = new String[ stringArray.length + 3 ];
        tempArray[0] = "None";
        tempArray[1] = firstElement;
        tempArray[2] = secondElement;
        System.arraycopy( stringArray, 0, tempArray, 3, stringArray.length );
        return tempArray;
    }

    public void savePointsCrossingData(View view) {
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
        editor.putBoolean( "pointsCrossingActivityComplete",isActivityComplete );
        editor.apply();
    }

    private void saveDataInSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(getResources().getString( R.string.point_doesn_t_get_operated_when_point_zone_tr_is_dropped ), pointOperatedCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.point_does_not_stop_when_point_zone_tr_is_dropped_during_point_operation ), pointStopCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.opening_of_point_is_around_115mm_shall_not_be_less_than_95mm_in_any_case ), pointOpeningCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.filler_gauge_shall_be_in_between_1mm_to_3mm ), fillerGaugeCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.records_of_point_maintenance_were_maintained_and_were_placed_in_respective_point_machines ), pointMaintenanceCheckBox.isChecked());
        editor.putString( "pointsDeficiencyEditText", pointsDeficiencyEditText.getText().toString() );
        editor.putString( "pointsCrossingEnsureActionBySpinner",pointsCrossingEnsureActionBySpr.getSelectedItem().toString() );
        editor.putInt( "pointsCrossingEnsureActionBySpinnerPosition",pointsCrossingEnsureActionBySpr.getSelectedItemPosition());
        editor.putString( "pointsCrossingEnsureActionByEditText",pointsCrossingEnsureActionByEditTxt.getText().toString() );
        editor.putString( "pointsDetailEditText", pointsDetailEditText.getText().toString() );
        editor.putString( "obstructionCurrentEditText", obstructionCurrentEditText.getText().toString() );
        editor.putString( "lockedPointsEditText", lockedPointsEditText.getText().toString() );
        editor.putString( "pointsCrossingDetailsActionBySpinner",pointsCrossingDetailsActionBySpr.getSelectedItem().toString() );
        editor.putInt( "pointsCrossingDetailsActionBySprPosition",pointsCrossingDetailsActionBySpr.getSelectedItemPosition());
        editor.putString( "pointsCrossingDetailsActionByEditText",pointsCrossingDetailsActionByEditTxt.getText().toString() );
        editor.putString( "selectDateEditText", selectDateEtxt.getText().toString() );
        editor.putString( "lastJointDeficiencyEditText", lastJointDeficiencyEditText.getText().toString() );
        editor.putString( "pointsCrossingLastJointActionBySpinner",pointsCrossingLastJointActionBySpr.getSelectedItem().toString() );
        editor.putInt( "pointsCrossingLastJointActionBySprPosition",pointsCrossingLastJointActionBySpr.getSelectedItemPosition());
        editor.putString( "pointsCrossingLastJointActionByEditText",pointsCrossingLastJointActionByEditTxt.getText().toString() );
        editor.putBoolean( "pointsCrossingActivityComplete",isActivityComplete );
        editor.apply();
    }

    private void getSavedDataFromSharedPreferences() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        pointOperatedCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.point_doesn_t_get_operated_when_point_zone_tr_is_dropped ), false ) );
        pointStopCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.point_does_not_stop_when_point_zone_tr_is_dropped_during_point_operation ), false ) );
        pointOpeningCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.opening_of_point_is_around_115mm_shall_not_be_less_than_95mm_in_any_case ), false ) );
        fillerGaugeCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.filler_gauge_shall_be_in_between_1mm_to_3mm ), false ) );
        pointMaintenanceCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.records_of_point_maintenance_were_maintained_and_were_placed_in_respective_point_machines ), false ) );
        pointsDeficiencyEditText.setText( sharedpreferences.getString("pointsDeficiencyEditText", null) );
        pointsCrossingEnsureActionByEditTxt.setText( sharedpreferences.getString( "pointsCrossingEnsureActionByEditText",null ) );
        pointsDetailEditText.setText( sharedpreferences.getString("pointsDeficiencyEditText", null) );
        obstructionCurrentEditText.setText( sharedpreferences.getString("obstructionCurrentEditText", null) );
        lockedPointsEditText.setText( sharedpreferences.getString("lockedPointsEditText", null) );
        pointsCrossingDetailsActionByEditTxt.setText( sharedpreferences.getString("pointsCrossingDetailsActionByEditText", null) );
        selectDateEtxt.setText( sharedpreferences.getString("selectDateEditText", null) );
        lastJointDeficiencyEditText.setText( sharedpreferences.getString("lastJointDeficiencyEditText", null) );
        pointsCrossingLastJointActionByEditTxt.setText( sharedpreferences.getString("pointsCrossingLastJointActionByEditText", null) );
    }

    private boolean validateUserInput() {
        Boolean isAnyFieldsEmpty = false;
        if(pointsCrossingEnsureActionBySpr.getSelectedItem().toString().equals( "Other" )) {
            if (pointsCrossingEnsureActionByEditTxt.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                pointsCrossingEnsureActionByEditTxt.setError( "Invalid Input" );
            }
        }
        if(pointsDetailEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            pointsDetailEditText.setError( "Invalid Input" );
        }
        if(obstructionCurrentEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            obstructionCurrentEditText.setError( "Invalid Input" );
        }
        if(lockedPointsEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            lockedPointsEditText.setError( "Invalid Input" );
        }
        if(pointsCrossingDetailsActionBySpr.getSelectedItem().toString().equals( "Other" )) {
            if (pointsCrossingDetailsActionByEditTxt.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                pointsCrossingDetailsActionByEditTxt.setError( "Invalid Input" );
            }
        }
        if(selectDateEtxt.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            selectDateEtxt.setError( "Invalid Input" );
        }
        if(pointsCrossingLastJointActionBySpr.getSelectedItem().toString().equals( "Other" )) {
            if (pointsCrossingLastJointActionByEditTxt.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                pointsCrossingLastJointActionByEditTxt.setError( "Invalid Input" );
            }
        }
        return !isAnyFieldsEmpty;
    }
}
