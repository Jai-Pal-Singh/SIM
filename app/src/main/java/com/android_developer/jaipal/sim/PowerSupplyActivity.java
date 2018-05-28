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

public class PowerSupplyActivity extends AppCompatActivity implements View.OnClickListener{

    private String selectedDivision="", stationCode="";
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;
    public Spinner ipsMakespnr, eiMakeSpnr,powerSupplyActionBySpr, relayRoomOpeningSpinner, spareRelaySpinner, whiteWashingRelaySpinner, electricalGeneralSpinner, earthingArrangementsSpinner, whetherAMCSpinner, maintenanceRecordsSpinner, eiRackSpinner, voltageParameterSpinner;
    public EditText ipsMakeEditTxt, ipsOnEditText,ipsOFFEditText, ipsAfterEditText, specificGravityEditText, specificGravityMaxEditText, lastValidationByEditText, eiMakeEditTxt,lastSystemSwitchAEditText, lastSystemSwitchBEditText, powerSupplyActionByEditTxt;
    public CheckBox amcExecutedCheckBox, smrLoadCheckBox, earthingIpsCheckBox, recordsBatteryCheckBox, whiteWashingBatteryCheckBox;
    private EditText dataLoggerDate, eiDate;
    private DatePickerDialog dataLoggerDatePicker, eiDatePicker;
    private SimpleDateFormat dataLoggerDateFormatter, eiDateFormatter;
    Boolean isActivityComplete;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.power_supply_activity);
        getDivisionFromSharedPreferences();

        initialUISetup();
        isActivityComplete = false;
        applyOnFocusChangeListener( ipsMakeEditTxt );
        applyOnFocusChangeListener( ipsOnEditText );
        applyOnFocusChangeListener( ipsOFFEditText );
        applyOnFocusChangeListener( ipsAfterEditText );
        applyOnFocusChangeListener( specificGravityEditText );
        applyOnFocusChangeListener( specificGravityMaxEditText );
        applyOnFocusChangeListener( dataLoggerDate );
        applyOnFocusChangeListener( eiDate );
        applyOnFocusChangeListener( lastValidationByEditText );
        applyOnFocusChangeListener( eiMakeEditTxt );
        applyOnFocusChangeListener( lastSystemSwitchAEditText );
        applyOnFocusChangeListener( lastSystemSwitchBEditText );
        applyOnFocusChangeListener( powerSupplyActionByEditTxt );

        applyOnItemSelectedListener(ipsMakespnr, ipsMakeEditTxt);
        applyOnItemSelectedListener(eiMakeSpnr, eiMakeEditTxt);
        setActionBySpinner(selectedDivision,powerSupplyActionBySpr);
        applyOnItemSelectedListener(powerSupplyActionBySpr, powerSupplyActionByEditTxt);

        //////////////////////////////////////////////////////////////////////////////////////////////////
        //Date formatter
        dataLoggerDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        findViewsByIdDataLogger();
        setDateTimeFieldDataLogger();

        eiDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        findViewsByIdEI();
        setDateTimeFieldEI();
        getSavedDataFromSharedPreferences();
    }

    private void getDivisionFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        selectedDivision = sharedpreferences.getString("division", null);
        stationCode = sharedpreferences.getString( "stationCode",null );
    }

    @Override
    public void onClick(View view) {
        dataLoggerDatePicker.show();
        eiDatePicker.show();
    }


    private void findViewsByIdDataLogger() {
        dataLoggerDate.setInputType( InputType.TYPE_NULL);
    }

    private void setDateTimeFieldDataLogger() {
        dataLoggerDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataLoggerDatePicker.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        dataLoggerDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dataLoggerDate.setText(dataLoggerDateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dataLoggerDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void findViewsByIdEI() {
        eiDate.setInputType( InputType.TYPE_NULL);
    }

    private void setDateTimeFieldEI() {
        eiDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eiDatePicker.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        eiDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                eiDate.setText(eiDateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        eiDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void initialUISetup(){
        ipsMakespnr = findViewById( R.id.ipsMakeSpinner ) ;
        ipsMakeEditTxt = findViewById( R.id.ipsMakeEditText );
        eiMakeSpnr = findViewById( R.id.eiMakeSpinner ) ;
        eiMakeEditTxt = findViewById( R.id.eiMakeEditText );
        powerSupplyActionBySpr = findViewById( R.id.powerSupplyActionBySpinner ) ;
        powerSupplyActionByEditTxt =  findViewById( R.id.powerSupplyActionByEditText );
        dataLoggerDate = findViewById(R.id.lastValidationEditText);
        eiDate = findViewById(R.id.lastSystemOnEditText);
        amcExecutedCheckBox = findViewById(R.id.amcExecutedCheckBox);
        smrLoadCheckBox = findViewById(R.id.smrLoadCheckBox);
        earthingIpsCheckBox = findViewById(R.id.earthingIpsCheckBox);
        recordsBatteryCheckBox = findViewById(R.id.recordsBatteryCheckBox);
        whiteWashingBatteryCheckBox = findViewById(R.id.whiteWashingBatteryCheckBox);
        relayRoomOpeningSpinner = findViewById(R.id.relayRoomOpeningSpinner);
        spareRelaySpinner = findViewById(R.id.spareRelaySpinner);
        whiteWashingRelaySpinner = findViewById(R.id.whiteWashingRelaySpinner);
        electricalGeneralSpinner = findViewById(R.id.electricalGeneralSpinner);
        earthingArrangementsSpinner = findViewById(R.id.earthingArrangementsSpinner);
        whetherAMCSpinner = findViewById(R.id.whetherAMCSpinner);
        maintenanceRecordsSpinner = findViewById(R.id.maintenanceRecordsSpinner);
        eiRackSpinner = findViewById(R.id.eiRackSpinner);
        voltageParameterSpinner = findViewById(R.id.voltageParameterSpinner);
        ipsOnEditText = findViewById(R.id.ipsOnEditText);
        ipsOFFEditText = findViewById(R.id.ipsOFFEditText);
        ipsAfterEditText = findViewById(R.id.ipsAfterEditText);
        specificGravityEditText = findViewById(R.id.specificGravityEditText);
        specificGravityMaxEditText = findViewById(R.id.specificGravityMaxEditText);
        lastValidationByEditText = findViewById(R.id.lastValidationByEditText);
        lastSystemSwitchAEditText = findViewById(R.id.lastSystemSwitchAEditText);
        lastSystemSwitchBEditText = findViewById(R.id.lastSystemSwitchBEditText);
    }

    private void saveDataInSharedPreferences() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean( "powerActivityComplete",isActivityComplete );
        editor.putString( "ipsMakespnr",ipsMakespnr.getSelectedItem().toString() );
        editor.putString( "ipsMakeEditTxt",ipsMakeEditTxt.getText().toString() );
        editor.putBoolean(getResources().getString( R.string.amc_executed ), amcExecutedCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.smr_load_sharing_is_working_fine ), smrLoadCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.earthing_of_ips_equipment_was_proper ), earthingIpsCheckBox.isChecked());
        editor.putString( "ipsOnEditText",ipsOnEditText.getText().toString() );
        editor.putString( "ipsOFFEditText",ipsOFFEditText.getText().toString() );
        editor.putString( "ipsAfterEditText",ipsAfterEditText.getText().toString() );
        editor.putString( "specificGravityEditText",specificGravityEditText.getText().toString() );
        editor.putString( "specificGravityMaxEditText",specificGravityMaxEditText.getText().toString() );
        editor.putBoolean(getResources().getString( R.string.records_of_battery_readings_were_maintained ), recordsBatteryCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.white_washing_required_in_battery_room ), whiteWashingBatteryCheckBox.isChecked());
        editor.putString( "relayRoomOpeningSpinner",relayRoomOpeningSpinner.getSelectedItem().toString() );
        editor.putString( "spareRelaySpinner",spareRelaySpinner.getSelectedItem().toString() );
        editor.putString( "whiteWashingRelaySpinner",whiteWashingRelaySpinner.getSelectedItem().toString() );
        editor.putString( "electricalGeneralSpinner",electricalGeneralSpinner.getSelectedItem().toString() );
        editor.putString( "earthingArrangementsSpinner",earthingArrangementsSpinner.getSelectedItem().toString() );
        editor.putString( "whetherAMCSpinner",whetherAMCSpinner.getSelectedItem().toString() );
        editor.putString( "maintenanceRecordsSpinner",maintenanceRecordsSpinner.getSelectedItem().toString() );
        editor.putString( "dataLoggerDate",dataLoggerDate.getText().toString() );
        editor.putString( "lastValidationByEditText",lastValidationByEditText.getText().toString() );
        editor.putString( "eiMakeSpnr",eiMakeSpnr.getSelectedItem().toString() );
        editor.putString( "eiMakeEditTxt",eiMakeEditTxt.getText().toString() );
        editor.putString( "lastSystemSwitchAEditText",lastSystemSwitchAEditText.getText().toString() );
        editor.putString( "lastSystemSwitchBEditText",lastSystemSwitchBEditText.getText().toString() );
        editor.putString( "eiDate",eiDate.getText().toString() );
        editor.putString( "eiRackSpinner",eiRackSpinner.getSelectedItem().toString() );
        editor.putString( "voltageParameterSpinner",voltageParameterSpinner.getSelectedItem().toString() );
        editor.putString( "powerSupplyActionBySpr",powerSupplyActionBySpr.getSelectedItem().toString() );
        editor.putString( "powerSupplyActionByEditTxt",powerSupplyActionByEditTxt.getText().toString() );
        editor.apply();
    }

    private void getSavedDataFromSharedPreferences() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        ipsMakeEditTxt.setText( sharedpreferences.getString( "ipsMakeEditTxt",null ) );
        amcExecutedCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.amc_executed ), false ) );
        smrLoadCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.smr_load_sharing_is_working_fine ), false ) );
        earthingIpsCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.earthing_of_ips_equipment_was_proper ), false ) );
        ipsOnEditText.setText( sharedpreferences.getString( "ipsOnEditText",null ) );
        ipsOFFEditText.setText( sharedpreferences.getString( "ipsOFFEditText",null ) );
        ipsAfterEditText.setText( sharedpreferences.getString( "ipsAfterEditText",null ) );
        specificGravityEditText.setText( sharedpreferences.getString( "specificGravityEditText",null ) );
        specificGravityMaxEditText.setText( sharedpreferences.getString( "specificGravityMaxEditText",null ) );
        recordsBatteryCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.records_of_battery_readings_were_maintained ), false ) );
        whiteWashingBatteryCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.white_washing_required_in_battery_room ), false ) );
        dataLoggerDate.setText( sharedpreferences.getString( "dataLoggerDate",null ) );
        lastValidationByEditText.setText( sharedpreferences.getString( "lastValidationByEditText",null ) );
        eiMakeEditTxt.setText( sharedpreferences.getString( "eiMakeEditTxt",null ) );
        lastSystemSwitchAEditText.setText( sharedpreferences.getString( "lastSystemSwitchAEditText",null ) );
        lastSystemSwitchBEditText.setText( sharedpreferences.getString( "lastSystemSwitchBEditText",null ) );
        eiDate.setText( sharedpreferences.getString( "eiDate",null ) );
        powerSupplyActionByEditTxt.setText( sharedpreferences.getString( "powerSupplyActionByEditTxt",null ) );
    }

    private void applyOnItemSelectedListener(final Spinner powerSupplyActionBySpr, final EditText powerSupplyActionByEditTxt) {
        powerSupplyActionBySpr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = powerSupplyActionBySpr.getSelectedItem().toString();
                if(s.equals( "Other" )){
                    powerSupplyActionByEditTxt.setVisibility( View.VISIBLE );
                }
                else{
                    powerSupplyActionByEditTxt.setVisibility( View.GONE );
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

    private  boolean validateUserInput() {
        Boolean isAnyFieldsEmpty = false;
        if(ipsMakespnr.getSelectedItem().toString().equals( "Other" )) {
            if (ipsMakeEditTxt.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                ipsMakeEditTxt.setError( "Invalid Input" );
            }
        }
        if(eiMakeSpnr.getSelectedItem().toString().equals( "Other" )) {
            if (eiMakeEditTxt.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                eiMakeEditTxt.setError( "Invalid Input" );
            }
        }
        if(powerSupplyActionBySpr.getSelectedItem().toString().equals( "Other" )) {
            if (powerSupplyActionByEditTxt.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                powerSupplyActionByEditTxt.setError( "Invalid Input" );
            }
        }
        if(ipsOnEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            ipsOnEditText.setError( "Invalid Input" );
        }
        if(ipsOFFEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            ipsOFFEditText.setError( "Invalid Input" );
        }
        if(ipsAfterEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            ipsAfterEditText.setError( "Invalid Input" );
        }
        if(specificGravityEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            specificGravityEditText.setError( "Invalid Input" );
        }
        if(specificGravityMaxEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            specificGravityMaxEditText.setError( "Invalid Input" );
        }
        if(lastValidationByEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            lastValidationByEditText.setError( "Invalid Input" );
        }
        if(dataLoggerDate.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            dataLoggerDate.setError( "Invalid Input" );
        }
        if(lastSystemSwitchAEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            lastSystemSwitchAEditText.setError( "Invalid Input" );
        }
        if(lastSystemSwitchBEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            lastSystemSwitchBEditText.setError( "Invalid Input" );
        }if(eiDate.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            eiDate.setError( "Invalid Input" );
        }
        return !isAnyFieldsEmpty;
    }

    public void savePowerSupplyData(View view) {
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
        editor.putBoolean( "powerActivityComplete",isActivityComplete );
        editor.apply();
    }
}
