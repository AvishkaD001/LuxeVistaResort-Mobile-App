package com.example.luxevistaresort;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    // Define the form fields and buttons
    private EditText fullNameEditText, emailEditText, phoneEditText, passwordEditText;
    private CheckBox termsConditionsCheckBox;
    private Button signUpButton;
    private TextView loginLink;

    // Firebase Auth and Database instances
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_signup); // Set the layout for this activity

        // Initialize the form fields and buttons
        fullNameEditText = findViewById(R.id.full_name);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone_number);
        passwordEditText = findViewById(R.id.password);
        termsConditionsCheckBox = findViewById(R.id.terms_conditions);
        signUpButton = findViewById(R.id.signup_button);
        loginLink = findViewById(R.id.login_link);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set up the Sign-Up Button click listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from the form
                String fullName = fullNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                boolean isTermsChecked = termsConditionsCheckBox.isChecked();

                // Validate input
                if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!isTermsChecked) {
                    Toast.makeText(MainActivity.this, "You must agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
                } else {
                    // Sign up the user using Firebase Authentication
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    // Get the Firebase user ID
                                    String userId = mAuth.getCurrentUser().getUid();

                                    // Create a new User object
                                    User user = new User(fullName, email, phone);

                                    // Save the user data to Firebase Realtime Database
                                    mDatabase.child("users").child(userId).setValue(user)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    // Navigate to the login screen
                                                    Toast.makeText(MainActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(MainActivity.this, ActiveLoginpage.class);
                                                    startActivity(intent);
                                                    finish(); // Close the sign-up screen
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(MainActivity.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // Set up the "Already have an account? Login" link
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Login page
                Intent intent = new Intent(MainActivity.this, ActiveLoginpage.class); // Replace with your LoginActivity
                startActivity(intent);
            }


        });
    }

    // User class to represent the user data
    public static class User {
        public String fullName;
        public String email;
        public String phone;

        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        public User() {}

        public User(String fullName, String email, String phone) {
            this.fullName = fullName;
            this.email = email;
            this.phone = phone;
        }
    }
}
