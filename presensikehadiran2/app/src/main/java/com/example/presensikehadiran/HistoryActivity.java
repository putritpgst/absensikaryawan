package com.example.presensikehadiran;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<HistoryData> historyList = new ArrayList<>();
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);

        db = new Database(this); // Inisialisasi database SQLite

        // Memuat data history untuk user yang sedang login
        SharedPreferences prefs = getSharedPreferences("loginSession", MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        if (userId != null) {
            loadHistory(userId);
        } else {
            Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadHistory(String userId) {
        // Mengambil data riwayat absensi dari database SQLite
        Cursor cursor = db.getRiwayatAbsensi(userId);

        // Pastikan cursor tidak null dan ada data yang ditemukan
        if (cursor != null) {
            historyList.clear();
            Log.d("HistoryActivity", "Cursor has " + cursor.getCount() + " rows.");

            while (cursor.moveToNext()) {
                // Ambil data dari cursor
                String tanggal = cursor.getString(cursor.getColumnIndex("tanggal"));
                String jam = cursor.getString(cursor.getColumnIndex("jam"));
                String status = cursor.getString(cursor.getColumnIndex("status"));

                // Log data yang diambil
                Log.d("HistoryActivity", "Data: Tanggal=" + tanggal + ", Jam=" + jam + ", Status=" + status);

                // Tambahkan data ke dalam list
                historyList.add(new HistoryData(tanggal, jam, status));
            }

            // Tutup cursor setelah selesai digunakan
            cursor.close();
        } else {
            Log.d("HistoryActivity", "No data found for userId: " + userId);
        }

        // Notifikasi perubahan data ke adapter RecyclerView
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Memuat ulang data absensi ketika aktivitas kembali tampil
        SharedPreferences prefs = getSharedPreferences("loginSession", MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);
        if (userId != null) {
            loadHistory(userId);
        }
    }
}
