// Fragment_TaiKhoan.java
package com.example.tdc_hotel.Fragment_Menu;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tdc_hotel.Dang_Nhap;
import com.example.tdc_hotel.R;
import com.example.tdc_hotel.Xac_Thuc_OTP;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_TaiKhoan extends Fragment {
    Button btnLogout;
    Button btnSave;
    EditText edtTen;
    TextView tv_sdt;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__tai_khoan, container, false);
        setControl(view);
        Initialization();

        btnSave.setVisibility(View.GONE);

        // Thêm sự kiện lắng nghe để hiển thị btnSave khi edtTen được chọn
        edtTen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Khi edtTen được chọn, hiển thị btnSave và ẩn btnDangXuat
                    btnSave.setVisibility(View.VISIBLE);
                    btnLogout.setVisibility(View.GONE);
                } else {
                    // Khi edtTen mất focus, ẩn btnSave và hiển thị btnDangXuat nếu cần
                    btnSave.setVisibility(View.GONE);
                    btnLogout.setVisibility(View.VISIBLE);
                }
            }
        });

        // Thêm sự kiện lắng nghe cho btnSave khi được click
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức để lưu tên mới vào Firebase và thực hiện các thay đổi cần thiết
                saveNewName();
            }
        });
        return view;
    }

    private void Initialization() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Xac_Thuc_OTP.SharedPreferences, MODE_PRIVATE);
        String sdt_kh = sharedPreferences.getString(Xac_Thuc_OTP.sdt_kh, "");
        if (!sdt_kh.isEmpty()) {
            tv_sdt.setText(sdt_kh);

            // Truy vấn tên từ Firebase và hiển thị lên edtTen
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("khach_hang").child(sdt_kh);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Lấy giá trị tên từ dataSnapshot
                        String ten = dataSnapshot.child("ten").getValue(String.class);
                        // Hiển thị giá trị tên lên edtTen
                        edtTen.setText(ten);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý khi truy vấn bị hủy
                }
            });
        }

        // Thêm sự kiện lắng nghe cho btnLogout khi được click
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setTitle("Đăng xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Xac_Thuc_OTP.SharedPreferences, getActivity().MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                getActivity().finish();
                                Intent intent = new Intent(getActivity(), Dang_Nhap.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();

            }
        });
    }

    private void setControl(View view) {
        btnLogout = view.findViewById(R.id.btnDangXuat);
        edtTen = view.findViewById(R.id.edt_Ten);
        tv_sdt = view.findViewById(R.id.tv_SDT);
        btnSave = view.findViewById(R.id.btnLuu);
    }

    private void saveNewName() {
        // Lấy tên mới từ EditText
        String newName = edtTen.getText().toString();

        // Kiểm tra xem newName có rỗng không và đã thay đổi chưa
        if (!newName.isEmpty()) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Xac_Thuc_OTP.SharedPreferences, MODE_PRIVATE);
            String sdt_kh = sharedPreferences.getString(Xac_Thuc_OTP.sdt_kh, "");

            if (!sdt_kh.isEmpty()) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("khach_hang").child(sdt_kh);

                // Cập nhật giá trị mới lên Firebase
                databaseReference.child("ten").setValue(newName);

                // Hiển thị thông báo lưu thành công
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Đã lưu tên mới: " + newName);
                builder.setPositiveButton("OK", null);
                builder.show();

                // Ẩn btnSave và hiện btnDangXuat
                btnSave.setVisibility(View.GONE);
                btnLogout.setVisibility(View.VISIBLE);
            }
        } else {
            // Thông báo lưu thất bại (nếu cần)
        }
    }

}