package com.example.andra.splashscreendemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Vector;


public class Asset {
    int owner;
    EAssetType type;
    int x; //current x pos
    int y; //current y pos
    int x2; //x coordinate the unit is headed to
    int y2; //y coordinate the unit is headed to

    Vector<Integer> pixelCoordinates;
    //Bitmap assetImages;
    Bitmap assetBitmap;
    int assetWidth;
    int assetHeight;

    enum EAssetType{
        None(0),
        Peasant(1),
        Footman(2),
        Archer(3),
        Ranger(4),
        GoldMine(5),
        TownHall(6),
        Keep(7),
        Castle(8),
        Farm(9),
        Barracks(10),
        LumberMill(11),
        Blacksmith(12),
        ScoutTower(13),
        GuardTower(14),
        CannonTower(15);

        private int idx;

        EAssetType(int i) {
            idx = i;
        }

        int getIdx(){
            return idx;
        }
    };

    /*
     * Assets draws itself on the canvas it is given
     */
    public void drawAsset(Canvas canvas, int xOffset, int yOffset){
        canvas.drawBitmap(assetBitmap, x*32 - xOffset, y*32 - yOffset, null);
    }

    Asset(EAssetType t, int o, int x, int y){
        //

    }


    Asset(String input[]){
        type = EAssetType.valueOf(input[0]);
        owner = Integer.valueOf(input[1]);
        x = Integer.valueOf(input[2]);
        y = Integer.valueOf(input[3]);
    }
}
