package com.android.ecs160.warcraft;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.HashMap;


public class Asset {
    int owner;
    EAssetType type;
    boolean visible = true;
    int x; //current x pos
    int y; //current y pos
    EDirection direction;
    int HP;

    Queue<EAssetAction> commands;
    Queue<CTilePosition> positions;
    Asset building;//for peasants who are executing build command

    PlayerData.PlayerColor color;
    AssetData assetData;
    int steps;
    int lumber;

    Vector<Integer> pixelCoordinates;
    Bitmap assetBitmap;
    int assetWidth;
    int assetHeight;
    int TileSize = MainActivity_viewport.getTileSize();

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

    public AssetData getAssetData() {
        return this.assetData;
    }

    public String getAssetData_ResourceName() {
        return this.assetData.resourceName;
    }

    //todo: deal with "build-simple"
    //todo: implement upgrades (affects blacksmith, barracks?, and unit buttons)
    //action icons
    private static final HashMap<String, Integer[]> assetActionMap;
    static {
        //"Icons.dat line number" - 6 = "icon number"
        assetActionMap = new HashMap<String, Integer[]>();
        Integer PeasantIconNumbers[] = new Integer[] {83, 164, 116, 85, 86, 89, 87}; //need upgrades
        Integer FootmanIconNumbers[] = new Integer[] {83, 164, 116, 170, 172}; //need upgrades
        Integer RangedUnitIconNumbers[] = new Integer[] {83, 164, 124, 170, 172}; //need upgrades
        Integer BarracksIconNumbers[] = new Integer[] {2, 4}; //need upgrades?
        Integer TownHallIconNumbers[] = new Integer[] {0, 66};
        Integer BlacksmithIconNumbers[] = new Integer[] {}; //need upgrades
        Integer CannonTowerIconNumbers[] = new Integer[] {};
        Integer GuardTowerIconNumbers[] = new Integer[] {78};
        Integer ScoutTowerIconNumbers[] = new Integer[] {75};
        Integer KeepIconNumbers[] = new Integer[] {0, 68};
        Integer CastleIconNumbers[] = new Integer[] {0};
        Integer LumberMillIconNumbers[] = new Integer[] {};
        Integer FarmIconNumbers[] = new Integer[] {};
        Integer GoldMineIconNumbers[] = new Integer[] {};
        //Integer BuildSimpleIconNumbers[] = new Integer[] {38, 42, 40, 44, 46, 60, 91};

        assetActionMap.put("peasant", PeasantIconNumbers);
        assetActionMap.put("footman", FootmanIconNumbers);
        assetActionMap.put("archer", RangedUnitIconNumbers);
        assetActionMap.put("ranger", RangedUnitIconNumbers);
        assetActionMap.put("barracks", BarracksIconNumbers);
        assetActionMap.put("town_hall", TownHallIconNumbers);
        assetActionMap.put("blacksmith", BlacksmithIconNumbers);
        assetActionMap.put("cannon_tower", CannonTowerIconNumbers);
        assetActionMap.put("guard_tower", GuardTowerIconNumbers);
        assetActionMap.put("scout_tower", ScoutTowerIconNumbers);
        assetActionMap.put("keep", KeepIconNumbers);
        assetActionMap.put("castle", CastleIconNumbers);
        assetActionMap.put("lumber_mill", LumberMillIconNumbers);
        assetActionMap.put("farm", FarmIconNumbers);
        assetActionMap.put("gold_mine", GoldMineIconNumbers);
        //assetActionMap.put("build-simple", FootmanIconNumbers); //there is no build-simple enum
    }

    public static Integer[] getAssetActionIcons(String assetName) {
        return assetActionMap.get(assetName);
    }

    // Asset Icons
    private static final HashMap<String, Integer> assetImageMap;
    static {
        assetImageMap = new HashMap<String, Integer>();
        Integer PeasantImage = new Integer(0);
        Integer FootmanImage = new Integer(2);
        Integer ArcherImage = new Integer(4);
        Integer RangerImage = new Integer(6);
        Integer BarracksImage = new Integer(42);
        Integer TownHallImage = new Integer(40);
        Integer BlacksmithImage = new Integer(46);
        Integer CannonTowerImage = new Integer(76);
        Integer GuardTowerImage = new Integer(75);
        Integer ScoutTowerImage = new Integer(60);
        Integer KeepImage = new Integer(66);
        Integer CastleImage = new Integer(68);
        Integer LumberMillImage = new Integer(44);
        Integer FarmImage = new Integer(38);
        Integer GoldMineImage = new Integer(74);

        assetImageMap.put("peasant", PeasantImage);
        assetImageMap.put("footman", FootmanImage);
        assetImageMap.put("archer", ArcherImage);
        assetImageMap.put("ranger", RangerImage);
        assetImageMap.put("barracks", BarracksImage);
        assetImageMap.put("town_hall", TownHallImage);
        assetImageMap.put("blacksmith", BarracksImage);
        assetImageMap.put("cannon_tower", CannonTowerImage);
        assetImageMap.put("guard_tower", GuardTowerImage);
        assetImageMap.put("scout_tower", ScoutTowerImage);
        assetImageMap.put("keep", KeepImage);
        assetImageMap.put("castle", CastleImage);
        assetImageMap.put("lumber_mill", LumberMillImage);
        assetImageMap.put("farm", FarmImage);
        assetImageMap.put("gold_mine", GoldMineImage);
    }

    public static Integer getAssetImageIcons(String assetName) {
        return assetImageMap.get(assetName);
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

    public void removeCommand(){
        commands.remove();
        positions.remove();
    }

    public void addCommand(EAssetAction assetAction, CTilePosition pos) {
            commands.add(assetAction);
            positions.add(pos);
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

        //testing TODO:
        if(commands.peek() == EAssetAction.Walk) {
            switch (direction) {
                case North:
                case NorthWest:
                case NorthEast:
                    adjustY -= (steps % 5) * (TileSize / 5);
                    break;
                case South:
                case SouthWest:
                case SouthEast:
                    adjustY += (steps % 5) * (TileSize / 5);
            }
            switch (direction) {
                case West:
                case SouthWest:
                case NorthWest:
                    adjustX -= (steps % 5) * (TileSize / 5);
                    break;
                case East:
                case NorthEast:
                case SouthEast:
                    adjustX += (steps % 5) * (TileSize / 5);
            }
        }//

        Bitmap resizedAssetBitmap = Bitmap.createScaledBitmap(assetBitmap, assetSize, assetSize, true);
        canvas.drawBitmap(resizedAssetBitmap, adjustX, adjustY, null);
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
        type = t;
        owner = o;
        this.x = x;
        this.y = y;
        direction = EDirection.South;
        commands = new LinkedList<>();
        positions = new LinkedList<>();
        color = PlayerData.PlayerColor.Red;
        assetData = AssetTypeData.getAssetData(type);
        building = null;
        HP = 0; //if not made on game start (from map file), needs to be constructed
        //HP = assetData.hitPoints;
        if(isBuilding()){
            visible = false;
        }
    }

    Asset(String input[]) {
        type = EAssetType.valueOf(input[0]);
        owner = Integer.valueOf(input[1]);
        x = Integer.valueOf(input[2]);
        y = Integer.valueOf(input[3]);
        direction = EDirection.South;
        commands = new LinkedList<>();
        positions = new LinkedList<>();
        color = PlayerData.PlayerColor.Red;
        assetData = AssetTypeData.getAssetData(type);
        building = null;
        HP = assetData.hitPoints;
    }
}
