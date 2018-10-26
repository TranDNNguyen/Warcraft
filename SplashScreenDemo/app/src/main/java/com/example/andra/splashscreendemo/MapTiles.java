package com.example.andra.splashscreendemo;
import android.content.Context;

import java.util.Vector;
import java.io.*;
import java.util.Scanner;
import java.util.HashMap;

public class MapTiles {

    enum ETerrainTileType{
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

        int getIdx(){
            return idx;
        }
    };
    enum ETileType{
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
        int getIdx(){
            return idx;
        }
    };

    //file with a .map extension
    String fileName;

    //MapTiles title
    String mapName;

    //Folder containing map
    String mapDir = "/Users/jugaljain/AndroidStudioProjects/ECS160Android/SplashScreenDemo/app/src/main/assets/";

    //2D array denoting a map consisting of tile types
    Vector< Vector<ETerrainTileType>> terrainMap;
    Vector< Vector<String>> tileSet;
    Vector< Vector<Integer>> idxMap;

    //Parsed lines read from .map file
    Vector< Vector<String>> stringMap;

    private int mapWidth;
    private int mapHeight;

    private HashMap<String, Integer> mapToTileType;

    public MapTiles(String mapFileName, Context context){
        fileName = mapFileName;
        terrainMap = new Vector<Vector<ETerrainTileType>>();
        mapToTileType = new HashMap<String, Integer>();
        tileSet = new Vector< Vector<String>>();
        idxMap = new Vector<Vector<Integer>>();

        try {
            mapParse(fileName, context);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public String getMapDir() {
        return mapDir;
    }

    public void setMapDir(String mapDir) {
        this.mapDir = mapDir;
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
        for(int i = 0; i < mapHeight; i++) {
            processMapLine(line);
            line = scanner.nextLine();
        }

        //System.out.println(terrainMap.get(3).get(3).getIdx());
        //System.out.println("\n"); //XXX

        parseDatFile("terrain.dat", context);
        createIndexMap();
    }


    public String skipCommentLines(Scanner scanner){
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.charAt(0) != '#'){
                return line;
            }
        }
        return "";
    }

    public void processMapLine(String line){
        Vector<ETerrainTileType> mapLine = new Vector<ETerrainTileType>();
        for(int i = 0; i < mapWidth; i++){
            switch (line.charAt(i)){
                case 'G':   mapLine.add(ETerrainTileType.DarkGrass);
                    break;
                case 'g':   mapLine.add(ETerrainTileType.LightGrass);
                    break;
                case 'D':   mapLine.add(ETerrainTileType.DarkDirt);
                    break;
                case 'd':   mapLine.add(ETerrainTileType.LightDirt);
                    break;
                case 'R':   mapLine.add(ETerrainTileType.Rock);
                    break;
                case 'r':   mapLine.add(ETerrainTileType.RockPartial);
                    break;
                case 'F':   mapLine.add(ETerrainTileType.Forest);
                    break;
                case 'f':   mapLine.add(ETerrainTileType.ForestPartial);
                    break;
                case 'W':   mapLine.add(ETerrainTileType.DeepWater);
                    break;
                case 'w':   mapLine.add(ETerrainTileType.ShallowWater);
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

    public void parseDatFile(String fileName, Context context) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        Scanner scanner = new Scanner(is);
        String line = skipCommentLines(scanner);
        line = skipCommentLines(scanner);
        line = skipCommentLines(scanner);
        for(int i = 0; i < 293; i++){
            mapToTileType.put(line, i);
            if(scanner.hasNextLine()){
                line = scanner.nextLine();
            }
        }
    }

    //
    public void buildTileSet(){

    }

    public void createIndexMap(){
        for(int i = 0; i < mapHeight; i++){
            Vector<Integer> line = new Vector<Integer>();
            for(int j = 0; j < mapWidth; j++){
                line.add(getTileIdx(terrainMap.get(i).get(j)));
            }
            idxMap.add(line);
        }

        System.out.println(idxMap.get(3).get(3));
        System.out.println("\n"); //XXX
    }

    //Placeholder function, only returns one subtype of tile for each tileType, re-implement later
    public int getTileIdx(ETerrainTileType tileType){
        switch(tileType) {
            case DarkGrass:
                return mapToTileType.get("dark-grass-F-0");
            case LightGrass:
                return mapToTileType.get("light-grass-F-0");
            case DarkDirt:
                return mapToTileType.get("dark-dirt-F-0");
            case LightDirt:
                return mapToTileType.get("light-dirt-F-0");
            case Rock:
                return mapToTileType.get("rock-F-0");
            case Forest:
                return mapToTileType.get("forest-F-0");
            case DeepWater:
                return mapToTileType.get("deep-water-F-0");
            case ShallowWater:
                return mapToTileType.get("shallow-water-F-0");
            default:
                return -1;
        }
    }

}

