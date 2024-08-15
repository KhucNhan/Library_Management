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

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;


import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoanSlipController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    ObservableList<LoanSlip> LoanSlip = FXCollections.observableArrayList();

    private void loadLoanSlipFromFile() {
        File file = new File("LoanSlip.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    LoanSlip loanSlip = new LoanSlip(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    LoanSlip.add(loanSlip);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showError("Invalid data format, " + "There was an error reading the product data.");
        }
    }


    private void saveProductsToFile() {
        // Xác định đường dẫn file trong thư mục dự án hoặc một thư mục cụ thể
        File file = new File("LoanSlip.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (LoanSlip loanSlip : LoanSlip) {
                // Tạo chuỗi dữ liệu cho từng sách
                String line = String.join(",",
                        loanSlip.getIdUser(),
                        loanSlip.getIdBook(),
                        loanSlip.getDate(),
                        loanSlip.getReturnDate(),
                        loanSlip.getStatus()
                );
                writer.write(line);
                writer.newLine(); // Thêm dòng mới sau mỗi sản phẩm
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error saving data: Could not save LoanSlip data to file.");
        }
    }

    public void clearInFile() {
        File file = new File("LoanSlip.txt");
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void goToLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080, 720);
        stage.setScene(scene);
        stage.show();
    }

    public void goToBookScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminView.fxml"));
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoanSlip = FXCollections.observableArrayList();
        loadLoanSlipFromFile();
        tableView.setItems(LoanSlip);
//        idUserCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idUser"));
        idBookCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idBook"));
        dateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("date"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("returnDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("status"));
        Callback<TableColumn<LoanSlip, String>, TableCell<LoanSlip, String>> cellFactory = new Callback<>() {
            @Override
            public TableCell<LoanSlip, String> call(TableColumn<LoanSlip, String> loanSlipTableColumn) {
                final TableCell<LoanSlip, String> cell = new TableCell<>() {

                    private final Button changeStatusButton = new Button();

                    {
                        changeStatusButton.setOnAction((ActionEvent event) -> {
                            LoanSlip loanSlip = getTableView().getItems().get(getIndex());
                            if (loanSlip.getStatus().equalsIgnoreCase("On loan")) {
                                loanSlip.setStatus("Paid");
                                save();
                                changeStatusButton.setText("On loan");
                                tableView.refresh();
                            } else {
                                loanSlip.setStatus("On loan");
                                save();
                                changeStatusButton.setText("Paid");
                                tableView.refresh();
                            }
                        });
                        changeStatusButton.setPrefWidth(150);
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            LoanSlip loanSlip = getTableView().getItems().get(getIndex());
                            changeStatusButton.setText(loanSlip.getStatus());
                            HBox hBox = new HBox(changeStatusButton);
                            hBox.setSpacing(10);
                            HBox.setMargin(changeStatusButton, new Insets(0, 5, 0, 5)); // Thiết lập margin cho nút Edit
                            setGraphic(hBox);
                            save();
                        }
                    }
                };
                return cell;
            }
        };
        statusCol.setCellFactory(cellFactory);
    }
    public void save() {
        clearInFile();
        saveProductsToFile();
    }

}