package com.example.librarymanagement;

import java.util.ArrayList;

public class LoanSlip {
    private String id;
    private ArrayList<Book> books;
    private  String date;
    private String returnDate;
    private String status;

    public LoanSlip(String id, String date, String returnDate, String status) {
        this.id = id;
        this.date = date;
        this.returnDate = returnDate;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
