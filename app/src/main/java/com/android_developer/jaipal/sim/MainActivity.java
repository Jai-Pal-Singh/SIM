package com.android_developer.jaipal.sim;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,NavigationView.OnNavigationItemSelectedListener{

    private String[] division = { "Select Division","JP", "JU", "AII", "BKN", };
    private String  selectedDivision="", selectedDate, selectedStation="", stationCode="";
    private EditText dateOfInspectionEtxt,inspectingAuthName,inspectingAuthDesignation;
    private TextView divisionItemTxtView;
    boolean divisionFlag = false;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    private Button proceedButton;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    boolean doubleBackToExitPressedOnce = false;
    private Spinner divisionspin, stationCodeSpin;
    private Boolean isAnyFieldsEmpty = false;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invalidateOptionsMenu();
        findViewsById();
        getSavedDataFromSharedPreferences();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //sidebar navigation
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout1);
        mToggle = new ActionBarDrawerToggle( this, mDrawerLayout,R.string.open,R.string.close );
        mDrawerLayout.addDrawerListener( mToggle );
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        NavigationView navigationView = (NavigationView) findViewById(R.id.mainNavigationView);
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        if (sharedpreferences.getString( "admin", "0" ).equals( "1" )) {
            navigationView.getMenu().findItem(R.id.Register).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.Register).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener( this );
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Custom Spinner for Division to show hint
        divisionspin = findViewById(R.id.divisionSpinner);
        final List<String> divisionList = new ArrayList<>( Arrays.asList( division ) );
        final ArrayAdapter<String> divisionSpinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.division_spinner_item_activity,divisionList){
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        divisionSpinnerArrayAdapter.setDropDownViewResource(R.layout.division_spinner_item_activity);
        divisionspin.setAdapter(divisionSpinnerArrayAdapter);
        divisionspin.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedDivision = (String) adapterView.getItemAtPosition( position );
                if(position > 0){
                    // Notify the selected item text
                    TextView tv = (TextView) view;
                    tv.setTextColor(Color.BLACK);
                    setActionBySpinner( selectedDivision, stationCodeSpin );
                    isAnyFieldsEmpty = false;
                }
                else
                    isAnyFieldsEmpty = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );
        int spinnerPosition = divisionSpinnerArrayAdapter.getPosition(selectedDivision);
        //set the default according to value
//        divisionspin.setSelection(spinnerPosition);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Date formatter
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setDateTimeField();

//        applyOnFocusChangeListener( divisionspin );
        applyOnFocusChangeListener(dateOfInspectionEtxt  );
//        applyOnFocusChangeListener(stationCodeEtxt  );
        applyOnFocusChangeListener(inspectingAuthName  );
        applyOnFocusChangeListener(inspectingAuthDesignation  );
        setActionBySpinner(selectedDivision,stationCodeSpin);
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        //Item Selected Validation
        divisionspin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView divisionSpinner = (TextView) divisionspin.getSelectedItem();
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if(!TextUtils.isEmpty(selectedDivision)){
                        divisionFlag = true;
                    }
                    else {
                        divisionSpinner.setError( "Invalid Input" );
                    }
                }
                else
                divisionSpinner.setError( null );
                ////////////////////////////////////////////////////////////////////////////////////////////////////
            }
        });
    }

    private void findViewsById() {
        dateOfInspectionEtxt = findViewById(R.id.dateOfInspectionEditText);
        dateOfInspectionEtxt.setInputType(InputType.TYPE_NULL);
        stationCodeSpin = findViewById( R.id.stationCodeSpinner );
        inspectingAuthName = findViewById( R.id.officerNameEditText);
        inspectingAuthDesignation = findViewById( R.id.officerDesignationEditText );
        divisionItemTxtView = findViewById( R.id.divisionItemSpinner );
        proceedButton = findViewById( R.id.proceed_Button );
    }

    private void setDateTimeField() {
        dateOfInspectionEtxt.setOnClickListener(new View.OnClickListener() {
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
                dateOfInspectionEtxt.setText(dateFormatter.format(newDate.getTime()));
                selectedDate = dateOfInspectionEtxt.getText().toString();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {   }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    public boolean onOptionsItemSelected (MenuItem item) {
        return mToggle.onOptionsItemSelected( item ) || super.onOptionsItemSelected( item );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.downloadInspectionNotes){
            Intent myIntent = new Intent(MainActivity.this, InspectionNotesActivity.class);
            startActivity(myIntent);
        }
        if(id == R.id.Register){
            Intent myIntent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(myIntent);
        }
        if(id == R.id.AboutITCenter){
            Intent myIntent = new Intent(MainActivity.this, AboutITCenterActivity.class);
            startActivity(myIntent);
        }
        if(id == R.id.AboutDeveloper){
            Intent myIntent = new Intent(MainActivity.this, AboutDeveloper.class);
            startActivity(myIntent);
        }
        if(id == R.id.feedback){
            Intent myIntent = new Intent(MainActivity.this, Feedback.class);
            startActivity(myIntent);
        }
        if(id == R.id.logout){
            finish();
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove( "loggedIn" );
            editor.apply();
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(myIntent);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed( new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void sendData(View view) throws SQLException {
        if( validateUserInput() ) {
            findSelectedStation();
            setSharedPreferences();
            Intent myIntent = new Intent( MainActivity.this, MainGearsActivity.class );
            startActivity( myIntent );
        }
        else{
            Toast.makeText(getApplicationContext(), "Please fill all entires", Toast.LENGTH_SHORT).show();
        }
    }

    private void findSelectedStation() {
        int pos = selectedStation.indexOf( "-" );
        stationCode = selectedStation.substring( pos+2 );
    }

    private void setSharedPreferences() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("division", selectedDivision);
        editor.putString("inspectDate", dateOfInspectionEtxt.getText().toString());
        editor.putString("station", selectedStation);
        editor.putString("authName", inspectingAuthName.getText().toString());
        editor.putString("authDesignation", inspectingAuthDesignation.getText().toString());
        editor.putString("stationCode", stationCode);
        editor.apply();
    }

    private void getSavedDataFromSharedPreferences() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        inspectingAuthName.setText( sharedpreferences.getString( "authName", "" ) );
        inspectingAuthDesignation.setText( sharedpreferences.getString( "authDesignation", "" ) );
    }

    public Boolean validateUserInput() {
//        if(selectedDivision.equals( "Select Division" )){
//            isAnyFieldsEmpty = true;
//            divisionItemTxtView.setError( "Invalid Input" );
//        }
        if(dateOfInspectionEtxt.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
//            dateOfInspectionEtxt.setError( "Invalid Input" );
        }
        if(stationCodeSpin.getSelectedItem().toString().isEmpty()){
            isAnyFieldsEmpty = true;
//            stationCodeSpin.setError( "Invalid Input" );
        }
        if(inspectingAuthName.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            inspectingAuthName.setError( "Invalid Input" );
        }
        if(inspectingAuthDesignation.getText().toString().isEmpty()){
            isAnyFieldsEmpty = true;
            inspectingAuthDesignation.setError( "Invalid Input" );
        }
        if(!isAnyFieldsEmpty)
            return true;
        else
            return false;
    }

    private void setActionBySpinner(String selectedDivision, Spinner ActionBySpr) {
        ArrayAdapter<CharSequence> adapter;
        switch (selectedDivision) {
            case "JP":
                adapter = new ArrayAdapter<CharSequence>( this, R.layout.division_spinner_item_activity, getResources().getStringArray( R.array.stationUnderJaipurDivision ) ){
                    @Override
                    public View getDropDownView(int position, View convertView,ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                adapter.setDropDownViewResource( R.layout.division_spinner_item_activity );
                ActionBySpr.setAdapter( adapter );
                stationCodeSpin.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        selectedStation = (String) adapterView.getItemAtPosition( position );
                        TextView tv = (TextView) view;
                        tv.setTextColor(Color.BLACK);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) { }
                } );
                break;
            case "JU":
                adapter = new ArrayAdapter<CharSequence>( this, R.layout.division_spinner_item_activity, getResources().getStringArray( R.array.stationListOfJodhpurDivision ) ){
                    @Override
                    public View getDropDownView(int position, View convertView,ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                adapter.setDropDownViewResource( R.layout.division_spinner_item_activity );
                ActionBySpr.setAdapter( adapter );
                stationCodeSpin.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        selectedStation = (String) adapterView.getItemAtPosition( position );
                        TextView tv = (TextView) view;
                        tv.setTextColor(Color.BLACK);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) { }
                } );
                break;
            case "AII":
                adapter = new ArrayAdapter<CharSequence>( this, R.layout.division_spinner_item_activity, getResources().getStringArray( R.array.stationListUnderAjmerDivision ) ){
                    @Override
                    public View getDropDownView(int position, View convertView,ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                adapter.setDropDownViewResource( R.layout.division_spinner_item_activity );
                ActionBySpr.setAdapter( adapter );
                stationCodeSpin.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        selectedStation = (String) adapterView.getItemAtPosition( position );
                        TextView tv = (TextView) view;
                        tv.setTextColor(Color.BLACK);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) { }
                } );
                break;
            case "BKN":
                adapter = new ArrayAdapter<CharSequence>( this, R.layout.division_spinner_item_activity, getResources().getStringArray( R.array.stationListBikanerDivision ) ){
                    @Override
                    public View getDropDownView(int position, View convertView,ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                adapter.setDropDownViewResource( R.layout.division_spinner_item_activity );
                ActionBySpr.setAdapter( adapter );
                stationCodeSpin.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        selectedStation = (String) adapterView.getItemAtPosition( position );
                        TextView tv = (TextView) view;
                        tv.setTextColor(Color.BLACK);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) { }
                } );
                break;
            default:
                    adapter = new ArrayAdapter<CharSequence>( this, R.layout.division_spinner_item_activity, getResources().getStringArray( R.array.selectDivision ) ){
                        @Override
                        public View getDropDownView(int position, View convertView,ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            tv.setTextColor( getResources().getColor( R.color.hint ));
                            return view;
                        }
                    };
                    adapter.setDropDownViewResource( R.layout.division_spinner_item_activity );
                    ActionBySpr.setAdapter( adapter );
        }
    }

}
