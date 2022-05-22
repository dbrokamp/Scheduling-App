package com.company.schedulingapp.controller;

import com.company.schedulingapp.util.SceneController;
import javafx.event.ActionEvent;

public class MainController {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    public void goToCustomersView(ActionEvent event) {
        sceneController.setScene(event, "Customers.fxml");
    }

    public void goToAppointmentsView(ActionEvent event) {
        sceneController.setScene(event, "Appointments.fxml");
    }
}
