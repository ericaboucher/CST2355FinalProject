package com.example.nasaearthimagerydatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.nasaearthimagerydatabase.HomeActivity;

/**
@author Erica Boucher 040699523
 Main page loaded by application
 */
public class MainActivity extends AppCompatActivity {

    //create shared preference variable for saving user's email
    SharedPreferences prefs = null;
    //variable to save email/edittext box object
    private EditText email;

    /**
     * Function that loads the main page of the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fill previously declared variables
        prefs = getSharedPreferences("Filename", MODE_PRIVATE);
        email = findViewById(R.id.edit_email);
        //String variable to save email
        String savedString = prefs.getString("email", "");
        email.setText(savedString);

        //load button from login page
        Button btn = findViewById(R.id.login_button);
        //load home page when user clicks button
        btn.setOnClickListener( (click) -> {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
                );
    }

    /**
     * onPause function to save email in sharedPrefs when closing app
     */
    @Override
    protected void onPause(){
        super.onPause();
        //create a sharedPrefs editor
        SharedPreferences.Editor editor = prefs.edit();
        //put email back in the edit text
        editor.putString("email", email.getText().toString());
        //commit the changes
        editor.apply();
    }
}