package com.example.ativbook62014ed.ahang01;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class Record extends AppCompatActivity {

    final private static File RECORDED_FILE = Environment.getExternalStorageDirectory();

    String filename;
    MediaPlayer mPlayer;
    MediaRecorder mRecorder;

    private Button btn_record;
    private Button btn_record_stop;
    private Button btn_play;
    private Button btn_play_stop;

    void init(){
        btn_record = (Button) findViewById(R.id.btnRecord);
        btn_record_stop = (Button) findViewById(R.id.btnRecordStop);
        btn_play = (Button) findViewById(R.id.btnPlay);
        btn_play_stop = (Button) findViewById(R.id.btnPlayStop);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        init();

        filename = RECORDED_FILE.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4";

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRecorder != null){
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                }
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                mRecorder.setOutputFile(filename);

                try{
                    Toast.makeText(getApplicationContext(), "녹음이 시작되었습니다.", Toast.LENGTH_SHORT).show();
                    mRecorder.prepare();
                    mRecorder.start();
                }
                catch (Exception e){
                    Log.e("SampleAudioRecorder","Excpetion : ", e);
                }
            }
        });

        btn_record_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRecorder == null)
                    return;

                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;

                Toast.makeText(getApplicationContext(), "녹음이 중지되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer != null){
                    mPlayer.stop();
                    mPlayer.release();
                    mPlayer = null;
                }

                Toast.makeText(getApplicationContext(), "녹음된 파일을 재생합니다.", Toast.LENGTH_SHORT).show();

                try{
                    mPlayer = new MediaPlayer();
                    mPlayer.setDataSource(filename);
                    mPlayer.prepare();
                    mPlayer.start();
                }
                catch (Exception e){
                    Log.e("SampleAudioRecorder", "Audio play failed.", e);
                }
            }
        });

        btn_play_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer == null)
                    return;

                Toast.makeText(getApplicationContext(), "재생이 중지되었습니다.", Toast.LENGTH_SHORT).show();

                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }
        });
    }

    protected void onPause(){
        if(mRecorder != null){
            mRecorder.release();
            mRecorder = null;
        }
        if(mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        }

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
