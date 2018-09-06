package com.mycompany.android.imageclassifier.model.request;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request {

    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("features")
    @Expose
    private List<Feature> features = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Request() {
    }

    /**
     * 
     * @param features
     * @param image
     */
    public Request(Image image, List<Feature> features) {
        super();
        this.image = image;
        this.features = features;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

}
