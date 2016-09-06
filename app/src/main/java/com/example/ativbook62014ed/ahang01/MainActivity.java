package com.example.ativbook62014ed.ahang01;

import android.content.Intent;
import android.net.LinkAddress;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout record_layout;
    private LinearLayout search_layout;

    void init(){
        record_layout = (LinearLayout) findViewById(R.id.main_layout_record);
        search_layout = (LinearLayout) findViewById(R.id.main_layout_search);

        record_layout.setOnClickListener(this);
        search_layout.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.search_color:
                Toast.makeText(this, "요거", Toast.LENGTH_SHORT);
                break;

            case R.id.main_layout_record :
                intent = new Intent(MainActivity.this, Record.class);
                startActivity(intent);
                finish();
                break;

            case R.id.main_layout_search :
                intent = new Intent(MainActivity.this, SearchListActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }
}
