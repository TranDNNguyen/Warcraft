package com.android.ecs160.warcraft;

import java.util.Iterator;
import java.util.Vector;

public class AssetActionRenderer {

    AssetRenderer assetRenderer;
    public static Router router;
    Vector<Vector<MapTiles.ETerrainTileType>> terrainMap;
    MapTiles mapTiles;
    int updateFrequency;
    Vector<PlayerData> players;

    public AssetActionRenderer(AssetRenderer assetRenderer, MapRenderer mapRenderer, int frequency, Vector<PlayerData> players){
        this.assetRenderer = assetRenderer;
        this.players = players;
        router = new Router(assetRenderer);
        terrainMap = mapRenderer.mapTiles.terrainMap;
        mapTiles = mapRenderer.mapTiles;
        updateFrequency = frequency;
    }

    public void TimeStep(Vector<Asset> assets) {
        LockManager.assetLock.lock();
        try {
            Iterator<Asset> it = assets.iterator();
            Asset asset;
            //for (Asset asset : assets) {
            while(it.hasNext()){
                asset = it.next();
                if (asset == null) {
                    continue;
                }
                if(asset.HP <= 0){
                    it.remove();
                    //TODO: replace with corpse
                }//asset has died.
                if (asset.commands.isEmpty()) {
                    //assetRenderer.updateAssetFrame(asset);
                    continue;
                }
                switch (asset.commands.peek()) {
                    case None:
                        continue;
                    case Walk:
                        Walk(asset);
                        break;
                    case HarvestLumber:
                        HarvestLumber(asset);
                        break;
                    case Build:
                        Build(asset);
                        break;
                    case MineGold:
                        MineGold(asset);
                        break;
                    case Attack:
                        Attack(asset);
                        break;
                    case ConveyGold:
                        ConveyGold(asset);
                        break;
                    case ConveyLumber:
                        ConveyLumber(asset);
                        break;
                }
                assetRenderer.updateAssetFrame(asset);
            }
        }finally {
            LockManager.assetLock.unlock();
        }
    }
    public static void findCommand(Asset asset, int x, int y, Asset destAsset){
        //get tile type of (x,y)
        MapTiles mapTiles = MapTiles.getMapTilesInstance();
        MapTiles.ETerrainTileType tileType = mapTiles.getTileType(x, y);
        CTilePosition pos = new CTilePosition(x, y);

        if(destAsset != null){
            if(destAsset.type == Asset.EAssetType.GoldMine){
                asset.addCommand(Asset.EAssetAction.Walk, pos);
                asset.addCommand(Asset.EAssetAction.MineGold, pos);
                asset.building = destAsset;
            }
            else if(destAsset.owner != asset.owner){
                //TODO:refine attacking
                asset.addCommand(Asset.EAssetAction.Walk, pos);
                asset.addCommand(Asset.EAssetAction.Attack, pos);
            }//enemy...ATTACK!!!
        }//
        else if(mapTiles.isTraversable(tileType)){
            //set to walk
            asset.addCommand(Asset.EAssetAction.Walk, pos);
        }//
        else if (asset.type ==  Asset.EAssetType.Peasant && tileType == MapTiles.ETerrainTileType.Forest){
            //find closest forest tile
            CTilePosition newpos = router.FindNearestReachableTileType(pos, MapTiles.ETerrainTileType.Forest);
            asset.addCommand(Asset.EAssetAction.Walk, newpos); //will walk as close as it can to the rigth tile
            asset.addCommand(Asset.EAssetAction.HarvestLumber, newpos);
        }//
    }//used when asset is selected and then given a command by touching elsewhere on the map
    //either move, move and then harvest/mine, or move to attack enemy

    void Build(Asset asset){
        //by now the lumber/basic building has been placed, and asset has already moved to it.
        if(asset.building == null){
            System.exit(0);
        }

        double hitPoints = (double)asset.building.assetData.hitPoints;
        double buildTime = (double)asset.building.assetData.buildTime;

        if(asset.steps == 0 && asset.type == Asset.EAssetType.Peasant){
            asset.visible = false;
            asset.building.visible = true;
        }//peasant has just started building

        asset.building.HP += hitPoints / buildTime;
        assetRenderer.updateAssetFrame(asset.building);

        if (asset.building.HP >= hitPoints) {
            asset.building.HP = asset.building.assetData.hitPoints;

            asset.removeCommand();
            asset.building.visible = true;
            asset.building = null;
            asset.visible = true;
            asset.steps = 0;
            return;
        }//asset has finished building

        asset.steps++;
    }

    void Attack(Asset asset){
        CTilePosition pos = new CTilePosition(asset.x, asset.y);
        Asset enemy = assetRenderer.getNearbyEnemyAsset(asset);

        //TODO: update asset to face the asset it is attacking

        if(enemy == null){
            asset.steps = 0;
            asset.removeCommand();
            return;
        }//no enemies within view. stop attacking

        double distance = Math.hypot(asset.x-enemy.x, asset.y-enemy.y);
        if(distance > asset.assetData.range){

        }//TODO: move close to enemy if within sight but out of range

        if(asset.steps % asset.assetData.attackSteps == 0){
            enemy.HP -= asset.assetData.basicDamage;
        }//hit the

        asset.steps++;
    }

    void HarvestLumber(Asset asset){
        if(asset.steps >= GameModel.DHarvestSteps){
            asset.lumber = (GameModel.DLumberPerHarvest);
            asset.removeCommand();
            asset.steps = 0;

            Asset townHall = assetRenderer.getNearestTownHall(asset);
            if(townHall != null){
                CTilePosition pos = new CTilePosition(townHall.x, townHall.y);
                asset.addCommand(Asset.EAssetAction.Walk, pos);
                asset.addCommand(Asset.EAssetAction.ConveyLumber, pos);
                asset.building = townHall;
            }
        }else{
            asset.steps++;
        }
    }

    void ConveyLumber(Asset asset){
        if(asset.steps == 0){
            asset.visible = false;
        }
        if(asset.steps >= GameModel.DConveySteps){
            players.get(asset.owner-1).DLumber += asset.lumber;
            asset.lumber = 0;
            asset.removeCommand();
            asset.steps = 0;
            asset.visible = true;

            CTilePosition pos = new CTilePosition(asset.x, asset.y);
            pos = router.FindNearestReachableTileType(pos, MapTiles.ETerrainTileType.Forest);
            asset.addCommand(Asset.EAssetAction.Walk, pos);
            asset.addCommand(Asset.EAssetAction.HarvestLumber, pos);
        }else{
            asset.steps++;
        }
    }

    void MineGold(Asset asset){
        if(asset.steps == 0){
            asset.visible = false;
        }//asset has just started building

        asset.steps++;

        if(asset.steps >= GameModel.DMineSteps){
            asset.visible = true;
            asset.gold = (GameModel.DGoldPerMining);
            asset.removeCommand();
            asset.steps = 0;

            Asset townHall = assetRenderer.getNearestTownHall(asset);
            if(townHall != null){
                CTilePosition pos = new CTilePosition(townHall.x, townHall.y);
                asset.addCommand(Asset.EAssetAction.Walk, pos);
                asset.addCommand(Asset.EAssetAction.ConveyGold, pos);
                //asset.building = townHall;
            }
        }
    }

    void ConveyGold(Asset asset){
        if(asset.steps == 0){
            asset.visible = false;
        }
        if(asset.steps >= GameModel.DConveySteps){
            players.get(asset.owner-1).DGold += asset.gold;
            asset.gold = 0;
            asset.removeCommand();
            asset.steps = 0;
            asset.visible = true;

            if(asset.building != null) {
                CTilePosition pos = new CTilePosition(asset.building.x, asset.building.y);
                asset.addCommand(Asset.EAssetAction.Walk, pos);
                asset.addCommand(Asset.EAssetAction.MineGold, pos);
            }
        }else{
            asset.steps++;
        }
    }

    void Walk(Asset asset) {
        Asset.EDirection travelDirection;
        travelDirection = router.FindPath(terrainMap, asset, asset.positions.peek());

        if(travelDirection == Asset.EDirection.Max){
            asset.removeCommand();
            asset.steps = 0;
            return;
        }//asset cannot move further, as pathfinding is no longer giving useful directions
        else if (Arrived(asset)) {
            asset.removeCommand();
            asset.steps = 0;
            return;
        }

        asset.direction = travelDirection;
        asset.steps++;

        //testing
        if(asset.steps % 5 != 0){
            return;
        }

        switch (travelDirection) {
            case North:
            case NorthWest:
            case NorthEast:
                asset.y--;
                break;
            case South:
            case SouthWest:
            case SouthEast:
                asset.y++;
        }
        switch (travelDirection) {
            case West:
            case SouthWest:
            case NorthWest:
                asset.x--;
                break;
            case East:
            case NorthEast:
            case SouthEast:
                asset.x++;
        }
    }

    public boolean Arrived(Asset asset){
        MapTiles mapTiles = MapTiles.getMapTilesInstance();
        MapTiles.ETerrainTileType tileType = mapTiles.getTileType(asset.x, asset.y);

        if(asset.x == asset.positions.peek().x && asset.y == asset.positions.peek().y){
            return true;
        }else if(!mapTiles.isTraversable(tileType)){
 //           return true;
        }//TODO:we've actually gone too far by this point. need to update logic

        CTilePosition nextTile = getNextTile(asset);
        tileType = mapTiles.getTileType(asset.x, asset.y);

        if(!mapTiles.isTraversable(tileType) && nextTile == asset.positions.peek()){
            return true;
        }//we have reached a mining destination

        return false;
    }

    public CTilePosition getNextTile(Asset asset){
        int tempx = asset.x;
        int tempy = asset.y;

        switch (asset.direction) {
            case North:
            case NorthWest:
            case NorthEast:
                tempy--;
                break;
            case South:
            case SouthWest:
            case SouthEast:
                tempy++;
        }
        switch (asset.direction) {
            case West:
            case SouthWest:
            case NorthWest:
                tempx--;
                break;
            case East:
            case NorthEast:
            case SouthEast:
                tempx++;
        }

        return new CTilePosition(tempx, tempy);
    }
}
