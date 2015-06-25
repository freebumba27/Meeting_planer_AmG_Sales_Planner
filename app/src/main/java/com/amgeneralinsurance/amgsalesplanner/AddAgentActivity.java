package com.amgeneralinsurance.amgsalesplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.utils.ReuseableClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AddAgentActivity extends AppCompatActivity {

    private GoogleMap mMap;
    EditText editTextDate;
    EditText editTextMeetingStartTime;
    EditText editTextMeetingEndTime;
    EditText editTextObjective;
    Spinner SpinnerAgencyName;
    Spinner spinnerPurpose;
    String agencyName = "nothing";
    String agencyCode = "nothing";
    String agencyLat = "nothing";
    String agencyLng = "nothing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agent);
        setUpMapIfNeeded();

        editTextDate             = (EditText)findViewById(R.id.editTextDate);
        editTextMeetingStartTime = (EditText)findViewById(R.id.editTextMeetingStartTime);
        editTextMeetingEndTime   = (EditText)findViewById(R.id.editTextMeetingEndTime);
        editTextObjective        = (EditText)findViewById(R.id.editTextObjective);
        SpinnerAgencyName        = (Spinner)findViewById(R.id.SpinnerAgencyName);
        spinnerPurpose           = (Spinner)findViewById(R.id.spinnerPurpose);

        populateSpinner();
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //name value pair Spinner
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void populateSpinner()
    {
        SQLiteDatabase db = ReuseableClass.createAndOpenDb(this);
        Cursor cur = db.rawQuery("SELECT * FROM agent_tbl", null);

        final MyData items[] = new MyData[cur.getCount()+1];
        items[0] = new MyData( "Select Agency", "0", "0", "0" );
        if(cur.moveToNext())
        {
            int i = 1;
            do
            {
                items[i] = new MyData( cur.getString(2),cur.getString(1),cur.getString(3),cur.getString(4) );
                i++;
            }while (cur.moveToNext());
        }
        cur.close();
        db.close();

        ArrayAdapter<MyData> adapter = new ArrayAdapter<MyData>( this,android.R.layout.simple_spinner_item,items );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerAgencyName.setAdapter(adapter);
        SpinnerAgencyName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
              {
                  public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                  {
                      MyData d = items[position];

                      agencyLat = d.getLat();
                      agencyLng = d.getLng();

                      mMap.clear();
                      if (mMap != null) {
                          mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(agencyLat), Double.parseDouble(agencyLng))).title(d.getSpinnerText()));
                      }

                      CameraPosition cameraPosition = new CameraPosition.Builder()
                              .target(new LatLng(Double.parseDouble(agencyLat), Double.parseDouble(agencyLng))) // Center Set
                                      .zoom(18.0f)                // Zoom
                                      .bearing(90)                // Orientation of the camera to east
                                      .tilt(30)                   // Tilt of the camera to 30 degrees
                                      .build();                   // Creates a CameraPosition from the builder
                      mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                      //Toast.makeText(AddAgentActivity.this, "Value: " + d.getValue() + " Name: " + d.getSpinnerText(), Toast.LENGTH_LONG).show();
                      agencyName = d.getSpinnerText();
                      agencyCode = d.getValue();
                  }

                  public void onNothingSelected(AdapterView<?> parent)
                  {
                      //On Nothing selection do something
                  }
              }
        );
    }

    public void addingPlan(View view) {

        if(agencyName.equalsIgnoreCase("Select Agency") || agencyCode.equalsIgnoreCase("0") || agencyName.equalsIgnoreCase("nothing") ||
                agencyCode.equalsIgnoreCase("nothing") || editTextDate.getText().toString().length()==0 ||
                editTextMeetingStartTime.getText().toString().length()==0 || editTextMeetingEndTime.getText().toString().length()==0 ||
                spinnerPurpose.getSelectedItem().toString().equalsIgnoreCase("Select your purpose") || editTextObjective.getText().toString().length()==0)
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
                    String inputPattern = "dd-MM-yyyy";
                    String outputPattern = "yyyyMMdd";
                    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                    Log.d("tag", "insert into plan_tbl (agency_name, agency_id, agency_lat, agency_lng, meeting_date, meeting_start_time, meeting_end_time, " +
                            "purpose, objective, completed, meeting_date_modified) values ('"+agencyName+"','"+agencyCode+"','"+agencyLat+"','"+agencyLng+"','"+editTextDate.getText().toString()+"','"
                            +editTextMeetingStartTime.getText().toString()+"','"+editTextMeetingEndTime.getText().toString()+"','"+spinnerPurpose.getSelectedItem().toString()
                            +"','"+editTextObjective.getText().toString()+"','0', '"+outputFormat.format(inputFormat.parse(editTextDate.getText().toString()))+"');");

                            db.execSQL("insert into plan_tbl (agency_name, agency_id, agency_lat, agency_lng, meeting_date, meeting_start_time, meeting_end_time, " +
                            "purpose, objective, completed, meeting_date_modified) values ('"+agencyName+"','"+agencyCode+"','"+agencyLat+"','"+agencyLng+"','"+editTextDate.getText().toString()+"','"
                            +editTextMeetingStartTime.getText().toString()+"','"+editTextMeetingEndTime.getText().toString()+"','"+spinnerPurpose.getSelectedItem().toString()
                            +"','"+editTextObjective.getText().toString()+"','0', '"+outputFormat.format(inputFormat.parse(editTextDate.getText().toString()))+"');");

                    Toast.makeText(this, "Thanks for saving your plan.\nWish you all the best to archive it!!",Toast.LENGTH_LONG).show();

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

    class MyData
    {
        public MyData( String spinnerText, String value,  String lat, String lng)
        {
            this.spinnerText = spinnerText;
            this.value = value;
            this.lat = lat;
            this.lng = lng;
        }

        public String getSpinnerText() {
            return spinnerText;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return spinnerText;
        }

        String spinnerText;
        String value;
        String lat;
        String lng;

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //END name value pair Spinner
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ListOfAgentActivity.class);
        finish();
        startActivity(i);
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Google Map
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

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
    //Google Map END
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Date picker
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void datePicker(View v)
    {
        Calendar mcurrentDate=Calendar.getInstance();
        int mYear=mcurrentDate.get(Calendar.YEAR);
        int mMonth=mcurrentDate.get(Calendar.MONTH);
        int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker=new DatePickerDialog(AddAgentActivity.this, new DatePickerDialog.OnDateSetListener()
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
        mDatePicker.setTitle("Select your purpose");
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
        mTimePicker = new TimePickerDialog(AddAgentActivity.this, new TimePickerDialog.OnTimeSetListener()
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
        mTimePicker = new TimePickerDialog(AddAgentActivity.this, new TimePickerDialog.OnTimeSetListener()
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
