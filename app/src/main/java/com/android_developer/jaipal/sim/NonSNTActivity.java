package com.android_developer.jaipal.sim;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hp on 2018-04-02.
 */

public class NonSNTActivity extends AppCompatActivity {

    TextView nonSntEngineeringActionByTxtView, nonSntElectricalActionByTxtView, nonSntOperatingActionByTxtView;
    EditText engineeringDeficiencyEditText, electricalDeficiencyEditText, operatingDeficiencyEditText, otherDeficiencyEditText, otherDeficiencyActionEditText;
    private String selectedDivision="";
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs" ;
    Boolean isActivityComplete;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.non_snt_activity);

        initialUISetup();
        getDivisionFromSharedPreferences();
        getSavedDataFromSharedPreferences();
        isActivityComplete = false;
        setActionByForEngineeringDeficiency( selectedDivision );
        setActionByForElectricalDeficiency( selectedDivision );
        setActionByForOperatingDeficiency( selectedDivision );
    }

    private  void initialUISetup(){
        engineeringDeficiencyEditText = findViewById( R.id.engineeringDeficiencyEditText );
        electricalDeficiencyEditText = findViewById( R.id.electricalDeficiencyEditText );
        operatingDeficiencyEditText = findViewById( R.id.operatingDeficiencyEditText );
        otherDeficiencyEditText = findViewById( R.id.otherDeficiencyEditText );
        otherDeficiencyActionEditText = findViewById( R.id.otherDeficiencyActionEditText );
        nonSntEngineeringActionByTxtView = findViewById( R.id.nonSntEngineeringActionBySpinner );
        nonSntElectricalActionByTxtView = findViewById( R.id.nonSntElectricalActionBySpinner );
        nonSntOperatingActionByTxtView = findViewById( R.id.nonSntOperatingActionBySpinner );
    }

    private void saveDataInSharedPreferences() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean( "nonSntActivityComplete",isActivityComplete );
        editor.putString( "engineeringDeficiencyEditText",engineeringDeficiencyEditText.getText().toString() );
        editor.putString( "electricalDeficiencyEditText",electricalDeficiencyEditText.getText().toString() );
        editor.putString( "operatingDeficiencyEditText",operatingDeficiencyEditText.getText().toString() );
        editor.putString( "otherDeficiencyEditText",otherDeficiencyEditText.getText().toString() );
        editor.putString( "otherDeficiencyActionEditText",otherDeficiencyActionEditText.getText().toString() );
        editor.putString( "nonSntEngineeringActionByTxtView",nonSntEngineeringActionByTxtView.getText().toString() );
        editor.putString( "nonSntElectricalActionByTxtView",nonSntElectricalActionByTxtView.getText().toString() );
        editor.putString( "nonSntOperatingActionByTxtView",nonSntOperatingActionByTxtView.getText().toString() );
        editor.apply();
    }

    private void getSavedDataFromSharedPreferences() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        engineeringDeficiencyEditText.setText( sharedpreferences.getString( "engineeringDeficiencyEditText",null ) );
        electricalDeficiencyEditText.setText( sharedpreferences.getString( "electricalDeficiencyEditText",null ) );
        operatingDeficiencyEditText.setText( sharedpreferences.getString( "operatingDeficiencyEditText",null ) );
        otherDeficiencyActionEditText.setText( sharedpreferences.getString( "otherDeficiencyActionEditText",null ) );
    }
    private void getDivisionFromSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        selectedDivision = sharedpreferences.getString("division", null);
    }

    public void saveNonSntData(View view) {
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
        editor.putBoolean( "nonSntActivityComplete",isActivityComplete );
        editor.apply();
    }

    private boolean validateUserInput() {
        Boolean isAnyFieldsEmpty = false;
        if(!otherDeficiencyEditText.getText().toString().isEmpty()) {
            if (otherDeficiencyActionEditText.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                otherDeficiencyActionEditText.setError( "Invalid Input" );
            }
        }
        if(!otherDeficiencyActionEditText.getText().toString().isEmpty()) {
            if (otherDeficiencyEditText.getText().toString().isEmpty()) {
                isAnyFieldsEmpty = true;
                otherDeficiencyEditText.setError( "Invalid Input" );
            }
        }
        return !isAnyFieldsEmpty;
    }

    private void setActionByForEngineeringDeficiency (String selectedDivision) {
        switch (selectedDivision) {
            case "JP":
                nonSntEngineeringActionByTxtView.setText( "Sr. DEN/JP" );
                break;
            case "JU":
                nonSntEngineeringActionByTxtView.setText( "Sr. DEN/JU" );
                break;
            case "AII":
                nonSntEngineeringActionByTxtView.setText( "Sr. DEN/AII" );
                break;
            case "BKN":
                nonSntEngineeringActionByTxtView.setText( "Sr. DEN/BKN" );
                break;
        }
    }

    private void setActionByForElectricalDeficiency (String selectedDivision) {
        switch (selectedDivision) {
            case "JP":
                nonSntElectricalActionByTxtView.setText( "Sr. DEE/JP" );
                break;
            case "JU":
                nonSntElectricalActionByTxtView.setText( "Sr. DEE/JU" );
                break;
            case "AII":
                nonSntElectricalActionByTxtView.setText( "Sr. DEE/AII" );
                break;
            case "BKN":
                nonSntElectricalActionByTxtView.setText( "Sr. DEE/BKN" );
                break;
        }
    }

    private void setActionByForOperatingDeficiency (String selectedDivision) {
        switch (selectedDivision) {
            case "JP":
                nonSntOperatingActionByTxtView.setText( "Sr. DOM/JP" );
                break;
            case "JU":
                nonSntOperatingActionByTxtView.setText( "Sr. DOM/JU" );
                break;
            case "AII":
                nonSntOperatingActionByTxtView.setText( "Sr. DOM/AII" );
                break;
            case "BKN":
                nonSntOperatingActionByTxtView.setText( "Sr. DOM/BKN" );
                break;
        }
    }
}
