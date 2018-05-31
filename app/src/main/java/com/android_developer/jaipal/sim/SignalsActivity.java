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

public class SignalsActivity extends AppCompatActivity {

    CheckBox signalLampCheckBox,VECRDropsCheckBox, signalCascadedCheckBox,redLampCheckBox;
    private String selectedDivision="", stationCode="";
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;
    Spinner signalsActionBySpinner;
    EditText signalsActionByEditText;
    EditText signalNo1EditText,aspect1EditText,voltage1EditText,current1EditText, signalNo2EditText,aspect2EditText,voltage2EditText,current2EditText, signalNo3EditText,aspect3EditText,voltage3EditText,current3EditText, signalNo4EditText,aspect4EditText,voltage4EditText,current4EditText, signalNo5EditText,aspect5EditText,voltage5EditText,current5EditText, signalNo6EditText,aspect6EditText,voltage6EditText,current6EditText, signalNo7EditText,aspect7EditText,voltage7EditText,current7EditText, signalNo8EditText,aspect8EditText,voltage8EditText,current8EditText, signalNo9EditText,aspect9EditText,voltage9EditText,current9EditText, signalNo10EditText,aspect10EditText,voltage10EditText,current10EditText;
    Boolean isActivityComplete;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signals_activity);
        initialUISetup();
        getDivisionFromSharedPreferences();
        getSavedDataFromSharedPreferences();
        isActivityComplete=false;

        applyOnFocusChangeListener( signalsActionByEditText );
        setActionBySpinner(selectedDivision,signalsActionBySpinner,sharedpreferences.getInt( "signalsActionBySpinnerPosition",0));
        applyOnItemSelectedListener(signalsActionBySpinner,signalsActionByEditText);
    }

    private void initialUISetup(){
        signalLampCheckBox = findViewById( R.id.signalLampCheckBox );
        VECRDropsCheckBox = findViewById( R.id.VECRDropsCheckBox );
        signalCascadedCheckBox = findViewById( R.id.signalCascadedCheckBox );
        redLampCheckBox = findViewById( R.id.redLampCheckBox );
        signalNo1EditText = findViewById( R.id.signalNo1EditText );
        aspect1EditText = findViewById( R.id.aspect1EditText );
        voltage1EditText = findViewById( R.id.voltage1EditText );
        current1EditText = findViewById( R.id.current1EditText );
        signalNo2EditText = findViewById( R.id.signalNo2EditText );
        aspect2EditText = findViewById( R.id.aspect2EditText );
        voltage2EditText = findViewById( R.id.voltage2EditText );
        current2EditText = findViewById( R.id.current2EditText );
        signalNo3EditText = findViewById( R.id.signalNo3EditText );
        aspect3EditText = findViewById( R.id.aspect3EditText );
        voltage3EditText = findViewById( R.id.voltage3EditText );
        current3EditText = findViewById( R.id.current3EditText );
        signalNo4EditText = findViewById( R.id.signalNo4EditText );
        aspect4EditText = findViewById( R.id.aspect4EditText );
        voltage4EditText = findViewById( R.id.voltage4EditText );
        current4EditText = findViewById( R.id.current4EditText );
        signalNo5EditText = findViewById( R.id.signalNo5EditText );
        aspect5EditText = findViewById( R.id.aspect5EditText );
        voltage5EditText = findViewById( R.id.voltage5EditText );
        current5EditText = findViewById( R.id.current5EditText );
        signalNo6EditText = findViewById( R.id.signalNo6EditText );
        aspect6EditText = findViewById( R.id.aspect6EditText );
        voltage6EditText = findViewById( R.id.voltage6EditText );
        current6EditText = findViewById( R.id.current6EditText );
        signalNo7EditText = findViewById( R.id.signalNo7EditText );
        aspect7EditText = findViewById( R.id.aspect7EditText );
        voltage7EditText = findViewById( R.id.voltage7EditText );
        current7EditText = findViewById( R.id.current7EditText );
        signalNo8EditText = findViewById( R.id.signalNo8EditText );
        aspect8EditText = findViewById( R.id.aspect8EditText );
        voltage8EditText = findViewById( R.id.voltage8EditText );
        current8EditText = findViewById( R.id.current8EditText );
        signalNo9EditText = findViewById( R.id.signalNo9EditText );
        aspect9EditText = findViewById( R.id.aspect9EditText );
        voltage9EditText = findViewById( R.id.voltage9EditText );
        current9EditText = findViewById( R.id.current9EditText );
        signalNo10EditText = findViewById( R.id.signalNo10EditText );
        aspect10EditText = findViewById( R.id.aspect10EditText );
        voltage10EditText = findViewById( R.id.voltage10EditText );
        current10EditText = findViewById( R.id.current10EditText );
        signalsActionBySpinner  = findViewById( R.id.signalsActionBySpinner );
        signalsActionByEditText  = findViewById( R.id.signalsActionByEditText );
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
        editor.putString( "signalNo1EditText", signalNo1EditText.getText().toString() );
        editor.putString( "aspect1EditText", aspect1EditText.getText().toString() );
        editor.putString( "voltage1EditText", voltage1EditText.getText().toString() );
        editor.putString( "current1EditText", current1EditText.getText().toString() );
        editor.putString( "signalNo2EditText", signalNo2EditText.getText().toString() );
        editor.putString( "aspect2EditText", aspect2EditText.getText().toString() );
        editor.putString( "voltage2EditText", voltage2EditText.getText().toString() );
        editor.putString( "current2EditText", current2EditText.getText().toString() );
        editor.putString( "signalNo3EditText", signalNo3EditText.getText().toString() );
        editor.putString( "aspect3EditText", aspect3EditText.getText().toString() );
        editor.putString( "voltage3EditText", voltage3EditText.getText().toString() );
        editor.putString( "current3EditText", current3EditText.getText().toString() );
        editor.putString( "signalNo4EditText", signalNo4EditText.getText().toString() );
        editor.putString( "aspect4EditText", aspect4EditText.getText().toString() );
        editor.putString( "voltage4EditText", voltage4EditText.getText().toString() );
        editor.putString( "current4EditText", current4EditText.getText().toString() );
        editor.putString( "signalNo5EditText", signalNo5EditText.getText().toString() );
        editor.putString( "aspect5EditText", aspect5EditText.getText().toString() );
        editor.putString( "voltage5EditText", voltage5EditText.getText().toString() );
        editor.putString( "current5EditText", current5EditText.getText().toString() );
        editor.putString( "signalNo6EditText", signalNo6EditText.getText().toString() );
        editor.putString( "aspect6EditText", aspect6EditText.getText().toString() );
        editor.putString( "voltage6EditText", voltage6EditText.getText().toString() );
        editor.putString( "current6EditText", current6EditText.getText().toString() );
        editor.putString( "signalNo7EditText", signalNo7EditText.getText().toString() );
        editor.putString( "aspect7EditText", aspect7EditText.getText().toString() );
        editor.putString( "voltage7EditText", voltage7EditText.getText().toString() );
        editor.putString( "current7EditText", current7EditText.getText().toString() );
        editor.putString( "signalNo8EditText", signalNo8EditText.getText().toString() );
        editor.putString( "aspect8EditText", aspect8EditText.getText().toString() );
        editor.putString( "voltage8EditText", voltage8EditText.getText().toString() );
        editor.putString( "current8EditText", current8EditText.getText().toString() );
        editor.putString( "signalNo9EditText", signalNo9EditText.getText().toString() );
        editor.putString( "aspect9EditText", aspect9EditText.getText().toString() );
        editor.putString( "voltage9EditText", voltage9EditText.getText().toString() );
        editor.putString( "current9EditText", current9EditText.getText().toString() );
        editor.putString( "signalNo10EditText", signalNo10EditText.getText().toString() );
        editor.putString( "aspect10EditText", aspect10EditText.getText().toString() );
        editor.putString( "voltage10EditText", voltage10EditText.getText().toString() );
        editor.putString( "current10EditText", current10EditText.getText().toString() );
        editor.putString( "signalsActionBySpinner",signalsActionBySpinner.getSelectedItem().toString() );
        editor.putInt( "signalsActionBySpinnerPosition",signalsActionBySpinner.getSelectedItemPosition() );
        editor.putString( "signalsActionByEditText",signalsActionByEditText.getText().toString() );
        editor.apply();
    }

    private void getSavedDataFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        signalLampCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.signal_lamp_voltae_90_of_rated_value ), false ) );
        VECRDropsCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.vecr_drops_with_fusing_of_minimum_3_route_leds ), false ) );
        signalCascadedCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.signals_are_cascaded_for_e_g_fusing_of_a_signal_s_particular_aspect_other_than_red_illuminates_a_more_restrictive_aspect ), false ) );
        redLampCheckBox.setChecked( sharedpreferences.getBoolean( getResources().getString( R.string.red_lamp_protection_working_e_g_fusing_of_red_aspect_of_signal_should_illuminate_signal_in_rear_with_most_restrictive_aspect_red ), false ) );
        signalNo1EditText.setText( sharedpreferences.getString("signalNo1EditText", null) );
        aspect1EditText.setText( sharedpreferences.getString("aspect1EditText", null) );
        voltage1EditText.setText( sharedpreferences.getString("voltage1EditText", null) );
        current1EditText.setText( sharedpreferences.getString("current1EditText", null) );

        signalNo2EditText.setText( sharedpreferences.getString("signalNo2EditText", null) );
        aspect2EditText.setText( sharedpreferences.getString("aspect2EditText", null) );
        voltage2EditText.setText( sharedpreferences.getString("voltage2EditText", null) );
        current2EditText.setText( sharedpreferences.getString("current2EditText", null) );

        signalNo3EditText.setText( sharedpreferences.getString("signalNo3EditText", null) );
        aspect3EditText.setText( sharedpreferences.getString("aspect3EditText", null) );
        voltage3EditText.setText( sharedpreferences.getString("voltage3EditText", null) );
        current3EditText.setText( sharedpreferences.getString("current3EditText", null) );

        signalNo4EditText.setText( sharedpreferences.getString("signalNo4EditText", null) );
        aspect4EditText.setText( sharedpreferences.getString("aspect4EditText", null) );
        voltage4EditText.setText( sharedpreferences.getString("voltage4EditText", null) );
        current4EditText.setText( sharedpreferences.getString("current4EditText", null) );

        signalNo5EditText.setText( sharedpreferences.getString("signalNo5EditText", null) );
        aspect5EditText.setText( sharedpreferences.getString("aspect5EditText", null) );
        voltage5EditText.setText( sharedpreferences.getString("voltage5EditText", null) );
        current5EditText.setText( sharedpreferences.getString("current5EditText", null) );

        signalNo6EditText.setText( sharedpreferences.getString("signalNo6EditText", null) );
        aspect6EditText.setText( sharedpreferences.getString("aspect6EditText", null) );
        voltage6EditText.setText( sharedpreferences.getString("voltage6EditText", null) );
        current6EditText.setText( sharedpreferences.getString("current6EditText", null) );

        signalNo7EditText.setText( sharedpreferences.getString("signalNo7EditText", null) );
        aspect7EditText.setText( sharedpreferences.getString("aspect7EditText", null) );
        voltage7EditText.setText( sharedpreferences.getString("voltage7EditText", null) );
        current7EditText.setText( sharedpreferences.getString("current7EditText", null) );

        signalNo8EditText.setText( sharedpreferences.getString("signalNo8EditText", null) );
        aspect8EditText.setText( sharedpreferences.getString("aspect8EditText", null) );
        voltage8EditText.setText( sharedpreferences.getString("voltage8EditText", null) );
        current8EditText.setText( sharedpreferences.getString("current8EditText", null) );

        signalNo9EditText.setText( sharedpreferences.getString("signalNo9EditText", null) );
        aspect9EditText.setText( sharedpreferences.getString("aspect9EditText", null) );
        voltage9EditText.setText( sharedpreferences.getString("voltage9EditText", null) );
        current9EditText.setText( sharedpreferences.getString("current9EditText", null) );

        signalNo10EditText.setText( sharedpreferences.getString("signalNo10EditText", null) );
        aspect10EditText.setText( sharedpreferences.getString("aspect10EditText", null) );
        voltage10EditText.setText( sharedpreferences.getString("voltage10EditText", null) );
        current10EditText.setText( sharedpreferences.getString("current10EditText", null) );

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
        return !isAnyFieldsEmpty;
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
}
