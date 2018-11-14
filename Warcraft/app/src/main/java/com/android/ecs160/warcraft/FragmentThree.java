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
    public void peasantSelected(){

        imgBtn.setImageBitmap(Icon.returnImage6());

        return;
    }
}
