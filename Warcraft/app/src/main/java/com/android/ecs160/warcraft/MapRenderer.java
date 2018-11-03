package com.android.ecs160.warcraft;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


// MapRenderer Class
// - Draw bitmap of map using MapTiles class.

public class MapRenderer {

    //private
    public MapTiles mapTiles;// = new MapTiles("hedges.map", mContext);
    private Bitmap[] tileSet = new Bitmap[293];// = generateTileSet();

    int TileSize = 32;// MainActivity_viewport.getTileSize();

    private Context mContext;
    Bitmap terrain, terrainMap;
    Bitmap scaledMap;
    Bitmap viewArea;
    Bitmap minimap;
    private int mapWidth;
    private int mapHeight;
    private int viewWidth;
    private int viewHeight;


    //init. = init() + loadMapdata()
    MapRenderer(Context c) {
        mContext = c;
        //mapTiles = new MapTiles("hedges.map", mContext);
        mapTiles = new MapTiles("test.map", mContext);
        generateTileSet();
        //Log.e("MapError", "Failed to setup MapRenderer");

    }

    MapRenderer(Context c, int w, int h) {
        mContext = c;
        mapTiles = new MapTiles("test.map", mContext);
        generateTileSet();

        //viewWidth = 1000;
        //viewHeight = h;
        generateMap();
    }

    public void setViewportSize(int w, int h) {
        viewWidth = w;
        viewHeight = h;
    }

    //TODO
    // Currently, we are drawing map within viewport every time
    //  Maybe we can draw map once, and just copy the viewport region of map, using Canvas.
    //
    //REQUIRED ONE  // = drawTerrain() in Original Game Code
    public void generateMap() {

        int tileH = mapTiles.getMapHeight(), tileW = mapTiles.getMapWidth();

        terrainMap = Bitmap.createBitmap(tileW * TileSize, tileH * TileSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(terrainMap);
        Bitmap temp;

        int top = 0;
        for (int i = 0; i < tileH; i++) {
            int left = 0;
            for (int j = 0; j < tileW; j++) {
                temp = Bitmap.createScaledBitmap(tileSet[mapTiles.idxMap.get(i).get(j)], TileSize, TileSize, false);
                canvas.drawBitmap(temp, top, left, null);
                left += TileSize;
            }
            top += TileSize;
        }
    }


    /*
    public Bitmap generateMiniMap(){

        int tileH = mapTiles.getMapHeight(), tileW = mapTiles.getMapWidth();

        terrainMap = Bitmap.createBitmap(tileW, tileH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(terrainMap);
        Bitmap temp;

        int top = 0;
        for(int i = 0; i < tileH; i++) {
            int left = 0;
            for (int j = 0; j < tileW; j++) {
                temp = Bitmap.createScaledBitmap(tileSet[mapTiles.idxMap.get(i).get(j)],TileSize, TileSize, false);
                canvas.drawBitmap(temp, top, left, null);
                left += TileSize;
            }
            top += TileSize;
        }
        return minimap;
    }
    */


    public Bitmap drawTerrain(int x, int y) {

        //Tile Size Constant
        //Bitmap temp = Bitmap.createBitmap(terrain, startX, startY, viewWidth, viewHeight);
        viewArea = Bitmap.createBitmap(terrainMap, x, y, 1000, 600);
        //viewArea = Bitmap.createBitmap(scaledMap, x,y,1000,600);
        return viewArea;
    }


    public Bitmap drawMinimap() {

        //Tile Size Constant
        //Bitmap temp = Bitmap.createBitmap(terrain, startX, startY, viewWidth, viewHeight);
        minimap = Bitmap.createScaledBitmap(terrainMap, 100, 100, true);
        //viewArea = Bitmap.createBitmap(scaledMap, x,y,1000,600);
        return minimap;
    }


    //REQUIRED // = loadMapTiles()
    public Bitmap[] generateTileSet() {
        terrain = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.terrain);
        Bitmap[] ts = new Bitmap[293];  // = 293 tiles.
        for (int i = 0; i < 293; i++) {
            tileSet[i] = Bitmap.createBitmap(terrain, 0, 32 * i, 32, 32);
        }
        //Log.e("MapError", "What is going on?");
        return ts;
    }


    //Pocketmon Map Function ...
    public void loadMapData() {
        terrain = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pokemon);
        //map.setImageBitmap(terrain);  // No Scaling

        //Scaling - 2000px in height
        float factor = (float) terrain.getWidth() / (float) terrain.getHeight();
        mapWidth = (int) (2000 * factor);
        mapHeight = 2000;
        scaledMap = Bitmap.createScaledBitmap(terrain, mapWidth, mapHeight, true);
        //viewport.setImageBitmap(scaledMap);

        //viewWidth = 1000;
        //viewHeight = 600;
        //viewArea = Bitmap.createBitmap(scaledMap, 0,0,viewWidth,viewHeight);

    }


    //TODO - resize the viewport image.
    public void loadMapData(int viewW, int viewH) {
        this.loadMapData();
        viewWidth = viewW;
        viewHeight = viewH;
    }

    public Bitmap drawMap() {
        return scaledMap;
    }


    //Pokemon Map Function ...
    // Rendering - Required one.
    public Bitmap drawPokemonMap(int x, int y) {
        this.loadMapData();
        viewArea = Bitmap.createBitmap(scaledMap, x, y, 1000, 600);
        return viewArea;
    }

    //Pocketmon Map Function ...
    // Rendering - Required one.  //  version 2
    public Bitmap drawPokemonMap(int x, int y, int w, int h) {
        viewArea = Bitmap.createBitmap(scaledMap, x, y, w, h);
        return viewArea;
    }

    //Boundary Checking Function.
    public boolean checkBoundary(int x, int y) {
        if (x < 0 || mapWidth < x + viewWidth) return false;
        if (y < 0 || mapHeight < y + viewHeight) return false;
        return true;
    }
}




