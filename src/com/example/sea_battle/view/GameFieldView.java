package com.example.sea_battle.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.example.sea_battle.R;
import com.example.sea_battle.core.PostCommands;
import com.example.sea_battle.core.Ship;
import droidbro.seacore.Game;
import droidbro.seacore.Message;
import droidbro.seacore.Player;
import droidbro.seacore.SeaMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nixy on 01.02.2015.
 */
public class GameFieldView extends View {

    Bitmap bmpEmpty = BitmapFactory.decodeResource(getResources(), R.drawable.empty_cell);
    Bitmap bmpDamaged = BitmapFactory.decodeResource(getResources(), R.drawable.damaged);
    Bitmap bmpMissed = BitmapFactory.decodeResource(getResources(), R.drawable.missed);

    int w = -1;
    int h = -1;
    float px;
    float py;
    int[][] mapOfDamage;
    int[][] mapOfMyDamage;
    int[][] myShips;
    private Game currentGame = null;

    public static final int MISSED = 1;
    public static final int DAMAGED = 2;

    Player me;
    Player currentPlayer;
    List<Ship> placedShips = new ArrayList<Ship>();
    Context context;

    public GameFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (currentPlayer.getName().equals(me.getName())) {
            switch (action) {
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
                    for (int i = 0 ; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            if (pInside(j * w / 10, i * h / 10, j * w / 10 + w / 10, i * h / 10 + h / 10)) {
                                if(mapOfDamage[i][j] == 0){
                                    Log.d("DEBUG","Do turn x = " + j + " y = " + i);
                                    PostCommands commands = new PostCommands(context);
                                    Message m = commands.doTurn(me,j,i);
                                    Log.d("DEBUG","Answer = " + m.name());
                                    if (m == Message.HIT){
                                        mapOfDamage[i][j] = DAMAGED;
                                    }
                                    if (m == Message.MISS){
                                        mapOfDamage[i][j] = MISSED;
                                    }
                                    invalidate();
                                    break;
                                }
                            }
                        }
                    }


                    break;

            }
            invalidate();
        }  else {
            Toast.makeText(context,context.getString(R.string.current_player)+" "+ currentPlayer.getName(),Toast.LENGTH_SHORT).show();
        }

        return true ;
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
        for (int i = 0 ; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                canvas.drawBitmap(bmpEmpty, j * w / 10, i * h / 10, new Paint());
            }
        }

       if(currentPlayer != null && currentGame.getPlayers().size() > 1) {
           if (currentPlayer.getName().equals(me.getName())) {
               for (int i = 0; i < 10; i++) {
                   for (int j = 0; j < 10; j++) {
                       int startX = j * w / 10;
                       int startY = i * h / 10;
                       int endX = startX + w/10;
                       int endY = startY + h/10;
                       Rect dst = new Rect(startX,startY,endX,endY);

                       switch (mapOfDamage[i][j]) {
                           case DAMAGED:
                               Rect src = new Rect(0, 0, bmpDamaged.getWidth(), bmpDamaged.getHeight());
                               canvas.drawBitmap(bmpDamaged, src,dst, new Paint());
                               break;
                           case MISSED:
                               src = new Rect(0, 0, bmpMissed.getWidth(), bmpDamaged.getHeight());
                               canvas.drawBitmap(bmpMissed, src,dst, new Paint());
                               break;
                       }
                   }
               }
           } else {
               for (Ship ship : placedShips) {
                   if (ship.getPostion()[0] != -1) {
                       int startX = ship.getPostion()[0] * w / 10;
                       int startY = ship.getPostion()[1] * h / 10;
                       int endX = startX;
                       int endY = startY;
                       if (ship.getOrientation() == Ship.ORIENTATION_SOUTH) {
                           endY += (ship.getNumberOfCell()) * h / 10;
                           endX += w / 10;
                       } else {
                           endY += h / 10;
                           endX += (ship.getNumberOfCell()) * w / 10;
                       }
                       Rect dst = new Rect(startX, startY, endX, endY);
                       Bitmap bitmap = ship.getBitmap();
                       Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                       canvas.drawBitmap(ship.getBitmap(), src, dst, new Paint());
                   }
               }
               for (int i = 0; i < 10; i++) {
                   for (int j = 0; j < 10; j++) {
                       int startX = j * w / 10;
                       int startY = i * h / 10;
                       int endX = startX + w/10;
                       int endY = startY + h/10;
                       Rect dst = new Rect(startX,startY,endX,endY);
                       switch (mapOfMyDamage[i][j]) {
                           case DAMAGED:
                               Rect src = new Rect(0, 0, bmpDamaged.getWidth(), bmpDamaged.getHeight());
                               canvas.drawBitmap(bmpDamaged, src,dst, new Paint());
                               break;
                           case MISSED:
                               src = new Rect(0, 0, bmpMissed.getWidth(), bmpDamaged.getHeight());
                               canvas.drawBitmap(bmpMissed, src,dst, new Paint());
                               break;
                       }
                   }
               }
           }
       }
    }

    boolean pInside(int x1,int y1,int x2,int y2){
        return (px > x1) && (px < x2) && (py > y1) && (py < y2);
    }

    public void setCurrentGame(Game currentGame) {
        if (this.currentGame == null){
            Collection<SeaMap> maps = currentGame.getSeaMaps();
            for (SeaMap map : maps) {
                if(map.getPlayer().getName().equals(me.getName()))
                    myShips = map.getObjects();

            }
            HashMap<Integer,Ship> uniqShips = new HashMap<Integer, Ship>();
            for (int i = 0; i < 10; i++){
                for (int j = 0; j < 10; j++){
                    if (myShips[i][j] != 0){
                        uniqShips.put(myShips[i][j],new Ship(myShips[i][j]));
                    }
                }
            }
            placedShips.addAll(uniqShips.values());
        }
        Collection<SeaMap> maps = currentGame.getSeaMaps();
        for (SeaMap map : maps) {
            if(map.getPlayer().getName().equals(me.getName())) {
                mapOfMyDamage = map.getObjects();
            } else {
                mapOfDamage =  map.getObjects();
            }

        }
        currentPlayer = currentGame.getCurrentPlayer();
        this.currentGame = currentGame;
    }

    public void setMe(Player me) {
        this.me = me;
    }
}
