package com.android.ecs160.warcraft;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class Fragment_ActionButton extends Fragment {

    private View v;
    public Fragment_ActionButton(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_five, container, false);

        IconImage Icon = new IconImage(this.getContext());
        ImageButton imgBtn = (ImageButton) v.findViewById(R.id.archer);
        imgBtn.setImageBitmap(Icon.returnTestImage());

        IconImage Icon6 = new IconImage(this.getContext());
        ImageButton imgBtn6 = (ImageButton) v.findViewById(R.id.axethrower);
        imgBtn6.setImageBitmap(Icon6.returnImage6());



        //TextView tv = (TextView) v.findViewById(R.id.rightTextview);
        //tv.setText("Can you read??");

        return v;


    }

}
