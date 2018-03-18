package com.example.acer.project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getSupportActionBar().setTitle("Travello");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(21,91,142)));

        Thread thread=new Thread()
        {
            @Override
            public void run() {
                try
                {
                    sleep(2000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    Intent homeIntent=new Intent(WelcomeActivity.this,StartActivity.class);
                    startActivity(homeIntent);
                }
            }
        };
        thread.start();
    }
}
