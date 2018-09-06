
package com.mycompany.android.imageclassifier.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabelAnnotation {

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

    /**
     * No args constructor for use in serialization
     * 
     */
    public LabelAnnotation() {
    }

    /**
     * 
     * @param topicality
     * @param description
     * @param score
     * @param mid
     */
    public LabelAnnotation(String mid, String description, Double score, Double topicality) {
        super();
        this.mid = mid;
        this.description = description;
        this.score = score;
        this.topicality = topicality;
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

}
