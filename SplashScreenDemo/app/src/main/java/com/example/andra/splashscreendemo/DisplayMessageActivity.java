package com.example.andra.splashscreendemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {
    ImageView img;
    TextView txt;
    MyScaleGestures myScaleGestures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        img = (ImageView) findViewById(R.id.img);
        txt = (TextView) findViewById(R.id.txt);

        txt.setOnTouchListener(new MyScaleGestures(txt.getContext()));
        img.setOnTouchListener(new MyScaleGestures(txt.getContext()));
    }


    //ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getApplicationContext(), scaleGestureListener);
}
