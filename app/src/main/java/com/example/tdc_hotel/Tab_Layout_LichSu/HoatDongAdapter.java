package com.example.tdc_hotel.Tab_Layout_LichSu;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Danh_Gia;
import com.example.tdc_hotel.Model.hoa_don;
import com.example.tdc_hotel.Model.phong;
import com.example.tdc_hotel.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class HoatDongAdapter extends RecyclerView.Adapter<HoatDongAdapter.HoatDongViewHolder>{
    private List<hoa_don> hoaDonList;
    private List<phong> phongList;
    Context context;
    public HoatDongAdapter(List<hoa_don> hoaDonList, List<phong> phongList, Context context) {
        this.hoaDonList = hoaDonList;
        this.phongList = phongList;
        this.context = context;
    }

    @NonNull
    @Override
    public HoatDongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_da_dat,parent,false);
        return new HoatDongAdapter.HoatDongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoatDongViewHolder holder, int position) {
        hoa_don hoaDon  = hoaDonList.get(position);
        if(hoaDon==null)
        {
            return;
        }
        DecimalFormat decimalFormatt = new DecimalFormat("###,###,###");
        String tongThanhToanFormatted = decimalFormatt.format(hoaDon.getTong_thanh_toan());
        String tienCocFormatted = decimalFormatt.format(hoaDon.getTien_coc());
        String moTa = "Mã hóa đơn: "+hoaDon.getId_hoa_don().toString()
                +"\n"+hoaDon.getThoi_gian_nhan_phong().toString()
                +"-"+hoaDon.getThoi_gian_tra_phong().toString()
                +"\nĐã trả: " +tienCocFormatted+"đ"
                +"\nTổng: "+tongThanhToanFormatted+"đ";
        holder.tvMota.setText(moTa);
        for (int i = 0; i < phongList.size(); i++)
        {
            if(phongList.get(i).getId_phong().equals(hoaDon.getId_phong())) {
                holder.tvTenphong.setText(phongList.get(i).getTen_phong());
                Picasso.get().load(phongList.get(i).getAnh_phong().get(1).toString()).into(holder.ivPhong);
                break;
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(context, Danh_Gia.class);
               context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(hoaDonList != null)
        {
            return  hoaDonList.size();

        }
        return 0;
    }

    class HoatDongViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTenphong, tvMota;
        ImageView ivPhong;
        public HoatDongViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhong = itemView.findViewById(R.id.ivPhong);
            tvTenphong = itemView.findViewById(R.id.tvTenphong);
            tvMota = itemView.findViewById(R.id.tvMota);
        }

    }
}
