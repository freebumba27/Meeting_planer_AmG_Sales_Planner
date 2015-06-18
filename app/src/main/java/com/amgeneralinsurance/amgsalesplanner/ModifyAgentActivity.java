package com.amgeneralinsurance.amgsalesplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.utils.ReuseableClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ModifyAgentActivity extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    EditText editTextDate;
    EditText editTextMeetingStartTime;
    EditText editTextMeetingEndTime;
    EditText EditTextAgencyName;
    EditText EditTextPurpose;
    EditText EditTextObjective;
    EditText editTextOutCome;
    CheckBox checkBoxCompleted;
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
        EditTextObjective        = (EditText)findViewById(R.id.editTextObjective);
        editTextOutCome          = (EditText)findViewById(R.id.editTextOutCome);
        checkBoxCompleted        = (CheckBox)findViewById(R.id.checkBoxCompleted);

        plan_id = getIntent().getStringExtra("plan_id");
        editTextDate.setText(getIntent().getStringExtra("date"));
        editTextMeetingStartTime.setText(getIntent().getStringExtra("start_time"));
        editTextMeetingEndTime.setText(getIntent().getStringExtra("end_time"));
        EditTextAgencyName.setText(getIntent().getStringExtra("agency_name"));
        EditTextPurpose.setText(getIntent().getStringExtra("purpose"));
        EditTextObjective.setText(getIntent().getStringExtra("objective"));
        editTextOutCome.setText(getIntent().getStringExtra("outcome"));
        if(getIntent().getStringExtra("completed").equalsIgnoreCase("0"))
            checkBoxCompleted.setChecked(false);
        else
            checkBoxCompleted.setChecked(true);
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


    public void modifyingPlan(View view) {
        if(editTextDate.getText().toString().length()==0 || editTextMeetingStartTime.getText().toString().length()==0 ||
                editTextMeetingEndTime.getText().toString().length()==0 ||EditTextObjective.getText().toString().length()==0 ||
                editTextOutCome.getText().toString().length()==0)
        {
            Toast.makeText(this, "All Fields are mandatory !!", Toast.LENGTH_LONG).show();
        }
        else
        {
            String fromTime = editTextMeetingStartTime.getText().toString();
            String toTime   = editTextMeetingEndTime.getText().toString();
            String fromDate = editTextDate.getText().toString();

            long fromMilliSec = dateTimeToMilisec(fromDate + " " + fromTime);
            long toMilliSec = dateTimeToMilisec(fromDate + " " + toTime);

            long diffInMin = TimeUnit.MILLISECONDS.toMinutes(toMilliSec - fromMilliSec);
            System.out.println("Different in min :: " + diffInMin);
            if (diffInMin > 0) {
                SQLiteDatabase db = ReuseableClass.createAndOpenDb(this);
                try {
                    ContentValues values=new ContentValues();
                    values.put("date",editTextDate.getText().toString());
                    values.put("meeting_start_time",editTextMeetingStartTime.getText().toString());
                    values.put("meeting_end_time",editTextMeetingEndTime.getText().toString());
                    values.put("objective",EditTextObjective.getText().toString());
                    values.put("outcome",editTextOutCome.getText().toString());
                    if(checkBoxCompleted.isChecked())
                        values.put("completed",1);
                    else
                        values.put("completed",0);

                    int id = db.update("plan_tbl",values,"id='"+plan_id+"'",null);
                    Log.d("TAG", "Updated Plan Id: " + id);
                    Toast.makeText(this, "Thanks for updating your plan.\nWish you all the best to archive it!!",Toast.LENGTH_LONG).show();

                    Intent i = new Intent(this, ListOfAgentActivity.class);
                    finish();
                    startActivity(i);

                } catch (Exception e) {
                    Log.e("TAG", "Error while inserting: " + e);
                    Toast.makeText(this, "Sorry!! Some error occurred try again.",Toast.LENGTH_LONG).show();
                }
                finally {
                    db.close();
                }
            }
            else{
                Toast.makeText(this, "Meeting should end after starting !!", Toast.LENGTH_LONG).show();
            }

        }
    }

    public long dateTimeToMilisec(String givenDateString) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date Time in milli :: " + timeInMilliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }
}
