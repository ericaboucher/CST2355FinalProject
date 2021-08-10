package com.example.nasaearthimagerydatabase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);

        // Start the progress bar
        ProgressBar pb = findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);
        pb.setProgress(0);

        ImageSearch search = new ImageSearch();
        search.execute();
    }

    private class ImageSearch extends AsyncTask<String, Integer, String> {
        Bitmap NASAImage;


        @Override
        protected String doInBackground(String... strings) {
            try {
                //append url for finding image
                String searchUrl = "https://api.nasa.gov/planetary/earth/imagery?lon=" + SearchActivity.longitude + "&lat=" + SearchActivity.latitude + "&date="
                        + SearchActivity.date + "&api_key=DEMO_KEY";
                //progress bar update
                publishProgress(25);

                //create the url object
                URL url = new URL(searchUrl);
                //progress bar update
                publishProgress(50);

                //open connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                publishProgress(75);

                //wait for data
                int respCode = urlConnection.getResponseCode();
                if (respCode == 200) {
                    NASAImage = BitmapFactory.decodeStream(urlConnection.getInputStream());
                    publishProgress(85);
                }


                publishProgress(100);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
