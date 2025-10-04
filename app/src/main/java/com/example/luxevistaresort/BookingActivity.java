package com.example.luxevistaresort;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {

    private TextView roomTypeTextView;
    private TextView roomPriceTextView;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private Button confirmButton;
    private Button checkAvailabilityButton;
    private Button checkInDateButton;
    private Button checkOutDateButton;
    private TextView checkInDateTextView;
    private TextView checkOutDateTextView;

    private Calendar checkInDate;
    private Calendar checkOutDate;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Initialize views
        roomTypeTextView = findViewById(R.id .room_type_text_view);
        roomPriceTextView = findViewById(R.id.room_price_text_view);
        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        confirmButton = findViewById(R.id.confirm_button);
        checkAvailabilityButton = findViewById(R.id.check_availability_button);
        checkInDateButton = findViewById(R.id.check_in_date_button);
        checkOutDateButton = findViewById(R.id.check_out_date_button);
        checkInDateTextView = findViewById(R.id.check_in_date_text_view);
        checkOutDateTextView = findViewById(R.id.check_out_date_text_view);

        // Get the room details from the intent
        String roomType = getIntent().getStringExtra("ROOM_TYPE");
        double roomPrice = getIntent().getDoubleExtra("ROOM_PRICE", 0.0);
        roomPriceTextView.setText("Price: $" + roomPrice);
        roomTypeTextView.setText("Room: " + roomType);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Bookings");

        // Set up date selection
        checkInDate = Calendar.getInstance();
        checkOutDate = Calendar.getInstance();

        // Set up date pickers
        checkInDateButton.setOnClickListener(v -> showDatePicker(true));
        checkOutDateButton.setOnClickListener(v -> showDatePicker(false));

        // Check availability and save booking
        checkAvailabilityButton.setOnClickListener(v -> checkAvailability());

        confirmButton.setOnClickListener(v -> saveBooking());
    }

    private void showDatePicker(boolean isCheckIn) {
        Calendar calendar = isCheckIn ? checkInDate : checkOutDate;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    if (isCheckIn) {
                        checkInDateTextView.setText("Check-In: " + dayOfMonth + "/" + (month + 1) + "/" + year);
                    } else {
                        checkOutDateTextView.setText("Check-Out: " + dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void checkAvailability() {
        // Logic to check room availability in the database
        // This is a placeholder for the actual implementation
        // You would query the Bookings table for overlapping dates
        boolean isAvailable = true; // Assume availability for demonstration

        if (isAvailable) {
            Toast.makeText(this, "Room is available for the selected dates.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Room not available on selected dates.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBooking() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String checkInDateString = checkInDate.getTime().toString();
        String checkOutDateString = checkOutDate.getTime().toString();
        String roomPrice = roomPriceTextView.getText().toString();
        String roomType = roomTypeTextView.getText().toString();


        // Create a Booking object
        Booking booking = new Booking(name, email, address, roomType, roomPrice, checkInDateString, checkOutDateString);

        // Save booking details to Firebase
        databaseReference.push().setValue(booking).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Booking confirmed!", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                Toast.makeText(this, "Failed to confirm booking. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}