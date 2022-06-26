package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBAppointments;
import com.company.schedulingapp.dbaccess.DBContacts;
import com.company.schedulingapp.dbaccess.DBCustomers;
import com.company.schedulingapp.dbaccess.DBUsers;
import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.model.Contact;
import com.company.schedulingapp.model.Customer;
import com.company.schedulingapp.model.User;
import com.company.schedulingapp.util.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
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
    @FXML ComboBox<String> contactComboBox;

    final private static ObservableList<LocalTime> appointmentTimes = FXCollections.observableArrayList();
    final private static ObservableList<Integer> customerIDs = FXCollections.observableArrayList();
    final private static ObservableList<Integer> userIDs = FXCollections.observableArrayList();
    final private static ObservableList<String> contactNames = FXCollections.observableArrayList();

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
        LocalTime businessHoursStart = LocalTime.of(8, 0);
        LocalTime businessHoursEnd = LocalTime.of(22, 0);
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
        String start = startDate + " " + startTime + ":00";
        return Timestamp.valueOf(start);
    }

    private Timestamp createEndTimeTimestamp(String endDate, String endTime) {
        String end = endDate + " " + endTime + ":00";
        return Timestamp.valueOf(end);
    }

    private boolean checkForOverlappingAppointments(Integer customerID, Timestamp newAppointmentStart, Timestamp newAppointmentEnd) {
        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
        boolean hasOverlappingAppointment = false;
        Appointment overlappedAppointment = null;

        try {
            customerAppointments = DBAppointments.getCustomerAppointments(customerID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Appointment appointment : customerAppointments) {
            System.out.println("Testing appointments");
            if (((appointment.getStart().after(newAppointmentStart) || appointment.getStart().equals(newAppointmentStart)) && (appointment.getStart().before(newAppointmentEnd) || appointment.getStart().equals(newAppointmentEnd))) || ((appointment.getEnd().after(newAppointmentStart) || appointment.getEnd().equals(newAppointmentStart)) && (appointment.getEnd().before(newAppointmentEnd) || appointment.getEnd().equals(newAppointmentEnd)))) {
                hasOverlappingAppointment = true;
                overlappedAppointment = appointment;
            }
        }

        if (hasOverlappingAppointment) {
            presentHasOverlappedAppointment(overlappedAppointment);
        }

        return hasOverlappingAppointment;
    }

    private void presentHasOverlappedAppointment(Appointment overlappedAppointment) {
        Alert hasOverlappingAppointmentAlert = new Alert(Alert.AlertType.ERROR);
        hasOverlappingAppointmentAlert.setTitle("Application Message");
        hasOverlappingAppointmentAlert.setHeaderText("Appointment conflict");
        hasOverlappingAppointmentAlert.setContentText("This appointment conflicts with Appointment_ID: " + overlappedAppointment.getAppointmentID().toString());
        hasOverlappingAppointmentAlert.showAndWait();
    }

    private boolean checkIfAppointmentDateIsInPast(Timestamp newStart) {
        boolean isInPast = newStart.before(Timestamp.valueOf(LocalDateTime.now()));
        if (isInPast) {
            presentAppointmentStartIsInPast();
        }
        return isInPast;
    }

    private void presentAppointmentStartIsInPast() {
        Alert appointmentInPastAlert = new Alert(Alert.AlertType.ERROR);
        appointmentInPastAlert.setTitle("Application Message");
        appointmentInPastAlert.setHeaderText("Appointment error");
        appointmentInPastAlert.setContentText("Appointment start is in the past.");
        appointmentInPastAlert.showAndWait();
    }

    private boolean saveNewAppointment() {
        boolean saveSuccessful = false;
        boolean overlappingAppointment;
        boolean appointmentInPast;
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

        overlappingAppointment = checkForOverlappingAppointments(newAppointmentCustomerID, start, end);
        appointmentInPast = checkIfAppointmentDateIsInPast(start);

        if (overlappingAppointment || appointmentInPast) {
            System.out.println("Error in saving appointment.");
        } else {
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
            saveSuccessful = true;
        }

    return saveSuccessful;
    }

    public void cancel(ActionEvent event) {
        sceneController.setScene(event, "Main.fxml");
    }

    public void save(ActionEvent event) {

        if (saveNewAppointment()) {
            sceneController.setScene(event, "Main.fxml");
        } else {
            System.out.println("Save unsuccessful");
        }



    }

}
