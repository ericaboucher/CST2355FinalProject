package com.example.nasaearthimagerydatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class SearchActivity extends AppCompatActivity {
    public static EditText longitude;
    public static EditText latitude;
    public static EditText date;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        longitude = findViewById(R.id.edit_long);
        latitude = findViewById(R.id.edit_lat);
        date = findViewById(R.id.edit_date);

        Button search = findViewById(R.id.search_button);
        search.setOnClickListener( (click) ->
                startActivity(new Intent(this, ImageActivity.class))
                );
    }

}
