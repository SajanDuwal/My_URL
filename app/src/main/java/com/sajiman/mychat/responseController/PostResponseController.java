package com.sajiman.mychat.responseController;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostResponseController extends AsyncTask<String, Void, String> {

    private String params;
    private ResponseListener listener;

    public PostResponseController(ResponseListener listener, String params) {
        this.listener = listener;
        this.params = params;
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        try {
            URL urlToConnect = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlToConnect.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Accept", "application/jason");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            DataOutputStream dataOutputStream = (DataOutputStream) httpURLConnection.getOutputStream();
            dataOutputStream.writeBytes(params);
            dataOutputStream.flush();
            dataOutputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
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
        listener.onFinish(response);
    }
}
