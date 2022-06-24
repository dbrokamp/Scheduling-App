package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBAppointments;
import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.util.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;

public class ReportsController {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    public void goToCustomerAppointmentsByTypeAndMonth(ActionEvent event) {
        sceneController.setScene(event, "CustomerAppointmentsByTypeAndMonth.fxml");
    }

    public void goToContactsScheduleReports(ActionEvent event) {
        sceneController.setScene(event, "ContactsScheduleReports.fxml");
    }

    public void goToMain(ActionEvent event) {
        sceneController.setScene(event, "Main.fxml");
    }

}
