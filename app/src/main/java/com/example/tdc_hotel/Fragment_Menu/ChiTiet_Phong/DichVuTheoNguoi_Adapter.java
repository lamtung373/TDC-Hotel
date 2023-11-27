package com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Model.dich_vu;
import com.example.tdc_hotel.Model.loai_dich_vu;
import com.example.tdc_hotel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DichVuTheoNguoi_Adapter extends RecyclerView.Adapter<DichVuTheoNguoi_Adapter.DichVuTheoNguoi_Holder> {
    ArrayList<dich_vu> data_dv=new ArrayList<>();

    public ArrayList<dich_vu> getData_dv() {
        return data_dv;
    }

    public void setData_dv(ArrayList<dich_vu> data_dv) {
        this.data_dv = data_dv;
    }

    public DichVuTheoNguoi_Adapter() {
        Initialization();
    }

    @NonNull
    @Override
    public DichVuTheoNguoi_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DichVuTheoNguoi_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_dichvutheonguoi, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DichVuTheoNguoi_Holder holder, int position) {
        if(!data_dv.isEmpty()&&data_dv!=null) {
            holder.tv_dv.setText(data_dv.get(position).getTen_dich_vu());
            holder.tvGia.setText(data_dv.get(position).getGia_dich_vu() + "đ/người");
            holder.ivCong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int soluong = Integer.parseInt(holder.edtSonguoi.getText().toString().trim()) + 1;
                    holder.edtSonguoi.setText(soluong + "");
                    data_dv.get(holder.getAdapterPosition()).setSo_luong(soluong);
                }
            });
            holder.ivTru.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int soluong = Integer.parseInt(holder.edtSonguoi.getText().toString().trim()) - 1;
                    if (soluong >= 0) {
                        holder.edtSonguoi.setText(soluong + "");
                        data_dv.get(holder.getAdapterPosition()).setSo_luong(soluong);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class DichVuTheoNguoi_Holder extends RecyclerView.ViewHolder {
        TextView tv_dv, tvGia;
        EditText edtSonguoi;
        ImageView ivTru, ivCong;
        public DichVuTheoNguoi_Holder(@NonNull View itemView) {
            super(itemView);
            tv_dv = itemView.findViewById(R.id.tvdichvu);
            edtSonguoi = itemView.findViewById(R.id.edtSonguoi);
            ivTru = itemView.findViewById(R.id.ivTru);
            ivCong = itemView.findViewById(R.id.ivCong);
            tvGia = itemView.findViewById(R.id.tvGia);
        }
    }
    private void Initialization() {

        DatabaseReference reference_dichvu = FirebaseDatabase.getInstance().getReference("dich_vu");
        DatabaseReference reference_loaidichvu = FirebaseDatabase.getInstance().getReference("loai_dich_vu");
        reference_dichvu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference_loaidichvu.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        data_dv.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            loai_dich_vu loaiDichVu = dataSnapshot.getValue(loai_dich_vu.class);
                            reference_dichvu.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (loaiDichVu.getTen_loai_dich_vu().equals("Người")) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            dich_vu dichVu = dataSnapshot.getValue(dich_vu.class);
                                            if (dichVu.getId_loai_dich_vu().equals(loaiDichVu.getId_loai_dich_vu())) {
                                                data_dv.add(dichVu);
                                            }
                                        }
                                        notifyDataSetChanged();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
