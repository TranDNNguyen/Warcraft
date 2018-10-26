package com.example.andra.splashscreendemo;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

import java.util.Vector;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MapActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;


    private MapTiles mapTiles;
    private Bitmap[] tileSet;
    private MediaPlayer m = new MediaPlayer();

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        //GridView gridview = (GridView) findViewById(R.id.gridview);
        //gridview.setAdapter(new ImageAdapter(this));

        // read in all the terrain tiles from terrain.png
        Bitmap terrain = BitmapFactory.decodeResource(getResources(), R.drawable.terrain);

        /*
         * Sample code to add bitmap to imageview
         *
         * ImageView imageView = findViewById(R.id.terrain);
         * imageView.setImageBitmap(terrain);
         */

        int terrainHeight = terrain.getHeight();
        int terrainWidth = terrain.getWidth();

        tileSet = new Bitmap[293];
        for(int i = 0; i < 293; i++){
            tileSet[i] = Bitmap.createBitmap(terrain, 0,terrainHeight/293*i,terrainWidth,terrainWidth);
        }

        mapTiles = new MapTiles("test.map", this);

        Bitmap currentViewPort = generateViewPort(0,0, 64, 64);
        ViewportView imageView = (ViewportView) findViewById(R.id.map);
        float factor = (float) currentViewPort.getWidth() / currentViewPort.getHeight();
        Bitmap scaled = Bitmap.createScaledBitmap(currentViewPort, (int)(2000*factor), 2000, true);
        Bitmap test = tileSet[20];

        imageView.setImageBitmap(scaled);


        /*
        for(int i = 0; i < mapTiles.getMapHeight(); i++) {
            for (int j = 0; j < mapTiles.getMapWidth(); j++) {
                System.out.print(mapTiles.idxMap.get(i).get(j) + " ");
            }
            System.out.print("\n");
        }
        */

        playSound();
        /*
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

            }
        });
        */
    }

    protected void playSound()
    {
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
            } // delete any MediaPlayer objects if open and create new

            AssetFileDescriptor descriptor = getAssets().openFd("game1.mp3");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            m.prepare();
            m.setLooping(true);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void onPause() {
        super.onPause();
        m.stop();
    }

    protected void onResume(){
        super.onResume();
        try {
            m.prepare();
        } catch(Exception e){}
        m.setLooping(true);
        m.start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
            delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    //x & y coordinates of upper left and lower right corners
    public Bitmap generateViewPort(int startX, int startY, int endX, int endY){
        int viewPortHeight = endY-startY, viewPortWidth = endX - startX;
        Bitmap temp = Bitmap.createBitmap(viewPortWidth*32, viewPortHeight*32, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        int top = 0;
        for(int i = startY; i < endY; i++) {
            int left = 0;
            for (int j = startX; j < endX; j++) {
                //canvas.drawBitmap(tileSet[mapTiles.idxMap.get(i).get(j)], top, left, null);
                canvas.drawBitmap(tileSet[mapTiles.idxMap.get(i).get(j)], left, top, null); //XXX
                left += 32;
            }
            top += 32;
        }

        //Add assets on top of the map
        AssetRenderer assetRenderer = new AssetRenderer(this, getResources());
        canvas.drawBitmap( assetRenderer.renderAssets(), 0, 0, null);
        return temp;
    }
}

class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] mThumbIds = {};

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Bitmap terrain = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.terrain);
        Bitmap tile = Bitmap.createBitmap(terrain, 0,0,100,100);
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(tile);
        return imageView;
    }

}
