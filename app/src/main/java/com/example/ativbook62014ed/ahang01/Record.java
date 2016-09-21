package com.example.ativbook62014ed.ahang01;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Record extends Activity implements View.OnClickListener, MediaPlayer.OnCompletionListener{

    private static final int REC_STOP = 1;
    private static final int RECORDING = 0;
    private static final int PLAY_STOP = 0;
    private static final int PLAYING = 1;
    private static final int PLAY_PAUSE = 2;
    private static final int PLAY_NULL = 3;

    private int mStopState = 0;

    SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private TextView mTime;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private int mRecState = REC_STOP;   //현재 녹음 상태를 나타내는 것
    private int mPlayerState = PLAY_NULL;   //재상 상태를 나타내는 것
    private Button mBtnStartRec, mBtnStopRec, mBtnStartPlay;   //버튼
    private String mFileName = null; //파일경로와 파일명
    private String mFilePath = null;

    private int mCurTimeMs = 0;  //녹음을 할 때 시간을 나타내는 변수
//    private Button mBtnFile;    //파일리스트 띄우는 버튼
    private Button mBtnNext;

    private GpsInfo mGps;
    //    함수 시작
    Handler mProgressHandler = new Handler(){       //Handler를 통한 녹음 진행바 처리
        public void handleMessage(Message msg){
            mCurTimeMs = mCurTimeMs + 100;                            //시간의 흐름

            if(mCurTimeMs>0) {
                int maxMinPoint = mCurTimeMs / 1000 / 60;
                int maxSecPoint = (mCurTimeMs / 1000) % 60;
                String maxMinPointStr = "";
                String maxSecPointStr = "";

                if (maxMinPoint < 10)
                    maxMinPointStr = "0" + maxMinPoint + ":";
                else
                    maxMinPointStr = maxMinPoint + ":";

                if (maxSecPoint < 10)
                    maxSecPointStr = "0" + maxSecPoint;
                else
                    maxSecPointStr = String.valueOf(maxSecPoint);

                mTime.setText(maxMinPointStr + maxSecPointStr);   //재생시간 텍스트 설정

                mProgressHandler.sendEmptyMessageDelayed(0, 100);
            }
        }
    };
//    함수 끝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mTime = (TextView)findViewById(R.id.txt_Time);

        mBtnStartRec = (Button)findViewById(R.id.btn_Recording);
        mBtnStopRec = (Button)findViewById(R.id.btn_sRecording);
        mBtnStartPlay = (Button)findViewById(R.id.btn_Play);
//        mBtnFile = (Button)findViewById(R.id.btn_File);     //리스트 띄울 버튼
        mBtnNext = (Button) findViewById(R.id.btn_Next);

        mFilePath = getApplicationContext().getFilesDir().getAbsolutePath();
        mFilePath += "/AH_DialectCollector";
        File file = new File(mFilePath);
        file.mkdirs();

        mBtnStartRec.setOnClickListener((View.OnClickListener) this);
        mBtnStopRec.setOnClickListener((View.OnClickListener) this);
        mBtnStartPlay.setOnClickListener((View.OnClickListener) this);
//        mBtnFile.setOnClickListener((View.OnClickListener) this);
        mBtnNext.setOnClickListener(this);

        mBtnStartRec.setClickable(true);
        mBtnStopRec.setClickable(false);
        mBtnStartPlay.setClickable(false);

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("녹음을 하기위해서 마이크와 저장소 접근 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.READ_CONTACTS)
                .check();
    }


    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(Record.this, "권한 허가", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(Record.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };
    //  함수시작
    public void onClick(View v){        //위에서 입력받은 클릭 이벤트를 switch로 돌려서 어떤 이벤트인지 확인을해서 실행
        switch(v.getId()){
            case R.id.btn_Recording:
                mBtnStartRecOnClick();
                break;
            case R.id.btn_sRecording:
                if(mStopState == 1) {
                    mBtnStopRecOnClick();
                    break;
                }
                else if(mStopState == 2){
                    mBtnStopPlayOnClick();
                    break;
                }
                else
                    break;
            case R.id.btn_Play:
                mBtnStartPlayOnClick();
                break;
/*            case R.id.btn_File:
                mBtnFileOnClick();
                break;*/

            case R.id.btn_Next:
                nextOnClick();
                break;

            default:
                break;
        }
    }
/*    private void mBtnFileOnClick(){
        Toast.makeText(this, "성공?", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, FileBrowser.class);
        startActivity(intent);
    }*/
    //함수끝
    private void mBtnStopRecOnClick(){
        if(mRecState == RECORDING){
            /*NamePopUpActivity dialog = new NamePopUpActivity(this);     //다이얼로그 사용을 위한 선언
            dialog.setContentView(R.layout.activity_name_pop_up);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = 1000;
            params.height = 350;
            dialog.getWindow().setAttributes(params);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface $dialog) {
                    NamePopUpActivity dialog = (NamePopUpActivity) $dialog;
                    String name = dialog.getName();
                    File prefile =  new File(mFilePath + mFileName);
                    mFileName = "AH_" + name + "_Rec.mp4";
                    prefile.renameTo(new File(mFilePath + mFileName));
                }
            });
            dialog.show();      //다이얼로그 띄우기*/

            Toast.makeText(Record.this, mTime.getText(), Toast.LENGTH_SHORT).show();
            mBtnStartRec.setBackgroundResource(R.drawable.rec);
            mBtnStartRec.setClickable(true);
            mBtnStopRec.setClickable(false);
            mBtnStartPlay.setClickable(true);
            mRecState = REC_STOP;
            stopRec();
            mStopState = 0;
            mTime.setText("00:00");
            updateUI();
        }
    }
    //    함수시작
    private void mBtnStartRecOnClick(){     //녹음 버튼을 눌렀을때 녹음 시작및 중지
        if(mRecState == REC_STOP){
            Toast.makeText(Record.this, "녹음 시작", Toast.LENGTH_SHORT).show();
            mBtnStartRec.setBackgroundResource(R.drawable.recing);
            mBtnStartRec.setClickable(false);
            mBtnStopRec.setClickable(true);
            mBtnStartPlay.setClickable(false);
            mStopState = 1;
            mPlayerState = PLAY_STOP;
            mCurTimeMs = 0;
            mRecState = RECORDING;
            mFileName = "/AH_" + timeStampFormat.format(new Date()).toString() + "_Rec.mp4";
            startRec();
            updateUI();
        }
    }
//    함수끝

    //    함수시작
    private void startRec(){    //시작 버튼이 눌리고 녹음을 시작하게 설정
        mRecState = RECORDING;

        mProgressHandler.sendEmptyMessageDelayed(0, 100);   //시간의 딜레리를 가지고 지속적으로 핸들러를 호출

        if(mRecorder == null){
            mRecorder = new MediaRecorder();
            mRecorder.reset();
        }
        else{
            mRecorder.reset();
        }

        try{
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mRecorder.setOutputFile(mFilePath + mFileName);
            mRecorder.prepare();
            mRecorder.start();
        }
        catch(IllegalStateException e){
            Toast.makeText(this, "IllegalStateException", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e){
            Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show();
        }
    }
//    함수 끝

    //    함수시작
    private void stopRec(){     //중지를 누르거나 시간이 초과시 녹음이 중지
        try{
            mRecorder.stop();
        }
        catch (Exception e){

        }
        finally {
            mRecorder.release();
            mRecorder = null;
        }
        mCurTimeMs = -999;
        mProgressHandler.sendEmptyMessageDelayed(0, 0);
    }
//    함수 끝

    //    함수시작
    private void mBtnStartPlayOnClick(){    //재생 시작버튼
        if(mPlayerState == PLAY_STOP){  //정지되어 있으면 시작
            Toast.makeText(Record.this, "재생 시작", Toast.LENGTH_SHORT).show();
            mBtnStartPlay.setBackgroundResource(R.drawable.pause);
            mBtnStartRec.setClickable(false);
            mBtnStopRec.setClickable(true);
            mBtnStartPlay.setClickable(true);
            mStopState = 2;
            mPlayerState = PLAYING;
            mCurTimeMs = 0;
            initMediaPlayer();
            startPlay();
            updateUI();
        }
        else if(mPlayerState == PLAYING){   //재생 중이면 일시 정지
            Toast.makeText(Record.this, "일시정지", Toast.LENGTH_SHORT).show();
            mBtnStartPlay.setBackgroundResource(R.drawable.play);
            mPlayerState = PLAY_PAUSE;
            pausePlay();
            updateUI();
        }
        else if(mPlayerState == PLAY_PAUSE){    //일시 정지면 재생 시작
            Toast.makeText(Record.this, "재생 시작", Toast.LENGTH_SHORT).show();
            mBtnStartPlay.setBackgroundResource(R.drawable.pause);
            mPlayerState = PLAYING;
            startPlay();
            updateUI();
        }
    }
//    함수 끝

    //    함수 시작
    private void mBtnStopPlayOnClick(){
        if(mPlayerState == PLAYING || mPlayerState == PLAY_PAUSE){
            Toast.makeText(Record.this, "재생 중지", Toast.LENGTH_SHORT).show();
            mBtnStartPlay.setBackgroundResource(R.drawable.play);
            mBtnStartRec.setClickable(true);
            mBtnStopRec.setClickable(true);
            mBtnStartPlay.setClickable(true);
            mStopState = 0;
            mTime.setText("00:00");
            mPlayerState = PLAY_STOP;
            stopPlay();
            releaseMediaPlayer();   //무슨 함수인가?
            updateUI();
        }
    }
//    함수 끝

    //    함수 시작
    private void initMediaPlayer(){     //미디어를 초기화한다.(다음의 사용을 위해서)
        if(mPlayer == null)
            mPlayer = new MediaPlayer();
        else
            mPlayer.reset();

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mBtnStartRec.setClickable(true);
                mBtnStopRec.setClickable(true);
                mBtnStartPlay.setClickable(true);
                mStopState = 0;
                mTime.setText("00:00");
                Toast.makeText(Record.this, "재생 종료", Toast.LENGTH_SHORT).show();
                mBtnStartPlay.setBackgroundResource(R.drawable.play);
                mPlayerState = PLAY_STOP;
                stopPlay();
                releaseMediaPlayer();   //무슨 함수인가?
                updateUI();
            }
        });  //재생 중에 재생이 중지되었을때 호출이 된다.
        String fullFilePath = mFilePath + mFileName;

        try{
            FileInputStream fis = new FileInputStream(fullFilePath);
            FileDescriptor fd = fis.getFD();
            mPlayer.setDataSource(fd);        //실제 파일에 접근을 하는 것
            mPlayer.prepare();
        }
        catch (Exception e){
            Log.v("ProgressRecorder", "미디어 플레이어 Prepare Error =======> " + e);
        }
    }
//    함수 끝

    //    함수 시작
    private void startPlay(){       //재생을 시작하는 함수
        Log.v("ProgressRecorder", "startPlay()...");

        try{
            mPlayer.start();
            mProgressHandler.sendEmptyMessageDelayed(0, 100);
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
//    함수 끝

    //    함수 시작
    private void pausePlay(){   //재생을 일시 정지하는 함수
        Log.v("ProgressRecorder", "pausePlay()...");

        mPlayer.pause();    //재생을 일시정지하고
        mCurTimeMs = -999;
        mProgressHandler.sendEmptyMessageDelayed(0,0);     //재생이 일시 정지되면 즉시 상태바 메세지 핸들러를 호출
    }
//    함수 끝

    //    함수 시작
    private void stopPlay(){        //중지하는 함수
        Log.v("ProgressRecorder", "stopPlay()...");
        mPlayer.stop();
        mCurTimeMs = -999;
        mProgressHandler.sendEmptyMessageDelayed(0,0);
    }
//    함수 끝

    //    함수 시작
    private void releaseMediaPlayer(){  // 미디어 해제하기(다음에 또 미디어를 사용하기 위해서)
        Log.v("ProgressRecorder", "releaseMediaPlayer()...");
        mPlayer.release();
        mPlayer = null;
    }
//    함수 끝

    //    함수 시작
    public void onCompletion(MediaPlayer mp){   //재생이 종료되면 상태바의 상태를 호출
        mPlayerState = PLAY_STOP;
        mCurTimeMs = -999;
        mProgressHandler.sendEmptyMessageDelayed(0,0); //재생이 종료되면 즉시 상태바 메세지 핸드러를 호출
        updateUI();
    }
//    함수 끝

    //    함수 시작
    private void updateUI(){
/*        if(mRecState == REC_STOP){
            mBtnStartRec.setText("녹음");
//            mRecProgressBar.setProgress(0);
        }
        else if(mRecState == RECORDING){
            mBtnStartRec.setText("녹음 중");
        }
        if(mPlayerState == PLAY_STOP){
            mBtnStartPlay.setText("재생");
//            mPlayProgressBar.setProgress(0);
        }
        else if(mPlayerState == PLAYING)
            mBtnStartPlay.setText("일시정지");
        else if(mPlayerState == PLAY_PAUSE)
            mBtnStartPlay.setText("재생");*/
    }
//    함수 끝

    private void nextOnClick(){
        Log.e("다음버튼클릭","");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        mGps = new GpsInfo(this);
        Intent intent = new Intent(Record.this, GpsMapView.class);
        intent.putExtra("root_path",mFilePath);
        intent.putExtra("file_name",mFileName);
        intent.putExtra("Lat", mGps.getLatitude());
        intent.putExtra("Lon", mGps.getLongitude());
        intent.putExtra("Address", mGps.getAddress(getApplicationContext(), mGps.getLatitude(), mGps.getLongitude()));
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Record.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
