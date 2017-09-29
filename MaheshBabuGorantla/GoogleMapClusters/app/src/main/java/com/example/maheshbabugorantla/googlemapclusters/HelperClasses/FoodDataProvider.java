package com.example.maheshbabugorantla.googlemapclusters.HelperClasses;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * DESCRIPTION: FoodDataProvider class
 *              This is the Content Provider for the Food Items Table in TADA Database
 * Created by MaheshBabuGorantla
 * First Update On Apr 07, 2017 .
 * Last Update On Apr 07, 2017.
 */

public class FoodDataProvider extends ContentProvider{

    protected final String LOG_TAG = "FoodDataProvider";

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FoodDbHelper mFoodDbHelper;

    // Integer Constants for Food Items URI Matcher
    private static final int FOOD_ITEMS = 100; // Used to fetch or insert all the rows of food items
    private static final int FOOD_ITEM_ID = 101; // Used to fetch a row of single food item details

    public static UriMatcher buildUriMatcher() {

        String content_authority = FoodDbContract.CONTENT_AUTHORITY;

        // All paths  to the UriMatcher have a corresponding code to return
        // When a match is found (the ints declared above)
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(content_authority, FoodDbContract.PATH_FOOD_ENTRIES, FOOD_ITEMS);
        uriMatcher.addURI(content_authority, FoodDbContract.PATH_FOOD_ENTRIES + "/#", FOOD_ITEM_ID);

        return uriMatcher;
    }

    // Instantiate the DataBase Model in here.
    @Override
    public boolean onCreate() {
        mFoodDbHelper = new FoodDbHelper(getContext());
        return true;
    }

    /**
     * In order to query the database, we will switch based on the matched URI Integer
     * and query the appropriate Table as necessary
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase mDatabase = mFoodDbHelper.getWritableDatabase();

        Cursor mCursor;

        switch(sUriMatcher.match(uri)) {

            // Fetching all the food items in the table
            case FOOD_ITEMS:
                mCursor = mDatabase.query(FoodDbContract.FoodEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            // Fetching a food item that matches the ID.
            case FOOD_ITEM_ID:

                long food_item_id = ContentUris.parseId(uri);
                Cursor cursor = mDatabase.query(FoodDbContract.FoodEntry.TABLE_NAME, projection, FoodDbContract.FoodEntry._ID  + " = ?", new String[]{String.valueOf(food_item_id)}, null, null, sortOrder);

                // Move the Cursor to the First Row (moveToFirst).
                // This method will return false if the cursor is empty.
                if(cursor.moveToFirst()) {
                    food_item_id = cursor.getInt(cursor.getColumnIndex(FoodDbContract.FoodEntry.COLUMN_FOOD_ID));
                }

                // Have to close the cursor
                // Else this cause the app to be laggy and will be a security issue
                cursor.close();

                mCursor = mDatabase.query(FoodDbContract.FoodEntry.TABLE_NAME, projection, FoodDbContract.FoodEntry.COLUMN_FOOD_ID + " = ?", new String[]{String.valueOf(food_item_id)}, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " +uri);
        }

        /**
         * This causes the cursor to register the content observer, to watch for changes that
         * happen to the URI and any of its descendants. This allows the content provider to easily
         * tell the UI when the cursor changes, on operations like a database insert or update.
         * */

        try {
            mCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        return mCursor;

/*        catch (SQLiteException e) {
            Log.i(LOG_TAG, "TADA Database cannot be opened for writing");
            e.printStackTrace();
        }*/
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch(sUriMatcher.match(uri)) {
            case FOOD_ITEMS:
                return FoodDbContract.FoodEntry.CONTENT_TYPE;

            case FOOD_ITEM_ID:
                return FoodDbContract.FoodEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Cannot resolve Unknown Uri: " + uri);
        }
    }


    // Insert a new row into your provider.
    // Use the arguments to select the destination table and to get the column values to use.
    // Return a content URI for the newly-inserted row.
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase sqLiteDatabase = mFoodDbHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch(sUriMatcher.match(uri)) {

            case FOOD_ITEMS:
                // Convenience method for inserting a row into the database
                // And, this returns the ID where the Row is inserted
                _id = sqLiteDatabase.insert(FoodDbContract.FoodEntry.TABLE_NAME, null, values);

                if(_id > 0) {
                    returnUri = FoodDbContract.FoodEntry.buildFoodUri(_id);
                } else {
                    throw new SQLException("Unable to insert rows into: " + uri);
                }
                break;

            case FOOD_ITEM_ID:
                // Convenience method for inserting a row into the database
                // And, this returns the ID where the Row is inserted
                _id = sqLiteDatabase.insert(FoodDbContract.FoodEntry.TABLE_NAME, null, values);

                if(_id > 0) {
                    returnUri = FoodDbContract.FoodEntry.buildFoodUri(_id);
                } else {
                    throw new SQLException("Unable to insert rows into: " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    /**
    * Delete rows from your provider.
    * Use the arguments to select the table and the rows to delete.
    * Return the number of rows deleted.
    */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase sqLiteDatabase = mFoodDbHelper.getWritableDatabase();

        int rows = 0;

        switch (sUriMatcher.match(uri)) {

            case FOOD_ITEMS:
                rows = sqLiteDatabase.delete(FoodDbContract.FoodEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case FOOD_ITEM_ID:
                rows = sqLiteDatabase.delete(FoodDbContract.FoodEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if(selection == null || rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    /**
     * Update existing rows in your provider.
     * Use the arguments to select the table and rows to update and to get the updated column values.
     * Return the number of rows updated.
     * */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase sqLiteDatabase = mFoodDbHelper.getWritableDatabase();
        int rows = 0;

        switch(sUriMatcher.match(uri)) {

            case FOOD_ITEMS:
                sqLiteDatabase.update(FoodDbContract.FoodEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case FOOD_ITEM_ID:
                sqLiteDatabase.update(FoodDbContract.FoodEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if(rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    /**
     * Putting a bunch of inserts into a single transaction is much faster than inserting each row individually.
     * BulkInsert function will allow us to do that work in a single Transaction
     *  NOTE: If we do not set the transaction to be successful the records will not be committed when we call
     *        endTransaction.
     */
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase sqLiteDatabase = mFoodDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int returnCount = 0;

        switch(match) {

            case FOOD_ITEMS:

                sqLiteDatabase.beginTransaction();

                try {
                    for (ContentValues value: values) {
                        long _id = sqLiteDatabase.insert(FoodDbContract.FoodEntry.TABLE_NAME, null, value);
                        if(_id != -1) {
                            returnCount += 1;
                        }
                    }

                    // This needs to be done else the records will not be committed when we call endTransaction
                    sqLiteDatabase.setTransactionSuccessful();
                }
                finally {
                    sqLiteDatabase.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);
                break;

            case FOOD_ITEM_ID:

                sqLiteDatabase.beginTransaction();

                try {
                    for (ContentValues value: values) {
                        long _id = sqLiteDatabase.insert(FoodDbContract.FoodEntry.TABLE_NAME, null, value);
                        if(_id != -1) {
                            returnCount += 1;
                        }
                    }

                    // This needs to be done else the records will not be committed when we call endTransaction
                    sqLiteDatabase.setTransactionSuccessful();
                }
                finally {
                    sqLiteDatabase.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);
                break;

            default:
                return super.bulkInsert(uri, values);
        }

        return returnCount;
    }
}
