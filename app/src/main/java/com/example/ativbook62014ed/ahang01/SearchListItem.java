package com.example.ativbook62014ed.ahang01;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by ATIV Book 6 2014ED on 2016-09-04.
 */
public class SearchListItem implements Parcelable {

    private int mId;
    private String mDialect;
    private int mColor;
    private double mLatitude;
    private double mLongitude;


    public SearchListItem(Parcel in) {
        this.mId = in.readInt();
        this.mDialect = in.readString();
        this.mColor = in.readInt();
        this.mLatitude = in.readDouble();
        this.mLongitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mDialect);
        dest.writeInt(this.mColor);
        dest.writeDouble(this.mLatitude);
        dest.writeDouble(this.mLongitude);

    }

    public static final Parcelable.Creator<SearchListItem> CREATOR = new Parcelable.Creator<SearchListItem>() {
        public SearchListItem createFromParcel(Parcel in) {
            return new SearchListItem(in);
        }
        public SearchListItem[] newArray (int size) {
            return new SearchListItem[size];
        }
    };



    public SearchListItem(int id, String dialect, int color, double latitude, double longitude) {
        mId = id;
        mDialect = dialect;
        mColor = color;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getDialect() {
        return mDialect;
    }

    public void setDialect(String dialect) {
        mDialect = dialect;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}
