package com.example.ativbook62014ed.ahang01;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lai.OH on 2016-09-13.
 */
public class SearchDialectListActivity extends AppCompatActivity {

    private ArrayList<SearchListItem> mItem;
    private SearchDialectListAdapter mAdapter;
    private ListView listview;
    private RelativeLayout layout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialect_listview);

        listview = (ListView) findViewById(R.id.dialect_listview);
        layout = (RelativeLayout) findViewById(R.id.dialect_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mItem = new ArrayList<>();

        QueryDialectThread query = new QueryDialectThread();
        query.execute(getIntent().getStringExtra("dialect"));
    }


    class QueryDialectThread extends AsyncTask<String, Void, Void> {

        final String SERVER_URL = "http://210.117.181.66:8080/AHang/search_dialect_list.php";
        RequestHandler rh = new RequestHandler();
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(SearchDialectListActivity.this);
            loading.setMessage("목록 불러오는중...");
            loading.setCancelable(false);
            loading.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new SearchDialectListAdapter(SearchDialectListActivity.this, mItem);
            listview.setAdapter(mAdapter);

            loading.dismiss();
        }

        @Override
        protected Void doInBackground(String... params) {


            HashMap<String,String> data = new HashMap<>();

            String query = params[0];

            data.put("query", query);
            String result = rh.sendPostRequest(SERVER_URL,data);
            Log.e("result Data", result.toString());

            mItem.clear();
            try {
                JSONArray ja = new JSONArray(result);
                Log.e("ja.length()", String.valueOf(ja.length()));
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject order = ja.getJSONObject(i);

                    int getId = Integer.parseInt(order.get("id").toString());
                    String getDialect = order.get("dialect").toString();
                    Double getLatitude = Double.parseDouble(order.get("latitude").toString());
                    Double getLongitude = Double.parseDouble(order.get("longitude").toString());
                    String getAddress = order.get("do").toString();

                    mItem.add(new SearchListItem(getId, 0,getDialect, R.drawable.cyan, getLatitude, getLongitude,getAddress, BitmapDescriptorFactory.HUE_CYAN));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
