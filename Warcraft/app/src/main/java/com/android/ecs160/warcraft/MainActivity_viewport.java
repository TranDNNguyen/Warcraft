package com.android.ecs160.warcraft;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity_viewport extends AppCompatActivity {

    static int TILE_SIZE = 32;
    static int viewportWidth, viewportHeight;
    static int screenWidth, screenHeight, screenLeftBound, screenRightBound;
    static int ConstLayoutWidth = 1000, ConstLayoutHeight = 600;

    static float screenZoomFactor = 1.0f, ConstScreenZoomFactor = 0.6f;
    static FragmentManager fragManager;
    int updateFrequency = 50;

    int dx;
    int dy;

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

        setContentView(R.layout.activity_main_viewport);

        //Hiding SystemUI + Implementing UI changeListener~   src:https://stackoverflow.com/questions/32214258/how-to-hide-status-and-navigation-bars-after-first-touch-event
        hideUI();
        View decorView = this.getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // TODO: The system bars are visible. Make any desired
                    Message msg = uiHandler.obtainMessage(); //Implement your hide functionality accordingly
                    uiHandler.sendMessageDelayed(msg, 3000);
                } else {
                    // TODO: The system bars are NOT visible. Make any desired
                }
            }
        });

        new GameModel(updateFrequency);

        //Views - viewport and TextSection
        viewport = (ImageView) findViewById(R.id.viewportView);
        minimap = (ImageView) findViewById(R.id.minimapView);
        resultTV = (TextView) findViewById(R.id.xyTextView);

        //adjust map to size of screen

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int totalWidth = displaymetrics.widthPixels;
        int totalHeight = displaymetrics.heightPixels;
        double viewportWidthScale = 1;
        double viewportHeightScale = .9;
        viewportWidth = (int)(viewportWidthScale*totalWidth);
        viewportHeight = (int)(viewportHeightScale*totalHeight);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(viewportWidth, viewportHeight);
        viewport.setLayoutParams(parms);

        //viewportWidth = viewport.getLayoutParams().width;//.getMeasuredWidth();
        //viewportHeight = viewport.getLayoutParams().height;//.getMeasuredHeight();

        //Map Renderer

        mapRenderer = new MapRenderer(this, ConstLayoutWidth, ConstLayoutHeight);
        assetRenderer = new AssetRenderer(this, getResources(), ConstLayoutWidth, ConstLayoutHeight);
        assetActionRenderer = new AssetActionRenderer(assetRenderer, mapRenderer);

        //Initializations
        InitScreenSetup();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //updateViewport();
                viewportHandler.obtainMessage(1).sendToTarget();
                assetActionRenderer.TimeStep(assetRenderer.assets);
            }
        }, 0, updateFrequency);

        dx = dy = 0;

        //UI Fragment Setting
        fragManager = getSupportFragmentManager();

        //updateViewport();
        viewportHandler.obtainMessage(1).sendToTarget();
        viewport.setOnTouchListener(touchListener);

    }




    public Handler uiHandler = new Handler() {

        //TODO
        //REQUIRED
        //private boolean updateViewport(Message msg) {
        public void handleMessage(Message msg) {
            hideUI();
            // return true;
        }
    };

    public Handler viewportHandler = new Handler() {

        //TODO
        //REQUIRED
        //private boolean updateViewport(Message msg) {
        public void handleMessage(Message msg) {
            Bitmap result = Bitmap.createBitmap(ConstLayoutWidth, ConstLayoutHeight, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(result);
            if(!(currX > MapRenderer.bitmapWidth - ConstLayoutWidth
                    || currY > MapRenderer.bitmapHeight - ConstLayoutHeight
                    || currX < 0 || currY < 0)){//boundary check
                Bitmap temp = mapRenderer.drawTerrain(currX, currY);
                if (temp != null) {
                    canvas.drawBitmap(temp, 0, 0, null);  //  Draw Map
                    canvas.drawBitmap(assetRenderer.renderAssets(currX, currY), 0, 0, null);  //  Draw Assets
                    viewport.setImageBitmap(result);
                    result = null;  // Performance

                    //Minimap Part
                    Bitmap minimapTemp;
                    minimapTemp = Bitmap.createBitmap(mapRenderer.drawMinimap());
                    assetRenderer.generateMiniMap(minimapTemp);

                    minimap.setImageBitmap(minimapTemp);
                    minimapTemp = null; // for Garbage Collection

                }
            }
            // return true;
        }
    };



    //Set Screen Boundary wrt Zooming Factor things
    public void InitScreenSetup(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth  = size.x;
        screenHeight = size.y;

        //TODO current ScreenRatio is 10:6, in rare case, it is possible, some phone with larger width might have problems due to this part.
        viewportHeight = screenHeight;
        viewportWidth  = screenHeight * 10 / 6;

        screenLeftBound = (screenWidth - viewportWidth) / 2; // == empty margin on left side
        screenRightBound = screenWidth + screenLeftBound;  // == starting point of right empty margin

        screenZoomFactor = ((float)screenHeight) / 600f;  // ConstScreenZoomFactor = 0.6 = 1000/600  //  We keep the ratio of the screen
    }


    //DEBUG  //  Displaying all the coordinates
    // - X,Y absolute coord, ImageView X,Y, Map X, Y
    //xPos: the absolute coordinates on the screen
    //currX: the coorinates where you've touched the screen within the view
    //values[]: will be location of xy coordinate of the view relative to the layout
    public void displayCoordinates(double xPos, double yPos, int values[], int currX, int currY) {
        resultString = " Viewport = (" + (int)(xPos - values[0]) + "," + (int)(yPos - values[1]);
        resultString += ") / Map<top&left corner> = (" + (currX) + "," + (currY);

        //XY in Viewport
        double viewX = (xPos - values[0]);
        double viewY = (yPos - values[1]);
        if (viewX < 0) viewX = 0;
        if (viewY < 0) viewY = 0;

        //XY in Tile
        int tileX = (int)(viewX) / TILE_SIZE;
        int tileY = (int)(viewY) / TILE_SIZE;

        resultString += ") / Tile = (" + tileX + "," + tileY + ")";
        //if(map1.checkBoundary(xPos-values[0], yPos-values[1]))
        resultTV.setText(resultString);
    }


    // NOW
    // - Moving one or two fingers
    // - if asset is selected, put selection box around it.
    private static final int INVALID_POINTER_ID = -1;

    private float mLastTouchX, mLastTouchY, mFirstTouchX, mFirstTouchY;
    private float mLastGestureX, mLastGestureY;
    private int mActivePointerId = INVALID_POINTER_ID;


    private int currX = 0, currY = 0;  // Current LeftTop point
    private int selectionType = 0;  //  0 for None, 1 for AssetSelection(SingleTouch), 2 for Panning Map(Multitouch), 3 for Show/Hide minimap


    ImageView.OnTouchListener touchListener = new ImageView.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent ev) {

            final int action = ev.getAction();

            float x = ev.getX(0) - screenLeftBound;
            float y = ev.getY(0);
            int xPos = (int) (x / screenZoomFactor );
            int yPos = (int) (y / screenZoomFactor );

            // Get location of View in the screen.
            int values[] = new int[2];
            v.getLocationOnScreen(values);

            //TESTING FUNCTION // DEBUGGING
            //NOTE: Displaying all the coordinates
            // - X,Y absolute coord, ImageView X,Y, Map X, Y
            //displayCoordinates(xPos, yPos, values, currX, currY);


            //NOTE: disable multitouch with 3+ fingers
            //if(ev.getPointerCount() > 2) return true;
            if(xPos < 0 || yPos < 0) return true;

            switch (action & MotionEvent.ACTION_MASK) {


                //Releasing == Literally Final Decision Part
                case MotionEvent.ACTION_UP: {
                    mLastTouchX = x;
                    mLastTouchY = y;

                    //NOTE: Save the Location of First-Finger (PointerID is used to distinguish the touchInputs for each finger, - 0 for 1st finger.)
                    mActivePointerId = ev.getPointerId(0);  //  First Finger

                    //IF no multitouch used at all. -> select Asset.
                    if(selectionType == 1) {
                        Asset selectedAsset = assetRenderer.selectAsset(xPos, yPos, values, currX, currY);
                        //TODO - do something with the asset
                    }

                    // 3 Finger-tap -> Show/Hide Minimap
                    else if(selectionType == 3){
                        minimap.setVisibility(minimap.getVisibility()==View.INVISIBLE ? View.VISIBLE : View.INVISIBLE );
                    }

                    //Update
                    viewportHandler.obtainMessage(1).sendToTarget();

                    break;
                }

                //Pressing - First Finger
                case MotionEvent.ACTION_DOWN: {
                    mFirstTouchX = x;
                    mFirstTouchY = y;
                    mLastTouchX = x;
                    mLastTouchY = y;
                    mActivePointerId = ev.getPointerId(0);  //  First Finger
                    selectionType = 1; // AssetSelection Available

                    break;
                }

                //Multitouch Event Handling part
                case MotionEvent.ACTION_POINTER_DOWN: {

                    //Show/Hide minimap w/ 3 finger-tap
                    if(ev.getPointerCount() >= 3){
                        selectionType = 3;
                        //Toast.makeText(getApplicationContext(), "MultiTouch " + ev.getPointerCount(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    //  Panning Screen w/ 2 fingers
                    else if(ev.getPointerCount() == 2)
                        selectionType = 2;
                    break;
                }


                //General MOVE event
                case MotionEvent.ACTION_MOVE: {
                    //NOTE
                    //  Get the location of first finger that touched.
                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);

                    //3FingerTap - Show/Hide display
                    if(selectionType == 3) return true;
                    if(ev.getPointerCount() >= 3){
                        selectionType = 3;
                        return true;
                    }

                    //Drag - MultipleSelection
                    if(selectionType == 1){
                        mLastTouchX = x;
                        mLastGestureY = y;
                        return true;
                    }

                    //Setting up TwoFinger Drag
                    if(selectionType == 2) {
                        dx = (int) x - (int) mLastTouchX;
                        dy = (int) y - (int) mLastTouchY;
/*
                        if (viewportWidth - (mPosX + dx) >= 0)//&& mPosX+dx < viewportWidth)
                            mPosX += dx;
                        if (viewportHeight - (mPosY + dy) >= 0)//&& mPosY+dy < viewportHeight)
                            mPosY += dy;
*/

                        //TODO
                        //Boundary Check - Put it in the MapRenderer
                        if (currX - dx >= 0 && currX - dx < MapRenderer.bitmapWidth - ConstLayoutWidth) {
                            currX -= dx;
                        }
                        if (currY - dy >= 0 && currY - dy < MapRenderer.bitmapHeight - ConstLayoutHeight) {
                            currY -= dy;
                        }

                        viewportHandler.obtainMessage(1).sendToTarget();

                        mLastTouchX = x;
                        mLastTouchY = y;
                    }


                    break;
                }

                //Multitouch - One finger lifted
                //NOTE: Save the last location, update the mActivePointerID (if first finger had been lifted, set mActivePointerID to another finger ID, so that on touch listener can still go on without crash.).
                case MotionEvent.ACTION_POINTER_UP: {

                    final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerId = ev.getPointerId(pointerIndex);

                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = ev.getPointerId(newPointerIndex);
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
