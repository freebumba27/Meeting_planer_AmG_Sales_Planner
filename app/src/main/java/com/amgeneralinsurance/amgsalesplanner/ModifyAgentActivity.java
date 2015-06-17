package com.amgeneralinsurance.amgsalesplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Calendar;

public class ModifyAgentActivity extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    EditText editTextDate;
    EditText editTextMeetingStartTime;
    EditText editTextMeetingEndTime;
    EditText EditTextAgencyName;
    EditText EditTextPurpose;
    EditText EditTextObjective;
    String plan_id = "nothing";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_agent);
        setUpMapIfNeeded();

        editTextDate             = (EditText)findViewById(R.id.editTextDate);
        editTextMeetingStartTime = (EditText)findViewById(R.id.editTextMeetingStartTime);
        editTextMeetingEndTime   = (EditText)findViewById(R.id.editTextMeetingEndTime);
        EditTextAgencyName       = (EditText)findViewById(R.id.EditTextAgencyName);
        EditTextPurpose          = (EditText)findViewById(R.id.editTextPurpose);
        EditTextObjective = (EditText)findViewById(R.id.editTextObjective);

        plan_id = getIntent().getStringExtra("plan_id");
        editTextDate.setText(getIntent().getStringExtra("editTextDate"));
        editTextMeetingStartTime.setText(getIntent().getStringExtra("start_time"));
        editTextMeetingEndTime.setText(getIntent().getStringExtra("end_time"));
        EditTextAgencyName.setText(getIntent().getStringExtra("agency_name"));
        EditTextPurpose.setText(getIntent().getStringExtra("purpose"));
        EditTextObjective.setText(getIntent().getStringExtra("objective"));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ListOfAgentActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Date picker
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void datePicker(View v)
    {
        Calendar mcurrentDate=Calendar.getInstance();
        int mYear=mcurrentDate.get(Calendar.YEAR);
        int mMonth=mcurrentDate.get(Calendar.MONTH);
        int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker=new DatePickerDialog(ModifyAgentActivity.this, new DatePickerDialog.OnDateSetListener()
        {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday)
            {
                String selectedmonthModified = (selectedmonth+1)/10==0?("0"+(selectedmonth+1)): String.valueOf((selectedmonth+1));

                editTextDate.setText(new StringBuilder()
                        .append(selectedday).append("-")
                        .append(selectedmonthModified).append("-")
                        .append(selectedyear).append(" "));
            }
        },mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //END Date picker
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Time picker Start Meeting
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void timePickerStartingMeeting(View v)
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ModifyAgentActivity.this, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                String selectedMinuteStr = selectedMinute + "";
                if(selectedMinuteStr.length()<2)
                {
                    selectedMinuteStr = 0 + selectedMinuteStr;
                }

                String selectedHourStr = selectedHour + "";
                if(selectedHourStr.length()<2)
                {
                    selectedHourStr = 0 + selectedHourStr;
                }

                editTextMeetingStartTime.setText("" + selectedHourStr + ":" + selectedMinuteStr);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //END Time picker Start Meeting
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Time picker End Meeting
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void timePickerEndingMeeting(View v)
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ModifyAgentActivity.this, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                String selectedMinuteStr = selectedMinute + "";
                if(selectedMinuteStr.length()<2)
                {
                    selectedMinuteStr = 0 + selectedMinuteStr;
                }

                String selectedHourStr = selectedHour + "";
                if(selectedHourStr.length()<2)
                {
                    selectedHourStr = 0 + selectedHourStr;
                }

                editTextMeetingEndTime.setText("" + selectedHourStr + ":" + selectedMinuteStr);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //END Time picker End Meeting
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
