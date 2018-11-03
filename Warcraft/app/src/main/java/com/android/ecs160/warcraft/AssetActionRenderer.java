package com.android.ecs160.warcraft;

import java.util.Vector;

public class AssetActionRenderer {

    AssetRenderer assetRenderer;
    Router router;
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
            switch (asset.action) {
                case None:
                    continue;
                case Walk:
                    Walk(asset);
            }
        }
    }

    void Walk(Asset asset) {
        Asset.EDirection travelDirection;
        travelDirection = router.FindPath(terrainMap, asset, asset.x2, asset.y2);
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

        assetRenderer.updateAssetFrame(asset);

        if (asset.x == asset.x2 && asset.y == asset.y2) {
            asset.action = Asset.EAssetAction.None;
        }
    }


}
