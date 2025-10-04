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

public class AdminRoomBookingActivity extends AppCompatActivity {

    private static final String TAG = "RoomBookingsActivity";
    private LinearLayout roomBookingsSection;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_roombooking_activity);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Find the RoomBookings section layout
        roomBookingsSection = findViewById(R.id.RoomBookings_section);

        // Load all bookings from the database
        loadRoomBookings();
    }

    private void loadRoomBookings() {
        mDatabase.child("Bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    roomBookingsSection.removeAllViews();
                    for (DataSnapshot bookingSnapshot : snapshot.getChildren()) {
                        // Extract booking details
                        String bookingId = bookingSnapshot.getKey();
                        String name = bookingSnapshot.child("name").getValue(String.class);
                        String email = bookingSnapshot.child("email").getValue(String.class);
                        String address = bookingSnapshot.child("address").getValue(String.class);
                        String checkinDate = bookingSnapshot.child("checkinDate").getValue(String.class);
                        String checkoutDate = bookingSnapshot.child("checkoutDate").getValue(String.class);
                        String roomType = bookingSnapshot.child("roomType").getValue(String.class);
                        String roomPrice = bookingSnapshot.child("roomPrice").getValue(String.class);

                        // Add booking details to the UI
                        addBookingToUI(bookingId, name, email, address, checkinDate, checkoutDate, roomType, roomPrice);
                    }
                } else {
                    // Show message if no bookings exist
                    addBookingToUI(null, "No bookings found", null, null, null, null, null, null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Error fetching bookings: " + error.getMessage());
                Toast.makeText(AdminRoomBookingActivity.this, "Failed to load bookings.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBookingToUI(String bookingId, String name, String email, String address,
                                String checkinDate, String checkoutDate, String roomType, String roomPrice) {
        LinearLayout bookingContainer = new LinearLayout(this);
        bookingContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        bookingContainer.setOrientation(LinearLayout.VERTICAL);
        bookingContainer.setPadding(16, 16, 16, 16);
        bookingContainer.setBackgroundColor(getResources().getColor(android.R.color.white));
        bookingContainer.setElevation(4);

        // Booking Details Text
        TextView bookingView = new TextView(this);
        bookingView.setTextSize(16);
        bookingView.setTextColor(getResources().getColor(android.R.color.black));
        StringBuilder bookingDetails = new StringBuilder();
        if (bookingId != null) bookingDetails.append("ID: ").append(bookingId).append("\n");
        if (name != null) bookingDetails.append("Name: ").append(name).append("\n");
        if (email != null) bookingDetails.append("Email: ").append(email).append("\n");
        if (address != null) bookingDetails.append("Address: ").append(address).append("\n");
        if (checkinDate != null) bookingDetails.append("Check-in Date: ").append(checkinDate).append("\n");
        if (checkoutDate != null) bookingDetails.append("Check-out Date: ").append(checkoutDate).append("\n");
        if (roomPrice != null) bookingDetails.append("").append(roomPrice).append("\n");
        if (roomType != null) bookingDetails.append("").append(roomType);


        bookingView.setText(bookingDetails.toString());

        // Add booking details to container
        bookingContainer.addView(bookingView);

        // Add container to the RoomBookings section
        roomBookingsSection.addView(bookingContainer);
    }
}
