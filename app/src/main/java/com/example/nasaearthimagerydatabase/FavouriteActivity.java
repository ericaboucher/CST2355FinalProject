package com.example.nasaearthimagerydatabase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    private static List<Bitmap> favList = new ArrayList<>();
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_layout);

        // Set the adapter which will populate the list view
        ListAdapter adapter = new ListAdapter();
        ListView listView = findViewById(R.id.favList);
        listView.setAdapter(adapter);

        // Open the connection to SQLite
        db = new Database(this).getWritableDatabase();

        // Load any existing messages in the DB.
        //favList = loadAllMessages(db);
        //if (!favList.isEmpty()) {
        //    adapter.notifyDataSetChanged();
        //}

        // When long clicking an item in the ListView, provide a dialog to delete the message
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
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

                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(getString(R.string.list_delete_negative), null)
                    .create();

            dialog.show();
            return true;
        });

    }



    private class ListAdapter extends BaseAdapter {
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
}
