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
            if(asset.commands.isEmpty()){
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
    }



    void HarvestLumber(Asset asset){
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
        //travelDirection = router.FindPath(terrainMap, asset, asset.xs.peek(), asset.xs.peek());
        travelDirection = router.FindPath(terrainMap, asset, asset.positions.peek());
        asset.direction = travelDirection;

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

        if (Arrived(asset)) {
            asset.removeCommand();
            //asset.action = Asset.EAssetAction.None;
        }
    }

    public boolean Arrived(Asset asset){
        MapTiles mapTiles = MapTiles.getMapTilesInstance();
        MapTiles.ETerrainTileType tileType = mapTiles.getTileType(asset.x, asset.y);

        if(asset.x == asset.positions.peek().x && asset.y == asset.positions.peek().y){
            return true;
        }else if(!mapTiles.isTraversable(tileType)){
            return true;
        }//TODO:we've actually gone too far by this point. need to update logic

        return false;
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