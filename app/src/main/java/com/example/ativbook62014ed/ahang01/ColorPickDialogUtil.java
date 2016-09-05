package com.example.ativbook62014ed.ahang01;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

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


    public ColorPickDialogUtil(Context context, ImageView imageview) {
        super(context);

        this.originImageView = imageview;
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
                dismiss();
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.red);
                dismiss();
            }
        });

        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.orange);
                dismiss();
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.yellow);
                dismiss();
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.blue);
                dismiss();
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.green);
                dismiss();
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.purple);
                dismiss();
            }
        });
        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originImageView.setImageResource(R.drawable.pink);
                dismiss();
            }
        });
    }
}
