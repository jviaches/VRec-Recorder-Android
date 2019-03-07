package com.techflask.vrec;

/**
 * Created by IntelliJ IDEA.
 * User: Delph
 * Date: 11/4/10
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rec extends Activity {

    boolean isRecording = false;  //Status of recorder
    private Handler mHandler = new Handler();
    private long mStartTime = 0L;
    TextView mTimeTextField;
    TextView recStatus;
    TextView timeLine;
    ImageButton rec_Btn;
    ImageButton stop_Btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vrec);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // http://developer.android.com/reference/android/R.attr.html#screenOrientation
        mTimeTextField = findViewById(R.id.timeLine);
        recStatus = findViewById(R.id.recStatus);
        rec_Btn = findViewById(R.id.btnRecord);
        stop_Btn = findViewById(R.id.btnStop);
        timeLine = findViewById(R.id.timeLine);
    }

    // Button Listener for starting recording
    // -> Listener bound through Rec.xml, inside ImageButton tag
    public void btn_StartRecordClick(android.view.View v) {

        if (!isRecording) {
            if (mStartTime == 0L) {
                mStartTime = System.currentTimeMillis();
                mHandler.removeCallbacks(mUpdateTimeTask);
                mHandler.postDelayed(mUpdateTimeTask, 100);
            }

            isRecording = true;
            rec_Btn.setImageDrawable(this.getResources().getDrawable(R.drawable.rec_start_btn_desable));
            stop_Btn.setImageDrawable(this.getResources().getDrawable(R.drawable.rec_stop_btn));
            recStatus.setText("Recording   ");
            startService(new Intent(this, vrec_service.class));

        } else {
            Toast msg = Toast.makeText(this, "Can't start new recording !\nAlready recording !", Toast.LENGTH_LONG);
            msg.show();
        }

    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long millis = System.currentTimeMillis() - mStartTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;
            minutes = minutes % 60;

            if (seconds < 10) {
                if (minutes < 10) {
                    mTimeTextField.setText("" + hours + ":0" + minutes + ":0" + seconds);
                }
            } else {
                if (minutes < 10) {
                    mTimeTextField.setText("" + hours + ":0" + minutes + ":" + seconds);
                } else {
                    mTimeTextField.setText("" + hours + ":" + minutes + ":" + seconds);
                }
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    // Button Listener for end recording
    // -> Listener bound through Rec.xml, inside ImageButton tag
    public void btn_StopRecordClick(android.view.View v) {
        if (isRecording /*|| isPaused*/) {
            isRecording = false;
            rec_Btn.setImageDrawable(getResources().getDrawable(R.drawable.rec_start_btn));
            stop_Btn.setImageDrawable(this.getResources().getDrawable(R.drawable.rec_stop_btn_desable));
            recStatus.setText("Stopped   ");
            timeLine.setText("0:00:00");
            stopService(new Intent(this, vrec_service.class));
            mHandler.removeCallbacks(mUpdateTimeTask);
            mStartTime = 0L;
            Toast.makeText(this, "New record has been created !", Toast.LENGTH_LONG).show();
        } else if (!isRecording) {
            Toast msg = Toast.makeText(this, "Nothing to stop ! \nNo active recording found ! !", Toast.LENGTH_LONG);
            msg.show();
        }
    }

/*    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH.mm.ss");
        Date date = new Date();
        return dateFormat.format(date);
    }*/

}