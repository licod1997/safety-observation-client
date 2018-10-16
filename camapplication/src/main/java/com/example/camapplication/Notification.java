package com.example.camapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notification extends AsyncTask<Void,Void,Void> {
    public final static String AUTH_KEY_FCM = "AIzaSyAD2VmSKTqrJDNQdQXfDo4TmpzNyBAoxfo";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/v1/projects/safetyobservationsclient-2ef53/messages:send";
    public final static String topic = "SafeObject";
    @Override
    protected Void doInBackground(Void... voids) {
        try{
            URL url = new URL(API_URL_FCM);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization","Bearer " + AUTH_KEY_FCM);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();

            json.put("topic", topic);


            JSONObject info = new JSONObject();
            info.put("title", "SafeObject");   // Notification title
            info.put("body", "Có vật nguy hiểm");// Notification body
//            info.put("image","");

            json.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();

        }
        catch (Exception e)
        {
            Log.d("Error",""+e);
        }
        return null;
    }
}
