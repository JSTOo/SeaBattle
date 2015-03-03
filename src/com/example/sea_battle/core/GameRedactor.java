package com.example.sea_battle.core;

import droidbro.seacore.SeaMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nixy on 26.01.2015.
 */
public class GameRedactor {

    SeaMap map = new SeaMap(10,10);
    public List<Ship> ships = new ArrayList<Ship>();
    public List<Ship> placedShips = new ArrayList<Ship>();
    public int[][] counts = new int[][]{
            {4,Ship.ORIENTATION_EAST},
            {3,Ship.ORIENTATION_EAST},
            {2,Ship.ORIENTATION_EAST},
            {1,Ship.ORIENTATION_EAST}};

    public GameRedactor(){
        for(int i = 0; i < 4; i++){

            ships.add(new Ship(i,1));
        }
        for (int i = 0; i < 3; i++){
            ships.add(new Ship(i,2));
        }
        for (int i = 0; i < 2; i++){
            ships.add(new Ship(i,3));
        }
        ships.add(new Ship(0,4));
    }

    public Ship currentShip = null;

    public SeaMap getCorrectedMap(){
        return getMap();
    }

    public SeaMap getMap() {
        return map;
    }

    public void setMap(SeaMap map) {
        this.map = map;
    }

    public Ship getShip(int numberOfCell){
        if (currentShip != null){
            counts[currentShip.numberOfCell-1][0]++;
            ships.add(currentShip);
        }
        counts[numberOfCell-1][0]--;
        for (Ship ship : ships) {
            if (ship.numberOfCell == numberOfCell) {
                currentShip = ship;
                ships.remove(ship);
                break;
            }
        }

        return currentShip;
    }



    public void removeShipFromField(int x,int y,int uniqcode){

        int[][] map = this.map.getObjects();
        for (int i = 0 ; i < 10; i++){
            for (int j = 0; j < 10; j++){
                    if (map[i][j] == uniqcode){
                        map[i][j] = 0;
                    }
            }
        }

    }

    public Ship pickUpShip(int x,int y){
        int[][] map = this.map.getObjects();
        if (map[y][x] != 0){
            for (Ship ship : placedShips){
                if (ship.getUniqCode() == map[y][x]){
                    placedShips.remove(ship);
                    currentShip = ship;
                    break;
                }
            }
            return currentShip;
        }
        currentShip = null;
        return null;
    }

    public boolean placeShip(Ship ship,int startX,int startY){
        if (ship != null) {
            int orientation = ship.orientation;
            if(ship.getPostion()[0] != -1){
                removeShipFromField(ship.getPostion()[0],ship.getPostion()[1],ship.getUniqCode());
            }
            boolean flag = true;
            switch (orientation) {
                case Ship.ORIENTATION_SOUTH:
                    for (int i = 0; i < ship.numberOfCell; i++) {
                        flag &= checkCell(ship.getUniqCode(), startX, startY + i);
                    }
                    if (flag) {
                        int[][] map = this.map.getObjects();
                        ship.setPostion(startX, startY);
                        for (int i = 0; i < ship.numberOfCell; i++) {
                            map[startY + i][startX] = ship.getUniqCode();
                        }
                        this.map.setObjects(map);
                        return flag;
                    }

                case Ship.ORIENTATION_EAST:
                    for (int i = 0; i < ship.numberOfCell; i++) {
                        flag &= checkCell(ship.getUniqCode(), startX + i, startY);
                    }
                    if (flag) {
                        int[][] map = this.map.getObjects();
                        ship.setPostion(startX, startY);
                        for (int i = 0; i < ship.numberOfCell; i++) {
                            map[startY][startX + i] = ship.getUniqCode();
                        }
                        this.map.setObjects(map);
                        return flag;
                    }

            }
            return flag;
        }
        return false;
    }

    /**
     *  Метод проверки кораблей в соседней клетке и выхода за границы
     * @param uniqCode
     * @param x
     * @param y
     * @return
     */
    private boolean checkCell(int uniqCode, int x, int y) {
        boolean flag = x > -1 && x < 10 && y > -1 && y < 10;
        int[][] map = this.map.getObjects();
        for ( int i = - 1; i < 2 & flag; i++){
            for (int j = -1; j < 2 & flag; j++){
                int cellX = x + j;
                int cellY = y + i;
                if (cellX > -1 && cellX < 10 && cellY > -1 && cellY < 10){
                    flag &=  map[cellY][cellX] == 0 | map[cellY][cellX] == uniqCode;
                }
            }
        }

        return flag;
    }

    public void setShipsOrientation(int numberOfCell, int orientation) {
        for (Ship ship : ships) {
            if(ship.getNumberOfCell() == numberOfCell ){
                ship.setOrientation(orientation);
            }
        }
        if (currentShip != null && currentShip.getNumberOfCell() == numberOfCell){
            currentShip.setOrientation(orientation);
        }
    }

    public void returnShip(Ship currentShip) {
        counts[currentShip.numberOfCell-1][0]++;
        ships.add(currentShip);
        this.currentShip = null;
    }
}
