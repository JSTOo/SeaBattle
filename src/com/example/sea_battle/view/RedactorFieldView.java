package com.example.sea_battle.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.example.sea_battle.R;
import com.example.sea_battle.adapter.ShipChooserAdapter;
import com.example.sea_battle.core.GameRedactor;
import com.example.sea_battle.core.Ship;

/**
 * Created by Nixy on 26.01.2015.
 */
public class RedactorFieldView extends View {

    GameRedactor gameRedactor = new GameRedactor();
    int w = -1;
    int h = -1;
    float px;
    float py;

    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.empty_cell);

    GestureDetector gestureDetector;
    ShipChooserAdapter adapter;

    public RedactorFieldView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
                return false;
            }
        });
        gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                px = motionEvent.getX();
                py = motionEvent.getY();
                for (int i = 0 ; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (pInside(j * w / 10, i * h / 10, j * w / 10 + w / 10, i * h / 10 + h / 10)) {
                            int uniqcode = gameRedactor.getMap().getObjects()[i][j];
                            if (uniqcode != 0) {
                                gameRedactor.removeShipFromField(j, i, uniqcode);
                                for (Ship ship : gameRedactor.placedShips) {
                                    if (ship.getUniqCode() == uniqcode) {
                                        gameRedactor.placedShips.remove(ship);
                                        gameRedactor.ships.add(ship);
                                        gameRedactor.counts[ship.getNumberOfCell() - 1][0]++;
                                        if (adapter != null) {
                                            adapter.notifyDataSetChanged();
                                        }
                                        break;
                                    }
                                }
                                break;
                            } else {
                                break;
                            }
                        }
                    }
                }
                px = -1;
                py = -1;
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                return false;
            }
        });
    }

    public GameRedactor getGameRedactor() {
        return gameRedactor;
    }

    public void setGameRedactor(GameRedactor gameRedactor) {
        this.gameRedactor = gameRedactor;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_CANCEL:
                px = -1;
                py = -1;

                break;
            case MotionEvent.ACTION_DOWN:
                px = event.getX();
                py = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                px = event.getX();
                py = event.getY();

                break;
            case MotionEvent.ACTION_UP:
                px = -1;
                py = -1;
                if (gameRedactor.currentShip != null && gameRedactor.currentShip.getPostion()[0] != -1) {
                    gameRedactor.placedShips.add(gameRedactor.currentShip);
                    gameRedactor.currentShip = null;
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }

                break;

        }
        invalidate();

        return true | gestureDetector.onTouchEvent(event);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        if( this.w == -1) {
            if ((w > h)) {
                this.w = h;
                this.h = h;

            } else {
                this.w = w;
                this.h = w;

            }
        } else {
            if ( w < this.w){
                this.w = w;
                this.h = w;
            }
            if (h < this.w){
                this.w = h;
                this.h = h;
            }
        }
        this.setMeasuredDimension(this.w, this.h);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        int[][] map = gameRedactor.getMap().getObjects();

        for (int i = 0 ; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                    canvas.drawBitmap(bmp, j * w / 10, i * h / 10, new Paint());
            }
        }

        for (int i = 0 ; i < 10; i++){
            for (int j = 0; j < 10; j++){
                if (pInside(j * w / 10, i * h / 10, j * w / 10 + w / 10, i * h / 10 + h / 10)) {
                    if (gameRedactor.currentShip != null) {
                        if (!gameRedactor.placeShip(gameRedactor.currentShip, j, i)) {
                            Paint paint = new Paint();
                            paint.setColor(Color.RED);
                            int startX = j * w / 10;
                            int startY = i * h / 10;
                            int endX = startX;
                            int endY = startY;
                            if (gameRedactor.currentShip.getOrientation() == Ship.ORIENTATION_SOUTH) {
                                endY += (gameRedactor.currentShip.getNumberOfCell()) * h / 10;
                                endX += w / 10;
                            } else {
                                endY += h / 10;
                                endX += (gameRedactor.currentShip.getNumberOfCell()) * w / 10;
                            }
                            Rect dst = new Rect(startX, startY, endX, endY);
                            canvas.drawRect(dst, paint);
                        } else {
                            int startX = gameRedactor.currentShip.getPostion()[0] * w / 10;
                            int startY = gameRedactor.currentShip.getPostion()[1] * h / 10;
                            int endX = startX;
                            int endY = startY;
                            if (gameRedactor.currentShip.getOrientation() == Ship.ORIENTATION_SOUTH) {
                                endY += (gameRedactor.currentShip.getNumberOfCell()) * h / 10;
                                endX += w / 10;
                            } else {
                                endY += h / 10;
                                endX += (gameRedactor.currentShip.getNumberOfCell()) * w / 10;
                            }
                            Rect dst = new Rect(startX, startY, endX, endY);
                            Bitmap bitmap = gameRedactor.currentShip.getBitmap();
                            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                            canvas.drawBitmap(gameRedactor.currentShip.getBitmap(), src, dst, new Paint());

                        }
                    } else {
                        if ( map[i][j] != 0){
                            gameRedactor.pickUpShip(j,i);
                        }
                    }

                }
            }
        }
        for(Ship ship : gameRedactor.placedShips){
            if (ship.getPostion()[0] != -1){
                int startX = ship.getPostion()[0]*w/10;
                int startY = ship.getPostion()[1]*h/10;
                int endX = startX;
                int endY = startY;
                if (ship.getOrientation() == Ship.ORIENTATION_SOUTH){
                    endY += (ship.getNumberOfCell())*h/10;
                    endX += w/10;
                }   else {
                    endY += h/10;
                    endX += (ship.getNumberOfCell())*w/10;
                }
                Rect dst = new Rect(startX,startY,endX,endY);
                Bitmap bitmap = ship.getBitmap();
                Rect src = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
                canvas.drawBitmap(ship.getBitmap(),src,dst,new Paint());
            }
        }

    }

    boolean pInside(int x1,int y1,int x2,int y2){
        return (px > x1) && (px < x2) && (py > y1) && (py < y2);
    }

    public void setAdapter(ShipChooserAdapter adapter) {
        this.adapter = adapter;
    }
}
