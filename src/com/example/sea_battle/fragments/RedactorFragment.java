package com.example.sea_battle.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.sea_battle.R;
import com.example.sea_battle.activity.LobbyActivity;
import com.example.sea_battle.adapter.ShipChooserAdapter;
import com.example.sea_battle.view.RedactorFieldView;

/**
 * Created by Nixy on 27.01.2015.
 */
public class RedactorFragment extends android.support.v4.app.Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.redactor_fragment,null);
        RedactorFieldView gameFieldView = (RedactorFieldView) layout.findViewById(R.id.gameField);
        gameFieldView.setGameRedactor(LobbyActivity.gameRedactor);
        ListView gridView = (ListView) layout.findViewById(R.id.list);
        gridView.setAdapter(new ShipChooserAdapter(inflater.getContext(),LobbyActivity.gameRedactor));
        gameFieldView.setAdapter((ShipChooserAdapter)gridView.getAdapter());
        return layout;
    }


}
