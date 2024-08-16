package com.example.librarymanagement;

import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.Objects;

public class LoanSlip {
    private String idUser;
    private String idBook;
    private String date;
    private String returnDate;
    private String status;

    public LoanSlip() {
    }

    public LoanSlip(String idUser, String idBook, String date, String returnDate, String status) {
        this.idUser = idUser;
        this.idBook = idBook;
        this.date = date;
        this.returnDate = returnDate;
        this.status = status;
    }

    public LoanSlip(String part, String part1, String part2, String part3, String part4, String part5, String part6) {
    }
    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdBook() {
        return idBook;
    }

    public void setIdBook(String idBook) {
        this.idBook = idBook;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LoanSlip{" +
                "idUser='" + idUser + '\'' +
                ", idBook='" + idBook + '\'' +
                ", date='" + date + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanSlip loanSlip = (LoanSlip) o;
        return Objects.equals(idUser, loanSlip.idUser) &&
                Objects.equals(idBook, loanSlip.idBook) &&
                Objects.equals(date, loanSlip.date) &&
                Objects.equals(returnDate, loanSlip.returnDate) &&
                Objects.equals(status, loanSlip.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idBook, date, returnDate, status);
    }
}