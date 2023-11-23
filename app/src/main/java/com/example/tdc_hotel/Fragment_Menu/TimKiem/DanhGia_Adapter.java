package com.example.tdc_hotel.Fragment_Menu.TimKiem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Model.chi_tiet_tien_nghi;
import com.example.tdc_hotel.Model.danh_gia;
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

public class DanhGia_Adapter extends RecyclerView.Adapter<DanhGia_Adapter.DanhGia_Holder> {
    private ArrayList<phong> danhsachphong = new ArrayList<>();
    Context context;


    public DanhGia_Adapter(Context context, String filter) {
        this.context = context;
        DanhGia(filter);
    }

    @NonNull
    @Override
    public DanhGia_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_kqtimkiem, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return danhsachphong.size();
    }

    public class DanhGia_Holder extends RecyclerView.ViewHolder {
        ImageView iv_pictureFinding;
        ProgressBar progressBar;
        TextView tvTenphongFinding, tv_ratting, tv_soluongdanhgia, tv_luotThue, tv_tiennghi, tv_giaphong;

        public DanhGia_Holder(@NonNull View itemView) {
            super(itemView);
            iv_pictureFinding = itemView.findViewById(R.id.iv_pictureFinding);
            tvTenphongFinding = itemView.findViewById(R.id.tvTenphongFinding);
            tv_ratting = itemView.findViewById(R.id.tv_ratting);
            tv_soluongdanhgia = itemView.findViewById(R.id.tv_soluongdanhgia);
            tv_luotThue = itemView.findViewById(R.id.tv_luotThue);
            tv_tiennghi = itemView.findViewById(R.id.tv_tiennghi);
            tv_giaphong = itemView.findViewById(R.id.tv_giaphong);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void DanhGia(String filter) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("phong");
        databaseReference.orderByChild(filter).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhsachphong.clear(); // Clear the current list to avoid data accumulation on updates

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        phong room = dataSnapshot.getValue(phong.class);
                        if (room != null) {
                            danhsachphong.add(room);
                        }
                    }
                    if (!filter.equals("gia")) {
                        // Đảo ngược thứ tự của danh sách
                        Collections.reverse(danhsachphong);

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
