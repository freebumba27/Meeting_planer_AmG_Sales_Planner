package com.amgeneralinsurance.amgsalesplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.utils.ReuseableClass;

public class DashBoardActivity extends AppCompatActivity {
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ((TextView)findViewById(R.id.textViewName)).setText("NAME: " + ReuseableClass.getFromPreference("name", DashBoardActivity.this));
        ((TextView)findViewById(R.id.textViewId)).setText("EMP ID: " + ReuseableClass.getFromPreference("empid", DashBoardActivity.this));
    }

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), getString(R.string.double_back_press_message), Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

    public void planing_an_week(View view) {
        Intent i = new Intent(this, ListOfAgentActivity.class);
        finish();
        startActivity(i);
    }

    public void showingPlanSummary(View view) {
        Intent i = new Intent(this, PlanSummaryActivity.class);
        finish();
        startActivity(i);
    }

    public void openingCalendarSummary(View view) {
        Intent i = new Intent(this, CalendarSummaryActivity.class);
        finish();
        startActivity(i);
    }
}
