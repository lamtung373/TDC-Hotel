package com.example.tdc_hotel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Thanh_Toan_Bank extends AppCompatActivity {
    ImageView ivCopy, ivCopy_nd,ivback;
    TextView vuilong, tv_sotien, tvNd, tvStk;
    Button btn_thanhtoan;
    String ma_hoa_don = "";
    String id_phong = "";
    ProgressBar pb;
    double so_tien = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan_bank);

        ma_hoa_don = getIntent().getStringExtra("ma_hoa_don");
        id_phong = getIntent().getStringExtra("id_phong");
        so_tien = getIntent().getDoubleExtra("so_tien", 0);
        setControl();
        setEvent();
    }

    private void setEvent() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        vuilong.setText("Vui lòng thanh toán số tiền " + decimalFormat.format(so_tien) + "đ qua tài khoản của chúng tôi qua mã QR cùng nội dung “" + ma_hoa_don + "”");
        tv_sotien.setText(decimalFormat.format(so_tien) + "đ");
        tvNd.setText(ma_hoa_don);
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
        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                btn_thanhtoan.setVisibility(View.GONE);
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("hoa_don").child(id_phong).child(ma_hoa_don);
                        reference.child("thoi_gian_coc").setValue(dateFormat.format(calendar.getTime()), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(Thanh_Toan_Bank.this, "Thanh toán thành công!!!", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                        btn_thanhtoan.setVisibility(View.VISIBLE);
                        Intent intent=new Intent(Thanh_Toan_Bank.this, Main_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setControl() {
        ivCopy = findViewById(R.id.ivCopy);
        ivCopy_nd = findViewById(R.id.ivCopy_nd);
        tvStk = findViewById(R.id.tvStk);
        tvNd = findViewById(R.id.tvNd);
        btn_thanhtoan = findViewById(R.id.btn_thanhtoan);
        pb = findViewById(R.id.pb);
        vuilong = findViewById(R.id.vuilong);
        tv_sotien = findViewById(R.id.tv_sotien);
        ivback = findViewById(R.id.ivback);
    }
}