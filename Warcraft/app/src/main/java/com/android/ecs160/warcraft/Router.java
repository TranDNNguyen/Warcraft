package com.android.ecs160.warcraft;

import java.util.ArrayList;
import java.util.Vector;

//NOTE: In the linux code this is called "RouterMap", and is in charge of pathfinding
public class Router {

    static boolean DEBUG = true;
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
        ArrayList<Integer> temp;

        for (int i = 0; i < mapWidth + 2; i++) {
            temp = new ArrayList<Integer>();
            for (int j = 0; j < mapHeight + 2; j++) {
                temp.add(0);
            }
            DMap.add(temp);
        }
    }

    //takes in asset and it's new coordinates
    public static Asset.EDirection calcDirection(Asset asset, int destX, int destY) {
        int assetX = asset.x;
        int assetY = asset.y;
        Asset.EDirection currentDir = Asset.EDirection.Max;
        int deltaX, deltaY;
        boolean right = false;
        boolean left = false;
        boolean up = false;
        boolean down = false;

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
            } else if (down == true) {
                currentDir = Asset.EDirection.SouthEast;
            } else {
                currentDir = Asset.EDirection.East;
            }
        } else if (left == true) {
            if (up == true) {
                currentDir = Asset.EDirection.NorthWest;
            } else if (down == true) {
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
        int Value;
        if ((0 > dir2) || (Asset.EDirection.Max.getIdx()) <= dir2) {
            return false;
        }
        Value = ((Asset.EDirection.Max.getIdx() + dir2) - dir1.getIdx()) % Asset.EDirection.Max.getIdx();
        if ((1 >= Value) || (Asset.EDirection.Max.getIdx() - 1 <= Value)) {
            return true;
        }
        return false;
    }

    public Asset.EDirection FindPath(Vector<Vector<MapTiles.ETerrainTileType>> terrainMap,
                                     Asset asset, int destX, int destY) {
        //NOTE: below does works for now, but it ignores traversability.
        //return calcDirection(asset, destX, destY);

        int MapWidth = MapTiles.getMapWidth();
        int MapHeight = MapTiles.getMapHeight();
        int StartX = asset.x;
        int StartY = asset.y;

        SSearchTarget CurrentSearch = new SSearchTarget(),
                BestSearch = new SSearchTarget(),
                TempSearch;
        CTilePosition CurrentTile,
                TargetTile,
                TempTile = new CTilePosition(0, 0);

        Asset.EDirection SearchDirecitons[] = {Asset.EDirection.North, Asset.EDirection.East, Asset.EDirection.South, Asset.EDirection.West};
        int ResMapXOffsets[] = {0, 1, 0, -1};
        int ResMapYOffsets[] = {-1, 0, 1, 0};
        int DiagCheckXOffset[] = {0, 1, 1, 1, 0, -1, -1, -1};
        int DiagCheckYOffset[] = {-1, -1, 0, 1, 1, 1, 0, -1};
        int SearchDirectionCount = SearchDirecitons.length; //TODO debug

        Asset.EDirection LastInDirection, DirectionBeforeLast;
        ArrayList<SSearchTarget> SearchQueue = new ArrayList<>();

        TargetTile = new CTilePosition(destX, destY);
        if ((DMap.size() != MapHeight + 2) || (DMap.get(0).size() != MapWidth + 2)) {
            int LastYIndex = MapHeight + 1;
            int LastXIndex = MapWidth + 1;
            DMap.ensureCapacity(MapHeight + 2);
            ArrayList temp = new ArrayList();
            for (int i = 0; i < MapWidth; i++) {
                DMap.get(i).add(0);
                DMap.get(i).add(0, 0);
            }
            for (int i = 0; i < MapWidth + 2; i++) {
                temp.add(0);
            }
            DMap.add(temp);
            DMap.add(0, temp);

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

        for (Asset Res : assetRenderer.assets) {
            if (asset != Res) {
                if (Asset.EAssetType.None != Res.type) {
                    if ((Asset.EAssetAction.Walk != Res.action) || (asset.color != Res.color)) {
                        if ((asset.color != Res.color) || ((Asset.EAssetAction.ConveyGold != Res.action) && (Asset.EAssetAction.ConveyLumber != Res.action) && (Asset.EAssetAction.MineGold != Res.action))) {
                            for (int YOff = 0; YOff < Res.assetData.size; YOff++) {
                                for (int XOff = 0; XOff < Res.assetData.size; XOff++) {
                                    DMap.get(Res.y + YOff + 1).set(Res.x + XOff + 1, SEARCH_STATUS_VISITED);
                                }
                            }
                        }
                    } else {
                        DMap.get(Res.y + 1).set(Res.x + 1, SEARCH_STATUS_OCCUPIED);
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

                    DMap.get(TempTile.y + 1).set(TempTile.x + 1, Index);
                    MapTiles.ETerrainTileType CurTileType = terrainMap.get(TempTile.x).get(TempTile.y);
                    if (MapTiles.isTraversable(CurTileType)) {
                        TempSearch = new SSearchTarget();
                        TempSearch.DX = TempTile.x;
                        TempSearch.DY = TempTile.y;
                        TempSearch.DSteps = CurrentSearch.DSteps + 1;
                        TempSearch.DTileType = CurTileType;
                        TempSearch.DTargetDistanceSquared = TempTile.DistanceSquared(TargetTile);
                        TempSearch.DInDirection = SearchDirecitons[Index];
                        SearchQueue.add(SearchQueue.size(), TempSearch); //TODO: should add to end...
                        //SearchQueue.add(TempSearch);
                    }
                }
            }
            if (SearchQueue.isEmpty()) {
                break;
            }
            CurrentSearch = SearchQueue.remove(0);
            CurrentTile.x = CurrentSearch.DX;
            CurrentTile.y = CurrentSearch.DY;
        }

        //BestSearch should be set by now
        //Below should unpack the search steps
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

        //TODO:DUBUG: figure out what the below chunk of code actually does
        if (DirectionBeforeLast != LastInDirection) {
            MapTiles.ETerrainTileType CurTileType = terrainMap.get(StartX + DiagCheckXOffset[DirectionBeforeLast.getIdx()]).get(StartY + DiagCheckYOffset[DirectionBeforeLast.getIdx()]);
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
    }
}


