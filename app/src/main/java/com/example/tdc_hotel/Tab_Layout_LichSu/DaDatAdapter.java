package com.example.tdc_hotel.Tab_Layout_LichSu;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DaDatAdapter extends  RecyclerView.Adapter<DaDatAdapter.DaDatHolder>{
    private List<hoa_>
    @NonNull
    @Override
    public DaDatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DaDatHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DaDatHolder extends RecyclerView.ViewHolder {

        public DaDatHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
