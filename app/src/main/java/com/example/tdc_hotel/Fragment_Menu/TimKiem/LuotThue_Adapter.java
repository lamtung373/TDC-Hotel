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
import com.example.tdc_hotel.Model.phong;
import com.example.tdc_hotel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LuotThue_Adapter extends RecyclerView.Adapter<LuotThue_Adapter.LuotThue_Holder> {
    Context context;
    ArrayList<phong> data_phong = new ArrayList<>();

    public LuotThue_Adapter(Context context) {
        this.context = context;
        Initialization();
    }

    private void Initialization() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phong");
        reference.orderByChild("luot_thue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    phong phong = dataSnapshot.getValue(phong.class);
                    data_phong.add(0,phong);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public LuotThue_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LuotThue_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_phong, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LuotThue_Holder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chi_Tiet_Phong.class);
                intent.putExtra("phong",data_phong.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(data_phong.size()>10){
            return 10;
        }
        return data_phong.size();
    }

    class LuotThue_Holder extends RecyclerView.ViewHolder {

        public LuotThue_Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
