package com.mycompany.android.imageclassifier.networking;


import com.mycompany.android.imageclassifier.model.request.ImageClassifierRequest;
import com.mycompany.android.imageclassifier.model.response.ImageClassifierResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by delaroy on 5/18/17.
 */
public interface Service {

    @POST("v1/images:annotate")
    Call<ImageClassifierResponse> imageClassifier(@Query("key") String apiKey, @Body ImageClassifierRequest s);

}
