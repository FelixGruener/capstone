package com.mycompany.android.imageclassifier.model.request;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageClassifierRequest {

    @SerializedName("requests")
    @Expose
    private List<Request> requests = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ImageClassifierRequest() {
    }

    /**
     * 
     * @param requests
     */
    public ImageClassifierRequest(List<Request> requests) {
        super();
        this.requests = requests;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

}
