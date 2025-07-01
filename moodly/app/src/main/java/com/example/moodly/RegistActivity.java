package com.example.moodly;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moodly.data.DatabaseHelper;
import com.example.moodly.data.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegistActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Fallback to plain text if hashing fails
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regist);

        databaseHelper = new DatabaseHelper(this);
        EditText inputName = findViewById(R.id.regist_username);
        EditText inputEmail = findViewById(R.id.regist_email);
        EditText inputPassword = findViewById(R.id.regist_password);
        Button registButton = findViewById(R.id.register);

        registButton.setOnClickListener(v -> {
            String username = inputName.getText().toString().trim();
            String email = inputEmail.getText().toString().trim().toLowerCase();
            String password = inputPassword.getText().toString().trim();

            if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                String hashedPassword = hashPassword(password);
                User user = new User(username, email, hashedPassword);
                long id = databaseHelper.addUser(user);
                if (id > 0) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegistActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        });
    }
}