package com.example.tdc_hotel.Fragment_Menu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

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

    RecyclerView rcvLuotthue, rcvDanhgia, rcvGia;
    Button btnTimkiem, btn_thoigiannhan, btn_thoigiantra;
    Spinner spRoomType;
    ArrayList<String> typeRoomList = new ArrayList<>();
    private String getLoaiPhong = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

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
        spRoomType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getLoaiPhong = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });
        ChonThoiGianNhan();
        ChonThoiGianTra();
        timKiemPhong(btn_thoigiannhan.getText().toString(), btn_thoigiantra.getText().toString());
    }


    void timKiemPhong(String thoiGianNhan, String thoiGianTra) {
        btnTimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("sploaiphong", getLoaiPhong + btn_thoigiannhan.getText() + btn_thoigiantra.getText());
                if (thoiGianNhan != null && thoiGianTra != null&& getLoaiPhong!=null) {
                    Intent intent = new Intent(getActivity(), Ket_Qua_Tim_Kiem.class);
                    intent.putExtra("ngayNhan", thoiGianNhan);
                    intent.putExtra("ngayTra", thoiGianTra);
                    intent.putExtra("loaiPhong", getLoaiPhong);
                    getActivity().startActivity(intent);
                } else {
                    // Hiển thị thông báo lỗi nếu thiếu thông tin
                    Toast.makeText(getActivity(), "Vui lòng chọn đủ ngày nhận phòng, trả phòng và loại phòng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    void ChonThoiGianNhan() {
        btn_thoigiannhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentCalendar = Calendar.getInstance();
                final Calendar selectedCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dataListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedCalendar.set(year, month, dayOfMonth);
                        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedCalendar.set(Calendar.MINUTE, minute);

                                // Kiểm tra ngày và thời gian được chọn có sau (hoặc bằng) ngày và thời gian hiện tại không
                                if (!selectedCalendar.before(currentCalendar)) {
                                    btn_thoigiannhan.setText(dateFormat.format(selectedCalendar.getTime()));
                                } else {
                                    // Hiển thị thông báo lỗi nếu ngày và thời gian được chọn không hợp lệ
                                    Toast.makeText(getActivity(), "Vui lòng chọn một thời gian từ bây giờ trở đi.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        new TimePickerDialog(getActivity(), timeSetListener, selectedCalendar.get(Calendar.HOUR_OF_DAY), selectedCalendar.get(Calendar.MINUTE), true).show();
                    }
                };
                new DatePickerDialog(getActivity(), dataListener, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    void ChonThoiGianTra() {
        btn_thoigiantra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendarNhan = Calendar.getInstance();
                final Calendar calendarTra = Calendar.getInstance();

                // Kiểm tra xem ngày nhận đã được chọn chưa
                if (btn_thoigiannhan.getText().toString().equals("")) {
                    // Nếu chưa chọn, đặt ngày nhận là ngày hiện tại và cập nhật btn_thoigiannhan
                    btn_thoigiannhan.setText(dateFormat.format(calendarNhan.getTime()));
                    Toast.makeText(getActivity(), "Thời gian nhận phòng đã được mặc định là bây giờ", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        calendarNhan.setTime(dateFormat.parse(btn_thoigiannhan.getText().toString()));
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Định dạng thời gian nhận không hợp lệ.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                DatePickerDialog.OnDateSetListener dataListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendarTra.set(Calendar.YEAR, year);
                        calendarTra.set(Calendar.MONTH, month);
                        calendarTra.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendarTra.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendarTra.set(Calendar.MINUTE, minute);

                                if (!calendarTra.after(calendarNhan)) {
                                    Toast.makeText(getActivity(), "Thời gian trả phòng phải sau thời gian nhận phòng.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                btn_thoigiantra.setText(dateFormat.format(calendarTra.getTime()));
                            }
                        };
                        new TimePickerDialog(getActivity(), timeSetListener, calendarTra.get(Calendar.HOUR_OF_DAY), calendarTra.get(Calendar.MINUTE), false).show();
                    }
                };
                new DatePickerDialog(getActivity(), dataListener, calendarTra.get(Calendar.YEAR), calendarTra.get(Calendar.MONTH), calendarTra.get(Calendar.DAY_OF_MONTH)).show();
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
        typeRoomList.add("1 Người");
        typeRoomList.add("2 Người");
        typeRoomList.add("3 Người");
        typeRoomList.add("4 Người");
        typeRoomList.add("5 Người");
        spRoomType.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, typeRoomList));
    }

    private void setControl(View view) {
        rcvLuotthue = view.findViewById(R.id.rcvLuotthue);
        rcvDanhgia = view.findViewById(R.id.rcvDanhgia);
        rcvGia = view.findViewById(R.id.rcvGia);
        btnTimkiem = view.findViewById(R.id.btnTimkiem);
        spRoomType = view.findViewById(R.id.spRoomType);
        btn_thoigiannhan = view.findViewById(R.id.btn_thoigiannhan);
        btn_thoigiantra = view.findViewById(R.id.btn_thoigiantra);
    }
}