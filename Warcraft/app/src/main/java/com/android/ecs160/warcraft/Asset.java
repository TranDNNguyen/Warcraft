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
    PlayerColor color;
    AssetData assetData;

    Vector<Integer> pixelCoordinates;
    //Bitmap assetImages;
    Bitmap assetBitmap;
    int assetWidth;
    int assetHeight;
    int TileSize = MainActivity_viewport.getTileSize();
    //int size;

    boolean isSelected = false;

    static class AssetData{
        protected String resourceName;
        protected int hitPoints;
        protected int armor;
        protected int sight;
        protected int sightDuringConstruction;
        protected int size;
        protected int speed;
        protected int goldCost;
        protected int lumberCost;
        protected int foodConsumption;
        protected int buildTime;
        protected int attackSteps;
        protected int reloadSteps;
        protected int basicDamage;
        protected int piercingDamage;
        protected int range;

        AssetData(String resourceName, int hitPoints, int armor, int sight, int sightDuringConstruction,
                  int size, int speed, int goldCost, int lumberCost, int foodConsumption,
                  int buildTime, int attackSteps, int reloadSteps, int basicDamage,
                  int piercingDamage, int range){

            this.resourceName = resourceName; //TODO: change to asset enum?
            this.hitPoints = hitPoints;
            this.armor = armor;
            this.sight = sight;
            this.sightDuringConstruction = sightDuringConstruction;
            this.size = size;
            this.speed = speed;
            this.goldCost = goldCost;
            this.lumberCost = lumberCost;
            this.foodConsumption = foodConsumption;
            this.buildTime = buildTime;
            this.attackSteps = attackSteps;
            this.reloadSteps = reloadSteps;
            this.basicDamage = basicDamage;
            this.piercingDamage = piercingDamage;
            this.range = range;
        }
    }

    static class AssetTypeData{
        static AssetData archer = new AssetData("archer", 60, 2, 4, 0, 1, 10, 500, 50, 1, 70, 10, 0, 3, 6, 4);
        static AssetData barracks = new AssetData("barracks", 800, 20, 3, 0, 3, 0, 700, 400, 0, 200, 0, 0, 0, 0, 0);
        static AssetData blacksmith = new AssetData("blacksmith", 775, 20, 3, 0, 3, 0, 800, 450, 0, 200, 0, 0, 0, 0, 0);
        static AssetData cannon_tower = new AssetData("cannon_tower", 160, 20, 9, 9, 2, 0, 500, 150, 0, 190, 1, 20, 50, 0, 7);
        static AssetData castle = new AssetData("castle", 1600, 0, 9, 6, 4, 0, 2500, 1200, 60, 0, 0, 0, 0, 0, 0);
        static AssetData farm = new AssetData("farm", 400, 0, 3, 0, 2, 0, 500, 250, 100, 0, 0, 0, 0, 0, 0);
        static AssetData footman = new AssetData("footman", 60, 2, 4, 0, 1, 10, 600, 0, 1, 60, 10, 0, 6, 3, 1);
        static AssetData gold_mine = new AssetData("gold_mine", 25500, 0, 1, 0, 3, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0);
        static AssetData guard_tower = new AssetData("guard_tower", 130, 20, 9, 9, 2, 0, 500, 150, 0, 140, 1, 20, 4, 12, 6);
        static AssetData keep = new AssetData("keep", 1400, 0, 6, 4, 4, 0, 2000, 1000, 60, 0, 0, 0, 0, 0, 0);
        static AssetData lumber_mill = new AssetData("lumber_mill", 600, 20, 3, 0, 3, 0, 600, 450, 0, 150, 0, 0, 0, 0, 0);
        static AssetData peasant = new AssetData("peasant", 30, 0, 4, 0, 1, 10, 400, 0, 1, 45, 10, 0, 3, 2, 1);
        static AssetData ranger = new AssetData("ranger", 50, 2, 5, 0, 1, 10, 500, 50, 1, 70, 10, 10, 3, 6, 4);
        static AssetData scout_tower = new AssetData("scout_tower", 100, 20, 9, 0, 2, 0, 550, 200, 0, 120, 0, 0, 0, 0, 0);
        static AssetData town_hall = new AssetData("town_hall", 1200, 0, 4, 0, 4, 0, 800, 0, 60, 0, 0, 0, 0, 0, 0);


        public static AssetData getAssetData(EAssetType type){
            switch(type){
                case Archer: return archer;
                case Barracks: return barracks;
                case Blacksmith: return blacksmith;
                case CannonTower: return cannon_tower;
                case Castle: return castle;
                case Farm: return farm;
                case Footman: return footman;
                case GoldMine: return gold_mine;
                case GuardTower:return guard_tower;
                case Keep:return keep;
                case LumberMill: return lumber_mill;
                case Peasant: return peasant;
                case Ranger: return ranger;
                case ScoutTower: return scout_tower;
                case TownHall: return town_hall;
                default:
                    return null;
            }
        }

    }

    enum EAssetAction {
        None(0),
        Construct(1),
        Build(2),
        Repair(3),
        Walk(4),
        StandGround(5),
        Attack(6),
        HarvestLumber(7),
        MineGold(8),
        ConveyLumber(9),
        ConveyGold(10),
        Death(11),
        Decay(12),
        Capability(13);

        private int idx;

        EAssetAction(int i) {
            idx = i;
        }

        int getIdx() {
            return idx;
        }
    }

    //TODO CapabilityType Enum

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

    enum PlayerColor{
        Red(0),
        Blue(1),
        Green(2),
        Purple(3),
        Orange(4),
        Yellow(5),
        Black(6),
        White(7),
        None(8);

        private int idx;

        PlayerColor(int i) {
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

    public boolean isBuilding(){
        switch (type){
            case None:
            case Peasant:
            case Footman:
            case Archer:
            case Ranger:
                return false;
            case GoldMine:
            case TownHall:
            case Keep:
            case Castle:
            case Farm:
            case Barracks:
            case LumberMill:
            case Blacksmith:
            case ScoutTower:
            case GuardTower:
            case CannonTower:
                return true;
            default:
                return false;
        }
    }


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

        //NOTE
        // - Displaying Correct Size for Asset / 11/04/18
        // - Had to place the image without scaling in the middle of tile (72 px image on 32px tile, centered.)
        //
        int assetSize = assetBitmap.getWidth();
        int adjustX = x*TileSize - xOffset + (TileSize/2 - assetSize/2);
        int adjustY = y*TileSize - yOffset + (TileSize/2 - assetSize/2);
        Bitmap resizedAsetBitmap = Bitmap.createScaledBitmap(assetBitmap, assetSize, assetSize, true);
        canvas.drawBitmap(resizedAsetBitmap, adjustX, adjustY, null);
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
        color = PlayerColor.Red;
        assetData = AssetTypeData.getAssetData(type);
    }
}
