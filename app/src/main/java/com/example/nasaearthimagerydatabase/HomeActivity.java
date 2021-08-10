package com.example.nasaearthimagerydatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final Map<Integer, Integer> optionsItemMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //load home page
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
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
