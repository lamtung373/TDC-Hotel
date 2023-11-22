package com.example.tdc_hotel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong.Bottom_Sheet_Fragment;
import com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong.DichVuPhong_Adapter;
import com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong.DichVuTheoNguoi_Adapter;
import com.example.tdc_hotel.Fragment_Menu.ChiTiet_Phong.Hoan_Tat_Thongtin_DatPhong;
import com.example.tdc_hotel.Fragment_Menu.TimKiem.Activity_TimKiem.KQ_TimKiem_Adapter;
import com.example.tdc_hotel.Model.chi_tiet_tien_nghi;
import com.example.tdc_hotel.Model.phong;
import com.example.tdc_hotel.Model.tien_nghi;
import com.example.tdc_hotel.Model.yeu_thich;
import com.example.tdc_hotel.Photo.Photo_Adapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator3;

public class Chi_Tiet_Phong extends AppCompatActivity {
    ViewPager2 vpiv;
    ImageView iv_heart;
    CircleIndicator3 ci;
    Button btnDatphong;
    TextView tvGiacu, tv_tenphong, tvGiamoi, tv_tiennghi, tv_danhgia, tv_mota;
    ImageView img_Back;
    phong phong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phong);
        phong = (phong) getIntent().getSerializableExtra("phong");
        setControl();
        Initialization();
        setEvent();
    }

    private void setEvent() {
        String id_kh = "123";
        DatabaseReference reference_yeuthich = FirebaseDatabase.getInstance().getReference("yeu_thich");
        reference_yeuthich.child(id_kh).child("-Nj83mHkf60DbzdG20ai").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    iv_heart.setImageResource(R.drawable.heart_red);
                } else {
                    iv_heart.setImageResource(R.drawable.heart_black);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        iv_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy Drawable được gán cho ImageView
                Drawable drawable = iv_heart.getDrawable();

// So sánh địa chỉ của Drawable với địa chỉ của Drawable ảnh A
                if (drawable != null && drawable.getConstantState() != null) {
                    Drawable drawableA = getResources().getDrawable(R.drawable.heart_red); // Thay thế "image_a" bằng tên của ảnh A trong thư mục drawable
                    if (drawable.getConstantState().equals(drawableA.getConstantState())) {
                        iv_heart.setImageResource(R.drawable.heart_black);
                        reference_yeuthich.child(id_kh).child(phong.getId_phong()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(Chi_Tiet_Phong.this, "Đã xóa khỏi danh sách yêu thích của bạn!!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        iv_heart.setImageResource(R.drawable.heart_red);
                        yeu_thich yeuThich = new yeu_thich(id_kh, phong.getId_phong());
                        reference_yeuthich.child(id_kh).child(phong.getId_phong()).setValue(yeuThich, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(Chi_Tiet_Phong.this, "Đã thêm vào danh sách yêu thích của bạn!!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnDatphong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chi_Tiet_Phong.this, Hoan_Tat_Thongtin_DatPhong.class);
                intent.putExtra("phong", phong);
                startActivity(intent);
            }
        });
    }

    private void Initialization() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phong").child(phong.getId_phong());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phong phong = snapshot.getValue(phong.class);

                //Lay du lieu duoc truyen tu man hinh danh sach phong san sang

                //Chuyen anh
                if (phong.getAnh_phong() != null && !phong.getAnh_phong().isEmpty()) {
                    Photo_Adapter adapter = new Photo_Adapter(phong.getAnh_phong());
                    vpiv.setAdapter(adapter);
                    ci.setViewPager(vpiv);
                    AutoSlideImage();
                }
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
                //AutoSlideImage();

                //Gach ngang chu
                java.text.DecimalFormat formatter = new java.text.DecimalFormat("#");
                if (phong.getSale() != 0) {
                    tvGiacu.setPaintFlags(tvGiacu.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvGiacu.setText(formatter.format(phong.getGia()) + "đ/đêm");
                    //Giá
                    tvGiamoi.setText(formatter.format(phong.getSale()) + "đ/đêm");
                } else {
                    tvGiamoi.setVisibility(View.GONE);
                    tvGiacu.setText(formatter.format(phong.getGia()) + "đ/đêm");
                }

                //Tên phòng
                tv_tenphong.setText(phong.getTen_phong());


                //Tiện nghi
                LayDuLieuTienNghi(phong.getId_phong());

                //Mô tả
                tv_mota.setText(phong.getMo_ta_chung());

                //Đánh giá
                tv_danhgia.setText(phong.getDanh_gia_sao() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void LayDuLieuTienNghi(String id_phong) {
        DatabaseReference reference_chi_tiet_tien_nghi = FirebaseDatabase.getInstance().getReference("chi_tiet_tien_nghi");
        DatabaseReference reference_tien_nghi = FirebaseDatabase.getInstance().getReference("tien_nghi");
        reference_chi_tiet_tien_nghi.child(id_phong).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tiennghi[] = {""};
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    chi_tiet_tien_nghi chiTietTienNghi = dataSnapshot.getValue(chi_tiet_tien_nghi.class);
                    //   Log.e("a",""+chiTietTienNghi.getId_tien_nghi());
                    if (chiTietTienNghi.getSo_luong() != 0) {
                        reference_tien_nghi.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    tien_nghi tien_nghi = dataSnapshot.getValue(tien_nghi.class);
                                    if (chiTietTienNghi.getId_tien_nghi().equals(tien_nghi.getId_tien_nghi())) {
                                        //  Log.e("a", "" + tien_nghi.getTen_tien_nghi());
                                        tiennghi[0] += "\u2022  " + chiTietTienNghi.getSo_luong() + " " + tien_nghi.getTen_tien_nghi() + " \n";
                                        tv_tiennghi.setText(tiennghi[0]);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                if (currentItem == phong.getAnh_phong().size() - 1) {
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
        tv_tenphong = findViewById(R.id.tv_tenphong);
        tv_tiennghi = findViewById(R.id.tv_tiennghi);
        tv_danhgia = findViewById(R.id.tv_danhgia);
        tv_mota = findViewById(R.id.tv_mota);
        tvGiamoi = findViewById(R.id.tvGiamoi);
        img_Back = findViewById(R.id.img_Back);
        iv_heart = findViewById(R.id.iv_heart);

    }
}