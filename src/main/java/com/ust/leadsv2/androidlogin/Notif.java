package com.ust.leadsv2.androidlogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Notif extends Activity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    TextView notifications;
    TextView view1, view2, view3, view4, view5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        notifications = (TextView) findViewById(R.id.notificationText);
        view1 = (TextView) findViewById(R.id.location);
        view2 = (TextView) findViewById(R.id.rain);
        view3 = (TextView) findViewById(R.id.movement);
        view4 = (TextView) findViewById(R.id.moisture);
        view5 = (TextView) findViewById(R.id.level);
        getJSON("http://192.168.1.14/LEAdS%20v.2/user/readings.php");
        new AsyncRetrieve().execute();

    }

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] heroes = new String[jsonArray.length()];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String loc = obj.getString("location");
            String rain = obj.getString("rain");
            String movement = obj.getString("movement");
            String moisture = obj.getString("moisture");
            String level = obj.getString("level");

            view1.setText(loc);
            view2.setText(rain);
            view3.setText(movement);
            view4.setText(moisture);
            view5.setText(level);
        }

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
        //view.setAdapter(arrayAdapter);
    }

    public void currentReadings(View v){
        Intent intent = new Intent(this, CurrentReadings.class);
        startActivity(intent);
    }

    private class AsyncRetrieve extends AsyncTask<String, String, String> {
        ProgressDialog loading = new ProgressDialog(Notif.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading.setMessage("\tLoading...");
            loading.setCancelable(false);
            loading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://192.168.56.1/LEAdS%20v.2/user/notifications.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return e.toString();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
            } catch (IOException e1) {
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {

                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return (result.toString());

                } else {
                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }

        // this method will interact with UI, display result sent from doInBackground method
        @Override
        protected void onPostExecute(String result) {

            loading.dismiss();
            if(result.equals("Get ready for an evacuation and wait for further announcements!")) {
                notifications.setText(result.toString());
                Toast.makeText(Notif.this, "LEVEL 2", Toast.LENGTH_LONG).show();
            } else if (result.equals("Immediate evacuation is required!")) {
                notifications.setText(result.toString());
                Toast.makeText(Notif.this, "LEVEL 3", Toast.LENGTH_LONG).show();
            } else {
                // you to understand error returned from doInBackground method
                Toast.makeText(Notif.this, result.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}
