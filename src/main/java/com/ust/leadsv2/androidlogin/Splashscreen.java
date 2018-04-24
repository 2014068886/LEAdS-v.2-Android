package com.ust.leadsv2.androidlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splashscreen extends AppCompatActivity {

    private ImageView ivSplashLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        ivSplashLogo = (ImageView) findViewById(R.id.iv_splashlogo);
        Animation splashtransition = AnimationUtils.loadAnimation(this, R.anim.splashtransition);
        ivSplashLogo.startAnimation(splashtransition);


        final Intent i = new Intent(this, SigninActivity.class);

        Thread timer = new Thread () {
            public void run (){
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();

    }
}
