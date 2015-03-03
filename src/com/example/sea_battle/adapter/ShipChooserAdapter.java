package com.example.sea_battle.adapter;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.sea_battle.R;
import com.example.sea_battle.core.GameRedactor;
import com.example.sea_battle.core.Ship;

/**
 * Created by Nixy on 28.01.2015.
 */
public class ShipChooserAdapter extends BaseAdapter {

    Context context;
    GameRedactor gameRedactor;
    LayoutInflater layoutInflater;

    float[] disableFilter = new float[]{
            0.3f, 0.59f, 0.11f, 0, 0,
            0.3f, 0.59f, 0.11f, 0, 0,
            0.3f, 0.59f, 0.11f, 0, 0,
            0, 0, 0, 1, 0,}; // GrayScale Matrix

    float[] enableFilter = new float[]{
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0,};
    public ShipChooserAdapter(Context context,GameRedactor gameRedactor){
        this.context = context;


        this.gameRedactor = gameRedactor;
        layoutInflater = ((LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    @Override
    public int getCount() {
        return gameRedactor.counts.length;
    }

    @Override
    public Object getItem(int i) {
        return gameRedactor.counts[i][0];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View layout = layoutInflater.inflate(R.layout.ship_chooser_item,viewGroup,false);
        ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);

        imageView.setImageBitmap(ImagesAdapter.images[i][gameRedactor.counts[i][1]]);
        final TextView textView = (TextView) layout.findViewById(R.id.textView);
        textView.setText(String.valueOf(getItem(i)));
        if (gameRedactor.currentShip != null && gameRedactor.currentShip.getNumberOfCell()-1 == i){
            imageView.setColorFilter(new ColorMatrixColorFilter(enableFilter));
        } else {
            imageView.setColorFilter(new ColorMatrixColorFilter(disableFilter));
        }
        imageView.setTag(R.integer.numberOfCells_tag, i);
        imageView.setTag(R.integer.orientaion_tag, gameRedactor.counts[i][1]);
        imageView.setTag(R.integer.adapter_tag,this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numberOfCells = (Integer) view.getTag(R.integer.numberOfCells_tag);
                if (gameRedactor.currentShip != null && gameRedactor.currentShip.getNumberOfCell()-1 == numberOfCells){
                    gameRedactor.returnShip(gameRedactor.currentShip);
                    ((ShipChooserAdapter) view.getTag(R.integer.adapter_tag)).notifyDataSetChanged();
                } else {
                    if (gameRedactor.counts[numberOfCells][0] > 0) {
                        gameRedactor.getShip(numberOfCells + 1);
                        textView.setText(String.valueOf(gameRedactor.counts[numberOfCells]));
                        ((ShipChooserAdapter) view.getTag(R.integer.adapter_tag)).notifyDataSetChanged();
                    }
                }
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                    int numberOfCells = (Integer) view.getTag(R.integer.numberOfCells_tag);
                    gameRedactor.counts[numberOfCells][1] = gameRedactor.counts[numberOfCells][1] == Ship.ORIENTATION_EAST ? Ship.ORIENTATION_SOUTH : Ship.ORIENTATION_EAST;
                    gameRedactor.setShipsOrientation(numberOfCells+1, gameRedactor.counts[numberOfCells][1]);
                    if (gameRedactor.currentShip == null)
                        view.performClick();
                    return true;
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView view1 = (ImageView) view.findViewById(R.id.imageView);
                view1.performClick();
            }
        });
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ImageView view1 = (ImageView) view.findViewById(R.id.imageView);
                view1.performLongClick();
                return true;
            }
        });
        return layout;
    }
}
