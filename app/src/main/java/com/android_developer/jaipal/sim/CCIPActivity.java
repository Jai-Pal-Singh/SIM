package com.android_developer.jaipal.sim;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by hp on 2018-04-02.
 */

public class CCIPActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText lastAnnualDateEtxt;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ccip_activity);

        //Date formatter
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        findViewsById();
        setDateTimeField();

    }

    @Override
    public void onClick(View view) {
        datePicker.show();
    }


    private void findViewsById() {
        lastAnnualDateEtxt = (EditText) findViewById(R.id.lastAnnualDateEditText);
        lastAnnualDateEtxt.setInputType( InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        lastAnnualDateEtxt.setOnClickListener(new View.OnClickListener() {
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
                lastAnnualDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
}
