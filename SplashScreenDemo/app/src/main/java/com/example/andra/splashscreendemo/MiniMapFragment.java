package com.example.andra.splashscreendemo;

import android.support.v4.app.Fragment;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.view.LayoutInflater;

import java.util.Vector;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MiniMapFragment extends Fragment {

    private MapTiles mapTiles;
    private Bitmap[] tileSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        //ImageButton imgBtn = (ImageButton) getView().findViewById(R.id.archer);
        //IconImage Icon = new IconImage();
       // Icon.setIconImage(imgBtn);

        View miniMapView = inflater.inflate(R.layout.fragment_mini_map, container, false);

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

        mapTiles = new MapTiles("test.map", getActivity());

        Bitmap currentViewPort = generateViewPort(0,0, 64, 64);
        ViewportView imageView = (ViewportView) miniMapView.findViewById(R.id.minimap);
        float factor = (float) currentViewPort.getWidth() / currentViewPort.getHeight();
        Bitmap scaled = Bitmap.createScaledBitmap(currentViewPort, (int)(500*factor), 500, true);
        Bitmap test = tileSet[20];

        imageView.setImageBitmap(scaled);

        return miniMapView;
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

        AssetRenderer assetRenderer = new AssetRenderer(getActivity(), getResources());
        canvas.drawBitmap( assetRenderer.renderAssets(), 0, 0, null);
        return temp;
    }
}