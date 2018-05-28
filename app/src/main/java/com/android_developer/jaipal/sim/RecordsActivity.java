package com.android_developer.jaipal.sim;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by hp on 2018-04-02.
 */

public class RecordsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner signalInfringementSpinner, earthTestingSpinner, cableMeggeringSpinner, updatedCktDiagramSpinner, cableRouteSpinner, cableCoreSpinner;
    Spinner signalInterlockingSpinner, SMC1Spinner, SMC12Spinner, signalHistorySpinner, recordsActionBySpr;
    EditText signalPoint1EditText, signalTrackCircuit1EditText, signalBI1EditText, signalPowerSupply1EditText, signalOthers1EditText;
    EditText signalPoint2EditText, signalTrackCircuit2EditText, signalBI2EditText, signalPowerSupply2EditText, signalOthers2EditText;
    EditText signalPoint3EditText, signalTrackCircuit3EditText, signalBI3EditText, signalPowerSupply3EditText, signalOthers3EditText;
    EditText signalFailureRemarkEditText, disconnection1DREditText, disconnection2DREditText, disconnection3DREditText, disconnectionReconnectionEditText;
    EditText relayRoom1RREditText, relayRoom2RREditText, relayRoom3RREditText,relayRoomEditText, recordsActionByEditTxt;
    String thisMonth,lastMonth,last2ndMonth;
    private String selectedDivision="",selectedInspectDate="", stationCode="";
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;
    java.util.Date month;
    TextView signalThisMonthTxtView, signalLastMonthTxtView, signalLast2ndMonthTxtView,disconnectionThisMonthTxtView, disconnectionLastMonthTxtView, disconnectionLast2ndMonthTxtView,relayThisMonthTxtView, relayLastMonthTxtView, relayLast2ndMonthTxtView;
    Boolean isActivityComplete;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.records_activity );
        getDivisionFromSharedPreferences();
        initialUISetup();
        isActivityComplete = false;

        getSavedDataFromSharedPreferences();
        applyOnFocusChangeListener( signalPoint1EditText );
        applyOnFocusChangeListener( signalPoint2EditText );
        applyOnFocusChangeListener( signalPoint3EditText );
        applyOnFocusChangeListener( signalTrackCircuit1EditText );
        applyOnFocusChangeListener( signalTrackCircuit2EditText );
        applyOnFocusChangeListener( signalTrackCircuit3EditText );
        applyOnFocusChangeListener( signalBI1EditText );
        applyOnFocusChangeListener( signalBI2EditText );
        applyOnFocusChangeListener( signalBI3EditText );
        applyOnFocusChangeListener( signalPowerSupply1EditText );
        applyOnFocusChangeListener( signalPowerSupply2EditText );
        applyOnFocusChangeListener( signalPowerSupply3EditText );
        applyOnFocusChangeListener( signalOthers1EditText );
        applyOnFocusChangeListener( signalOthers2EditText );
        applyOnFocusChangeListener( signalOthers3EditText );
        applyOnFocusChangeListener( disconnection1DREditText );
        applyOnFocusChangeListener( disconnection2DREditText );
        applyOnFocusChangeListener( disconnection3DREditText );
        applyOnFocusChangeListener( relayRoom1RREditText );
        applyOnFocusChangeListener( relayRoom2RREditText );
        applyOnFocusChangeListener( relayRoom3RREditText );
        applyOnFocusChangeListener( recordsActionByEditTxt );

        setActionBySpinner(selectedDivision,recordsActionBySpr);
        applyOnItemSelectedListener(recordsActionBySpr, recordsActionByEditTxt);
        //////////////////////////////////////////////////////////////////////////////////////////////////
        //get Inspection Month and set it in the tables
        thisMonth=extractMonthFromDate(selectedInspectDate);
        lastMonth=extractLastMonthFromDate(selectedInspectDate);
        last2ndMonth=extract2ndLastMonthFromDate( selectedInspectDate );
        setMonthsToTextView();
        //////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void getDivisionFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        selectedDivision = sharedpreferences.getString("division", null);
        selectedInspectDate = sharedpreferences.getString( "inspectDate", null );
        stationCode = sharedpreferences.getString( "stationCode",null );
    }

    private void initialUISetup() {
        signalThisMonthTxtView = findViewById( R.id.signalMonth3TextView );
        signalLastMonthTxtView = findViewById( R.id.signalMonth2TextView );
        signalLast2ndMonthTxtView = findViewById( R.id.signalMonth1TextView );
        disconnectionThisMonthTxtView = findViewById( R.id.disconnection3TextView );
        disconnectionLastMonthTxtView = findViewById( R.id.disconnection2TextView );
        disconnectionLast2ndMonthTxtView = findViewById( R.id.disconnection1TextView );
        relayThisMonthTxtView = findViewById( R.id.relayRoom3TextView );
        relayLastMonthTxtView = findViewById( R.id.relayRoom2TextView );
        relayLast2ndMonthTxtView = findViewById( R.id.relayRoom1TextView );
        recordsActionBySpr = findViewById( R.id.recordsActionBySpinner ) ;
        recordsActionByEditTxt = findViewById( R.id.recordsActionByEditText );
        signalInfringementSpinner = findViewById( R.id.signalInfringementSpinner );
        earthTestingSpinner = findViewById( R.id.earthTestingSpinner );
        cableMeggeringSpinner = findViewById( R.id.cableMeggeringSpinner );
        updatedCktDiagramSpinner = findViewById( R.id.updatedCktDiagramSpinner );
        cableRouteSpinner = findViewById( R.id.cableRouteSpinner );
        cableCoreSpinner = findViewById( R.id.cableCoreSpinner );
        signalInterlockingSpinner = findViewById( R.id.signalInterlockingSpinner );
        SMC1Spinner = findViewById( R.id.SMC1Spinner );
        SMC12Spinner = findViewById( R.id.SMC12Spinner );
        signalHistorySpinner = findViewById( R.id.signalHistorySpinner );
        signalPoint1EditText = findViewById( R.id.signalPoint1EditText );
        signalTrackCircuit1EditText = findViewById( R.id.signalTrackCircuit1EditText );
        signalBI1EditText = findViewById( R.id.signalBI1EditText );
        signalPowerSupply1EditText = findViewById( R.id.signalPowerSupply1EditText );
        signalOthers1EditText = findViewById( R.id.signalOthers1EditText );
        signalPoint2EditText = findViewById( R.id.signalPoint2EditText );
        signalTrackCircuit2EditText = findViewById( R.id.signalTrackCircuit2EditText );
        signalBI2EditText = findViewById( R.id.signalBI2EditText );
        signalPowerSupply2EditText = findViewById( R.id.signalPowerSupply2EditText );
        signalOthers2EditText = findViewById( R.id.signalOthers2EditText );
        signalPoint3EditText = findViewById( R.id.signalPoint3EditText );
        signalTrackCircuit3EditText = findViewById( R.id.signalTrackCircuit3EditText );
        signalBI3EditText = findViewById( R.id.signalBI3EditText );
        signalPowerSupply3EditText = findViewById( R.id.signalPowerSupply3EditText );
        signalOthers3EditText = findViewById( R.id.signalOthers3EditText );
        signalFailureRemarkEditText = findViewById( R.id.signalFailureRemarkEditText );
        disconnection1DREditText = findViewById( R.id.disconnection1DREditText );
        disconnection2DREditText = findViewById( R.id.disconnection2DREditText );
        disconnection3DREditText = findViewById( R.id.disconnection3DREditText );
        disconnectionReconnectionEditText = findViewById( R.id.disconnectionReconnectionEditText );
        relayRoom1RREditText = findViewById( R.id.relayRoom1RREditText );
        relayRoom2RREditText = findViewById( R.id.relayRoom2RREditText );
        relayRoom3RREditText = findViewById( R.id.relayRoom3RREditText );
        relayRoomEditText = findViewById( R.id.relayRoomEditText );
    }

    private void saveDataInSharedPreferences() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean( "recordsActivityComplete",isActivityComplete );
        editor.putString( "month1",signalLast2ndMonthTxtView.getText().toString() );
        editor.putString( "month2",signalLastMonthTxtView.getText().toString() );
        editor.putString( "month3",signalThisMonthTxtView.getText().toString() );
        editor.putString( "signalPoint1EditText",signalPoint1EditText.getText().toString() );
        editor.putString( "signalTrackCircuit1EditText",signalTrackCircuit1EditText.getText().toString() );
        editor.putString( "signalBI1EditText",signalBI1EditText.getText().toString() );
        editor.putString( "signalPowerSupply1EditText",signalPowerSupply1EditText.getText().toString() );
        editor.putString( "signalOthers1EditText",signalOthers1EditText.getText().toString() );
        editor.putString( "signalPoint2EditText",signalPoint2EditText.getText().toString() );
        editor.putString( "signalTrackCircuit2EditText",signalTrackCircuit2EditText.getText().toString() );
        editor.putString( "signalBI2EditText",signalBI2EditText.getText().toString() );
        editor.putString( "signalPowerSupply2EditText",signalPowerSupply2EditText.getText().toString() );
        editor.putString( "signalOthers2EditText",signalOthers2EditText.getText().toString() );
        editor.putString( "signalPoint3EditText",signalPoint3EditText.getText().toString() );
        editor.putString( "signalTrackCircuit3EditText",signalTrackCircuit3EditText.getText().toString() );
        editor.putString( "signalBI3EditText",signalBI3EditText.getText().toString() );
        editor.putString( "signalPowerSupply3EditText",signalPowerSupply3EditText.getText().toString() );
        editor.putString( "signalOthers3EditText",signalOthers3EditText.getText().toString() );
        editor.putString( "signalFailureRemarkEditText",signalFailureRemarkEditText.getText().toString() );
        editor.putString( "disconnection1DREditText",disconnection1DREditText.getText().toString() );
        editor.putString( "disconnection2DREditText",disconnection2DREditText.getText().toString() );
        editor.putString( "disconnection3DREditText",disconnection3DREditText.getText().toString() );
        editor.putString( "disconnectionReconnectionEditText",disconnectionReconnectionEditText.getText().toString() );
        editor.putString( "relayRoom1RREditText",relayRoom1RREditText.getText().toString() );
        editor.putString( "relayRoom2RREditText",relayRoom2RREditText.getText().toString() );
        editor.putString( "relayRoom3RREditText",relayRoom3RREditText.getText().toString() );
        editor.putString( "relayRoomEditText",relayRoomEditText.getText().toString() );
        editor.putString( "signalInfringementSpinner",signalInfringementSpinner.getSelectedItem().toString() );
        editor.putString( "earthTestingSpinner",earthTestingSpinner.getSelectedItem().toString() );
        editor.putString( "cableMeggeringSpinner",cableMeggeringSpinner.getSelectedItem().toString() );
        editor.putString( "updatedCktDiagramSpinner",updatedCktDiagramSpinner.getSelectedItem().toString() );
        editor.putString( "cableRouteSpinner",cableRouteSpinner.getSelectedItem().toString() );
        editor.putString( "cableCoreSpinner",cableCoreSpinner.getSelectedItem().toString() );
        editor.putString( "signalInterlockingSpinner",signalInterlockingSpinner.getSelectedItem().toString() );
        editor.putString( "SMC1Spinner",SMC1Spinner.getSelectedItem().toString() );
        editor.putString( "SMC12Spinner",SMC12Spinner.getSelectedItem().toString() );
        editor.putString( "signalHistorySpinner",signalHistorySpinner.getSelectedItem().toString() );
        editor.putString( "recordsActionBySpr",recordsActionBySpr.getSelectedItem().toString() );
        editor.putString( "recordsActionByEditTxt",recordsActionByEditTxt.getText().toString() );
        editor.apply();
    }

    private void getSavedDataFromSharedPreferences() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        signalPoint1EditText.setText( sharedpreferences.getString( "signalPoint1EditText",null ) );
        signalTrackCircuit1EditText.setText( sharedpreferences.getString( "signalTrackCircuit1EditText",null ) );
        signalBI1EditText.setText( sharedpreferences.getString( "signalBI1EditText",null ) );
        signalPowerSupply1EditText.setText( sharedpreferences.getString( "signalPowerSupply1EditText",null ) );
        signalOthers1EditText.setText( sharedpreferences.getString( "signalOthers1EditText",null ) );
        signalPoint2EditText.setText( sharedpreferences.getString( "signalPoint2EditText",null ) );
        signalTrackCircuit2EditText.setText( sharedpreferences.getString( "signalTrackCircuit2EditText",null ) );
        signalBI2EditText.setText( sharedpreferences.getString( "signalBI2EditText",null ) );
        signalPowerSupply2EditText.setText( sharedpreferences.getString( "signalPowerSupply2EditText",null ) );
        signalOthers2EditText.setText( sharedpreferences.getString( "signalOthers2EditText",null ) );
        signalPoint3EditText.setText( sharedpreferences.getString( "signalPoint3EditText",null ) );
        signalTrackCircuit3EditText.setText( sharedpreferences.getString( "signalTrackCircuit3EditText",null ) );
        signalBI3EditText.setText( sharedpreferences.getString( "signalBI3EditText",null ) );
        signalPowerSupply3EditText.setText( sharedpreferences.getString( "signalPowerSupply3EditText",null ) );
        signalOthers3EditText.setText( sharedpreferences.getString( "signalOthers3EditText",null ) );
        signalFailureRemarkEditText.setText( sharedpreferences.getString( "signalFailureRemarkEditText",null ) );
        disconnection1DREditText.setText( sharedpreferences.getString( "disconnection1DREditText",null ) );
        disconnection2DREditText.setText( sharedpreferences.getString( "disconnection2DREditText",null ) );
        disconnection3DREditText.setText( sharedpreferences.getString( "disconnection3DREditText",null ) );
        disconnectionReconnectionEditText.setText( sharedpreferences.getString( "disconnectionReconnectionEditText",null ) );
        relayRoom1RREditText.setText( sharedpreferences.getString( "relayRoom1RREditText",null ) );
        relayRoom2RREditText.setText( sharedpreferences.getString( "relayRoom2RREditText",null ) );
        relayRoom3RREditText.setText( sharedpreferences.getString( "relayRoom3RREditText",null ) );
        relayRoomEditText.setText( sharedpreferences.getString( "relayRoomEditText",null ) );
        recordsActionByEditTxt.setText( sharedpreferences.getString( "recordsActionByEditTxt",null ) );
    }

    private void setMonthsToTextView() {
        signalThisMonthTxtView.setText( thisMonth );
        signalLastMonthTxtView.setText( lastMonth );
        signalLast2ndMonthTxtView.setText( last2ndMonth );
        disconnectionThisMonthTxtView.setText( thisMonth );
        disconnectionLastMonthTxtView.setText( lastMonth );
        disconnectionLast2ndMonthTxtView.setText( last2ndMonth );
        relayThisMonthTxtView.setText( thisMonth );
        relayLastMonthTxtView.setText( lastMonth );
        relayLast2ndMonthTxtView.setText( last2ndMonth );
    }

    private String extract2ndLastMonthFromDate(String selectedInspectDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            Date date = format.parse(selectedInspectDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, -2);
            date = c.getTime();
            return (String) DateFormat.format("MMM",  date);
//            Toast.makeText(getApplicationContext(), prevMonthString, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
            return "2nd Last Month";
        }
    }

    private String extractLastMonthFromDate(String selectedInspectDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            Date date = format.parse(selectedInspectDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, -1);
            date = c.getTime();
            return (String) DateFormat.format("MMM",  date);
//            Toast.makeText(getApplicationContext(), prevMonthString, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
            return "Last Month";
        }
    }

    private String extractMonthFromDate(String selectedInspectDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            Date date = format.parse(selectedInspectDate);
            return (String) DateFormat.format("MMM",  date);
//            Toast.makeText(getApplicationContext(), monthString, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
            return  "This Month";
        }
    }

    private void setActionBySpinner(String selectedDivision, Spinner ActionBySpr) {
        ArrayAdapter<String> adapter;
        String[] actionBy;
        final List<String> actionByList;
        switch (selectedDivision) {
            case "JP":
                actionBy = createActionByList(stationCode,"SSE/Sig/","SSE/Tele/", getResources().getStringArray( R.array.jaipurActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                break;
            case "JU":
                actionBy = createActionByList(stationCode, "SSE/Sig/","SSE/Tele/",getResources().getStringArray( R.array.jodhpurActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                break;
            case "AII":
                actionBy = createActionByList(stationCode,"SSE/Sig/","SSE/Tele/", getResources().getStringArray( R.array.ajmerActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );
                ActionBySpr.setAdapter( adapter );
                break;
            case "BKN":
                actionBy = createActionByList(stationCode, "SSE/Sig/","SSE/Tele/",getResources().getStringArray( R.array.bikanerActionBy ));
                actionByList = new ArrayList<>( Arrays.asList( actionBy ) );
                adapter = new ArrayAdapter<>( this, R.layout.support_simple_spinner_dropdown_item, actionByList );
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

    private void applyOnItemSelectedListener(final Spinner recordsActionBySpr, final EditText recordsActionByEditTxt) {
        recordsActionBySpr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = recordsActionBySpr.getSelectedItem().toString();
                if(s.equals( "Other" )){
                    recordsActionByEditTxt.setVisibility( View.VISIBLE );
                }
                else{
                    recordsActionByEditTxt.setVisibility( View.GONE );
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

    private  boolean validateUserInput(){
        Boolean isAnyFieldsEmpty = false;
        if(signalPoint1EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalPoint1EditText.setError( "Invalid Input" );
        }
        if(signalPoint2EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalPoint2EditText.setError( "Invalid Input" );
        }
        if(signalPoint3EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalPoint3EditText.setError( "Invalid Input" );
        }
        if(signalTrackCircuit1EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalTrackCircuit1EditText.setError( "Invalid Input" );
        }
        if(signalTrackCircuit2EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalTrackCircuit2EditText.setError( "Invalid Input" );
        }
        if(signalTrackCircuit3EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalTrackCircuit3EditText.setError( "Invalid Input" );
        }
        if(signalBI1EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalBI1EditText.setError( "Invalid Input" );
        }
        if(signalBI2EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalBI2EditText.setError( "Invalid Input" );
        }
        if(signalBI3EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalBI3EditText.setError( "Invalid Input" );
        }
        if(signalPowerSupply1EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalPowerSupply1EditText.setError( "Invalid Input" );
        }
        if(signalPowerSupply2EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalPowerSupply2EditText.setError( "Invalid Input" );
        }
        if(signalPowerSupply3EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalPowerSupply3EditText.setError( "Invalid Input" );
        }
        if(signalOthers1EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalOthers1EditText.setError( "Invalid Input" );
        }
        if(signalOthers2EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalOthers2EditText.setError( "Invalid Input" );
        }
        if(signalOthers3EditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            signalOthers3EditText.setError( "Invalid Input" );
        }
        if(disconnection1DREditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            disconnection1DREditText.setError( "Invalid Input" );
        }
        if(disconnection2DREditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            disconnection2DREditText.setError( "Invalid Input" );
        }
        if(disconnection3DREditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            disconnection3DREditText.setError( "Invalid Input" );
        }
        if(relayRoom1RREditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            relayRoom1RREditText.setError( "Invalid Input" );
        }
        if(relayRoom2RREditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            relayRoom2RREditText.setError( "Invalid Input" );
        }
        if(relayRoom3RREditText.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            relayRoom3RREditText.setError( "Invalid Input" );
        }
        if(recordsActionBySpr.getSelectedItem().toString().equals( "Other" )) {
            if (recordsActionByEditTxt.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                recordsActionByEditTxt.setError( "Invalid Input" );
            }
        }
        return !isAnyFieldsEmpty;
    }

    public void saveRecordsData(View view) {
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
        editor.putBoolean( "recordsActivityComplete",isActivityComplete );
        editor.apply();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {  }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }
}
