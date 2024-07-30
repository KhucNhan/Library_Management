package com.example.librarymanagement;

import java.util.Arrays;
import java.util.Objects;

public class BookController {
    private Book[] Books = new Book[0];

    public boolean add(Book Book) {
        if (isExist(Book)) {
            return false;
        }
        Books = Arrays.copyOf(Books, Books.length + 1);
        Books[Books.length - 1] = Book;
        return true;
    }

    public boolean isExist(Book Book) {
        for (com.example.librarymanagement.Book book : Books) {
            return book == Book;
        }
        return false;
    }

    public boolean isExist(String id) {
        for (Book book : Books) {
            return Objects.equals(book.getId(), id);
        }
        return false;
    }

    public Book getBook(String text) {
        for (Book book : Books) {
            if (((book.getId()).equalsIgnoreCase((text)) || (book.getTitle().equalsIgnoreCase(text)))) {
                return book;
            }
        }
        return null;
    }

    public boolean remove(String id) {
        for (int i = 0; i < Books.length; i++) {
            if (Objects.equals((Books[i].getId()).toLowerCase(), id.toLowerCase())) {
                remove(i);
                break;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean remove(int index) {
        if (index > Books.length) {
            return false;
        }
        for (int i = index; i < Books.length; i++) {
            Books[i] = Books[i + 1];
        }
        return true;
    }

    public boolean isEmpty() {
        return Books.length == 0;
    }

    public void display() {
        for (Book book : Books) {
            System.out.println(book.toString());
        }
    }

    public int getBookAmount() {
        return Books.length;
    }
}