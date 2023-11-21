package com.example.tdc_hotel.Model;

public class yeu_thich {
    String so_dien_thoai;

    public String getSo_dien_thoai() {
        return so_dien_thoai;
    }

    public void setSo_dien_thoai(String so_dien_thoai) {
        this.so_dien_thoai = so_dien_thoai;
    }

    public String getId_phong() {
        return id_phong;
    }

    public void setId_phong(String id_phong) {
        this.id_phong = id_phong;
    }

    public yeu_thich(String so_dien_thoai, String id_phong) {
        this.so_dien_thoai = so_dien_thoai;
        this.id_phong = id_phong;
    }

    String id_phong;
}
