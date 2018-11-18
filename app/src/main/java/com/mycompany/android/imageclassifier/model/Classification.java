package com.mycompany.android.imageclassifier.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.*;

import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_IMAGE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LABEL_DESC;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LANDMARK_DESC;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LATITUDE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LONGITUDE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry._ID;

public class Classification implements Parcelable {

    public Integer id;
    public String landmarkdesc;
    public Double latitude;
    public Double longitude;
    public String labeldesc;
    public String image;


    protected Classification(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.landmarkdesc = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.longitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.labeldesc = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));

    }

    public Classification() {
    }

    public Classification(Integer id, String landmarkdesc, Double latitude, Double longitude, String labeldesc, String image) {
        super();
        this.id = id;
        this.landmarkdesc = landmarkdesc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.labeldesc = labeldesc;
        this.image = image;
    }

    /**
     * Create a new classification from a database Cursor
     */
    public Classification(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.landmarkdesc = getColumnString(cursor, COLUMN_LANDMARK_DESC);
        this.latitude = getColumnDouble(cursor, COLUMN_LATITUDE);
        this.longitude = getColumnDouble(cursor, COLUMN_LONGITUDE);
        this.labeldesc = getColumnString(cursor, COLUMN_LABEL_DESC);
        this.image = getColumnString(cursor, COLUMN_IMAGE);
    }


    public static final Creator<Classification> CREATOR = new Creator<Classification>() {
        @Override
        public Classification createFromParcel(Parcel in) {
            return new Classification(in);
        }

        @Override
        public Classification[] newArray(int size) {
            return new Classification[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLandmarkdesc() {
        return landmarkdesc;
    }

    public void setLandmarkdesc(String landmarkdesc) {
        this.landmarkdesc = landmarkdesc;
    }

    public String getLabeldesc() {
        return labeldesc;
    }

    public void setLabeldesc(String labeldesc) {
        this.labeldesc = labeldesc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(landmarkdesc);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(labeldesc);
        dest.writeString(image);
    }
}
