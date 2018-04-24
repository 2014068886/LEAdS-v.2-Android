package com.ust.leadsv2.androidlogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.ArrayList;
import java.util.List;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTextMessage;
    BottomNavigationView navigation;
    TextView tv_notification, tv_location, tv_moisture, tv_rain, tv_movement, tv_level;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        loadFragment(new FragmentHome());
    }

    public void viewLogs(View v){
        Intent intent = new Intent(this, ViewLogs.class);
        startActivity(intent);
    }

    public void checkNotifications(View v){
        Intent intent = new Intent(this, Notif.class);
        startActivity(intent);
    }

    public void currentReadings(View v){
        Intent intent = new Intent(this, CurrentReadings.class);
        startActivity(intent);
    }

    private boolean loadFragment(android.support.v4.app.Fragment fragment) {
        if (fragment != null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

            return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        android.support.v4.app.Fragment fragment = null;

        switch (item.getItemId()) {

            case R.id.BottombarHome:
                fragment = new FragmentHome();
                break;

            case R.id.BottombarFaqs:
                fragment = new FragmentFaqs();
                break;

            case R.id.BottombarSettings:
                fragment = new FragmentSettings();
                break;
        }

        return loadFragment(fragment);
    }


}
