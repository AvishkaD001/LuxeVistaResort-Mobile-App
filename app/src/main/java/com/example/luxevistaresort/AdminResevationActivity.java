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

public class AdminResevationActivity extends AppCompatActivity {

    private static final String TAG = "ServiceReservationsActivity";
    private LinearLayout serviceReservationsSection;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_serviceresevation_activity);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Find the ServiceReservations section layout
        serviceReservationsSection = findViewById(R.id.RoomBookings_section);

        // Load all reservations from the database
        loadServiceReservations();
    }

    private void loadServiceReservations() {
        mDatabase.child("Reservations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    serviceReservationsSection.removeAllViews();
                    for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                        // Extract reservation details
                        String reservationId = reservationSnapshot.getKey();
                        String name = reservationSnapshot.child("name").getValue(String.class);
                        String email = reservationSnapshot.child("email").getValue(String.class);
                        String address = reservationSnapshot.child("address").getValue(String.class);
                        String checkinDate = reservationSnapshot.child("checkinDate").getValue(String.class);
                        String checkoutDate = reservationSnapshot.child("checkoutDate").getValue(String.class);
                        String serviceType = reservationSnapshot.child("serviceType").getValue(String.class);
                        String servicePrice = reservationSnapshot.child("servicePrice").getValue(String.class);

                        // Add reservation details to the UI
                        addReservationToUI(reservationId, name, email, address, checkinDate, checkoutDate, serviceType, servicePrice);
                    }
                } else {
                    // Show message if no reservations exist
                    addReservationToUI(null, "No reservations found", null, null, null, null, null, null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Error fetching reservations: " + error.getMessage());
                Toast.makeText(AdminResevationActivity.this, "Failed to load reservations.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addReservationToUI(String reservationId, String name, String email, String address,
                                    String checkinDate, String checkoutDate, String serviceType, String servicePrice) {
        LinearLayout reservationContainer = new LinearLayout(this);
        reservationContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        reservationContainer.setOrientation(LinearLayout.VERTICAL);
        reservationContainer.setPadding(16, 16, 16, 16);
        reservationContainer.setBackgroundColor(getResources().getColor(android.R.color.white));
        reservationContainer.setElevation(4);

        // Reservation Details Text
        TextView reservationView = new TextView(this);
        reservationView.setTextSize(16);
        reservationView.setTextColor(getResources().getColor(android.R.color.black));
        StringBuilder reservationDetails = new StringBuilder();
        if (reservationId != null) reservationDetails.append("ID: ").append(reservationId).append("\n");
        if (name != null) reservationDetails.append("Name: ").append(name).append("\n");
        if (email != null) reservationDetails.append("Email: ").append(email).append("\n");
        if (address != null) reservationDetails.append("Address: ").append(address).append("\n");
        if (checkinDate != null) reservationDetails.append("Check-in Date: ").append(checkinDate).append("\n");
        if (checkoutDate != null) reservationDetails.append("Check-out Date: ").append(checkoutDate).append("\n");
        if (servicePrice != null) reservationDetails.append("").append(servicePrice).append("\n");
        if (serviceType != null) reservationDetails.append("").append(serviceType);

        reservationView.setText(reservationDetails.toString());

        // Add reservation details to container
        reservationContainer.addView(reservationView);

        // Add container to the ServiceReservations section
        serviceReservationsSection.addView(reservationContainer);
    }
}
