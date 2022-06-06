package com.company.schedulingapp.controller;

import com.company.schedulingapp.model.Customer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    @FXML TableView<Customer> customerTableView = new TableView<>();
    @FXML TableColumn<Customer, Integer> customerIDColumn = new TableColumn<>("ID");
    @FXML TableColumn<Customer, String> customerNameColumn = new TableColumn<>("Name");
    @FXML TableColumn<Customer, String> customerAddressColumn = new TableColumn<>("Address");
    @FXML TableColumn<Customer, String> customerPostalCodeColumn = new TableColumn<>("Postal Code");
    @FXML TableColumn<Customer, String> customerPhoneColumn = new TableColumn<>("Phone");
    @FXML TableColumn<Customer, Integer> customerDivisionIDColumn = new TableColumn<>("Division ID");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        

    }
}
