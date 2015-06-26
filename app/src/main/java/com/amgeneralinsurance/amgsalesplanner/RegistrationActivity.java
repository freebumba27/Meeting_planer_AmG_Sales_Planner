package com.amgeneralinsurance.amgsalesplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.utils.ReuseableClass;

public class RegistrationActivity extends AppCompatActivity {

    EditText EditTextFirstName;
    EditText EditTextLastName;
    EditText EditTextEmployeeId;
    EditText EditTextMobile;
    EditText EditTextManagerEmail;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        EditTextFirstName    = (EditText)findViewById(R.id.EditTextFirstName);
        EditTextLastName     = (EditText)findViewById(R.id.EditTextLastName);
        EditTextEmployeeId   = (EditText)findViewById(R.id.EditTextEmployeeId);
        EditTextMobile       = (EditText)findViewById(R.id.EditTextMobile);
        EditTextManagerEmail = (EditText)findViewById(R.id.EditTextManagerEmail);
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

    public void registering(View view) {

        if(EditTextFirstName.getText().toString().length()>0 && EditTextLastName.getText().toString().length()>0 &&
                EditTextEmployeeId.getText().toString().length()>0 && EditTextMobile.getText().toString().length()>0 &&
                EditTextManagerEmail.getText().toString().length()>0) {
            ReuseableClass.saveInPreference("empid", EditTextEmployeeId.getText().toString(), RegistrationActivity.this);
            ReuseableClass.saveInPreference("first_name", EditTextFirstName.getText().toString(), RegistrationActivity.this);
            ReuseableClass.saveInPreference("manager_email_id", EditTextManagerEmail.getText().toString(), RegistrationActivity.this);
            ReuseableClass.saveInPreference("name", EditTextFirstName.getText().toString() + " " +
                    EditTextLastName.getText().toString(), RegistrationActivity.this);

            Toast.makeText(this, "Thanks for registering !!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, DashBoardActivity.class);
            finish();
            startActivity(i);
        }
        else {
            Toast.makeText(this, "All fields are mandatory !!", Toast.LENGTH_LONG).show();
        }
    }
}
