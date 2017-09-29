package com.example.maheshbabugorantla.googlemapclusters.HelperClasses;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * DESCRIPTION: FoodDbContract class
 * Created by MaheshBabuGorantla
 * First Update On Apr 06, 2017 .
 * Last Update On Apr 06, 2017.
 */

public final class FoodDbContract  {

    private FoodDbContract() {};

    /*
        Content Authority is a name for the entire content provider, similar to the
        relationship between a domain name and its website. A Convenient string to use
        for the content authority is the package name for the app, which is guaranteed
        to be unique on the device
    */
    public static final String CONTENT_AUTHORITY = "com.example.maheshbabugorantla.googlemapclusters";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use
    // to contact the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_FOOD_ENTRIES = "food_entries";

    public static class FoodEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOOD_ENTRIES).build();

        // Android Platform's base MIME Type for a content: URI containing a Cursor of zero or more items
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FOOD_ENTRIES;

        // Android Platform's base MIME Type for a content: URI containing a Cursor of a single item
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FOOD_ENTRIES;

        // Table Name
        public static final String TABLE_NAME = PATH_FOOD_ENTRIES;

        // Unique Food ID for every food entry
        public static final String COLUMN_FOOD_ID = "food_id";

        // Category of the Food
        public static final String COLUMN_FOOD_CATEGORY = "food_category";

        // Food Item Names
        public static final String COLUMN_FOOD_ITEM_NAME = "food_item_name";

        // Food Item TimeStamp
        public static final String COLUMN_TIME_STAMP = "time_stamp";

        // Calories Intake
        public static final String COLUMN_CALORIES = "calories";

/*        // Units is grams
        // Protein
        public static final String COLUMN_PROTEIN = "protein";

        // Carbohydrates
        public static final String COLUMN_CARBOHYDRATES = "carbohydrates";

        // Fat Content
        public static final String COLUMN_FAT = "fat_content";*/

        // Defining a method to build a URI to find a specific food entry by its identifier
        public static Uri buildFoodUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
