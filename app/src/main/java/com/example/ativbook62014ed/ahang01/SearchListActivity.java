package com.example.ativbook62014ed.ahang01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchListActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView dialect_listview;
    private EditText search_query_edt;
    private Button search_button;
    private ImageView mapview_button;

    private SearchListAdapter mAdapter;
    private ArrayList<SearchListItem> mDialectList;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        init();

    }

    private void init(){
        dialect_listview = (ListView) findViewById(R.id.search_listview);
        search_query_edt = (EditText) findViewById(R.id.search_query_edt);
        search_button = (Button) findViewById(R.id.search_button);
        mapview_button = (ImageView) findViewById(R.id.go_mapview_button);
        search_button.setOnClickListener(this);
        mapview_button.setOnClickListener(this);

        mDialectList = new ArrayList<>();


    }

    class QueryMessageThread extends AsyncTask<String, Void, Void> {

        final String SERVER_URL = "http://210.117.181.66:8080/AHang/search_dialect_list.php";
        RequestHandler rh = new RequestHandler();
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(SearchListActivity.this);
            loading.setMessage("목록 불러오는중...");
            loading.setCancelable(false);
            loading.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new SearchListAdapter(SearchListActivity.this, mDialectList);
            dialect_listview.setAdapter(mAdapter);

            loading.dismiss();
        }

        @Override
        protected Void doInBackground(String... params) {


            HashMap<String,String> data = new HashMap<>();

            String query = params[0];

            data.put("query", query);
            String result = rh.sendPostRequest(SERVER_URL,data);
            Log.e("result Data", result.toString());

            mDialectList.clear();
            try {
                JSONArray ja = new JSONArray(result);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject order = ja.getJSONObject(i);

                    int getId = Integer.parseInt(order.get("id").toString());
                    String getDialect = order.get("dialect").toString();
                    Double getLatitude = Double.parseDouble(order.get("latitude").toString());
                    Double getLongitude = Double.parseDouble(order.get("longitude").toString());

                    mDialectList.add(new SearchListItem(getId, getDialect, R.drawable.cyan, getLatitude, getLongitude, BitmapDescriptorFactory.HUE_CYAN));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button :
                QueryMessageThread query = new QueryMessageThread();
                query.execute(search_query_edt.getText().toString());
                search_query_edt.setText("");
                break;

            case R.id.go_mapview_button :
                Log.e("mapview gogogogo", "gogogogo");
                for (SearchListItem item :
                        mDialectList) {
                    Log.e(item.getDialect() + "Value", String.valueOf(item.getId()) + "," + String.valueOf(item.getColor()));
                }

                Intent intent = new Intent(SearchListActivity.this, SearchMapView.class);
                intent.putParcelableArrayListExtra("dialectList", mDialectList);
                startActivity(intent);
                break;
        }
    }
}
