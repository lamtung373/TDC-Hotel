package com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tdc_hotel.R;

import java.util.ArrayList;

public class Ket_Qua_Tim_Kiem extends AppCompatActivity {
    RecyclerView rcvPhong;
    Spinner spSapxep;
    ArrayList<String> arrForspSapxep = new ArrayList<>();
    Adapter_ketQuaTimKiem adapter_Finding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_qua_tim_kiem);
        Intent intent = getIntent();

        String ngayNhan = intent.getStringExtra("ngayNhan");
        String ngayTra = intent.getStringExtra("ngayTra");
        String loaiPhong = intent.getStringExtra("loaiPhong");
        adapter_Finding = new Adapter_ketQuaTimKiem(this, ngayNhan, ngayTra, loaiPhong);
        setControl();
        setEvent();
    }

    private void setEvent() {
        KhoiTao();
        rcvPhong.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvPhong.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcvPhong.setAdapter(adapter_Finding);
       // Toast.makeText(this,"Số lượng phòng phù hợp là: "+adapter_Finding.getItemCount(),Toast.LENGTH_LONG).show();

        // Thiết lập adapter cho Spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrForspSapxep);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSapxep.setAdapter(adapterSpinner);
    }

    private void KhoiTao() {
        arrForspSapxep.add("Tăng dần theo giá");
        arrForspSapxep.add("Giảm dần theo giá");
        arrForspSapxep.add("Tăng dần theo đánh giá sao");
        arrForspSapxep.add("Giảm dần theo đánh giá sao");
    }

    private void setControl() {
        rcvPhong = findViewById(R.id.rcvPhong_finding);
        spSapxep = findViewById(R.id.spSapxep);
    }
}
