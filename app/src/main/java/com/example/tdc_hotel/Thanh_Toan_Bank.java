package com.example.tdc_hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Thanh_Toan_Bank extends AppCompatActivity {
ImageView ivCopy,ivCopy_nd;
TextView tvStk,tvNd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan_bank);
        setControl();
        setEvent();
    }

    private void setEvent() {
        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToCopy = tvStk.getText().toString(); // Lấy nội dung của TextView
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", textToCopy);
                clipboardManager.setPrimaryClip(clipData); // Sao chép nội dung vào clipboard
                Toast.makeText(getApplicationContext(), "Đã sao chép số tài khoản", Toast.LENGTH_SHORT).show();
            }
        });
        ivCopy_nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToCopy = tvNd.getText().toString(); // Lấy nội dung của TextView
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", textToCopy);
                clipboardManager.setPrimaryClip(clipData); // Sao chép nội dung vào clipboard
                Toast.makeText(getApplicationContext(), "Đã sao chép nội dung chuyển khoản", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setControl() {
        ivCopy=findViewById(R.id.ivCopy);
        ivCopy_nd=findViewById(R.id.ivCopy_nd);
        tvStk=findViewById(R.id.tvStk);
        tvNd=findViewById(R.id.tvNd);
    }
}