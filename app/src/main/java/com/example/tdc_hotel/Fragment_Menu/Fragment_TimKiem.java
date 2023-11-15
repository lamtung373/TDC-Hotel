package com.example.tdc_hotel.Fragment_Menu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem.Ket_Qua_Tim_Kiem;
import com.example.tdc_hotel.Fragment_Menu.TimKiem.DanhGia_Adapter;
import com.example.tdc_hotel.Fragment_Menu.TimKiem.Gia_Adapter;
import com.example.tdc_hotel.Fragment_Menu.TimKiem.LuotThue_Adapter;
import com.example.tdc_hotel.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_TimKiem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_TimKiem extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rcvLuotthue, rcvDanhgia, rcvGia;
    Button btnTimkiem, btn_thoigiannhan;
    Spinner spTimkiem;
    ArrayList<String> arrForspTimkiem = new ArrayList<>();

    public Fragment_TimKiem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_TimKiem.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_TimKiem newInstance(String param1, String param2) {
        Fragment_TimKiem fragment = new Fragment_TimKiem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__tim_kiem, container, false);
        setControl(view);
        Initialization();
        setEvent();
        return view;
    }

    private void setEvent() {
        ChonThoiGianNhan();
        btnTimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Ket_Qua_Tim_Kiem.class);
                getActivity().startActivity(intent);
            }
        });
    }

    void ChonThoiGianNhan() {
        btn_thoigiannhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dataListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                btn_thoigiannhan.setText(dateFormat.format(calendar.getTime()));
                            }
                        };
                        new TimePickerDialog(getActivity(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
                    }
                };
                new DatePickerDialog(getActivity(), dataListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void Initialization() {
        LuotThue_Adapter luotThue_adapter = new LuotThue_Adapter(getActivity());
        DanhGia_Adapter danhGia_adapter = new DanhGia_Adapter();
        Gia_Adapter gia_adapter = new Gia_Adapter();
        rcvLuotthue.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcvLuotthue.setAdapter(luotThue_adapter);
        rcvDanhgia.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcvDanhgia.setAdapter(danhGia_adapter);
        rcvGia.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcvGia.setAdapter(gia_adapter);
        arrForspTimkiem.add("Phòng 1 người");
        arrForspTimkiem.add("Phòng 2 người");
        arrForspTimkiem.add("Phòng 3 người");
        arrForspTimkiem.add("Phòng 4 người");
        arrForspTimkiem.add("Phòng 5 người");
        spTimkiem.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrForspTimkiem));
    }

    private void setControl(View view) {
        rcvLuotthue = view.findViewById(R.id.rcvLuotthue);
        rcvDanhgia = view.findViewById(R.id.rcvDanhgia);
        rcvGia = view.findViewById(R.id.rcvGia);
        btnTimkiem = view.findViewById(R.id.btnTimkiem);
        spTimkiem = view.findViewById(R.id.spTimkiem);
        btn_thoigiannhan = view.findViewById(R.id.btn_thoigiannhan);
    }
}