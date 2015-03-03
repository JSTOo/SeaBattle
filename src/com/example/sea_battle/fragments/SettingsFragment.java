package com.example.sea_battle.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sea_battle.R;
import com.example.sea_battle.core.PostCommands;
import droidbro.seacore.Message;
import droidbro.seacore.Player;

/**
 * Created by Nixy on 30.01.2015.
 */
public class SettingsFragment extends Fragment {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.settings_fragment,null);
        final TextView nameText = (TextView) layout.findViewById(R.id.name_text);
        nameText.setText(nameText.getText()+" "+preferences.getString(getString(R.string.playerName),""));
        Button changeName = (Button) layout.findViewById(R.id.change_nameButton);
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog =  new Dialog(context,R.style.CustomDialogTheme);
                View dialogContent =  ((Activity) context).getLayoutInflater().inflate(R.layout.change_name_dialog,null);
                dialog.setContentView(dialogContent);
                dialog.setTitle(R.string.change_name_title);
                EditText editText = (EditText) dialogContent.findViewById(R.id.edit_name);
                editText.setText(preferences.getString(getString(R.string.playerName),""));
                Button change = (Button) dialogContent.findViewById(R.id.button_change);
                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) dialog.findViewById(R.id.edit_name);
                        PostCommands commands = new PostCommands(context);
                        String newName = editText.getText().toString();
                        Message message = commands.checkName(new Player().setName(newName));
                        if (message == Message.NO_REGISTERED){

                            String password = preferences.getString(getString(R.string.playerPassword),"");
                            commands = new PostCommands(context);
                            commands.deletePlayer(PostCommands.me);
                            commands = new PostCommands(context);
                            PostCommands.me = new Player().setName(newName).setPassword(password);
                            commands.registration(PostCommands.me);
                            editor.putString(getString(R.string.playerName),newName);
                            editor.commit();
                            dialog.dismiss();
                            nameText.setText(getResources().getString(R.string.name_text)+" "+newName);
                            Toast.makeText(context,R.string.success_change,Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context,R.string.name_exist,Toast.LENGTH_LONG).show();
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
}
