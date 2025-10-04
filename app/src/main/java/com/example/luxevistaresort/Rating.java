package com.example.luxevistaresort;

public class Rating {
    public String fullname;
    public String feedback;
    public float rating;

    public Rating() {
        // Default constructor required for Firebase
    }

    public Rating(String fullname, String feedback, float rating) {
        this.fullname = fullname;
        this.feedback = feedback;
        this.rating = rating;
    }


}
