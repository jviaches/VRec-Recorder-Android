package com.techflask.vrec;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * @author Delph
 */
public class main extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        setRequestedOrientation(1); // http://developer.android.com/reference/android/R.attr.html#screenOrientation

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;
        MyView view = null;

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, Rec.class);
        view = new MyView(this, R.drawable.rec_cat_icon_not_sel, R.drawable.rec_cat_icon_sel, "");
        view.setBackgroundDrawable(this.getResources().getDrawable(R.layout.selecttabbackground));
        spec = tabHost.newTabSpec("RecTab").setIndicator(view).setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, RecList.class);
        view = new MyView(this, R.drawable.list_cat_icon_not_sel, R.drawable.list_cat_icon_sel, "");
        view.setBackgroundDrawable(this.getResources().getDrawable(R.layout.selecttabbackground));
        spec = tabHost.newTabSpec("RecListTab").setIndicator(view).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Settingz.class);
        view = new MyView(this, R.drawable.sett_cat_icon_not_sel, R.drawable.sett_cat_icon_sel, "");
        view.setFocusable(true);
        view.setBackgroundDrawable(this.getResources().getDrawable(R.layout.selecttabbackground));
        spec = tabHost.newTabSpec("PropertiesTab").setIndicator(view).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, About.class);
        view = new MyView(this, R.drawable.info_cat_not_sel, R.drawable.info_cat_sel, "");
        view.setFocusable(true);
        view.setBackgroundDrawable(this.getResources().getDrawable(R.layout.selecttabbackground));
        spec = tabHost.newTabSpec("AboutTab").setIndicator(view).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = LayoutParams.WRAP_CONTENT;
        tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = LayoutParams.WRAP_CONTENT;
        tabHost.getTabWidget().getChildAt(2).getLayoutParams().height = LayoutParams.WRAP_CONTENT;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {

                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Exit VRec");
                alertDialog.setMessage("Are you sure ?");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                alertDialog.show();
                return true;
            }
            return super.dispatchKeyEvent(event);
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    private class MyView extends LinearLayout {
        ImageView iv;
        TextView tv, tv1;

        public MyView(Context c, int drawable, int drawableselec, String label) {
            super(c);

            iv = new ImageView(c);

            StateListDrawable listDrawable = new StateListDrawable();
            listDrawable.addState(SELECTED_STATE_SET, this.getResources()
                    .getDrawable(drawableselec));
            listDrawable.addState(ENABLED_STATE_SET, this.getResources()
                    .getDrawable(drawable));

            iv.setImageDrawable(listDrawable);
            iv.setPadding(20, 15, 20, 20);
            iv.setLayoutParams(new LayoutParams(80, 80, (float) 0.0));

            setGravity(Gravity.CENTER_HORIZONTAL);
            addView(iv);
        }
    }
}