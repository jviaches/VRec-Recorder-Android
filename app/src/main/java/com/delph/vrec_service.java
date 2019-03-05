package com.delph;

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
        audio_codec = sharedPreferences.getString("AudioCodec", "");
        file_counter = sharedPreferences.getInt("fCounter", 0);
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
            Toast.makeText(this, "Cant define storage directory - [Error 1]", 100).show();
        } catch (IllegalStateException e) {

        }

        String avaiable_counter = checkForCounter(file_counter, "Record N_", storageDir);
        try {
            // init recorder
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            if (audio_codec.equals("")) {
                SharedPreferences.Editor spe = sharedPreferences.edit();
                spe.putString("AudioCodec", "3GP");
                spe.commit();
                audio_codec = sharedPreferences.getString("AudioCodec", "");
            }

            if (audio_codec.equals("3GP")) {
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(Environment.getExternalStoragePublicDirectory(this.getString(R.string.FILEPATH)) + "/" +
                        avaiable_counter + ".3gp");
            } else if (audio_codec.equals("AMR")) {
                recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(Environment.getExternalStoragePublicDirectory(this.getString(R.string.FILEPATH)) + "/" +
                        avaiable_counter + ".amr");
            } else if (audio_codec.equals("MP4")) {
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(Environment.getExternalStoragePublicDirectory(this.getString(R.string.FILEPATH)) + "/" +
                        avaiable_counter + ".mp4");
            }
            recorder.prepare();
            recorder.start();

        } catch (IllegalStateException e) {
            Toast.makeText(this, "Illegal recorder state - [Error 2] ", 100).show();
            recorder.reset();
        } catch (IOException e) {
            Toast.makeText(this, "Illegal file format - [Error 3] ", 100).show();
            recorder.reset();
        }
    }

    private void stopRecord() {
        try {
            recorder.stop();
        } catch (Exception e) {
            Toast.makeText(this, "Cant stop record - [Error 4]", 100).show();
        }
    }

    // Method that makes audio file name according to current system counter
/*    public String checkForCounter(int counter, String filePreName, File dir) {

        String goodFileName = "";
        String[] children = dir.list();

        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                String filename = children[i];
                String[] tempName = filename.split("\\.");

                if (tempName[0].equals(filePreName + counter)) {
                    counter += 1;
                } else {
                    SharedPreferences.Editor spe = sharedPreferences.edit();
                    spe.putInt("fCounter", counter);
                    spe.commit();
                    return goodFileName += filePreName + counter;
                }
            }
            SharedPreferences.Editor spe = sharedPreferences.edit();
            spe.putInt("fCounter", counter);
            spe.commit();
            return goodFileName += filePreName + counter;
        }
        return goodFileName += filePreName+"0";
    }*/

    public String checkForCounter(int counter, String filePreName, File dir) {

        String goodFileName = "";


        String[] children = dir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                String filename = children[i];
                String[] tempName = filename.split("\\.");

                if (tempName[0].equals(filePreName + counter)) {
                    counter += 1;
                } else {
                    SharedPreferences.Editor spe = sharedPreferences.edit();
                    spe.putInt("fCounter", counter);
                    spe.commit();
                    return goodFileName += filePreName + counter;
                }
            }
            SharedPreferences.Editor spe = sharedPreferences.edit();
            spe.putInt("fCounter", counter);
            spe.commit();
            return goodFileName += filePreName + counter;
        }
        return goodFileName += filePreName;
    }


}
