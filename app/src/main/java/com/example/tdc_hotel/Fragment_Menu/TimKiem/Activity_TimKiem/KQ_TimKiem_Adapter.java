package com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Danh_Gia;
import com.example.tdc_hotel.Fragment_Menu.TimKiem.DanhGia_Adapter;
import com.example.tdc_hotel.R;

public class KQ_TimKiem_Adapter extends  RecyclerView.Adapter<KQ_TimKiem_Adapter.KQ_TimKiem_Holder>{

Intent intent;
Context context;
    @NonNull
    @Override
    public KQ_TimKiem_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        intent=new Intent(parent.getContext(), Danh_Gia.class);
        context=parent.getContext();
        return new KQ_TimKiem_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_kqtimkiem,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull KQ_TimKiem_Holder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class KQ_TimKiem_Holder extends RecyclerView.ViewHolder {
        public KQ_TimKiem_Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
