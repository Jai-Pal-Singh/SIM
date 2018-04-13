package com.android_developer.jaipal.sim;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String[] division = { "JP", "JU", "AII", "BKN", };
    private EditText dateOfInspectionEtxt;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    private Button proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner divisionspin = (Spinner) findViewById(R.id.divisionSpinner);
        customSpinnerAdapter adapter=new customSpinnerAdapter(this, android.R.layout.simple_spinner_item, division,
                "Select Division");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionspin.setAdapter(adapter);
        divisionspin.setSelection(adapter.getCount()); //This line is must to show hint

        //Date formatter
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();

        setDateTimeField();

        //proceed Button onclick
        proceedButton = (Button)findViewById(R.id.proceed_Button);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this, MainGearsActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void findViewsById() {
        dateOfInspectionEtxt = (EditText) findViewById(R.id.dateOfInspectionEditText);
        dateOfInspectionEtxt.setInputType(InputType.TYPE_NULL);
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
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
