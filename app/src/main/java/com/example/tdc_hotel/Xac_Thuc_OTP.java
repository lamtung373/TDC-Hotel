package com.example.tdc_hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Xac_Thuc_OTP extends AppCompatActivity {
Button btnXacminh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_thuc_otp);
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnXacminh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Xac_Thuc_OTP.this, Main_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setControl() {
        btnXacminh=findViewById(R.id.btnXacminh);
    }
}