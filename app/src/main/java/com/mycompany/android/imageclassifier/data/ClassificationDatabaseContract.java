package com.mycompany.android.imageclassifier.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class ClassificationDatabaseContract {
    public static final String CONTENT_AUTHORITY = "com.mycompany.android.imageclassifier";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_CLASSIFIER = "classifier-path";
    public static final String PATH_COURSE = "course-path";

    public static final class ClassificationEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CLASSIFIER);


        public final static String TABLE_CLASSIFIER = "classifiertable";


        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_LANDMARK_DESC = "landmarkdesc";
        public final static String COLUMN_LATITUDE = "latitude";
        public final static String COLUMN_LONGITUDE = "longitude";
        public final static String COLUMN_LABEL_DESC = "labeldesc";
        public final static String COLUMN_IMAGE = "column_image";

    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }

    public static Double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble( cursor.getColumnIndex(columnName) );
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }
}