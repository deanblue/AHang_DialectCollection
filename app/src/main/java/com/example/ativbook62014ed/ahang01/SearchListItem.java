package com.example.ativbook62014ed.ahang01;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.BitmapDescriptor;


/**
 * Created by ATIV Book 6 2014ED on 2016-09-04.
 */
public class SearchListItem implements Parcelable {

    private int mId;
    private int mCheck;
    private String mDialect;
    private int mColor;
    private double mLatitude;
    private double mLongitude;
    private String mAddress;
    private float mColorString;

    public SearchListItem(Parcel in) {
        this.mId = in.readInt();
        this.mCheck = in.readInt();
        this.mDialect = in.readString();
        this.mColor = in.readInt();
        this.mLatitude = in.readDouble();
        this.mLongitude = in.readDouble();
        this.mAddress = in.readString();
        this.mColorString = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeInt(this.mCheck);
        dest.writeString(this.mDialect);
        dest.writeInt(this.mColor);
        dest.writeDouble(this.mLatitude);
        dest.writeDouble(this.mLongitude);
        dest.writeString(this.mAddress);
        dest.writeFloat(this.getColorString());

    }

    public static final Parcelable.Creator<SearchListItem> CREATOR = new Parcelable.Creator<SearchListItem>() {
        public SearchListItem createFromParcel(Parcel in) {
            return new SearchListItem(in);
        }
        public SearchListItem[] newArray (int size) {
            return new SearchListItem[size];
        }
    };



    public SearchListItem(int id, int check, String dialect, int color, double latitude, double longitude, String address, float colorString) {
        mId = id;
        mCheck = check;
        mDialect = dialect;
        mColor = color;
        mLatitude = latitude;
        mLongitude = longitude;
        mAddress = address;
        mColorString = colorString;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getCheck() {
        return mCheck;
    }

    public void setCheck(int check) {
        mCheck = check;
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

    public float getColorString() {
        return mColorString;
    }

    public void setColorString(float colorString) {
        mColorString = colorString;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }
}
