package com.example.tdc_hotel.Model;

public class chuc_vu {
    String id_chuc_vu;
    String ten_chuc_vu;

    public chuc_vu(String id_chuc_vu, String ten_chuc_vu) {
        this.id_chuc_vu = id_chuc_vu;
        this.ten_chuc_vu = ten_chuc_vu;
    }
    public chuc_vu() {
    }

    public String getId_chuc_vu() {
        return id_chuc_vu;
    }

    public void setId_chuc_vu(String id_chuc_vu) {
        this.id_chuc_vu = id_chuc_vu;
    }

    public String getTen_chuc_vu() {
        return ten_chuc_vu;
    }

    public void setTen_chuc_vu(String ten_chuc_vu) {
        this.ten_chuc_vu = ten_chuc_vu;
    }
}
