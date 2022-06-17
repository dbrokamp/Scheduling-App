package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBCountries;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
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


        getFirstLevelDivisionsForSelectedCountry();
        populateFirstLevelDivisionComboBox();

        populateFormFieldsWithCustomerData();

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

    private void getFirstLevelDivisionsForSelectedCountry() {
        try {
            for (FirstLevelDivision firstLevelDivision : DBFirstLevelDivisions.getFirstLevelDivisionsForCountryID(customerToModifyCountry.getCountryID())) {
                firstLevelDivisionNames.add(firstLevelDivision.getDivision());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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


    public void cancel(ActionEvent event) {
        returnToMainScene(event);
    }


}
