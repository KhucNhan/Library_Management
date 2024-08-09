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

import java.net.URL;
import java.util.ResourceBundle;


import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoanSlipController implements Initializable {
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
    private TableView<LoanSlip> tableView;
    @FXML
    private TableColumn<LoanSlip, String> idUserCol;
    @FXML
    private TableColumn<LoanSlip, String> idBookCol;
    @FXML
    private TableColumn<LoanSlip, String> dateCol;
    @FXML
    private TableColumn<LoanSlip, String> returnDateCol;
    @FXML
    private TableColumn<LoanSlip, String> statusCol;

    private ObservableList<LoanSlip> loanSlips;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loanSlips = FXCollections.observableArrayList(
                new LoanSlip("Nhan12345", "b1", "9/8/2024", "10/9/2024", "true"),
                new LoanSlip("Nhan12345", "b2", "9/8/2024", "10/9/2024", "true"),
                new LoanSlip("Nhan12345", "b3","9/8/2024", "10/9/2024", "true"),
                new LoanSlip("Nhan12345", "b4", "9/8/2024", "10/9/2024", "true")
        );
        idUserCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idUser"));
        idBookCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idBook"));
        dateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("date"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("returnDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("status"));
        tableView.setItems(loanSlips);
    }

    @FXML
    public void ChangeStatus() {
        LoanSlip selectedLoanSlip = tableView.getSelectionModel().getSelectedItem();
        if (selectedLoanSlip != null) {
            selectedLoanSlip.setStatus("false");
        }
    }
}