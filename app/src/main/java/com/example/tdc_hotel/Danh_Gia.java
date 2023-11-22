package com.example.tdc_hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cham_cong");
                String id_chamCong = ref.push().getKey();
            }
        });
    }

    private void setControl() {
        ivback = findViewById(R.id.ivback);
        rtDanhGia = findViewById(R.id.rtDanhGia);
        edtComment = findViewById(R.id.edtComment);
        btnChon = findViewById(R.id.btnChon);
    }
}