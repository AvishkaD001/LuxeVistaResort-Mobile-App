package com.example.luxevistaresort;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminuserview extends AppCompatActivity {

    private static final String TAG = "adminuserview";
    private LinearLayout feedbackSection;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_userview_activity);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Find the feedback section layout
        feedbackSection = findViewById(R.id.feedback_section);

        // Load all users from the database
        loadUsers();
    }

    private void loadUsers() {
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    feedbackSection.removeAllViews();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Extract user details
                        String userId = userSnapshot.getKey();
                        String fullName = userSnapshot.child("fullName").getValue(String.class);
                        String email = userSnapshot.child("email").getValue(String.class);
                        String phone = userSnapshot.child("phone").getValue(String.class);

                        // Add user details to the UI
                        addUserToUI(userId, fullName, email, phone);
                    }
                } else {
                    // Show message if no users exist
                    addUserToUI(null, "No users found", null, null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Error fetching users: " + error.getMessage());
                Toast.makeText(adminuserview.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUserToUI(String userId, String name, String email, String phone) {
        LinearLayout userContainer = new LinearLayout(this);
        userContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        userContainer.setOrientation(LinearLayout.VERTICAL);
        userContainer.setPadding(16, 16, 16, 16);
        userContainer.setBackgroundColor(getResources().getColor(android.R.color.white));
        userContainer.setElevation(4);

        // User Details Text
        TextView userView = new TextView(this);
        userView.setTextSize(16);
        userView.setTextColor(getResources().getColor(android.R.color.black));
        StringBuilder userDetails = new StringBuilder();
        if (userId != null) userDetails.append("ID: ").append(userId).append("\n");
        if (name != null) userDetails.append("Name: ").append(name).append("\n");
        if (email != null) userDetails.append("Email: ").append(email).append("\n");
        if (phone != null) userDetails.append("Phone: ").append(phone);

        userView.setText(userDetails.toString());

        // Add user details to container
        userContainer.addView(userView);

        // Add container to the feedback section
        feedbackSection.addView(userContainer);
    }
}
