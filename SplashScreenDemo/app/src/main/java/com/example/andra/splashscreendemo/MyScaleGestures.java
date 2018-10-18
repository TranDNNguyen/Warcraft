package com.example.andra.splashscreendemo;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;

public class MyScaleGestures implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {
    private View view;
    private ScaleGestureDetector gestureScale;
    private float scaleFactor = 1;
    private boolean inScale;

    public MyScaleGestures(Context c) {
        gestureScale = new ScaleGestureDetector(c, this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.view = v;
        gestureScale.onTouchEvent(event);
        v.performClick();//XXX
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        //((TextView)view).setText("You are scaling");
        scaleFactor *= detector.getScaleFactor();
        //scaleFactor = (scaleFactor < 1 ? 1 : scaleFactor); // prevent our view from becoming too small //
        //scaleFactor = ((float)((int)(scaleFactor * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //
        scaleFactor = ((float) ((int) (scaleFactor * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        inScale = true;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        inScale = false;
    }
}
