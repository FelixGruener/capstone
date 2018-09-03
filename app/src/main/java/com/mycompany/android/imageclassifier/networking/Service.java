package com.mycompany.android.imageclassifier.networking;


import com.mycompany.android.imageclassifier.model.ImageClassifierRequest;
import com.mycompany.android.imageclassifier.model.Request;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by delaroy on 5/18/17.
 */
public interface Service {

    @POST("images")
    Call<ImageClassifierRequest> imageClassifier(@Query("key") String apiKey, @Body ImageClassifierRequest s);

}
