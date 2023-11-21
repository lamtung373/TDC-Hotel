package com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Model.phong;
import com.example.tdc_hotel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_ketQuaTimKiem extends RecyclerView.Adapter<Adapter_ketQuaTimKiem.KQ_TimKiem_Holder> {
    Context context;
    Intent intent;
    ArrayList<phong> findingList = new ArrayList<>();
    ArrayList<phong> arrangeList = new ArrayList<>();
    String ngayNhan, ngayTra, loaiPhong;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @NonNull
    @Override
    public Adapter_ketQuaTimKiem.KQ_TimKiem_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_kqtimkiem, parent, false);
        return new KQ_TimKiem_Holder(view);
    }

    public Adapter_ketQuaTimKiem(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
//        khoiTao(loaiPhong);
        ngayNhan = intent.getStringExtra("ngayNhan");
        ngayTra = intent.getStringExtra("ngayTra");
        loaiPhong = intent.getStringExtra("loaiPhong");
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_ketQuaTimKiem.KQ_TimKiem_Holder holder, int position) {
        phong data = findingList.get(position);
        holder.tv_ratting.setText(String.valueOf(data.getDanh_gia_sao()));
        holder.tvTenphongFinding.setText(String.valueOf(data.getTen_phong()));
    }

    @Override
    public int getItemCount() {
        return findingList.size();
    }

    public class KQ_TimKiem_Holder extends RecyclerView.ViewHolder {
        ImageView iv_pictureFinding;
        TextView tvTenphongFinding, tv_ratting;

        public KQ_TimKiem_Holder(@NonNull View itemView) {
            super(itemView);
            iv_pictureFinding = itemView.findViewById(R.id.iv_pictureFinding);
            tvTenphongFinding = itemView.findViewById(R.id.tvTenphongFinding);
            tv_ratting = itemView.findViewById(R.id.tv_ratting);
        }
    }

    void khoiTao(String thoiGianNhan, String thoiGianTra, String loaiPhong) throws ParseException {
        Date ngaynhan = dateFormat.parse(thoiGianNhan);
        Date ngaytra = dateFormat.parse(thoiGianTra);

        DatabaseReference databaseReference_Phong = FirebaseDatabase.getInstance().getReference("phong");
        DatabaseReference databaseReference_HoaDon = FirebaseDatabase.getInstance().getReference("hoa_don");

        databaseReference_Phong.orderByChild("loai_phong").equalTo(loaiPhong).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot_phong) {
                if (snapshot_phong.exists()) {
                    findingList.clear();

                    for (DataSnapshot dataSnapshot_phong : snapshot_phong.getChildren()) {
                        phong room = dataSnapshot_phong.getValue(phong.class);

                        // Kiểm tra xem loại phòng đã được tìm có tồn tại trong hóa đơn không
                        databaseReference_HoaDon.child(room.getId_phong()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot_hoaDon) {
                                if (snapshot_hoaDon.exists()) {
                                    // Tìm ngày nhận và ngày trả trong hóa đơn
                                    String ngayNhanHoaDon = snapshot_hoaDon.child("ngayNhan").getValue(String.class);
                                    String ngayTraHoaDon = snapshot_hoaDon.child("ngayTra").getValue(String.class);

                                    try {
                                        Date getngaynhan = dateFormat.parse(ngayNhanHoaDon);
                                        Date getngaytra = dateFormat.parse(ngayTraHoaDon);

                                        // Kiểm tra điều kiện ngày nhận và ngày trả
                                        if (ngaynhan.before(getngaytra) && ngaytra.after(getngaynhan)) {
                                            findingList.add(room);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
                            }
                        });
                    }

                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
            }
        });
    }


}
