package com.example.presensikehadiran;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView camera, history, visualisasi, profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the ImageViews by their IDs
        camera = findViewById(R.id.camera2);
        history = findViewById(R.id.history);
        visualisasi = findViewById(R.id.visualisasi);
        profil = findViewById(R.id.profil);

        // Set onClickListeners for each ImageView
        camera.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
        });

        history.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        visualisasi.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VisualisasiActivity.class);
            startActivity(intent);
        });

        profil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}

