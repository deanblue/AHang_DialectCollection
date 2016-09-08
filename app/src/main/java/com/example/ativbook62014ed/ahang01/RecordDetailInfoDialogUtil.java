package com.example.ativbook62014ed.ahang01;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;

/**
 * Created by Lai.OH on 2016-09-08.
 */
public class RecordDetailInfoDialogUtil extends Dialog {

    private Context mContext;
    private String mStandard;
    private String mDialect;
    private String mAddress;

    private TextView standard_tv;
    private TextView dialect_tv;
    private TextView address_tv;
    private ImageView audio_iv;

    public RecordDetailInfoDialogUtil(Context context, String standard, String dialect, String address) {
        super(context);

        this.mContext = context;
        this.mStandard = standard;
        this.mDialect = dialect;
        this.mAddress = address;
    }

    private void init(){
        standard_tv = (TextView) findViewById(R.id.detail_standard);
        dialect_tv = (TextView) findViewById(R.id.detail_dialect);
        address_tv = (TextView) findViewById(R.id.detail_address);
        audio_iv = (ImageView) findViewById(R.id.detail_record);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_infodetail_dialog);

        init();

        standard_tv.setText("표준어 : " + mStandard);
        dialect_tv.setText("지역어 : " + mDialect);
        address_tv.setText("주소 : " + mAddress);

    }
}
