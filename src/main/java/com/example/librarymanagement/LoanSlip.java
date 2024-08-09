package com.example.librarymanagement;

import javafx.scene.Parent;

import java.util.ArrayList;

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
}