package com.example.andra.splashscreendemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.peasant);

        int peasantWidth = b.getWidth();
        int peasantHeight = peasantWidth;

        Bitmap peasantImg1 = b.crop

        image.setImageBitmap(b);
    }




}
