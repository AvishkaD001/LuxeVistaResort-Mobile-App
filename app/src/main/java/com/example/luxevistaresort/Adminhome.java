package com.example.luxevistaresort;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class Adminhome extends AppCompatActivity {

    private Button roomAddButton;
    private Button ServiceaddButton;
    private Button UserviewButton;

    private Button roombookButton;

    private Button servicebookButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_activity); // Link to the HomeActivity layout


        // Initialize the Log Out button
        roomAddButton = findViewById(R.id.roomAddButton);
        ServiceaddButton = findViewById(R.id.ServiceaddButton);
        UserviewButton = findViewById(R.id.UserviewButton);
        roombookButton = findViewById(R.id.roombookButton);
        servicebookButton = findViewById(R.id.servicebookButton);

        roomAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Navigate back to the LoginActivity (login screen)
                Intent intent = new Intent(Adminhome.this, AddRoomActivity.class);
                startActivity(intent);
            }
        });

        ServiceaddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Navigate back to the LoginActivity (login screen)
                Intent intent = new Intent(Adminhome.this, AddServiceActivity.class);
                startActivity(intent);
            }
        });

        UserviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Navigate back to the LoginActivity (login screen)
                Intent intent = new Intent(Adminhome.this, adminuserview.class);
                startActivity(intent);
            }
        });

        roombookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Navigate back to the LoginActivity (login screen)
                Intent intent = new Intent(Adminhome.this, AdminRoomBookingActivity.class);
                startActivity(intent);
            }
        });


        servicebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Navigate back to the LoginActivity (login screen)
                Intent intent = new Intent(Adminhome.this, AdminResevationActivity.class);
                startActivity(intent);
            }
        });






    }
}
