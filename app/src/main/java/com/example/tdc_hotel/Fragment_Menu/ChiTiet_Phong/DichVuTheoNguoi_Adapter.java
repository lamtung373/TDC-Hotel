package com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Fragment_Menu.TimKiem.DanhGia_Adapter;
import com.example.tdc_hotel.R;

public class DichVuTheoNguoi_Adapter extends RecyclerView.Adapter<DichVuTheoNguoi_Adapter.DichVuTheoNguoi_Holder> {
    @NonNull
    @Override
    public DichVuTheoNguoi_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DichVuTheoNguoi_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_dichvutheonguoi, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DichVuTheoNguoi_Holder holder, int position) {
        holder.tvGiacu.setPaintFlags(holder.tvGiacu.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class DichVuTheoNguoi_Holder extends RecyclerView.ViewHolder {
        TextView tvGiacu;
        public DichVuTheoNguoi_Holder(@NonNull View itemView) {
            super(itemView);
            tvGiacu = itemView.findViewById(R.id.tvGiacu);
        }
    }
}
