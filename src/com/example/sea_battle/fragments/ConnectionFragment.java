package com.example.sea_battle.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.sea_battle.R;
import com.example.sea_battle.activity.GameActivity;
import com.example.sea_battle.activity.LobbyActivity;
import com.example.sea_battle.adapter.LobbyAdapter;
import com.example.sea_battle.core.PostCommands;
import droidbro.seacore.Game;
import droidbro.seacore.Message;
import droidbro.seacore.Player;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class ConnectionFragment extends android.support.v4.app.Fragment {

    Context context;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public ListView list;
    private boolean inLobby = true;
    String name;
    String password ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();

        name = preferences.getString(getResources().getString(R.string.playerName),"");
        password = preferences.getString(getString(R.string.playerPassword),"");
        PostCommands.me = new Player().setName(name).setPassword(password);

        if (name.equals("")|password.equals("")){
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            name =  manager.getDeviceId();
            password = generatePassword();
            editor.putString(getString(R.string.playerName),name);
            editor.putString(getString(R.string.playerPassword),password);
            editor.commit();
            PostCommands postCommands = new PostCommands(context);
            Message m = postCommands.registration(PostCommands.me);
        }

    }

    private String generatePassword() {
        Random r = new Random();
        String charSet = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890qwertyuiopasdfghjklzxcvbnm";
        String password = "";
        for (int i = 0 ; i < 10; i++ ){
            password += charSet.charAt(r.nextInt(charSet.length()));
        }
        return password;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.connection_fragment,null);

        ConnectivityManager connMgr = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            list = (ListView) layout.findViewById(R.id.lobbyList);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    PostCommands postCommands = new PostCommands(context);
                    Game game = (Game) view.getTag(R.integer.gameID_tag);
                    Message m = postCommands.connectGame(PostCommands.me,game, LobbyActivity.gameRedactor.getCorrectedMap());
                    if (m == Message.CONNECTED){
                        inLobby = false;
                        Toast.makeText(context, getString(R.string.connected_game), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, GameActivity.class);
                        startActivity(intent);
                    }
                    if (m == Message.CREATED){
                        inLobby = false;
                        Toast.makeText(context, getString(R.string.created_game), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, GameActivity.class);
                        startActivity(intent);
                    }

                }
            });
        }  else {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }

        Button createGameButton = (Button) layout.findViewById(R.id.create_button);
        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog =  new Dialog(context,R.style.CustomDialogTheme);
                View dialogContent =  ((Activity) context).getLayoutInflater().inflate(R.layout.change_name_dialog,null);
                dialog.setContentView(dialogContent);
                dialog.setTitle(R.string.create_game_title);
                EditText editText = (EditText) dialogContent.findViewById(R.id.edit_name);
                editText.setText(getString(R.string.new_game));
                Button change = (Button) dialogContent.findViewById(R.id.button_change);
                change.setText(getString(R.string.create_button));
                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) dialog.findViewById(R.id.edit_name);
                        PostCommands commands = new PostCommands(context);
                        String gameName = editText.getText().toString();
                        Message message = commands.createGame(PostCommands.me,new Game().setGameName(gameName),LobbyActivity.gameRedactor.getCorrectedMap());
                        switch (message){
                            case CREATED:
                                inLobby = false;
                                dialog.dismiss();
                                Toast.makeText(context, "Created success!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context, GameActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                });
                Button cancel = (Button) dialogContent.findViewById(R.id.button_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        inLobby = true;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (inLobby) {
                    PostCommands commands = new PostCommands(null);
                    final List<Game> lobby = commands.getLobby();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (inLobby) {
                                if(list != null && lobby != null)
                                    list.setAdapter(new LobbyAdapter(context, R.layout.lobby_item, lobby));
                                Log.d("DEBUG", "In ui thread update list");
                            } else {
                                cancel();
                            }
                        }
                    });
                    Log.d("DEBUG", "In timer thread get list");
                }
            }
        },0,35000);

        PostCommands commands = new PostCommands(null);
        Game game = commands.getCurrentGame(PostCommands.me);
        if(  game != null){

           new AlertDialog.Builder(context,R.style.CustomDialogTheme)
                    .setTitle(R.string.return_in_game)
                    .setPositiveButton(R.string.accept_return, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(context, GameActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.decline_return, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            PostCommands commands = new PostCommands(null);
                            commands.leaveGame(PostCommands.me);
                        }
                    })
                    .setCancelable(false)
                    .create().show();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        inLobby = false;
    }
}

