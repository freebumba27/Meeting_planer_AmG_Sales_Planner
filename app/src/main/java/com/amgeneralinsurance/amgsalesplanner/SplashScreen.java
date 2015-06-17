package com.amgeneralinsurance.amgsalesplanner;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.utils.DataBaseHelper;

import java.io.IOException;


public class SplashScreen extends Activity {
    boolean backPressed = false;
    LinearLayout logo_layout;
    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        createAndOpenDb();

        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        logo_layout = (LinearLayout) findViewById(R.id.linearLayoutLogoLayout);
        logo_layout.setAnimation(slideUp);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!backPressed) {
                    Intent myIntent = new Intent(SplashScreen.this, LoginActivity.class);
                    finish();
                    startActivity(myIntent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            }
        }, 3500);
    }

    @Override
    public void onBackPressed() {
        backPressed = true;
        finish();
    }

    private void createAndOpenDb()
    {
        //----------------------------------------
        // Create the database
        //----------------------------------------

        DataBaseHelper myDbHelper = new DataBaseHelper(this.getApplicationContext());
        myDbHelper = new DataBaseHelper(this);

        try
        {
            myDbHelper.createDataBase();
            Log.d("DB Log","Database Created");
        }
        catch (IOException ioe)
        {
            Log.d("DB Log", "Unable to create database Error: " + ioe + "\n");
        }

        //----------------------------------------
        //----------------------------------------



        //----------------------------------------
        // Open the database
        //----------------------------------------
        try
        {
            myDbHelper.openDataBase();
            Log.d("DB Log","Database Opened");

        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }

        // Get the readable version
        db = myDbHelper.getReadableDatabase();

        //----------------------------------------
        //----------------------------------------
    }
}
