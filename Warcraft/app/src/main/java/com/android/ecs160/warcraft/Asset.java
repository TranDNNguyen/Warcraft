package com.android.ecs160.warcraft;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Vector;


public class Asset {
    int owner;
    EAssetType type;
    int x; //current x pos
    int y; //current y pos
    int x2; //x coordinate the unit is headed to
    int y2; //y coordinate the unit is headed to
    //int direction; //TODO: use enum instead
    EDirection direction;
    EAssetAction action;

    Vector<Integer> pixelCoordinates;
    //Bitmap assetImages;
    Bitmap assetBitmap;
    int assetWidth;
    int assetHeight;
    int TileSize = MainActivity_viewport.getTileSize();

    boolean isSelected = false;

    enum EAssetAction {
        None(0),
        Walk(1);
        //TODO: add more

        private int idx;

        EAssetAction(int i) {
            idx = i;
        }

        int getIdx() {
            return idx;
        }
    }

    enum EDirection {
        North(0),
        NorthEast(1),
        East(2),
        SouthEast(3),
        South(4),
        SouthWest(5),
        West(6),
        NorthWest(7),
        Max(8); //?

        private int idx;

        EDirection(int i) {
            idx = i;
        }

        int getIdx() {
            return idx;
        }
    }

    enum EAssetType {
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

        int getIdx() {
            return idx;
        }
    }

    ;


    public void setAction(EAssetAction assetAction, int x, int y) {
        if (assetAction == EAssetAction.Walk) {
            action = assetAction;
            x2 = x;
            y2 = y;
        }
    }

    /*
     * Assets draws itself on the canvas it is given
     */
    public void drawAsset(Canvas canvas, int xOffset, int yOffset) {
        Bitmap resizedAsetBitmap = Bitmap.createScaledBitmap(assetBitmap, TileSize, TileSize, true);
        canvas.drawBitmap(resizedAsetBitmap, x * TileSize - xOffset, y * TileSize - yOffset, null);
    }

    public void drawAssetSelection(Canvas canvas, int xOffset, int yOffset) {
        if (this.isSelected) {
            Paint paint = new Paint();

            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);

            canvas.drawRect(x * TileSize - xOffset, y * TileSize - yOffset, x * TileSize - xOffset + TileSize, y * TileSize - yOffset + TileSize, paint);
        }
    }

    Asset(EAssetType t, int o, int x, int y) {
        //

    }

    Asset(String input[]) {
        type = EAssetType.valueOf(input[0]);
        owner = Integer.valueOf(input[1]);
        x = Integer.valueOf(input[2]);
        y = Integer.valueOf(input[3]);
        direction = EDirection.North;
        action = EAssetAction.None;
    }
}
