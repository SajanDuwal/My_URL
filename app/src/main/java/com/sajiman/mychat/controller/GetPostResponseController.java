package com.sajiman.mychat.controller;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetPostResponseController extends AsyncTask<String, Void, String> {

    private String url;
    private OnResponseListener onResponseListener;

    public GetPostResponseController(String url, OnResponseListener listener) {
        this.url = url;
        this.onResponseListener = listener;
    }

    @Override
    protected void onPreExecute() {
        onResponseListener.onStarted(url);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL urlToConnect = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlToConnect.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(strings[0]);
            dataOutputStream.flush();
            dataOutputStream.close();

            connection.connect();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null && !response.startsWith("Error")) {
            onResponseListener.onFinished(response);
        } else {
            onResponseListener.onFailed();
        }
    }
}
