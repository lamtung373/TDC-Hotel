package com.example.tdc_hotel.Fragment_Menu.TimKiem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Chi_Tiet_Phong;
import com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem.Ket_Qua_Tim_Kiem;
import com.example.tdc_hotel.R;

public class LuotThue_Adapter extends RecyclerView.Adapter<LuotThue_Adapter.LuotThue_Holder>{
    Context context;
    public LuotThue_Adapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public LuotThue_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LuotThue_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_phong,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LuotThue_Holder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Chi_Tiet_Phong.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class LuotThue_Holder extends RecyclerView.ViewHolder{

        public LuotThue_Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
