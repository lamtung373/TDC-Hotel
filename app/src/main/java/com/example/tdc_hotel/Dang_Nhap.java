package com.example.tdc_hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dang_Nhap extends AppCompatActivity {
    Button btndn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        setControl();
        setEvent();
    }

    private void setEvent() {
        btndn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dang_Nhap.this, Xac_Thuc_OTP.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setControl() {
        btndn = findViewById(R.id.btndn);
    }
}