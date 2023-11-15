package com.example.tdc_hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Thanh_Toan extends AppCompatActivity {
ImageView ivBank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        setControl();
        setEvent();
    }

    private void setEvent() {
        ivBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Thanh_Toan.this, Thanh_Toan_Bank.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        ivBank=findViewById(R.id.ivBank);
    }
}