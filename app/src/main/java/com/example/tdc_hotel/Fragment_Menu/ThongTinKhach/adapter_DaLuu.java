package com.example.tdc_hotel.Fragment_Menu.ThongTinKhach;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Chi_Tiet_Phong;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class adapter_DaLuu extends RecyclerView.Adapter<adapter_DaLuu.MyViewHolder> {

    private ArrayList<phong> phongList = new ArrayList<>();
    private Context context;

    public adapter_DaLuu(Context context) {
        this.context = context;
        khoi_tao();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_phongdaluu, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        phong dataItem = phongList.get(position);
        holder.tv_tenphong.setText(dataItem.getTen_phong());
        holder.tv_luotthue.setText(String.valueOf(dataItem.getLuot_thue()));
        holder.tvgia.setText(String.valueOf(dataItem.getGia()));
        // Kiểm tra xem phòng có ảnh hay không
        if (dataItem.getAnh_phong() != null && !dataItem.getAnh_phong().isEmpty()) {
            // Lấy URL ảnh đầu tiên từ ArrayList<String>
            String firstImageUrl = dataItem.getAnh_phong().get(0);
            // Load ảnh bằng Picasso
            Picasso.get().load(firstImageUrl).into(holder.imgView, new Callback() {
                @Override
                public void onSuccess() {
                    // Ảnh đã được tải thành công
                }
                @Override
                public void onError(Exception e) {
                    // Có lỗi xảy ra khi tải ảnh
                    Log.e("Picasso", "Error loading image: " + e.getMessage());
                }
            });
        }

        getTienNghi(dataItem.getId_phong(), new TienNghiCallback() {
            @Override
            public void onTienNghiBuilt(String result) {
                holder.tv_tiennghi.setText(result);
            }
        });
        getSoLuongDanhGia(dataItem.getId_phong(), new DanhGiaCallback() {
            @Override
            public void onDanhGiaCount(int count) {
                holder.tv_danhgia.setText(count+ " Đánh giá");
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mở màn hình chi tiết và chuyển dữ liệu phòng
                openChiTietPhong(dataItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return phongList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView tv_tenphong, tv_danhgia, tv_luotthue, tv_tiennghi, tvgia;
        ImageView imgView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.iv_item_phong);
            tv_tenphong = itemView.findViewById(R.id.tvTenPhong);
            layout = itemView.findViewById(R.id.layout_daluu);
            tv_danhgia = itemView.findViewById(R.id.tvDanhGia);
            tv_luotthue = itemView.findViewById(R.id.tvLuotThue);
            tv_tiennghi = itemView.findViewById(R.id.tvTienNghiPhong);
            tvgia = itemView.findViewById(R.id.tvGiaThue);
        }
    }
    public void sortByPriceDescending() {
        Collections.sort(phongList, new Comparator<phong>() {
            @Override
            public int compare(phong room1, phong room2) {
                // So sánh giá của hai phòng
                return Double.compare(room2.getGia(), room1.getGia());
            }
        });
        notifyDataSetChanged();
    }
    public void sortByPriceAscending() {
        Collections.sort(phongList, new Comparator<phong>() {
            @Override
            public int compare(phong room1, phong room2) {
                // So sánh giá của hai phòng
                return Double.compare(room1.getGia(), room2.getGia());
            }
        });
        notifyDataSetChanged();
    }
    public void sortByRentals() {
        Collections.sort(phongList, new Comparator<phong>() {
            @Override
            public int compare(phong room1, phong room2) {
                // So sánh giá của hai phòng
                return Double.compare(room2.getLuot_thue(), room1.getLuot_thue());
            }
        });
        notifyDataSetChanged();
    }
    public void sortByLastLetter() {
        Collections.sort(phongList, new Comparator<phong>() {
            @Override
            public int compare(phong room1, phong room2) {
                // So sánh chữ cái đầu của tên phòng
                return room2.getTen_phong().compareToIgnoreCase(room1.getTen_phong());
            }
        });
        notifyDataSetChanged();
    }
    public void sortByFirstLetter() {
        Collections.sort(phongList, new Comparator<phong>() {
            @Override
            public int compare(phong room1, phong room2) {
                // So sánh chữ cái đầu của tên phòng
                return room1.getTen_phong().compareToIgnoreCase(room2.getTen_phong());
            }
        });
        notifyDataSetChanged();
    }
    private void openChiTietPhong(phong selectedPhong) {
        // Tạo Intent để mở màn hình chi tiết
        Intent intent = new Intent(context, Chi_Tiet_Phong.class);

        // Đưa dữ liệu phòng vào Intent để hiển thị chi tiết
        intent.putExtra("phong", selectedPhong);

        // Bắt đầu Activity mới
        context.startActivity(intent);
    }
    public interface TienNghiCallback {
        void onTienNghiBuilt(String result);
    }

    void getTienNghi(String id_phong, TienNghiCallback callback) {
        DatabaseReference databaseReference_tiennghi = FirebaseDatabase.getInstance().getReference("chi_tiet_tien_nghi").child(id_phong);
        databaseReference_tiennghi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Tạo một StringBuilder để xây dựng chuỗi kết quả
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
                                    // Set lại số lượng
                                    tn.setSo_luong(cttn.getSo_luong());

                                    // Thêm tien_nghi vào StringBuilder
                                    resultBuilder.append(cttn.getSo_luong())
                                            .append(" ")
                                            .append(tn.getTen_tien_nghi())
                                            .append(" - ");
                                }
                            }

                            // Gọi callback khi chuỗi kết quả đã được xây dựng
                            callback.onTienNghiBuilt(resultBuilder.toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi khi đọc dữ liệu từ "tien_nghi"
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi đọc dữ liệu từ "chi_tiet_tien_nghi"
            }
        });
    }
    public  interface DanhGiaCallback {
        void onDanhGiaCount(int count);
    }
    void getSoLuongDanhGia(String id_phong, DanhGiaCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("danh_gia").child(id_phong);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int tongDanhGia = 0;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        danh_gia rating = dataSnapshot.getValue(danh_gia.class);
                        if (rating != null) {
                            // Tính tổng số lượng đánh giá
                            tongDanhGia++;
                        }
                    }

                    // Gọi callback với giá trị tổng số lượng đánh giá
                    callback.onDanhGiaCount(tongDanhGia);
                } else {
                    // Gọi callback với giá trị 0 nếu không có đánh giá
                    callback.onDanhGiaCount(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi đọc dữ liệu
            }
        });
    }
    void khoi_tao() {

        // Lấy số điện thoại hiện tại, bạn có thể thay thế bằng cách nào đó để lấy số điện thoại đăng nhập
        String currentPhoneNumber = "123";

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference yeuThichRef = database.getReference("yeu_thich").child(currentPhoneNumber);

        yeuThichRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phongList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id_phong = snapshot.getKey();
                    DatabaseReference phongRef = database.getReference("phong").child(id_phong);

                    phongRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot phongSnapshot) {
                            phong room = phongSnapshot.getValue(phong.class);
                            if (room != null) {
                                phongList.add(room);
                                notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError phongError) {
                            // Xử lý lỗi khi đọc dữ liệu phong
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
