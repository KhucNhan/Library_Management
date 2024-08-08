package com.example.librarymanagement;

public class LoanSlip {
    private String maPhieuMuon;
    private String maSach;
    private String maDocGia;
    private String ngayMuon;
    private String ngayTra;

    // Constructors, getters, and setters
    public LoanSlip(String maPhieuMuon, String maSach, String maDocGia, String ngayMuon, String ngayTra) {
        this.maPhieuMuon = maPhieuMuon;
        this.maSach = maSach;
        this.maDocGia = maDocGia;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
    }

    public String getMaPhieuMuon() {
        return maPhieuMuon;
    }

    public void setMaPhieuMuon(String maPhieuMuon) {
        this.maPhieuMuon = maPhieuMuon;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getMaDocGia() {
        return maDocGia;
    }

    public void setMaDocGia(String maDocGia) {
        this.maDocGia = maDocGia;
    }

    public String getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(String ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public String getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(String ngayTra) {
        this.ngayTra = ngayTra;
    }
}
