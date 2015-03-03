package com.example.sea_battle.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nixy on 29.01.2015.
 */
public class PostTask extends AsyncTask<String,String,String> {

    private final Context context;
    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    public PostTask(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        publishProgress();
        InputStream in;
        try {
            HttpPost post = new HttpPost(params[0]);
            HttpClient client = new DefaultHttpClient();
            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            for (int i = 1; i < params.length; i += 2) {
                postParams.add(new BasicNameValuePair(params[i], params[i + 1]));
            }
            post.setEntity(new UrlEncodedFormEntity(postParams));
            HttpResponse response = client.execute(post);
            in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            in.close();
            return sb.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (context != null) {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (dialog != null && dialog.isShowing())
             dialog.dismiss();
    }
}
