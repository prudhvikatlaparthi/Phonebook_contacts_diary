package com.andy.pru.phone_contact_diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Splash_screen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView i= findViewById(R.id.splash);
        Glide.with(Splash_screen.this).load(R.drawable.splash).into(i);
        Thread splash=new Thread() {
            public void run() {
                try{
// set sleep time
                    sleep(1500);
                    Intent i =new Intent(getBaseContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        splash.start();
    }
    }

