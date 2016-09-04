package com.example.ativbook62014ed.ahang01;

import android.graphics.Color;

/**
 * Created by ATIV Book 6 2014ED on 2016-09-04.
 */
public class SearchListItem {

    private int id;
    private String standardLanguage;
    private String regionalLanguage;
    private Color mColor;

    public int getId() {
        return id;
    }

    public String getStandardLanguage() {
        return standardLanguage;
    }

    public void setStandardLanguage(String standardLanguage) {
        this.standardLanguage = standardLanguage;
    }

    public String getRegionalLanguage() {
        return regionalLanguage;
    }

    public void setRegionalLanguage(String regionalLanguage) {
        this.regionalLanguage = regionalLanguage;
    }

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color color) {
        mColor = color;
    }
}
