package com.example.presensikehadiran;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface Apiservice {

    @GET("simpan.php")
    Call<Result> simpanDataCloud(
            @Query("apikey") String apiKey,
            @Query("id_absensi") String idAbsensi,
            @Query("id_user") String idUser,
            @Query("nama_user") String namaUser,
            @Query("tanggal") String tanggal,
            @Query("jam") String jam,
            @Query("status") String status
    );

    @GET("history.php")
    Call<HistoryResponse> getHistory(
            @Query("apikey") String apikey,
            @Query("id_user") String userId
    );
}
