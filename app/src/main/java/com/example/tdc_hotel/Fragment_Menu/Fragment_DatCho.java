package com.example.tdc_hotel.Fragment_Menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem.KQ_TimKiem_Adapter;
import com.example.tdc_hotel.R;
import com.example.tdc_hotel.Tab_Layout_LichSu.Tab_Layout_Adapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_DatCho#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_DatCho extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rcvPhong;
    TabLayout tlphong;
    ViewPager2 vpphong;
    public Fragment_DatCho() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_DatCho.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_DatCho newInstance(String param1, String param2) {
        Fragment_DatCho fragment = new Fragment_DatCho();
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
        View view = inflater.inflate(R.layout.fragment__dat_cho, container, false);
        setControl(view);
        Initialization();
        return view;
    }
    private void Initialization() {
        Tab_Layout_Adapter tab_layout_fragment_trang_chu = new Tab_Layout_Adapter(getActivity());
        vpphong.setAdapter(tab_layout_fragment_trang_chu);
        new TabLayoutMediator(tlphong, vpphong, ((tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Đã đặt");
                    break;
                case 1:
                    tab.setText("Hoạt động");
                    break;
                case 2:
                    tab.setText("Hoàn tất");
                    break;
                    case 3:
                    tab.setText("Đã hủy");
                    break;
            }
        })).attach();

    }
    private void setControl(View view) {
        tlphong = view.findViewById(R.id.tlphong);
        vpphong = view.findViewById(R.id.vpphong);
    }
}