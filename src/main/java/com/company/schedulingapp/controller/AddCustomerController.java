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

    }

    private void setFirstLevelDivisionComboBox() { firstLevelDivisionComboBox.setItems(firstLevelDivisionNames); }

    private void clearFirstLevelDivisionNames() { firstLevelDivisionNames.clear(); }

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

    private void presentEmptyFieldMessage() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
        emptyFieldAlert.setTitle("Application Message");
        emptyFieldAlert.setHeaderText("Form Input");
        emptyFieldAlert.setContentText("All fields must be completed.");
        emptyFieldAlert.showAndWait();
    }

    private void presentCustomerAddedSuccessAlert() {
        Alert customerAddedAlert = new Alert(Alert.AlertType.INFORMATION);
        customerAddedAlert.setTitle("Database Message");
        customerAddedAlert.setHeaderText("Success");
        customerAddedAlert.setContentText("Customer added to database.");
        customerAddedAlert.showAndWait();

    }

    private void returnToMainScene(ActionEvent event) {
        sceneController.setScene(event, "Main.fxml");
    }


    public void cancel(ActionEvent event) {
        returnToMainScene(event);
    }


}
