package com.example.luxevistaresort;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> roomList;
    private Context context;

    public RoomAdapter(List<Room> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.roomType.setText(room.getRoomType());
        holder.roomDescription.setText(room.getDescription());
        holder.roomPrice.setText("$" + room.getPrice());
        Glide.with(context).load(room.getImageUrl()).into(holder.roomImage);

        holder.bookRoomButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingActivity.class);
            // Pass room details to the BookingActivity
            intent.putExtra("ROOM_TYPE", room.getRoomType());
            intent.putExtra("ROOM_DESCRIPTION", room.getDescription());
            intent.putExtra("ROOM_PRICE", room.getPrice());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomType, roomDescription, roomPrice;
        ImageView roomImage;
        Button bookRoomButton;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomType = itemView.findViewById(R.id.roomType);
            roomDescription = itemView.findViewById(R.id.roomDescription);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            roomImage = itemView.findViewById(R.id.roomImage);
            bookRoomButton = itemView.findViewById(R.id.bookRoomButton);
        }
    }
}