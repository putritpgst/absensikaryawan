package com.example.presensikehadiran;

public class HistoryData {

    private String tanggal;
    private String jam;
    private String status;

    public HistoryData(String tanggal, String jam, String status) {
        this.tanggal = tanggal;
        this.jam = jam;
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJam() {
        return jam;
    }

    public String getStatus() {
        return status;
    }
}
