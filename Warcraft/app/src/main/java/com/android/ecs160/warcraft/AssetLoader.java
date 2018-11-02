package com.android.ecs160.warcraft;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;

public class AssetLoader {
    private String filename;
    //private int mapWidth;
    //private int mapHeight;
    private static int numAssets;
    private static Vector<Asset> assets;

    public int tilePixelSize = 32;

    //public static Bitmap peasantImages;
    static Bitmap[] peasantImages = new Bitmap[172];
    public static Bitmap goldMineImages;
    //public static Bitmap footmanImages;
    static Bitmap[] footmanImages = new Bitmap[92];


    /*
    * Takes asset information from the map file, and compiles a vetor
    * of assets that are part of the game upon launch
    */
    public static Vector<Asset> assetParse(String fileName, Context context, Resources res) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        Scanner scanner = new Scanner(is);
        String[] temp = new String[4];

        //Note: We set the bitmaps here instead of in the individual assets because these
        //files are really big and should only be stored once TODO:move to another function? add other units


        Bitmap peasant = BitmapFactory.decodeResource(res, R.drawable.peasant);
        Bitmap footman = BitmapFactory.decodeResource(res, R.drawable.footman);

        int peasantHeight = peasant.getHeight()/172; //used for indexing bitmap
        int peasantWidth = peasant.getWidth();
        int footmanHeight = footman.getHeight()/92; //used for indexing bitmap
        int footmanWidth = footman.getWidth();

        for(int i = 0; i < 172; i++){
            peasantImages[i] = Bitmap.createBitmap(peasant, 0, peasantHeight*i, peasantWidth, peasantHeight);
        }//parses initial bitmap into smaller sprite-sized bitmaps
        peasant = null;
        for(int i = 0; i < 92; i++){
            footmanImages[i] = Bitmap.createBitmap(footman, 0, footmanHeight*i, footmanWidth, footmanHeight);
        }//parses initial bitmap into smaller sprite-sized bitmaps
        footman = null; //XXX

        goldMineImages = BitmapFactory.decodeResource(res, R.drawable.gold_mine);

        String line = skipToNumAssets(scanner);
        numAssets = Integer.valueOf(line);
        line = scanner.nextLine(); //skip the assets comment

        assets = new Vector<Asset>();

        for(int i = 0; i < numAssets; i++){
            line = scanner.nextLine();
            temp = line.split(" ");
            Asset newAsset = new Asset(temp);
            setAssetBitmap(newAsset, res);
            assets.add(newAsset);
        }//make the specified assets

        return assets;
    }

    /*
     * sets the asset's internal bitmap based on the images stored in the loader
     */
    public static void setAssetBitmap(Asset a, Resources res){
        //TODO: add other unit types

        switch(a.type){
            case Peasant:
                //a.assetImages = BitmapFactory.decodeResource(res, R.drawable.peasant);
                a.assetWidth = peasantImages[0].getWidth();
                a.assetHeight = peasantImages[0].getHeight();
                a.assetBitmap = Bitmap.createBitmap(peasantImages[0], 0,0, a.assetWidth,a.assetHeight);
                break;
            case GoldMine:
                //a.assetImages = BitmapFactory.decodeResource(res, R.drawable.gold_mine);
                a.assetWidth = goldMineImages.getWidth();
                a.assetHeight = goldMineImages.getHeight()/2;
                a.assetBitmap = Bitmap.createBitmap(goldMineImages, 0,0, a.assetWidth,a.assetHeight);
                break;
            case Footman:
                //a.assetImages = BitmapFactory.decodeResource(res, R.drawable.footman);
                a.assetWidth = footmanImages[0].getWidth();
                a.assetHeight = footmanImages[0].getHeight();
                a.assetBitmap = Bitmap.createBitmap(footmanImages[0], 0,0, a.assetWidth,a.assetHeight);
                break;
            default:
                break;
        }
    }

    /*
     * skips excess information in the map file
     */
    public static String skipToNumAssets(Scanner scanner){
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.equals("# Number of assets")){
                return scanner.nextLine();
            }
        }
        return "";
    }



}
