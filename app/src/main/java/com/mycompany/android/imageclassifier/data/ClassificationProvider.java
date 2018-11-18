package com.mycompany.android.imageclassifier.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class ClassificationProvider extends ContentProvider {
    public static final String LOG_TAG = ClassificationProvider.class.getSimpleName();

    private static final int CLASSIFIER = 200;
    private static final int CLASSIFIER_ID = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(ClassificationDatabaseContract.CONTENT_AUTHORITY, ClassificationDatabaseContract.PATH_CLASSIFIER, CLASSIFIER);

        sUriMatcher.addURI(ClassificationDatabaseContract.CONTENT_AUTHORITY, ClassificationDatabaseContract.PATH_CLASSIFIER + "/#", CLASSIFIER_ID);
    }

    /** Database helper object */
    private ClassificationDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new ClassificationDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get readable database
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CLASSIFIER:

                cursor = database.query(ClassificationDatabaseContract.ClassificationEntry.TABLE_CLASSIFIER, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case CLASSIFIER_ID:

                selection = ClassificationDatabaseContract.ClassificationEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };;

                cursor = database.query(ClassificationDatabaseContract.ClassificationEntry.TABLE_CLASSIFIER, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CLASSIFIER:
                return insertClassification(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertClassification(Uri uri, ContentValues values) {

        // Get writeable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Insert the new school with the given values
        long id = database.insert(ClassificationDatabaseContract.ClassificationEntry.TABLE_CLASSIFIER, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the school content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CLASSIFIER:
                //Rows aren't counted with null selection
                rowsDeleted = database.delete(ClassificationDatabaseContract.ClassificationEntry.TABLE_CLASSIFIER, s, strings);
                break;

            case CLASSIFIER_ID:
                long courseId = ContentUris.parseId(uri);
                s = String.format("%s = ?", ClassificationDatabaseContract.ClassificationEntry._ID);
                strings = new String[]{String.valueOf(courseId)};
                rowsDeleted = database.delete(ClassificationDatabaseContract.ClassificationEntry.TABLE_CLASSIFIER, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Illegal delete URI");
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
