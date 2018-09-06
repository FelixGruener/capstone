package com.mycompany.android.imageclassifier.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.sql.Blob;
import java.util.Date;

/**
 * Created by delaroy on 9/4/18.
 */

@Entity(tableName = "imageentry")
public class ImageEntry {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "landmarkdesc")
    private String landmarkdesc;

    @ColumnInfo(name = "latitude")
    private Double latitude;

    @ColumnInfo(name = "longitude")
    private Double longitude;

    @ColumnInfo(name = "labeldesc")
    private String labeldesc;

    @ColumnInfo(name = "image",typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    @Ignore
    public ImageEntry(String landmarkdesc, Double latitude, Double longitude, String labeldesc, byte[] image) {
        this.landmarkdesc = landmarkdesc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.labeldesc = labeldesc;
        this.image = image;
    }

    public ImageEntry(int id, String landmarkdesc, Double latitude, Double longitude, String labeldesc, byte[] image) {
        this.id = id;
        this.landmarkdesc = landmarkdesc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.labeldesc = labeldesc;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLandmarkdesc() {
        return landmarkdesc;
    }

    public void setLandmarkdesc(String landmarkdesc) {
        this.landmarkdesc = landmarkdesc;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLabeldesc(String labeldesc){ this.labeldesc = labeldesc; }

    public String getLabeldesc() { return labeldesc; }

    public void setImage(byte[] image) { this.image = image; }

    public byte[] getImage() { return image; }

}
