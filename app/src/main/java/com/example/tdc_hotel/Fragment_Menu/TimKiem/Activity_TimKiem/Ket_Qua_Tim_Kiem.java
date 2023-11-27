package com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tdc_hotel.Model.phong;
import com.example.tdc_hotel.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Ket_Qua_Tim_Kiem extends AppCompatActivity {
    RecyclerView rcvPhong;
    TextView tv_thongtintim;
    Spinner spSapxep;
    ImageButton btn_back;
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
        tv_thongtintim.setText(ngayNhan + "-" + ngayTra + "( " + loaiPhong + " )");
    }

    private void setEvent() {
        KhoiTao();
        rcvPhong.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvPhong.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcvPhong.setAdapter(adapter_Finding);

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrForspSapxep);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSapxep.setAdapter(adapterSpinner);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spSapxep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter = parent.getItemAtPosition(position).toString();
                ArrayList<phong> filterList = new ArrayList<>();
                filterList.addAll(adapter_Finding.getFindingList());

                switch (filter) {
                    case "Tăng dần theo giá":
                        if (filterList != null && filterList.size() > 0) {
                           Collections.sort(filterList, Comparator.comparingDouble(phong::getGia));
                        }
                        break;

                    case "Giảm dần theo giá":
                        if (filterList != null && filterList.size() > 0) {
                           Collections.sort(filterList, Comparator.comparingDouble(phong::getGia).reversed());

                        }
                        break;

                    case "Tăng dần theo đánh giá sao":
                        if (filterList != null && filterList.size() > 0) {
                            Collections.sort(filterList, Comparator.comparingDouble(phong::getDanh_gia_sao));
                        }
                        break;

                    case "Giảm dần theo đánh giá sao":
                        if (filterList != null && filterList.size() > 0) {
                            Collections.sort(filterList, Comparator.comparingDouble(phong::getDanh_gia_sao).reversed());
                        }
                        break;

                    default:
                        break;
                }

                adapter_Finding.setFindingList(filterList);
                adapter_Finding.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        tv_thongtintim = findViewById(R.id.tv_thongtintim);
        btn_back = findViewById(R.id.btn_back);
    }
}
