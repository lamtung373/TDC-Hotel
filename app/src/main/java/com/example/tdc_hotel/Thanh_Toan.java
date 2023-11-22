package com.example.tdc_hotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.tdc_hotel.Model.chi_tiet_tien_nghi;
import com.example.tdc_hotel.Model.dich_vu;
import com.example.tdc_hotel.Model.phong;
import com.example.tdc_hotel.Model.tien_nghi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Thanh_Toan extends AppCompatActivity {
    ImageView ivBank,iv_back;
    TextView tvTenPhong, tvNgayNhanPhong, tvNgayTraPhong, tvTienPhong, tvTienNghi, tvDichVu_a, tvDichVu_b, tvTongHD, tvMaHoaDon, tvTienCoc_SS;
    phong phong;
    String ngayNhan, ngayTra, ma_hoa_don;
    public static String SHARED_PRE = "shared_pre";
    ArrayList<dich_vu> dichVuTheoNguoi, dichVuTheoPhong;
    double tienPhong = 0;
    double tong_thanh_toan=0;

    //TỔNG HOÁ ĐƠN
    double tienCoc = 0;
    RadioButton radTiencoc,radToanbohoadon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        phong = (phong) getIntent().getSerializableExtra("phong");
        //Lấy dữ liệu từ màn hình trước
        Intent intent = getIntent();


        ngayNhan = intent.getStringExtra("thoi_gian_nhan");
        ngayTra = intent.getStringExtra("thoi_gian_tra");
        dichVuTheoNguoi = (ArrayList<dich_vu>) getIntent().getSerializableExtra("dich_vu_theo_nguoi");
        dichVuTheoPhong = (ArrayList<dich_vu>) getIntent().getSerializableExtra("dich_vu_phong");
        ma_hoa_don = intent.getStringExtra("ma_hoa_don");
        tong_thanh_toan = intent.getDoubleExtra("tong_thanh_toan",0);
        tienPhong = intent.getDoubleExtra("tien_phong",0);
        phong = (phong) intent.getSerializableExtra("phong");

        setControl();
        Initialization();
        setEvent();
    }

    private void Initialization() {
        //Đổ dữ liệu lên tv


        //Mã hóa đơn
        tvMaHoaDon.setText("Mã hóa đơn: " + ma_hoa_don);


        //Tên phòng
        tvTenPhong.setText("Phòng: " + phong.getTen_phong());

        //Nhận phòng
        tvNgayNhanPhong.setText(ngayNhan);

        //Trả phòng
        tvNgayTraPhong.setText(ngayTra);

        //Tiền phòng
        tvTienPhong.setText(MessageFormat.format("{0}đ", tienPhong));


        //Tiện nghi phòng
        DatabaseReference reference_chi_tiet_tien_nghi = FirebaseDatabase.getInstance().getReference("chi_tiet_tien_nghi");
        DatabaseReference reference_tien_nghi = FirebaseDatabase.getInstance().getReference("tien_nghi");
        reference_chi_tiet_tien_nghi.child(phong.getId_phong()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tiennghi[] = {""};
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    chi_tiet_tien_nghi chiTietTienNghi = dataSnapshot.getValue(chi_tiet_tien_nghi.class);
                    if (chiTietTienNghi.getSo_luong() != 0) {
                        reference_tien_nghi.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    tien_nghi tien_nghi = dataSnapshot.getValue(tien_nghi.class);
                                    if (chiTietTienNghi.getId_tien_nghi().equals(tien_nghi.getId_tien_nghi())) {
                                        tiennghi[0] += "• " + tien_nghi.getTen_tien_nghi() + " x " + chiTietTienNghi.getSo_luong() + "\n";
                                        tvTienNghi.setText(tiennghi[0]);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        //Hiển thị danh sách dịch vụ
        String dv_a = "", dv_b = "";
        for (dich_vu dv : dichVuTheoNguoi) {
            if (dv.getSo_luong() > 0) {
                double giaDichVu = dv.getGia_dich_vu();
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                String giaDichVuFormatted = decimalFormat.format(giaDichVu);

                // Tạo chuỗi hiển thị dịch vụ theo người
                dv_a += "• " + dv.getTen_dich_vu() + "\n";
                dv_b += giaDichVuFormatted + "đ x " + dv.getSo_luong() + "\n";
            }
        }

        for (dich_vu dv : dichVuTheoPhong) {
            if (dv.getSo_luong() > 0) {
                double giaDichVu = dv.getGia_dich_vu();
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                String giaDichVuFormatted = decimalFormat.format(giaDichVu);

                // Tạo chuỗi hiển thị dịch vụ theo phòng
                dv_a += "• " + dv.getTen_dich_vu() + "\n";
                dv_b += giaDichVuFormatted + "đ \n";
            }
        }
        tvDichVu_a.setText(dv_a);
        tvDichVu_b.setText(dv_b);

        tvTongHD.setText(MessageFormat.format("{0}đ", tong_thanh_toan));
        tienCoc = tienPhong / 2;
        tvTienCoc_SS.setText(MessageFormat.format("{0}đ", tienCoc));
    }

    private void setEvent() {
        ivBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Thanh_Toan.this, Thanh_Toan_Bank.class);
                intent.putExtra("ma_hoa_don",ma_hoa_don);
                if(radTiencoc.isChecked()){
                    intent.putExtra("so_tien",tienCoc);
                } else if (radToanbohoadon.isChecked()) {
                    intent.putExtra("so_tien",tong_thanh_toan);
                }
                intent.putExtra("id_phong",phong.getId_phong());
                startActivity(intent);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setControl() {
        tvTenPhong = findViewById(R.id.tvTenPhong_SS);
        tvNgayNhanPhong = findViewById(R.id.tvNgayNhanPhong_SS);
        tvNgayTraPhong = findViewById(R.id.tvNgayTraPhong_SS);
        tvTienPhong = findViewById(R.id.tvTienPhong_SS);
        tvTienNghi = findViewById(R.id.tvTienNghi_SS);
        tvDichVu_a = findViewById(R.id.tvDichVu_a_SS);
        tvDichVu_b = findViewById(R.id.tvDichVu_b_SS);
        tvTongHD = findViewById(R.id.tvTongHD_SS);
        tvMaHoaDon = findViewById(R.id.tvMaHoaDon);
        tvTienCoc_SS = findViewById(R.id.tvTienCoc_SS);
        ivBank = findViewById(R.id.ivBank);
        radTiencoc = findViewById(R.id.radTiencoc);
        radToanbohoadon = findViewById(R.id.radToanbohoadon);
        iv_back = findViewById(R.id.iv_back);
    }
}