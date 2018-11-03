package com.android.ecs160.warcraft;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity_viewport extends AppCompatActivity {

    static int TILE_SIZE = 32;
    static int viewportWidth = 1000;
    static int viewportHeight = 600;

    ImageView viewport, minimap;
    MapRenderer mapRenderer; //,map1; // for Pokemon Map
    AssetRenderer assetRenderer;
    AssetActionRenderer assetActionRenderer;

    TextView resultTV;
    String resultString;

    public static int getTileSize() {
        return TILE_SIZE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideUI();
        setContentView(R.layout.activity_main_viewport);

        //Views - viewport and TextSection
        viewport = (ImageView) findViewById(R.id.viewportView);
        minimap = (ImageView) findViewById(R.id.minimapView);
        resultTV = (TextView) findViewById(R.id.xyTextView);

        //Map Renderer
        mapRenderer = new MapRenderer(this, 1000, 600);
        assetRenderer = new AssetRenderer(this, getResources());
        assetActionRenderer = new AssetActionRenderer(assetRenderer);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //updateViewport();

                assetActionRenderer.TimeStep(assetRenderer.assets);
            }
        }, 0, 100);

        updateViewport();
        viewport.setOnTouchListener(touchListener);
    }

    //TODO - implement runnable to make it as a thread.
    //REQUIRED
    private boolean updateViewport() {

        Bitmap result = Bitmap.createBitmap(1000, 600, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(mapRenderer.drawTerrain(currX, currY), 0, 0, null);  //  Draw Map
        canvas.drawBitmap(assetRenderer.renderAssets(currX, currY), 0, 0, null);  //  Draw Assets
        viewport.setImageBitmap(result);
        minimap.setImageBitmap(mapRenderer.drawMinimap());

        return true;
    }

    //DEBUG  //  Displaying all the coordinates
    // - X,Y absolute coord, ImageView X,Y, Map X, Y
    //xPos: the absolute coordinates on the screen
    //currX: the coorinates where you've touched the screen within the view
    //values[]: will be location of xy coordinate of the view relative to the layout
    public void displayCoordinates(int xPos, int yPos, int values[], int currX, int currY) {
        resultString = " Viewport = (" + (xPos - values[0]) + "," + (yPos - values[1]);
        resultString += ") / Map<top&left corner> = (" + (currX) + "," + (currY);

        //XY in Viewport
        int viewX = (xPos - values[0]);
        int viewY = (yPos - values[1]);
        if (viewX < 0) viewX = 0;
        if (viewY < 0) viewY = 0;

        //XY in Tile
        int tileX = (currX + viewX) / TILE_SIZE;
        int tileY = (currY + viewY) / TILE_SIZE;

        resultString += ") / Tile = (" + tileX + "," + tileY + ")";
        //if(map1.checkBoundary(xPos-values[0], yPos-values[1]))
        resultTV.setText(resultString);
    }


    //TODO - TouchEvent Listener
    //  need to clean up, and also need to add features
    //  - TwoFinger Drag for moving map
    //  - OneFinger event for selecting assets + etc.


    // NOW
    // - Moving one or two fingers
    // - if asset is selected, put sleection box around it.
    int GLOBAL_TOUCH_POSITION_X = 0;
    int GLOBAL_TOUCH_CURRENT_POSITION_X = 0;

    private static final int INVALID_POINTER_ID = -1;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private float mLastGestureX;
    private float mLastGestureY;

    private int mActivePointerId = INVALID_POINTER_ID;

    private int currX = 0, currY = 0;

    ImageView.OnTouchListener touchListener = new ImageView.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent ev) {

            //Toast.makeText(this.getContext(), "This is my Toast message!", Toast.LENGTH_LONG).show();

            //String actionString = "Touch Location (" + 10 + "," + 10 + ")";
            //TextView resultTV = (TextView) findViewById(R.id.xyTextView);
            //resultTV.setText(actionString);

            final int action = ev.getAction();

            int xPos = (int) ev.getX();
            int yPos = (int) ev.getY();

            int values[] = new int[2];
            v.getLocationOnScreen(values);

            //Displaying all the coordinates
            // - X,Y absolute coord, ImageView X,Y, Map X, Y
            displayCoordinates(xPos, yPos, values, currX, currY);

            switch (action & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_UP: {
                    final float x = ev.getX();
                    final float y = ev.getY();

                    Log.i("viewport", "mLastTouchY " + mLastTouchY);

                    break;
                }

                case MotionEvent.ACTION_DOWN: {

                    final float x = ev.getX();
                    final float y = ev.getY();

                    mLastTouchX = x;
                    mLastTouchY = y;

                    Log.i("viewport", "mLastTouchX " + mLastTouchX);
                    Log.i("viewport", "mLastTouchY " + mLastTouchY);

                    mActivePointerId = ev.getPointerId(0);

                    assetRenderer.selectAsset(xPos, yPos, values, currX, currY);
                    updateViewport();

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


                    if (800 - (mPosX + dx) >= 0)//&& mPosX+dx < viewportWidth)
                        mPosX += dx;
                    if (600 - (mPosY + dy) >= 0)//&& mPosY+dy < viewportHeight)
                        mPosY += dy;


                    //TODO
                    //Boundary Check - Put it in the MapRenderer
                    if (currX - dx >= 0) currX -= dx;
                    if (currY - dy >= 0) currY -= dy;


                    if (!updateViewport()) {

                        // if viewport update failed,
                        currX += dx;
                        currY += dy;
                    }

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
    };

    private void hideUI() {

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);

    }


}
