package com.mycompany.android.imageclassifier;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mycompany.android.imageclassifier.ViewModel.AddClassifierViewModel;
import com.mycompany.android.imageclassifier.ViewModel.AddClassifierViewModelFactory;
import com.mycompany.android.imageclassifier.database.AppDatabase;
import com.mycompany.android.imageclassifier.database.ImageEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by delaroy on 9/8/18.
 */

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    // Extra for the image ID to be received in the intent
    public static final String EXTRA_IMAGE_ID = "extraImageId";
    // Extra for the image ID to be received after rotation
    public static final String INSTANCE_IMAGE_ID = "instanceImageId";
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_IMAGE_ID = -1;
    private int mImageId = DEFAULT_IMAGE_ID;
    private AppDatabase mDb;
    private Button mButton;
    private GoogleMap mMap;
    private String labelDescription = "";
    private String landmarkDescription = "";
    private Double latitude ;
    private Double longitude;

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

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_IMAGE_ID)) {
            mImageId = savedInstanceState.getInt(INSTANCE_IMAGE_ID, DEFAULT_IMAGE_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_IMAGE_ID)) {
            // populate the UI
            mImageId = intent.getIntExtra(EXTRA_IMAGE_ID, DEFAULT_IMAGE_ID);

            AddClassifierViewModelFactory factory = new AddClassifierViewModelFactory(mDb, mImageId);
            final AddClassifierViewModel viewModel
                    = ViewModelProviders.of(this, factory).get(AddClassifierViewModel.class);

            viewModel.getImageClassifier().observe(this, new Observer<ImageEntry>() {
                @Override
                public void onChanged(@Nullable ImageEntry taskEntry) {
                    viewModel.getImageClassifier().removeObserver(this);
                    populateUI(taskEntry);
                }
            });
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_IMAGE_ID, mImageId);
        super.onSaveInstanceState(outState);
    }

    private void populateUI(ImageEntry imageentry) {
        if (imageentry == null) {
            return;
        }

        String image = imageentry.getImage();
        labelDescription = imageentry.getLabeldesc();
        landmarkDescription = imageentry.getLandmarkdesc();
        latitude = imageentry.getLatitude();
        longitude = imageentry.getLongitude();

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title(landmarkDescription));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
