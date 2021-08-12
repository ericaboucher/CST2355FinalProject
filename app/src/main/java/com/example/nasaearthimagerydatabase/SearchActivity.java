package com.example.nasaearthimagerydatabase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    SharedPreferences prefs = null;
    private EditText lon;
    private EditText lat;
    private EditText date2;
    public static EditText longitude;
    public static EditText latitude;
    public static EditText date;
    private static XmlPullParser xpp;
    private static final Map<Integer, Integer> optionsItemMap = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        longitude = findViewById(R.id.edit_long);
        latitude = findViewById(R.id.edit_lat);
        date = findViewById(R.id.edit_date);

        Button fav = findViewById(R.id.fav_button);
        fav.setOnClickListener( (click) ->
                startActivity(new Intent(this, FavouriteActivity.class))
        );

        //fill previously declared variables
        prefs = getSharedPreferences("Filename", MODE_PRIVATE);
        //set longitude
        lon = findViewById(R.id.edit_long);
        //String variable to save longitude
        String savedString = prefs.getString("lon", "");
        lon.setText(savedString);

        //set latitude
        lat = findViewById(R.id.edit_lat);
        //String variable to save latitude
        savedString = prefs.getString("lat", "");
        lat.setText(savedString);

        //set date
        date2 = findViewById(R.id.edit_date);
        //String variable to save date
        savedString = prefs.getString("date", "");
        date2.setText(savedString);

        // Start the progress bar
        ProgressBar pb = findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);
        pb.setProgress(0);

        //get and load toolbar
        Toolbar tBar = findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        //use a hashmap to map buttons
        optionsItemMap.put(R.id.home_item, 1);
        optionsItemMap.put(R.id.search_item, 2);
        optionsItemMap.put(R.id.favourite_item, 3);
        optionsItemMap.put(R.id.logout_item, 4);

        //create navigation drawer
        DrawerLayout drawer = findViewById(R.id.nav_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageSearch search = new ImageSearch();

        Button btn = findViewById(R.id.search_button);
        btn.setOnClickListener( (click) ->
                search.execute());
    }

    private class ImageSearch extends AsyncTask<String, Integer, String> {
        String icon = null;
        Bitmap NASAImage;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Setup everything on the activity

            /*
            EditText editLon = findViewById(R.id.edit_long);
            String lo = editLon.getText().toString();
            ((TextView) findViewById(R.id.longitude)).setText(lo);
            EditText editLat = findViewById(R.id.edit_lat);
            String la = editLat.getText().toString();
            ((TextView) findViewById(R.id.latitude)).setText(la);
            ((ImageView) findViewById(R.id.image_view)).setImageBitmap(NASAImage);
            */
            // Hide the progress bar
            ((ProgressBar) findViewById(R.id.progress_bar)).setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ((ProgressBar) findViewById(R.id.progress_bar)).setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                String SEARCH_URL = "https://api.nasa.gov/planetary/earth/imagery?lon=" +
                        longitude + "&lat=" + latitude + "&date="
                        + date + "&api_key=DEMO_KEY";

                URL url = new URL(SEARCH_URL);
                publishProgress(25);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                publishProgress(50);

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                int responseCode = urlConnection.getResponseCode();
                publishProgress(75);
                if (responseCode == 200) {
                    NASAImage = BitmapFactory.decodeStream(urlConnection.getInputStream());
                }


                if (fileExists("NASAImage.png")) {
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput("NASAImage.png");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    NASAImage = BitmapFactory.decodeStream(fis);
                } else {
                    FileOutputStream outputStream = openFileOutput("NASAImage.png", Context.MODE_PRIVATE);
                    NASAImage.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
                publishProgress(100);

            /*

                InputStream in = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }
                out.close();
                in.close();
                byte[] response = out.toByteArray();

                NASAImage = BitmapFactory.decodeStream(in);
                FileOutputStream fos = new FileOutputStream("C://NASAImage.jpg");
                fos.write(response);
                fos.close();


             */


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

            /*InputStream is = null;
            try {
                // First retrieve the image
                is = new URL(SEARCH_URL).openConnection().getInputStream();
                xpp.setInput(is, null);

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                            publishProgress(25);
                            icon = xpp.getAttributeValue(null, "icon");
                            publishProgress(75);
                    }
                    eventType = xpp.next();
                }

                // Download the image if we don't already have it.
                Log.i(this.getClass().getName(), "Looking for " + icon);
                if (!fileExists(icon)) {
                    Log.i(this.getClass().getName(), "Downloading " + icon);
                    NASAImage = BitmapFactory.decodeStream(is);

                    // Save it, using a try-with-resources will automatically close and flush
                    try (FileOutputStream fos = openFileOutput(icon, Context.MODE_PRIVATE)) {
                        NASAImage.compress(Bitmap.CompressFormat.PNG, 80, fos);
                    }
                } else {
                    Log.i(this.getClass().getName(), icon + " already exists");

                    // Load it from disk
                    try (FileInputStream fis = openFileInput(icon)) {
                        NASAImage = BitmapFactory.decodeStream(fis);
                    }
                }
                publishProgress(100);
            } catch (Exception e) {
                Log.e(this.getClass().getName(), e.getMessage());
            }
            return null;
        }*/

        public boolean fileExists(String fname) {
            return getBaseContext().getFileStreamPath(fname).exists();
        }
    }


    @Override
    protected void onPause(){
        super.onPause();
        //create a sharedPrefs editor
        SharedPreferences.Editor editor = prefs.edit();
        //put data back in the edit text
        editor.putString("date", date.getText().toString());
        editor.putString("lon", lon.getText().toString());
        editor.putString("lat", lat.getText().toString());
        //commit the changes
        editor.apply();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_item:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.search_item:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.favourite_item:
                startActivity(new Intent(this, FavouriteActivity.class));
                break;
            case R.id.logout_item:
                finish();
                break;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg;
        if (optionsItemMap.containsKey(item.getItemId())) {
            msg = "You clicked on item " + optionsItemMap.get(item.getItemId());
        } else {
            msg = "You clicked on the overflow menu";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}
