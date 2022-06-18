package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBContacts;
import com.company.schedulingapp.dbaccess.DBCustomers;
import com.company.schedulingapp.dbaccess.DBUsers;
import com.company.schedulingapp.model.Contact;
import com.company.schedulingapp.model.Customer;
import com.company.schedulingapp.model.User;
import com.company.schedulingapp.util.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
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

    private String newAppointmentTitle;
    private String newAppointmentDescription;
    private String newAppointmentLocation;
    private String newAppointmentType;
    private String newAppointmentStartDate;
    private String newAppointmentEndDate;
    private String newAppointmentStartTime;
    private String newAppointmentEndTime;
    private Integer newAppointmentCustomerID;
    private Integer newAppointmentUserID;
    private Integer newAppointmentContactID;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        createAppointmentTimes();
        populateStartTimeComboBox();
        populateEndTimeComboBox();
        populateCustomerIDComboBox();
        populateUserIDComboBox();
        populateContactIDComboBox();
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
        for (User user : DBUsers.getUsers()) {
            userIDs.add(user.getUserID());
        }

    }

    private void populateUserIDComboBox() {
        getUserIDs();
        userComboBox.setItems(userIDs);
    }

    private void getContactIDs() {
        for (Contact contact : DBContacts.getContacts()) {
            contactIDs.add(contact.getContactID());
        }
    }

    private void populateContactIDComboBox() {
        getContactIDs();
        contactComboBox.setItems(contactIDs);
    }

    private void getInputFromTitleField() {
        newAppointmentTitle = titleTextField.getText();
    }

    private void getInputFromDescriptionField() {
        newAppointmentDescription = descriptionTextField.getText();
    }

    private void getInputFromLocationField() {
        newAppointmentLocation = locationTextField.getText();
    }

    private void getInputFromTypeField() {
        newAppointmentType = typeTextField.getText();
    }

    private void getInputFromStartDateField() {
        newAppointmentStartDate = startDatePicker.toString();
    }

    private void getInputFromEndDateField() {
        newAppointmentEndDate = endDatePicker.toString();
    }

    private void getInputFromStartTimeField() {
        newAppointmentStartTime = startTimeComboBox.toString();
    }

    private void getInputFromEndTimeField() {
        newAppointmentEndTime = endTimeComboBox.toString();
    }

    private void getInputFromCustomerIDField() {
        newAppointmentCustomerID = customerComboBox.getValue();
    }

    private void getInputFromUserIDField() {
        newAppointmentUserID = userComboBox.getValue();
    }

    private void getInputFromContactIDField() {
        newAppointmentContactID = contactComboBox.getValue();
    }

    public void cancel(ActionEvent event) {
        sceneController.setScene(event, "Main.fxml");
    }

    public void save(ActionEvent event) {

    }

}
