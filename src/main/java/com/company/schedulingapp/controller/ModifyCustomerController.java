package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBCountries;
import com.company.schedulingapp.dbaccess.DBCustomers;
import com.company.schedulingapp.dbaccess.DBFirstLevelDivisions;
import com.company.schedulingapp.model.Country;
import com.company.schedulingapp.model.Customer;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {

    SceneController sceneController = SceneController.getSceneControllerInstance();
    private Customer customerToModify;
    private FirstLevelDivision customerToModifyFirstLevelDivision;
    private Country customerToModifyCountry;

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
        customerToModify = MainController.getSelectedCustomer();
        System.out.println(customerToModify.getCustomerName());
        getFirstLevelDivisionForCustomer();
        getCountryForCustomer();

        getCountryNames();
        populateCountryComboBox();


        getFirstLevelDivisionsForCustomerCountry();
        populateFirstLevelDivisionComboBox();

        populateFormFieldsWithCustomerData();

        countryComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {

            if (!Objects.equals(newValue, oldValue)) {
                try {
                    getFirstLevelDivisionNamesForSelectedCountry(newValue);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                populateFirstLevelDivisionComboBox();
            }


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

    private void populateCountryComboBox() {
        countryComboBox.setItems(countryNames);
    }

    private void populateFormFieldsWithCustomerData() {
        nameTextField.setText(customerToModify.getCustomerName());
        addressTextField.setText(customerToModify.getAddress());
        postalCodeTextField.setText(customerToModify.getPostalCode());
        countryComboBox.getSelectionModel().select(customerToModifyCountry.getCountry());
        firstLevelDivisionComboBox.getSelectionModel().select(customerToModifyFirstLevelDivision.getDivision());
        phoneTextField.setText(customerToModify.getPhone());


    }

    private void getFirstLevelDivisionsForCustomerCountry() {
        clearFirstLevelDivisionNames();
        try {
            for (FirstLevelDivision firstLevelDivision : DBFirstLevelDivisions.getFirstLevelDivisionsForCountryID(customerToModifyCountry.getCountryID())) {
                firstLevelDivisionNames.add(firstLevelDivision.getDivision());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getFirstLevelDivisionNamesForSelectedCountry(String countryName) throws SQLException {
        clearFirstLevelDivisionNames();

        Country country = DBCountries.getCountryByName(countryName);

        for (FirstLevelDivision firstLevelDivision : DBFirstLevelDivisions.getFirstLevelDivisionsForCountryID(country.getCountryID())) {
            firstLevelDivisionNames.add(firstLevelDivision.getDivision());
        }

    }



    private void clearFirstLevelDivisionNames() { firstLevelDivisionNames.clear(); }

    private void populateFirstLevelDivisionComboBox() {
        firstLevelDivisionComboBox.setItems(firstLevelDivisionNames);
    }

    private void getFirstLevelDivisionForCustomer() {
        try {
            customerToModifyFirstLevelDivision = DBFirstLevelDivisions.getFirstLevelDivisionFromDivisionID(customerToModify.getDivisionID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getCountryForCustomer() {
        try {
            customerToModifyCountry = DBCountries.getCountryByID(customerToModifyFirstLevelDivision.getCountryID());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void returnToMainScene(ActionEvent event) {
        sceneController.setScene(event, "Main.fxml");
    }

    private void checkNameFieldForChanges() {
        if (nameTextField.getText() == customerToModify.getCustomerName()) {
            System.out.println("No changes to name field");
        } else {
            updateCustomerName();
        }
    }

    private void updateCustomerName() {
        newCustomerName = nameTextField.getText();
        try {
            DBCustomers.updateCustomerName(newCustomerName, customerToModify.getCustomerID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkAddressFieldForChanges() {
        if (addressTextField.getText() == customerToModify.getAddress()) {
            System.out.println("No changes to address field");
        } else {
            updateCustomerAddress();
        }
    }

    private void updateCustomerAddress() {
        newCustomerAddress = addressTextField.getText();
        try {
            DBCustomers.updateCustomerAddress(newCustomerAddress, customerToModify.getCustomerID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkPostalCodeFieldForChanges() {
        if (postalCodeTextField.getText() == customerToModify.getPostalCode()) {
            System.out.println("No changes to postal code field");
        } else {
            updateCustomerPostalCode();
        }
    }

    private void updateCustomerPostalCode() {
        newCustomerPostalCode = postalCodeTextField.getText();
        try {
            DBCustomers.updateCustomerPostalCode(newCustomerPostalCode, customerToModify.getCustomerID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkPhoneFieldForChanges() {
        if (phoneTextField.getText() == customerToModify.getPhone()) {
            System.out.println("No changes to phone field");
        } else {
            updateCustomerPhone();
        }
    }

    private void updateCustomerPhone() {
        newCustomerPhone = phoneTextField.getText();
        try {
            DBCustomers.updateCustomerPhone(newCustomerPhone, customerToModify.getCustomerID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkFirstLevelDivisionFieldForChanges() throws SQLException {
        if (firstLevelDivisionComboBox.getValue() == DBFirstLevelDivisions.getFirstLevelDivisionFromDivisionID(customerToModify.getDivisionID()).getDivision()) {
            System.out.println("No changes to first level division field");
        } else {
            updateCustomerFirstLevelDivision();
        }
    }

    private void updateCustomerFirstLevelDivision() {
        newCustomerFirstLevelDivision = firstLevelDivisionComboBox.getValue();
        try {
            DBCustomers.updateCustomerDivisionID(DBFirstLevelDivisions.getDivisionID(newCustomerFirstLevelDivision), customerToModify.getCustomerID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void presentCustomerUpdatedSuccessAlert() {
        Alert updateSuccessful = new Alert(Alert.AlertType.INFORMATION);
        updateSuccessful.setTitle("Application Message");
        updateSuccessful.setHeaderText("Success");
        updateSuccessful.setContentText("Customer information updated successfully");
        updateSuccessful.showAndWait();
    }


    public void cancel(ActionEvent event) {
        returnToMainScene(event);
    }

    public void save(ActionEvent event) throws SQLException {
        // First test if there are any changes to info
        checkNameFieldForChanges();
        checkAddressFieldForChanges();
        checkPostalCodeFieldForChanges();
        checkPhoneFieldForChanges();
        checkFirstLevelDivisionFieldForChanges();
        presentCustomerUpdatedSuccessAlert();
        returnToMainScene(event);

    }


}
