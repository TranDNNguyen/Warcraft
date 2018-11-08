package com.android.ecs160.warcraft;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.io.IOException;
import java.util.Vector;

//public class AssetActivity extends AppCompatActivity {
public class AssetRenderer {
    public Vector<Asset> assets;
    Asset selectedAsset;
    int TileSize = MainActivity_viewport.getTileSize();
    Asset lastSelectedAsset;
    AssetLoader assetLoader;

    public int tilePixelSize = 32;

    //@Override
    //protected void onCreate(Bundle savedInstanceState) {
    //super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_asset);


    public AssetRenderer(Context context, Resources res) {
        //get initial assets from loader
        try {
            //AssetLoader.setBitmaps(getResources());
            assetLoader = new AssetLoader();
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

        //tileIndicies = getTileIndex(x, y);
        lastSelectedAsset = selectedAsset;
        if (lastSelectedAsset != null) {
            lastSelectedAsset.isSelected = false;
        }
        selectedAsset = getAsset(tileX, tileY);

        //selection - drawing box
        //selectedAsset.isSelected = !selectedAsset.isSelected;

        if (selectedAsset != null) { //an asset was selected
            selectedAsset.isSelected = true;
        } else if (lastSelectedAsset != null) {
            if (lastSelectedAsset.type == Asset.EAssetType.Peasant || lastSelectedAsset.type == Asset.EAssetType.Footman) {
                lastSelectedAsset.setAction(Asset.EAssetAction.Walk, tileX, tileY);
                updateAssetFrame(lastSelectedAsset); //, tileX, tileY);
                lastSelectedAsset.isSelected = false;

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
        //TODO: figure out better way to set size of asset bitmap
        int screenWidth = 1000;//Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = 600;//Resources.getSystem().getDisplayMetrics().heightPixels;

        //TODO: set/get offset that corresponds with what parts of the map are visible
        //int widthOffset = 0;
        //int heightOffset = 0;

        Bitmap result = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        for (Asset asset : assets) {
            if (asset == null) {
                System.out.println("EMPTY ASSET\n");
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
            frameIndex = 0;
        }//TODO - add active/inactive states for buildings
        else if(asset.type == Asset.EAssetType.Peasant){
            frameIndex = asset.direction.getIdx() * 5; //just for walking + direction
            //TODO: consider action type
        }//peasant
        else if(asset.type == Asset.EAssetType.Footman || asset.type == Asset.EAssetType.Archer){
            frameIndex = asset.direction.getIdx() * 5;
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