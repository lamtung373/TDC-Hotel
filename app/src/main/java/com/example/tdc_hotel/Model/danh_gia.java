package com.example.tdc_hotel.Model;

public class danh_gia {
    double  so_sao;
    String id_danh_gia;
    String id_phong;
    String id_hoadon;
    String so_dien_thoai;
    String chi_tiet_danh_gia;
    String thoi_gian;

    public String getTen_khach_hang() {
        return ten_khach_hang;
    }

    public void setTen_khach_hang(String ten_khach_hang) {
        this.ten_khach_hang = ten_khach_hang;
    }

    String ten_khach_hang;

    @Override
    public String toString() {
        return "danh_gia{" +
                "so_sao=" + so_sao +
                ", id_danh_gia='" + id_danh_gia + '\'' +
                ", id_phong='" + id_phong + '\'' +
                ", id_hoadon='" + id_hoadon + '\'' +
                ", so_dien_thoai='" + so_dien_thoai + '\'' +
                ", chi_tiet_danh_gia='" + chi_tiet_danh_gia + '\'' +
                ", thoi_gian='" + thoi_gian + '\'' +
                '}';
    }

    public danh_gia(double so_sao, String id_danh_gia, String id_phong, String id_hoadon, String so_dien_thoai, String chi_tiet_danh_gia, String thoi_gian) {
        this.so_sao = so_sao;
        this.id_danh_gia = id_danh_gia;
        this.id_phong = id_phong;
        this.id_hoadon = id_hoadon;
        this.so_dien_thoai = so_dien_thoai;
        this.chi_tiet_danh_gia = chi_tiet_danh_gia;
        this.thoi_gian = thoi_gian;
    }

    public String getId_hoadon() {
        return id_hoadon;
    }

    public void setId_hoadon(String id_hoadon) {
        this.id_hoadon = id_hoadon;
    }

    public String getId_phong() {
        return id_phong;
    }

    public void setId_phong(String id_phong) {
        this.id_phong = id_phong;
    }


    public String getThoi_gian() {
        return thoi_gian;
    }

    public void setThoi_gian(String thoi_gian) {
        this.thoi_gian = thoi_gian;
    }

    public danh_gia() {
    }

    public String getId_danh_gia() {
        return id_danh_gia;
    }

    public void setId_danh_gia(String id_danh_gia) {
        this.id_danh_gia = id_danh_gia;
    }

    public double getSo_sao() {
        return so_sao;
    }

    public void setSo_sao(double so_sao) {
        this.so_sao = so_sao;
    }

    public String getSo_dien_thoai() {
        return so_dien_thoai;
    }

    public void setSo_dien_thoai(String so_dien_thoai) {
        this.so_dien_thoai = so_dien_thoai;
    }

    public String getChi_tiet_danh_gia() {
        return chi_tiet_danh_gia;
    }

    public void setChi_tiet_danh_gia(String chi_tiet_danh_gia) {
        this.chi_tiet_danh_gia = chi_tiet_danh_gia;
    }
}
