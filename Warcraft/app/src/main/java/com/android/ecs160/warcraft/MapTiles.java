package com.android.ecs160.warcraft;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class MapTiles {

    public class CTilePosition {
        //TODO: should this be in the map classes? tiles?
        int x;
        int y;

        public CTilePosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int DistanceSquared(CTilePosition pos) {
            int DeltaX = pos.x - x;
            int DeltaY = pos.y - y;

            return DeltaX * DeltaX + DeltaY * DeltaY;
        }
    }

    enum ETerrainTileType {
        None(0),
        DarkGrass(1),
        LightGrass(2),
        DarkDirt(3),
        LightDirt(4),
        Rock(5),
        RockPartial(6),
        Forest(7),
        ForestPartial(8),
        DeepWater(9),
        ShallowWater(10),
        Max(11);

        private int idx;

        ETerrainTileType(int i) {
            idx = i;
        }

        int getIdx() {
            return idx;
        }
    }

    ;

    enum ETileType {
        None(0),
        DarkGrass(1),
        LightGrass(2),
        DarkDirt(3),
        LightDirt(4),
        Rock(5),
        Rubble(6),
        Forest(7),
        Stump(8),
        DeepWater(9),
        ShallowWater(10),
        Max(11);

        private int idx;

        ETileType(int i) {
            idx = i;
        }

        int getIdx() {
            return idx;
        }
    }

    ;

    //file with a .map extension
    private String fileName;

    //MapTiles title
    private String mapName;

    //2D array denoting a map consisting of tile types
    public Vector<Vector<ETerrainTileType>> terrainMap;
    // feeds into
    private HashMap<ETerrainTileType, String> typeToName;
    //which feeds into
    private HashMap<String, Vector<String>> nameToStrings;
    //which feeds into
    private HashMap<String, Integer> stringToIdx;
    //which lets us create
    Vector<Vector<Integer>> idxMap;

    private static int mapWidth;
    private static int mapHeight;

    private static MapTiles myMapTiles = null;

    public MapTiles(String mapFileName, Context context) {

        if(myMapTiles != null){
            //todo: throw error
        }
        myMapTiles = this;
        fileName = mapFileName;

        terrainMap = new Vector<Vector<ETerrainTileType>>();
        idxMap = new Vector<Vector<Integer>>();

        stringToIdx = new HashMap<String, Integer>();
        typeToName = new HashMap<ETerrainTileType, String>();
        nameToStrings = new HashMap<String, Vector<String>>();

        try {
            mapParse(fileName, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MapTiles getMapTilesInstance(){
        if(myMapTiles != null){
            return myMapTiles;
        }
        else{
            return null;
            //TODO: throw error?
        }
    }

    public static int getMapWidth() {
        return mapWidth;
    }

    public static int getMapHeight() {
        return mapHeight;
    }

    public void mapTypeToName() {
        typeToName.put(ETerrainTileType.DarkGrass, "dark-grass");
        typeToName.put(ETerrainTileType.LightGrass, "light-grass");
        typeToName.put(ETerrainTileType.DarkDirt, "dark-dirt");
        typeToName.put(ETerrainTileType.LightDirt, "light-dirt");
        typeToName.put(ETerrainTileType.Rock, "rock");
        typeToName.put(ETerrainTileType.Forest, "forest");
        typeToName.put(ETerrainTileType.DeepWater, "deep-water");
        typeToName.put(ETerrainTileType.ShallowWater, "shallow-water");
    }


    public void mapParse(String fileName, Context context) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        Scanner scanner = new Scanner(is);

        String line = skipCommentLines(scanner);
        mapName = line;
        line = skipCommentLines(scanner);
        String tmpBuffer[] = line.split(" ");
        mapWidth = Integer.parseInt(tmpBuffer[0]);
        mapHeight = Integer.parseInt(tmpBuffer[1]);
        line = skipCommentLines(scanner);
        for (int i = 0; i < mapHeight; i++) {
            processMapLine(line);
            line = scanner.nextLine();
        }
        parseDatFile("terrain.dat", context);
        mapTypeToName();
        createIndexMap();
    }

    public static boolean isTraversable(ETerrainTileType tileType) {
        switch (tileType) {
            case None:
            case DarkGrass:
            case LightGrass:
            case DarkDirt:
            case LightDirt:
            //case Rubble:
            //case Stump:
                return true;
            default:
                return false;
        }
    }

    public Vector<Vector<ETerrainTileType>> getCurrentTerrain(){
        return terrainMap;
    }

    public ETerrainTileType getTileType(int x, int y){
        return terrainMap.get(x).get(y);
    }

    public String skipCommentLines(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.charAt(0) != '#') {
                return line;
            }
        }
        return "";

    }

    public void processMapLine(String line) {
        Vector<ETerrainTileType> mapLine = new Vector<ETerrainTileType>();
        for (int i = 0; i < mapWidth; i++) {
            switch (line.charAt(i)) {
                case 'G':
                    mapLine.add(ETerrainTileType.DarkGrass);
                    break;
                case 'g':
                    mapLine.add(ETerrainTileType.LightGrass);
                    break;
                case 'D':
                    mapLine.add(ETerrainTileType.DarkDirt);
                    break;
                case 'd':
                    mapLine.add(ETerrainTileType.LightDirt);
                    break;
                case 'R':
                    mapLine.add(ETerrainTileType.Rock);
                    break;
                case 'r':
                    mapLine.add(ETerrainTileType.RockPartial);
                    break;
                case 'F':
                    mapLine.add(ETerrainTileType.Forest);
                    break;
                case 'f':
                    mapLine.add(ETerrainTileType.ForestPartial);
                    break;
                case 'W':
                    mapLine.add(ETerrainTileType.DeepWater);
                    break;
                case 'w':
                    mapLine.add(ETerrainTileType.ShallowWater);
                    break;
                default:
                    /*
                        TO DO
                    Go back to Main Menu, map load failed
                    *
                    *
                    *
                    * TO DO
                    * */
                    break;
            }
        }
        terrainMap.add(mapLine);
    }

    //Helper function to remove the last digit and hyphen from a tile variant name
    //for input "light-grass-F-0" should return "light-grass-F"
    private String stripLastDigit(String input) {
//        String out;
//        int idx;
//        for(idx = input.length()-1; idx >= 0; idx--){
//            if(Character.isLetter(input.charAt(idx))){
//                break;
//            }
//        }
//        out = input.substring(0,(idx+1));

        String parts[] = input.split("-");
        return parts.length == 4 ? parts[0] + '-' + parts[1] + '-' + parts[2] : parts[0] + '-' + parts[1];
    }

    public void parseDatFile(String fileName, Context context) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        Scanner scanner = new Scanner(is);
        String line = skipCommentLines(scanner);
        line = skipCommentLines(scanner);
        line = skipCommentLines(scanner);
        for (int i = 0; i < 293; i++) {
            stringToIdx.put(line, i);
            String key = stripLastDigit(line);
            System.out.print(key + " " + line + "\n");
            if (nameToStrings.containsKey(key)) {
                nameToStrings.get(key).add(line);
            } else {
                Vector<String> val = new Vector<String>();
                val.add(line);
                nameToStrings.put(key, val);
                //System.out.print("entered");
            }
            if (scanner.hasNextLine()) {
                line = scanner.nextLine();
            }

        }
    }

    //
    public void buildTileSet() {

    }


    public void createIndexMap() {
        for (int i = 0; i < mapHeight; i++) {
            Vector<Integer> line = new Vector<Integer>();
            for (int j = 0; j < mapWidth; j++) {
                line.add(getTileIdx(terrainMap.get(i).get(j), i, j));
            }
            idxMap.add(line);
        }
    }

    //Placeholder function, only returns one subtype of tile for each tileType, re-implement later
    public int getTileIdx(ETerrainTileType tileType, int y, int x) {

        if (x == mapWidth - 1 || y == mapHeight - 1) {
            String tileName = typeToName.get(tileType);
            tileName += "-F";
            int max = nameToStrings.get(tileName).size();
            int var = (int) Math.floor(Math.random() * max);
            tileName += "-" + var;

            return stringToIdx.get(tileName);
        }

        ETerrainTileType UL = terrainMap.get(y).get(x);
        ETerrainTileType UR = terrainMap.get(y).get(x + 1);
        ETerrainTileType LL = terrainMap.get(y + 1).get(x);
        ETerrainTileType LR = terrainMap.get(y + 1).get(x + 1);
        int typeIndex;
        ETerrainTileType overallType = ETerrainTileType.Max;

        ETerrainTileType typeA = UL;
        ETerrainTileType typeB = typeA;


        if (UR != typeA) {
            typeB = UR;
        } else if (LL != typeA) {
            typeB = LL;
        } else if (LR != typeA) {
            typeB = LR;
        }

        //System.out.println(typeA + " " + typeB);
        if (typeA == typeB) {
            overallType = typeA;
            String tileName = typeToName.get(overallType);
            tileName += "-F";

            int max = nameToStrings.get(tileName).size();
            int var = (int) Math.floor(Math.random() * max);
            tileName += "-" + var;

            return stringToIdx.get(tileName);

        } else {
            // w + d = w    shallow water and light dirt = shallow water
            // D + d = D    dark dirt and light dirt = dark dirt
            // R + d = R    rock and light dirt = rock
            // d + g = d    light dirt and light grass = light dirt
            // G + g = G    dark grass and light grass = dark grass
            // F + g = F    forest and light grass = forest
            // W + w = W    deep water and shallow water = deep water
            if ((typeA == ETerrainTileType.ShallowWater && typeB == ETerrainTileType.LightDirt)
                    || (typeB == ETerrainTileType.ShallowWater && typeA == ETerrainTileType.LightDirt)) {
                overallType = ETerrainTileType.ShallowWater;
            } else if ((typeA == ETerrainTileType.DarkDirt && typeB == ETerrainTileType.LightDirt)
                    || (typeB == ETerrainTileType.DarkDirt && typeA == ETerrainTileType.LightDirt)) {
                overallType = ETerrainTileType.DarkDirt;
            } else if ((typeA == ETerrainTileType.Rock && typeB == ETerrainTileType.LightDirt)
                    || (typeB == ETerrainTileType.Rock && typeA == ETerrainTileType.LightDirt)) {
                overallType = ETerrainTileType.Rock;
            } else if ((typeA == ETerrainTileType.LightDirt && typeB == ETerrainTileType.LightGrass)
                    || (typeB == ETerrainTileType.LightDirt && typeA == ETerrainTileType.LightGrass)) {
                overallType = ETerrainTileType.LightDirt;
            } else if ((typeA == ETerrainTileType.DarkGrass && typeB == ETerrainTileType.LightGrass)
                    || (typeB == ETerrainTileType.DarkGrass && typeA == ETerrainTileType.LightGrass)) {
                overallType = ETerrainTileType.DarkGrass;
            } else if ((typeA == ETerrainTileType.Forest && typeB == ETerrainTileType.LightGrass)
                    || (typeB == ETerrainTileType.Forest && typeA == ETerrainTileType.LightGrass)) {
                overallType = ETerrainTileType.Forest;
            } else if ((typeA == ETerrainTileType.DeepWater && typeB == ETerrainTileType.ShallowWater)
                    || (typeB == ETerrainTileType.DeepWater && typeA == ETerrainTileType.ShallowWater)) {
                overallType = ETerrainTileType.DeepWater;
            }
        }

        if (overallType == ETerrainTileType.Max) {
            return -1;
        }

        int indexBits = 0;
        if (UL == overallType) indexBits |= 0x1;
        if (UR == overallType) indexBits |= 0x2;
        if (LL == overallType) indexBits |= 0x4;
        if (LR == overallType) indexBits |= 0x8;

        String tileName = typeToName.get(overallType);
        int var = (int) Math.ceil(Math.random() * 15);
        String hexIdx = Integer.toString(indexBits, 16).toUpperCase();
        tileName += '-';
        tileName += hexIdx.charAt(0);
        //tileName += "F";
        //System.out.println("this is " + tileName);
        int max = nameToStrings.get(tileName).size();
        if (max > 1) {
            var = new Random().nextInt(max);
        } else {
            var = 0;
        }
        tileName += '-';
        tileName += var;

        //System.out.println("this is also " + tileName);

        return stringToIdx.get(tileName);
        /*switch(tileType) {
            case DarkGrass:
                return stringToIdx.get("dark-grass-F-0");
            case LightGrass:
                return stringToIdx.get("light-grass-F-0");
            case DarkDirt:
                return stringToIdx.get("dark-dirt-F-0");
            case LightDirt:
                return stringToIdx.get("light-dirt-F-0");
            case Rock:
                return stringToIdx.get("rock-F-0");
            case Forest:
                return stringToIdx.get("forest-F-0");
            case DeepWater:
                return stringToIdx.get("deep-water-F-0");
            case ShallowWater:
                return stringToIdx.get("shallow-water-F-0");
            default:
                return -1;
        }*/

    }

}