package com.android.ecs160.warcraft;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentThree extends Fragment implements View.OnClickListener{   //https://stackoverflow.com/questions/27964611/how-to-set-onclick-listener-for-a-button-in-a-fragment-in-android

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
        imgBtn = (ImageButton) v.findViewById(R.id.AssetImageBtn);
        imgBtn.setImageBitmap(Icon.returnTestImage());
        imgBtn.setOnClickListener(this);
       // TextView tv = (TextView) v.findViewById(R.id.rightTextview);
       // tv.setText("Can you read?");

        return v;
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(this.getContext(), "Asset Image Clicked", Toast.LENGTH_SHORT).show();

    }

    //NOTE: this is a test function with stupid name, so don't worry about it.
    public void peasantSelected(){

        imgBtn.setImageBitmap(Icon.returnImage6());

        return;
    }




}
