
package com.mycompany.android.imageclassifier.model.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BoundingPoly {

    @SerializedName("vertices")
    @Expose
    private List<Vertex> vertices = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public BoundingPoly() {
    }

    /**
     * 
     * @param vertices
     */
    public BoundingPoly(List<Vertex> vertices) {
        super();
        this.vertices = vertices;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

}
