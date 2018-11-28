package com.android.ecs160.warcraft;

import android.content.Context;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;

public class PlayerData {

    int DGold;
    int DLumber;
    int DStone;
    int DGameCycle;
    PlayerColor playerColor;

    PlayerData(PlayerColor color, int gold, int lumber){
        DGold = gold;
        DLumber = lumber;
        DStone = 0;
        DGameCycle = 0;
        playerColor = color;
    }

    enum PlayerColor{
        Red(0),
        Blue(1),
        Green(2),
        Purple(3),
        Orange(4),
        Yellow(5),
        Black(6),
        White(7),
        None(8);

        private int idx;

        PlayerColor(int i) {
            idx = i;
        }

        int getIdx() {
            return idx;
        }
    }





}
