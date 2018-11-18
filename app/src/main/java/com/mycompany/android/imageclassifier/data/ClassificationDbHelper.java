package com.mycompany.android.imageclassifier.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_IMAGE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LABEL_DESC;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LANDMARK_DESC;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LATITUDE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LONGITUDE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.TABLE_CLASSIFIER;

public class ClassificationDbHelper extends SQLiteOpenHelper {
    private static final String TAG = ClassificationDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "classification.db";
    private static final int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase db;


    public ClassificationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_CLASSIFIER_TABLE = "CREATE TABLE " + TABLE_CLASSIFIER + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_LANDMARK_DESC + " TEXT , " +
                COLUMN_LATITUDE + " REAL , " +
                COLUMN_LONGITUDE + " REAL, " +
                COLUMN_LABEL_DESC + " TEXT , " +
                COLUMN_IMAGE + " TEXT" + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_CLASSIFIER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ClassificationDatabaseContract.ClassificationEntry.TABLE_CLASSIFIER);
        onCreate(db);
    }

}
