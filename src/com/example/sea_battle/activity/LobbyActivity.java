package com.example.sea_battle.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import com.example.sea_battle.R;
import com.example.sea_battle.adapter.ImagesAdapter;
import com.example.sea_battle.adapter.LobbyAdapter;
import com.example.sea_battle.adapter.SimpleFragmentAdapter;
import com.example.sea_battle.core.GameRedactor;
import com.example.sea_battle.core.PostCommands;
import com.example.sea_battle.fragments.ConnectionFragment;
import droidbro.seacore.Game;

import java.util.List;

/**
 * Created by Nixy on 30.01.2015.
 */
public class LobbyActivity extends FragmentActivity {

    public static GameRedactor gameRedactor = new GameRedactor();
    ViewPager pager;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_activity);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SimpleFragmentAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(SimpleFragmentAdapter.CONNECTION_FRAGMENT);
        new ImagesAdapter(this);
        context = this;
        getActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(getString(R.string.refresh));
        MenuItem refreshItem = menu.getItem(0);
        refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        refreshItem.setIcon(R.drawable.refresh_menu_btn);
        refreshItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(pager.getCurrentItem() == SimpleFragmentAdapter.CONNECTION_FRAGMENT){
                    PostCommands commands = new PostCommands(null);
                    List<Game> lobby = commands.getLobby();
                    Fragment connectionFragment = ((SimpleFragmentAdapter) pager.getAdapter()).getItem(pager.getCurrentItem());
                    ListView listView = ((ConnectionFragment) connectionFragment).list;
                    if (lobby != null && listView != null){
                        listView.setAdapter(new LobbyAdapter(context, R.layout.lobby_item,lobby));
                    }
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
