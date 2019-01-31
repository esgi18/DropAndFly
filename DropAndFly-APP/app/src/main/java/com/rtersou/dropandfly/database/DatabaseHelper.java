package com.rtersou.dropandfly.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "DropAndFly";

    // Table Names
    public static final String TABLE_RESERVATION    = "reservations";

    // Common column names
    public static final String KEY_ID = "id";

    // RESERVATION  Table - column names
    public static final String KEY_DATE_START   = "date_start";
    public static final String KEY_DATE_END     = "date_end";
    public static final String KEY_H_START      = "h_start";
    public static final String KEY_H_END        = "h_end";
    public static final String KEY_NB_LUGGAGE   = "nb_luggage";
    public static final String KEY_PRICE        = "price";
    public static final String KEY_USER_ID      = "user_id";
    public static final String KEY_SHOP_ID      = "shop_id";



    // Reservation table create statement
    public static final String CREATE_TABLE_RESERVATION = "CREATE TABLE "
            + TABLE_RESERVATION
            + "("
            + KEY_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DATE_START    + " TEXT NOT NULL,"
            + KEY_DATE_END      + " TEXT NOT NULL,"
            + KEY_H_START       + " TEXT NOT NULL,"
            + KEY_H_END         + " TEXT NOT NULL,"
            + KEY_NB_LUGGAGE    + " INTEGER,"
            + KEY_PRICE         + " INTEGER,"
            + KEY_USER_ID       + " INTEGER,"
            + KEY_SHOP_ID       + " INTEGER"
            + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_RESERVATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_RESERVATION);

        // create new tables
        onCreate(db);
    }

}
