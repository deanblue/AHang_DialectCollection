package com.example.ativbook62014ed.ahang01;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchMapView extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private static LatLng centerAddress = new LatLng(35.95, 128.25);
    private GoogleMap mGoogleMap;

    private ArrayList<SearchListItem> mDialectList;

    private Marker selectedMarker;
    private View marker_root_view;

    private double mLat;
    private double mLon;
    private String mNowAddressKorea;

    private Context mContext;

    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map_view);

       /* SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38, 127),7));
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnMapClickListener(this);

        getMarkerItem();
    }

    private void getMarkerItem(){
        mDialectList = new ArrayList<>();
        Intent intent = getIntent();
        mDialectList = intent.getParcelableArrayListExtra("dialectList");

        Log.e("Marker Data : ", String.valueOf(mDialectList));

        for (SearchListItem markerItem : mDialectList){
            Log.e("count : " , Integer.toString(count++));
            addMarker(markerItem, false);
        }
    }

    private Marker addMarker(SearchListItem markerItem, boolean isSelecteMarker){
        LatLng position = new LatLng(markerItem.getLatitude(), markerItem.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(markerItem.getDialect());
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(markerItem.getColorString()));
        markerOptions.snippet(Integer.toString(markerItem.getId()));

        Log.e("Marker id : ", Integer.toString(markerItem.getId()));
        Log.e("Marker dialect : ", markerItem.getDialect());
        Log.e("Marker lat : ", Double.toString(markerItem.getLatitude()));
        Log.e("Marker lon : ", Double.toString(markerItem.getLongitude()));


        Log.e("Marker Options", String.valueOf(markerOptions));
        return mGoogleMap.addMarker(markerOptions);
    }

/*    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }*/

    @Override
    public boolean onMarkerClick(Marker marker) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        mGoogleMap.animateCamera(center);
        Log.e("Click Marker Id : ", marker.getSnippet());
        Log.e("Click Marker Dialect", marker.getTitle());


        AudioDetailInfo http = new AudioDetailInfo();
        http.execute(marker.getSnippet());
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    class AudioDetailInfo extends AsyncTask<String, Void, String> {

        final String SERVER_URL = "http://210.117.181.66:8080/AHang/audio_detail_info.php";
        RequestHandler rh = new RequestHandler();
        private ProgressDialog loading;

        private Context mContext = SearchMapView.this;

        int getId;
        String getAudioPath;
        String getStandard;
        String getDialect;
        String getAddress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(SearchMapView.this);
            loading.setMessage("목록 불러오는중...");
            loading.setCancelable(false);
            loading.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e("id : ", Integer.toString(getId));
            Log.e("audioPath : ", getAudioPath);
            Log.e("standard : ", getStandard);
            Log.e("dialect : ", getDialect);
            Log.e("getAddress : ", getAddress);

            RecordDetailInfoDialogUtil dialog = new RecordDetailInfoDialogUtil(this.mContext, getStandard, getDialect, getAddress);
            dialog.show();
            loading.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {


            HashMap<String,String> data = new HashMap<>();

            String id = params[0];

            data.put("audio_id", id);
            String result = rh.sendPostRequest(SERVER_URL,data);
            Log.e("result Data", result.toString());

            try {
                JSONArray ja = new JSONArray(result);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject order = ja.getJSONObject(i);

                    getId = Integer.parseInt(order.get("id").toString());
                    getAudioPath = order.get("audio_path").toString();
                    getStandard = order.get("standard").toString();
                    getDialect = order.get("dialect").toString();
                    getAddress = order.get("address").toString();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
