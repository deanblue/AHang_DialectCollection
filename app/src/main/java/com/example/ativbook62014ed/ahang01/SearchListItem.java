package com.example.ativbook62014ed.ahang01;

import android.graphics.Color;

/**
 * Created by ATIV Book 6 2014ED on 2016-09-04.
 */
public class SearchListItem {

    private int mId;
    private String mDialect;
    private int mColor;

    public SearchListItem(int id, String dialect, int color) {
        mId = id;
        mDialect = dialect;
        mColor = color;
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
}
