package com.example.tdc_hotel.Tab_Layout_LichSu;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem.Adapter_ketQuaTimKiem;
import com.example.tdc_hotel.Model.hoa_don;
import com.example.tdc_hotel.Model.khach_hang;
import com.example.tdc_hotel.Model.phong;
import com.example.tdc_hotel.R;
import com.example.tdc_hotel.Xac_Thuc_OTP;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Danghd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Danghd extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<hoa_don> hoaDonList = new ArrayList<>();
    private List<khach_hang> khachHangList = new ArrayList<>();
    private List<phong> phongList = new ArrayList<>();
    private HoatDongAdapter daDatAdapter;
    RecyclerView rcvPhong;
    public Fragment_Danghd() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Danghd.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Danghd newInstance(String param1, String param2) {
        Fragment_Danghd fragment = new Fragment_Danghd();
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
        View view = inflater.inflate(R.layout.fragment__danghd, container, false);
        setControl(view);
        return view;
    }
    private void Initialization() {
        //Adapter_ketQuaTimKiem kq_timKiem_adapter=new Adapter_ketQuaTimKiem();
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        rcvPhong.addItemDecoration(dividerItemDecoration);
        rcvPhong.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
       // rcvPhong.setAdapter(kq_timKiem_adapter);
    }
    private void LoadHoaDon() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("hoa_don");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hoaDonList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        hoa_don hoaDon = dataSnapshot1.getValue(hoa_don.class);
                        SharedPreferences sharedPreferences =getActivity().getSharedPreferences(Xac_Thuc_OTP.SharedPreferences, MODE_PRIVATE);
                        String sdt_kh = sharedPreferences.getString(Xac_Thuc_OTP.sdt_kh, "");
                        if(!hoaDon.getThoi_gian_duyet().equals("")&&hoaDon.getSo_dien_thoai().toString().equals(sdt_kh)&&!hoaDon.getCCCD().get(0).equals("  ")&&!hoaDon.getCCCD().get(1).equals(""))
                        {
                            if(hoaDon.getThoi_gian_thanh_toan().toString().equals("")&&hoaDon.getThoi_gian_huy().toString().equals(""))
                            {
                                hoaDonList.add(hoaDon);
                            }
                        }
                    }
                }
                daDatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void LoadPhong() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("phong");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phongList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    phong phong = dataSnapshot.getValue(phong.class);
                    phongList.add(phong);
                }
                daDatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setControl(View view) {
        rcvPhong = view.findViewById(R.id.rcvPhong);
        LoadHoaDon();
        LoadPhong();

        daDatAdapter = new HoatDongAdapter(hoaDonList,phongList,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvPhong.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        rcvPhong.addItemDecoration(decoration);
        rcvPhong.setAdapter(daDatAdapter);
    }
}