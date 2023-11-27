// Fragment_TaiKhoan.java
package com.example.tdc_hotel.Fragment_Menu;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tdc_hotel.Dang_Nhap;
import com.example.tdc_hotel.Model.khach_hang;
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
    private boolean isNameChanged = false;

    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Xac_Thuc_OTP.SHARED_PRE, MODE_PRIVATE);
    String sdt_kh=sharedPreferences.getString(Xac_Thuc_OTP.sdt_kh,"");
    private View view; // Thêm view như biến toàn cục



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__tai_khoan, container, false);
        setControl(view);
        Initialization();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewName(); // Gọi phương thức xử lý lưu tên mới
            }
        });
        btnSave.setVisibility(View.GONE); // Ẩn nút lưu khi chưa làm gì

        // Hiển thị nút lưu khi người dùng nhấn vào edtTen
        edtTen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    btnSave.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    private void Initialization() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

    }


    private void saveNewName() {
        // Lấy tên mới từ EditText
        String newName = edtTen.getText().toString();

        // Kiểm tra xem newName có rỗng không và đã thay đổi chưa
        if (!newName.isEmpty() && isNameChanged) {
            // Cập nhật giá trị mới lên Firebase
            //sdt_kh.child("1").child("ten").setValue(newName);

            // Hiển thị thông báo lưu thành công
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Đã lưu tên mới: " + newName);
            builder.setPositiveButton("OK", null);
            builder.show();

            // Đặt lại cờ sau khi lưu
            isNameChanged = false;
        } else {
            // Thông báo lưu thất bại (nếu cần)
        }
    }


    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận đăng xuất");
        builder.setMessage("Bạn có chắc muốn đăng xuất?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý đăng xuất ở đây
                // Ví dụ: Chuyển người dùng đến màn hình đăng nhập
                Intent intent = new Intent(getContext(), Dang_Nhap.class);
                startActivity(intent);
                getActivity().finish(); // Đóng Fragment hoặc Activity hiện tại
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng dialog nếu người dùng không muốn đăng xuất
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void setControl(View view) {
        btnLogout = view.findViewById(R.id.btnDangXuat);
        edtTen = view.findViewById(R.id.edt_Ten); // Khởi tạo EditText
        btnSave = view.findViewById(R.id.btnLuu); // Thêm dòng này để khởi tạo imgbSave

    }
}