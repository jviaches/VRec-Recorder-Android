package com.techflask.vrec;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Delph
 * Date: 3/24/11
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class vrec_service extends Service {

    private MediaRecorder recorder;
    SharedPreferences sharedPreferences;


    @Override
    public void onCreate() {
        super.onCreate();
        startService();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopRecord();
        //Toast.makeText(this, "Vrec Service Stopped!", Toast.LENGTH_LONG).show();
    }

    private void startService() {
        startRecord();
        //Toast.makeText(this, "Service Started.", Toast.LENGTH_LONG).show();
    }

    private void startRecord() {
        String audio_codec;     // codec type: 3gp,amr,mp4,..
        int file_counter;
        File storageDir = null;

        // Find out what codec is active
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        audio_codec = sharedPreferences.getString("AudioCodec", "MP4");
        recorder = new MediaRecorder();

        try {
            //if such directory  exist
            if (Environment.getExternalStoragePublicDirectory(this.getString(R.string.FILEPATH)).exists()) {
                storageDir = Environment.getExternalStoragePublicDirectory(this.getString(R.string.FILEPATH));
            }//if directory does not exist
            else {
                storageDir = new File(Environment.getExternalStoragePublicDirectory(this.getString(R.string.FILEPATH)) + "");
                storageDir.mkdir();
            }

        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Cant define storage directory - [Error 1]", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {

        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String dateString = format.format( new Date()   );

        String fileName = "Record_" + dateString;
        try {
            // init recorder
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

//            if (audio_codec.equals("")) {
//                SharedPreferences.Editor spe = sharedPreferences.edit();
//                spe.putString("AudioCodec", "MP4");
//                spe.commit();
//                audio_codec = sharedPreferences.getString("AudioCodec", "");
//            }

            switch (audio_codec) {
                case "3GP":
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    recorder.setOutputFile(Environment.getExternalStoragePublicDirectory(this.getString(R.string.FILEPATH)) + "/" +
                            fileName + ".3gp");
                    break;
                case "AMR":
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    recorder.setOutputFile(Environment.getExternalStoragePublicDirectory(this.getString(R.string.FILEPATH)) + "/" +
                            fileName + ".amr");
                    break;
                case "MP4":
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    recorder.setOutputFile(Environment.getExternalStoragePublicDirectory(this.getString(R.string.FILEPATH)) + "/" +
                            fileName + ".mp4");
                    break;

                    default:
                        break;
            }
            recorder.prepare();
            recorder.start();

        } catch (IllegalStateException e) {
            Toast.makeText(this, "Illegal recorder state - [Error 2] ", Toast.LENGTH_SHORT).show();
            recorder.reset();
        } catch (IOException e) {
            Toast.makeText(this, "Illegal file format - [Error 3] ", Toast.LENGTH_SHORT).show();
            recorder.reset();
        }
    }

    private void stopRecord() {
        try {
            recorder.stop();
        } catch (Exception e) {
            Toast.makeText(this, "Cant stop record - [Error 4]", Toast.LENGTH_SHORT).show();
        }
    }

}
