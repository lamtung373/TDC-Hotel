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

import com.example.tdc_hotel.Model.chi_tiet_tien_nghi;
import com.example.tdc_hotel.Model.hoa_don;
import com.example.tdc_hotel.Model.phong;
import com.example.tdc_hotel.Model.tien_nghi;
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
    private ArrayList<tien_nghi> tienNghiList = new ArrayList<>();
    private String ngayNhan, ngayTra, loaiPhong;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Định dạng ngày giờ

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
        getTienNghi(data.getId_phong(), new TienNghiCallback() {
            @Override
            public void onTienNghiBuilt(String result) {
                holder.tv_tiennghi.setText(result);
            }
        });
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

//                            if (resultBuilder.length() > 0) {
//                                // Xoá 2 ký tự cuối cùng (1 khoảng trắng và 1 dấu gạch nối)
//                                resultBuilder.delete(resultBuilder.length() - 2, resultBuilder.length());
//                            }

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
                    if (hoaDon != null && !isDateOverlap(ngayNhan, ngayTra, hoaDon.getThoi_gian_nhan_phong(), hoaDon.getThoi_gian_tra_phong())) {
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
    // Kiểm tra xem hai khoảng thời gian có chồng lấn không
    private boolean isDateOverlap(String newDateStart, String newDateEnd, String dateStart_bill, String dateEnd_bill) {
        try {
            Date newDate_Start = dateFormat.parse(newDateStart);
            Date newDate_End = dateFormat.parse(newDateEnd);
            Date start_bill = dateFormat.parse(dateStart_bill);
            Date end_bill = dateFormat.parse(dateEnd_bill);

            // Kiểm tra xem thời gian mới bắt đầu trước thời gian hóa đơn kết thúc
            // và thời gian mới kết thúc sau thời gian hóa đơn bắt đầu
            return newDate_Start.before(end_bill) || newDate_Start.equals(end_bill) || newDate_End.after(start_bill) || newDate_End.equals(start_bill);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


}
