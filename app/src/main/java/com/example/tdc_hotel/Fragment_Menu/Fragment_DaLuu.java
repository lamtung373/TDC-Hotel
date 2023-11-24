package com.example.tdc_hotel.Fragment_Menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Fragment_Menu.ThongTinKhach.adapter_DaLuu;
import com.example.tdc_hotel.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_DaLuu extends Fragment {
    RecyclerView rcvPhong;
    adapter_DaLuu adapterDaLuu;
    Spinner spTimkiem;

    public Fragment_DaLuu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__da_luu, container, false);
        setControl(view);
        Initialization();
        return view;
    }

    private void Initialization() {
        adapterDaLuu = new adapter_DaLuu(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvPhong.addItemDecoration(dividerItemDecoration);
        rcvPhong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvPhong.setAdapter(adapterDaLuu);

        // Khởi tạo dữ liệu cho Spinner
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Sắp xếp");
        spinnerItems.add("Sắp xếp giảm dần theo giá");
        spinnerItems.add("Sắp xếp tăng dần theo giá");
        spinnerItems.add("Sắp xếp theo lượt thuê");
        spinnerItems.add("Sắp xếp từ A - Z");
        spinnerItems.add("Sắp xếp từ Z - A");


        // Tạo ArrayAdapter để hiển thị dữ liệu trên Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Gán Adapter cho Spinner
        spTimkiem.setAdapter(spinnerAdapter);

        // Xử lý sự kiện khi chọn một phần tử trên Spinner
        // Trong Fragment_DaLuu
        spTimkiem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Xử lý khi chọn một phần tử trên Spinner
                String selectedItem = spinnerItems.get(position);

                // Thực hiện các hành động mong muốn dựa trên phần tử được chọn
                if ("Sắp xếp giảm dần theo giá".equals(selectedItem)) {
                    adapterDaLuu.sortByPriceDescending();
                } else if ("Sắp xếp tăng dần theo giá".equals(selectedItem)) {
                    adapterDaLuu.sortByPriceAscending();
                }else if ("Sắp xếp theo lượt thuê".equals(selectedItem)) {
                    adapterDaLuu.sortByRentals();
                }else if ("Sắp xếp từ A - Z".equals(selectedItem)) {
                    adapterDaLuu.sortByFirstLetter();
                }else if ("Sắp xếp từ Z - A".equals(selectedItem)) {
                    adapterDaLuu.sortByLastLetter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Nếu không có phần tử nào được chọn
            }
        });
    }
        private void setControl(View view) {

        rcvPhong = view.findViewById(R.id.rcvPhongDaLuu);
        spTimkiem = view.findViewById(R.id.spTimkiem);
    }
}
