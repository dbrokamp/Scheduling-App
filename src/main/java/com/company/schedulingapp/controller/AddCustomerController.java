package com.company.schedulingapp.controller;

import com.company.schedulingapp.util.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;



public class AddCustomerController {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    private String newCustomerName;
    private String newCustomerAddress;
    private String newCustomerPostalCode;
    private String newCustomerPhone;
    private String newCustomerState;
    private String newCustomerCountry;


    public void save() {

    }

    public void cancel(ActionEvent event) {
        sceneController.setScene(event, "Customers");

    }

}
