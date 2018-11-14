package com.android.ecs160.warcraft;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class FragmentFour extends Fragment {

    private View v;
    public FragmentFour(){


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_four, container, false);

        //IconImage Icon = new IconImage(this.getContext());
        //ImageButton imgBtn = (ImageButton) v.findViewById(R.id.archer);
        //imgBtn.setImageBitmap(Icon.returnImage());

         TextView tv = (TextView) v.findViewById(R.id.rightTextview);
         tv.setText("Can you read?");

        return v;


    }

}

