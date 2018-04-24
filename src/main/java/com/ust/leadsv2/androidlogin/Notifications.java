package com.ust.leadsv2.androidlogin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.ust.leadsv2.androidlogin.Notifications.CONNECTION_TIMEOUT;
import static com.ust.leadsv2.androidlogin.Notifications.READ_TIMEOUT;

public class Notifications extends Activity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    TextView notifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notifications = (TextView) findViewById(R.id.notificationText);
        new AsyncRetrieve().execute();
    }

    public void currentReadings(View v){
        Intent intent = new Intent(this, CurrentReadings.class);
        startActivity(intent);
    }

    private class AsyncRetrieve extends AsyncTask<String, String, String> {
        ProgressDialog loading = new ProgressDialog(Notifications.this);
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
                Toast.makeText(Notifications.this, "LEVEL 2", Toast.LENGTH_LONG).show();
            } else if (result.equals("Immediate evacuation is required!")) {
                notifications.setText(result.toString());
                Toast.makeText(Notifications.this, "LEVEL 3", Toast.LENGTH_LONG).show();
            } else {
                // you to understand error returned from doInBackground method
                Toast.makeText(Notifications.this, result.toString(), Toast.LENGTH_LONG).show();
            }

        }

        }
}
