package com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;

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
        Intent intent=getIntent();
        adapter_Finding=new Adapter_ketQuaTimKiem(this,intent);
        setControl();
        setEvent();
    }

    private void setEvent() {
        KhoiTao();

    }

    private void KhoiTao() {

        arrForspSapxep.add("Tăng dần theo giá");
        arrForspSapxep.add("Giảm dần theo giá");
        arrForspSapxep.add("Tăng dần theo đánh giá sao");
        arrForspSapxep.add("Giảm dần theo đánh giá sao");
    }

    private void setControl() {
        rcvPhong = findViewById(R.id.rcvPhong);
        spSapxep = findViewById(R.id.spSapxep);
    }
}