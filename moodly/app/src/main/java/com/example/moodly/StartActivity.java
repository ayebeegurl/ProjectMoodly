package com.example.moodly;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);

        // Set up window insets to handle system UI visibility
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the login button click listener
        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up the registration button click listener
        Button registButton = findViewById(R.id.regist);
        registButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, RegistActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources if necessary
    }
}