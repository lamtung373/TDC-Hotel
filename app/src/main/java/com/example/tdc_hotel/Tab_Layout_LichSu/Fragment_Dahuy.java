package com.example.tdc_hotel.Tab_Layout_LichSu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem.KQ_TimKiem_Adapter;
import com.example.tdc_hotel.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Dahuy#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Dahuy extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rcvPhong;
    public Fragment_Dahuy() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Dahuy.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Dahuy newInstance(String param1, String param2) {
        Fragment_Dahuy fragment = new Fragment_Dahuy();
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
        View view = inflater.inflate(R.layout.fragment__dahuy, container, false);
        setControl(view);
        Initialization();
        return view;
    }
    private void Initialization() {
        KQ_TimKiem_Adapter kq_timKiem_adapter=new KQ_TimKiem_Adapter();
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        rcvPhong.addItemDecoration(dividerItemDecoration);
        rcvPhong.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rcvPhong.setAdapter(kq_timKiem_adapter);
    }
    private void setControl(View view) {
        rcvPhong = view.findViewById(R.id.rcvPhong);
    }
}