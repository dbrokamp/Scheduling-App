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

    private boolean checkForEmptyFields() {
        boolean hasEmptyField = false;
        if (nameTextField.getText().isEmpty()) {
            nameTextField.setId("empty-field");
            hasEmptyField = true;
        }  else {
            nameTextField.setId("reset-border");
        }

        if (addressTextField.getText().isEmpty()) {
            addressTextField.setId("empty-field");
            hasEmptyField = true;
        } else {
            addressTextField.setId("reset-border");
        }

        if (postalCodeTextField.getText().isEmpty()) {
            postalCodeTextField.setId("empty-field");
            hasEmptyField = true;
        } else {
            postalCodeTextField.setId("reset-border");
        }

        if (countryComboBox.getValue() == null) {
            countryComboBox.setId("empty-field");
            hasEmptyField = true;
        } else {
            countryComboBox.setId("reset-border");
        }

        if (firstLevelDivisionComboBox.getValue() == null) {
            firstLevelDivisionComboBox.setId("empty-field");
            hasEmptyField = true;
        } else {
            firstLevelDivisionComboBox.setId("reset-border");
        }

        if (phoneTextField.getText().isEmpty()) {
            phoneTextField.setId("empty-field");
            hasEmptyField = true;
        } else {
            phoneTextField.setId("reset-border");
        }

        return hasEmptyField;
    }

    private void presentEmptyFieldMessage() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
        emptyFieldAlert.setTitle("Application Message");
        emptyFieldAlert.setHeaderText("Form Input");
        emptyFieldAlert.setContentText("All fields must be completed.");
        emptyFieldAlert.showAndWait();
    }

    private void checkNameFieldForChanges() {
        if (nameTextField.getText().equals(customerToModify.getCustomerName())) {
            System.out.println("No changes to name field");
        } else {
            updateCustomerName();
        }
    }

    private void updateCustomerName() {
        try {
            DBCustomers.updateCustomerName(nameTextField.getText(), customerToModify.getCustomerID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkAddressFieldForChanges() {
        if (addressTextField.getText().equals(customerToModify.getAddress())) {
            System.out.println("No changes to address field");
        } else {
            updateCustomerAddress();
        }
    }

    private void updateCustomerAddress() {
        try {
            DBCustomers.updateCustomerAddress(addressTextField.getText(), customerToModify.getCustomerID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkPostalCodeFieldForChanges() {
        if (postalCodeTextField.getText().equals(customerToModify.getPostalCode())) {
            System.out.println("No changes to postal code field");
        } else {
            updateCustomerPostalCode();
        }
    }

    private void updateCustomerPostalCode() {
        try {
            DBCustomers.updateCustomerPostalCode(postalCodeTextField.getText(), customerToModify.getCustomerID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkPhoneFieldForChanges() {
        if (phoneTextField.getText().equals(customerToModify.getPhone())) {
            System.out.println("No changes to phone field");
        } else {
            updateCustomerPhone();
        }
    }

    private void updateCustomerPhone() {
        try {
            DBCustomers.updateCustomerPhone(phoneTextField.getText(), customerToModify.getCustomerID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkFirstLevelDivisionFieldForChanges() throws SQLException {
        try {
            if (DBFirstLevelDivisions.getDivisionID(firstLevelDivisionComboBox.getValue()).equals(DBFirstLevelDivisions.getFirstLevelDivisionFromDivisionID(customerToModify.getDivisionID()).getDivisionID())) {
                System.out.println("No changes to first level division field");
            } else {
                updateCustomerFirstLevelDivision();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void updateCustomerFirstLevelDivision() {
        try {
            DBCustomers.updateCustomerDivisionID(DBFirstLevelDivisions.getDivisionID(firstLevelDivisionComboBox.getValue()), customerToModify.getCustomerID());
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

    private void checkFieldsForChangesAndUpdateDatabase() {
        checkNameFieldForChanges();
        checkAddressFieldForChanges();
        checkPostalCodeFieldForChanges();
        checkPhoneFieldForChanges();
        try {
            checkFirstLevelDivisionFieldForChanges();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        presentCustomerUpdatedSuccessAlert();

    }


    public void cancel(ActionEvent event) {
        returnToMainScene(event);
    }

    public void save(ActionEvent event) throws SQLException {
        if (checkForEmptyFields()) {
            presentEmptyFieldMessage();
        } else {
            checkFieldsForChangesAndUpdateDatabase();
            returnToMainScene(event);
        }

    }


}
