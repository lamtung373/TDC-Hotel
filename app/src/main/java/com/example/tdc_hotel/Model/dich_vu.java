package com.example.tdc_hotel.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class dich_vu implements Serializable {
    int  gia_dich_vu;
    String id_loai_dich_vu,id_dich_vu,ten_dich_vu;
    String anh_dich_vu;
    int so_luong;

    public int getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(int so_luong) {
        this.so_luong = so_luong;
    }

    public dich_vu(String id_loai_dich_vu, int gia_dich_vu, String id_dich_vu, String ten_dich_vu, String anh_dich_vu) {
        this.id_loai_dich_vu = id_loai_dich_vu;
        this.gia_dich_vu = gia_dich_vu;
        this.id_dich_vu = id_dich_vu;
        this.ten_dich_vu = ten_dich_vu;
        this.anh_dich_vu = anh_dich_vu;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("gia_dich_vu", gia_dich_vu);
        result.put("ten_dich_vu", ten_dich_vu);
        result.put("anh_dich_vu", anh_dich_vu);
        return result;
    }
    public dich_vu() {
    }

    public String getId_loai_dich_vu() {
        return id_loai_dich_vu;
    }

    public void setId_loai_dich_vu(String id_loai_dich_vu) {
        this.id_loai_dich_vu = id_loai_dich_vu;
    }

    public String getAnh_dich_vu() {
        return anh_dich_vu;
    }

    public void setAnh_dich_vu(String anh_dich_vu) {
        this.anh_dich_vu = anh_dich_vu;
    }

    public String getId_dich_vu() {
        return id_dich_vu;
    }

    public void setId_dich_vu(String id_dich_vu) {
        this.id_dich_vu = id_dich_vu;
    }

    public int getGia_dich_vu() {
        return gia_dich_vu;
    }

    public void setGia_dich_vu(int gia_dich_vu) {
        this.gia_dich_vu = gia_dich_vu;
    }

    public String getTen_dich_vu() {
        return ten_dich_vu;
    }

    public void setTen_dich_vu(String ten_dich_vu) {
        this.ten_dich_vu = ten_dich_vu;
    }
}
