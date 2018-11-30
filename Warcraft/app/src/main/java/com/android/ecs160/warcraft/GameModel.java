package com.android.ecs160.warcraft;

public class GameModel {
    //protected:
    //CRandomNumberGenerator DRandomNumberGenerator;
    //std::shared_ptr< CAssetDecoratedMap > DActualMap;
    //std::vector< std::vector< std::shared_ptr< CPlayerAsset > > > DAssetOccupancyMap;
    //std::vector< std::vector< bool > > DDiagonalOccupancyMap;
    //CRouterMap DRouterMap;
    //std::array< std::shared_ptr< CPlayerData >, to_underlying(EPlayerColor::Max)> DPlayers;
    static int DGameCycle;
    static int DHarvestTime;
    static int DHarvestSteps;
    static int DQuarryTime;
    static int DQuarrySteps;
    static int DMineTime;
    static int DMineSteps;
    static int DConveyTime;
    static int DConveySteps;
    static int DDeathTime;
    static int DDeathSteps;
    static int DDecayTime;
    static int DDecaySteps;
    static int DLumberPerHarvest;
    static int DGoldPerMining;
    static int DStonePerQuarrying;

    GameModel(int updateFrequency){
        DHarvestTime = 5;
        DHarvestSteps = updateFrequency * DHarvestTime;
        DQuarryTime = 5;
        DQuarrySteps = updateFrequency * DQuarryTime;
        DMineTime = 5;
        DMineSteps = updateFrequency * DMineTime;
        DConveyTime = 1;
        DConveySteps = updateFrequency * DConveyTime;
        DDeathTime = 1;
        DDeathSteps = updateFrequency * DDeathTime;
        DDecayTime = 4;
        DDecaySteps = updateFrequency * DDecayTime;
        DLumberPerHarvest = 100;
        DStonePerQuarrying = 100;
        DGoldPerMining = 100;
    }



}
