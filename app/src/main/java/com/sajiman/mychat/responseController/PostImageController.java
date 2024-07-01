package com.sajiman.mychat.responseController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostImageController extends AsyncTask<String, Void, String> {

    private File finalImageFile;
    private Context context;
    private int serverResponseCode = 0;
    private ImageResponseListener listener;
    private String serverResponseMassage;
    private HttpURLConnection httpURLConnection = null;
    private DataOutputStream dataOutputStream = null;
    private String endLine = "\r\n";
    private String twoHyphens = "--";
    private String boundary = "*****";
    private int bytesRead, bytesAvailable, bufferSize;
    private byte[] buffer;
    private int maxBufferSize = 1 * 1024 * 1024;


    public PostImageController(File finalImageFile, Context context, ImageResponseListener listener) {
        this.finalImageFile = finalImageFile;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.e(PostImageController.class.getSimpleName(), "image file name: " + finalImageFile.getName());
        String uploadServerUri = strings[0];
        try {
            FileInputStream fileInputStream = new FileInputStream(finalImageFile);
            URL urlToConnect = new URL(uploadServerUri);
            httpURLConnection = (HttpURLConnection) urlToConnect.openConnection();
            httpURLConnection.setDoOutput(true); //allows output
            httpURLConnection.setDoInput(true); // allows input
            httpURLConnection.setUseCaches(false); //don't use a cached copy
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//            httpURLConnection.connect();

            dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(twoHyphens + boundary + endLine);
            dataOutputStream.writeBytes("Content-Disposition: form-data; " +
                    "name=\"image\";filename=\"" + finalImageFile.getName() + "\"" + endLine);
            dataOutputStream.writeBytes(endLine);

            bytesAvailable = fileInputStream.available(); // creating a buffer of maximum size
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            //read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dataOutputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            //send multipart form data necessary after file data...
            dataOutputStream.writeBytes(endLine);
            dataOutputStream.writeBytes(twoHyphens + boundary + endLine);

            //Responses from the server (code and message).
            serverResponseCode = httpURLConnection.getResponseCode();
            serverResponseMassage = httpURLConnection.getResponseMessage();
            fileInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            serverResponseMassage = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponseMassage;
    }

    @Override
    protected void onPostExecute(String response) {
        listener.onFinish(response);
    }
}
