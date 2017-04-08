package com.example.maheshbabugorantla.googlemapclusters.HelperClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.maheshbabugorantla.googlemapclusters.HelperClasses.FoodDbContract.FoodEntry;

/**
 * DESCRIPTION: FoodDbHelper class
 * Created by MaheshBabuGorantla
 * First Update On Apr 06, 2017 .
 * Last Update On Apr 06, 2017.
 */

public class FoodDbHelper extends SQLiteOpenHelper {

    // If need to change the database schema, Increment the Number here
    // The Database Version typically starts at version '1', and must be manually incremented each time we release a new APK
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "tada.db";

    // Constructor to create the Database
    public FoodDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FOOD_ENTRY = "CREATE TABLE " + FoodEntry.TABLE_NAME + " (" +

                FoodEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                FoodEntry.COLUMN_FOOD_ID + " INTEGER, NOT NULL, " +
                FoodEntry.COLUMN_FOOD_CATEGORY + " TEXT NOT NULL, " +
                FoodEntry.COLUMN_FOOD_ITEM_NAME + " TEXT NOT NULL, " +
                FoodEntry.COLUMN_TIME_STAMP + " TEXT NOT NULL, " +
                FoodEntry.COLUMN_CALORIES + " INTEGER NOT NULL;";

        db.execSQL(SQL_CREATE_FOOD_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*
            This database acts a cache for online data source to prevent the data requests from the
            internet for the same data again. So its upgrade policy is to simply discard the existing
            data and update the data with new data.
            REMEMBER: This onUpgrade Function will only be called when there is a change in the version
                      number of the database
         */

        db.execSQL("DROP TABLE IF EXISTS " + FoodEntry.TABLE_NAME);

        // Recreating all the tables with the new schema.
        onCreate(db);
    }
}
