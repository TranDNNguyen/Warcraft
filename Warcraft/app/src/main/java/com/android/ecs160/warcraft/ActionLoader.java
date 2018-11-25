package com.android.ecs160.warcraft;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;


public class ActionLoader {
    private String fileName;
    private String assetName;
    private static int numFrames;
    Router router;
    Vector<Vector<MapTiles.ETerrainTileType>> terrainMap;
    Resources resources;

    Vector<Integer> walkN = new Vector<>();
    Vector<Integer> walkNE = new Vector<>();
    Vector<Integer> walkNW = new Vector<>();
    Vector<Integer> walkS = new Vector<>();
    Vector<Integer> walkSW = new Vector<>();
    Vector<Integer> walkSE = new Vector<>();
    Vector<Integer> walkE = new Vector<>();
    Vector<Integer> walkW = new Vector<>();
    Vector<Integer> attackN = new Vector<>();
    Vector<Integer> attackNE = new Vector<>();
    Vector<Integer> attackNW = new Vector<>();
    Vector<Integer> attackS = new Vector<>();
    Vector<Integer> attackSW = new Vector<>();
    Vector<Integer> attackSE = new Vector<>();
    Vector<Integer> attackE = new Vector<>();
    Vector<Integer> attackW = new Vector<>();
    Vector<Integer> deathN = new Vector<>();
    Vector<Integer> deathNE = new Vector<>();
    Vector<Integer> deathNW = new Vector<>();
    Vector<Integer> deathS = new Vector<>();
    Vector<Integer> deathSW = new Vector<>();
    Vector<Integer> deathSE = new Vector<>();
    Vector<Integer> deathE = new Vector<>();
    Vector<Integer> deathW = new Vector<>();
    Vector<Integer> decayN = new Vector<>();
    Vector<Integer> decayNE = new Vector<>();
    Vector<Integer> decayNW = new Vector<>();
    Vector<Integer> decayS = new Vector<>();
    Vector<Integer> decaySW = new Vector<>();
    Vector<Integer> decaySE = new Vector<>();
    Vector<Integer> decayE = new Vector<>();
    Vector<Integer> decayW = new Vector<>();
    Vector<Integer> goldN = new Vector<>();
    Vector<Integer> goldNE = new Vector<>();
    Vector<Integer> goldNW = new Vector<>();
    Vector<Integer> goldS = new Vector<>();
    Vector<Integer> goldSW = new Vector<>();
    Vector<Integer> goldSE = new Vector<>();
    Vector<Integer> goldE = new Vector<>();
    Vector<Integer> goldW = new Vector<>();
    Vector<Integer> lumberN = new Vector<>();
    Vector<Integer> lumberNE = new Vector<>();
    Vector<Integer> lumberNW = new Vector<>();
    Vector<Integer> lumberS = new Vector<>();
    Vector<Integer> lumberSW = new Vector<>();
    Vector<Integer> lumberSE = new Vector<>();
    Vector<Integer> lumberE = new Vector<>();
    Vector<Integer> lumberW = new Vector<>();


    /*
            walkE, walkSE, walkS, walkSW, walkW, walkNW,
            attackN, attackNE, attackE, attackSE, attackS, attackSW, attackW, attackNW,
            deathN, deathNE, deathE, deathSE, deathS, deathSW, deathW, deathNW,
            decayN, decayNE, decayE, decaySE, decayS, decaySW, decayW, decayNW,
            goldN, goldNE, goldE, goldSE, goldS, goldSW, goldW, goldNW,
            lumberN, lumberNE, lumberE, lumberSE, lumberS, lumberSW, lumberW, lumberNW,
            noneN, noneNE, noneE, noneSE, noneS, noneSW, noneW, noneNW = new Vector<>();*/

    public String skipCommentLines(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.charAt(0) != '#') {
                return line;
            }
        }
        return "";

    }

    public ActionLoader(String name, Context context) {
        assetName = name;
        //fileName = name + ".dat";
        /*
        try {
            ActionLoaderParse(name, context);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void ActionLoaderParse(String name, Context context) throws IOException {
        fileName = name + ".dat";
        //Log.i("FILENAME",fileName);

        InputStream is = context.getAssets().open(fileName); //was fileName
        Scanner scanner = new Scanner(is);

        String line = skipCommentLines(scanner);
        assetName = line;
        Log.i("ASSETNAME",line);
        line = skipCommentLines(scanner);
        numFrames = Integer.parseInt(line);
        Log.i("NUMBERNAME",line);
        line = skipCommentLines(scanner);


        for (int i = 0; i < numFrames - 1; i++) {
            processFrame(line, i);
            line = scanner.nextLine();
            Log.i("FILENAME", line);
            //actions.add(line);
        }

    }

    // find line of action and store it in an array based off of the enum value

    public void processFrame(String line, int x) {
        //int walkN[], walkNE[], walkE[], walkSE[], walkS[], walkSW[], walkW[], walkNW[], attackN[], attackNE[], attackE[], attackSE[];
        String parts[] = line.split("-");
        Log.i("PROCESSING", parts[0]);
        Log.i("PROCESSING", parts[1]);
        Log.i("PROCESSING", parts[2]);
        //int x = i + 5;
        Log.i("XVALUE " , "value: " + x);
        switch (parts[0]) {
            case "walk":
                switch (parts[1]) {
                    case "n":
                        walkN.add(x);
                        break;
                    case "ne":
                        walkNE.add(x);
                        break;
                    case "e":
                        walkE.add(x);
                        break;
                    case "se":
                        walkSE.add(x);
                        break;
                    case "s":
                        walkS.add(x);
                        break;
                    case "sw":
                        walkSW.add(x);
                        break;
                    case "w":
                        walkW.add(x);
                        break;
                    case "nw":
                        walkNW.add(x);
                        break;
                }
                //break;

            case "attack":
                switch (parts[1]) {
                    case "n":
                        attackN.add(x);
                        break;
                    case "ne":
                        attackNE.add(x);
                        break;
                    case "e":
                        attackE.add(x);
                        break;
                    case "se":
                        attackSE.add(x);
                        break;
                    case "s":
                        attackS.add(x);
                        break;
                    case "sw":
                        attackSW.add(x);
                        break;
                    case "w":
                        attackW.add(x);
                        break;
                    case "nw":
                        attackNW.add(x);
                        break;
                }
                break;
            case "death":
                switch (parts[1]) {
                    case "n":
                        deathN.add(x);
                        break;
                    case "ne":
                        deathNE.add(x);
                        break;
                    case "e":
                        deathE.add(x);
                        break;
                    case "se":
                        deathSE.add(x);
                        break;
                    case "s":
                        deathS.add(x);
                        break;
                    case "sw":
                        deathSW.add(x);
                        break;
                    case "w":
                        deathW.add(x);
                        break;
                    case "nw":
                        deathNW.add(x);
                        break;
                }
                break;
            case "decay":
                switch (parts[1]) {
                    case "n":
                        decayN.add(x);
                        break;
                    case "ne":
                        decayNE.add(x);
                        break;
                    case "e":
                        decayE.add(x);
                        break;
                    case "se":
                        decaySE.add(x);
                        break;
                    case "s":
                        decayS.add(x);
                        break;
                    case "sw":
                        decaySW.add(x);
                        break;
                    case "w":
                        decayW.add(x);
                        break;
                    case "nw":
                        decayNW.add(x);
                        break;
                }
                break;
            case "gold":
                switch (parts[1]) {
                    case "n":
                        goldN.add(x);
                        break;
                    case "ne":
                        goldNE.add(x);
                        break;
                    case "e":
                        goldE.add(x);
                        break;
                    case "se":
                        goldSE.add(x);
                        break;
                    case "s":
                        goldS.add(x);
                        break;
                    case "sw":
                        goldSW.add(x);
                        break;
                    case "w":
                        goldW.add(x);
                        break;
                    case "nw":
                        goldNW.add(x);
                        break;
                }
            case "lumber":
                switch (parts[1]) {
                    case "n":
                        lumberN.add(x);
                        break;
                    case "ne":
                        lumberNE.add(x);
                        break;
                    case "e":
                        lumberE.add(x);
                        break;
                    case "se":
                        lumberSE.add(x);
                        break;
                    case "s":
                        lumberS.add(x);
                        break;
                    case "sw":
                        lumberSW.add(x);
                        break;
                    case "w":
                        lumberW.add(x);
                        break;
                    case "nw":
                        lumberNW.add(x);
                        break;
                }
                break;

            default:

                break;
        }
    }

    static int[] R_IDS = {0, R.drawable.peasant, R.drawable.footman,R.drawable.archer, R.drawable.ranger,
            R.drawable.gold_mine, R.drawable.town_hall, R.drawable.keep, R.drawable.castle,
            R.drawable.farm, R.drawable.barracks, R.drawable.lumber_mill, R.drawable.blacksmith,
            R.drawable.scout_tower, R.drawable.guard_tower, R.drawable.cannon_tower};

    public void updateFrame(Asset asset, Vector<Integer> action) {
        for (int i = 0; i < action.size(); i++) {
            setAssetBitmap(asset, i);
        }
    }

    public void setAssetBitmap(Asset a, int frame) {

        int index = a.type.getIdx();
        Bitmap allFrames = BitmapFactory.decodeResource(resources, R_IDS[index]);
        int imageHeight = allFrames.getHeight() / numFrames;
        int imageWidth = allFrames.getWidth();

        //grab the current requested frame from the appropriate file
        a.assetBitmap = Bitmap.createBitmap(allFrames, 0, imageHeight * frame, imageWidth, imageHeight);
        a.assetWidth = imageWidth;
        a.assetHeight = imageHeight;
    }

    /*
    public void walk(Asset asset) {
        Asset.EDirection travelDirection;
        travelDirection = router.FindPath(terrainMap, asset, asset.x2, asset.y2);
        asset.direction = travelDirection;

        switch (travelDirection) {
            case North:
                updateFrame(asset, walkN);
                break;
            case NorthWest:
                updateFrame(asset, walkNW);
                break;
            case NorthEast:
                updateFrame(asset, walkNE);
                asset.y--;
                break;
            case South:
                updateFrame(asset, walkS);
                break;
            case SouthWest:
                updateFrame(asset, walkSW);
                break;
            case SouthEast:
                updateFrame(asset, walkSE);
                asset.y++;
                break;
        }
        switch (travelDirection) {
            case West:
                updateFrame(asset, walkW);
                break;
            case SouthWest:
                updateFrame(asset, walkSW);
                break;
            case NorthWest:
                updateFrame(asset, walkNW);
                asset.x--;
                break;
            case East:
                updateFrame(asset, walkE);
                break;
            case NorthEast:
                updateFrame(asset, walkNE);
                break;
            case SouthEast:
                updateFrame(asset, walkSE);
                asset.x++;
                break;
        }
        assetRenderer.updateAssetFrame(asset);

        if (asset.x == asset.x2 && asset.y == asset.y2) {
            asset.action = Asset.EAssetAction.None;
        }
    }*/
}
