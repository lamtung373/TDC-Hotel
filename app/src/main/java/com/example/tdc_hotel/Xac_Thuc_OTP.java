package com.example.tdc_hotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tdc_hotel.Model.khach_hang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Xac_Thuc_OTP extends AppCompatActivity {
    Button btnXacminh;
    ImageButton btnQuayLai;
    EditText edtOTP;
    String soDienThoai, hoTen;
    LinearLayout progressBar_OTP;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    public static String SharedPreferences = "SHARE_PRE";
    public static String sdt_kh = "sdt_kh";
    public static String name_kh = "name_kh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_thuc_otp);

        //Lấy dữ liệu từ màn hình trước
        Intent intent = getIntent();
        soDienThoai = intent.getStringExtra("so_dien_thoai");
        if (soDienThoai.startsWith("0")) {
            soDienThoai = soDienThoai.substring(1);
        }

        hoTen = intent.getStringExtra("ho_ten");

        mAuth = FirebaseAuth.getInstance();

        setControl();
        setEvent();
    }

    private void setEvent() {
        sendVerificationCode(soDienThoai);

        btnXacminh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    String code = edtOTP.getText().toString().trim();
                    if (!code.isEmpty()) {
                        verifyVerificationCode(code);
                    }
                }
            }
        });

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendVerificationCode(String soDienThoai) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + soDienThoai)       // Số điện thoại để gửi mã
                        .setTimeout(60L, TimeUnit.SECONDS) // Thời gian timeout
                        .setActivity(this)                 // Activity hiện tại
                        .setCallbacks(mCallbacks)          // Callbacks khi có sự kiện
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                }
                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    mVerificationId = verificationId; // Lưu ID xác minh
                    btnXacminh.setVisibility(View.VISIBLE);
                    progressBar_OTP.setVisibility(View.GONE);
                    // Thông báo cho người dùng rằng mã đã được gửi
                    Toast.makeText(Xac_Thuc_OTP.this, "Đã gửi mã OTP đến số điện thoại của bạn!", Toast.LENGTH_SHORT).show();
                }

            };

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            kiemTraVaDangNhapNguoiDung();

                            // Khởi tạo SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferences, MODE_PRIVATE);
                            // Lấy Editor để chỉnh sửa
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            // Lưu một chuỗi
                            editor.putString(sdt_kh, "+84" + soDienThoai);
                            // Áp dụng thay đổi
                            editor.apply();
                        } else {
                            // Thông báo lỗi xác minh
                            Toast.makeText(Xac_Thuc_OTP.this, "Mã OTP bạn nhập không đúng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void kiemTraVaDangNhapNguoiDung() {
        // Khởi tạo SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferences, MODE_PRIVATE);
        // Lấy Editor để chỉnh sửa
        SharedPreferences.Editor editor = sharedPreferences.edit();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("khach_hang");
        databaseReference.child("+84" + soDienThoai).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    khach_hang kh = new khach_hang();
                    kh.setSo_dien_thoai("+84" + soDienThoai);
                    kh.setTen(hoTen);
                    // Nếu chưa có, thêm người dùng mới
                    databaseReference.child("+84" + soDienThoai).setValue(kh);

                    // Lưu một chuỗi
                    editor.putString(name_kh, hoTen);
                } else {
                    khach_hang kh = snapshot.getValue(khach_hang.class);
                    // Lưu một chuỗi
                    editor.putString(name_kh, kh.getTen());
                }
                // Áp dụng thay đổi
                editor.apply();
                // Chuyển sang màn hình chính
                Intent intent = new Intent(Xac_Thuc_OTP.this, Main_Activity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
    }

    private boolean validateInput() {
        if (edtOTP.getText().toString().trim().isEmpty()) {
            edtOTP.setError("Vui lòng nhập mã OTP");
            return false;
        }
        return true;
    }

    private void setControl() {
        btnXacminh = findViewById(R.id.btnXacminh);
        edtOTP = findViewById(R.id.edtOTP);
        progressBar_OTP = findViewById(R.id.progressBar_OTP);
        btnQuayLai = findViewById(R.id.btnQuayLai_OTP);
    }
}