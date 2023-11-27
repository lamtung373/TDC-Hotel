package com.example.tdc_hotel.Tab_Layout_LichSu;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdc_hotel.Danh_Gia;
import com.example.tdc_hotel.Model.danh_gia;
import com.example.tdc_hotel.Model.hoa_don;
import com.example.tdc_hotel.Model.phong;
import com.example.tdc_hotel.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class HoanTatAdapter extends RecyclerView.Adapter<HoanTatAdapter.HoanTatViewHolder>{
    private List<hoa_don> hoaDonList;
    private List<phong> phongList;
    private List<danh_gia> danhGiaList;
    Context context;
    public HoanTatAdapter(List<hoa_don> hoaDonList, List<phong> phongList, Context context,List<danh_gia> danhGiaList) {
        this.hoaDonList = hoaDonList;
        this.phongList = phongList;
        this.context = context;
        this.danhGiaList = danhGiaList;
    }
    @NonNull
    @Override
    public HoanTatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_hoantat,parent,false);
        return new HoanTatAdapter.HoanTatViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HoanTatViewHolder holder, int position) {
        hoa_don hoaDon  = hoaDonList.get(position);
        if(hoaDon==null)
        {
            return;
        }
        DecimalFormat decimalFormatt = new DecimalFormat("###,###,###");
        String tongThanhToanFormatted = decimalFormatt.format(hoaDon.getTong_thanh_toan());
        String moTa = "Tổng thanh toán: "+tongThanhToanFormatted
                +"\n"+hoaDon.getThoi_gian_nhan_phong().toString()
                +"-"+hoaDon.getThoi_gian_tra_phong().toString();
        holder.tvMota.setText(moTa);
        for (int i = 0; i < phongList.size(); i++)
        {
            if(phongList.get(i).getId_phong().equals(hoaDon.getId_phong())) {
                holder.tvTenphong.setText(phongList.get(i).getTen_phong());
                Picasso.get().load(phongList.get(i).getAnh_phong().get(0).toString()).into(holder.ivPhong);
                holder.tvDanhGia.setText(String.valueOf(phongList.get(i).getDanh_gia_sao()));
                break;
            }
        }
        for (int i = 0; i < danhGiaList.size(); i++)
        {
            if(danhGiaList.get(i).getId_hoadon().equals(hoaDon.getId_hoa_don()))
            {
                holder.btnDanhgia.setText("Đã đánh giá");
                holder.btnDanhgia.setEnabled(false);
                break;
            }
        }
        holder.btnDanhgia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, Danh_Gia.class);
                        intent.putExtra("hoaDon",hoaDon);
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

    class HoanTatViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTenphong, tvMota, tvDanhGia;
        ImageView ivPhong;
        Button btnDanhgia;
        public HoanTatViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhong = itemView.findViewById(R.id.ivPhong);
            tvTenphong = itemView.findViewById(R.id.tvTenphong);
            tvDanhGia = itemView.findViewById(R.id.tvDanhGia);
            tvMota = itemView.findViewById(R.id.tvMota);
            btnDanhgia = itemView.findViewById(R.id.btnDanhgia);
        }
    }
}
