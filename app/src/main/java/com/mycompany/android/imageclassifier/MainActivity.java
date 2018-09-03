package com.mycompany.android.imageclassifier;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Image;
import com.mycompany.android.imageclassifier.model.Feature;
import com.mycompany.android.imageclassifier.model.ImageClassifierRequest;
import com.mycompany.android.imageclassifier.model.Request;
import com.mycompany.android.imageclassifier.networking.Client;
import com.mycompany.android.imageclassifier.networking.Service;
import com.mycompany.android.imageclassifier.utils.PreferenceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mycompany.android.imageclassifier.utils.Constants.BASE_URL;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.selectImage)
    ImageView selectImage;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.image_header)
    ImageView image_header;

    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_PICK_PHOTO = 2;
    private Uri mMediaUri;
    private static final int CAMERA_PIC_REQUEST = 1111;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    private String mediaPath;
    private String mImageFileLocation = "";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    private String postPath = "";
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private Account mAccount;
    private static String accessToken;
    private static final int REQUEST_CODE_PICK_ACCOUNT = 101;
    private static final int REQUEST_ACCOUNT_AUTHORIZATION = 102;
    private Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initpDialog();
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.GET_ACCOUNTS }, 0);
                } else {
                    launchImagePicker();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAuthToken();
            }else{
                Toast.makeText(MainActivity.this, "Permission denied, the permissions are very important for the apps usage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (postPath.isEmpty()) {
            MenuItem menuItem = menu.findItem(R.id.upload);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload:
                if (postPath.isEmpty() || postPath == null){
                    Toast.makeText(this, "select a valid image for classifying", Toast.LENGTH_SHORT).show();
                }else {
                    //
                    // launchImagePicker();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchImagePicker(){
        new MaterialDialog.Builder(this)
                .title(R.string.uploadImages)
                .items(R.array.uploadImages)
                .itemsIds(R.array.itemIds)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
                                break;
                            case 1:
                                captureImage();
                                break;
                            case 2:
                                image_header.setImageResource(R.color.colorPrimary);
                                postPath.equals("");
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            Intent callCameraApplicationIntent = new Intent();
            callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            // We give some instruction to the intent to save the image
            File photoFile = null;

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile();
                // Here we call the function that will try to catch the exception made by the throw function
            } catch (IOException e) {
                Logger.getAnonymousLogger().info("Exception error in generating the file");
                e.printStackTrace();
            }
            // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
            Uri outputUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Logger.getAnonymousLogger().info("Calling the camera App by intent");

            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    setProgressBar();
                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPath = cursor.getString(columnIndex);
                    // Set the Image in ImageView for Previewing the Media
                    image_header.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                    cursor.close();
                    postPath = mediaPath;
                    bm = BitmapFactory.decodeFile(mediaPath);
                    invalidateOptionsMenu();
                   // String image = uploadImage(postPath);
                    //Toast.makeText(this, "this is base64, " + image, Toast.LENGTH_SHORT).show();
                }
            }else if (requestCode == CAMERA_PIC_REQUEST){
                if (Build.VERSION.SDK_INT > 21) {
                    setProgressBar();
                    Glide.with(this).load(mImageFileLocation).into(image_header);
                    postPath = mImageFileLocation;
                    bm = BitmapFactory.decodeFile(postPath);
                    invalidateOptionsMenu();
                }else{
                    setProgressBar();
                    Glide.with(this).load(fileUri).into(image_header);
                    postPath = fileUri.getPath();
                    bm = BitmapFactory.decodeFile(postPath);
                    invalidateOptionsMenu();
                }
            }else if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
                if (resultCode == RESULT_OK) {
                    String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    AccountManager am = AccountManager.get(this);
                    Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                    for (Account account : accounts) {
                        if (account.name.equals(email)) {
                            mAccount = account;
                            break;
                        }
                    }
                    getAuthToken();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "No Account Selected", Toast.LENGTH_SHORT)
                            .show();
                }
            } else if (requestCode == REQUEST_ACCOUNT_AUTHORIZATION) {
                if (resultCode == RESULT_OK) {
                    Bundle extra = data.getExtras();
                    onTokenReceived(extra.getString("authtoken"));
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Authorization Failed", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
        else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show();
        }
    }

    File createImageFile() throws IOException {
        Logger.getAnonymousLogger().info("Generating the image - method started");

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp;
        // Here we specify the environment location and the exact path where we want to save the so-created file
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app");
        Logger.getAnonymousLogger().info("Storage directory set");

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir();

        // Here we create the file using a prefix, a suffix and a directory
        File image = new File(storageDirectory, imageFileName + ".jpg");
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set");

        mImageFileLocation = image.getAbsolutePath();
        fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image;
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + ".jpg");
        }  else {
            return null;
        }

        return mediaFile;
    }

    /*private String uploadImage(String picturePath) {
        Bitmap bm = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        String _bytes64Sting = "";
        if (bm != null){
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] byteArray = bao.toByteArray();
            _bytes64Sting = Base64.encodeBytes(byteArray);
        }else {

        }

        return _bytes64Sting;

    }*/

    /*private List<Feature> getFeature(){
        List<Feature> featureList = new ArrayList<>();
        Feature feature = new Feature("LANDMARK_DETECTION", 1);
        Feature feature1 = new Feature("LABEL_DETECTION", 5);

        featureList.add(feature);
        featureList.add(feature1);

        return featureList;
    }

    private List<Request> getRequests(String image){
        List<Request> requestList = new ArrayList<>();

        Request request = new Request(new Image(image), getFeature());
        requestList.add(request);
        return requestList;
    }

    private void submitFile(String image){
        showpDialog();
        String byteImage = uploadImage(image);
        List<Request> requestList = getRequests(byteImage);
        try {
            Service service = Client.createService(Service.class, BASE_URL);
            Call<ImageClassifierRequest> call = service.imageClassifier(BuildConfig.VISION_API, new ImageClassifierRequest(requestList));
            call.enqueue(new Callback<ImageClassifierRequest>() {
                @Override
                public void onResponse(Call<ImageClassifierRequest> call, Response<ImageClassifierRequest> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            hidepDialog();
                           List<Request> request = response.body().getRequests();
                            Toast.makeText(MainActivity.this, "result is " + request, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ImageClassifierRequest> call, Throwable t) {
                    hidepDialog();
                    Toast.makeText(MainActivity.this, "this is the error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "cause of exception is " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
*/
   /* @SuppressLint("StaticFieldLeak")
    private void callCloudVision(final Bitmap bitmap) throws IOException {
        showpDialog();

        new AsyncTask<Object, Void, BatchAnnotateImagesResponse>() {
            @Override
            protected BatchAnnotateImagesResponse doInBackground(Object... params) {
                try {
                    GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder
                            (httpTransport, jsonFactory, credential);
                    Vision vision = builder.build();

                    List<Feature> featureList = new ArrayList<>();
                    Feature labelDetection = new Feature();
                    labelDetection.setType("LABEL_DETECTION");
                    labelDetection.setMaxResults(10);
                    featureList.add(labelDetection);

                    Feature textDetection = new Feature();
                    textDetection.setType("LANDMARK_DETECTION");
                    textDetection.setMaxResults(10);
                    featureList.add(textDetection);

                    List<AnnotateImageRequest> imageList = new ArrayList<>();
                    AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
                    Image base64EncodedImage = getBase64EncodedJpeg(bitmap);
                    annotateImageRequest.setImage(base64EncodedImage);
                    annotateImageRequest.setFeatures(featureList);
                    imageList.add(annotateImageRequest);

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(imageList);

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "Sending request to Google Cloud");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return response;

                } catch (GoogleJsonResponseException e) {
                    Log.e(TAG, "Request error: " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "Request error: " + e.getMessage());
                }
                return null;
            }

            protected void onPostExecute(BatchAnnotateImagesResponse response) {
                hidepDialog();
                //textResults.setText(getDetectedTexts(response));
                //labelResults.setText(getDetectedLabels(response));
            }

        }.execute();
    }*/

    public void setProgressBar(){
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressBarStatus < 100){
                    progressBarStatus += 30;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBarbHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }
                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.dismiss();
                }

            }
        }).start();
    }

    protected void initpDialog() {

        progressBar = new ProgressDialog(this);
        progressBar.setMessage(getString(R.string.msg_loading));
        progressBar.setCancelable(true);
    }


    protected void showpDialog() {

        if (!progressBar.isShowing()) progressBar.show();
    }

    protected void hidepDialog() {

        if (progressBar.isShowing()) progressBar.dismiss();
    }

    public Image getBase64EncodedJpeg(Bitmap bitmap) {
        Image image = new Image();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        image.encodeContent(imageBytes);
        return image;
    }

    private void getAuthToken() {
        String SCOPE = "oauth2:https://www.googleapis.com/auth/cloud-platform";
        if (mAccount == null) {
            pickUserAccount();
        } else {
            new GetOAuthToken(MainActivity.this, mAccount, SCOPE, REQUEST_ACCOUNT_AUTHORIZATION)
                    .execute();
        }
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    public void onTokenReceived(String token){
        accessToken = token;
        PreferenceUtils.saveToken(token, getApplicationContext());
        launchImagePicker();
    }

}
