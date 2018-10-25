package com.example.andra.splashscreendemo;

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
            assets = AssetLoader.assetParse("testMap", this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ImageView image = (ImageView) findViewById(R.id.bitmapView);
         //load the peasant frames into one large bitmap
        Bitmap peasantImages = BitmapFactory.decodeResource(getResources(), R.drawable.peasant);
        Bitmap goldMineImages = BitmapFactory.decodeResource(getResources(), R.drawable.gold_mine);

        int peasantWidth = peasantImages.getWidth();
        int peasantHeight = peasantWidth;
        int goldMineWidth = goldMineImages.getWidth();
        int goldMineHeight = goldMineWidth;

        //grabbing the first frame from the peasant file
        Bitmap peasantBitmap =  Bitmap.createBitmap(peasantImages, 0,0, peasantWidth,peasantHeight);
        Bitmap goldMineBitmap =  Bitmap.createBitmap(goldMineImages, 0,0, goldMineWidth,goldMineHeight);
        Bitmap result = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(peasantBitmap, 300, 700, null);
        canvas.drawBitmap(goldMineBitmap, 0, 0, null);

        image.setImageBitmap(result);

        //return result;
    }
}
