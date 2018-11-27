package com.android.ecs160.warcraft;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Vector;

//public class AssetActivity extends AppCompatActivity {
public class AssetRenderer {
    public Vector<Asset> assets;
    Asset selectedAsset;
    int TileSize = MainActivity_viewport.getTileSize();
    Asset lastSelectedAsset;
    AssetLoader assetLoader;
    private Context mContext;
    public int tilePixelSize = 32;

    //TODO: figure out better way to set size of asset bitmap
    int screenWidth;// = 1000;//Resources.getSystem().getDisplayMetrics().widthPixels;
    int screenHeight;// = 600;//Resources.getSystem().getDisplayMetrics().heightPixels;

    //@Override
    //protected void onCreate(Bundle savedInstanceState) {
    //super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_asset);

    public AssetRenderer(Context context, Resources res, int width, int height) {

        screenWidth = width;
        screenHeight = height;

        //get initial assets from loader
        try {
            //AssetLoader.setBitmaps(getResources());
            assetLoader = new AssetLoader();
            mContext = context;
            assets = assetLoader.assetParse("test.map", context, res);
            // selectedAsset = assets.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //ImageView image = (ImageView) findViewById(R.id.bitmapView);
        //Bitmap result = renderAssets();
        //image.setImageBitmap(result);
        //return result;
    }

    //Takes in tile cordinates and returns the asset at that location, if one exists
    public Asset getAsset(int x, int y) {
        for (Asset asset : assets) {
            if ((asset.x == x) && asset.y == y) {
                //if coordinates match return assets
                return asset;
            }
        }
        return null;
    }

    public void addAsset(Asset asset){
        assetLoader.setAssetBitmap(asset, 0);
        assets.add(asset);
    }

    /*
    //x y: map coordinate offset in pixel
    public void selectAssetOnLocation(int tileX, int tileY, int x, int y) {
        Asset selected = null;
        for (Asset asset : assets) {
            if (asset == null) {
                System.out.println("EMPTY ASSET\n");
                continue;
            }
            Log.i("assetRenderer", "assetType=" + asset.type + " (" + asset.x + "," + asset.y + ")");
            if (asset.x == tileX && asset.y == tileY) {
                asset.isSelected = true;//!asset.isSelected;
                selected = asset;
                Log.i("assetRenderer", "assetType=" + asset.type + " = " + asset.isSelected);

                break;
            }
        }
        if (selected != null) {
            renderAssets(x, y);
        }
    }
    */



    /*
     * Takes in pixel coordinates and returns asset at that location if there is one
     * assumes that the pixel coordinates are relative to the map, not the screen
     */
    //xPos: the absolute coordinates on the screen
    //currX: the coorinates where you've touched the screen within the view
    //values[]: will be location of xy coordinate of the view relative to the layout

    public Asset selectAsset(int xPos, int yPos, int values[], int currX, int currY) {
        //public Asset selectAsset(int x, int y) {

        //XY in Viewport
        int viewX = (xPos - values[0]);
        int viewY = (yPos - values[1]);
        if (viewX < 0) viewX = 0;
        if (viewY < 0) viewY = 0;

        //XY in Tile
        int tileX = (currX + viewX) / tilePixelSize;
        int tileY = (currY + viewY) / tilePixelSize;


        //tile Indicies = getTileIndex(x, y);
        lastSelectedAsset = selectedAsset;
        if (lastSelectedAsset != null) {
            lastSelectedAsset.isSelected = false;
        }
        selectedAsset = getAsset(tileX, tileY);

        //selection - drawing box
        //selectedAsset.isSelected = !selectedAsset.isSelected;

        if (selectedAsset != null) { //an asset was selected
            selectedAsset.isSelected = true;
            FragmentThree actionFragment = (FragmentThree) MainActivity_viewport.fragManager.findFragmentById(R.id.fragment3);
            actionFragment.updateButtonImages(selectedAsset);  //  New Asset UI Update Method - 181126 Joon from "newdesign" branch

        } else if (lastSelectedAsset != null) {  // Move Command - Finger Tap
            if (lastSelectedAsset.type == Asset.EAssetType.Peasant || lastSelectedAsset.type == Asset.EAssetType.Footman) {
                AssetActionRenderer.findCommand(lastSelectedAsset, tileX, tileY);
                //lastSelectedAsset.findCommand(tileX, tileY);
                updateAssetFrame(lastSelectedAsset); //, tileX, tileY);
                lastSelectedAsset.isSelected = false;


                if(lastSelectedAsset.type == Asset.EAssetType.Peasant){


                    //NOTE: UIFrag
                    //Fragment, changing image, based on the selection,
                    //TODO: 1. It is currently changing images after selecting units, so we may have to modify the onTouchListener in MainActivity
                    //TODO: 2. Need to Implement all features like setting appropriate image upon selecting specific unit, and building.
                    //TODO: 3. Need to work on deselection, change image to transparent, or empty image.s
//                    FragmentThree fragment = (FragmentThree) MainActivity_viewport.fragManager.findFragmentById(R.id.fragment3);
//                    fragment.peasantSelected();
                }
                else if(lastSelectedAsset.type == Asset.EAssetType.Footman){

                }
                FragmentThree actionFragment = (FragmentThree) MainActivity_viewport.fragManager.findFragmentById(R.id.fragment3);
                actionFragment.resetUIButtonImages();;
                actionFragment = null;

            }//TODO
        }

        return selectedAsset;
    }

    /*
     * Takes in pixel coordinates and returns the coresponding tile index

    public int[] getTileIndex(int x, int y) {

        return null;
    }
    */


    /*
     * Creates bitmap of current assets
     * //TODO needs to be passed widths, heights, offsets from viewport/mapactivity instead of declaring them here
     */
    public Bitmap renderAssets(int widthOffset, int heightOffset) {

        //TODO: set/get offset that corresponds with what parts of the map are visible
        //int widthOffset = 0;
        //int heightOffset = 0;

        Bitmap result = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        for (Asset asset : assets) {
            if (asset == null) {
                System.out.println("EMPTY ASSET\n");
                continue;
            }else if(asset.visible == false){
                continue;
            }

            //TODO should this be in drawAsset()??
            int x = asset.x;
            int y = asset.y;
            int w = TileSize; //asset.assetWidth;
            int h = TileSize; //asset.assetHeight;

            //TODO fix this to make it less sphagetti
            if ((x * w - widthOffset) - 3 * w > screenWidth || (y * h - heightOffset) - 3 * w > screenHeight
                    || (x * w) + 3 * w < widthOffset || (y * h) + 3 * w < heightOffset) {
                continue;
            }//image won't fit on bitmap, so it is out of view

            asset.drawAsset(canvas, widthOffset, heightOffset);
            asset.drawAssetSelection(canvas, widthOffset, heightOffset);
        }

        return result;
    }


    public void generateMiniMap(Bitmap minimap){

        int sampleMultiplier = 5; // Map Scaling Factor

        Canvas canvas = new Canvas(minimap);
        Bitmap temp;
        int radius = 4, x, y;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        for (Asset asset : assets) {
            if (asset == null) {
                System.out.println("EMPTY ASSET\n");
                continue;
            }
            //TODO: Add change different color for different player
            //  For example, Goldmine, neutral, different color.
            if(asset.type == Asset.EAssetType.GoldMine)
                paint.setColor(Color.WHITE);
            else if(asset.type == Asset.EAssetType.Archer ||
                    asset.type == Asset.EAssetType.Footman ||
                    asset.type == Asset.EAssetType.Peasant ||
                    asset.type == Asset.EAssetType.Ranger)
                paint.setColor(Color.RED);
            else
                paint.setColor(Color.WHITE);
            //TODO should this be in drawAsset()??
            x = asset.x * sampleMultiplier;
            y = asset.y * sampleMultiplier;

            canvas.drawCircle(x, y, radius, paint);

        }
        canvas = null;
    }

    /*
     * figures out what frame is needed for the asset, and requests it from loader.
     */
    public void updateAssetFrame(Asset asset) {//}, int newX, int newY){
        int currentDir = 0;
        int frameIndex = 0;
        //int direction[] = {0, 5, 10, 15, 20, 25, 30, 35};
        //calcDirection(asset, newX, newY);

        if(asset.isBuilding()){
            if(asset.type == Asset.EAssetType.GoldMine){
                frameIndex = 0;
            }//TODO: consider whether peasants are inside
            else if(asset.HP <= asset.assetData.hitPoints / 2){
                frameIndex = 1;
            }//TODO: difference between frame when being built/being destroyed?
            else if(asset.HP > asset.assetData.hitPoints / 2){
                frameIndex = 2;
            }else {
                frameIndex = 0;
            }
        }//TODO - add active/inactive states for buildings
        else if(asset.type == Asset.EAssetType.Peasant){
            Asset.EAssetAction action = asset.commands.peek();
            if(action == Asset.EAssetAction.Walk){
                frameIndex = asset.direction.getIdx() * 5;
                if(asset.lumber > 0){
                    frameIndex += 120;
                }
                frameIndex += (asset.steps % 5);
            }else if(action == Asset.EAssetAction.HarvestLumber){
                frameIndex = 40 + asset.direction.getIdx() * 5;
                frameIndex += (asset.steps % 5);
            }//TODO: fix magic numbers?
            //TODO: consider action type, switch statement?
        }//peasant
        else if(asset.type == Asset.EAssetType.Footman || asset.type == Asset.EAssetType.Archer){
            frameIndex = asset.direction.getIdx() * 5;
            frameIndex += (asset.steps % 5);
        }//footman or archer

        assetLoader.setAssetBitmap(asset, frameIndex);
    }

    //takes in asset and it's new coordinates
    /*
    public void calcDirection(Asset asset, int destX, int destY) {
        int assetX = asset.x;
        int assetY = asset.y;
        //int currentDir = 0; // = asset.direction;
        Asset.EDirection currentDir = Asset.EDirection.Max;
        int deltaX, deltaY;
        boolean right = false;
        boolean left = false;
        boolean up = false;
        boolean down = false;
        asset.x2 = destX;
        asset.y2 = destY;

        deltaX = destX - assetX;
        deltaY = assetY - destY;
        //calc left or right
        if(deltaX < 0) {
            left = true;
        }
        else if(deltaX > 0) {
            right = true;
        }
        //calc up or down
        if (deltaY < 0) {
            down = true;
        }
        else if(deltaY > 0) {
            up = true;
        }
        //change global direction variable accordingly
        if(right == true) {
            if (up == true){
                currentDir = Asset.EDirection.NorthEast;
            }
            else if (down = true) {
                currentDir = Asset.EDirection.SouthEast;
            }
            else {
                currentDir = Asset.EDirection.East;
            }
        }
        else if(left == true) {
            if(up == true){
                currentDir = Asset.EDirection.NorthWest;
            }
            else if(down = true) {
                currentDir = Asset.EDirection.SouthWest;
            }
            else {
                currentDir = Asset.EDirection.West;
            }
        }
        else if (up == true){
            currentDir = Asset.EDirection.North;
        }
        else if (down = true) {
            currentDir = Asset.EDirection.South;
        }

        asset.direction = currentDir;
    }
    */


}
