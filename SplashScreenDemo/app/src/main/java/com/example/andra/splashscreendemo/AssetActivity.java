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

        try {
            assets = AssetLoader.assetParse("test.map", this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ImageView image = (ImageView) findViewById(R.id.bitmapView);
        int screenWidth = 2000;//Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = 2000;//Resources.getSystem().getDisplayMetrics().heightPixels;
        int widthOffset = 0;
        int heightOffset = 0;

         //load the peasant frames into one large bitmap
        Bitmap peasantImages = BitmapFactory.decodeResource(getResources(), R.drawable.peasant);
        Bitmap goldMineImages = BitmapFactory.decodeResource(getResources(), R.drawable.gold_mine);
        Bitmap footmanImages = BitmapFactory.decodeResource(getResources(), R.drawable.footman);

        int peasantWidth = peasantImages.getWidth();
        int peasantHeight = peasantImages.getHeight()/172;//peasantWidth;
        int goldMineWidth = goldMineImages.getWidth();
        int goldMineHeight = goldMineImages.getHeight()/2;//goldMineWidth;
        int footmanWidth = footmanImages.getWidth();
        int footmanHeight = footmanImages.getHeight()/92;//footmanWidth;

        //grabbing the first frame from the peasant file
        Bitmap peasantBitmap =  Bitmap.createBitmap(peasantImages, 0,0, peasantWidth,peasantHeight);
        Bitmap footmanBitmap =  Bitmap.createBitmap(footmanImages, 0,0, footmanWidth,footmanHeight);
        Bitmap goldMineBitmap =  Bitmap.createBitmap(goldMineImages, 0,0, goldMineWidth,goldMineHeight);
        Bitmap result = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        for(Asset asset : assets){
            if(asset == null){
                System.out.println("EMPTY ASSET\n");
                continue;
            }
            int x = asset.x;
            int y = asset.y;
            if(x*32 - widthOffset > screenWidth || y*32 - heightOffset > screenHeight
                    || x*32 < widthOffset || y*32 < heightOffset){
                continue;
            }//image won't fit on bitmap, so it is out of view

            if(asset.type == Asset.EAssetType.Peasant) {
                canvas.drawBitmap(peasantBitmap, x*footmanWidth - widthOffset, y*footmanHeight - heightOffset, null);
            }
            else if (asset.type == Asset.EAssetType.Footman){
                canvas.drawBitmap(footmanBitmap, x*footmanWidth - widthOffset, y*footmanHeight - heightOffset, null);
            }
            else if (asset.type == Asset.EAssetType.GoldMine){
                canvas.drawBitmap(goldMineBitmap, x*goldMineWidth - widthOffset, y*goldMineHeight - heightOffset, null);
            }
        }

        image.setImageBitmap(result);

        //return result;
    }
}
