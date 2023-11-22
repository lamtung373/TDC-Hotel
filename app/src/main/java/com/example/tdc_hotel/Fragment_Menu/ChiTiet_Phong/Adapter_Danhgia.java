package com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Model.danh_gia;
import com.example.tdc_hotel.Model.khach_hang;
import com.example.tdc_hotel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter_Danhgia extends RecyclerView.Adapter<Adapter_Danhgia.Danhgia_Holder> {
    String id_phong;
    ArrayList<danh_gia> data_danhgia = new ArrayList<>();

    public Adapter_Danhgia(String id_phong) {
        this.id_phong = id_phong;
        Initialization();
    }

    private void Initialization() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("danh_gia").child(id_phong);
        DatabaseReference reference_kh = FirebaseDatabase.getInstance().getReference("khach_hang");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    danh_gia danhGia = dataSnapshot.getValue(danh_gia.class);
                    reference_kh.child(danhGia.getSo_dien_thoai()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                khach_hang khach_hang = snapshot.getValue(khach_hang.class);
                                danhGia.setTen_khach_hang(khach_hang.getTen());
                                data_danhgia.add(danhGia);
                                Log.e("a","aaa");
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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
    public Danhgia_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Danhgia_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_danhgia, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Danhgia_Holder holder, int position) {
        danh_gia danhGia=data_danhgia.get(holder.getAdapterPosition());
        holder.tv_ten.setText(danhGia.getTen_khach_hang());
        holder.tv_danhgia.setText(danhGia.getSo_sao()+"");
        holder.tv_mota.setText(danhGia.getChi_tiet_danh_gia());
        holder.tv_thoigian.setText(danhGia.getThoi_gian());
    }

    @Override
    public int getItemCount() {
        return data_danhgia.size();
    }

    class Danhgia_Holder extends RecyclerView.ViewHolder {
        TextView tv_ten, tv_danhgia, tv_mota, tv_thoigian;

        public Danhgia_Holder(@NonNull View itemView) {
            super(itemView);
            tv_ten = itemView.findViewById(R.id.tv_ten);
            tv_danhgia = itemView.findViewById(R.id.tv_danhgia);
            tv_mota = itemView.findViewById(R.id.tv_mota);
            tv_thoigian = itemView.findViewById(R.id.tv_thoigian);
        }
    }
}
