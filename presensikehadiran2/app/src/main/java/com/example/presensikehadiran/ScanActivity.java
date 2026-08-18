package com.example.presensikehadiran;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScanActivity extends AppCompatActivity {

    private CaptureManager captureManager;
    private DecoratedBarcodeView barcodeView;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        barcodeView = new DecoratedBarcodeView(this);
        setContentView(barcodeView);

        db = new Database(this);

        captureManager = new CaptureManager(this, barcodeView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);

        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                handleDecode(result.getText());
            }

            @Override
            public void possibleResultPoints(java.util.List<ResultPoint> resultPoints) {
            }
        });

        captureManager.decode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    public void handleDecode(String contents) {
        SharedPreferences prefs = getSharedPreferences("loginSession", MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);
        String namaUser = prefs.getString("nama", null);

        if (userId != null && namaUser != null) {
            try {
                // Mengambil data dari QR Code
                StringTokenizer tokens = new StringTokenizer(contents, "#");
                String npak = tokens.nextToken(); // NIP atau NPAK
                String nama = tokens.nextToken();

                String uid = UUID.randomUUID().toString();  // Generate UUID untuk absensi
                String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String jam = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                Log.v("SCAN", uid + " | " + npak + " | " + nama + " | " + tanggal + " | " + jam);

                // Cek apakah sudah absen
                boolean isSaved = db.addAbsensi(uid, userId, namaUser, tanggal, jam, "Hadir");

                if (isSaved) {
                    Toast.makeText(this, "Absen berhasil.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Anda sudah absen!", Toast.LENGTH_SHORT).show();
                }

                // Kembali ke MainActivity setelah pemindaian
                startActivity(new Intent(ScanActivity.this, MainActivity.class));
                finish();

            } catch (Exception e) {
                Toast.makeText(this, "Format QR tidak valid", Toast.LENGTH_SHORT).show();
                Log.e("QR_ERROR", "Format error: " + contents);
            }
        } else {
            Toast.makeText(this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }
}
