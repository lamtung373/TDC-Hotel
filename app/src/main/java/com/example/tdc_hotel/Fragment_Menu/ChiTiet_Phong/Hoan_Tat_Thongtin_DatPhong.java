package com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tdc_hotel.R;
import com.example.tdc_hotel.Thanh_Toan;

public class Hoan_Tat_Thongtin_DatPhong extends AppCompatActivity {
    RecyclerView rcvDichvu_phong, rcvDichvu_theonguoi;
    Button btnTienhanhthanhtoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoan_tat_thongtin_dat_phong);
        setControl();
        Initialization();
        setEvent();
    }

    private void setEvent() {
        btnTienhanhthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Hoan_Tat_Thongtin_DatPhong.this, Thanh_Toan.class);
                startActivity(intent);
            }
        });
    }

    private void Initialization() {
        //Tao danh sach dich vu phong
        DichVuPhong_Adapter dichVuPhong_adapter = new DichVuPhong_Adapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvDichvu_phong.addItemDecoration(dividerItemDecoration);
        rcvDichvu_phong.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvDichvu_phong.setAdapter(dichVuPhong_adapter);

        //Tao danh sach dich vu theo nguoi
        DichVuTheoNguoi_Adapter dichVuTheoNguoi_adapter = new DichVuTheoNguoi_Adapter();
        rcvDichvu_theonguoi.addItemDecoration(dividerItemDecoration);
        rcvDichvu_theonguoi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvDichvu_theonguoi.setAdapter(dichVuTheoNguoi_adapter);


    }

    private void setControl() {
        rcvDichvu_phong = findViewById(R.id.rcvDichvu_phong);
        rcvDichvu_theonguoi = findViewById(R.id.rcvDichvu_theonguoi);
        btnTienhanhthanhtoan = findViewById(R.id.btnTienhanhthanhtoan);

    }
}