package com.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Dream on 17-Jun-15.
 */
public class  ReuseableClass {

    public static SQLiteDatabase createAndOpenDb(Context con)
    {
        //----------------------------------------
        // Create the database
        //----------------------------------------

        DataBaseHelper myDbHelper = new DataBaseHelper(con.getApplicationContext());
        myDbHelper = new DataBaseHelper(con);

        try
        {
            myDbHelper.createDataBase();
            Log.d("DB Log", "Database Created");
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
        return myDbHelper.getReadableDatabase();

        //----------------------------------------
        //----------------------------------------
    }

    //===================================================================================================================================
    //Preference variable
    //===================================================================================================================================

    //--------------------------------------------
    // method to save variable in preference
    //--------------------------------------------
    public static void saveInPreference(String name, String content, Context myActivity) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(myActivity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(name, content);
        editor.commit();
    }

    //--------------------------------------------
    // getting content from preferences
    //--------------------------------------------
    public static String getFromPreference(String variable_name, Context myActivity) {
        String preference_return;
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(myActivity);
        preference_return = preferences.getString(variable_name, "");

        return preference_return;
    }


    //===================================================================================================================================
    //Preference variable
    //===================================================================================================================================

}
