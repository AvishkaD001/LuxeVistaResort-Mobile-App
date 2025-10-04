package com.example.luxevistaresort;

public class Reservation {
    private String name;
    private String email;
    private String address;
    private String ServiceType;
    private String ServicePrice;
    private String checkInDate;
    private String checkOutDate;

    public Reservation() {
        // Default constructor required for calls to DataSnapshot.getValue(Booking.class)
    }

    public Reservation(String name, String email, String address, String ServicePrice, String ServiceType, String checkInDate, String checkOutDate) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.ServiceType = ServiceType;
        this.ServicePrice = ServicePrice;
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

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String ServiceType) {
        this.ServiceType = ServiceType;
    }


    public String getServicePrice() {
        return ServicePrice;
    }

    public void getServicePrice(String ServicePrice) {
        this.ServicePrice = ServicePrice;
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
        return "Reservations{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", ServiceType='" + ServiceType + '\'' +
                ", ServicePrice='" + ServicePrice + '\'' +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                '}';
    }
}

