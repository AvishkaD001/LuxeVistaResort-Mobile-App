package com.example.luxevistaresort;


import android.os.Bundle;
import android.text.TextUtils;
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

public class    ViewRoomActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private EditText searchBar, minPrice, maxPrice;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rooms);

        recyclerView = findViewById(R.id.recyclerView);
        searchBar = findViewById(R.id.searchBar);
        minPrice = findViewById(R.id.minPrice);
        maxPrice = findViewById(R.id.maxPrice);
        searchButton = findViewById(R.id.searchButton);

        roomList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomAdapter = new RoomAdapter(roomList, this);
        recyclerView.setAdapter(roomAdapter);

        fetchRooms();

        searchButton.setOnClickListener(v -> filterRooms());
    }

    private void fetchRooms() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("rooms");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Room room = snapshot.getValue(Room.class);
                    roomList.add(room);
                }
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }


    private void filterRooms() {
        String searchText = searchBar.getText().toString().toLowerCase();
        String minPriceText = minPrice.getText().toString();
        String maxPriceText = maxPrice.getText().toString();

        List<Room> filteredList = new ArrayList<>();
        for (Room room : roomList) {
            boolean matchesSearch = room.getRoomType().toLowerCase().contains(searchText);
            boolean matchesPrice = true;

            if (!TextUtils.isEmpty(minPriceText)) {
                matchesPrice = room.getPrice() >= Double.parseDouble(minPriceText);
            }
            if (!TextUtils.isEmpty(maxPriceText)) {
                matchesPrice = matchesPrice && room.getPrice() <= Double.parseDouble(maxPriceText);
            }

            if (matchesSearch && matchesPrice) {
                filteredList.add(room);
            }
        }

        roomAdapter = new RoomAdapter(filteredList, this);
        recyclerView.setAdapter(roomAdapter);
    }
}