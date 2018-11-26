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
import android.graphics.Bitmap;
import android.util.Log;

public class FragmentThree extends Fragment implements View.OnClickListener{   //https://stackoverflow.com/questions/27964611/how-to-set-onclick-listener-for-a-button-in-a-fragment-in-android

    private final int MAX_BUTTONS = 9;

    private View v;
    private IconImage Icon;
    private ImageButton imgBtn;

    private Integer images[] = new Integer[] {
            R.id.actionBtn1,
            R.id.actionBtn2,
            R.id.actionBtn3,
            R.id.actionBtn4,
            R.id.actionBtn5,
            R.id.actionBtn6,
            R.id.actionBtn7,
            R.id.actionBtn8,
            R.id.actionBtn9,
    };

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



    // Update the button images in this asset image fragment
    public void updateButtonImages(Asset selectedAsset) {
        if(selectedAsset == null)
            return;

        Bitmap currentIcon;
        ImageButton currentImageBtn;
        Integer[] buttonNumbers = Asset.getAssetActionIcons("Peasant");;  // Holding index numbers of Action Icons for selectedAsset

        // Directly grabb the asset data  // In Asset.java - Implemented GetAssetData method, to grab the resource data(String value of resource type)
        String assetType = selectedAsset.getAssetData_ResourceName();
        buttonNumbers = Asset.getAssetActionIcons(assetType);

        //Set images on ImageBtns  // Order: Asset Profile Image -> Action Icons
        currentImageBtn = getActivity().findViewById(R.id.AssetImageBtn);
        currentImageBtn.setVisibility(View.VISIBLE);
        currentImageBtn.setImageBitmap(Icon.getIconImage(Asset.getAssetImageIcons(assetType)));


        //Init. Action Buttons
        int numberOfButtons = buttonNumbers.length;
        for (int buttonIndex = 0; buttonIndex < 9; buttonIndex++) {
            currentImageBtn = getActivity().findViewById(images[buttonIndex]);
            currentImageBtn.setVisibility(View.INVISIBLE);

            if(buttonIndex < numberOfButtons)
                currentImageBtn.setVisibility(View.VISIBLE);
        }

        //Action Button Setting
        if(selectedAsset != null){
            for (int buttonIndex = 0; buttonIndex < numberOfButtons; buttonIndex++) {
                currentIcon = Icon.getIconImage(buttonNumbers[buttonIndex]);
                currentImageBtn = getActivity().findViewById(images[buttonIndex]);
                currentImageBtn.setImageBitmap(currentIcon);
            }

        }

        currentIcon = null;
        currentImageBtn = null;
        buttonNumbers = null;
    }


    // Update the button images in this asset image fragment
    public void updateButtonImages(Integer[] buttonNumbers) {
        Bitmap currentIcon;
        ImageButton currentImageBtn;

        //Initialize the visibility of Buttons
        for (int buttonIndex = 0; buttonIndex < 9; buttonIndex++) {
            currentImageBtn = getActivity().findViewById(images[buttonIndex]);
            currentImageBtn.setVisibility(View.INVISIBLE);

            if(buttonIndex < 5)
                currentImageBtn.setVisibility(View.VISIBLE);
        }

        //Set images on ImageBtns  // Order: Asset Profile Image -> Action Icons
        currentImageBtn = getActivity().findViewById(R.id.AssetImageBtn);
        currentImageBtn.setVisibility(View.VISIBLE);
        //currentImageBtn.setImageBitmap();

        for (int buttonIndex = 0; buttonIndex < 5; buttonIndex++) {
            currentIcon = Icon.getIconImage(buttonNumbers[buttonIndex]);
            currentImageBtn = getActivity().findViewById(images[buttonIndex]);
            currentImageBtn.setImageBitmap(currentIcon);
        }
        currentIcon = null;
        currentImageBtn = null;
    }

    public void resetUIButtonImages(){
        Bitmap currentIcon;
        ImageButton currentImageBtn;


        //Initialize AssetProfile Image
        currentImageBtn = getActivity().findViewById(R.id.AssetImageBtn);
        currentImageBtn.setVisibility(View.INVISIBLE);

        //Initialize the visibility of Buttons
        for (int buttonIndex = 0; buttonIndex < 9; buttonIndex++) {
            currentImageBtn = getActivity().findViewById(images[buttonIndex]);
            currentImageBtn.setVisibility(View.INVISIBLE);
        }

        currentIcon = null;
        currentImageBtn = null;
    }

}

