package com.example.nasaearthimagerydatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    static final String DB_NAME = "NASADB";
    static final int VERSION = 1;
    static final String TABLE_NAME = "Images";
    static final String COL_ID = "Id";
    static final String COL_IMAGE = "Image";

    public Database(Context ctx) {
        super(ctx, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s blob);", TABLE_NAME, COL_ID, COL_IMAGE));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
        onCreate(db);
    }
}
