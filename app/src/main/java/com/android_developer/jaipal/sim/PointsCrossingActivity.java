package com.android_developer.jaipal.sim;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by hp on 2018-04-02.
 */

public class PointsCrossingActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText selectDateEtxt;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    private Integer position;

    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.points_crossing_activity);

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
        selectDateEtxt = (EditText) findViewById(R.id.selectDateEditText);
        selectDateEtxt.setInputType( InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        selectDateEtxt.setOnClickListener(new View.OnClickListener() {
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
                selectDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
}
