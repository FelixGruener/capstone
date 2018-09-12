package com.mycompany.android.imageclassifier;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mycompany.android.imageclassifier.ViewModel.AddClassifierViewModel;
import com.mycompany.android.imageclassifier.ViewModel.AddClassifierViewModelFactory;
import com.mycompany.android.imageclassifier.database.AppDatabase;
import com.mycompany.android.imageclassifier.database.ImageEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by delaroy on 9/8/18.
 */

public class DetailActivity extends AppCompatActivity {
    // Extra for the image ID to be received in the intent
    public static final String EXTRA_IMAGE_ID = "extraImageId";
    // Extra for the image ID to be received after rotation
    public static final String INSTANCE_IMAGE_ID = "instanceImageId";
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_IMAGE_ID = -1;
    private int mImageId = DEFAULT_IMAGE_ID;
    private AppDatabase mDb;
    Button mButton;

    @BindView(R.id.classifier_image)
    ImageView classifier_image;

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

            // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
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

        Glide.with(this)
                .load(image)
                .into(classifier_image);

    }
}
