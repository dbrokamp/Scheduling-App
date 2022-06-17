package com.company.schedulingapp.controller;

import com.company.schedulingapp.util.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    private String newCustomerName;
    private String newCustomerAddress;
    private String newCustomerPostalCode;
    private String newCustomerPhone;
    private String newCustomerFirstLevelDivision;

    @FXML
    TextField nameTextField;
    @FXML TextField addressTextField;
    @FXML TextField postalCodeTextField;
    @FXML
    ComboBox<String> countryComboBox;
    @FXML ComboBox<String> firstLevelDivisionComboBox;
    @FXML TextField phoneTextField;

    ObservableList<String> countryNames = FXCollections.observableArrayList();
    ObservableList<String> firstLevelDivisionNames = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
