package com.example.presensikehadiran;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<HistoryData> historyList;

    public HistoryAdapter(List<HistoryData> historyList) {
        this.historyList = historyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryData data = historyList.get(position);
        holder.tanggal.setText(data.getTanggal());
        holder.jam.setText(data.getJam());
        holder.status.setText(data.getStatus());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tanggal, jam, status;

        public ViewHolder(View itemView) {
            super(itemView);
            tanggal = itemView.findViewById(R.id.tanggal);
            jam = itemView.findViewById(R.id.jam);
            status = itemView.findViewById(R.id.status);
        }
    }
}
