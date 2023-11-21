package com.example.tdc_hotel.Fragment_Menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tdc_hotel.Fragment_Menu.ThongTinKhach.adapter_DaLuu;
import com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem.KQ_TimKiem_Adapter;
import com.example.tdc_hotel.R;

import java.util.ArrayList;


public class Fragment_DaLuu extends Fragment {
    RecyclerView rcvPhong;
    Spinner spTimkiem;
    ArrayList<String> arrForspTimkiem = new ArrayList<>();
    adapter_DaLuu adapterDaLuu;

    public Fragment_DaLuu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__da_luu, container, false);
        setControl(view);
        Initialization();
        return view;
    }

    private void Initialization() {
        adapterDaLuu = new adapter_DaLuu(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcvPhong.addItemDecoration(dividerItemDecoration);
        rcvPhong.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rcvPhong.setAdapter(adapterDaLuu);
        arrForspTimkiem.add("Tăng dần theo giá");
        arrForspTimkiem.add("Giảm dần theo giá");
        arrForspTimkiem.add("Tăng dần theo đánh giá sao");
        arrForspTimkiem.add("Giảm dần theo đánh giá sao");
        spTimkiem.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrForspTimkiem));
    }

    private void setControl(View view) {
        rcvPhong = view.findViewById(R.id.rcvPhongDaLuu);
        spTimkiem = view.findViewById(R.id.spTimkiem);
    }
}
