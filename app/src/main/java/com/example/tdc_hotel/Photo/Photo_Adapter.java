package com.example.tdc_hotel.Photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.tdc_hotel.R;

public class Photo_Adapter extends  RecyclerView.Adapter<Photo_Adapter.Photo_ViewHolder>{
    public Photo_Adapter() {
    }

    @NonNull
    @Override
    public Photo_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_iv,parent,false);
        return new Photo_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Photo_ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class Photo_ViewHolder extends RecyclerView.ViewHolder {

        public Photo_ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
