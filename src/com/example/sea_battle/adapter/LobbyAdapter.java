package com.example.sea_battle.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.sea_battle.R;
import droidbro.seacore.Game;

import java.util.List;

/**
 * Created by Nixy on 18.01.2015.
 */
public class LobbyAdapter extends ArrayAdapter<Game>{

    int layoutid;
    LayoutInflater mInflater;

    public LobbyAdapter(Context context, int recource, List<Game> objects) {
        super(context, recource, objects);
        layoutid = recource;
        mInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = mInflater.inflate(layoutid,parent,false);
        TextView gameName = (TextView) layout.findViewById(R.id.game_name);
        TextView playersCount = (TextView) layout.findViewById(R.id.players_count);
        Game game = getItem(position);

        gameName.setText(game.getGameName());
        playersCount.setText(getContext().getString(R.string.playersCountTextViewText)+" "+String.valueOf(game.getPlayers().size()));
        layout.setTag(R.integer.gameID_tag,game);
        return layout;
    }
}
