package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBCustomers;
import com.company.schedulingapp.dbaccess.DBUsers;
import com.company.schedulingapp.model.Contact;
import com.company.schedulingapp.model.Customer;
import com.company.schedulingapp.model.User;
import com.company.schedulingapp.util.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    @FXML TextField titleTextField;
    @FXML TextField descriptionTextField;
    @FXML TextField locationTextField;
    @FXML TextField typeTextField;
    @FXML DatePicker startDatePicker;
    @FXML ComboBox<String> startTimeComboBox;
    @FXML DatePicker endDatePicker;
    @FXML ComboBox<String> endTimeComboBox;
    @FXML ComboBox<Integer> customerComboBox;
    @FXML ComboBox<Integer> userComboBox;
    @FXML ComboBox<Integer> contactComboBox;

    private static ObservableList<LocalTime> appointmentTimes = FXCollections.observableArrayList();
    private static ObservableList<Integer> customerIDs = FXCollections.observableArrayList();
    private static ObservableList<Integer> userIDs = FXCollections.observableArrayList();
    private static ObservableList<Integer> contactIDs = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        createAppointmentTimes();
        populateStartTimeComboBox();
        populateEndTimeComboBox();
        populateCustomerIDComboBox();
        populateUserIDComboBox();
    }

    private static void createAppointmentTimes() {
        LocalTime businessHoursStart = LocalTime.of(8,00);
        LocalTime businessHoursEnd = LocalTime.of(22,00);
        Duration quarterHour = Duration.ofMinutes(15);

        LocalTime appointment = businessHoursStart;

        while (appointment.isBefore(businessHoursEnd)) {
            appointmentTimes.add(appointment);
            appointment = appointment.plus(quarterHour);
        }

        appointmentTimes.add(businessHoursEnd);
    }

    private void populateStartTimeComboBox() {
        ObservableList<String> appointmentTimesStrings = FXCollections.observableArrayList();

        for (LocalTime time : appointmentTimes) {
            appointmentTimesStrings.add(time.toString());
        }

        startTimeComboBox.setItems(appointmentTimesStrings);
    }

    private void populateEndTimeComboBox() {
        ObservableList<String> appointmentTimesStrings = FXCollections.observableArrayList();

        for (LocalTime time : appointmentTimes) {
            appointmentTimesStrings.add(time.toString());
        }

        endTimeComboBox.setItems(appointmentTimesStrings);
    }

    private void getCustomerIDs() {
        try {
            for (Customer customer : DBCustomers.getAllCustomers()) {
                customerIDs.add(customer.getCustomerID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateCustomerIDComboBox() {
        getCustomerIDs();
        customerComboBox.setItems(customerIDs);
    }

    private void getUserIDs() {
        for (User user : DBUsers.getAllUsers()) {
            userIDs.add(user.getUserID());
        }

    }

    private void populateUserIDComboBox() {
        getUserIDs();
        userComboBox.setItems(userIDs);
    }



}
