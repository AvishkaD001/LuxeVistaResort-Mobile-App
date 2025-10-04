package com.example.luxevistaresort;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private TextView userName, userEmail, userPhone;
    private LinearLayout roomBookingsSection, servicesBookingsSection;
    private Button logoutButton, submitRatingButton;
    private ProgressBar progressBar;
    private RatingBar ratingBar;
    private EditText feedbackMessage;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize UI components
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPhone = findViewById(R.id.user_phone);
        roomBookingsSection = findViewById(R.id.room_bookings_section);
        servicesBookingsSection = findViewById(R.id.services_section);
        logoutButton = findViewById(R.id.logout_button);
        progressBar = findViewById(R.id.progress_bar);
        ratingBar = findViewById(R.id.user_rating_bar);
        feedbackMessage = findViewById(R.id.user_rating_message);
        submitRatingButton = findViewById(R.id.rate_button);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Fetch user details
        fetchUserDetails();

        // Handle logout button click
        logoutButton.setOnClickListener(v -> showLogoutConfirmation());

        // Handle rating submission
        submitRatingButton.setOnClickListener(v -> submitRating());
    }

    private void fetchUserDetails() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            redirectToLogin();
            return;
        }

        String userId = currentUser.getUid();
        String userEmailStr = currentUser.getEmail();
        progressBar.setVisibility(View.VISIBLE);

        // Fetch user profile data
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                if (snapshot.exists()) {
                    String fullName = snapshot.child("fullName").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);

                    userName.setText("Name: " + (fullName != null ? fullName : "N/A"));
                    userEmail.setText("Email: " + (email != null ? email : "N/A"));
                    userPhone.setText("Phone: " + (phone != null ? phone : "N/A"));
                } else {
                    Toast.makeText(UserProfileActivity.this, "User profile data not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Log.e("UserProfileActivity", "Error fetching user profile: " + error.getMessage());
            }
        });

        // Fetch bookings and services
        fetchBookings(userEmailStr);
        fetchServices(userEmailStr);
    }

    private void fetchBookings(String userEmail) {
        if (userEmail == null) {
            Toast.makeText(this, "User email is null. Cannot fetch bookings.", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabase.child("Bookings").orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomBookingsSection.removeAllViews();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot booking : dataSnapshot.getChildren()) {
                        String roomType = booking.child("roomType").getValue(String.class);
                        String roomPrice = booking.child("roomPrice").getValue(String.class);
                        String checkInDate = booking.child("checkInDate").getValue(String.class);
                        String checkOutDate = booking.child("checkOutDate").getValue(String.class);

                        String bookingDetails = "Room: " + (roomType != null ? roomType : "N/A") +
                                "\nPrice: " + (roomPrice != null ? roomPrice : "N/A") +
                                "\nCheck-In: " + (checkInDate != null ? checkInDate : "N/A") +
                                "\nCheck-Out: " + (checkOutDate != null ? checkOutDate : "N/A");

                        addDetailToUI(roomBookingsSection, bookingDetails);
                    }
                } else {
                    addDetailToUI(roomBookingsSection, "No bookings found.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("UserProfileActivity", "Error fetching bookings: " + error.getMessage());
            }
        });
    }

    private void fetchServices(String userEmail) {
        if (userEmail == null) {
            Toast.makeText(this, "User email is null. Cannot fetch services.", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabase.child("Reservations").orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servicesBookingsSection.removeAllViews();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot reservation : dataSnapshot.getChildren()) {
                        String serviceType = reservation.child("serviceType").getValue(String.class);
                        String servicePrice = reservation.child("servicePrice").getValue(String.class);

                        String serviceDetails = "Service: " + (serviceType != null ? serviceType : "N/A") +
                                "\nPrice: " + (servicePrice != null ? servicePrice : "N/A");

                        addDetailToUI(servicesBookingsSection, serviceDetails);
                    }
                } else {
                    addDetailToUI(servicesBookingsSection, "No services found.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("UserProfileActivity", "Error fetching services: " + error.getMessage());
            }
        });
    }

    private void submitRating() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You need to log in to submit a rating.", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = currentUser.getEmail();
        String feedback = feedbackMessage.getText().toString().trim();
        float rating = ratingBar.getRating();

        if (rating == 0) {
            Toast.makeText(this, "Please provide a star rating.", Toast.LENGTH_SHORT).show();
            return;
        }

        Rating ratingData = new Rating(email, feedback, rating);

        mDatabase.child("Ratings").push().setValue(ratingData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                    ratingBar.setRating(0);
                    feedbackMessage.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to submit feedback. Try again.", Toast.LENGTH_SHORT).show();
                    Log.e("UserProfileActivity", "Error submitting rating: " + e.getMessage());
                });
    }

    private void addDetailToUI(LinearLayout section, String detail) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setText(detail);
        textView.setTextSize(16);
        textView.setPadding(8, 8, 8, 8);
        section.addView(textView);
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    mAuth.signOut();
                    redirectToLogin();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(UserProfileActivity.this, ActiveLoginpage.class);
        startActivity(intent);
        finish();
    }
}
