package com.example.tdc_hotel.Fragment_Menu;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem.Ket_Qua_Tim_Kiem;
import com.example.tdc_hotel.Fragment_Menu.TimKiem.Adapter_ChonLoc;
import com.example.tdc_hotel.Model.khach_hang;
import com.example.tdc_hotel.R;
import com.example.tdc_hotel.Xac_Thuc_OTP;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

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

    // Khai báo các thành phần giao diện
    RecyclerView rcvLuotthue, rcvDanhgia, rcvGia, rcvAll;
    Button btnTimkiem, btn_thoigiannhan, btn_thoigiantra;
    Spinner spRoomType;
    ArrayList<String> typeRoomList = new ArrayList<>();
    private String getLoaiPhong = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Timer timerLuotthue, timerDanhgia, timerGia, timerAll;
    TextView tvxinchao;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Gắn layout cho fragment
        View view = inflater.inflate(R.layout.fragment__tim_kiem, container, false);
        setControl(view);
        Initialization();
        setEvent();
        return view;
    }

    private void setEvent() {
        getNameCustomer();
        // Xử lý sự kiện khi loại phòng được chọn từ Spinner
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
        timKiemPhong();
    }


    void timKiemPhong() {
        // Xử lý sự kiện khi nút tìm kiếm được nhấn
        btnTimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra nếu đã chọn đủ thông tin cần thiết
                if (btn_thoigiannhan.getText().toString() != null &&
                        btn_thoigiantra.getText().toString() != null &&
                        getLoaiPhong != null) {
                    // Chuyển sang màn hình kết quả tìm kiếm với các thông tin đã chọn
                    Intent intent = new Intent(getActivity(), Ket_Qua_Tim_Kiem.class);
                    intent.putExtra("ngayNhan", btn_thoigiannhan.getText().toString());
                    intent.putExtra("ngayTra", btn_thoigiantra.getText().toString());
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
                Calendar calendar = Calendar.getInstance();
                final Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);

                DatePickerDialog.OnDateSetListener dataListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if (calendar.before(today)) {
                            Toast.makeText(getActivity(), "Thời gian nhận phòng phải ít nhất phải bắt đầu từ hôm nay.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        btn_thoigiannhan.setText(dateFormat.format(calendar.getTime()));
                    }
                };
                new DatePickerDialog(getActivity(), dataListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                        if (btn_thoigiantra.getText().toString().equals(btn_thoigiannhan.getText())) {
                            Toast.makeText(getActivity(), "Thời gian trả phòng phải sau ngày nhận phòng ít nhất 1 ngày", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (calendarTra.before(Calendar.getInstance())) {
                            Toast.makeText(getActivity(), "Thời gian trả phòng phải sau thời gian hiện tại.", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            btn_thoigiantra.setText(dateFormat.format(calendarTra.getTime()));
                        }
                    }
                };
                new DatePickerDialog(getActivity(), dataListener, calendarTra.get(Calendar.YEAR), calendarTra.get(Calendar.MONTH), calendarTra.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    void getNameCustomer() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Xac_Thuc_OTP.SharedPreferences, MODE_PRIVATE);
        String sdt = sharedPreferences.getString(Xac_Thuc_OTP.sdt_kh, "");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("khach_hang").child(sdt);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (snapshot.exists()) {
                        khach_hang customer=snapshot.getValue(khach_hang.class);
                        if (!customer.toMap().isEmpty()&& customer.getSo_dien_thoai().equals(sdt)){
                            tvxinchao.setText("Xin chào "+ customer.getTen());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Initialization() {
        Adapter_ChonLoc luotThue = new Adapter_ChonLoc(getContext(), "luot_thue");
        rcvLuotthue.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcvLuotthue.setAdapter(luotThue);


        Adapter_ChonLoc danhGia = new Adapter_ChonLoc(getContext(), "danh_gia_sao");
        rcvDanhgia.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcvDanhgia.setAdapter(danhGia);


        Adapter_ChonLoc giaThue = new Adapter_ChonLoc(getContext(), "sale");
        rcvGia.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcvGia.setAdapter(giaThue);

        Adapter_ChonLoc all = new Adapter_ChonLoc(getContext(), "");
        rcvAll.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcvAll.setAdapter(all);
        // Khởi tạo và thiết lập sự kiện cho từng RecyclerView
        AutoScroll(rcvLuotthue, timerLuotthue, 5300);
        AutoScroll(rcvDanhgia, timerDanhgia, 5700);
        AutoScroll(rcvGia, timerGia, 5300);
        AutoScroll(rcvAll, timerAll, 5700);


        typeRoomList.add("1 Người");
        typeRoomList.add("2 Người");
        typeRoomList.add("3 Người");
        typeRoomList.add("4 Người");
        typeRoomList.add("5 Người");
        spRoomType.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, typeRoomList));
    }

    private void AutoScroll(RecyclerView recyclerView, Timer timer, long scrollTime) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (layoutManager.findFirstCompletelyVisibleItemPosition() < (recyclerView.getAdapter().getItemCount() - 1)) {
                    recyclerView.smoothScrollToPosition(layoutManager.findFirstCompletelyVisibleItemPosition() + 1);
                } else {
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        }, 0, scrollTime); // Thời gian cuộn tùy thuộc vào tham số scrollTime
    }

    private void setControl(View view) {
        tvxinchao = view.findViewById(R.id.tvxinchao);
        rcvLuotthue = view.findViewById(R.id.rcvLuotthue);
        rcvDanhgia = view.findViewById(R.id.rcvDanhgia);
        rcvGia = view.findViewById(R.id.rcvGia);
        rcvAll = view.findViewById(R.id.rcvAll);
        btnTimkiem = view.findViewById(R.id.btnTimkiem);
        spRoomType = view.findViewById(R.id.spRoomType);
        btn_thoigiannhan = view.findViewById(R.id.btn_thoigiannhan);
        btn_thoigiantra = view.findViewById(R.id.btn_thoigiantra);
    }
}