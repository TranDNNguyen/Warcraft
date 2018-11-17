package com.android.ecs160.warcraft;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class FragmentThree extends Fragment {

    private View v;
    private IconImage Icon;
    private ImageButton imgBtn;

    public FragmentThree(){


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        v =inflater.inflate(R.layout.fragment_three, container, false);

        Icon = new IconImage(this.getContext());
        imgBtn = (ImageButton) v.findViewById(R.id.archer);
        imgBtn.setImageBitmap(Icon.returnTestImage());

       // TextView tv = (TextView) v.findViewById(R.id.rightTextview);
       // tv.setText("Can you read?");

        return v;
    }

    //NOTE: this is a test function with stupid name, so don't worry about it.
    public void peasantSelected() {
        imgBtn.setImageBitmap(Icon.returnImage6());
    }

    public void footmanUnitButtons() {
        imgBtn.setImageBitmap(Icon.returnImage(83)); //human-move
        imgBtn.setImageBitmap(Icon.returnImage(164)); //human-armor-1
        imgBtn.setImageBitmap(Icon.returnImage(116)); //human-weapon-1
        imgBtn.setImageBitmap(Icon.returnImage(170)); //human-patrol
        imgBtn.setImageBitmap(Icon.returnImage(172)); //human-stand-ground
    }

    public void rangedUnitButtons() {
        imgBtn.setImageBitmap(Icon.returnImage(83)); //human-move
        imgBtn.setImageBitmap(Icon.returnImage(164)); //human-armor-1
        imgBtn.setImageBitmap(Icon.returnImage(124)); //human-arrow-1
        imgBtn.setImageBitmap(Icon.returnImage(170)); //human-patrol
        imgBtn.setImageBitmap(Icon.returnImage(172)); //human-stand-ground
    }

    public void peasantButtons() {
        imgBtn.setImageBitmap(Icon.returnImage(83)); //human-move
        imgBtn.setImageBitmap(Icon.returnImage(164)); //human-armor-1
        imgBtn.setImageBitmap(Icon.returnImage(116)); //human-weapon-1
        imgBtn.setImageBitmap(Icon.returnImage(85)); //repair
        imgBtn.setImageBitmap(Icon.returnImage(86)); //mine
        imgBtn.setImageBitmap(Icon.returnImage(89)); //human-convey
        imgBtn.setImageBitmap(Icon.returnImage(87)); //build-simple
    }

    public void buildSimpleButtons() {
        imgBtn.setImageBitmap(Icon.returnImage(38)); //chicken-farm
        imgBtn.setImageBitmap(Icon.returnImage(42)); //human-barracks
        imgBtn.setImageBitmap(Icon.returnImage(40)); //town-hall
        imgBtn.setImageBitmap(Icon.returnImage(44)); //human-lumber-mill
        imgBtn.setImageBitmap(Icon.returnImage(46)); //human-blacksmith
        imgBtn.setImageBitmap(Icon.returnImage(60)); //scout-tower
        imgBtn.setImageBitmap(Icon.returnImage(91)); //cancel
    }

    public void barracksButtons() {
        imgBtn.setImageBitmap(Icon.returnImage(2)); //footman

        //TODO: need to store state of buildings and upgrades
        if(1 /*if ranger upgrade not complete*/ ) {
            imgBtn.setImageBitmap(Icon.returnImage(4)); //archer
        } else {
            imgBtn.setImageBitmap(Icon.returnImage(6)); //ranger
        }
    }

    public void chickenFarmButtons() {

    }

    public void townHallButtons() {
        imgBtn.setImageBitmap(Icon.returnImage(0)); //peasant

        //TODO: need to store state of buildings and upgrades
        if(1 /*if keep upgrade not complete*/ ) {
            imgBtn.setImageBitmap(Icon.returnImage(66)); //keep
        } else if (1 /*if castle upgrade not complete*/ ){
            imgBtn.setImageBitmap(Icon.returnImage(68)); //castle
        }
    }

    public void blacksmithButtons() {

    }

    public void lumberMillButtons() {

    }

    //used for building and units while being trained or built
    public void cancelButton() {
        imgBtn.setImageBitmap(Icon.returnImage(91)); //cancel
    }
}
