package com.android.ecs160.warcraft;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


// MapRenderer Class
// - Draw bitmap of map using MapTiles class.

public class MapRenderer {

    public boolean updateFailed;
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

    static public int bitmapWidth;
    static public int bitmapHeight;

    //init. = init() + loadMapdata()
    MapRenderer(Context c) {
        mContext = c;
        //mapTiles = new MapTiles("hedges.map", mContext);
        mapTiles = new MapTiles("test.map", mContext);
        generateTileSet();
        generateMiniMap();
        //Log.e("MapError", "Failed to setup MapRenderer");

    }

    MapRenderer(Context c, int w, int h) {
        mContext = c;
        mapTiles = new MapTiles("test.map", mContext);
        generateTileSet();
        generateMiniMap();
        updateFailed = false;

        viewWidth = w;
        viewHeight = h;
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
        mapHeight = mapTiles.getMapHeight();
        mapWidth = mapTiles.getMapWidth();

        bitmapWidth = tileW * TileSize;
        bitmapHeight = tileH * TileSize;

        terrainMap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(terrainMap);
        Bitmap temp;

        int top = 0;
        for (int i = 0; i < tileH; i++) {
            int left = 0;
            for (int j = 0; j < tileW; j++) {
                temp = Bitmap.createScaledBitmap(tileSet[mapTiles.idxMap.get(i).get(j)], TileSize, TileSize, false);
                canvas.drawBitmap(temp, left, top, null);
                left += TileSize;
            }
            top += TileSize;
        }
    }



    //public void generateMiniMap(Canvas canvas){
    //public void generateMiniMap(Bitmap minimap){
    public void generateMiniMap(){

        int sampleMultiplier = 5; // Map Scaling Factor
        int tileH = mapTiles.getMapHeight(), tileW = mapTiles.getMapWidth();

        minimap = Bitmap.createBitmap(MapTiles.getMapWidth()*5, MapTiles.getMapHeight()*5, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(minimap);
        Bitmap temp;

        int top = 0;
        for(int i = 0; i < tileH; i++) {
            int left = 0;
            for (int j = 0; j < tileW; j++) {
                temp = Bitmap.createScaledBitmap(tileSet[mapTiles.idxMap.get(i).get(j)],sampleMultiplier, sampleMultiplier, false);
                canvas.drawBitmap(temp, left, top, null);
                left += sampleMultiplier;
            }
            top += sampleMultiplier;
        }
    }


    public Bitmap drawMinimap(){
        return minimap;
    }

    public Bitmap drawTerrain(int x, int y) {

        //TODO: boundary checking?
        //if (currX - dx >= 0) currX -= dx;
        /*
        if(x < 0 || y < 0 || x > 2048-1000 || y > 2048 - 600){
            updateFailed = true;
            return null;
        }
        */
        // if (currY - dy >= 0) currY -= dy;
        //Tile Size Constant
        //Bitmap temp = Bitmap.createBitmap(terrain, startX, startY, viewWidth, viewHeight);
        //viewArea = Bitmap.createBitmap(terrainMap, x, y, 1000, 600);
        viewArea = Bitmap.createBitmap(terrainMap, x, y, viewWidth, viewHeight);
        //viewArea = Bitmap.createBitmap(scaledMap, x,y,1000,600);

        updateFailed = false;
        return viewArea;
    }


    public Bitmap drawMinimap_test() {

        //Tile Size Constant
        //Bitmap temp = Bitmap.createBitmap(terrain, startX, startY, viewWidth, viewHeight);
        minimap = Bitmap.createScaledBitmap(terrainMap, 100, 100, true);



        //viewArea = Bitmap.createBitmap(scaledMap, x,y,1000,600);
        return minimap;
    }


    //REQUIRED // = loadMapTiles()
    public void generateTileSet() {
        terrain = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.terrain);
        for (int i = 0; i < 293; i++) {
            tileSet[i] = Bitmap.createBitmap(terrain, 0, 32 * i, 32, 32);
        }
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
    /*
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
    */

    //Boundary Checking Function.
    public boolean checkBoundary(int x, int y) {
        if (x < 0 || mapWidth < x + viewWidth) return false;
        if (y < 0 || mapHeight < y + viewHeight) return false;
        return true;
    }
}




