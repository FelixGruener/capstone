package com.mycompany.android.imageclassifier.widget;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.mycompany.android.imageclassifier.R;
import com.mycompany.android.imageclassifier.model.Classification;

import java.util.ArrayList;
import java.util.List;

import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_IMAGE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LABEL_DESC;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LANDMARK_DESC;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LATITUDE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LONGITUDE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.CONTENT_URI;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry._ID;

public class ListRemoteService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;

    List<Classification> classificationList;
    public ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        String[] projection = {
                _ID,
                COLUMN_LANDMARK_DESC,
                COLUMN_LATITUDE,
                COLUMN_LONGITUDE,
                COLUMN_LABEL_DESC,
                COLUMN_IMAGE
        };

        Cursor classifyCursor = mContext.getContentResolver().query(
                CONTENT_URI,
                projection,
                null,
                null,
                _ID
        );

        classificationList = getClassificationListFromCursor(classifyCursor, mContext);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return classificationList != null ? classificationList.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Classification classification = classificationList.get(position);
        Uri uri = ContentUris.withAppendedId(CONTENT_URI, classificationList.get(position).id);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_items);
        views.setTextViewText(R.id.classification_text, classification.getLabeldesc());
        views.setImageViewUri(R.id.classification_image, Uri.parse(classification.getImage()));

        Intent fillInIntent = new Intent();
        fillInIntent.setData(uri);
        views.setOnClickFillInIntent(R.id.classification_text, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public static ArrayList<Classification> getClassificationListFromCursor(Cursor classification, Context context) {

        ArrayList<Classification> classifications = new ArrayList<>();

        int idIndex = classification.getColumnIndex(_ID);
        int landmarkIndex = classification.getColumnIndex(COLUMN_LANDMARK_DESC);
        int latitudeIndex = classification.getColumnIndex(COLUMN_LATITUDE);
        int longitudeIndex = classification.getColumnIndex(COLUMN_LONGITUDE);
        int labelIndex = classification.getColumnIndex(COLUMN_LABEL_DESC);
        int imageIndex = classification.getColumnIndex(COLUMN_IMAGE);


        Classification classify;

        while (classification.moveToNext()) {
            classify = new Classification(
                    classification.getInt(idIndex),
                    classification.getString(landmarkIndex),
                    classification.getDouble(latitudeIndex),
                    classification.getDouble(longitudeIndex),
                    classification.getString(labelIndex),
                    classification.getString(imageIndex)
            );

            classifications.add(classify);
        }
        classification.close();
        return classifications;

    }
}