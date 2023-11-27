package com.example.tdc_hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Dang_Nhap extends AppCompatActivity {
    Button btnDangNhap;
    EditText edtHoTen, edtSDT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        AutoLogin();

        setControl();
        setEvent();
    }

    private void setEvent() {

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    String soDienThoai = edtSDT.getText().toString();
                    String hoTen = edtHoTen.getText().toString();

                    Intent intent = new Intent(Dang_Nhap.this, Xac_Thuc_OTP.class);
                    intent.putExtra("so_dien_thoai", soDienThoai);
                    intent.putExtra("ho_ten", hoTen);
                    startActivity(intent);
                }
            }
        });
    }

    private void AutoLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(Xac_Thuc_OTP.SharedPreferences, MODE_PRIVATE);
        String sdt_kh = sharedPreferences.getString(Xac_Thuc_OTP.sdt_kh, "");
        if(!sdt_kh.equals("")){
            Intent intent = new Intent(Dang_Nhap.this, Main_Activity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean validateInput() {
        if (edtSDT.getText().toString().trim().isEmpty()) {
            edtSDT.setError("Số điện thoại không được để trống");
            return false;
        }
        if (edtHoTen.getText().toString().trim().isEmpty()) {
            edtHoTen.setError("Họ tên không được để trống");
            return false;
        }
        return true;
    }

    private void setControl() {
        btnDangNhap = findViewById(R.id.btnDangNhap);
        edtSDT = findViewById(R.id.edtSDT_DN);
        edtHoTen = findViewById(R.id.edtHoTen_DN);
    }
}