package com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Model.chi_tiet_hoa_don_dich_vu;
import com.example.tdc_hotel.Model.dich_vu;
import com.example.tdc_hotel.Model.hoa_don;
import com.example.tdc_hotel.Model.phong;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tdc_hotel.R;
import com.example.tdc_hotel.Thanh_Toan;
import com.example.tdc_hotel.Xac_Thuc_OTP;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Hoan_Tat_Thongtin_DatPhong extends AppCompatActivity {
    Button btnNgayNhan, btnNgayTra, btnXacNhanDV;
    ImageView ivTru, ivCong, img_Back;
    RecyclerView rcv_dvphong, rcv_dvtheonguoi;
    ProgressBar pb_datphong;
    EditText edtSonguoi;
    DichVuTheoNguoi_Adapter adapterDvTheoNguoi = new DichVuTheoNguoi_Adapter();
    DichVuPhong_Adapter adapterDvTheoPhong = new DichVuPhong_Adapter();
    phong phong;
    Date thoi_gian_nhan = null;
    Date thoi_gian_tra = null;
    String id_hoadon = "";
    double tienPhong = 0;
    double tongThanhToan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoan_tat_thongtin_dat_phong);
        phong = (phong) getIntent().getSerializableExtra("phong");
        setControl();
        Initialization();
        setEvent();
    }

    private void setEvent() {
        DatabaseReference reference_hoadon = FirebaseDatabase.getInstance().getReference("hoa_don").child(phong.getId_phong());
        btnXacNhanDV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date now = new Date();
                    thoi_gian_nhan = new SimpleDateFormat("dd/MM/yyyy").parse(btnNgayNhan.getText().toString());
                    thoi_gian_tra = new SimpleDateFormat("dd/MM/yyyy").parse(btnNgayTra.getText().toString());
                    if (thoi_gian_nhan.before(new SimpleDateFormat("dd/MM/yyyy").parse(dateFormat.format(now))) || thoi_gian_nhan.after(thoi_gian_tra)) {
                        Toast.makeText(Hoan_Tat_Thongtin_DatPhong.this, "Thời gian đã đặt không hợp lệ!!!", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (((thoi_gian_tra.getTime() - thoi_gian_nhan.getTime()) / (24 * 3600 * 1000)) < 1) {
                        Toast.makeText(Hoan_Tat_Thongtin_DatPhong.this, "Thời gian ở phải ít nhất 1 ngày!!!", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (edtSonguoi.getText().toString().trim().equals("0")) {
                        Toast.makeText(Hoan_Tat_Thongtin_DatPhong.this, "Số lượng khách phải lớn hơn 0!!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    reference_hoadon.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Date thoi_gian_hoadon_nhan = null;
                                Date thoi_gian_hoadon_tra = null;
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    hoa_don hoa_don = dataSnapshot.getValue(hoa_don.class);


                                    //Lấy những hóa đơn chưa thanh toán
                                    if (hoa_don.getThoi_gian_thanh_toan().equals("")) {
                                        try {
                                            //kiểm tra thời gian đặt phòng có hợp lệ so với thời gian trong hóa đơn hay không

                                            //Chuyển đổi thời gian
                                            thoi_gian_hoadon_nhan = new SimpleDateFormat("dd/MM/yyyy").parse(hoa_don.getThoi_gian_nhan_phong().toString());
                                            thoi_gian_hoadon_tra = new SimpleDateFormat("dd/MM/yyyy").parse(hoa_don.getThoi_gian_tra_phong().toString());

                                            //Kiểm tra thời gian nhận có tồn tại trong thời gian của hóa đơn hay không
                                            if (thoi_gian_nhan.after(thoi_gian_hoadon_nhan) && thoi_gian_nhan.before(thoi_gian_hoadon_tra) || thoi_gian_nhan.equals(thoi_gian_hoadon_nhan) || thoi_gian_nhan.equals(thoi_gian_hoadon_tra)) {
                                                Toast.makeText(Hoan_Tat_Thongtin_DatPhong.this, "Đã có lịch", Toast.LENGTH_SHORT).show();
                                                return;
                                            }


                                            //Kiểm tra thời gian trả có tồn tại trong thời gian của hóa đơn hay không
                                            else if (thoi_gian_tra.after(thoi_gian_hoadon_nhan) && thoi_gian_tra.before(thoi_gian_hoadon_tra) || thoi_gian_tra.equals(thoi_gian_hoadon_nhan) || thoi_gian_tra.equals(thoi_gian_hoadon_tra)) {
                                                Toast.makeText(Hoan_Tat_Thongtin_DatPhong.this, "Đã có lịch", Toast.LENGTH_SHORT).show();
                                                return;
                                            }


                                            //Kiểm tra thời gian nhận của hóa đơn có tồn tại trong thời gian nhận hoặc trả hay không
                                            else if (thoi_gian_hoadon_nhan.after(thoi_gian_nhan) && thoi_gian_hoadon_nhan.before(thoi_gian_tra) || thoi_gian_hoadon_nhan.equals(thoi_gian_nhan) || thoi_gian_hoadon_nhan.equals(thoi_gian_tra)) {
                                                Toast.makeText(Hoan_Tat_Thongtin_DatPhong.this, "Đã có lịch", Toast.LENGTH_SHORT).show();
                                                return;
                                            }


                                            //Kiểm tra thời gian trả của hóa đơn có tồn tại trong thời gian nhận hoặc trả hay không
                                            else if (thoi_gian_hoadon_tra.after(thoi_gian_nhan) && thoi_gian_hoadon_nhan.before(thoi_gian_tra) || thoi_gian_hoadon_tra.equals(thoi_gian_nhan) || thoi_gian_hoadon_tra.equals(thoi_gian_tra)) {
                                                Toast.makeText(Hoan_Tat_Thongtin_DatPhong.this, "Đã có lịch", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        } catch (Exception e) {
                                            Log.e("Lỗi chuyển đổi dữ liệu thời gian thanh toán", e.getMessage());
                                        }
                                    }

                                }
                                //  Log.e("dvphong",""+adapterDvTheoPhong.getData_dv().get(0).getTen_dich_vu()+" "+adapterDvTheoPhong.getData_dv().get(0).isCheck());
                                //  Log.e("dvphong",""+adapterDvTheoNguoi.getData_dv().get(0).getTen_dich_vu()+" "+adapterDvTheoNguoi.getData_dv().get(0).getSo_luong());
                            }
                            LuuHoaDonToFirebase();
                            Intent intent = new Intent(Hoan_Tat_Thongtin_DatPhong.this, Thanh_Toan.class);
                            intent.putExtra("thoi_gian_nhan", btnNgayNhan.getText().toString());
                            intent.putExtra("thoi_gian_tra", btnNgayTra.getText().toString());
                            intent.putExtra("dich_vu_theo_nguoi", adapterDvTheoNguoi.getData_dv());
                            intent.putExtra("dich_vu_phong", adapterDvTheoPhong.getData_dv());
                            intent.putExtra("ma_hoa_don", id_hoadon);
                            intent.putExtra("tong_thanh_toan", tongThanhToan);
                            intent.putExtra("tien_phong", tienPhong);
                            intent.putExtra("phong", phong);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch (
                        Exception e) {
                    Log.e("Lỗi chuyển đổi dữ liệu thời gian đã đặt", e.getMessage());
                }
            }
        });
        String t[] = phong.getLoai_phong().split(" ");
        ivCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soluong = Integer.parseInt(edtSonguoi.getText().toString().trim()) + 1;
                if (soluong <= Integer.parseInt(t[0])) {
                    edtSonguoi.setText(soluong + "");
                }
            }
        });
        ivTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soluong = Integer.parseInt(edtSonguoi.getText().toString().trim()) - 1;
                if (soluong >= 0) {
                    edtSonguoi.setText(soluong + "");
                }
            }
        });
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void LuuHoaDonToFirebase() {
        btnXacNhanDV.setVisibility(View.GONE);
        pb_datphong.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("hoa_don");
        id_hoadon = reference.push().getKey();
        String id_laocong = "";
        String id_letan = "";
        String id_phong = phong.getId_phong();

        SharedPreferences sharedPreferences = getSharedPreferences(Xac_Thuc_OTP.SharedPreferences, MODE_PRIVATE);
        String id_kh = sharedPreferences.getString(Xac_Thuc_OTP.sdt_kh, "");
        String name_kh = sharedPreferences.getString(Xac_Thuc_OTP.name_kh, "");
        String soDienThoai = id_kh;
        String tenKhachHang = name_kh;
        String thoiGianCoc = "";
        String thoiGianDuyet = "";
        String thoiGianHuy = "";
        String thoiGianNhanPhong = btnNgayNhan.getText().toString();
        String thoiGianTraPhong = btnNgayTra.getText().toString();
        String thoiGianThanhToan = "";
        double tienCoc = 0;
        double tongDV = 0;
        double tongDVP = 0;
        double tongTienNghi = 0;

        try {
            // Chuyển đổi String sang Date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateNhan = sdf.parse(btnNgayNhan.getText().toString());
            Date dateTra = sdf.parse(btnNgayTra.getText().toString());

            // Tính số ngày lưu trú
            long diff = dateTra.getTime() - dateNhan.getTime();
            int soNgayLuuTru = (int) (diff / (24 * 60 * 60 * 1000));

            // Hiển thị hoặc sử dụng giá trị tienPhong như mong muốn
            if (phong.getSale() > 0) {
                tienPhong = phong.getSale() * soNgayLuuTru;
            } else {
                tienPhong = phong.getGia() * soNgayLuuTru;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Định dạng thời gian theo "dd/MM/yyyy HH:mm"
        for (dich_vu dv : adapterDvTheoNguoi.getData_dv()) {
            if (dv.getSo_luong() > 0) {
                tongDV += dv.getGia_dich_vu() * dv.getSo_luong();
            }
        }

        for (dich_vu dv : adapterDvTheoPhong.getData_dv()) {
            if (dv.getSo_luong() > 0) {
                tongDV += dv.getGia_dich_vu();
            }
        }
        tongThanhToan = tienPhong + tongDV;
        ArrayList<String> arr_cccd = new ArrayList<>();
        arr_cccd.add("");
        arr_cccd.add("");
        hoa_don hoaDon = new hoa_don(
                id_hoadon,
                soDienThoai,
                tenKhachHang,
                id_phong,
                id_letan,
                id_laocong,
                arr_cccd,
                tienCoc,
                tienPhong,
                tongDV,
                tongDVP,
                tongTienNghi,
                tongThanhToan,
                thoiGianCoc,
                thoiGianNhanPhong,
                thoiGianTraPhong,
                thoiGianHuy,
                thoiGianThanhToan,
                thoiGianDuyet
        );

        reference.child(id_phong).child(id_hoadon).setValue(hoaDon).addOnSuccessListener(aVoid -> {
            //Thêm chi_tiet_hoa_don_dich_vu
            DatabaseReference dataChiTietHoaDonDichVu = FirebaseDatabase.getInstance().getReference("chi_tiet_hoa_don_dich_vu");
            for (dich_vu dv : adapterDvTheoNguoi.getData_dv()) {
                chi_tiet_hoa_don_dich_vu chi_tiet_hoa_don_dich_vu = new chi_tiet_hoa_don_dich_vu(
                        dv.getSo_luong(),
                        id_hoadon,
                        dv.getId_dich_vu()
                );
                dataChiTietHoaDonDichVu.child(id_hoadon).child(dv.getId_dich_vu()).setValue(chi_tiet_hoa_don_dich_vu);
            }
            for (dich_vu dv : adapterDvTheoPhong.getData_dv()) {
                chi_tiet_hoa_don_dich_vu chi_tiet_hoa_don_dich_vu = new chi_tiet_hoa_don_dich_vu(
                        dv.getSo_luong(),
                        id_hoadon,
                        dv.getId_dich_vu()
                );
                dataChiTietHoaDonDichVu.child(id_hoadon).child(dv.getId_dich_vu()).setValue(chi_tiet_hoa_don_dich_vu);
            }

            Toast.makeText(Hoan_Tat_Thongtin_DatPhong.this, "Đặt phòng thành công!", Toast.LENGTH_SHORT).show();
            pb_datphong.setVisibility(View.GONE);
            btnXacNhanDV.setVisibility(View.VISIBLE);

        }).addOnFailureListener(e -> {
            Toast.makeText(Hoan_Tat_Thongtin_DatPhong.this, "Lỗi khi đặt phòng!", Toast.LENGTH_SHORT).show();
            pb_datphong.setVisibility(View.GONE);
            btnXacNhanDV.setVisibility(View.VISIBLE);
        });
    }

    private void Initialization() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        btnNgayNhan.setText(dateFormat.format(calendar.getTime()));
        calendar.roll(Calendar.DATE, 1);
        btnNgayTra.setText(dateFormat.format(calendar.getTime()));
        edtSonguoi.setFocusable(false);
        rcv_dvtheonguoi.setLayoutManager(new LinearLayoutManager(Hoan_Tat_Thongtin_DatPhong.this, LinearLayoutManager.VERTICAL, false));
        rcv_dvtheonguoi.addItemDecoration(new DividerItemDecoration(Hoan_Tat_Thongtin_DatPhong.this, DividerItemDecoration.VERTICAL));
        rcv_dvtheonguoi.setAdapter(adapterDvTheoNguoi);

        rcv_dvphong.setLayoutManager(new LinearLayoutManager(Hoan_Tat_Thongtin_DatPhong.this, LinearLayoutManager.VERTICAL, false));
        rcv_dvphong.addItemDecoration(new DividerItemDecoration(Hoan_Tat_Thongtin_DatPhong.this, DividerItemDecoration.VERTICAL));
        rcv_dvphong.setAdapter(adapterDvTheoPhong);
        ChonThoiGian(btnNgayNhan);
        ChonThoiGian(btnNgayTra);
    }

    void ChonThoiGian(Button btnThoigian) {
        btnThoigian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dataListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        btnThoigian.setText(dateFormat.format(calendar.getTime()));
                    }
                };
                new DatePickerDialog(Hoan_Tat_Thongtin_DatPhong.this, dataListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void setControl() {
        btnNgayNhan = findViewById(R.id.btnNgayNhan);
        btnNgayTra = findViewById(R.id.btnNgayTra);
        btnXacNhanDV = findViewById(R.id.btnXacNhanDV);
        ivTru = findViewById(R.id.ivTru);
        ivCong = findViewById(R.id.ivCong);
        img_Back = findViewById(R.id.img_Back);
        rcv_dvphong = findViewById(R.id.rcv_dvphong);
        rcv_dvtheonguoi = findViewById(R.id.rcv_dvtheonguoi);
        edtSonguoi = findViewById(R.id.edtSonguoi);
        pb_datphong = findViewById(R.id.pb_datphong);
    }
}