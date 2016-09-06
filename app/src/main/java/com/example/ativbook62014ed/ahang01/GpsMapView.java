package com.example.ativbook62014ed.ahang01;

import android.*;
import android.Manifest;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class GpsMapView extends AppCompatActivity implements OnMapReadyCallback {

    private static LatLng nowAddress;
    private GoogleMap mGoogleMap;

    private double mLat;
    private double mLon;
    private String mNowAddressKorea;
    private String mRoot_path;
    private String mFile_name;
    private String mStandard;
    private String mDialect;


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
        mRoot_path = intent.getStringExtra("root_path");
        mFile_name = intent.getStringExtra("file_name");

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
                mStandard = et_standard.getText().toString();
                mDialect = et_dialect.getText().toString();

                UploadFileTask upfile = new UploadFileTask();
                upfile.execute(mRoot_path, mFile_name);
            }
        });
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }


    class UploadFileTask extends AsyncTask<String, Void, String> {

        final String urlString = "http://210.117.181.66:8080/AHang/audio_upload.php";


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.e("onPostExecute", s);
            } catch (Exception e) {
                e.printStackTrace();
            }

            AudioInfoInsertTask audioTask = new AudioInfoInsertTask();
            audioTask.execute(s, mStandard, mDialect, String.valueOf(mLat), String.valueOf(mLon), mNowAddressKorea);

        }

        @Override
        protected String doInBackground(String... voids) {
            HttpURLConnection conn = null;
            MultipartEntity entity = null;
            ByteArrayOutputStream bos = null;
            ByteArrayBody bab = null;
            OutputStream os = null;
            FileInputStream mFileInputStream = null;
            DataInputStream inStream = null;

            String file_path = voids[0];
            String file_name = voids[1];

            Log.e("filename : ", file_name);
            Log.e("selectedPath : ", file_path + file_name);
            try {
                File file = new File(file_path + file_name);
                mFileInputStream = new FileInputStream(file);

                URL url = new URL(urlString);
                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                // Allow Inputs
                conn.setDoInput(true);
                // Allow Outputs
                conn.setDoOutput(true);
                // Don't use a cached copy.
                conn.setUseCaches(false);
                // Use a post method.
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                //conn.setRequestProperty("Content-Type", "application/json");
                //conn.setRequestProperty("Accept", "application/json");

                entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                byte[] fileData = new byte[(int) mFileInputStream.available()];
                inStream = new DataInputStream(mFileInputStream);
                inStream.readFully(fileData);
                inStream.close();


                //bab = new ByteArrayBody(fileData, URLEncoder.encode(file_name, "UTF-8"));
                bab = new ByteArrayBody(fileData, file_name);
                entity.addPart("upload", bab);
                mFileInputStream.close();


                conn.addRequestProperty("Content-length", entity.getContentLength() + "");
                conn.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());



                os = conn.getOutputStream();
                entity.writeTo(conn.getOutputStream());
                os.flush();
                os.close();
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.e("HTTP OK", "HTTP OK");
                    String result = readStream(conn.getInputStream());
                    //Log.e("result", result);
                    return result;
                } else {
                    Log.e("HTTP CODE", "HTTP CONN FAILED");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    class AudioInfoInsertTask extends AsyncTask<String, Void, String> {

        final String urlString = "http://210.117.181.66:8080/AHang/insert_audio_info.php";

        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("success")) {
                Intent intent = new Intent(GpsMapView.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(GpsMapView.this, "파일 업로드중 문제가 발생하였습니다.", Toast.LENGTH_SHORT);
            }
            Log.e("AudioInfoInsertTask", s);
        }

        @Override
        protected String doInBackground(String... params) {

            String audio_path = params[0];
            String standard = params[1];
            String dialect = params[2];
            String latitude = params[3];
            String longitude = params[4];
            String address = params[5];


            HashMap<String,String> data = new HashMap<>();

            data.put("audio", audio_path);
            data.put("standard", standard);
            data.put("dialect", dialect);
            data.put("latitude", latitude);
            data.put("longitude", longitude);
            data.put("address",address);

            String result = rh.sendPostRequest(urlString, data);

            return result;
        }
    }
}
