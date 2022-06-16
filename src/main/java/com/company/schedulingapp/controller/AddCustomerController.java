package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBCountries;
import com.company.schedulingapp.dbaccess.DBCustomers;
import com.company.schedulingapp.dbaccess.DBFirstLevelDivisions;
import com.company.schedulingapp.model.Country;
import com.company.schedulingapp.model.FirstLevelDivision;
import com.company.schedulingapp.util.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AddCustomerController implements Initializable {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    private String newCustomerName;
    private String newCustomerAddress;
    private String newCustomerPostalCode;
    private String newCustomerPhone;
    private String newCustomerFirstLevelDivision;

    @FXML TextField nameTextField;
    @FXML TextField addressTextField;
    @FXML TextField postalCodeTextField;
    @FXML ComboBox<String> countryComboBox;
    @FXML ComboBox<String> firstLevelDivisionComboBox;
    @FXML TextField phoneTextField;

    ObservableList<String> countryNames = FXCollections.observableArrayList();
    ObservableList<String> firstLevelDivisionNames = FXCollections.observableArrayList();


    public void initialize(URL url, ResourceBundle resourceBundle) {
        getCountryNames();
        setCountryComboBox();


        countryComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {

            try {
                getFirstLevelDivisionNamesForSelectedCountry(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            setFirstLevelDivisionComboBox();

        });

    }

    private void getCountryNames() {
        try {
            for (Country country : DBCountries.getCountries()){
                countryNames.add(country.getCountry());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCountryComboBox() {
        countryComboBox.setItems(countryNames);
    }

    private void getFirstLevelDivisionNamesForSelectedCountry(String countryName) throws SQLException {
        clearFirstLevelDivisionNames();

        Country country = DBCountries.getCountryByName(countryName);

        for (FirstLevelDivision firstLevelDivision : DBFirstLevelDivisions.getFirstLevelDivisionsForCountryID(country.getCountryID())) {
            firstLevelDivisionNames.add(firstLevelDivision.getDivision());
        }

        for (String name : firstLevelDivisionNames) {
            System.out.println(name);
        }
    }

    private void setFirstLevelDivisionComboBox() { firstLevelDivisionComboBox.setItems(firstLevelDivisionNames); }

    private void clearFirstLevelDivisionNames() { firstLevelDivisionNames.clear(); }

    private void getTextInputFromFormFields() {
        newCustomerName = nameTextField.getText();
        newCustomerAddress = addressTextField.getText();
        newCustomerPostalCode = postalCodeTextField.getText();
        newCustomerFirstLevelDivision = firstLevelDivisionComboBox.getValue();
        newCustomerPhone = phoneTextField.getText();
    }


    public void save(ActionEvent event) {
        getTextInputFromFormFields();
        try {
            DBCustomers.addNewCustomer(newCustomerName, newCustomerAddress, newCustomerPostalCode, newCustomerPhone, newCustomerFirstLevelDivision);
            presentCustomerAddedSuccessAlert();
            returnToCustomersScene(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void presentCustomerAddedSuccessAlert() {
        Alert customerAddedAlert = new Alert(Alert.AlertType.INFORMATION);
        customerAddedAlert.setTitle("Database Message");
        customerAddedAlert.setHeaderText("Success");
        customerAddedAlert.setContentText("Customer added to database.");
        customerAddedAlert.showAndWait();

    }

    private void returnToCustomersScene(ActionEvent event) {
        sceneController.setScene(event, "Customers.fxml");
    }


    public void cancel(ActionEvent event) {
        returnToCustomersScene(event);
    }


}
