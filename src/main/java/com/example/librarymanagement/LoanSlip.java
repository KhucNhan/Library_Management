package com.example.librarymanagement;

import java.util.ArrayList;

public class LoanSlip {
    private String id;
    private ArrayList<Book> books;
    private String date;
    private String returnDate;
    private String status;
    private String BillUser;

    public LoanSlip() {
    }

    public LoanSlip(String id, String BillUser, ArrayList<Book> books, String date, String returnDate, String status) {
        this.id = id;
        this.books = books;
        this.date = date;
        this.returnDate = returnDate;
        this.status = status;
        this.BillUser = BillUser;
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

    public String getBillUser() {
        return BillUser;
    }

    public void setBillUser(String BillUser) {
        this.BillUser = BillUser;
    }
}
