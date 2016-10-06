package com.example.ativbook62014ed.ahang01;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Lai.OH on 2016-09-08.
 */
public class RecordDetailInfoDialogUtil extends Dialog {

    private Context mContext;
    private String mRecordPath;
    private String mStandard;
    private String mDialect;
    private String mAddress;

    private String filename;

    private TextView standard_tv;
    private TextView dialect_tv;
    private TextView address_tv;
    private ImageView audio_iv;


    // Media Player Object
    private MediaPlayer mPlayer;
    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Progress Dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    public RecordDetailInfoDialogUtil(Context context, String record_path, String standard, String dialect, String address) {
        super(context);

        this.mContext = context;
        this.mRecordPath = record_path;
        this.mStandard = standard;
        this.mDialect = dialect;
        this.mAddress = address;
    }

    private void init(){
        standard_tv = (TextView) findViewById(R.id.detail_standard);
        dialect_tv = (TextView) findViewById(R.id.detail_dialect);
        address_tv = (TextView) findViewById(R.id.detail_address);
        audio_iv = (ImageView) findViewById(R.id.detail_record);

        audio_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio_iv.setEnabled(false);
                //record path에서 filename만 뽑아냄
                String[] array = mRecordPath.split("/");
                filename = array[array.length - 1];
                File file = new File(mContext.getFilesDir().getAbsolutePath() + "/AH_DialectCollector/" + filename);

                // Check if the Music file already exists
                if (file.exists()) {
                    //Toast.makeText(mContext, "File already exist under SD card, playing Music", Toast.LENGTH_LONG).show();
                    // Play Music
                    playMusic();
                    // If the Music File doesn't exist in SD card (Not yet downloaded)
                } else {
                    //Toast.makeText(mContext, "File doesn't exist under SD Card, downloading Mp3 from Internet", Toast.LENGTH_LONG).show();
                    // Trigger Async Task (onPreExecute method)
                    new DownloadMusicfromInternet().execute(mRecordPath);
                }
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_infodetail_dialog);

        init();

        standard_tv.setText("표준어 : " + mStandard);
        dialect_tv.setText("지역어 : " + mDialect);
        address_tv.setText("주소 : " + mAddress);

    }

    // Async Task Class
    class DownloadMusicfromInternet extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            prgDialog = new ProgressDialog(mContext);
            prgDialog.setMessage("녹음파일을 다운로드 중입니다...");
            prgDialog.setIndeterminate(false);
            prgDialog.setMax(100);
            prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // Get Music file length
                int lenghtOfFile = conection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(),10*1024);
                // Output stream to write file in SD card
                OutputStream output = new FileOutputStream(mContext.getFilesDir().getAbsolutePath()+ "/AH_DialectCollector/" + filename);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress which triggers onProgressUpdate method
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // Write data to file
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();
                // Close streams
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
            prgDialog.dismiss();
            Toast.makeText(mContext, "다운로드 완료", Toast.LENGTH_LONG).show();
            // Play the music
            playMusic();
        }
    }

    // Play Music
    protected void playMusic(){
        // Read Mp3 file present under SD card
        Uri myUri1 = Uri.parse(mContext.getFilesDir().getAbsolutePath()+ "/AH_DialectCollector/" + filename);
        mPlayer  = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(mContext, myUri1);
            mPlayer.prepare();
            // Start playing the Music file
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    // Once Music is completed playing, enable the button
                    audio_iv.setEnabled(true);
                    //Toast.makeText(mContext, "Music completed playing",Toast.LENGTH_LONG).show();
                }
            });
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContext, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(mContext, "URI cannot be accessed, permissed needed", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(mContext, "Media Player is not in correct state", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(mContext, "IO Error occured", Toast.LENGTH_LONG).show();
        }
    }
}
