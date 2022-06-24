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
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;

public class CustomerAppointmentsByTypeAndMonthController implements Initializable {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void getAppointmentByMonth() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        ObservableList<Month> months = FXCollections.observableArrayList();
        ObservableList<String> appointmentType = FXCollections.observableArrayList();

        try {
            appointments = DBAppointments.getAllAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Appointment appointment: appointments) {
            LocalDateTime dateTime = appointment.getStart().toLocalDateTime();
            if (!months.contains(dateTime.getMonth())) {
                months.add(dateTime.getMonth());
            }

            if (!appointmentType.contains(appointment.getType())) {
                appointmentType.add(appointment.getType());
            }
        }

        for (Month month : months) {
            System.out.println(month);
        }

        for (String type : appointmentType) {
            System.out.println(type);
        }
    }

    public void goToReports(ActionEvent event) {
        sceneController.setScene(event, "Reports.fxml");
    }
}
