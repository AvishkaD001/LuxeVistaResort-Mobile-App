package com.example.luxevistaresort;


public class Room {
    private String roomType;
    private String description;
    private double price;
    private String imageUrl; // Optional, can be null

    public Room() {
        // Default constructor required for calls to DataSnapshot.getValue(Room.class)
    }

    public Room(String roomType, String description, double price, String imageUrl) {
        this.roomType = roomType;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}