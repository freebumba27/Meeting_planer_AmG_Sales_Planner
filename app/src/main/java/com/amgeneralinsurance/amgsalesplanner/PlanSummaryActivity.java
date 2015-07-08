package com.amgeneralinsurance.amgsalesplanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;
import com.utils.ReuseableClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlanSummaryActivity extends AppCompatActivity {

    Spinner textViewSelectMonth;
    TextView textViewTotalValue;
    TextView textViewCompleteValue;
    TextView textViewRemainingValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_view);

        textViewSelectMonth     = (Spinner)findViewById(R.id.textViewSelectMonth);
        textViewTotalValue      = (TextView)findViewById(R.id.textViewTotalValue);
        textViewCompleteValue   = (TextView)findViewById(R.id.textViewCompleteValue);
        textViewRemainingValue  = (TextView)findViewById(R.id.textViewRemainingValue);

        textViewSelectMonth.setSelection(Arrays.asList(getResources().getStringArray(R.array.all_months))
                .indexOf((String) android.text.format.DateFormat.format("MMMM", new Date())));
        ((Button)findViewById(R.id.buttonGetDetails)).performClick();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, DashBoardActivity.class);
        finish();
        startActivity(i);
    }

    public void gettingDetails(View view) {

//        String date = textViewSelectMonth.getSelectedItem().toString().substring(0,1);
//        String month = textViewSelectMonth.getSelectedItem().toString().substring(2,5);
//        String year = textViewSelectMonth.getSelectedItem().toString().substring(6,9);
        if(textViewSelectMonth.getSelectedItem().toString().equalsIgnoreCase("Select Month"))
        {
            Toast.makeText(this, "Please select a month !!", Toast.LENGTH_LONG).show();
        }
        else {
            String monthName = textViewSelectMonth.getSelectedItem().toString();
            String month = null;

            if (monthName.equalsIgnoreCase("January"))
                month = "01";
            else if (monthName.equalsIgnoreCase("February"))
                month = "02";
            else if (monthName.equalsIgnoreCase("March"))
                month = "03";
            else if (monthName.equalsIgnoreCase("April"))
                month = "04";
            else if (monthName.equalsIgnoreCase("May"))
                month = "05";
            else if (monthName.equalsIgnoreCase("June"))
                month = "06";
            else if (monthName.equalsIgnoreCase("July"))
                month = "07";
            else if (monthName.equalsIgnoreCase("August"))
                month = "08";
            else if (monthName.equalsIgnoreCase("September"))
                month = "09";
            else if (monthName.equalsIgnoreCase("October"))
                month = "10";
            else if (monthName.equalsIgnoreCase("November"))
                month = "11";
            else if (monthName.equalsIgnoreCase("December"))
                month = "12";

            int year = Calendar.getInstance().get(Calendar.YEAR);

            String sql = "select * from plan_tbl Where meeting_date_modified >= "+year+month+"01 AND meeting_date_modified <= "+year+month+"31";
            Log.d("TAG", "SQL: "+ sql);
            SQLiteDatabase db = ReuseableClass.createAndOpenDb(this);
            Cursor cur = db.rawQuery(sql, null);
            String totalPlan = cur.getCount()+"";
            textViewTotalValue.setText(": " + totalPlan);

            try {
                String csv = "/sdcard/"+ReuseableClass.getFromPreference("empid",PlanSummaryActivity.this)+".csv";
                CSVWriter writer = new CSVWriter(new FileWriter(csv));

                List<String[]> data = new ArrayList<String[]>();
                data.add(new String[] {"Agency Name", "Meeting Date", "Meeting Starts", "Meeting Ends", "Purpose", "Objective", "Status"});
                if (cur.moveToNext()) {
                    do {
                        data.add(new String[] {cur.getString(2), cur.getString(6), cur.getString(7), cur.getString(8),
                                cur.getString(9), cur.getString(10),cur.getString(12).equalsIgnoreCase("0") ? "IN PROGRESS" : "COMPLETED" });
                    } while (cur.moveToNext());
                }
                cur.close();
                writer.writeAll(data);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }



            String sql1 = "select * from plan_tbl Where meeting_date_modified >= "+year+month+"01 AND meeting_date_modified <= "+year+month+"31 AND completed ==1";
            Log.d("TAG", "SQL: "+ sql1);
            Cursor cur1 = db.rawQuery(sql1, null);
            String completed = cur1.getCount()+"";
            textViewCompleteValue.setText(": " + completed);
            cur1.close();

            String sql2 = "select * from plan_tbl Where meeting_date_modified >= "+year+month+"01 AND meeting_date_modified <= "+year+month+"31 AND completed ==0";
            Log.d("TAG", "SQL: "+ sql2);
            Cursor cur2 = db.rawQuery(sql2, null);
            String remaining = cur2.getCount()+"";
            textViewRemainingValue.setText(": " + remaining);
            cur2.close();
            db.close();
        }
    }

    public void sharingDetails(View view) {
        if(textViewTotalValue.getText().toString().equalsIgnoreCase(": 0"))
        {
            Toast.makeText(this,"There is nothing to share !!",Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String[] strTo = {ReuseableClass.getFromPreference("manager_email_id", PlanSummaryActivity.this)};

            intent.putExtra(Intent.EXTRA_EMAIL, strTo);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Plan Summary of "
                    + ReuseableClass.getFromPreference("first_name", PlanSummaryActivity.this) + " (" +
                    ReuseableClass.getFromPreference("empid", PlanSummaryActivity.this) + ")");

            intent.putExtra(Intent.EXTRA_TEXT, "Please Find the attachment for the details.");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/sdcard/" + ReuseableClass.getFromPreference("empid", PlanSummaryActivity.this) + ".csv")));

            intent.setType("message/rfc822");
            intent.setPackage("com.google.android.gm");
            startActivity(intent);
        }
    }
}
