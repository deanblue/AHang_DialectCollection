package com.example.ativbook62014ed.ahang01;

import android.app.Dialog;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Lai.OH on 2016-09-05.
 */
public class ColorPickDialogUtil extends Dialog {

    private ImageView originImageView;

    private ImageView black;
    private ImageView red;
    private ImageView orange;
    private ImageView yellow;
    private ImageView blue;
    private ImageView green;
    private ImageView purple;
    private ImageView pink;

    private ArrayList<SearchListItem> item;
    int position;

    public ColorPickDialogUtil(Context context, ImageView imageview, ArrayList<SearchListItem> item, int position) {
        super(context);

        this.originImageView = imageview;
        this.item = item;
        this.position = position;
    }

    public void setColor(int position, int colorResource) {
        this.item.get(position).setColor(colorResource);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_colorpick_dialog);

        black = (ImageView) findViewById(R.id.color_dialog_black);
        red = (ImageView) findViewById(R.id.color_dialog_red);
        orange = (ImageView) findViewById(R.id.color_dialog_orange);
        yellow = (ImageView) findViewById(R.id.color_dialog_yellow);
        blue = (ImageView) findViewById(R.id.color_dialog_blue);
        green = (ImageView) findViewById(R.id.color_dialog_green);
        purple = (ImageView) findViewById(R.id.color_dialog_purple);
        pink = (ImageView) findViewById(R.id.color_dialog_pink);

        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.black);
                setColor(position, R.drawable.black);
                dismiss();
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.red);
                setColor(position, R.drawable.red);
                dismiss();
            }
        });

        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.orange);
                setColor(position, R.drawable.orange);
                dismiss();
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.yellow);
                setColor(position, R.drawable.yellow);
                dismiss();
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.blue);
                setColor(position, R.drawable.blue);
                dismiss();
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.green);
                setColor(position, R.drawable.green);
                dismiss();
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.purple);
                setColor(position, R.drawable.purple);
                dismiss();
            }
        });
        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.pink);
                setColor(position, R.drawable.pink);
                dismiss();
            }
        });
    }
}
