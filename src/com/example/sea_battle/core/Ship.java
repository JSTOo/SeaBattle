package com.example.sea_battle.core;

import android.graphics.Bitmap;
import com.example.sea_battle.adapter.ImagesAdapter;

/**
 * Created by Nixy on 26.01.2015.
 */
public class Ship {

    public static final int ORIENTATION_EAST = 0;
    public static final int ORIENTATION_SOUTH = 1;

    int uniqCode;
    int numberOfCell;

    int ix = -1;
    int iy = -1;
    int orientation;


    public Ship(int code,int numberOfCell){
        uniqCode = code;
        this.numberOfCell = numberOfCell;
        orientation = ORIENTATION_EAST;
    }

    public Ship(int hasCode){
        orientation = (hasCode >> 15) & 0x1;  // 0x1 = 0001
        numberOfCell = (hasCode >> 12) & 0x7; // 0x7 = 0111
        ix = (hasCode >> 8) & 0xF; // 0xF = 1111
        iy = (hasCode >> 4) & 0xF;
        uniqCode = hasCode & 0xF;
    }

    public int getUniqCode() {
        return hashCode();
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getNumberOfCell() {
        return numberOfCell;
    }

    public void setPostion(int x,int y){
        ix = x;
        iy = y;
    }

    @Override
    public String toString() {
        return "Ship ix = " + ix + " iy = " + iy
                + "\norientation = " + orientation
                + "\nnumberOfCell = " + numberOfCell
                + "\nhashCode = " + hashCode();
    }

    public int[] getPostion(){
        return new int[]{ix,iy};
    }

    public Bitmap getBitmap() {
        return ImagesAdapter.images[numberOfCell-1][orientation];
    }

    @Override
    public int hashCode() {
        int code = (orientation << 15)| ( numberOfCell) << 12 | (ix << 8) | (iy << 4) | uniqCode ;
        return  code;
    }
}
