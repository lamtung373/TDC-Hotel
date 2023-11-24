package com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Chi_Tiet_Phong;
import com.example.tdc_hotel.Model.chi_tiet_tien_nghi;
import com.example.tdc_hotel.Model.danh_gia;
import com.example.tdc_hotel.Model.hoa_don;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Adapter cho việc hiển thị kết quả tìm kiếm phòng
public class Adapter_ketQuaTimKiem extends RecyclerView.Adapter<Adapter_ketQuaTimKiem.KQ_TimKiem_Holder> {
    private Context context;
    ImageView iv_pictureFinding;
    private ArrayList<phong> findingList = new ArrayList<>();
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
        if (data.getSale()!=0){
            holder.tv_giaphong.setVisibility(View.VISIBLE);
            holder.tv_giaphong.setTextColor(Color.GRAY);
            holder.tv_giaphong.setTextSize(13);
            holder.tv_giaphong.setPaintFlags(holder.tv_giaphong.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_giaphong.setText(formatter.format(data.getGia()));
            holder.tv_salephong.setText(formatter.format(data.getSale()) + " VNĐ");
        }
        else {
            holder.tv_giaphong.setText(formatter.format(data.getGia())+ " VNĐ");
            holder.tv_salephong.setVisibility(View.GONE);
            holder.tv_giaphong.setTextColor(Color.RED);
            holder.tv_giaphong.setTextSize(15);
            holder.tv_giaphong.setPaintFlags(holder.tv_salephong.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.tv_ratting.setText(String.valueOf(data.getDanh_gia_sao()));
        holder.tvTenphongFinding.setText(String.valueOf(data.getTen_phong()));
        holder.tv_luotThue.setText(data.getLuot_thue() + " Lượt thuê");
        if (data.getAnh_phong() != null && !data.getAnh_phong().isEmpty()) {
            String imageUrl = data.getAnh_phong().get(0); // Sử dụng phần tử đầu tiên hoặc bất kỳ phần tử nào bạn muốn hiển thị

            // Hiển thị ProgressBar
            holder.progressBar.setVisibility(View.VISIBLE);

            // Sử dụng Picasso để tải ảnh
            Picasso.get().load(imageUrl).into(holder.iv_pictureFinding, new Callback() {
                @Override
                public void onSuccess() {
                    // Ẩn ProgressBar khi tải thành công
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    // Ẩn ProgressBar và xử lý lỗi
                    holder.progressBar.setVisibility(View.GONE);
                    // Bạn có thể đặt một ảnh mặc định nếu tải ảnh thất bại
                    holder.iv_pictureFinding.setImageResource(R.drawable.phong); // Thay thế bằng ảnh mặc định của bạn
                }
            });
        }
        getTienNghi(data.getId_phong(), new TienNghiCallback() {
            @Override
            public void onTienNghiBuilt(String result) {
                holder.tv_tiennghi.setText(result);
            }
        });
        getSoLuongDanhGia(data.getId_phong(), new DanhGiaCallback() {
            @Override
            public void onDanhGiaCount(int count) {
                holder.tv_soluongdanhgia.setText(count + " Đánh giá");
            }
        });
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
    // Lấy số lượng items
    public int getItemCount() {
        return findingList.size();
    }

    // ViewHolder cho RecyclerView
    public class KQ_TimKiem_Holder extends RecyclerView.ViewHolder {
        ImageView iv_pictureFinding;
        ProgressBar progressBar;
        TextView tvTenphongFinding, tv_ratting, tv_soluongdanhgia, tv_luotThue, tv_tiennghi, tv_giaphong,tv_salephong;

        public KQ_TimKiem_Holder(@NonNull View itemView) {
            super(itemView);
            iv_pictureFinding = itemView.findViewById(R.id.iv_pictureFinding);
            tvTenphongFinding = itemView.findViewById(R.id.tvTenphongFinding);
            tv_ratting = itemView.findViewById(R.id.tv_ratting);
            tv_soluongdanhgia = itemView.findViewById(R.id.tv_soluongdanhgia);
            tv_luotThue = itemView.findViewById(R.id.tv_luotThue);
            tv_tiennghi = itemView.findViewById(R.id.tv_tiennghi);
            tv_giaphong = itemView.findViewById(R.id.tv_giaphong);
            tv_salephong = itemView.findViewById(R.id.tv_salephong);
            iv_pictureFinding = itemView.findViewById(R.id.iv_pictureFinding);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    interface DanhGiaCallback {
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
                    if (room != null && room.getId_phong() != null && room.getId_trang_thai_phong().equals("1")) {
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

        reference_HoaDon.child(room.getId_phong()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
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
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
    }


    // Kiểm tra xem hai khoảng thời gian có trùng nhau không
    // Kiểm tra xem hai khoảng thời gian có chồng lấn không
    private boolean isDateOverlap(String newDateStart, String newDateEnd, String dateStart_bill, String dateEnd_bill) {
        Log.e("thoigian", newDateStart + "-" + newDateEnd + "    " + dateStart_bill + "-" + dateEnd_bill);
        try {
            Date newDate_Start = dateFormat.parse(newDateStart);
            Date newDate_End = dateFormat.parse(newDateEnd);
            Date start_bill = dateFormat.parse(dateStart_bill);
            Date end_bill = dateFormat.parse(dateEnd_bill);

            // Kiểm tra xem có bất kỳ sự chồng lấn nào
            return (newDate_Start.before(end_bill) && newDate_End.after(start_bill)) ||
                    (newDate_Start.before(start_bill) && newDate_End.after(end_bill)) ||
                    (newDate_End.before(end_bill) && newDate_End.after(start_bill)) ||
                    (newDate_Start.after(start_bill) && newDate_Start.before(end_bill)) ||
                    newDate_Start.equals(start_bill) ||
                    newDate_End.equals(end_bill);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

}
