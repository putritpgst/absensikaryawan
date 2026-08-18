package com.example.presensikehadiran;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "absensi.db";
    private static final int DATABASE_VERSION = 2; // Update jika ada perubahan struktur

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabel user untuk login dan register
        db.execSQL("CREATE TABLE user (" +
                "id TEXT PRIMARY KEY, " +
                "nama TEXT, " +
                "jabatan TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT)");

        // Tabel absensi lengkap dengan UUID dan status
        db.execSQL("CREATE TABLE history_absensi (" +
                "id_absensi TEXT PRIMARY KEY, " +      // UUID absensi
                "id_user TEXT, " +                      // ID user yang absen
                "nama_user TEXT, " +
                "tanggal TEXT, " +
                "jam TEXT, " +
                "status TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS history_absensi");
        onCreate(db);
    }

    // ====================
    // Register dan Login
    // ====================
    public boolean RegisterUser(String id, String nama, String jabatan, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("nama", nama);
        values.put("jabatan", jabatan);
        values.put("email", email);
        values.put("password", password);
        long result = db.insert("user", null, values);
        return result != -1;
    }

    public Cursor LoginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM user WHERE email = ? AND password = ?", new String[]{email, password});
    }

    // ====================
    // Tambah Data Absensi
    // ====================
    public boolean addAbsensi(String idAbsensi, String userId, String namaUser, String tanggal, String jam, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Cek apakah user sudah absen hari ini
        Cursor cursor = db.rawQuery("SELECT * FROM history_absensi WHERE id_user = ? AND tanggal = ?", new String[]{userId, tanggal});
        boolean sudahAbsen = cursor.getCount() > 0;
        cursor.close();

        if (sudahAbsen) {
            return false; // Gagal karena sudah absen
        }

        ContentValues values = new ContentValues();
        values.put("id_absensi", idAbsensi);
        values.put("id_user", userId);
        values.put("nama_user", namaUser);
        values.put("tanggal", tanggal);
        values.put("jam", jam);
        values.put("status", status);

        long result = db.insert("history_absensi", null, values);
        return result != -1;
    }

    // ====================
    // Ambil Riwayat Absensi per User
    // ====================
    public Cursor getRiwayatAbsensi(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT tanggal, jam, status FROM history_absensi WHERE id_user = ? ORDER BY tanggal DESC", new String[]{userId});
    }

}