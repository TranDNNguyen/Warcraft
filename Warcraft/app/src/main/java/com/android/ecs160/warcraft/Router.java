package com.android.ecs160.warcraft;


import java.util.ArrayList;
import java.util.Vector;

//NOTE: In the linux code this is called "RouterMap", and is in charge of pathfinding
public class Router {

    //Vector< Vector< Integer > > DMap;
    ArrayList<ArrayList<Integer>> DMap;

    static final Integer SEARCH_STATUS_UNVISITED = -1;
    static final Integer SEARCH_STATUS_VISITED = -2;
    static final Integer SEARCH_STATUS_OCCUPIED = -3;

    static int DMapWidth;
    Asset.EDirection DIdealSearchDirection = Asset.EDirection.North;
    AssetRenderer assetRenderer;

    public Router(AssetRenderer assetRenderer) {
        int mapWidth = MapTiles.getMapWidth();
        int mapHeight = MapTiles.getMapHeight();
        this.assetRenderer = assetRenderer;

        DMap = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> temp = new ArrayList<Integer>();

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                temp.add(0);
            }
            DMap.add(temp);
        }
    }

    //takes in asset and it's new coordinates
    public static Asset.EDirection calcDirection(Asset asset, int destX, int destY) {
        int assetX = asset.x;
        int assetY = asset.y;
        //int currentDir = 0; // = asset.direction;
        Asset.EDirection currentDir = Asset.EDirection.Max;
        int deltaX, deltaY;
        boolean right = false;
        boolean left = false;
        boolean up = false;
        boolean down = false;
        //asset.x2 = destX;
        //asset.y2 = destY;

        deltaX = destX - assetX;
        deltaY = assetY - destY;
        //calc left or right
        if (deltaX < 0) {
            left = true;
        } else if (deltaX > 0) {
            right = true;
        }
        //calc up or down
        if (deltaY < 0) {
            down = true;
        } else if (deltaY > 0) {
            up = true;
        }
        //change global direction variable accordingly
        if (right == true) {
            if (up == true) {
                currentDir = Asset.EDirection.NorthEast;
            } else if (down = true) {
                currentDir = Asset.EDirection.SouthEast;
            } else {
                currentDir = Asset.EDirection.East;
            }
        } else if (left == true) {
            if (up == true) {
                currentDir = Asset.EDirection.NorthWest;
            } else if (down = true) {
                currentDir = Asset.EDirection.SouthWest;
            } else {
                currentDir = Asset.EDirection.West;
            }
        } else if (up == true) {
            currentDir = Asset.EDirection.North;
        } else if (down = true) {
            currentDir = Asset.EDirection.South;
        }

        return currentDir;
    }

    protected class SSearchTarget {
        int DX;
        int DY;
        int DSteps;
        MapTiles.ETerrainTileType DTileType;
        int DTargetDistanceSquared;
        Asset.EDirection DInDirection;
    }

    protected class CTilePosition {
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


    boolean MovingAway(Asset.EDirection dir1, int dir2) {
    //boolean MovingAway(Asset.EDirection dir1, Asset.EDirection dir2) {
        int Value;
        //if ((0 > dir2.getIdx()) || (Asset.EDirection.Max.getIdx()) <= dir2.getIdx()) {
        if ((0 > dir2) || (Asset.EDirection.Max.getIdx()) <= dir2) {
            return false;
        }
        //Value = ((Asset.EDirection.Max.getIdx() + dir2.getIdx()) - dir1.getIdx()) % Asset.EDirection.Max.getIdx();
        Value = ((Asset.EDirection.Max.getIdx() + dir2) - dir1.getIdx()) % Asset.EDirection.Max.getIdx();
        if ((1 >= Value) || (Asset.EDirection.Max.getIdx() - 1 <= Value)) {
            return true;
        }
        return false;
    }

    //TODO: DEBUG

    //TODO should take asset, asset target, and asset decorated map
    //for now I will treat the map as flat and totally traversable.
    public Asset.EDirection FindPath(Vector<Vector<MapTiles.ETerrainTileType>> terrainMap,
                                     Asset asset, int destX, int destY) {
        
        int MapWidth = MapTiles.getMapWidth();
        int MapHeight = MapTiles.getMapHeight();
        int StartX = asset.x;
        int StartY = asset.y;

        SSearchTarget CurrentSearch = new SSearchTarget(),
                BestSearch = new SSearchTarget(),
                TempSearch = new SSearchTarget();
        CTilePosition CurrentTile, // = new CTilePosition(),
                TargetTile, // = new CTilePosition(),
                TempTile = new CTilePosition(0, 0);

        Asset.EDirection SearchDirecitons[] = {Asset.EDirection.North, Asset.EDirection.East, Asset.EDirection.South, Asset.EDirection.West};
        int ResMapXOffsets[] = {0, 1, 0, -1};
        int ResMapYOffsets[] = {-1, 0, 1, 0};
        int DiagCheckXOffset[] = {0, 1, 1, 1, 0, -1, -1, -1};
        int DiagCheckYOffset[] = {-1, -1, 0, 1, 1, 1, 0, -1};
        int SearchDirectionCount = SearchDirecitons.length; //TODO debug

        // / Asset.EDirection.Max.getIdx();
        Asset.EDirection LastInDirection, DirectionBeforeLast;
        //Queue<SSearchTarget> SearchQueue ;
        Vector<SSearchTarget> SearchQueue = new Vector<>();

        //TargetTile.x = destX;
        //TargetTile.y = destY;
        TargetTile = new CTilePosition(destX, destY);
        if ((DMap.size() != MapHeight + 2) || (DMap.get(0).size() != MapWidth + 2)) {
            int LastYIndex = MapHeight + 1;
            int LastXIndex = MapWidth + 1;
            DMap.ensureCapacity(MapHeight + 2);
            //for (ArrayList<Integer> Row : DMap) {
            //Row.ensureCapacity(MapWidth + 2);
            ArrayList temp = new ArrayList();
            for (int i = 0; i < MapWidth; i++) {
                DMap.get(i).add(0);
                DMap.get(i).add(0, 0);
            }
            for (int i = 0; i < MapWidth + 2; i++) {
                temp.add(0);
            }
            DMap.add(temp);
            for (int i = 0; i < MapWidth + 2; i++) {
                temp.add(0);
            }
            DMap.add(0, temp);

            //}
            for (int Index = 0; Index < DMap.size(); Index++) {
                DMap.get(Index).set(0, SEARCH_STATUS_VISITED);
                DMap.get(Index).set(LastXIndex, SEARCH_STATUS_VISITED);
            }
            for (int Index = 0; Index < MapWidth; Index++) {
                DMap.get(0).set(Index + 1, SEARCH_STATUS_VISITED);
                DMap.get(LastYIndex).set(Index + 1, SEARCH_STATUS_VISITED);
            }
            DMapWidth = MapWidth + 2;
        }

        if (asset.x == destX && asset.y == destY) {
            return calcDirection(asset, destX, destY);
        }

        for (int Y = 0; Y < MapHeight; Y++) {
            for (int X = 0; X < MapWidth; X++) {
                DMap.get(Y + 1).set(X + 1, SEARCH_STATUS_UNVISITED);
            }
        }

        // const std::list< std::shared_ptr< CPlayerAsset > > &Assets() const;

        //for(auto &Res : resmap.Assets()){
        for (Asset Res : assetRenderer.assets) {

            if (asset != Res) {
                if (Asset.EAssetType.None != Res.type) {
                    if ((Asset.EAssetAction.Walk != Res.action) || (asset.color != Res.color)) {
                        if ((asset.color != Res.color) || ((Asset.EAssetAction.ConveyGold != Res.action) && (Asset.EAssetAction.ConveyLumber != Res.action) && (Asset.EAssetAction.MineGold != Res.action))) {
                            for (int YOff = 0; YOff < Res.size; YOff++) {
                                for (int XOff = 0; XOff < Res.size; XOff++) {
                                    DMap.get(Res.y + YOff + 1).set(Res.x + XOff + 1, SEARCH_STATUS_VISITED);
                                }
                            }
                        }
                    } else {
                        DMap.get(Res.y + 1).set(Res.x + 1, SEARCH_STATUS_OCCUPIED - Res.direction.getIdx());
                    }
                }
            }
        }

        DIdealSearchDirection = asset.direction;
        CurrentTile = new CTilePosition(asset.x, asset.y);
        CurrentSearch.DX = BestSearch.DX = CurrentTile.x;
        CurrentSearch.DY = BestSearch.DY = CurrentTile.y;
        CurrentSearch.DSteps = 0;
        CurrentSearch.DTargetDistanceSquared = BestSearch.DTargetDistanceSquared = CurrentTile.DistanceSquared(TargetTile);
        CurrentSearch.DInDirection = BestSearch.DInDirection = Asset.EDirection.Max;
        DMap.get(StartY + 1).set(StartX + 1, SEARCH_STATUS_VISITED);

        while (true) {
            if (CurrentTile.x == TargetTile.x && CurrentTile.y == TargetTile.y) {
                BestSearch = CurrentSearch;
                break;
            }
            if (CurrentSearch.DTargetDistanceSquared < BestSearch.DTargetDistanceSquared) {
                BestSearch = CurrentSearch;
            }
            for (int Index = 0; Index < SearchDirectionCount; Index++) {
                TempTile.x = CurrentSearch.DX + ResMapXOffsets[Index];
                TempTile.y = CurrentSearch.DY + ResMapYOffsets[Index];
                if ((SEARCH_STATUS_UNVISITED == DMap.get(TempTile.y + 1).get(TempTile.x + 1)
                        || MovingAway(SearchDirecitons[Index], SEARCH_STATUS_OCCUPIED - DMap.get(TempTile.y + 1).get(TempTile.x + 1)))) {
                        //|| MovingAway(SearchDirecitons[Index], Asset.EDirection.values()[SEARCH_STATUS_OCCUPIED - DMap.get(TempTile.y + 1).get(TempTile.x + 1)]))) {



                    //if((SEARCH_STATUS_UNVISITED == DMap[TempTile.Y() + 1][TempTile.X() + 1])||MovingAway(SearchDirecitons[Index], (EDirection)(SEARCH_STATUS_OCCUPIED - DMap[TempTile.Y() + 1][TempTile.X() + 1]))){

                    DMap.get(TempTile.y + 1).set(TempTile.x + 1, Index);
                    MapTiles.ETerrainTileType CurTileType = terrainMap.get(TempTile.x).get(TempTile.y); //resmap.TileType(TempTile.X(), TempTile.Y());
                    //if((CTerrainMap::ETileType::Grass == CurTileType)||(CTerrainMap::ETileType::Dirt == CurTileType)||(CTerrainMap::ETileType::Stump == CurTileType)||(CTerrainMap::ETileType::Rubble == CurTileType)||(CTerrainMap::ETileType::None == CurTileType)){
                    //if(terrainMap.IsTraversable(CurTileType)){
                    if (MapTiles.isTraversable(CurTileType)) {
                        TempSearch.DX = TempTile.x;
                        TempSearch.DY = TempTile.y;
                        TempSearch.DSteps = CurrentSearch.DSteps + 1;
                        TempSearch.DTileType = CurTileType;
                        TempSearch.DTargetDistanceSquared = TempTile.DistanceSquared(TargetTile);
                        TempSearch.DInDirection = SearchDirecitons[Index];
                        SearchQueue.add(TempSearch);
                    }
                }
            }
            if (SearchQueue.isEmpty()) {
                break;
            }
            CurrentSearch = SearchQueue.remove(0);
            //SearchQueue.pop();
            CurrentTile.x = CurrentSearch.DX;
            CurrentTile.y = CurrentSearch.DY;
        }


        DirectionBeforeLast = LastInDirection = BestSearch.DInDirection;
        CurrentTile.x = BestSearch.DX;
        CurrentTile.y = BestSearch.DY;
        while ((CurrentTile.x != StartX) || (CurrentTile.y != StartY)) {
            int Index = DMap.get(CurrentTile.y + 1).get(CurrentTile.x + 1);

            if ((0 > Index) || (SearchDirectionCount <= Index)) {
                System.exit(0);
            }
            DirectionBeforeLast = LastInDirection;
            LastInDirection = SearchDirecitons[Index];
            CurrentTile.x -= ResMapXOffsets[Index];
            CurrentTile.y -= ResMapYOffsets[Index];
        }

        if (DirectionBeforeLast != LastInDirection) {
            //MapTiles.ETileType CurTileType = resmap.TileType(StartX + DiagCheckXOffset[to_underlying(DirectionBeforeLast)], StartY + DiagCheckYOffset[to_underlying(DirectionBeforeLast)]);
            MapTiles.ETerrainTileType CurTileType = terrainMap.get(StartX + DiagCheckXOffset[DirectionBeforeLast.getIdx()]).get(StartY + DiagCheckYOffset[DirectionBeforeLast.getIdx()]);
            //if((CTerrainMap::ETileType::Grass == CurTileType)||(CTerrainMap::ETileType::Dirt == CurTileType)||(CTerrainMap::ETileType::Stump == CurTileType)||(CTerrainMap::ETileType::Rubble == CurTileType)||(CTerrainMap::ETileType::None == CurTileType)){
            if (MapTiles.isTraversable(CurTileType)) {
                int Sum = LastInDirection.getIdx() + DirectionBeforeLast.getIdx();
                if ((6 == Sum) && ((Asset.EDirection.North == LastInDirection) || (Asset.EDirection.North == DirectionBeforeLast))) { // NW wrap around
                    Sum += 8;
                }
                Sum /= 2;
                LastInDirection = Asset.EDirection.values()[Sum];
            }
        }

        return LastInDirection;


        //for now, this is all we need to do, since the whole map is traversable
        //return calcDirection(asset, destX, destY);
    }

}
