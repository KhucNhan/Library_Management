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
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.librarymanagement.BookController.newLoanSlip;
import static com.example.librarymanagement.UserController.currentUser;

public class LoanSlipController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static ObservableList<LoanSlip> loanSlips = FXCollections.observableArrayList();
    private static ObservableList<LoanSlip> userLoanSlips = FXCollections.observableArrayList();
    BookController bookController = new BookController();

    public void removeLoanSlip(LoanSlip loanSlip) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (bookController.isBookOnLoan(loanSlip.getIdBook()) && currentUser.getRole().equalsIgnoreCase("admin")) {
            alert.setContentText("Cannot remove loan slip. The book is currently on loan.");
            alert.show();
            return;
        }
        loanSlips.remove(loanSlip);
        bookController.updateBookStatus(loanSlip.getIdBook(), "Activated");
        save(); // Gọi phương thức save() để lưu các thay đổi vào tệp tin
    }

    public ObservableList<LoanSlip> getLoanSlips() {
        return loanSlips;
    }

    void loadLoanSlipFromFile() {
        loanSlips.clear(); // Xóa tất cả các phiếu mượn hiện có trước khi tải lại
        File file = new File("loanSlip.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    LoanSlip loanSlip = new LoanSlip(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    loanSlips.add(loanSlip);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showError("Invalid data format, There was an error reading the loan slip data.");
        }
    }

    void loadLoanSlipFromFileForUser() {
        loanSlips.clear();
        File file = new File("loanSlip.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    LoanSlip loanSlip = new LoanSlip(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    if (loanSlip.getIdUser().equals(currentUser.getUserId())) {
                        userLoanSlips.add(loanSlip);
                    }
                    loanSlips.add(loanSlip);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showError("Invalid data format, There was an error reading the loan slip data.");
        }
    }

    public void addNewLoanSlip() {
        loanSlips.add(newLoanSlip);
        save();
    }

    private void saveProductsToFile() {
        File file = new File("loanSlip.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (LoanSlip loanSlip : loanSlips) {
                String line = String.join(",",
                        loanSlip.getIdUser(),
                        loanSlip.getIdBook(),
                        loanSlip.getDate(),
                        loanSlip.getReturnDate(),
                        loanSlip.getStatus()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error saving data: Could not save LoanSlip data to file.");
        }
    }

    public void clearInFile() {
        File file = new File("loanSlip.txt");
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
        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            Parent root = FXMLLoader.load(getClass().getResource("AdminView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1080, 720);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("UserView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1080, 720);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        }
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

    @FXML

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            initializeAdmin();
        } else {
            initializeUser();
        }
    }

    private void initializeUser() {
        userLoanSlips = FXCollections.observableArrayList();
        loadLoanSlipFromFileForUser();
        tableView.setItems(userLoanSlips);
//        idUserCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idUser"));
        idUserCol.setVisible(false);
        idBookCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idBook"));
        dateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("date"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("returnDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("status"));
        actionCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, Void>(""));
        Callback<TableColumn<LoanSlip, Void>, TableCell<LoanSlip, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<LoanSlip, Void> call(TableColumn<LoanSlip, Void> loanSlipTableColumn) {
                final TableCell<LoanSlip, Void> cell = new TableCell<>() {
                    private final Button changeStatusButton = new Button("Paid");

                    {
                        changeStatusButton.setOnAction((ActionEvent event) -> {
                            LoanSlip loanSlip = getTableView().getItems().get(getIndex());
                            if (loanSlip.getStatus().equalsIgnoreCase("On loan")) {
                                loanSlip.setStatus("Paid");
                                bookController.updateBookStatus(loanSlip.getIdBook(), "Activated"); // Cập nhật trạng thái sách

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
                            LoanSlip loanSlip = getTableView().getItems().get(getIndex());
                            if (loanSlip.getStatus().equalsIgnoreCase("Paid")) {
                                changeStatusButton.setDisable(true);
                            }
                            tableView.refresh(); // Làm mới TableView
                            save(); // Lưu các thay đổi vào tệp tin
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
    }

    public boolean showConfirmation() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete loan slip");
        alert.setHeaderText("Are you sure want to remove this loan slip ?");

        Optional<ButtonType> option = alert.showAndWait();

        return option.get() == ButtonType.OK;
    }

    private void initializeAdmin() {
        loanSlips = FXCollections.observableArrayList();
        loadLoanSlipFromFile();
        tableView.setItems(loanSlips);
        idUserCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idUser"));
        idBookCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idBook"));
        dateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("date"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("returnDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("status"));
        Callback<TableColumn<LoanSlip, Void>, TableCell<LoanSlip, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<LoanSlip, Void> call(final TableColumn<LoanSlip, Void> param) {
                final TableCell<LoanSlip, Void> cell = new TableCell<>() {
                    private final Button removeButton = new Button("Remove");

                    {
                        removeButton.setOnAction((ActionEvent event) -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            LoanSlip loanSlip = getTableView().getItems().get(getIndex());
                            if (bookController.isBookOnLoan(loanSlip.getIdBook())) {
                                alert.setContentText("Cannot remove loan slip. The book is currently on loan.");
                                alert.show();
                                return;
                            }
                            if (showConfirmation()) {
                                removeLoanSlip(loanSlip);
                                tableView.refresh();
                            }
                        });
                        removeButton.setPrefWidth(150);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hBox = new HBox(removeButton);
                            hBox.setSpacing(10);
                            setGraphic(hBox);
                        }
                    }
                };
                return cell;
            }
        };
        actionCol.setCellFactory(cellFactory);
    }

    public void save() {
        clearInFile();
        saveProductsToFile();
    }
}