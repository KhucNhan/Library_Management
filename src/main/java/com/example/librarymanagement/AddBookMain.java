package com.example.librarymanagement;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AddBookMain extends Application {
    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader login = new FXMLLoader(LoginMain.class.getResource("AddBook.fxml"));
//        Scene scene = new Scene(login.load(), 720, 480);
//        stage.setTitle("Add more books to the library");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }


    public void start(Stage stage) {
        Book book1 = new Book("b1", "toan", "nhan", "2018", "Subject");
        Book book2 = new Book("b2", "tieng anh", "khanh", "2018", "Subject");
        Book book3 = new Book("b3", "one piece", "oda", "1999", "Animation");
        // Một danh sách
        ObservableList<Book> books = FXCollections.observableArrayList(book1, book2, book3);

        // Tạo một ListView
        ListView<Book> listView = new ListView<Book>(books);


        // Cho phép lựa chọn nhiều dòng trên danh sách.
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        // Lựa chọn phần tử Index = 1,2
        listView.getSelectionModel().selectIndices(1, 2);

        // Focus
        listView.getFocusModel().focus(1);

        StackPane root = new StackPane();
        root.getChildren().add(listView);

        stage.setTitle("Home");

        Scene scene = new Scene(root, 350, 200);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}