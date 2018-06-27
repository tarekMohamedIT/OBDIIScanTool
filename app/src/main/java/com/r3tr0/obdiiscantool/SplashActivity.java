package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import communications.CommandsReceiver;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.r3tr0.obdiiscantool.R.layout.activity_splash);

        sendBroadcast(new Intent(this, CommandsReceiver.class));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, IntroductionActivity.class));
                finish();
            }
        }, 2000);
    }
}
