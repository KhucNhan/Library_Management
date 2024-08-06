package com.example.librarymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class BookController implements Initializable {
    private ObservableList<Book> Books;

    public Book getBook(String text) {
        for (Book book : Books) {
            if (((book.getId()).equalsIgnoreCase((text)) || (book.getTitle().equalsIgnoreCase(text)))) {
                return book;
            }
        }
        return null;
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void goToLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080, 720);
        stage.setScene(scene);
        stage.show();
    }

    public void goToAddBookScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AddBook.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080, 720);
        stage.setScene(scene);
        stage.show();
    }

    public void goToBookScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ListView.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080, 720);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    TextField id;
    @FXML
    TextField title;
    @FXML
    TextField author;
    @FXML
    TextField releaseYear;
    @FXML
    TextField genre;
    @FXML
    TextField status;

    public void edit() {
        Book book = getBook(id.getText());
        if (book != null) {
            book.setId(id.getText());
            book.setTitle(title.getText());
            book.setAuthor(author.getText());
            book.setReleaseYear(releaseYear.getText());
            book.setGenre(genre.getText());
            book.setStatus(status.getText());
        }
    }

    @FXML
    TextField idAdd;
    @FXML
    TextField titleAdd;
    @FXML
    TextField authorAdd;
    @FXML
    TextField releaseYearAdd;
    @FXML
    TextField genreAdd;
    @FXML
    TextField statusAdd;

    public void add() {
        Book book = getBook(idAdd.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (book == null) {
            Books.add(new Book(idAdd.getText(), titleAdd.getText(), authorAdd.getText(), releaseYearAdd.getText(), genreAdd.getText(), statusAdd.getText()));
        } else {
            alert.setContentText("This id is exist. Try again please.");
            alert.show();
        }
    }

    @FXML
    private TableView<Book> table;
    @FXML
    private TableColumn<Book, String> idCol;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> releaseYearCol;
    @FXML
    private TableColumn<Book, String> genreCol;
    @FXML
    private TableColumn<Book, String> statusCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Books = FXCollections.observableArrayList(
                new Book("b1", "toan", "nhan", "2018", "Subject", "true"),
                new Book("b2", "tieng anh", "khanh", "2018", "Subject", "true"),
                new Book("b3", "one piece", "oda", "1999", "Animation", "true"),
                new Book("b4", "doraemon", "fuji", "2000", "Animation", "true")
        );
        idCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Author"));
        releaseYearCol.setCellValueFactory(new PropertyValueFactory<Book, String>("ReleaseYear"));
        genreCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Genre"));
        statusCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Status"));
        table.setItems(Books);
    }
}