
package com.mycompany.android.imageclassifier.model.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageClassifierResponse {

    @SerializedName("responses")
    @Expose
    private List<Response> responses = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ImageClassifierResponse() {
    }

    /**
     * 
     * @param responses
     */
    public ImageClassifierResponse(List<Response> responses) {
        super();
        this.responses = responses;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

}
