package com.example.librarymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class LoanSlipController {
    @FXML
    private TableView<LoanSlip> tableView;
    @FXML
    private TableColumn<LoanSlip, String> maPhieuMuonColumn;
    @FXML
    private TableColumn<LoanSlip, String> maSachColumn;
    @FXML
    private TableColumn<LoanSlip, String> maDocGiaColumn;
    @FXML
    private TableColumn<LoanSlip, LocalDate> ngayMuonColumn;
    @FXML
    private TableColumn<LoanSlip, LocalDate> ngayTraColumn;

    @FXML
    private TextField maPhieuMuonField;
    @FXML
    private TextField maSachField;
    @FXML
    private TextField maDocGiaField;
    @FXML
    private DatePicker ngayMuonPicker;
    @FXML
    private DatePicker ngayTraPicker;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;

    private ObservableList<LoanSlip> loanSlipList;

    public void initialize() {
        loanSlipList = FXCollections.observableArrayList();

        maPhieuMuonColumn.setCellValueFactory(new PropertyValueFactory<>("maPhieuMuon"));
        maSachColumn.setCellValueFactory(new PropertyValueFactory<>("maSach"));
        maDocGiaColumn.setCellValueFactory(new PropertyValueFactory<>("maDocGia"));
        ngayMuonColumn.setCellValueFactory(new PropertyValueFactory<>("ngayMuon"));
        ngayTraColumn.setCellValueFactory(new PropertyValueFactory<>("ngayTra"));

        tableView.setItems(loanSlipList);

        addButton.setOnAction(event -> addLoanSlip());
        deleteButton.setOnAction(event -> deleteLoanSlip());
    }

    private void addLoanSlip() {
        String maPhieuMuon = maPhieuMuonField.getText();
        String maSach = maSachField.getText();
        String maDocGia = maDocGiaField.getText();
        String ngayMuon = ngayMuonPicker.getAccessibleText();
        String ngayTra = ngayTraPicker.getAccessibleText();

        LoanSlip loanSlip = new LoanSlip(maPhieuMuon, maSach, maDocGia, ngayMuon, ngayTra);
        loanSlipList.add(loanSlip);

        clearFields();
    }

    private void deleteLoanSlip() {
        LoanSlip selectedLoanSlip = tableView.getSelectionModel().getSelectedItem();
        if (selectedLoanSlip != null) {
            loanSlipList.remove(selectedLoanSlip);
        }
    }

    private void clearFields() {
        maPhieuMuonField.clear();
        maSachField.clear();
        maDocGiaField.clear();
        ngayMuonPicker.setValue(null);
        ngayTraPicker.setValue(null);
    }

}
