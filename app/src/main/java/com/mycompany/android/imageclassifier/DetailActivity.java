package com.mycompany.android.imageclassifier;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_IMAGE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LABEL_DESC;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LANDMARK_DESC;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LATITUDE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LONGITUDE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry._ID;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.getColumnDouble;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.getColumnInt;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.getColumnString;

/**
 * Created by delaroy on 9/8/18.
 */

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {
    // Extra for the image ID to be received after rotation
    public static final String INSTANCE_IMAGE_ID = "instanceImageId";
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_IMAGE_ID = -1;
    private int mImageId = DEFAULT_IMAGE_ID;
    //private AppDatabase mDb;
    private Button mButton;
    private GoogleMap mMap;
    private String labelDescription = "";
    private String landmarkDescription = "";
    private Double latitude ;
    private Double longitude;
    Uri classificationUri;
    private static final int CLASSIFICATION_DETAIL_LOADER = 0;

    @BindView(R.id.classifier_image)
    ImageView classifier_image;

    @BindView(R.id.fragment_layout)
    LinearLayout fragmentLayout;

    @BindView(R.id.classifiedText)
    TextView classifiedText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        classificationUri = getIntent().getData();

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_IMAGE_ID)) {
            mImageId = savedInstanceState.getInt(INSTANCE_IMAGE_ID, DEFAULT_IMAGE_ID);
        }

        if (classificationUri != null){
            getSupportLoaderManager().initLoader(CLASSIFICATION_DETAIL_LOADER, null, this);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_IMAGE_ID, mImageId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title(landmarkDescription));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                _ID,
                COLUMN_LANDMARK_DESC,
                COLUMN_LATITUDE,
                COLUMN_LONGITUDE,
                COLUMN_LABEL_DESC,
                COLUMN_IMAGE
        };

        return new CursorLoader(this,
                classificationUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            int id = getColumnInt(cursor, _ID);
            landmarkDescription = getColumnString(cursor, COLUMN_LANDMARK_DESC);
            latitude = getColumnDouble(cursor, COLUMN_LATITUDE);
            longitude = getColumnDouble(cursor, COLUMN_LONGITUDE);
            labelDescription = getColumnString(cursor, COLUMN_LABEL_DESC);
            String image = getColumnString(cursor, COLUMN_IMAGE);

            classifiedText.setText(labelDescription);

            Glide.with(this)
                .load(image)
                    .into(classifier_image);

            if (landmarkDescription.isEmpty()) {
                fragmentLayout.setVisibility(View.INVISIBLE);
            } else {

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
