package com.company.schedulingapp.controller;

import com.company.schedulingapp.util.SceneController;
import javafx.event.ActionEvent;

public class ContactsScheduleReportsController {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    public void goToReports(ActionEvent event) {
        sceneController.setScene(event, "Reports.fxml");
    }
}
