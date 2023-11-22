package com.example.tdc_hotel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.tdc_hotel.Model.danh_gia;
import com.example.tdc_hotel.Model.hoa_don;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Danh_Gia extends AppCompatActivity {

    ImageView ivback;
    RatingBar rtDanhGia;
    EditText edtComment;
    Button btnChon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia);
        setControl();
        setEvent();
    }

    private void setEvent() {

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                hoa_don data = (hoa_don) intent.getSerializableExtra("hoaDon");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("danh_gia").child(data.getId_phong());
                String id_danh_gia = ref.push().getKey();

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String strDate = formatter.format(date);
                danh_gia danhGia = new danh_gia(rtDanhGia.getRating(),id_danh_gia,data.getId_phong(),data.getId_hoa_don(),data.getSo_dien_thoai(),edtComment.getText().toString(),strDate);
                ref.child(id_danh_gia).setValue(danhGia, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(Danh_Gia.this, "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
                        SuaSoSaoPhong();
                        finish();
                    }
                });
            }
        });
    }

    private void setControl() {
        ivback = findViewById(R.id.ivback);
        rtDanhGia = findViewById(R.id.rtDanhGia);
        edtComment = findViewById(R.id.edtComment);
        btnChon = findViewById(R.id.btnChon);
    }
    private void SuaSoSaoPhong()
    {
        Intent intent = getIntent();
        hoa_don data = (hoa_don) intent.getSerializableExtra("hoaDon");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("danh_gia").child(data.getId_phong());
        DatabaseReference refPhong = FirebaseDatabase.getInstance().getReference("phong");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long soLuong = snapshot.getChildrenCount();
                double danhGia = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    danh_gia danh_gia = dataSnapshot.getValue(danh_gia.class);
                    danhGia+=danh_gia.getSo_sao();
                }
                danhGia = (double) Math.round(danhGia / soLuong * 10) / 10;
                refPhong.child(data.getId_phong()).child("danh_gia_sao").setValue(danhGia, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}