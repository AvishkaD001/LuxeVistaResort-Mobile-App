package com.example.luxevistaresort;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button UserprofiletButton;
    private Button RoomviewButton;
    private Button ServiceviewButton;

    private Button aboutviewButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Link to the HomeActivity layout


        // Initialize the Log Out button
        UserprofiletButton = findViewById(R.id.UserprofileButton);
        RoomviewButton = findViewById(R.id.roomviewButton);
        ServiceviewButton = findViewById(R.id.ServiceviewButton);
        aboutviewButton = findViewById(R.id.aboutviewButton);

        UserprofiletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Navigate back to the LoginActivity (login screen)
                Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        RoomviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Navigate back to the LoginActivity (login screen)
                Intent intent = new Intent(HomeActivity.this, ViewRoomActivity.class);
                startActivity(intent);
            }
        });

        ServiceviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Navigate back to the LoginActivity (login screen)
                Intent intent = new Intent(HomeActivity.this, ViewServiceActivity.class);
                startActivity(intent);
            }
        });

        aboutviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Navigate back to the LoginActivity (login screen)
                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });





    }
}
