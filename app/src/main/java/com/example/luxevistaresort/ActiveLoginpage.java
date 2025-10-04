package com.example.luxevistaresort;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class ActiveLoginpage extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private CheckBox termsConditionsCheckBox;
    private Button loginButton;
    private TextView signUpLink;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        termsConditionsCheckBox = findViewById(R.id.terms_conditions);
        loginButton = findViewById(R.id.loginButton);
        signUpLink = findViewById(R.id.signUpLink);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            boolean isTermsChecked = termsConditionsCheckBox.isChecked();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(ActiveLoginpage.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(ActiveLoginpage.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isTermsChecked) {
                Toast.makeText(ActiveLoginpage.this, "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show();
                return;
            }

            ProgressDialog progressDialog = new ProgressDialog(ActiveLoginpage.this);
            progressDialog.setMessage("Logging in...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            loginButton.setEnabled(false);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        progressDialog.dismiss();
                        loginButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(ActiveLoginpage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActiveLoginpage.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            String errorMessage;
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                errorMessage = "No account found with this email.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                errorMessage = "Incorrect password.";
                            } catch (Exception e) {
                                errorMessage = "Authentication failed. Please try again.";
                            }
                            Toast.makeText(ActiveLoginpage.this, errorMessage, Toast.LENGTH_SHORT).show();
                            passwordEditText.setText("");
                        }
                    });
        });

        signUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(ActiveLoginpage.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
