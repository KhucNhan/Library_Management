package com.example.librarymanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class BookController {
    Book book1 = new Book("b1", "toan", "nhan", "2018","Subject");
    Book book2 = new Book("b2", "tieng anh", "khanh", "2018", "Subject");
    Book book3 = new Book("b3", "one piece", "oda", "1999", "Animation");
    Book book4 = new Book("b4", "doraemon", "fuji", "2000", "Animation");
    private Book[] Books = {book1, book2, book3, book4};

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

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void goToLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root,720,480);
        stage.setScene(scene);
        stage.show();
    }
}