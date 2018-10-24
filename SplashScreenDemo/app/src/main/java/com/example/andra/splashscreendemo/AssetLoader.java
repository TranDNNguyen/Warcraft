package com.example.andra.splashscreendemo;

import android.content.Context;
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

    public static Vector<Asset> assetParse(String fileName, Context context) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        Scanner scanner = new Scanner(is);
        String[] temp = new String[4];

        String line = skipToNumAssets(scanner);
        numAssets = Integer.valueOf(line);
        line = scanner.nextLine(); //skip the assets comment


        for(int i = 0; i < numAssets; i++){
            line = scanner.nextLine();
            temp = line.split(" ");
            Asset newAsset = new Asset(temp);
            assets.add(newAsset);
        }

        return assets;
    }

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
