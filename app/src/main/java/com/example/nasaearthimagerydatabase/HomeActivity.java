package com.example.nasaearthimagerydatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity to load apps home page
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //map to load options for items
    private static final Map<Integer, Integer> optionsItemMap = new HashMap<>();

    /**
     * Loads page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        //load search button
        Button btn = findViewById(R.id.search_database);
        //load search page when user clicks
        btn.setOnClickListener( (click) -> {
                    startActivity(new Intent(this, SearchActivity.class));
                }
        );

    }

    /**
     * Loads different activities based on button that user clicks
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
                alert.setTitle("Home")
                        .setMessage("Welcome to your home page! Press Search to begin your image search")
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
        //inflate toolbar menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * Toast message to tell user which item was clicked
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg;
        //if item clicked has an id, tell the user what item it is
        if (optionsItemMap.containsKey(item.getItemId())) {
            msg = "You clicked on item " + optionsItemMap.get(item.getItemId());
        } else {
            msg = "You clicked on the overflow menu";
        }
        //show toast message to user
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}
