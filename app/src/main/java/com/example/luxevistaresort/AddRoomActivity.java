package com.example.luxevistaresort;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddRoomActivity extends AppCompatActivity {

    private EditText roomType, roomDescription, roomPrice;
    private Button addRoomButton;
    private DatabaseReference databaseRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        roomType = findViewById(R.id.roomType);
        roomDescription = findViewById(R.id.roomDescription);
        roomPrice = findViewById(R.id.roomPrice);
        addRoomButton = findViewById(R.id.addRoomButton);

        // Initialize Firebase Database
        databaseRooms = FirebaseDatabase.getInstance().getReference("rooms");

        addRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoom();
            }
        });
    }

    private void addRoom() {
        String roomTypeValue = roomType.getText().toString().trim();
        String roomDescriptionValue = roomDescription.getText().toString().trim();
        String roomPriceValue = roomPrice.getText().toString().trim();

        Log.d("AddRoomActivity", "Room Type: " + roomTypeValue);
        Log.d("AddRoomActivity", "Room Description: " + roomDescriptionValue);
        Log.d("AddRoomActivity", "Room Price: " + roomPriceValue);

        if (roomTypeValue.isEmpty() || roomDescriptionValue.isEmpty() || roomPriceValue.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate price input
        double price;
        try {
            price = Double.parseDouble(roomPriceValue);
            if (price <= 0) {
                Toast.makeText(this, "Price must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
            return;
        }

        final String roomId = UUID.randomUUID().toString();

        // Create a Room object
        Room room = new Room(roomTypeValue, roomDescriptionValue, price, null);

        // Save room to the database
        databaseRooms.child(roomId).setValue(room).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddRoomActivity.this, "Room added successfully", Toast.LENGTH_SHORT).show();
                    // Clear fields after successful addition
                    roomType.setText("");
                    roomDescription.setText("");
                    roomPrice.setText("");
                } else {
                    // Log the error message
                    Log.e("AddRoomActivity", "Failed to add room", task.getException());
                    Toast.makeText(AddRoomActivity.this, "Failed to add room: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}