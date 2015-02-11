package com.example.sea_battle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sea_battle.R;
import com.example.sea_battle.core.PostCommands;
import com.example.sea_battle.view.GameFieldView;
import droidbro.seacore.Game;
import droidbro.seacore.Message;
import droidbro.seacore.Player;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nixy on 01.02.2015.
 */
public class GameActivity extends Activity {

    Player me;
    Timer timer;
    Context context;

    TextView turnTextView;
    TextView playersTextView;
    GameFieldView gameView;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_activity);
        context = this;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        me = new Player()
                .setName(preferences.getString(getString(R.string.playerName), ""))
                .setPassword(preferences.getString(getString(R.string.playerPassword), ""));
        Button leave = (Button) findViewById(R.id.button_leave);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PostCommands commands = new PostCommands(context);
        Message m = commands.leaveGame(me);
        timer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flag = false;
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag = true;
        turnTextView = (TextView) findViewById(R.id.turn_text);
        gameView  = (GameFieldView) findViewById(R.id.game_field);
        playersTextView = (TextView) findViewById(R.id.players_text);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                PostCommands commands = new PostCommands(null);
                final Game game = commands.getCurrentGame(me);
                Log.d("DEBUG","Update game from server ");
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (flag) {
                            if (game != null )
                                if (game.getWinner() == null) {
                                    if (game.getCurrentPlayer()!= null)
                                        turnTextView.setText(game.getCurrentPlayer().getName() + " " + getString(R.string.turn_text_view_text));
                                    String names = "";
                                    for (Player p : game.getPlayers()) {
                                        if (p != null)
                                            names += p.getName() + "\n";
                                    }
                                    playersTextView.setText(getString(R.string.player_text) + "\n" + names);
                                    gameView.setCurrentGame(game);
                                    gameView.invalidate();
                                } else {
                                    Toast.makeText(context, game.getWinner().getName() + " " + getString(R.string.win_game), Toast.LENGTH_LONG).show();
                                    PostCommands commands = new PostCommands(null);
                                    commands.leaveGame(PostCommands.me);
                                    onBackPressed();
                                }
                        }  else {
                            cancel();
                        }
                    }
                });
            }
        },0,2000);
        gameView.setMe(me);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;
    }
}
