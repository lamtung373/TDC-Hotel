package com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Chi_Tiet_Phong;
import com.example.tdc_hotel.R;
import com.example.tdc_hotel.Thanh_Toan;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Bottom_Sheet_Fragment extends BottomSheetDialogFragment {
    RecyclerView rcvDichvu_phong,rcvDichvu_theonguoi;
    Button btnTienhanhthanhtoan;
    public static final Bottom_Sheet_Fragment newInstance(){
        Bottom_Sheet_Fragment bottom_sheet_fragment=new Bottom_Sheet_Fragment();
        return bottom_sheet_fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog= (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottomsheet_dichvu,null);
        bottomSheetDialog.setContentView(view);
        setControl(view);
        Initialization();
        BottomSheetBehavior behavior=BottomSheetBehavior.from((View)view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        setEvent();
        return bottomSheetDialog;
    }

    private void setEvent() {
        btnTienhanhthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Thanh_Toan.class);
                startActivity(intent);
            }
        });
    }

    private void Initialization() {
        //Tao danh sach dich vu phong
        DichVuPhong_Adapter dichVuPhong_adapter=new DichVuPhong_Adapter();
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvDichvu_phong.addItemDecoration(dividerItemDecoration);
        rcvDichvu_phong.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rcvDichvu_phong.setAdapter(dichVuPhong_adapter);

        //Tao danh sach dich vu theo nguoi
        DichVuTheoNguoi_Adapter dichVuTheoNguoi_adapter=new DichVuTheoNguoi_Adapter();
        rcvDichvu_theonguoi.addItemDecoration(dividerItemDecoration);
        rcvDichvu_theonguoi.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rcvDichvu_theonguoi.setAdapter(dichVuTheoNguoi_adapter);

    }

    private void setControl(View view) {
        rcvDichvu_phong = view.findViewById(R.id.rcvDichvu_phong);
        rcvDichvu_theonguoi = view.findViewById(R.id.rcvDichvu_theonguoi);
        btnTienhanhthanhtoan = view.findViewById(R.id.btnTienhanhthanhtoan);
    }
}