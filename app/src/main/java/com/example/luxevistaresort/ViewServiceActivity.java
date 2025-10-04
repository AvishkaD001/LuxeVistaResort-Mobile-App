package com.example.luxevistaresort;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewServiceActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;
    private EditText searchBar, minPrice, maxPrice;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_services);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        searchBar = findViewById(R.id.searchBar);
        minPrice = findViewById(R.id.minPrice);
        maxPrice = findViewById(R.id.maxPrice);
        searchButton = findViewById(R.id.searchButton);

        // Initialize service list and RecyclerView
        serviceList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceAdapter(serviceList, this);
        recyclerView.setAdapter(serviceAdapter);

        fetchServices(); // Fetch services from Firebase

        // Set up search button click listener
        searchButton.setOnClickListener(v -> filterServices());
    }

    // Fetch services from Firebase Database
    private void fetchServices() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceList.clear(); // Clear previous data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = snapshot.getValue(Service.class);
                    if (service != null) { // Avoid NullPointerException
                        serviceList.add(service); // Add the service to the list
                        Log.d("ViewServiceActivity", "Service fetched: " + service.getServiceType());
                    }
                }
                serviceAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
                Log.d("ViewServiceActivity", "Services fetched: " + serviceList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors (e.g., log error)
                Log.e("ViewServiceActivity", "Error fetching services", databaseError.toException());
            }
        });
    }

    // Filter services based on search and price range
    private void filterServices() {
        String searchText = searchBar.getText().toString().toLowerCase();
        String minPriceText = minPrice.getText().toString();
        String maxPriceText = maxPrice.getText().toString();

        List<Service> filteredList = new ArrayList<>();

        // Check if the serviceList is empty (Firebase data not yet loaded)
        if (serviceList.isEmpty()) {
            Log.d("ViewServiceActivity", "Service list is empty, nothing to filter");
            return;
        }

        for (Service service : serviceList) {
            boolean matchesSearch = service.getServiceType().toLowerCase().contains(searchText);
            boolean matchesPrice = true;

            // Check price range if provided
            if (!TextUtils.isEmpty(minPriceText)) {
                try {
                    double min = Double.parseDouble(minPriceText);
                    if (service.getPrice() < min) {
                        matchesPrice = false;
                    }
                } catch (NumberFormatException e) {
                    minPrice.setError("Invalid min price");
                    return;
                }
            }
            if (!TextUtils.isEmpty(maxPriceText)) {
                try {
                    double max = Double.parseDouble(maxPriceText);
                    if (service.getPrice() > max) {
                        matchesPrice = false;
                    }
                } catch (NumberFormatException e) {
                    maxPrice.setError("Invalid max price");
                    return;
                }
            }

            // Add to filtered list if it matches both search and price criteria
            if (matchesSearch && matchesPrice) {
                filteredList.add(service);
            }
        }

        // Update adapter with filtered list without replacing the adapter itself
        serviceAdapter.updateServices(filteredList);
    }
}
