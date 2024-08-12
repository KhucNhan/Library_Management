package com.example.librarymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

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
    @FXML
    private TableColumn<LoanSlip, Void> actionCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<LoanSlip> loanSlips = FXCollections.observableArrayList(
                new LoanSlip("Nhan12345", "b1", "9/8/2024", "10/9/2024", "On loan"),
                new LoanSlip("Nhan12345", "b2", "9/8/2024", "10/9/2024", "On loan"),
                new LoanSlip("Nhan12345", "b3", "9/8/2024", "10/9/2024", "On loan"),
                new LoanSlip("Nhan12345", "b4", "9/8/2024", "10/9/2024", "On loan")
        );
//        idUserCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idUser"));
        idBookCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idBook"));
        dateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("date"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("returnDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("status"));
        actionCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, Void>(" "));
        Callback<TableColumn<LoanSlip, Void>, TableCell<LoanSlip, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<LoanSlip, Void> call(TableColumn<LoanSlip, Void> loanSlipVoidTableColumn) {
                final TableCell<LoanSlip, Void> cell = new TableCell<>() {

                    private final Button changeStatusButton = new Button("Change status");

                    {
                        changeStatusButton.setOnAction((ActionEvent event) -> {
                            LoanSlip loanSlip = getTableView().getItems().get(getIndex());
                            if (loanSlip.getStatus().equalsIgnoreCase("On loan")) {
                                loanSlip.setStatus("Paid");
                                changeStatusButton.setText("On loan");
                                tableView.refresh();
                            } else {
                                loanSlip.setStatus("On loan");
                                changeStatusButton.setText("Paid");
                                tableView.refresh();
                            }
                        });
                        changeStatusButton.setPrefWidth(150);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hBox = new HBox(changeStatusButton);
                            hBox.setSpacing(10);
                            HBox.setMargin(changeStatusButton, new Insets(0, 5, 0, 5)); // Thiết lập margin cho nút Edit
                            setGraphic(hBox);
                        }
                    }
                };
                return cell;
            }
        };
        actionCol.setCellFactory(cellFactory);
        tableView.setItems(loanSlips);
    }
}