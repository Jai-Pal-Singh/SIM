package com.android_developer.jaipal.sim;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by hp on 2018-04-01.
 */

class DetailActivity extends AppCompatActivity {

    private EditText dateOfTestingEtxt;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    private Integer position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        ScrollView scroll_view = (ScrollView)findViewById(R.id.groupsScrollView);
        scroll_view.fullScroll(ScrollView.FOCUS_UP);

        position = getIntent().getIntExtra( "position" ,0);
        //Date formatter
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();

        setDateTimeField();
    }
    private void findViewsById() {
        dateOfTestingEtxt = (EditText) findViewById(R.id.TestingDateEditText);
        dateOfTestingEtxt.setInputType( InputType.TYPE_NULL);
        dateOfTestingEtxt.requestFocus();
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

    }
}
