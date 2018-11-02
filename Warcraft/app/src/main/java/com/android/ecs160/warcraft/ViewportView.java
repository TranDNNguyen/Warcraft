//https://stackoverflow.com/questions/12169905/zoom-and-panning-imageview-android
//Panning and Zooming Imageview


package com.android.ecs160.warcraft;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

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

    int GLOBAL_TOUCH_POSITION_X = 0;
    int GLOBAL_TOUCH_CURRENT_POSITION_X = 0;




    String actionString = "Touch Location (" + 10 + "," + 10 + ")";
    TextView resultTV;
    private int viewportWidth =800;
    private int viewportHeight=600;



    private static final int INVALID_POINTER_ID = -1;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private float mLastGestureX;
    private float mLastGestureY;
    private int mActivePointerId = INVALID_POINTER_ID;

    public ViewportView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ViewportView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public void setResultTextView(TextView tv){
        resultTV = tv;
    }



    /*
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        //Toast.makeText(this.getContext(), "This is my Toast message!", Toast.LENGTH_LONG).show();

        //String actionString = "Touch Location (" + 10 + "," + 10 + ")";
        //TextView resultTV = (TextView) findViewById(R.id.xyTextView);
        //resultTV.setText(actionString);

        final int action = ev.getAction();

        int xPos = (int)ev.getX();
        int yPos = (int)ev.getY();

        Log.i("viewport", "X & Y = " + xPos + "," + yPos);


        switch (action & MotionEvent.ACTION_MASK) {


            case MotionEvent.ACTION_UP:{
                final float x = ev.getX();
                final float y = ev.getY();

                //Toast.makeText(this.getContext(), "Touch Location (" + x + "," + y + ")" + "\n Last Touch Location (" + mPosX + "," + mPosY + ")", Toast.LENGTH_SHORT).show();

                break;

            }



            case MotionEvent.ACTION_DOWN: {

                final float x = ev.getX();
                final float y = ev.getY();

                mLastTouchX = x;
                mLastTouchY = y;


                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                final float gx = ev.getX();
                final float gy = ev.getY();


                mLastGestureX = gx;
                mLastGestureY = gy;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
               final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final int x = (int) ev.getX(pointerIndex);
                final int y = (int) ev.getY(pointerIndex);

                final int dx = x - (int) mLastTouchX;
                final int dy = y - (int) mLastTouchY;


                if(800-(mPosX+dx) >= 0 )//&& mPosX+dx < viewportWidth)
                    mPosX += dx;
                if(600-(mPosY+dy) >= 0 )//&& mPosY+dy < viewportHeight)
                    mPosY += dy;


                resultTV.setText("Move X & Y = " + mPosX + "," + mPosY);
                Log.i("viewport", "Move X & Y = " + mPosX + "," + mPosY);
                invalidate();

                mLastTouchX = x;
                mLastTouchY = y;

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

    */
    @Override
    public void onDraw(Canvas canvas) {

      //  canvas.save();

      //  canvas.translate(mPosX, mPosY);  // Detail : https://stackoverflow.com/questions/5789813/what-does-canvas-translate-do


        super.onDraw(canvas);
     //   canvas.restore();

        /*

            Why Canvas.save() ~ and ~ Canvas.restore()?  -> https://stackoverflow.com/questions/29040064/save-canvas-then-restore-why-is-that

            In short, This function keep using one Canvas, regardless of panning and zooming.
            It just update the left top position of the canvas for panning,
            and use canvas.scale() for zooming.
            after modification, use super.onDraw(~) to display the current canvas.

            after that, just bring back original canvas.
         */

    }

}


