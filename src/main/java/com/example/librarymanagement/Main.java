package com.example.librarymanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Controller master = new Controller();
        master.add(new Admin("Nhan12345","khucnhan","nhan2005"));
        master.add(new Admin("Khanh23456","baokhanh","khanh2005"));
        master.add(new Admin("Dam34567","vandam","dam2005"));
        master.add(new User("Phuong45678","nguyenphuong","phuong2005"));
        master.setRole("nguyenphuong", "admin");
        master.display();
        launch();
    }
}