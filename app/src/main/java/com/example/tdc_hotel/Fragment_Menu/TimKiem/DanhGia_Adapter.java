package com.example.tdc_hotel.Fragment_Menu.TimKiem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.R;

public class DanhGia_Adapter extends RecyclerView.Adapter<DanhGia_Adapter.DanhGia_Holder>{
    @NonNull
    @Override
    public DanhGia_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DanhGia_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_phong,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull DanhGia_Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class DanhGia_Holder extends RecyclerView.ViewHolder{

        public DanhGia_Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
