package com.example.driver.ui.layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.driver.R;

import Driver.MapsActivity;
import User.UserMapActivity;

public class dashboard extends AppCompatActivity {

    private DrawerLayout drawer;
    CardView user,driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        user = findViewById(R.id.user);
        driver = findViewById(R.id.driver);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, UserMapActivity.class);
                startActivity(intent);

            }
        });

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, MapsActivity.class);
                startActivity(intent);
            }
        });



    }
}