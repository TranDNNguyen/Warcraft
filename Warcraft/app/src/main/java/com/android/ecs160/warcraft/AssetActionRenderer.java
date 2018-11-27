package com.android.ecs160.warcraft;

import java.util.Vector;

public class AssetActionRenderer {

    AssetRenderer assetRenderer;
    public static Router router;
    Vector<Vector<MapTiles.ETerrainTileType>> terrainMap;
    MapTiles mapTiles;

    public AssetActionRenderer(AssetRenderer assetRenderer, MapRenderer mapRenderer){
        this.assetRenderer = assetRenderer;
        router = new Router(assetRenderer);
        terrainMap = mapRenderer.mapTiles.terrainMap;
        mapTiles = mapRenderer.mapTiles;
    }

    public void TimeStep(Vector<Asset> assets) {
        for (Asset asset : assets) {
            if(asset == null){
                continue;
            }
            if(asset.commands.isEmpty()){
                assetRenderer.updateAssetFrame(asset);
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
            }
            assetRenderer.updateAssetFrame(asset);
        }
    }


    public static void findCommand(Asset asset, int x, int y){
        //get tile type of (x,y)
        MapTiles mapTiles = MapTiles.getMapTilesInstance();
        MapTiles.ETerrainTileType tileType = mapTiles.getTileType(x, y);
        CTilePosition pos = new CTilePosition(x, y);

        //see if traversable
        if(mapTiles.isTraversable(tileType)){
            //set to walk
            asset.addCommand(Asset.EAssetAction.Walk, pos);
        }else{
            //find closest forest tile
            CTilePosition newpos = router.FindNearestReachableTileType(pos, MapTiles.ETerrainTileType.Forest);

            if(asset.type ==  Asset.EAssetType.Peasant && tileType == MapTiles.ETerrainTileType.Forest){
                asset.addCommand(Asset.EAssetAction.Walk, newpos); //will walk as close as it can to the rigth tile
                asset.addCommand(Asset.EAssetAction.HarvestLumber, newpos);
            }
            //TODO: add other mining
        }
    }//used when asset is selected and then given a command by touching elsewhere on the map
    //either move, move and then harvest/mine, or move to attack enemy

    void Build(Asset asset){
        //by now the lumber/basic building has been placed, and asset has already moved to it.
        if(asset.steps == 0){
            asset.visible = false;
        }//asset has just started building

        asset.steps++;

        //TODO: update HP continuously
        //change checks to be based off HP (could be attacked while being built,
        //so don't want to use steps alone.
        //but: cant increment by totalHP/buildTime because some units might have
        //buildTime > HP

        if(asset.building == null){
            System.exit(0);
        }

        if(asset.steps == asset.building.assetData.buildTime / 2){
            asset.building.HP = asset.building.assetData.hitPoints /2;
        }//halfway through building, adjust so display can too

        //if(asset.building.HP >= asset.building.assetData.hitPoints){
        if(asset.steps >= asset.building.assetData.buildTime){ //TODO: mod by update frequency?
            asset.visible = true;
            asset.removeCommand();
            asset.steps = 0;
            asset.building.HP = asset.building.assetData.hitPoints;
            asset.building = null;
        }//asset has finished building
    }

    void HarvestLumber(Asset asset){
        //
        /*if(asset.steps == 0){
            Walk(asset);
            if (Arrived(asset)) {
                asset.steps++;
            }
        }//have not reached lumber yet
        */
        if(asset.steps >= GameModel.DHarvestSteps){
            asset.lumber = (GameModel.DLumberPerHarvest);
            asset.removeCommand();
            asset.steps = 0;

            //TODO: remove, testing only
            CTilePosition pos = new CTilePosition(15, 15);
            asset.addCommand(Asset.EAssetAction.Walk, pos);

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




/*

    void CGameModel::THarvestLumber(std::shared_ptr < CPlayerAsset > Asset, std::vector < SGameEvent > & CurrentEvents){

        SGameEvent TempEvent;
        SAssetCommand Command = Asset->CurrentCommand();
        CTilePosition TilePosition = Command.DAssetTarget->TilePosition();
        EDirection HarvestDirection = Asset->TilePosition().AdjacentTileDirection(TilePosition);

        if(CTerrainMap::ETileType::Forest != DActualMap->TileType(TilePosition)){
            HarvestDirection = EDirection::Max;
            TilePosition = Asset->TilePosition();
        }

        //if front tile is not a forest tile
        if(EDirection::Max == HarvestDirection){
            if(TilePosition == Asset->TilePosition()){
                CTilePosition TilePosition = DPlayers[to_underlying(Asset->Color())]->PlayerMap()->FindNearestReachableTileType(Asset->TilePosition(), CTerrainMap::ETileType::Forest);
                // Find new lumber
                Asset->PopCommand();
                if(0 <= TilePosition.X()){
                    CPixelPosition NewPosition;
                    NewPosition.SetFromTile(TilePosition);
                    Command.DAssetTarget = DPlayers[to_underlying(Asset->Color())]->CreateMarker(NewPosition, false);
                    Asset->PushCommand(Command);
                    Command.DAction = EAssetAction::Walk;
                    Asset->PushCommand(Command);
                    Asset->ResetStep();
                }
            }
            else{
                SAssetCommand NewCommand = Command;

                NewCommand.DAction = EAssetAction::Walk;
                Asset->PushCommand(NewCommand);
                Asset->ResetStep();
            }
        }
        else{ //is a forest tile
            TempEvent.DType = EEventType::Harvest;
            TempEvent.DAsset = Asset;
            CurrentEvents.push_back(TempEvent);
            Asset->Direction(HarvestDirection);
            Asset->IncrementStep();
            if(DHarvestSteps <= Asset->Step()){
                std::weak_ptr< CPlayerAsset > NearestRepository = DPlayers[to_underlying(Asset->Color())]->FindNearestOwnedAsset(Asset->Position(), {EAssetType::TownHall, EAssetType::Keep, EAssetType::Castle, EAssetType::LumberMill});

                DActualMap->RemoveLumber(TilePosition, Asset->TilePosition(), DLumberPerHarvest);

                if(!NearestRepository.expired()){
                    Command.DAction = EAssetAction::ConveyLumber;
                    Command.DAssetTarget = NearestRepository.lock();
                    Asset->PushCommand(Command);
                    Command.DAction = EAssetAction::Walk;
                    Asset->PushCommand(Command);
                    Asset->Lumber(DLumberPerHarvest);
                    Asset->ResetStep();
                }
                else{
                    Asset->PopCommand();
                    Asset->Lumber(DLumberPerHarvest);
                    Asset->ResetStep();
                }
            }
        }
    }
 */