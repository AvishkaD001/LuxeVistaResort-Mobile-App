package com.example.luxevistaresort;

public class Service {
    private String serviceType;
    private String description;
    private double price;
    private String imageUrl; // Optional, can be null

    // Default constructor (required for Firebase)
    public Service() {
        // Empty constructor required for Firebase
    }

    // Parameterized constructor for initializing the Service object
    public Service(String serviceType, String description, double price, String imageUrl) {
        this.serviceType = serviceType;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Service(String name, String email, String address, String serviceType, String servicePrice, String checkInDateString, String checkOutDateString) {
    }

    // Getter methods
    public String getServiceType() {
        return serviceType;
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
