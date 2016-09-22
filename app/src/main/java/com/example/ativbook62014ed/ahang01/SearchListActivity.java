package com.example.ativbook62014ed.ahang01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    private Button btn_colorChange;

    private SearchStandardListAdapter mStandardAdapter;
    private SearchDialectListAdapter mDialectAdapter;
    private ArrayList<SearchListItem> mDialectList;

    private ArrayList<SearchListItem> mMapViewList;

    private ArrayList<String> getDialectList;
    private ArrayList<Float> getColorList;

    private boolean confirm = true;

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
        btn_colorChange = (Button) findViewById(R.id.search_color_change);
        search_button.setOnClickListener(this);
        mapview_button.setOnClickListener(this);
        btn_colorChange.setOnClickListener(this);


        mDialectList = new ArrayList<>();
        mMapViewList = new ArrayList<>();

        getDialectList = new ArrayList<>();
        getColorList = new ArrayList<>();
        /*dialect_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchListItem item = (SearchListItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(SearchListActivity.this, SearchDialectListActivity.class);
                intent.putExtra("dialect", item.getDialect());
                startActivity(intent);
            }
        });*/
    }

    class QueryStandardThread extends AsyncTask<String, Void, Void> {

        final String SERVER_URL = "http://210.117.181.66:8080/AHang/search_standard_list.php";
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
            mStandardAdapter = new SearchStandardListAdapter(SearchListActivity.this, mDialectList);
            dialect_listview.setAdapter(mStandardAdapter);
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
                Log.e("ja.length()", String.valueOf(ja.length()));
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject order = ja.getJSONObject(i);

                    //int getId = Integer.parseInt(order.get("id").toString());
                    String getDialect = order.get("dialect").toString();
                    //Double getLatitude = Double.parseDouble(order.get("latitude").toString());
                    //Double getLongitude = Double.parseDouble(order.get("longitude").toString());

                    mDialectList.add(new SearchListItem(1, 0, getDialect, R.drawable.cyan, 1.1, 1.1,"", BitmapDescriptorFactory.HUE_CYAN));
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
                QueryStandardThread query = new QueryStandardThread();
                query.execute(search_query_edt.getText().toString());
                search_query_edt.setText("");
                btn_colorChange.setEnabled(true);
                break;

            case R.id.go_mapview_button :

                getDialectList.clear();

                Log.e("mapview gogogogo", "gogogogo");
                for (SearchListItem item :
                        mDialectList) {
                    if (item.getCheck() == 1) {
                        getDialectList.add(item.getDialect());
                        getColorList.add(item.getColorString());
                    }
                }

                GetDialectInfoTask task = new GetDialectInfoTask();
                task.execute();

                break;

            case R.id.search_color_change :
                change_color();
                mStandardAdapter = new SearchStandardListAdapter(SearchListActivity.this, mDialectList);
                dialect_listview.setAdapter(mStandardAdapter);
                break;
        }
    }

    class GetDialectInfoTask extends AsyncTask<String, Void, Void> {

        final String SERVER_URL = "http://210.117.181.66:8080/AHang/mapview_info_list.php";
        RequestHandler rh = new RequestHandler();
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(SearchListActivity.this);
            loading.setMessage("방언지도 준비중...");
            loading.setCancelable(false);
            loading.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(SearchListActivity.this, SearchMapView.class);
            intent.putParcelableArrayListExtra("dialectList", mMapViewList);
            startActivity(intent);
            loading.dismiss();
        }

        @Override
        protected Void doInBackground(String... params) {


            HashMap<String,String> data = new HashMap<>();

            data.put("size", String.valueOf(getDialectList.size()));
            for(int i = 0; i < getDialectList.size(); i++) {
                data.put("data" + String.valueOf(i), getDialectList.get(i));
                //data.put("color" + String.valueOf(i), String.valueOf(getColorList.get(i)));
            }
            String result = rh.sendPostRequest(SERVER_URL,data);
            Log.e("result Data", result.toString());

            mMapViewList.clear();
            try {
                JSONArray ja = new JSONArray(result);
                Log.e("ja.length()", String.valueOf(ja.length()));
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject order = ja.getJSONObject(i);

                    int getId = Integer.parseInt(order.get("id").toString());
                    String getDialect = order.get("dialect").toString();
                    Log.e("지역어", order.getString("dialect").toString());
                    Double getLatitude = Double.parseDouble(order.get("latitude").toString());
                    Double getLongitude = Double.parseDouble(order.get("longitude").toString());
                    String getAddress = order.get("address").toString();
                    int getColor = 0;
                    float getColorString = 0;

                    for(int k = 0; k < mDialectList.size(); k++) {
                        if (getDialect.equals(mDialectList.get(k).getDialect())) {
                            getColor = mDialectList.get(k).getColor();
                            getColorString = mDialectList.get(k).getColorString();
                            break;
                        }
                    }
                    mMapViewList.add(new SearchListItem(getId, 0, getDialect, getColor, getLatitude, getLongitude,getAddress, getColorString));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void change_color(){
        if (confirm){
            confirm = false;
            btn_colorChange.setText(R.string.btn_change_one);
            int count = mDialectList.size();
            for(int i=0; i<count; i++){
                switch (i%10){
                    case 0 :
                        mDialectList.get(i).setColorString(BitmapDescriptorFactory.HUE_RED);
                        mDialectList.get(i).setColor(R.drawable.red);
                        break;
                    case 1 :
                        mDialectList.get(i).setColorString(BitmapDescriptorFactory.HUE_ROSE);
                        mDialectList.get(i).setColor(R.drawable.pink);
                        break;
                    case 2 :
                        mDialectList.get(i).setColorString(BitmapDescriptorFactory.HUE_MAGENTA);
                        mDialectList.get(i).setColor(R.drawable.magenta);
                        break;
                    case 3 :
                        mDialectList.get(i).setColorString(BitmapDescriptorFactory.HUE_VIOLET);
                        mDialectList.get(i).setColor(R.drawable.purple);
                        break;
                    case 4 :
                        mDialectList.get(i).setColorString(BitmapDescriptorFactory.HUE_CYAN);
                        mDialectList.get(i).setColor(R.drawable.cyan);
                        break;
                    case 5 :
                        mDialectList.get(i).setColorString(BitmapDescriptorFactory.HUE_AZURE);
                        mDialectList.get(i).setColor(R.drawable.azure);
                        break;
                    case 6 :
                        mDialectList.get(i).setColorString(BitmapDescriptorFactory.HUE_BLUE);
                        mDialectList.get(i).setColor(R.drawable.blue);
                        break;
                    case 7 :
                        mDialectList.get(i).setColorString(BitmapDescriptorFactory.HUE_GREEN);
                        mDialectList.get(i).setColor(R.drawable.green);
                        break;
                    case 8 :
                        mDialectList.get(i).setColorString(BitmapDescriptorFactory.HUE_YELLOW);
                        mDialectList.get(i).setColor(R.drawable.yellow);
                        break;
                    case 9 :
                        mDialectList.get(i).setColorString(BitmapDescriptorFactory.HUE_ORANGE);
                        mDialectList.get(i).setColor(R.drawable.orange);
                        break;
                }
            }
        }
        else{
            confirm = true;
            btn_colorChange.setText(R.string.btn_change_random);
            int count = mDialectList.size();
            for(int i=0; i<count; i++){
                mDialectList.get(i).setColorString(BitmapDescriptorFactory.HUE_CYAN);
                mDialectList.get(i).setColor(R.drawable.cyan);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
