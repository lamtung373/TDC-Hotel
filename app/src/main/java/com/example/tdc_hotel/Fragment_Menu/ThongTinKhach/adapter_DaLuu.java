package com.example.tdc_hotel.Fragment_Menu.ThongTinKhach;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Model.phong;
import com.example.tdc_hotel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class adapter_DaLuu extends RecyclerView.Adapter<adapter_DaLuu.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private ArrayList<phong> phongList = new ArrayList<>();
    private Context context;

    public adapter_DaLuu(Context context) {
        this.context = context;
        khoi_tao();
        loadPhongData();
    }

    private void loadPhongData() {
        DatabaseReference phongReference = FirebaseDatabase.getInstance().getReference("phong");
        phongReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phongList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    phong phong = dataSnapshot.getValue(phong.class);
                    if (phong != null) {
                        phongList.add(phong);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_phongdaluu, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        phong dataItem = phongList.get(position);
        holder.tv_tenphong.setText(dataItem.getTen_phong());
        holder.tv_luotthue.setText(dataItem.getLuot_thue());
        holder.tvgia.setText(String.valueOf(dataItem.getGia()));

    }

    @Override
    public int getItemCount() {
        return phongList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView  tv_tenphong, tv_danhgia, tv_luotthue,tv_tiennghi,tvgia;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tenphong = itemView.findViewById(R.id.tvTenPhong);
            layout = itemView.findViewById(R.id.layout_daluu);
            tv_danhgia = itemView.findViewById(R.id.tvDanhGia);
            tv_luotthue = itemView.findViewById(R.id.tvLuotThue);
            tv_tiennghi = itemView.findViewById(R.id.tvTienNghiPhong);
            tvgia = itemView.findViewById(R.id.tvGiaThue);

        }
    }

    // Phương thức này khởi tạo dữ liệu từ Firebase và thêm vào ArrayList datalist
    void khoi_tao() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference phongRef = database.getReference("phong"); // Thay đổi từ "hoa_don" thành "phong"

        phongRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phongList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Lấy dữ liệu từ cấp độ con, không phải từ danh sách
                    phong room = snapshot.getValue(phong.class);
                    if (room != null) {
                        phongList.add(room);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }


    // Tìm kiếm tên phòng dựa vào ID phòng
    private String findTenPhongById(String idPhong) {
        for (phong phong : phongList) {
            if (phong.getId_phong().equals(idPhong)) {
                return phong.getTen_phong();
            }
        }
        return "Phòng không tồn tại";
    }
}
