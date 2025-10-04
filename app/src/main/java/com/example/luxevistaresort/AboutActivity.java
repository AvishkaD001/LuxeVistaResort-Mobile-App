package com.example.luxevistaresort;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "AboutActivity";
    private LinearLayout feedbackSection;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.about_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("About LuxeVista");
        }

        // Initialize feedback section
        feedbackSection = findViewById(R.id.feedback_section);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Load all feedback from Firebase
        loadFeedback();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadFeedback() {
        mDatabase.child("Ratings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    feedbackSection.removeAllViews();
                    for (DataSnapshot feedbackSnapshot : snapshot.getChildren()) {
                        // Extract feedback details
                        String email = feedbackSnapshot.child("fullname").getValue(String.class);
                        String feedback = feedbackSnapshot.child("feedback").getValue(String.class);
                        Float rating = feedbackSnapshot.child("rating").getValue(Float.class);

                        // Add feedback to the UI
                        if (rating != null) {
                            addFeedbackToUI(email, rating, feedback);
                        } else {
                            Log.w(TAG, "Rating is null for feedback.");
                        }
                    }
                } else {
                    // Show default message if no feedback exists
                    addFeedbackToUI(null, 0, "No feedback available.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Error fetching feedback: " + error.getMessage());
                Toast.makeText(AboutActivity.this, "Failed to load feedback.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFeedbackToUI(String fullname, float rating, String feedback) {
        TextView feedbackView = new TextView(this);
        feedbackView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        feedbackView.setPadding(8, 8, 8, 8);
        feedbackView.setTextSize(16);
        feedbackView.setTextColor(getResources().getColor(android.R.color.black));

        // Build feedback text
        StringBuilder feedbackText = new StringBuilder();
        if (fullname != null) {
            feedbackText.append("Email: ").append(fullname).append("\n");
        }
        feedbackText.append("Rating: ").append(rating).append("/5\n");
        feedbackText.append("Message: ").append(feedback);

        // Set feedback text and add to layout
        feedbackView.setText(feedbackText.toString());
        feedbackSection.addView(feedbackView);
    }
}
