package com.example.tdc_hotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong.Bottom_Sheet_Fragment;
import com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong.DichVuPhong_Adapter;
import com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong.DichVuTheoNguoi_Adapter;
import com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong.Hoan_Tat_Thongtin_DatPhong;
import com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem.KQ_TimKiem_Adapter;
import com.example.tdc_hotel.Photo.Photo_Adapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator3;

public class Chi_Tiet_Phong extends AppCompatActivity {
    ViewPager2 vpiv;
    CircleIndicator3 ci;
    Button btnDatphong;
    TextView tvGiacu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phong);
        setControl();
        Initialization();
        setEvent();
    }

    private void setEvent() {
        btnDatphong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chi_Tiet_Phong.this, Hoan_Tat_Thongtin_DatPhong.class);
                startActivity(intent);
//                ClickOpenBottomSheet();
//                Intent intent = new Intent(Chi_Tiet_Phong.this, Thanh_Toan.class);
//                startActivity(intent);
            }
        });

    }

//    private void ClickOpenBottomSheet() {
//        Bottom_Sheet_Fragment bottom_sheet_fragment=Bottom_Sheet_Fragment.newInstance();
//        bottom_sheet_fragment.show(getSupportFragmentManager(),bottom_sheet_fragment.getTag());
//    }

    private void Initialization() {

        //Chuyen anh
        Photo_Adapter adapter = new Photo_Adapter();
        vpiv.setAdapter(adapter);
        ci.setViewPager(vpiv);

        //Tao hieu ung khi chuyen anh
        vpiv.setOffscreenPageLimit(3);
        vpiv.setClipToPadding(false);
        vpiv.setClipChildren(false);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        vpiv.setPageTransformer(compositePageTransformer);

        //Tu dong chuyen anh
        AutoSlideImage();

        //Gach ngang chu
        tvGiacu.setPaintFlags(tvGiacu.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


    }

    private void AutoSlideImage() {
//        if(mTimer==null){
//            mTimer=new Timer();
//        }
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        int curentItem=vpiv.getCurrentItem();
//                        int totalItem=5-1;
//                        if(curentItem<totalItem){
//                            curentItem++;
//                            vpiv.setCurrentItem(curentItem);
//                        }
//                        else{
//                            vpiv.setCurrentItem(0);
//                        }
//                    }
//                });
//            }
//        },500,3000);
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = vpiv.getCurrentItem();
                if (currentItem == 5 - 1) {
                    vpiv.setCurrentItem(0);
                } else {
                    vpiv.setCurrentItem(vpiv.getCurrentItem() + 1);
                }
            }
        };
        vpiv.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 3000);
            }
        });
    }

    private void setControl() {
        vpiv = findViewById(R.id.vpiv);
        ci = findViewById(R.id.ci);

        btnDatphong = findViewById(R.id.btnDatphong);
        tvGiacu = findViewById(R.id.tvGiacu);


    }
}