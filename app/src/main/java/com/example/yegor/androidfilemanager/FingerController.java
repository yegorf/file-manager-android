package com.example.yegor.androidfilemanager;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.io.IOException;

public class FingerController extends GestureDetector.SimpleOnGestureListener {
    private final int SWIPE_MIN_DISTANCE = 70;
    private final int SWIPE_THRESHOLD_VELOCITY = 200;
    private MainActivity activity = null;

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            activity.close();
        }
        if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            activity.open();
        }
        return false;
    }

    private boolean check = false;

    @Override
    public void onLongPress(MotionEvent e) {
        if(!check) {
            activity.copy();
            check = true;
        }
        else {
            try {
                activity.paste();
                check = false;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
