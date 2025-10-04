package com.example.luxevistaresort;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLogin extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    // Hardcoded admin credentials
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login_activity);

        // Initialize views
        emailEditText = findViewById(R.id.emailText);
        passwordEditText = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.loginButton);

        // Set up login button listener
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateInputs(email, password)) {
                authenticateAdmin(email, password);
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            return false;
        }

        return true;
    }

    private void authenticateAdmin(String email, String password) {
        // Compare input with hardcoded credentials
        if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
            Toast.makeText(AdminLogin.this, "Login successful.", Toast.LENGTH_SHORT).show();

            // Redirect to admin home activity
            startActivity(new Intent(AdminLogin.this, Adminhome.class));
            finish();
        } else {
            Toast.makeText(AdminLogin.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
        }
    }
}
