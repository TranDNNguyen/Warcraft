package com.example.andra.splashscreendemo;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

public class IconImage  {
    //REQUIRED // = loadMapTiles()

    Bitmap iconImage;


    private Bitmap[] iconSet = new Bitmap[179];
    private Context mContext;  //  Check MapRenderer.java to how they set the variable.


    IconImage(){
        mContext = null;
    }
    IconImage(Context c){
        mContext = c;
        generateTileSet();
    }


    public void generateTileSet() {

        iconImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icons);   // Copy&Paste your icons.png into drawable folder.
        for(int i = 0; i < 179; i++){
            iconSet[i] = Bitmap.createBitmap(iconImage, 0,38*i,46,38);
        }


    }


    public Bitmap returnImage(){
        return Bitmap.createScaledBitmap(iconSet[5], 100,100,false);
    }
    public void setIconImage(ImageButton imgBtn){
        imgBtn.setImageBitmap(iconSet[5]);
    }

    public Bitmap returnImage6(){
        return Bitmap.createScaledBitmap(iconSet[6], 100,100,false);
    }
    public void setIconImage6(ImageButton imgBtn6){
        imgBtn6.setImageBitmap(iconSet[6]);
    }



}

