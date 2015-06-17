package com.amgeneralinsurance.amgsalesplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AddAgentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agent);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ListOfAgentActivity.class);
        finish();
        startActivity(i);
    }
}
