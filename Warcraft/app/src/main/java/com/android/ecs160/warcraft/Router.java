package com.android.ecs160.warcraft;


//NOTE: In the linux code this is called "RouterMap", and is in charge of pathfinding
public class Router {

    //TODO should take asset, asset target, and asset decorated map
    //for now I will treat the map as flat and totally traversable.
    public static Asset.EDirection FindPath(Asset asset, int destX, int destY) {


        //for now, this is all we need to do, since the whole map is traversable
        return calcDirection(asset, destX, destY);
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

}
