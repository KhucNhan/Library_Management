package com.example.librarymanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader login = new FXMLLoader(LoginMain.class.getResource("Login.fxml"));
        Scene scene = new Scene(login.load(), 720, 480);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public void testApp () {
        // thay Duy
    }

    public static void main(String[] args) {
        launch();
    }
}