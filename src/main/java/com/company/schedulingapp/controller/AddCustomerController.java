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


/**
 * Allows a user to add a new customer to the database
 */
public class AddCustomerController implements Initializable {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    @FXML TextField nameTextField;
    @FXML TextField addressTextField;
    @FXML TextField postalCodeTextField;
    @FXML ComboBox<String> countryComboBox;
    @FXML ComboBox<String> firstLevelDivisionComboBox;
    @FXML TextField phoneTextField;

    ObservableList<String> countryNames = FXCollections.observableArrayList();
    ObservableList<String> firstLevelDivisionNames = FXCollections.observableArrayList();

    /**
     * Initializes scene by populating combo boxes with necessary data
     *
     * Adds listener to country combo box in order to populate first division combo box
     *
     * From https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/Initializable.html:
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resourceBundle  The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getCountryNames();
        populateCountryComboBox();


        countryComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {

            try {
                getFirstLevelDivisionNamesForSelectedCountry(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            populateFirstLevelDivisionComboBox();

        });

    }

    /**
     * Get all country names from database
     */
    private void getCountryNames() {
        try {
            for (Country country : DBCountries.getCountries()){
                countryNames.add(country.getCountry());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates list of countries in countryComboBox
     */
    private void populateCountryComboBox() {
        countryComboBox.setItems(countryNames);
    }

    /**
     * Once a country is selected, the first level divisions are retrieved for that country from database
     * @param countryName - country name to query database with
     * @throws SQLException throws error if SQL query fails
     */
    private void getFirstLevelDivisionNamesForSelectedCountry(String countryName) throws SQLException {
        clearFirstLevelDivisionNames();

        Country country = DBCountries.getCountryByName(countryName);

        for (FirstLevelDivision firstLevelDivision : DBFirstLevelDivisions.getFirstLevelDivisionsForCountryID(country.getCountryID())) {
            firstLevelDivisionNames.add(firstLevelDivision.getDivision());
        }

    }

    /**
     * Populates list of first level divisions in firstLevelDivisionComboBox
     */
    private void populateFirstLevelDivisionComboBox() { firstLevelDivisionComboBox.setItems(firstLevelDivisionNames); }

    /**
     * Clears list of first level divisions if a new country is selected
     */
    private void clearFirstLevelDivisionNames() { firstLevelDivisionNames.clear(); }

    /**
     * Checks for empty fields and highlights them in red
     * @return true is there is one empty field
     */
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


    /**
     * Check for empty fields, saves new customer to database
     * @param event save button
     */
    public void save(ActionEvent event) {
        boolean hasEmptyFields = checkForEmptyFields();

        if (hasEmptyFields) {
            presentEmptyFieldMessage();
        } else {
            try {
                DBCustomers.addNewCustomer(nameTextField.getText(),
                                            addressTextField.getText(),
                                            postalCodeTextField.getText(),
                                            phoneTextField.getText(),
                                            firstLevelDivisionComboBox.getValue());
                presentCustomerAddedSuccessAlert();
                returnToMainScene(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



    }

    /**
     * Presents on screen message if there is an empty or null field in the form
     */
    private void presentEmptyFieldMessage() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
        emptyFieldAlert.setTitle("Application Message");
        emptyFieldAlert.setHeaderText("Form Input");
        emptyFieldAlert.setContentText("All fields must be completed.");
        emptyFieldAlert.showAndWait();
    }

    /**
     * Presents on screen message if the customer was successfully added to the database
     */
    private void presentCustomerAddedSuccessAlert() {
        Alert customerAddedAlert = new Alert(Alert.AlertType.INFORMATION);
        customerAddedAlert.setTitle("Database Message");
        customerAddedAlert.setHeaderText("Success");
        customerAddedAlert.setContentText("Customer added to database.");
        customerAddedAlert.showAndWait();

    }

    /**
     * Returns user to main screen
     * @param event button clicked
     */
    private void returnToMainScene(ActionEvent event) {
        sceneController.setScene(event, "Main.fxml");
    }


    /**
     * Returns user to main screen
     * @param event cancel button
     */
    public void cancel(ActionEvent event) {
        returnToMainScene(event);
    }


}
