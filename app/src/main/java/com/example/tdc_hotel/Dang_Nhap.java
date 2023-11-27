package com.example.tdc_hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Dang_Nhap extends AppCompatActivity {
    Button btnDangNhap;
    EditText edtHoTen, edtSDT;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        databaseReference = FirebaseDatabase.getInstance().getReference("khach_hang");
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String soDienThoai = edtSDT.getText().toString();
                String hoTen = edtHoTen.getText().toString();

                Intent intent = new Intent(Dang_Nhap.this, Xac_Thuc_OTP.class);
                intent.putExtra("so_dien_thoai", soDienThoai);
                intent.putExtra("ho_ten", hoTen);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setControl() {
        btnDangNhap = findViewById(R.id.btnDangNhap);
        edtSDT = findViewById(R.id.edtSDT_DN);
        edtHoTen = findViewById(R.id.edtHoTen_DN);
    }
}