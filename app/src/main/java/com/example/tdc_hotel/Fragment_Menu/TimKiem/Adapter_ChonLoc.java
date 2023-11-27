package com.example.tdc_hotel.Fragment_Menu.TimKiem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Chi_Tiet_Phong;
import com.example.tdc_hotel.Model.chi_tiet_tien_nghi;
import com.example.tdc_hotel.Model.phong;
import com.example.tdc_hotel.Model.tien_nghi;
import com.example.tdc_hotel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class Adapter_ChonLoc extends RecyclerView.Adapter<Adapter_ChonLoc.DanhGia_Holder> {
    private ArrayList<phong> danhsachphong = new ArrayList<>();
    Context context;
    private String orderBy;
    private boolean ascending;

    public Adapter_ChonLoc(Context context, String filter) {
        this.context = context;
        if (filter.equals("sale")) {
            orderBy = "sale";
            ascending = false; // Sắp xếp tăng dần cho trường sale
        } else if (filter.equals("danh_gia_sao")) {
            orderBy = "danh_gia_sao";
            ascending = true; // Sắp xếp giảm dần cho trường danh_gia_sao
        } else if (filter.equals("luot_thue")) {
            orderBy = "luot_thue";
            ascending = true; // Sắp xếp giảm dần cho trường luot_thue
        } else {
            orderBy = "id_phong"; // Đặt orderBy mặc định của bạn ở đây
            ascending = false; // Sắp xếp tăng dần cho trường sale
        }
        KhoiTao(orderBy, ascending);
    }

    @NonNull
    @Override
    public DanhGia_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_phong, parent, false);
        return new DanhGia_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhGia_Holder holder, int position) {
        phong data = danhsachphong.get(position);
        DecimalFormat formatter = new DecimalFormat("#");
        holder.tv_ratting.setText(String.valueOf(data.getDanh_gia_sao()));
        holder.tvTenphongFinding.setText(String.valueOf(data.getTen_phong()));
        holder.tv_giaphong.setText(formatter.format(data.getGia()) + " VNđ/đêm");
        holder.tv_luotThue.setText(data.getLuot_thue() + " Lượt thuê");
        if (data.getSale() != 0) {
            holder.tv_giaphong.setVisibility(View.VISIBLE);
            holder.tv_giaphong.setTextColor(Color.GRAY);
            holder.tv_giaphong.setTextSize(13);
            holder.tv_giaphong.setPaintFlags(holder.tv_giaphong.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_giaphong.setText(formatter.format(data.getGia()));
            holder.tv_salephong.setTextSize(15);
            holder.tv_salephong.setText(formatter.format(data.getSale()) + " VNĐ");
        } else {
            holder.tv_giaphong.setText(formatter.format(data.getGia()) + " VNĐ");
            holder.tv_salephong.setVisibility(View.GONE);
            holder.tv_giaphong.setTextColor(Color.RED);
            holder.tv_giaphong.setTextSize(15);
            holder.tv_giaphong.setPaintFlags(holder.tv_salephong.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        if (data.getAnh_phong() != null && !data.getAnh_phong().isEmpty()) {
            String imageUrl = data.getAnh_phong().get(0); // Use the first element or any element you want to display

            // Show ProgressBar
            holder.progressBar.setVisibility(View.VISIBLE);

            // Use Picasso to load the image
            Picasso.get().load(imageUrl).into(holder.iv_pictureFinding, new Callback() {
                @Override
                public void onSuccess() {
                    // Hide ProgressBar when successfully loaded
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    // Hide ProgressBar and handle error
                    holder.progressBar.setVisibility(View.GONE);
                    // You can set a default image if loading fails
                    holder.iv_pictureFinding.setImageResource(R.drawable.phong); // Replace with your default image
                }
            });
        }

        getTienNghi(data.getId_phong(), result -> holder.tv_tiennghi.setText(result));
        getSoLuongDanhGia(data.getId_phong(), count -> holder.tv_soluongdanhgia.setText(count + " Đánh giá"));


        //Sự kiện khi chọn 1 item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chi_Tiet_Phong.class);
                intent.putExtra("phong",data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhsachphong.size();
    }

    public class DanhGia_Holder extends RecyclerView.ViewHolder {
        ImageView iv_pictureFinding;
        ProgressBar progressBar;
        TextView tvTenphongFinding, tv_ratting, tv_soluongdanhgia, tv_luotThue, tv_tiennghi, tv_giaphong, tv_salephong;

        public DanhGia_Holder(@NonNull View itemView) {
            super(itemView);
            tvTenphongFinding = itemView.findViewById(R.id.tvTenphongRoom);
            tv_ratting = itemView.findViewById(R.id.tv_rattingRoom);
            tv_soluongdanhgia = itemView.findViewById(R.id.tv_soluongdanhgiaRoom);
            tv_luotThue = itemView.findViewById(R.id.tv_luotThueRoom);
            tv_tiennghi = itemView.findViewById(R.id.tv_tiennghiRoom);
            tv_giaphong = itemView.findViewById(R.id.tv_giaphongRoom);
            tv_salephong = itemView.findViewById(R.id.tv_salephongRoom);
            progressBar = itemView.findViewById(R.id.progressBarRoom);
            iv_pictureFinding=itemView.findViewById(R.id.iv_pictureRoom);
        }
    }

    private void KhoiTao(String filter, Boolean ascending) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("phong");
        databaseReference.orderByChild(filter).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhsachphong.clear(); // Clear the current list to avoid data accumulation on updates

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        phong room = dataSnapshot.getValue(phong.class);

                        if (room != null) {
                            if ("sale".equals(filter)) {
                                if (room.getSale() > 0) {
                                    danhsachphong.add(room);
                                }
                            } else {
                                danhsachphong.add(room);
                            }
                        }
                    }
                    if (ascending) {
                        // Đảo ngược thứ tự của danh sách
                        Collections.reverse(danhsachphong);
                    }
                    if (!filter.equals("id_phong")){
                        // Giữ lại chỉ 10 phòng đầu tiên
                        while (danhsachphong.size() > 10) {
                            danhsachphong.remove(danhsachphong.size() - 1);
                        }
                    }
                    notifyDataSetChanged(); // Notify the adapter that the data has changed
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }


    public interface DanhGiaCallback {
        void onDanhGiaCount(int count);
    }

    private void getSoLuongDanhGia(String id_phong, DanhGiaCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("danh_gia").child(id_phong);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int tongDanhGia = (int) snapshot.getChildrenCount();

                    // Call callback with the total count of ratings
                    callback.onDanhGiaCount(tongDanhGia);
                } else {
                    // Call callback with count 0 if there are no ratings
                    callback.onDanhGiaCount(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error when reading data
            }
        });
    }

    public interface TienNghiCallback {
        void onTienNghiBuilt(String result);
    }

    private void getTienNghi(String id_phong, TienNghiCallback callback) {
        DatabaseReference databaseReference_tiennghi = FirebaseDatabase.getInstance().getReference("chi_tiet_tien_nghi").child(id_phong);
        databaseReference_tiennghi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Create a StringBuilder to build the result string
                StringBuilder resultBuilder = new StringBuilder();

                for (DataSnapshot dataSnapshot_tiennghi : snapshot.getChildren()) {
                    chi_tiet_tien_nghi cttn = dataSnapshot_tiennghi.getValue(chi_tiet_tien_nghi.class);
                    DatabaseReference databaseReference_tiennghi = FirebaseDatabase.getInstance().getReference("tien_nghi");
                    databaseReference_tiennghi.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot_tiennghi : snapshot.getChildren()) {
                                tien_nghi tn = dataSnapshot_tiennghi.getValue(tien_nghi.class);
                                if (tn.getId_tien_nghi().equals(cttn.getId_tien_nghi()) && cttn.getSo_luong() > 0) {
                                    // Set the quantity
                                    tn.setSo_luong(cttn.getSo_luong());

                                    // Add tien_nghi to the StringBuilder
                                    resultBuilder.append(cttn.getSo_luong())
                                            .append(" ")
                                            .append(tn.getTen_tien_nghi())
                                            .append(" - ");
                                }
                            }

                            // Call the callback when the result string has been built
                            callback.onTienNghiBuilt(resultBuilder.toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error when reading data from "tien_nghi"
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error when reading data from "chi_tiet_tien_nghi"
            }
        });
    }
}
