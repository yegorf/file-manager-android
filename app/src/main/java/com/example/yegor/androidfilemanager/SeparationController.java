package com.example.yegor.androidfilemanager;

import android.util.Log;
import android.view.ScaleGestureDetector;

public class SeparationController extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    private MainActivity activity;

    public SeparationController(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        if (detector.getScaleFactor() * 100 > 100) {
            activity.open();
            Log.w("kek", "open");
        } else {
            activity.close();
            Log.w("kek", "close");
        }
    }
}
