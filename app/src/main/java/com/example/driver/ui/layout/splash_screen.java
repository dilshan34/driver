package com.example.driver.ui.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.driver.R;

public class splash_screen extends AppCompatActivity {

    private static int splash_time_out = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(splash_screen.this, Login.class);
                startActivity(homeIntent);
                finish();
            }
        }, splash_time_out);
    }
}