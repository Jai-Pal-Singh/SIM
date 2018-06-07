package com.android_developer.jaipal.sim;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.mail.Quota;

/**
 * Created by hp on 2018-04-01.
 */

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText dateOfTestingEtxt;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    public RadioGroup SmKeyRadioGroup,VHFSetRadioGroup,ControlPhoneRadioGroup, RailwayPhoneRadioGroup, VHFRepeaterRadioGroup, PASystemRadioGroup, DigitalEquipmentRadioGroup, BatteryRecordsRadioGroup, EarthTerminationRadioGroup, EmergencySocketRadioGroup;
    private Spinner vhfSetActionBySpr,digitalEquipActionBySpr,testedSocketsActionBySpr;
    private EditText vhfSetActionByEditTxt,digitalEquipActionByEditTxt,testedSocketsActionByEditTxt;
    public EditText SMeditText, TelecomInstallationEditText, TestedPointsEditText, TestedCHEditText, BatteryVoltageEditText, OFCHutEditText, TestedSocketsEditText;
    private String selectedDivision="", stationCode="";
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;
    RadioButton radioButton;
    Boolean isActivityComplete = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        findViewsById();
        getDivisionFromSharedPreferences();
        isActivityComplete = false;

        //Date formatter
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        findViewsById();
        getSavedDataFromSharedPreferences();
        setDateTimeField();

        applyOnFocusChangeListener( SMeditText );
        applyOnFocusChangeListener( vhfSetActionByEditTxt );
        applyOnFocusChangeListener( digitalEquipActionByEditTxt );
        applyOnFocusChangeListener( testedSocketsActionByEditTxt );
        applyOnFocusChangeListener( dateOfTestingEtxt );
        applyOnFocusChangeListener( TestedPointsEditText );
        applyOnFocusChangeListener( TestedCHEditText );
        applyOnFocusChangeListener( BatteryVoltageEditText );

        setActionBySpinner(selectedDivision,vhfSetActionBySpr,sharedpreferences.getInt( "vhfSetActionBySprPosition",0));
        setActionBySpinner(selectedDivision,digitalEquipActionBySpr,sharedpreferences.getInt( "digitalEquipActionBySprPosition",0));
        setActionBySpinner(selectedDivision,testedSocketsActionBySpr,sharedpreferences.getInt( "testedSocketsActionBySprPosition",0));

        applyOnItemSelectedListener(vhfSetActionBySpr, vhfSetActionByEditTxt);
        applyOnItemSelectedListener(digitalEquipActionBySpr, digitalEquipActionByEditTxt);
        applyOnItemSelectedListener(testedSocketsActionBySpr, testedSocketsActionByEditTxt);
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
        dateOfTestingEtxt = findViewById(R.id.TestingDateEditText);
        dateOfTestingEtxt.setInputType( InputType.TYPE_NULL);

        SmKeyRadioGroup = findViewById(R.id.SmKeyRadioGroup);
        VHFSetRadioGroup = findViewById(R.id.VHFSetRadioGroup);
        ControlPhoneRadioGroup = findViewById(R.id.ControlPhoneRadioGroup);
        RailwayPhoneRadioGroup = findViewById(R.id.RailwayPhoneRadioGroup);
        VHFRepeaterRadioGroup = findViewById(R.id.VHFRepeaterRadioGroup);
        PASystemRadioGroup = findViewById(R.id.PASystemRadioGroup);
        DigitalEquipmentRadioGroup = findViewById(R.id.DigitalEquipmentRadioGroup);
        BatteryRecordsRadioGroup = findViewById(R.id.BatteryRecordsRadioGroup);
        EarthTerminationRadioGroup = findViewById(R.id.EarthTerminationRadioGroup);
        EmergencySocketRadioGroup = findViewById(R.id.EmergencySocketRadioGroup);
        SMeditText= findViewById(R.id.SMeditText);
        TelecomInstallationEditText= findViewById(R.id.TelecomInstallationEditText);
        TestedPointsEditText= findViewById(R.id.TestedPointsEditText);
        TestedCHEditText= findViewById(R.id.TestedCHEditText);
        BatteryVoltageEditText= findViewById(R.id.BatteryVoltageEditText);
        OFCHutEditText= findViewById(R.id.OFCHutEditText);
        TestedSocketsEditText= findViewById(R.id.TestedSocketsEditText);
        vhfSetActionBySpr = findViewById( R.id.vhfSetActionBySpinner ) ;
        vhfSetActionByEditTxt = findViewById( R.id.vhfSetActionByEditText );
        digitalEquipActionBySpr = findViewById( R.id.digitalEquipActionBySpinner ) ;
        digitalEquipActionByEditTxt = findViewById( R.id.digitalEquipActionByEditText );
        testedSocketsActionBySpr = findViewById( R.id.testedSocketsActionBySpinner ) ;
        testedSocketsActionByEditTxt = findViewById( R.id.testedSocketsActionByEditText );
    }

    private void setDateTimeField() {
        dateOfTestingEtxt.setOnClickListener(new View.OnClickListener() {
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
                dateOfTestingEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void setActionBySpinner(String selectedDivision, Spinner ActionBySpr, int position) {
        ArrayAdapter<String> adapter;
        String[] actionBy;
        final List<String> actionByList;
        switch (selectedDivision) {
            case "JP":
                actionBy = createActionByList(stationCode, "SSE/Tele/","SSE/Sig/",getResources().getStringArray( R.array.jaipurActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<String>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                ActionBySpr.setSelection( position );
                break;
            case "JU":
                actionBy = createActionByList(stationCode, "SSE/Tele/","SSE/Sig/",getResources().getStringArray( R.array.jodhpurActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<String>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                ActionBySpr.setSelection( position );
                break;
            case "AII":
                actionBy = createActionByList(stationCode, "SSE/Tele/","SSE/Sig/",getResources().getStringArray( R.array.ajmerActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<String>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                ActionBySpr.setSelection( position );
                break;
            case "BKN":
                actionBy = createActionByList(stationCode, "SSE/Tele/","SSE/Sig/",getResources().getStringArray( R.array.bikanerActionBy ));
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

    private void applyOnItemSelectedListener(final Spinner detailsActionBySpr, final EditText detailsActionByEditTxt) {
        detailsActionBySpr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = detailsActionBySpr.getSelectedItem().toString();
                if(s.equals( "Other" )){
                    detailsActionByEditTxt.setVisibility( View.VISIBLE );
                }
                else{
                    detailsActionByEditTxt.setVisibility( View.GONE );
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

    public void saveDetailActivityData(View view) {
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
        editor.putBoolean( "detailActivityComplete",isActivityComplete );
        editor.apply();
    }


    private void saveDataInSharedPreferences() {
        int selectedId;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString( "SMeditText",SMeditText.getText().toString() );

//        selectedId = SmKeyRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(SmKeyRadioGroup.getCheckedRadioButtonId());
        editor.putString( "SMKeyValue", radioButton.getText().toString() );
        editor.putInt( "SMKey", SmKeyRadioGroup.getCheckedRadioButtonId() );

        selectedId = VHFSetRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        editor.putString( "VHFsetValue", radioButton.getText().toString() );
        editor.putInt( "VHFset", VHFSetRadioGroup.getCheckedRadioButtonId() );
        selectedId = ControlPhoneRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        editor.putString( "ControlPhoneValue", radioButton.getText().toString() );
        editor.putInt( "ControlPhone", ControlPhoneRadioGroup.getCheckedRadioButtonId() );
        selectedId = RailwayPhoneRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        editor.putString( "RailwayPhoneValue", radioButton.getText().toString() );
        editor.putInt( "RailwayPhone", RailwayPhoneRadioGroup.getCheckedRadioButtonId() );
        selectedId = VHFRepeaterRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        editor.putString( "VHFrepeaterValue", radioButton.getText().toString() );
        editor.putInt( "VHFrepeater", VHFRepeaterRadioGroup.getCheckedRadioButtonId() );
        selectedId = PASystemRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        editor.putString( "PAsystemValue", radioButton.getText().toString() );
        editor.putInt( "PAsystem", PASystemRadioGroup.getCheckedRadioButtonId() );
        editor.putString( "TelecomInstallationEditText",TelecomInstallationEditText.getText().toString() );
        editor.putString( "vhfSetActionByEditText",vhfSetActionByEditTxt.getText().toString() );
        editor.putString( "TestingDateEditText",dateOfTestingEtxt.getText().toString() );
        editor.putString( "TestedPointsEditText",TestedPointsEditText.getText().toString() );
        editor.putString( "TestedCHEditText",TestedCHEditText.getText().toString() );
        editor.putString( "BatteryVoltageEditText",BatteryVoltageEditText.getText().toString() );
        selectedId = DigitalEquipmentRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        editor.putString( "DigitalEquipmentRadioGroupValue", radioButton.getText().toString() );
        editor.putInt( "DigitalEquipmentRadioGroup", DigitalEquipmentRadioGroup.getCheckedRadioButtonId() );
        selectedId = BatteryRecordsRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        editor.putString( "BatteryRecordsRadioGroupValue", radioButton.getText().toString() );
        editor.putInt( "BatteryRecordsRadioGroup", BatteryRecordsRadioGroup.getCheckedRadioButtonId() );
        selectedId = EarthTerminationRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        editor.putString( "EarthTerminationRadioGroupValue", radioButton.getText().toString() );
        editor.putInt( "EarthTerminationRadioGroup", EarthTerminationRadioGroup.getCheckedRadioButtonId() );
        editor.putString( "OFCHutEditText",OFCHutEditText.getText().toString() );
        editor.putString( "digitalEquipActionByEditText",digitalEquipActionByEditTxt.getText().toString() );
        selectedId = EmergencySocketRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        editor.putString( "EmergencySocketRadioGroupValue", radioButton.getText().toString() );
        editor.putInt( "EmergencySocketRadioGroup", EmergencySocketRadioGroup.getCheckedRadioButtonId() );
        editor.putString( "TestedSocketsEditText",TestedSocketsEditText.getText().toString() );
        editor.putString( "testedSocketsActionByEditText",testedSocketsActionByEditTxt.getText().toString() );
        editor.putString( "vhfSetActionBySpinner",vhfSetActionBySpr.getSelectedItem().toString() );
        editor.putInt( "vhfSetActionBySprPosition",vhfSetActionBySpr.getSelectedItemPosition() );
        editor.putString( "digitalEquipActionBySpr",digitalEquipActionBySpr.getSelectedItem().toString() );
        editor.putInt( "digitalEquipActionBySprPosition",digitalEquipActionBySpr.getSelectedItemPosition() );
        editor.putString( "testedSocketsActionBySpr",testedSocketsActionBySpr.getSelectedItem().toString() );
        editor.putInt( "testedSocketsActionBySprPosition",testedSocketsActionBySpr.getSelectedItemPosition() );
        editor.putBoolean( "detailActivityComplete",isActivityComplete );
        editor.apply();
    }
    private void getSavedDataFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SMeditText.setText( sharedpreferences.getString( "SMeditText",null ) );
        radioButton = findViewById(sharedpreferences.getInt( "SMKey",R.id.SMKeyYesRadioButton ));
        radioButton.setChecked( true );
        radioButton = (RadioButton) findViewById(sharedpreferences.getInt( "VHFset",R.id.VHFYesRadioButton ));
        radioButton.setChecked( true );
        radioButton = findViewById(sharedpreferences.getInt( "ControlPhone",R.id.ControlPhoneYesRadioButton ));radioButton.setChecked( true );
        radioButton = findViewById(sharedpreferences.getInt( "RailwayPhone",R.id.RailwayPhoneYesRadioButton ));radioButton.setChecked( true );
        radioButton = findViewById(sharedpreferences.getInt( "VHFrepeater",R.id.VHFRepeaterYesRadioButton ));radioButton.setChecked( true );
        radioButton = findViewById(sharedpreferences.getInt( "PAsystem",R.id.PASystemYesRadioButton ));radioButton.setChecked( true );
        radioButton = findViewById(sharedpreferences.getInt( "DigitalEquipmentRadioGroup",R.id.DigitalEquipOkRadioButton ));radioButton.setChecked( true );
        radioButton = findViewById(sharedpreferences.getInt( "BatteryRecordsRadioGroup",R.id.BatteryRecordsYesRadioButton ));radioButton.setChecked( true );
        radioButton = findViewById(sharedpreferences.getInt( "EarthTerminationRadioGroup",R.id.EarthTerminationFoundRadioButton ));radioButton.setChecked( true );
        radioButton = findViewById(sharedpreferences.getInt( "EmergencySocketRadioGroup",R.id.EmergencySocketOkRadioButton ));radioButton.setChecked( true );
        TelecomInstallationEditText.setText( sharedpreferences.getString( "TelecomInstallationEditText",null )  );
        vhfSetActionByEditTxt.setText( sharedpreferences.getString( "vhfSetActionByEditText",null )  );
        dateOfTestingEtxt.setText( sharedpreferences.getString( "TestingDateEditText",null )  );
        TestedPointsEditText.setText( sharedpreferences.getString( "TestedPointsEditText",null )  );
        TestedCHEditText.setText( sharedpreferences.getString( "TestedCHEditText",null )  );
        BatteryVoltageEditText.setText( sharedpreferences.getString( "BatteryVoltageEditText",null )  );
        OFCHutEditText.setText( sharedpreferences.getString( "OFCHutEditText",null )  );
        digitalEquipActionByEditTxt.setText( sharedpreferences.getString( "digitalEquipActionByEditText",null )  );
        TestedSocketsEditText.setText( sharedpreferences.getString( "TestedSocketsEditText",null )  );
        testedSocketsActionByEditTxt.setText( sharedpreferences.getString( "testedSocketsActionByEditText",null )  );
    }

    private boolean validateUserInput() {
        Boolean isAnyFieldsEmpty = false;
        if(SMeditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            SMeditText.setError( "Invalid Input" );
        }
        if(vhfSetActionBySpr.getSelectedItem().toString().equals( "Other" )) {
            if (vhfSetActionByEditTxt.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                vhfSetActionByEditTxt.setError( "Invalid Input" );
            }
        }
        if(digitalEquipActionBySpr.getSelectedItem().toString().equals( "Other" )) {
            if (digitalEquipActionByEditTxt.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                digitalEquipActionByEditTxt.setError( "Invalid Input" );
            }
        }
        if(testedSocketsActionBySpr.getSelectedItem().toString().equals( "Other" )) {
            if (testedSocketsActionByEditTxt.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                testedSocketsActionByEditTxt.setError( "Invalid Input" );
            }
        }
        if(dateOfTestingEtxt.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            dateOfTestingEtxt.setError( "Invalid Input" );
        }
        if(TestedPointsEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            TestedPointsEditText.setError( "Invalid Input" );
        }
        if(TestedCHEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            TestedCHEditText.setError( "Invalid Input" );
        }
        if(BatteryVoltageEditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            BatteryVoltageEditText.setError( "Invalid Input" );
        }
        return !isAnyFieldsEmpty;
    }
}
