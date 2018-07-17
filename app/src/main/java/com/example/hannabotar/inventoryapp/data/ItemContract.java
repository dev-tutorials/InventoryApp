package com.example.hannabotar.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hanna on 7/10/2018.
 */

public final class ItemContract {

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "ro.urgentcargus.items";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://ro.urgentcargus.items/items/ is a valid path for
     * looking at item data. content://ro.urgentcargus.items/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_ITEMS = "items";

    public static abstract class ItemEntry implements BaseColumns {

        /** The content URI to access the item data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public static final String TABLE_NAME = "items";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERIAL_NO = "serial_no";
        public static final String COLUMN_CONDITION = "condition";
        public static final String COLUMN_DESCRIPTION = "description";


        public static final String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_SERIAL_NO + " TEXT NOT NULL, " +
                COLUMN_CONDITION + " INTEGER NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT);";

        public static final int CONDITION_UNKNOWN = 0;
        public static final int CONDITION_NEW = 1;
        public static final int CONDITION_USED = 2;
        public static final int CONDITION_DEFECT = 3;

        /**
         * Returns whether or not the given gender is {@link #CONDITION_UNKNOWN}, {@link #CONDITION_NEW},
         * {@link #CONDITION_USED}, or {@link #CONDITION_DEFECT}.
         */
        public static boolean isValidCondition(int condition) {
            return condition == CONDITION_UNKNOWN || condition == CONDITION_NEW ||
                    condition == CONDITION_USED || condition == CONDITION_DEFECT;
        }
    }

}
