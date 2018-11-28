package com.android.ecs160.warcraft;

import java.util.Vector;

public class AssetBuilder {

    AssetRenderer assetRenderer;
    Vector<PlayerData> playerData;

    AssetBuilder(AssetRenderer assetRenderer, Vector<PlayerData> playerData){
        this.assetRenderer = assetRenderer;
        this.playerData = playerData;
    }

    /*
     * Call this function to build a new Asset
     * builder: the selected asset that will be performing (either a building or a peasant)
     * newAsset Type: the type of the asset user is requesting to build
     * position: the location selected for the new asset (if a building)
     */
    public void Build(Asset builder, Asset.EAssetType newAssetType, CTilePosition position){

        int player = builder.owner;
        PlayerData playerData = this.playerData.get(player - 1);
        Asset.AssetData newAssetData = Asset.AssetTypeData.getAssetData(newAssetType);

        //TODO: add prerequisite checks

        //Check to see if we can afford the new asset
        if(newAssetData.lumberCost > playerData.DLumber ||
                newAssetData.goldCost > playerData.DGold){
            return;
        }//can't afford this new asset
        else{
            playerData.DLumber -= newAssetData.lumberCost;
            playerData.DGold -= newAssetData.goldCost;
        }//we can - make the purchase

        //Makes new asset
        Asset newAsset = new Asset(newAssetType, player, position.x, position.y);
        assetRenderer.addAsset(newAsset);

        //Assign commands to the builder
        if(builder.isBuilding()){
            //TODO
        }else if(builder.type == Asset.EAssetType.Peasant){
            builder.addCommand(Asset.EAssetAction.Walk, position);
            builder.addCommand(Asset.EAssetAction.Build, position);
            builder.building = newAsset;
        }

    }

}
