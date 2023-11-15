package com.example.tdc_hotel.Fragment_Menu.TimKiem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.R;

public class Gia_Adapter extends RecyclerView.Adapter<Gia_Adapter.Gia_Holder>{
    @NonNull
    @Override
    public Gia_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Gia_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_phong,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Gia_Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class Gia_Holder extends RecyclerView.ViewHolder {
        public Gia_Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
