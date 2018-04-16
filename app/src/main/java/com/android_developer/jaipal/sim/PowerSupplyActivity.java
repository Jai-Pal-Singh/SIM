package com.android_developer.jaipal.sim;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by hp on 2018-04-02.
 */

public class PowerSupplyActivity extends AppCompatActivity implements View.OnClickListener{

    String ipsMakeSpinnerVal, eiMakeSpinnerVal;
    Spinner ipsMakespnr, eiMakeSpnr;
    EditText ipsMakeEditTxt, eiMakeEditTxt;
    private EditText dataLoggerDate, eiDate;
    private DatePickerDialog dataLoggerDatePicker, eiDatePicker;
    private SimpleDateFormat dataLoggerDateFormatter, eiDateFormatter;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.power_supply_activity);
        /////////////////////////////////////////////////////////////////////////////////////////////////
        ipsMakespnr = (Spinner) findViewById( R.id.ipsMakeSpinner ) ;
        ipsMakeEditTxt = (EditText) findViewById( R.id.ipsMakeEditText );
        ipsMakespnr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ipsMakeSpinnerVal = ipsMakespnr.getSelectedItem().toString();
                if(ipsMakeSpinnerVal.equals( "Other" )){
                    ipsMakeEditTxt.setVisibility( View.VISIBLE );
                }
                else{
                    ipsMakeEditTxt.setVisibility( View.GONE );
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        } );

        eiMakeSpnr = (Spinner) findViewById( R.id.eiMakeSpinner ) ;
        eiMakeEditTxt = (EditText) findViewById( R.id.eiMakeEditText );
        eiMakeSpnr.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                eiMakeSpinnerVal = eiMakeSpnr.getSelectedItem().toString();
                if(eiMakeSpinnerVal.equals( "Other" )){
                    eiMakeEditTxt.setVisibility( View.VISIBLE );
                }
                else{
                    eiMakeEditTxt.setVisibility( View.GONE );
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        } );
        //////////////////////////////////////////////////////////////////////////////////////////////////
        //Date formatter
        dataLoggerDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        findViewsByIdDataLogger();
        setDateTimeFieldDataLogger();

        eiDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        findViewsByIdEI();
        setDateTimeFieldEI();
    }

    @Override
    public void onClick(View view) {
        dataLoggerDatePicker.show();
        eiDatePicker.show();
    }


    private void findViewsByIdDataLogger() {
        dataLoggerDate = (EditText) findViewById(R.id.lastValidationEditText);
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

    }

    private void findViewsByIdEI() {
        eiDate = (EditText) findViewById(R.id.lastSystemOnEditText);
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

    }
}
