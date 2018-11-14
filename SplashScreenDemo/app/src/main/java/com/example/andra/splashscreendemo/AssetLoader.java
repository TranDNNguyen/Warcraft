package com.example.andra.splashscreendemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

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

    public static Bitmap peasantImages;
    public static Bitmap goldMineImages;
    public static Bitmap footmanImages;

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
        peasantImages = BitmapFactory.decodeResource(res, R.drawable.peasant);
        goldMineImages = BitmapFactory.decodeResource(res, R.drawable.gold_mine);
        footmanImages = BitmapFactory.decodeResource(res, R.drawable.footman);

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
                a.assetWidth = peasantImages.getWidth();
                a.assetHeight = peasantImages.getHeight()/172;
                a.assetBitmap = Bitmap.createBitmap(peasantImages, 0,0, a.assetWidth,a.assetHeight);
                break;
            case GoldMine:
                //a.assetImages = BitmapFactory.decodeResource(res, R.drawable.gold_mine);
                a.assetWidth = goldMineImages.getWidth();
                a.assetHeight = goldMineImages.getHeight()/2;
                a.assetBitmap = Bitmap.createBitmap(goldMineImages, 0,0, a.assetWidth,a.assetHeight);
                break;
            case Footman:
                //a.assetImages = BitmapFactory.decodeResource(res, R.drawable.footman);
                a.assetWidth = footmanImages.getWidth();
                a.assetHeight = footmanImages.getHeight()/92;
                a.assetBitmap = Bitmap.createBitmap(footmanImages, 0,0, a.assetWidth,a.assetHeight);
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
