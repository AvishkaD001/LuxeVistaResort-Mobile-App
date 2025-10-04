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


public class AddServiceActivity extends AppCompatActivity {

    private EditText ServiceType, ServiceDescription, ServicePrice;
    private Button addServiceButton;
    private DatabaseReference databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        ServiceType = findViewById(R.id.ServiceType);
        ServiceDescription = findViewById(R.id.ServiceDescription);
        ServicePrice = findViewById(R.id.ServicePrice);
        addServiceButton = findViewById(R.id.addServiceButton);

        // Initialize Firebase Database
        databaseService = FirebaseDatabase.getInstance().getReference("Services");

        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addService();
            }
        });
    }

    private void addService() {
        String ServiceTypeValue = ServiceType.getText().toString().trim();
        String ServiceDescriptionValue = ServiceDescription.getText().toString().trim();
        String ServicePriceValue = ServicePrice.getText().toString().trim();

        Log.d("AddServiceActivity", "Service Type: " + ServiceTypeValue);
        Log.d("AddServiceActivity", "Service Description: " + ServiceDescriptionValue);
        Log.d("AddServiceActivity", "Service Price: " + ServicePriceValue);

        if (ServiceTypeValue.isEmpty() || ServiceDescriptionValue.isEmpty() || ServicePriceValue.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate price input
        double price;
        try {
            price = Double.parseDouble(ServicePriceValue);
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
        Service service = new Service(ServiceTypeValue, ServiceDescriptionValue, price, null);

        // Save room to the database
        databaseService.child(roomId).setValue(service).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddServiceActivity.this, "Service added successfully", Toast.LENGTH_SHORT).show();
                    // Clear fields after successful addition
                    ServiceType.setText("");
                    ServiceDescription.setText("");
                    ServicePrice.setText("");
                } else {
                    // Log the error message
                    Log.e("AddServiceRoomActivity", "Failed to add Service", task.getException());
                    Toast.makeText(AddServiceActivity.this, "Failed to add Service: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}