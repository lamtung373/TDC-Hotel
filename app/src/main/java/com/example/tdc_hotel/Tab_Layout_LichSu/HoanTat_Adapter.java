package com.example.tdc_hotel.Tab_Layout_LichSu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.R;

public class HoanTat_Adapter extends RecyclerView.Adapter<HoanTat_Adapter.HoanTat_Holder>{
    @NonNull
    @Override
    public HoanTat_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HoanTat_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_hoantat,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HoanTat_Holder holder, int position) {
        holder.btnDanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class HoanTat_Holder extends RecyclerView.ViewHolder {
        Button btnDanhgia;
        public HoanTat_Holder(@NonNull View itemView) {
            super(itemView);
            btnDanhgia=itemView.findViewById(R.id.btnDanhgia);
        }
    }
}
