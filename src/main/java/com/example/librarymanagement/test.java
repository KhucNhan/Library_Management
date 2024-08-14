package com.example.librarymanagement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class test extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox vbox = new VBox();
        Button buttonToRemove = new Button("Remove Me");
        Button removeButton = new Button("Click to Remove");

        removeButton.setOnAction(event -> {
            // Xóa nút buttonToRemove khỏi VBox
            vbox.getChildren().remove(buttonToRemove);
        });

        vbox.getChildren().addAll(buttonToRemove, removeButton);

        Scene scene = new Scene(vbox, 300, 250);
        primaryStage.setTitle("Remove Button Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
