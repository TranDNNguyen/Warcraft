package com.android.ecs160.warcraft;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;

import java.util.Vector;

public class FragmentThree extends Fragment implements View.OnClickListener{   //https://stackoverflow.com/questions/27964611/how-to-set-onclick-listener-for-a-button-in-a-fragment-in-android

    private final int MAX_BUTTONS = 9;

    private View v;
    private IconImage Icon;
    private ImageButton assetProfileBtn;
    private Vector<ImageButton> imageButtons;
    private Vector<ImageButton> buildingButtons;
    Asset selectedAsset;
    Asset lastSelectedAsset;
    AssetBuilder assetBuilder;
    /*
    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private ImageButton imageButton4;
    private ImageButton imageButton5;
    private ImageButton imageButton6;
    private ImageButton imageButton7;
    private ImageButton imageButton8;
    private ImageButton imageButton9;
    */

    private Asset currentAsset;

    //TODO - Implement Multiple Asset Selected Case
    //private Asset[] selectedAssets;

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

    private Integer buildings[] = new Integer[]{
            R.id.barracks,
            R.id.blacksmith,
            R.id.cannon_tower,
            R.id.castle,
            R.id.farm,
            R.id.guard_tower,
            R.id.keep,
            R.id.lumber_mill,
            R.id.scout_tower,
            R.id.town_hall,
    };
    private String building_names[] = new String[]{
            "barracks",
            "blacksmith",
            "cannon_tower",
            "castle",
            "farm",
            "guard_tower",
            "keep",
            "lumber_mill",
            "scout_tower",
            "town_hall",
    };

    public FragmentThree(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        v =inflater.inflate(R.layout.fragment_three, container, false);

        Icon = new IconImage(this.getContext());
        assetProfileBtn = (ImageButton) v.findViewById(R.id.AssetImageBtn);
        assetProfileBtn.setVisibility(View.INVISIBLE);
        assetProfileBtn.setOnClickListener(this);

        ImageButton imageButton;
        imageButtons = new Vector<>();
        buildingButtons = new Vector<>();
        for(int i = 0; i < 9; i++){
            imageButton = (ImageButton) v.findViewById(images[i]);
            imageButton.setVisibility(View.INVISIBLE);
            imageButton.setOnClickListener(this);
            imageButtons.add(imageButton);
        }
        for(int i = 0; i < buildings.length; i++){
            imageButton = (ImageButton) v.findViewById(buildings[i]);
            Integer index = Asset.getAssetImageIcons(building_names[i]);
            imageButton.setImageBitmap(Icon.getIconImage(index));
            imageButton.setVisibility(View.INVISIBLE);
            imageButton.setOnClickListener(this);
            buildingButtons.add(imageButton);
        }

        currentAsset = null;
       // TextView tv = (TextView) v.findViewById(R.id.rightTextview);
       // tv.setText("Can you read?");

        return v;
    }

    public void Setup(AssetBuilder assetBuilder){
        this.assetBuilder = assetBuilder;
    }

    //Display Asset Info.  //  Invoked when Asset Image was being clicked.
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.AssetImageBtn){
            ActionButton(v);
        }else if(v.getId() == R.id.actionBtn3){
            AttackButton(v);
        }else if(v.getId() == R.id.actionBtn7){
            BuildOptionsButton(v);
        }else if(buildingButtons.contains(v)){
            BuildButton(v);
        }
    }

    public void BuildOptionsButton(View v){
        if(buildingButtons.get(0).getVisibility() == View.VISIBLE){
            //TODO: only display buildings that can be built rn
            for(int i = 0; i < buildingButtons.size(); i++){
                buildingButtons.get(i).setVisibility(View.INVISIBLE);
            }
        }else{
            for(int i = 0; i < buildingButtons.size(); i++){
                buildingButtons.get(i).setVisibility(View.VISIBLE);
            }
        }

    }

    public void BuildButton(View v){
        //hide buttons
        resetUIButtonImages();
        //call Build
        CTilePosition pos = new CTilePosition(selectedAsset.x, selectedAsset.y);

        //TODO:let user select where to build building. for now: builds wherever the peasant is.
        //TODO:make sure to check that building isn't built on top of other buildings or units.

        switch (v.getId()){
            case R.id.barracks:
                assetBuilder.Build(selectedAsset, Asset.EAssetType.Barracks, pos);
                break;
            case R.id.blacksmith:
                assetBuilder.Build(selectedAsset, Asset.EAssetType.Blacksmith, pos);
                break;
            case R.id.cannon_tower:
                assetBuilder.Build(selectedAsset, Asset.EAssetType.CannonTower, pos);
                break;
            case R.id.farm:
                assetBuilder.Build(selectedAsset, Asset.EAssetType.Farm, pos);
                break;
            case R.id.guard_tower:
                assetBuilder.Build(selectedAsset, Asset.EAssetType.GuardTower, pos);
                break;
            case R.id.keep:
                assetBuilder.Build(selectedAsset, Asset.EAssetType.Keep, pos);
                break;
            case R.id.lumber_mill:
                assetBuilder.Build(selectedAsset, Asset.EAssetType.LumberMill, pos);
                break;
            case R.id.scout_tower:
                assetBuilder.Build(selectedAsset, Asset.EAssetType.ScoutTower, pos);
                break;
            case R.id.town_hall:
                assetBuilder.Build(selectedAsset, Asset.EAssetType.TownHall, pos);
                break;
        }
    }

    public void AttackButton(View v) {

    }

    public void ActionButton(View v){
        //Custom Toast View
        //https://stackoverflow.com/questions/11288475/custom-toast-on-android-a-simple-example
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.customlayout_toast, (ViewGroup) v.findViewById(R.id.toast_layout_Info));
        ImageView image = (ImageView) layout.findViewById(R.id.toast_info_image);
        image.setImageBitmap(Icon.getIconImage(Asset.getAssetImageIcons(currentAsset.getAssetData_ResourceName())));
        TextView text = (TextView) layout.findViewById(R.id.toast_info_text);
        text.setText(   currentAsset.getAssetData().resourceName + "\n" +
                "Health : " + currentAsset.getAssetData().hitPoints + "/"+Asset.AssetTypeData.getAssetData(currentAsset.type).hitPoints + "\n"+
                " Armor : " + currentAsset.getAssetData().armor + "\n" +
                "Damage : " + currentAsset.getAssetData().piercingDamage+
                "-"+ currentAsset.getAssetData().basicDamage + "\n" +
                " Range : " + currentAsset.getAssetData().range + "\n" +
                " Sight : " + currentAsset.getAssetData().sight + "\n" +
                " Speed : " + currentAsset.getAssetData().speed + "\n");

        Toast toast = new Toast(getActivity().getApplicationContext()); // https://stackoverflow.com/questions/25329275/the-method-getapplicationcontext-is-undefined-fragment-issues
        toast.setGravity(Gravity.CENTER_VERTICAL, 10, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
        layout = null;
    }


    // Update the button images in this asset image fragment
    public void updateButtonImages(Asset selectedAsset, Asset lastSelectedAsset) {
        this.selectedAsset = selectedAsset;
        this.lastSelectedAsset = lastSelectedAsset;

        if(selectedAsset == null) {
            currentAsset = null;
            resetUIButtonImages();
            return;
        }

        currentAsset = selectedAsset;

        Bitmap currentIcon;
        ImageButton currentImageBtn;
        Integer[] buttonNumbers = Asset.getAssetActionIcons("Peasant");;  // Holding index numbers of Action Icons for selectedAsset

        // Asset.java - Implemented GetAssetData method, to grab the resource data(String value of resource type)
        String assetType = selectedAsset.getAssetData_ResourceName();
        buttonNumbers = Asset.getAssetActionIcons(assetType);

        //Set images on ImageBtns  // Order: Asset Profile Image -> Action Icons
        assetProfileBtn.setVisibility(View.VISIBLE);
        Integer index = Asset.getAssetImageIcons(assetType);
        //if(index != null) {
            assetProfileBtn.setImageBitmap(Icon.getIconImage(index));
       //}

        //Initialize the visibility of Buttons
        int numberOfButtons = buttonNumbers.length;

        for (int buttonIndex = 0; buttonIndex < 9; buttonIndex++) {
            currentImageBtn = getActivity().findViewById(images[buttonIndex]);
            currentImageBtn.setVisibility(View.INVISIBLE);

            if(buttonIndex < numberOfButtons)
                currentImageBtn.setVisibility(View.VISIBLE);
        }

        ////int iconDetail[9];

        if(selectedAsset != null){
            for (int buttonIndex = 0; buttonIndex < numberOfButtons; buttonIndex++) {
                currentIcon = Icon.getIconImage(buttonNumbers[buttonIndex]);
                currentImageBtn = getActivity().findViewById(images[buttonIndex]);
                currentImageBtn.setImageBitmap(currentIcon);

                ////if(images[buttonIndex] == 87) iconDetail[buttonIndex] = "~~~" buildSImple
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

        for(int i = 0; i < buildingButtons.size(); i++){
            buildingButtons.get(i).setVisibility(View.INVISIBLE);
        }

        currentIcon = null;
        currentImageBtn = null;
    }

}

