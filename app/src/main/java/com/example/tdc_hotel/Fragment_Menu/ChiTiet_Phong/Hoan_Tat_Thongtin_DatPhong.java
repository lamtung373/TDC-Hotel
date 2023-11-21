package com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Model.hoa_don;
import com.example.tdc_hotel.Model.phong;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tdc_hotel.R;
import com.example.tdc_hotel.Thanh_Toan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Hoan_Tat_Thongtin_DatPhong extends AppCompatActivity {
    Button btnNgayNhan, btnNgayTra, btnXacNhanDV;
    ImageView ivTru, ivCong, img_Back;
    RecyclerView rcv_dvphong, rcv_dvtheonguoi;
    EditText edtSonguoi;
    DichVuTheoNguoi_Adapter adapterDvTheoNguoi = new DichVuTheoNguoi_Adapter();
    DichVuPhong_Adapter adapterDvTheoPhong = new DichVuPhong_Adapter();
    phong phong;
    Date thoi_gian_nhan = null;
    Date thoi_gian_tra = null;

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
        DatabaseReference reference_hoadon = FirebaseDatabase.getInstance().getReference("hoa_don").child("a");
        btnXacNhanDV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Date now = new Date();
                    thoi_gian_nhan = new SimpleDateFormat("dd/MM/yyyy").parse(btnNgayNhan.getText().toString());
                    thoi_gian_tra = new SimpleDateFormat("dd/MM/yyyy").parse(btnNgayTra.getText().toString());
                    if (thoi_gian_nhan.before(now) || thoi_gian_nhan.after(thoi_gian_tra)) {
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
                            if (snapshot != null || snapshot.getChildrenCount() != 0) {
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
                                Intent intent = new Intent(Hoan_Tat_Thongtin_DatPhong.this, Hoan_Tat_Thongtin_DatPhong.class);
                                intent.putExtra("thoi_gian_nhan",btnNgayNhan.getText().toString());
                                intent.putExtra("thoi_gian_tra",btnNgayTra.getText().toString());
                                intent.putExtra("so_luong_khach",edtSonguoi.getText().toString());
                                intent.putExtra("dich_vu_theo_nguoi",adapterDvTheoNguoi.getData_dv());
                                intent.putExtra("dich_vu_phong",adapterDvTheoPhong.getData_dv());
                                intent.putExtra("phong",phong);
                                startActivity(intent);
                                //  Log.e("dvphong",""+adapterDvTheoPhong.getData_dv().get(0).getTen_dich_vu()+" "+adapterDvTheoPhong.getData_dv().get(0).isCheck());
                                //  Log.e("dvphong",""+adapterDvTheoNguoi.getData_dv().get(0).getTen_dich_vu()+" "+adapterDvTheoNguoi.getData_dv().get(0).getSo_luong());
                            }
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

    private void Initialization() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        btnNgayNhan.setText(dateFormat.format(calendar.getTime()));
        calendar.roll(Calendar.DATE,1);
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
    }
}