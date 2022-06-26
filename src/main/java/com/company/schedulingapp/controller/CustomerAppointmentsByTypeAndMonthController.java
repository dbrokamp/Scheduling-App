package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBAppointments;
import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.util.SceneController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class CustomerAppointmentsByTypeAndMonthController implements Initializable {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    Map<Month, Integer> totalsByMonth = new HashMap<>();
    Map<String, Integer> totalsByType = new HashMap<>();

    @FXML TableView<Map.Entry<Month, Integer>> appointmentsByMonth = new TableView<>();
    @FXML TableColumn<Map.Entry<Month, Integer>, Month> monthColumn = new TableColumn<>("Month");
    @FXML TableColumn<Map.Entry<Month, Integer>, Integer> monthCountColumn = new TableColumn<>("Count");

    @FXML TableView<Map.Entry<String, Integer>> appointmentsByType = new TableView<>();
    @FXML TableColumn<Map.Entry<String, Integer>, String> typeColumn = new TableColumn<>("Type");
    @FXML TableColumn<Map.Entry<String, Integer>, Integer> typeCountColumn = new TableColumn<>("Count");



    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAllAppointments();
        getAppointmentCountByMonth();
        getAppointmentCountByType();

        populateByMonthTable();
        populateByTypeTable();

    }

    private void getAllAppointments() {
        try {
            appointments = DBAppointments.getAllAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getAppointmentCountByMonth() {

        for (Appointment appointment: appointments) {
            LocalDateTime dateTime = appointment.getStart().toLocalDateTime();
            if (totalsByMonth.containsKey(dateTime.getMonth())) {
                Integer count = totalsByMonth.get(dateTime.getMonth());
                count++;
                totalsByMonth.put(dateTime.getMonth(), count);
            } else {
                totalsByMonth.put(dateTime.getMonth(), 1);
            }
        }
    }

    private void getAppointmentCountByType() {
        for (Appointment appointment : appointments) {
            if (totalsByType.containsKey(appointment.getType())) {
                Integer count = totalsByType.get(appointment.getType());
                count++;
                totalsByType.put(appointment.getType(), count);
            } else {
                totalsByType.put(appointment.getType(), 1);
            }
        }
    }

    private void populateByMonthTable() {
        monthColumn.setCellValueFactory(entryMonthCellDataFeatures -> new SimpleObjectProperty(entryMonthCellDataFeatures.getValue().getKey()));
        monthCountColumn.setCellValueFactory(entryIntegerCellDataFeatures -> new SimpleObjectProperty(entryIntegerCellDataFeatures.getValue().getValue()));
        ObservableList<Map.Entry<Month, Integer>> items = FXCollections.observableArrayList(totalsByMonth.entrySet());
        appointmentsByMonth.setItems(items);
    }

    private void populateByTypeTable() {
        typeColumn.setCellValueFactory(entryStringCellDataFeatures -> new SimpleObjectProperty(entryStringCellDataFeatures.getValue().getKey()));
        typeCountColumn.setCellValueFactory(entryIntegerCellDataFeatures -> new SimpleObjectProperty(entryIntegerCellDataFeatures.getValue().getValue()));
        ObservableList<Map.Entry<String, Integer>> items = FXCollections.observableArrayList(totalsByType.entrySet());
        appointmentsByType.setItems(items);
    }


    public void goToReports(ActionEvent event) {
        sceneController.setScene(event, "Reports.fxml");
    }
}
