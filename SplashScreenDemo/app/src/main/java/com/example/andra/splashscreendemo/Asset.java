package com.example.andra.splashscreendemo;

import java.util.Vector;

public class Asset {
    int owner;
    EAssetType type;
    int x;
    int y;
    Vector<Integer> pixelCoordinates;

    enum EAssetType{
        None(0),
        Peasant(1),
        Footman(2),
        Archer(3),
        Ranger(4),
        GoldMine(5),
        TownHall(6),
        Keep(7),
        Castle(8),
        Farm(9),
        Barracks(10),
        LumberMill(11),
        Blacksmith(12),
        ScoutTower(13),
        GuardTower(14),
        CannonTower(15);

        private int idx;

        EAssetType(int i) {
            idx = i;
        }

        int getIdx(){
            return idx;
        }
    };

    Asset(EAssetType t, int o, int x, int y){
        //

    }
    Asset(String input[]){
        type = EAssetType.valueOf(input[0]);
        owner = Integer.valueOf(input[1]);
        x = Integer.valueOf(input[2]);
        y = Integer.valueOf(input[3]);
    }

    //Asset(){}

}
