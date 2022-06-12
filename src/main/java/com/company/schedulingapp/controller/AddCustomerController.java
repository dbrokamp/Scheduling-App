package com.company.schedulingapp.controller;

import com.company.schedulingapp.util.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


public class AddCustomerController {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    private String newCustomerName;
    private String newCustomerAddress;
    private String newCustomerPostalCode;
    private String newCustomerPhone;
    private String newCustomerState;
    private String newCustomerCountry;

    @FXML TextField name;
    @FXML TextField address;
    @FXML TextField postalCode;
    @FXML ComboBox<String> country;
    @FXML ComboBox<String> state;


    public void save() {

    }

    public void cancel(ActionEvent event) {
        sceneController.setScene(event, "Customers");

    }

}
