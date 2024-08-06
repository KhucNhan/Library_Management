package com.example.librarymanagement;

public class BookMain {
    public static void main(String[] args) {
        BookController admin = new BookController();
        admin.add(new Book("1","Doraemon", "Fuji", "2000", "Animation"));
        admin.add(new Book("2","One Piece", "Nhan", "1999", "Animation"));
        admin.display();
    }
}
