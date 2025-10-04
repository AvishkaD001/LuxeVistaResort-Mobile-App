package com.example.luxevistaresort;


public class Booking {
    private String name;
    private String email;
    private String address;
    private String roomType;

    private String roomPrice;
    private String checkInDate;
    private String checkOutDate;

    public Booking() {
        // Default constructor required for calls to DataSnapshot.getValue(Booking.class)
    }

    public Booking(String name, String email, String address, String roomPrice, String roomType, String checkInDate, String checkOutDate) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }


    public String getRoomPrice() {
        return roomPrice;
    }

    public void getRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", roomPrice='" + roomPrice + '\'' +
                ", roomType='" + roomType + '\'' +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                '}';
    }
}