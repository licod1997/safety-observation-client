package com.example.camapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notification extends AsyncTask<Void,Void,Void> {
    public final static String AUTH_KEY_FCM = "AIzaSyDjOQOi3zd7zB4qZGbYcQ48QNjMTiRd3kA";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    public final static String topic = "evuAp7rzwek:APA91bEJh_25jzaq0Vr1Y3mgm-FJXGvo06ZUsVwFdboUHbz76oozW21GxXFGU-3wFtGB5JUino4t8oEXEctkmzcFXpJdt0hCUQyznzba2P9wNk4PA_pko6zSKmYFWABpt_fCd6FUmNGU";

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

            json.put("to", topic);


            JSONObject info = new JSONObject();
            info.put("title", "TechnoWeb");   // Notification title
            info.put("body", "Hello Test notification"); // Notification body

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
