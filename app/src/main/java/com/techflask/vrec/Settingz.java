package com.techflask.vrec;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by IntelliJ IDEA.
 * User: Delph
 * Date: 2/26/11
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Settingz extends Activity {

    public SharedPreferences prefs;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settgs);
        setRequestedOrientation(1); // http://developer.android.com/reference/android/R.attr.html#screenOrientation

        RadioButton radioButton_3pg = findViewById(R.id.codec_3pg);
        RadioButton radioButton_amr = findViewById(R.id.codec_amr);
        RadioButton radioButton_mpeg4 = findViewById(R.id.codec_mpeg4);

        // store variable
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String Acodec = prefs.getString("AudioCodec", "MP4");

        if (Acodec.equals("3GP")) {
            radioButton_amr.setChecked(false);
            radioButton_mpeg4.setChecked(false);
            radioButton_3pg.setChecked(true);

            radioButton_3pg.refreshDrawableState();
        } else if (Acodec.equals("AMR")) {
            radioButton_3pg.setChecked(false);
            radioButton_mpeg4.setChecked(false);
            radioButton_amr.setChecked(true);
        } else if (Acodec.equals("MP4")) {
            radioButton_3pg.setChecked(false);
            radioButton_amr.setChecked(false);
            radioButton_mpeg4.setChecked(true);
        }

        // Radio buttons Listeners implementations
        radioButton_3pg.setOnClickListener(new RadioButton.OnClickListener() {

            public void onClick(View view) {
                SharedPreferences.Editor spe = prefs.edit();
                spe.putString("AudioCodec", "3GP"); // we just save “true” in the xml file
                spe.apply();
            }
        });

        radioButton_amr.setOnClickListener(new RadioButton.OnClickListener() {

            public void onClick(View view) {
                SharedPreferences.Editor spe = prefs.edit();
                spe.putString("AudioCodec", "AMR"); // we just save “true” in the xml file
                spe.apply();
            }
        });


        radioButton_mpeg4.setOnClickListener(new RadioButton.OnClickListener() {

            public void onClick(View view) {
                SharedPreferences.Editor spe = prefs.edit();
                spe.putString("AudioCodec", "MP4"); // we just save “true” in the xml file
                spe.apply();
            }
        });


        TextView fPath = (TextView) findViewById(R.id.filepath);
        fPath.setText("    Storage directory : " + Environment.getExternalStoragePublicDirectory(this.getString(R.string.FILEPATH)));
    }
}