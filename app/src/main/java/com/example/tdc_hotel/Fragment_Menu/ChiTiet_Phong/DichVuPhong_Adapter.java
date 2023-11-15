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

public class DichVuPhong_Adapter extends RecyclerView.Adapter<DichVuPhong_Adapter.DichVuPhong_Holder> {
    @NonNull
    @Override
    public DichVuPhong_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DichVuPhong_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_dichvuphong, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DichVuPhong_Holder holder, int position) {
        holder.tvGiacu.setPaintFlags(holder.tvGiacu.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class DichVuPhong_Holder extends RecyclerView.ViewHolder {
        TextView tvGiacu;

        public DichVuPhong_Holder(@NonNull View itemView) {
            super(itemView);
            tvGiacu = itemView.findViewById(R.id.tvGiacu);
        }
    }
}
