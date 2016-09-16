package com.example.ativbook62014ed.ahang01;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class IntroActivity extends AppCompatActivity {

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mHandler = new Handler();
        mHandler.postDelayed(irun, 4000);
    }

    Runnable irun = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);

            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(irun);
    }
}
