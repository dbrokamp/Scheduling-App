package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBAppointments;
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
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    @FXML ComboBox<String> contactComboBox;

    private static ObservableList<LocalTime> appointmentTimes = FXCollections.observableArrayList();
    private static ObservableList<Integer> customerIDs = FXCollections.observableArrayList();
    private static ObservableList<Integer> userIDs = FXCollections.observableArrayList();
    private static ObservableList<String> contactNames = FXCollections.observableArrayList();

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
    private String newAppointmentContactName;

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

        appointmentTimes.clear();
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
        customerIDs.clear();
        getCustomerIDs();
        customerComboBox.setItems(customerIDs);
    }

    private void getUserIDs() {
        for (User user : DBUsers.getUsers()) {
            userIDs.add(user.getUserID());
        }

    }

    private void populateUserIDComboBox() {
        userIDs.clear();
        getUserIDs();
        userComboBox.setItems(userIDs);
    }

    private void getContactNames() {
        for (Contact contact : DBContacts.getContacts()) {
            contactNames.add(contact.getContactName());
        }
    }

    private void populateContactIDComboBox() {
        contactNames.clear();
        getContactNames();
        contactComboBox.setItems(contactNames);
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
        newAppointmentStartDate = String.valueOf(startDatePicker.getValue());
    }

    private void getInputFromEndDateField() {
        newAppointmentEndDate = String.valueOf(endDatePicker.getValue());
    }

    private void getInputFromStartTimeField() {
        newAppointmentStartTime = startTimeComboBox.getValue();
    }

    private void getInputFromEndTimeField() {
        newAppointmentEndTime = endTimeComboBox.getValue();
    }

    private void getInputFromCustomerIDField() {
        newAppointmentCustomerID = customerComboBox.getValue();
    }

    private void getInputFromUserIDField() {
        newAppointmentUserID = userComboBox.getValue();
    }

    private void getInputFromContactNameField() {
        newAppointmentContactName = contactComboBox.getValue();
    }

    private Timestamp createStartTimestamp(String startDate, String startTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = startDate + " " + startTime + ":00";
        return Timestamp.valueOf(start);
    }

    private Timestamp createEndTimeTimestamp(String endDate, String endTime) {
        String end = endDate + " " + endTime + ":00";
        return Timestamp.valueOf(end);
    }

    public void cancel(ActionEvent event) {
        sceneController.setScene(event, "Main.fxml");
    }

    public void save(ActionEvent event) {
        getInputFromTitleField();
        getInputFromDescriptionField();
        getInputFromLocationField();
        getInputFromTypeField();
        getInputFromStartDateField();
        getInputFromEndDateField();
        getInputFromStartTimeField();
        getInputFromEndTimeField();
        getInputFromCustomerIDField();
        getInputFromUserIDField();
        getInputFromContactNameField();

        Timestamp start = createStartTimestamp(newAppointmentStartDate, newAppointmentStartTime);
        Timestamp end = createEndTimeTimestamp(newAppointmentEndDate, newAppointmentEndTime);

        try {
            DBAppointments.addNewAppointment(newAppointmentTitle,
                                            newAppointmentDescription,
                                            newAppointmentLocation,
                                            newAppointmentType,
                                            start,
                                            end,
                                            newAppointmentCustomerID,
                                            newAppointmentUserID,
                                            newAppointmentContactName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sceneController.setScene(event, "Main.fxml");

    }

}
