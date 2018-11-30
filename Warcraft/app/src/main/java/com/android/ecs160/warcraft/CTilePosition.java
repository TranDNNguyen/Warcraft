package com.android.ecs160.warcraft;

public class CTilePosition {

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
