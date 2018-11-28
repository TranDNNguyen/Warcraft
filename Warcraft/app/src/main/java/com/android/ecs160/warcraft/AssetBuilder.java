package com.android.ecs160.warcraft;

public class AssetBuilder {

    AssetRenderer assetRenderer;

    AssetBuilder(AssetRenderer assetRenderer){
        this.assetRenderer = assetRenderer;
    }

    public void Build(Asset builder, Asset.EAssetType newAssetType, CTilePosition position){

        //check if can be built


        //makes new asset
        Asset townHall = new Asset(Asset.EAssetType.Barracks, 1, 16, 16);
        assetRenderer.addAsset(townHall);

        //adds actions for peasant
        if(builder.isBuilding()){

        }else if(builder.type == Asset.EAssetType.Peasant){

        }

        /*
        CTilePosition pos = new CTilePosition(15, 15);
        a.addCommand(Asset.EAssetAction.Walk, pos);
        a.addCommand(Asset.EAssetAction.Build, pos);
        a.building = townHall;
        */


    }

}
