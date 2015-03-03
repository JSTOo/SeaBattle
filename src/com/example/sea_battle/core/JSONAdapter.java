package com.example.sea_battle.core;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import droidbro.json.FullGameDeserializer;
import droidbro.json.GameLobbyDeserializer;
import droidbro.json.MessageDeserializer;
import droidbro.seacore.Game;
import droidbro.seacore.Message;

/**
 * Created by Nixy on 18.01.2015.
 */
public class JSONAdapter {
    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Game.class,new GameLobbyDeserializer())
            .registerTypeAdapter(Message.class,new MessageDeserializer())
            .create();
    public static Gson gson1 = new GsonBuilder()
            .registerTypeAdapter(Game.class,new FullGameDeserializer())
            .create();
    public static Gson getGson(){
        return gson;
    }
}
