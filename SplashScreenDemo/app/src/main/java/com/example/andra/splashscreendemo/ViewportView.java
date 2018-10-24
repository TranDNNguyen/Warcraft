//https://stackoverflow.com/questions/12169905/zoom-and-panning-imageview-android
//Panning and Zooming Imageview


package com.example.andra.splashscreendemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/*
<Viewport> in Week3
This is Custom View using ImageView.
Use this view in layout.xml instead of ImageView attribute.
 example) ->
   <com.android.ecs160.viewport_sample.ViewportView
        android:id="@+id/map"  ... >
- built in "onTouchEventHandler" with "motionEvent"  = Panning
-   as well as "ScaleGestureDetector" = Zooming
<CustomView - Functions>
constructors
onTouchEvent(~) = InputHandler
onDraw(~) = Refresh Image
<ScaleListener - Function>
onScale() - handling scale factor, on ScaleGestureDetector event-handler
//ACTION_DOWN = Pressed Gesture has started
//ACTION_UP = A pressed gesture has finished
//ACTION_POINTER_DOWN = A non-primary pointer has gone down.
//ACTION_POINTER_UP = A non-primary pointer has gone up.
//ACTION_MOVE = A change has happened during a press gesture (between ACTION_DOWN and ACTION_UP).
 */


@SuppressLint("AppCompatCustomView")
public class ViewportView extends ImageView {

    private static final int INVALID_POINTER_ID = -1;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private float mLastGestureX;
    private float mLastGestureY;
    private int mActivePointerId = INVALID_POINTER_ID;
    private MediaPlayer mp3 = new MediaPlayer();    //Create MediaPlayer

    private ScaleGestureDetector mScaleDetector;

    private float mScaleFactor = 1.f;

    public ViewportView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public ViewportView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                if (!mScaleDetector.isInProgress()) {
                    final float x = ev.getX();
                    final float y = ev.getY();

                    mLastTouchX = x;
                    mLastTouchY = y;

                    mActivePointerId = ev.getPointerId(0);

                    mp3 = MediaPlayer.create(this.getContext(),R.raw.building_explode1);
                    mp3.start();          //Start playing
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                if (!mScaleDetector.isInProgress()) {
                    final float gx = mScaleDetector.getFocusX();
                    final float gy = mScaleDetector.getFocusY();

                    mLastGestureX = gx;
                    mLastGestureY = gy;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (!mScaleDetector.isInProgress()) {
                    Log.i("hi", "SD not in progress");
                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    final float x = ev.getX(pointerIndex);
                    final float y = ev.getY(pointerIndex);

                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;

                    invalidate();

                    mLastTouchX = x;
                    mLastTouchY = y;
                } else {
                    Log.i("hi", "SD in progress");
                    final float gx = mScaleDetector.getFocusX();
                    final float gy = mScaleDetector.getFocusY();

                    final float gdx = gx - mLastGestureX;
                    final float gdy = gy - mLastGestureY;

                    mPosX += gdx;
                    mPosY += gdy;

                    // SOMETHING NEEDS TO HAPPEN RIGHT HERE.

                    invalidate();

                    mLastGestureX = gx;
                    mLastGestureY = gy;
                }

                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;

                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);

                    mActivePointerId = ev.getPointerId(newPointerIndex);
                } else {
                    final int tempPointerIndex = ev.findPointerIndex(mActivePointerId);

                    mLastTouchX = ev.getX(tempPointerIndex);
                    mLastTouchY = ev.getY(tempPointerIndex);
                }

                break;
            }
        }

        return true;
    }
    @Override
    public void onDraw(Canvas canvas) {

        canvas.save();

        canvas.translate(mPosX, mPosY);  // Detail : https://stackoverflow.com/questions/5789813/what-does-canvas-translate-do

        if (mScaleDetector.isInProgress()) {
            canvas.scale(mScaleFactor, mScaleFactor, mScaleDetector.getFocusX(), mScaleDetector.getFocusY());
        }
        else{
            canvas.scale(mScaleFactor, mScaleFactor, mLastGestureX, mLastGestureY);
        }
        super.onDraw(canvas);
        canvas.restore();

        /*
            Why Canvas.save() ~ and ~ Canvas.restore()?  -> https://stackoverflow.com/questions/29040064/save-canvas-then-restore-why-is-that
            In short, This function keep using one Canvas, regardless of panning and zooming.
            It just update the left top position of the canvas for panning,
            and use canvas.scale() for zooming.
            after modification, use super.onDraw(~) to display the current canvas.
            after that, just bring back original canvas.
         */

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            invalidate();
            return true;
        }
    }
}