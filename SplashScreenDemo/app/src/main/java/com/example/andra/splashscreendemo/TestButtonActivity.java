package com.example.andra.splashscreendemo;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;

public class TestButtonActivity extends AppCompatActivity {

    TextView txt;
    ImageView img1;
    ImageView img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_button);
        txt = (TextView) findViewById(R.id.txt);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);

        img1.setOnLongClickListener(longClickListener);
        img2.setOnLongClickListener(longClickListener);
        txt.setOnDragListener(dragListener);

        txt.setText("Drag an image here");

        ImageView imageView = (ImageView) findViewById(R.id.img2);
        imageView.setOnTouchListener(new ImageMatrixTouchHandler(imageView.getContext()));



        ImageView mapTileView = (ImageView) findViewById(R.id.mapTileView);
        Bitmap bitmapTerrain =  BitmapFactory.decodeResource(getResources(), R.drawable.terrain);

        Bitmap bitmapTile = Bitmap.createBitmap(bitmapTerrain, 0,bitmapTerrain.getWidth()*20,bitmapTerrain.getWidth(),640);
        mapTileView.setImageBitmap(bitmapTile);

        Context context = getApplicationContext();
        CharSequence text = "Terrain Map Width = " + bitmapTerrain.getWidth();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("","");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, myShadowBuilder,v,0);
            return true;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();
            //txt.setText(Integer.toString(event.getAction()));

            final View view = (View) event.getLocalState();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if(view.getId() == R.id.img1){
                        txt.setText("You picked up a cat");
                    }
                    else if(view.getId() == R.id.img2){
                        txt.setText("You picked up a flower");
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    if(view.getId() == R.id.img1){
                        txt.setText("The cat is in the box");
                    }
                    else if(view.getId() == R.id.img2){
                        txt.setText("The flower is in the box");
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    if(view.getId() == R.id.img1){
                        txt.setText("The cat has left the box");
                    }
                    else if(view.getId() == R.id.img2){
                        txt.setText("The flower has left the box");
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if(view.getId() == R.id.img1){
                        txt.setText("You let go of the cat");
                    }
                    else if(view.getId() == R.id.img2){
                        txt.setText("You let go of the flower");
                    }
                    break;
                case DragEvent.ACTION_DROP:
                    if(view.getId() == R.id.img1){
                        txt.setText("You let go of the cat in the box");
                    }
                    else if(view.getId() == R.id.img2){
                        txt.setText("You let go of the flower in the box");
                    }
                    break;
            }

            return true;
        }
    };

    public void sayHello(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }

    public void toastButton(View view){
        Context context = getApplicationContext();
        CharSequence text = "Hi! I'm a toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
