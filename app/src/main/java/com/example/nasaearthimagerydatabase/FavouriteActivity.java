package com.example.nasaearthimagerydatabase;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * activity to load users favourite images
 */
public class FavouriteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static List<Bitmap> favList = new ArrayList<>();
    private SQLiteDatabase db;
    private ListAdapter myAdapter;
    Cursor cursor;
    int version;
    private static final Map<Integer, Integer> optionsItemMap = new HashMap<>();

    /**
     * loads view, buttons and toolbar
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_layout);

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

        // Set the adapter which will populate the list view
        ListView myList = findViewById(R.id.favList);
        myList.setAdapter(myAdapter = new ListAdapter(favList, getApplicationContext()));

        // Open the connection to SQLite
        db = new Database(this).getWritableDatabase();

        loadDataFromDatabase();
        printCursor(cursor, version);

        // When long clicking an item in the ListView, provide a dialog to delete the message
        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.list_delete_title))
                    .setMessage(getString(R.string.list_delete_message, position, id))
                    .setPositiveButton(getString(R.string.list_delete_positive), (d, which) -> {
                        favList.remove(position);
                                //.delete(db);

                        // If a DetailedFragment is open, close it.
                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.favFragment);
                        if (fragment != null) {
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }

                        myAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(getString(R.string.list_delete_negative), null)
                    .create();

            dialog.show();
            return true;
        });

    }



    private class ListAdapter extends BaseAdapter {
        private Context context;
        private List<Bitmap> favList = new ArrayList<>();
        private LayoutInflater inflater;

        public ListAdapter(List<Bitmap> favList, Context context){
            this.favList = favList;
            this.context = context;
            this.inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return favList.size();
        }

        @Override
        public Object getItem(int position) {
            return favList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            Bitmap img = favList.get(position);

            View view = inflater.inflate(R.layout.list_layout, parent, false);

            //populate image
            ((ImageView) view.findViewById(R.id.image)).setImageBitmap(img);

            return view;
        }


    }

    private void loadDataFromDatabase() {
        //get database connection
        Database myOpener = new Database(this);
        db = myOpener.getWritableDatabase();

        //retrieve all columns
        String[] columns = {Database.COL_ID, Database.COL_IMAGE};
        //query results from db
        cursor = db.query(Database.TABLE_NAME, columns, null, null, null, null, null);

        int idColIndex = cursor.getColumnIndex(Database.COL_ID);
        int imgColIndex = cursor.getColumnIndex(Database.COL_IMAGE);

        //loop through results
        while(cursor.moveToNext()){
            long id = cursor.getLong(idColIndex);
            byte[] image = cursor.getBlob(imgColIndex);
        }
    }

    protected void deleteData (long id){
        db.delete(Database.TABLE_NAME, Database.COL_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public class Image {
        private Bitmap image;
        private long id;

        public Image(long id, Bitmap image){
            this.id = id;
            this.image = image;
        }

        public Bitmap getImage() {
            return image;
        }
    }
    private void printCursor(Cursor c, int version) {
        String className = this.getClass().getName();
        Log.d(className, String.format("Database Version: %d", version));
        Log.d(className, String.format("Number of columns: %d", c.getColumnCount()));
        Log.d(className, String.format("Column names: %s", Arrays.toString(c.getColumnNames())));
        Log.d(className, String.format("Number of rows: %d", c.getCount()));
        Log.d(className, "Results:");
    }
    /**
     * item menu for navigation drawer, when clicked go to activity
     * @param item
     * @return
     */
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
            case R.id.help_item:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Favourites")
                        .setMessage("Welcome to the favourites page! This is where you will find all of your favourite images, and you may view or delete images by clicking on them.")
                        .setPositiveButton("OK",(click, arg) -> { })
                        .create()
                        .show();
                break;
            case R.id.logout_item:
                finish();
                break;
        }
        return false;
    }

    /**
     * loads toolbar menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * loads menu items and toast message
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        return false;
    }
}


