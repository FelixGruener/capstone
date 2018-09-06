
package com.mycompany.android.imageclassifier.model.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("landmarkAnnotations")
    @Expose
    private List<LandmarkAnnotation> landmarkAnnotations = null;
    @SerializedName("labelAnnotations")
    @Expose
    private List<LabelAnnotation> labelAnnotations = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Response() {
    }

    /**
     * 
     * @param landmarkAnnotations
     * @param labelAnnotations
     */
    public Response(List<LandmarkAnnotation> landmarkAnnotations, List<LabelAnnotation> labelAnnotations) {
        super();
        this.landmarkAnnotations = landmarkAnnotations;
        this.labelAnnotations = labelAnnotations;
    }

    public List<LandmarkAnnotation> getLandmarkAnnotations() {
        return landmarkAnnotations;
    }

    public void setLandmarkAnnotations(List<LandmarkAnnotation> landmarkAnnotations) {
        this.landmarkAnnotations = landmarkAnnotations;
    }

    public List<LabelAnnotation> getLabelAnnotations() {
        return labelAnnotations;
    }

    public void setLabelAnnotations(List<LabelAnnotation> labelAnnotations) {
        this.labelAnnotations = labelAnnotations;
    }

}
