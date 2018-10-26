package com.example.andra.splashscreendemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.util.Vector;

public class AssetActivity extends AppCompatActivity {
    Vector<Asset> assets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        //get initial assets from loader
        try {
            //AssetLoader.setBitmaps(getResources());
            assets = AssetLoader.assetParse("test.map", this, getResources());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ImageView image = (ImageView) findViewById(R.id.bitmapView);
        Bitmap result = renderAssets();
        image.setImageBitmap(result);
        //return result;
    }


    /*
     * Creates bitmap of current assets
     */
    public Bitmap renderAssets() {
        //TODO: figure out better way to set size of asset bitmap
        int screenWidth = 2000;//Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = 2000;//Resources.getSystem().getDisplayMetrics().heightPixels;

        //TODO: set/get offset that corresponds with what parts of the map are visible
        int widthOffset = 0;
        int heightOffset = 0;

        Bitmap result = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        for(Asset asset : assets){
            if(asset == null){
                System.out.println("EMPTY ASSET\n");
                continue;
            }

            //TODO should this be in drawAsset()??
            int x = asset.x;
            int y = asset.y;
            int w = asset.assetWidth;
            int h = asset.assetHeight;

            if(x*w - widthOffset > screenWidth || y*h - heightOffset > screenHeight
                    || x*w < widthOffset || y*h < heightOffset){
                continue;
            }//image won't fit on bitmap, so it is out of view

            asset.drawAsset(canvas, widthOffset, heightOffset);
        }

        return result;
    }


}
