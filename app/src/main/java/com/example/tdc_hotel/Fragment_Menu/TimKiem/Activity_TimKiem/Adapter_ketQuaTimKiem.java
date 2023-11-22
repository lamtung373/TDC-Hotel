package com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Model.hoa_don;
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

// Adapter cho việc hiển thị kết quả tìm kiếm phòng
public class Adapter_ketQuaTimKiem extends RecyclerView.Adapter<Adapter_ketQuaTimKiem.KQ_TimKiem_Holder> {
    private Context context;
    private ArrayList<phong> findingList = new ArrayList<>();
    private String ngayNhan, ngayTra, loaiPhong;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm"); // Định dạng ngày giờ

    // Constructor với tham số là context và thông tin tìm kiếm
    public Adapter_ketQuaTimKiem(Context context, String ngayNhan, String ngayTra, String loaiPhong) {
        this.context = context;
        this.ngayNhan = ngayNhan;
        this.ngayTra = ngayTra;
        this.loaiPhong = loaiPhong;
        khoiTao(this.ngayNhan, this.ngayTra, this.loaiPhong);
    }

    @NonNull
    @Override
    // Tạo ViewHolder
    public KQ_TimKiem_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_kqtimkiem, parent, false);
        return new KQ_TimKiem_Holder(view);
    }

    @Override
    // Gán dữ liệu vào ViewHolder
    public void onBindViewHolder(@NonNull KQ_TimKiem_Holder holder, int position) {
        phong data = findingList.get(position);
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#");
        holder.tv_ratting.setText(String.valueOf(data.getDanh_gia_sao()));
        holder.tvTenphongFinding.setText(String.valueOf(data.getTen_phong()));
        holder.tv_giaphong.setText(formatter.format(data.getGia()) + " VNđ/đêm");
        holder.tv_luotThue.setText(data.getLuot_thue() + " Đánh giá");
    }

    @Override
    // Lấy số lượng items
    public int getItemCount() {
        return findingList.size();
    }

    // ViewHolder cho RecyclerView
    public class KQ_TimKiem_Holder extends RecyclerView.ViewHolder {
        ImageView iv_pictureFinding;
        TextView tvTenphongFinding, tv_ratting, tv_soluongdanhgia, tv_luotThue, tv_tiennghi, tv_giaphong;

        public KQ_TimKiem_Holder(@NonNull View itemView) {
            super(itemView);
            iv_pictureFinding = itemView.findViewById(R.id.iv_pictureFinding);
            tvTenphongFinding = itemView.findViewById(R.id.tvTenphongFinding);
            tv_ratting = itemView.findViewById(R.id.tv_ratting);
            tv_soluongdanhgia = itemView.findViewById(R.id.tv_soluongdanhgia);
            tv_luotThue = itemView.findViewById(R.id.tv_luotThue);
            tv_tiennghi = itemView.findViewById(R.id.tv_tiennghi);
            tv_giaphong = itemView.findViewById(R.id.tv_giaphong);
        }
    }

    // Khởi tạo dữ liệu cho adapter
    private void khoiTao(String ngayNhan, String ngayTra, String loaiPhong) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phong");

        // Truy vấn Firebase để tìm phòng theo loại phòng
        reference.orderByChild("loai_phong").equalTo(loaiPhong).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Khi dữ liệu thay đổi
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                findingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    phong room = dataSnapshot.getValue(phong.class);
                    if (room != null && room.getId_phong() != null) {
                        checkHoaDonAndAddToList(room, ngayNhan, ngayTra);
                    }
                }
            }

            @Override
            // Khi truy vấn bị hủy
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Lỗi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Kiểm tra hóa đơn và thêm phòng vào danh sách tìm kiếm
    private void checkHoaDonAndAddToList(phong room, String ngayNhan, String ngayTra) {
        DatabaseReference reference_HoaDon = FirebaseDatabase.getInstance().getReference("hoa_don");

        // Truy vấn Firebase để tìm hóa đơn của phòng
        reference_HoaDon.child(room.getId_phong()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Khi dữ liệu thay đổi
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isAvailable = true;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    hoa_don hoaDon = dataSnapshot.getValue(hoa_don.class);
                    if (hoaDon != null && isDateOverlap(ngayNhan, ngayTra, hoaDon.getThoi_gian_nhan_phong(), hoaDon.getThoi_gian_tra_phong())) {
                        isAvailable = false;
                        break;
                    }
                }
                if (isAvailable) {
                    findingList.add(room);
                    notifyDataSetChanged();
                }
            }

            @Override
            // Khi truy vấn bị hủy
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
    }

    // Kiểm tra xem hai khoảng thời gian có trùng nhau không
    private boolean isDateOverlap(String newDateStart, String newDateEnd, String dateStart_bill, String dateEnd_bill) {
        try {
            Date newDate_Start = dateFormat.parse(newDateStart);
            Date newDate_End = dateFormat.parse(newDateEnd);
            Date start_bill = dateFormat.parse(dateStart_bill);
            Date end_bill = dateFormat.parse(dateEnd_bill);

            // Kiểm tra xem thời gian mới bắt đầu sau thời gian hóa đơn kết thúc
            // hoặc thời gian mới kết thúc trước thời gian hóa đơn bắt đầu
            if (newDate_Start.after(end_bill) && newDate_End.after(end_bill)) {
                return true;
            }else return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

}
