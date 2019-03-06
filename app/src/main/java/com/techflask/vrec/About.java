package com.techflask.vrec;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * Created by IntelliJ IDEA.
 * User: Delph
 * Date: 2/26/11
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class About extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // http://developer.android.com/reference/android/R.attr.html#screenOrientation
    }
}