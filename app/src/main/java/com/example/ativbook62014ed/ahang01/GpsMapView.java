package com.example.ativbook62014ed.ahang01;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class GpsMapView extends AppCompatActivity implements OnMapReadyCallback {

    private static LatLng nowAddress;
    private GoogleMap mGoogleMap;

    private double mLat;
    private double mLon;
    private String mNowAddressKorea;

    private TextView tv_nowAddress;
    private EditText et_standard;
    private EditText et_dialect;
    private Button btn_register;

    private void init(){
        tv_nowAddress = (TextView) findViewById(R.id.tv_addressKorea);
        et_standard = (EditText) findViewById(R.id.et_standard);
        et_dialect = (EditText) findViewById(R.id.et_dialect);
        btn_register = (Button) findViewById(R.id.btn_register);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        Intent intent = getIntent();
        mLat = intent.getDoubleExtra("Lat", 0);
        mLon = intent.getDoubleExtra("Lon", 0);
        mNowAddressKorea = intent.getStringExtra("Address");

        nowAddress = new LatLng(mLat,mLon);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        Marker nowPosition = mGoogleMap.addMarker(new MarkerOptions().position(nowAddress).title(mNowAddressKorea));

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowAddress, 15));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        tv_nowAddress.setText(mNowAddressKorea);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_map_view);

        init();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Lat : ", String.valueOf(mLat));
                Log.e("Lon : ", String.valueOf(mLon));
                Log.e("표준어 : " , et_standard.getText().toString());
                Log.e("표준어 : " , et_dialect.getText().toString());
            }
        });
    }
}
