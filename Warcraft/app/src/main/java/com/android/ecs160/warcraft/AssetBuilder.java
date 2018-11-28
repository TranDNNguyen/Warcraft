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
        PlayerData currPlayerData = this.playerData.get(player - 1);
        Asset.AssetData newAssetData = Asset.AssetTypeData.getAssetData(newAssetType);

        //Check dependencies for the new asset
        if(!DependenciesMet(newAssetType)){
            return;
        }

        //Check to see if we can afford the new asset
        if(!Purchase(currPlayerData, newAssetData)){
            return;
        }

        //Makes new asset
        Asset newAsset = new Asset(newAssetType, player, position.x, position.y);
        assetRenderer.addAsset(newAsset);

        //Assign commands to the builder
        BuildCommands(builder, newAsset, position);
    }

    /*
     * This function determines whether the player has satisfied the prerequisites for
     * making the specified new asset. Returns false if they cannon yet make this asset.
     */
    private boolean DependenciesMet(Asset.EAssetType newAssetType){
        //TODO

        return true;
    }

    /*
     * This function is used to spend gathered resources in order to build a new unit
     * or building. If the purchase is successful, we return true. If the player cannot
     * yet afford this asset, we return false.
     * currPlayerData: contains the players resources used to make the purchase
     * newAssetData: contains the price of a new asset of the requested type
     */
    private boolean Purchase(PlayerData currPlayerData, Asset.AssetData newAssetData){
        //Check to see if we can afford the new asset
        if(newAssetData.lumberCost > currPlayerData.DLumber ||
                newAssetData.goldCost > currPlayerData.DGold){
            return false;
        }//can't afford this new asset
        else{
            currPlayerData.DLumber -= newAssetData.lumberCost;
            currPlayerData.DGold -= newAssetData.goldCost;
            return true;
        }//we can - make the purchase
    }

    /*
     * After the new asset is created, it still needs to be "built". In other words,
     * the asset playing the role of the builder needs to spend time during the game
     * performing the "build" action in order to fill the hitPoints of the new Asset.
     * The new asset cannot fully function until it is fully "built" by the builder.
     * This function assigns these build commands to the builder.
     */
    private void BuildCommands(Asset builder, Asset newAsset, CTilePosition position){
        if(builder.isBuilding()){
            //TODO
        }else if(builder.type == Asset.EAssetType.Peasant){
            builder.addCommand(Asset.EAssetAction.Walk, position);
            builder.addCommand(Asset.EAssetAction.Build, position);
            builder.building = newAsset;
        }
    }
}
