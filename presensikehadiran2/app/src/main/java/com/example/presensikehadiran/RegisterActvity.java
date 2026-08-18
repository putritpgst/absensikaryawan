package com.example.presensikehadiran;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.UUID;

public class RegisterActvity extends AppCompatActivity {

    // Deklarasi komponen
    EditText nama, jabatan, email2, password2;
    AppCompatButton btnregister;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Pastikan layout ini ada dan benar

        // Inisialisasi komponen
        nama = findViewById(R.id.nama);
        jabatan = findViewById(R.id.jabatan);
        email2 = findViewById(R.id.email2);
        password2 = findViewById(R.id.password2);
        btnregister = findViewById(R.id.btnregister);

        // Inisialisasi database
        db = new Database(this);

        // Tombol daftar
        btnregister.setOnClickListener(v -> {
            String id = UUID.randomUUID().toString();  // Membuat ID unik untuk user
            String nama = this.nama.getText().toString().trim();
            String jabatan = this.jabatan.getText().toString().trim();
            String email = this.email2.getText().toString().trim();
            String password = this.password2.getText().toString().trim();

            // Validasi input
            if (nama.isEmpty() || jabatan.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Panggil metode RegisterUser dari Database
            boolean sukses = db.RegisterUser(id, nama, jabatan, email, password);
            if (sukses) {
                Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                // Setelah berhasil register, alihkan ke LoginActivity
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Email sudah digunakan!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
