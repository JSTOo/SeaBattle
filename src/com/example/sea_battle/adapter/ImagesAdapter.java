package com.example.sea_battle.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.sea_battle.R;

/**
 * Created by Nixy on 28.01.2015.
 */
public class ImagesAdapter {
    public static Bitmap[][] images;
    public ImagesAdapter(Context context){
        Resources resources = context.getResources();
        images = new Bitmap[][]{
                {BitmapFactory.decodeResource(resources, R.drawable.ship_1_horizontal),BitmapFactory.decodeResource(resources,R.drawable.ship_1_vertical)},
                {BitmapFactory.decodeResource(resources,R.drawable.ship_2_horizontal),BitmapFactory.decodeResource(resources,R.drawable.ship_2_vertical)},
                {BitmapFactory.decodeResource(resources, R.drawable.ship_3_horizontal),BitmapFactory.decodeResource(resources, R.drawable.ship_3_vertical)},
                {BitmapFactory.decodeResource(resources,R.drawable.ship_4_horizontal),BitmapFactory.decodeResource(resources,R.drawable.ship_4_vertical)}};

    }

}
