package com.android_developer.jaipal.sim;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hp on 2018-04-02.
 */

public class TrackCircuitsActivity extends AppCompatActivity{

    private String selectedDivision="", stationCode="";
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;
    private Spinner trackCircuitsActionBySpr;
    EditText trackCircuitsActionByEditTxt, tracksDeficiencyEditText;
    CheckBox doubleBondingCheckBox,JTypeCheckBox,bothRailsCheckBox, specificGravityCheckBox,relayEndVoltageCheckBox, recordsTCCheckBox;
    Boolean isActivityComplete = false;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_circuits_activity);

        initialUISetup();
        getDivisionFromSharedPreferences();
        getSavedDataFromSharedPreferences();
        isActivityComplete = false;
        applyOnFocusChangeListener( trackCircuitsActionByEditTxt );
        setActionBySpinner(selectedDivision,trackCircuitsActionBySpr);
        applyOnItemSelectedListener(trackCircuitsActionBySpr,trackCircuitsActionByEditTxt);
    }

    private void getDivisionFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        selectedDivision = sharedpreferences.getString("division", null);
        stationCode = sharedpreferences.getString( "stationCode",null );
    }

    private void getSavedDataFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        doubleBondingCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.double_bonding_has_been_done_on_all_the_continuous_rail_joints_and_sejs ), false ) );
        JTypeCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.j_type_pandrol_clip_has_been_used_at_glued_joints_to_avoid_shorting_of_tc ), false ) );
        bothRailsCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.when_both_rails_are_shorted_using_tsr_tc_indication_on_panel_is_red_including_track_circuited_sidings ), false ) );
        specificGravityCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.specific_gravity_of_tc_battery_is_in_between_1180_1220 ), false ) );
        relayEndVoltageCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.relay_end_voltage_is_less_than_3_times_of_pick_up_voltage_of_tr_qt2_qta2_type ), false ) );
        recordsTCCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.records_of_tc_parameters_were_maintained_and_placed_in_respective_location_boxes ), false ) );
        tracksDeficiencyEditText.setText( sharedpreferences.getString("tracksDeficiencyEditText", null) );
        trackCircuitsActionByEditTxt.setText( sharedpreferences.getString( "trackCircuitsActionByEditText",null ) );
    }

    private void initialUISetup() {
        doubleBondingCheckBox = findViewById( R.id.doubleBondingCheckBox );
        JTypeCheckBox = findViewById( R.id.JTypeCheckBox );
        bothRailsCheckBox = findViewById( R.id.bothRailsCheckBox );
        specificGravityCheckBox = findViewById( R.id.specificGravityCheckBox );
        relayEndVoltageCheckBox = findViewById( R.id.relayEndVoltageCheckBox );
        recordsTCCheckBox = findViewById( R.id.recordsTCCheckBox );
        tracksDeficiencyEditText = findViewById( R.id.tracksDeficiencyEditText );
        trackCircuitsActionBySpr = findViewById( R.id.trackCircuitsActionBySpinner ) ;
        trackCircuitsActionByEditTxt = findViewById( R.id.trackCircuitsActionByEditText );
    }

    private void applyOnItemSelectedListener(final Spinner trackCircuitsActionBySpr, final EditText trackCircuitsActionByEditTxt) {
        trackCircuitsActionBySpr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = trackCircuitsActionBySpr.getSelectedItem().toString();
                if(s.equals( "Other" )){
                    trackCircuitsActionByEditTxt.setVisibility( View.VISIBLE );
                }
                else{
                    trackCircuitsActionByEditTxt.setVisibility( View.GONE );
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
        if(trackCircuitsActionBySpr.getSelectedItem().toString().equals( "Other" )) {
            if (trackCircuitsActionByEditTxt.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                trackCircuitsActionByEditTxt.setError( "Invalid Input" );
            }
        }
        return !isAnyFieldsEmpty;
    }

    public void saveTrackCircuitsData(View view) {
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
        editor.putBoolean( "trackCircuitsActivityComplete",isActivityComplete );
        editor.apply();
    }

    private void saveDataInSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(getResources().getString( R.string.double_bonding_has_been_done_on_all_the_continuous_rail_joints_and_sejs ), doubleBondingCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.j_type_pandrol_clip_has_been_used_at_glued_joints_to_avoid_shorting_of_tc ), JTypeCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.when_both_rails_are_shorted_using_tsr_tc_indication_on_panel_is_red_including_track_circuited_sidings ), bothRailsCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.specific_gravity_of_tc_battery_is_in_between_1180_1220 ), specificGravityCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.relay_end_voltage_is_less_than_3_times_of_pick_up_voltage_of_tr_qt2_qta2_type ), relayEndVoltageCheckBox.isChecked());
        editor.putBoolean(getResources().getString( R.string.records_of_tc_parameters_were_maintained_and_placed_in_respective_location_boxes ), recordsTCCheckBox.isChecked());
        editor.putString( "tracksDeficiencyEditText", tracksDeficiencyEditText.getText().toString() );
        editor.putString( "trackCircuitsActionBySpinner",trackCircuitsActionBySpr.getSelectedItem().toString() );
        editor.putString( "trackCircuitsActionByEditText",trackCircuitsActionByEditTxt.getText().toString() );
        editor.putBoolean( "trackCircuitsActivityComplete",isActivityComplete );
        editor.apply();
    }
}
