package com.example.sea_battle.core;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nixy on 11.01.2015.
 */
public class ServerConnection extends AsyncTask<String,String,Object> {


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {

        InputStream in = null;
        try {
            HttpURLConnection connection;
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            in = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            in.close();
            Log.d("ServerConnection execute code = " + params[0] , sb.toString());
            publishProgress(params[0],params[3],sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}




