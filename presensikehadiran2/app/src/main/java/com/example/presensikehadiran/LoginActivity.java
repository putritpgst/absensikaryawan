package com.example.presensikehadiran;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    TextView Register;
    AppCompatButton login;
    EditText email1, password1;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // XML-mu sudah lengkap

        // Inisialisasi komponen
        Register = findViewById(R.id.btnregister);
        login = findViewById(R.id.login);
      email1 = findViewById(R.id.email1);
        password1 = findViewById(R.id.password1);
        db = new Database(this);

        // Tombol daftar
        Register.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActvity.class));
        });

        // Tombol login
        login.setOnClickListener(v -> {
            String email = email1.getText().toString().trim();
            String password = password1.getText().toString().trim();

            Cursor cursor = db.LoginUser(email, password);
            if (cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String nama = cursor.getString(cursor.getColumnIndex("nama"));
                String jabatan = cursor.getString(cursor.getColumnIndex("jabatan"));

                // Simpan sesi login
                SharedPreferences prefs = getSharedPreferences("loginSession", MODE_PRIVATE);
                prefs.edit()
                        .putString("user_id", id)
                        .putString("nama", nama)
                        .putString("jabatan", jabatan)
                        .apply();

                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
