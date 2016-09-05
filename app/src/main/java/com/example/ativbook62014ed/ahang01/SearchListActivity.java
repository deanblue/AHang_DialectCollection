package com.example.ativbook62014ed.ahang01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchListActivity extends AppCompatActivity {

    private ListView dialect_listview;
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

        mDialectList = new ArrayList<>();

        mDialectList.add(new SearchListItem(0, "가위", R.drawable.black));
        mDialectList.add(new SearchListItem(1, "강팡", R.drawable.black));
        mDialectList.add(new SearchListItem(2, "가세", R.drawable.black));
        mDialectList.add(new SearchListItem(3, "거세", R.drawable.black));

        mAdapter = new SearchListAdapter(this, mDialectList);
        dialect_listview.setAdapter(mAdapter);
    }
}
