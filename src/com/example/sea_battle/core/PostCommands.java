package com.example.sea_battle.core;


import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import com.google.gson.reflect.TypeToken;
import droidbro.seacore.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Nixy on 29.01.2015.
 */
public class PostCommands {

    public static final String DOMEN = "site1.201511.brim.ru" ;
    public static final String SERVER = "/servlet";
    public static final String CONTROLLER = "/controller";
    public static final String CONNECT_URL = "http://"+DOMEN+SERVER+CONTROLLER;
    PostTask postTask;

    public static Player me;
    public static Game currentGame;

    public PostCommands(Context context){
        postTask = new PostTask(context);
    }

    public List<Game> getLobby() {

        try {
            return JSONAdapter.getGson().fromJson(postTask.execute(CONNECT_URL
                    , ParamName.command.name(), ParamValue.getLobby.name()).get(), new TypeToken<List<Game>>() {
            }.getType());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message registration(Player p){

        String playerJSON = JSONAdapter.getGson().toJson(p);
        try {
            return JSONAdapter.getGson().fromJson(postTask.execute(CONNECT_URL
                    , ParamName.command.name(), ParamValue.registration.name()
                    , ParamName.player.name(), playerJSON).get(), Message.class);
        }   catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Message connectGame(Player p,Game game,SeaMap map){

        try {
            return JSONAdapter.getGson().fromJson(postTask.execute(CONNECT_URL
            ,ParamName.command.name(),ParamValue.joinGame.name()
            ,ParamName.player.name(),JSONAdapter.getGson().toJson(p)
            ,ParamName.game.name(),JSONAdapter.getGson().toJson(game)
            ,ParamName.map.name(),JSONAdapter.getGson().toJson(map)).get() ,Message.class);
        }  catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }

    public Message createGame(Player p,Game g,SeaMap map){
        try {
            return JSONAdapter.getGson().fromJson(postTask.execute(CONNECT_URL
                    ,ParamName.command.name(),ParamValue.joinGame.name()
                    ,ParamName.player.name(),JSONAdapter.getGson().toJson(p)
                    ,ParamName.game.name(),JSONAdapter.getGson().toJson(g)
                    ,ParamName.map.name(),JSONAdapter.getGson().toJson(map)).get() ,Message.class);
        }  catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Message checkName(Player p){
        try {
            return JSONAdapter.getGson().fromJson(postTask.execute(CONNECT_URL
            ,ParamName.command.name(),ParamValue.checkName.name()
            ,ParamName.player.name(),JSONAdapter.getGson().toJson(p)).get(),Message.class);
        }  catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Message deletePlayer(Player p){
        try {
            return JSONAdapter.getGson().fromJson(postTask.execute(CONNECT_URL
            ,ParamName.command.name(),ParamValue.deletePlayer.name()
            ,ParamName.player.name(),JSONAdapter.getGson().toJson(p)).get(),Message.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Message leaveGame(Player p){
        try {
            return JSONAdapter.getGson().fromJson(postTask.execute(CONNECT_URL
            ,ParamName.command.name(),ParamValue.leaveGame.name()
            ,ParamName.player.name(),JSONAdapter.getGson().toJson(p)).get(),Message.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Game getCurrentGame(Player p) {
        try {
            String answer = postTask.execute(CONNECT_URL
                    ,ParamName.command.name(),ParamValue.currentGame.name()
                    ,ParamName.player.name(),JSONAdapter.getGson().toJson(p)).get();
            Log.d("DEBUG","Answer "+answer+ "\ncurrent game = " + answer);
            return JSONAdapter.gson1.fromJson(answer,Game.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Message doTurn(Player p,int x,int y){
        try {
            String pointJSON = JSONAdapter.getGson().toJson(new Point(x, y));
            String answer = postTask.execute(CONNECT_URL
                    ,ParamName.command.name(),ParamValue.doTurn.name()
                    ,ParamName.player.name(),JSONAdapter.getGson().toJson(p)
                    ,ParamName.point.name(),pointJSON).get();
            Log.d("DEBUG", "Answer " + answer + "\nPoint JSON = " + pointJSON);
            return JSONAdapter.getGson().fromJson(answer,Message.class);
        } catch (Exception e){
            e.printStackTrace();
            Log.d("DEBUG",e.toString());
        }
        return  null;
    }
}
