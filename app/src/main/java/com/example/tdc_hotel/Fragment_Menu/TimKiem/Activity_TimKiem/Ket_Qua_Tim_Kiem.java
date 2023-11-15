package com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tdc_hotel.R;

import java.util.ArrayList;

public class Ket_Qua_Tim_Kiem extends AppCompatActivity {
    RecyclerView rcvPhong;
    Spinner spSapxep;
    ArrayList<String> arrForspSapxep = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_qua_tim_kiem);
        setControl();
        Initialization();
    }

    private void Initialization() {
        KQ_TimKiem_Adapter kq_timKiem_adapter = new KQ_TimKiem_Adapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvPhong.addItemDecoration(dividerItemDecoration);
        rcvPhong.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvPhong.setAdapter(kq_timKiem_adapter);
        arrForspSapxep.add("Tăng dần theo giá");
        arrForspSapxep.add("Giảm dần theo giá");
        arrForspSapxep.add("Tăng dần theo đánh giá sao");
        arrForspSapxep.add("Giảm dần theo đánh giá sao");
        spSapxep.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrForspSapxep));
    }

    private void setControl() {
        rcvPhong = findViewById(R.id.rcvPhong);
        spSapxep = findViewById(R.id.spSapxep);
    }
}