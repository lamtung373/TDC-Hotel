package com.example.tdc_hotel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tdc_hotel.Model.chi_tiet_tien_nghi;
import com.example.tdc_hotel.Model.dich_vu;
import com.example.tdc_hotel.Model.phong;
import com.example.tdc_hotel.Model.tien_nghi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;

public class Thanh_Toan extends AppCompatActivity {
    ImageView ivBank, iv_back, ivMomopay;
    TextView tvTenPhong, tvNgayNhanPhong, tvNgayTraPhong, tvTienPhong, tvTienNghi, tvDichVu_a, tvDichVu_b, tvTongHD, tvMaHoaDon, tvTienCoc_SS;
    phong phong;
    String ngayNhan, ngayTra, ma_hoa_don;
    public static String SHARED_PRE = "shared_pre";
    ArrayList<dich_vu> dichVuTheoNguoi, dichVuTheoPhong;
    double tienPhong = 0;
    double tong_thanh_toan = 0;

    //TỔNG HOÁ ĐƠN
    double tienCoc = 0;
    RadioButton radTiencoc, radToanbohoadon;
    //momo
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "MoMo Test";
    private String merchantCode = "MOMO";
    private String merchantNameLabel = "TDC-Hotel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        //Payment
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        phong = (phong) getIntent().getSerializableExtra("phong");
        //Lấy dữ liệu từ màn hình trước
        Intent intent = getIntent();


        ngayNhan = intent.getStringExtra("thoi_gian_nhan");
        ngayTra = intent.getStringExtra("thoi_gian_tra");
        dichVuTheoNguoi = (ArrayList<dich_vu>) getIntent().getSerializableExtra("dich_vu_theo_nguoi");
        dichVuTheoPhong = (ArrayList<dich_vu>) getIntent().getSerializableExtra("dich_vu_phong");
        ma_hoa_don = intent.getStringExtra("ma_hoa_don");
        tong_thanh_toan = intent.getDoubleExtra("tong_thanh_toan", 0);
        tienPhong = intent.getDoubleExtra("tien_phong", 0);
        phong = (phong) intent.getSerializableExtra("phong");

        setControl();
        Initialization();
        setEvent();
    }

    private void Initialization() {
        //Đổ dữ liệu lên tv


        //Mã hóa đơn
        tvMaHoaDon.setText("Mã hóa đơn: " + ma_hoa_don);


        //Tên phòng
        tvTenPhong.setText("Phòng: " + phong.getTen_phong());

        //Nhận phòng
        tvNgayNhanPhong.setText(ngayNhan);

        //Trả phòng
        tvNgayTraPhong.setText(ngayTra);

        //Tiền phòng
        tvTienPhong.setText(MessageFormat.format("{0}đ", tienPhong));


        //Tiện nghi phòng
        DatabaseReference reference_chi_tiet_tien_nghi = FirebaseDatabase.getInstance().getReference("chi_tiet_tien_nghi");
        DatabaseReference reference_tien_nghi = FirebaseDatabase.getInstance().getReference("tien_nghi");
        reference_chi_tiet_tien_nghi.child(phong.getId_phong()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tiennghi[] = {""};
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    chi_tiet_tien_nghi chiTietTienNghi = dataSnapshot.getValue(chi_tiet_tien_nghi.class);
                    if (chiTietTienNghi.getSo_luong() != 0) {
                        reference_tien_nghi.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    tien_nghi tien_nghi = dataSnapshot.getValue(tien_nghi.class);
                                    if (chiTietTienNghi.getId_tien_nghi().equals(tien_nghi.getId_tien_nghi())) {
                                        tiennghi[0] += "• " + tien_nghi.getTen_tien_nghi() + " x " + chiTietTienNghi.getSo_luong() + "\n";
                                        tvTienNghi.setText(tiennghi[0]);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        //Hiển thị danh sách dịch vụ
        String dv_a = "", dv_b = "";
        for (dich_vu dv : dichVuTheoNguoi) {
            if (dv.getSo_luong() > 0) {
                double giaDichVu = dv.getGia_dich_vu();
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                String giaDichVuFormatted = decimalFormat.format(giaDichVu);

                // Tạo chuỗi hiển thị dịch vụ theo người
                dv_a += "• " + dv.getTen_dich_vu() + "\n";
                dv_b += giaDichVuFormatted + "đ x " + dv.getSo_luong() + "\n";
            }
        }

        for (dich_vu dv : dichVuTheoPhong) {
            if (dv.getSo_luong() > 0) {
                double giaDichVu = dv.getGia_dich_vu();
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                String giaDichVuFormatted = decimalFormat.format(giaDichVu);

                // Tạo chuỗi hiển thị dịch vụ theo phòng
                dv_a += "• " + dv.getTen_dich_vu() + "\n";
                dv_b += giaDichVuFormatted + "đ \n";
            }
        }
        tvDichVu_a.setText(dv_a);
        tvDichVu_b.setText(dv_b);

        tvTongHD.setText(MessageFormat.format("{0}đ", tong_thanh_toan));
        tienCoc = tienPhong / 2;
        tvTienCoc_SS.setText(MessageFormat.format("{0}đ", tienCoc));
    }

    private void setEvent() {
        ivMomopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radTiencoc.isChecked()) {
                    requestPayment(tienCoc);
                } else if (radToanbohoadon.isChecked()) {
                    requestPayment(tong_thanh_toan);
                }
            }
        });
        ivBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Thanh_Toan.this, Thanh_Toan_Bank.class);
                intent.putExtra("ma_hoa_don", ma_hoa_don);
                if (radTiencoc.isChecked()) {
                    intent.putExtra("so_tien", tienCoc);
                } else if (radToanbohoadon.isChecked()) {
                    intent.putExtra("so_tien", tong_thanh_toan);
                }
                intent.putExtra("id_phong", phong.getId_phong());
                startActivity(intent);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void requestPayment(double so_tien) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", so_tien); //Kiểu integer
        eventValue.put("orderId", ma_hoa_don); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", "ĐH ABC"); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", merchantNameLabel);//gán nhãn
        eventValue.put("fee", fee); //Kiểu integer
        eventValue.put("description", "Thanh toán hóa đơn "+ma_hoa_don); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId", merchantCode + "merchant_billId_" + System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }

    //Get token callback from MoMo app an submit to server side
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    //   tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if (env == null) {
                        env = "app";
                    }

                    if (token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                        Log.e("Thành công", data.getStringExtra("message"));
                        Calendar calendar=Calendar.getInstance();
                        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("hoa_don").child(phong.getId_phong()).child(ma_hoa_don);
                        reference.child("thoi_gian_coc").setValue(dateFormat.format(calendar.getTime()));
                        reference.child("thoi_gian_duyet").setValue(dateFormat.format(calendar.getTime()));
                        if(radTiencoc.isChecked()){
                            reference.child("tien_coc").setValue(tienCoc);
                        } else if (radToanbohoadon.isChecked()) {
                            reference.child("tien_coc").setValue(tong_thanh_toan);
                        }
                        Toast.makeText(this, "Thanh toán thành công!!!", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Thanh_Toan.this, Main_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Log.e("Thành công", "không thành công");
                        //     tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    }
                } else if (data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null ? data.getStringExtra("message") : "Thất bại";
                    Log.e("Thành công", "không thành công");
                    //  tvMessage.setText("message: " + message);
                } else if (data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    //   tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    Log.e("Thành công", "không thành công");
                } else {
                    //TOKEN FAIL
                    //  tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    Log.e("Thành công", "không thành công");
                }
            } else {
                Log.e("Thành công", "không thành công");
                //    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
            }
        } else {
            Log.e("Thành công", "không thành công");
            //   tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
        }
    }

    private void setControl() {
        tvTenPhong = findViewById(R.id.tvTenPhong_SS);
        tvNgayNhanPhong = findViewById(R.id.tvNgayNhanPhong_SS);
        tvNgayTraPhong = findViewById(R.id.tvNgayTraPhong_SS);
        tvTienPhong = findViewById(R.id.tvTienPhong_SS);
        tvTienNghi = findViewById(R.id.tvTienNghi_SS);
        tvDichVu_a = findViewById(R.id.tvDichVu_a_SS);
        tvDichVu_b = findViewById(R.id.tvDichVu_b_SS);
        tvTongHD = findViewById(R.id.tvTongHD_SS);
        tvMaHoaDon = findViewById(R.id.tvMaHoaDon);
        tvTienCoc_SS = findViewById(R.id.tvTienCoc_SS);
        ivBank = findViewById(R.id.ivBank);
        ivMomopay = findViewById(R.id.ivMomopay);
        radTiencoc = findViewById(R.id.radTiencoc);
        radToanbohoadon = findViewById(R.id.radToanbohoadon);
        iv_back = findViewById(R.id.iv_back);
    }
}