
package com.mycompany.android.imageclassifier.model.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LandmarkAnnotation {

    @SerializedName("mid")
    @Expose
    private String mid;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("score")
    @Expose
    private Double score;
    @SerializedName("topicality")
    @Expose
    private Double topicality;
    @SerializedName("boundingPoly")
    @Expose
    private BoundingPoly boundingPoly;
    @SerializedName("locations")
    @Expose
    private List<Location> locations = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public LandmarkAnnotation() {
    }

    /**
     * 
     * @param topicality
     * @param locations
     * @param description
     * @param score
     * @param boundingPoly
     * @param mid
     */
    public LandmarkAnnotation(String mid, String description, Double score, Double topicality, BoundingPoly boundingPoly, List<Location> locations) {
        super();
        this.mid = mid;
        this.description = description;
        this.score = score;
        this.topicality = topicality;
        this.boundingPoly = boundingPoly;
        this.locations = locations;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getTopicality() {
        return topicality;
    }

    public void setTopicality(Double topicality) {
        this.topicality = topicality;
    }

    public BoundingPoly getBoundingPoly() {
        return boundingPoly;
    }

    public void setBoundingPoly(BoundingPoly boundingPoly) {
        this.boundingPoly = boundingPoly;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

}
