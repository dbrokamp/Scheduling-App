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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.ResourceBundle;

/**
 * Allows user to modify an existing appointment
 */
public class ModifyAppointmentController implements Initializable {

    final private SceneController sceneController = SceneController.getSceneControllerInstance();
    final private Appointment appointmentToModify = MainController.getSelectedAppointment();
    final private ObservableList<String> appointmentTimes = FXCollections.observableArrayList();
    final private ObservableList<Integer> customerIDs = FXCollections.observableArrayList();
    final private ObservableList<Integer> userIDs = FXCollections.observableArrayList();
    final private ObservableList<String> contactNames = FXCollections.observableArrayList();
    private String currentStartDate;
    private String currentStartTime;
    private String currentEndDate;
    private String currentEndTime;

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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        createAppointmentTimes();
        getCustomerIDs();
        getUserIDs();
        getContactNames();

        startTimeComboBox.setItems(appointmentTimes);
        endTimeComboBox.setItems(appointmentTimes);
        customerComboBox.setItems(customerIDs);
        userComboBox.setItems(userIDs);
        contactComboBox.setItems(contactNames);

        setFields();
    }

    /**
     * Sets fields with information from appointment to modify retrieved from MainController
     */
    private void setFields() {
        titleTextField.setText(appointmentToModify.getTitle());
        descriptionTextField.setText(appointmentToModify.getDescription());
        locationTextField.setText(appointmentToModify.getLocation());
        typeTextField.setText(appointmentToModify.getType());
        getAndSetStartDateAndTime();
        getAndSetEndDateAndTime();
        getAndSetCustomerID();
        getAndSetUserID();
        getAndSetContactName();
    }

    /**
     * Creates a list of valid appointment start and end times
     */
    private void createAppointmentTimes() {
        LocalTime businessHoursStart = LocalTime.of(8,0);
        LocalTime businessHoursEnd = LocalTime.of(22,0);
        Duration quarterHour = Duration.ofMinutes(15);

        LocalTime appointment = businessHoursStart;

        appointmentTimes.clear();
        while (appointment.isBefore(businessHoursEnd)) {
            appointmentTimes.add(appointment + ":00");
            appointment = appointment.plus(quarterHour);
        }

        appointmentTimes.add(businessHoursEnd.toString());
    }

    /**
     * Gets a list of all customer ids from database
     */
    private void getCustomerIDs() {
        try {
            for (Customer customer : DBCustomers.getAllCustomers()) {
                customerIDs.add(customer.getCustomerID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of all user ids from database
     */
    private void getUserIDs() {
        for (User user : DBUsers.getUsers()) {
            userIDs.add(user.getUserID());
        }

    }

    /**
     * Gets a list of all contact names from database
     */
    private void getContactNames() {
        for (Contact contact : DBContacts.getContacts()) {
            contactNames.add(contact.getContactName());
        }
    }

    /**
     * Gets and formats the start date and time of appointment to be modified
     */
    private void getAndSetStartDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String startDateFormatted = dateFormat.format(appointmentToModify.getStart());
        String[] startString = startDateFormatted.split("\\s+");
        currentStartDate = startString[0];
        currentStartTime = startString[1];
        startDatePicker.setValue(LocalDate.parse(currentStartDate));
        startTimeComboBox.setValue(currentStartTime);
    }

    /**
     * Gets and formats the end date and time of appointment to be modified
     */
    private void getAndSetEndDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String endDateFormatted = dateFormat.format(appointmentToModify.getEnd());
        String[] endString = endDateFormatted.split("\\s+");
        currentEndDate = endString[0];
        currentEndTime = endString[1];
        endDatePicker.setValue(LocalDate.parse(currentEndDate));
        endTimeComboBox.setValue(currentEndTime);
    }

    private void getAndSetCustomerID() {
        customerComboBox.setValue(appointmentToModify.getCustomerID());
    }

    private void getAndSetUserID() {
        userComboBox.setValue(appointmentToModify.getUserID());
    }

    private void getAndSetContactName() {
        contactComboBox.setValue(DBContacts.getContactNameFromContactID(appointmentToModify.getContactID()));
    }

    /**
     * Checks title field for changes, updates record in database if change
     */
    private void checkTitleFieldForChange() {
        if (appointmentToModify.getTitle().equals(titleTextField.getText())) {
            System.out.println("No changes to title field");
        } else {
            try {
                DBAppointments.updateAppointmentTitle(titleTextField.getText(), appointmentToModify.getAppointmentID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks description field for change, updates record in database if change
     */
    private void checkDescriptionFieldForChange() {
        if (appointmentToModify.getDescription().equals(descriptionTextField.getText())) {
            System.out.println("No changes to description field");
        } else {
            try {
                DBAppointments.updateAppointmentDescription(descriptionTextField.getText(), appointmentToModify.getAppointmentID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks location field for change, updates record in database if change
     */
    private void checkLocationFieldForChange() {
        if (appointmentToModify.getLocation().equals(locationTextField.getText())) {
            System.out.println("No changes to location field");
        } else {
            try {
                DBAppointments.updateAppointmentLocation(locationTextField.getText(), appointmentToModify.getAppointmentID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks type field for change, updates record in database if change
     */
    private void checkTypeFieldForChange() {
        if (appointmentToModify.getType().equals(typeTextField.getText())) {
            System.out.println("No changes to type field");
        } else {
            try {
                DBAppointments.updateAppointmentType(typeTextField.getText(), appointmentToModify.getAppointmentID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks start date and time fields for changes, updates records in database if change
     */
    private void checkStartDateAndTimeFieldForChange() {
        if (currentStartDate.equals(startDatePicker.getValue().toString()) && currentStartTime.equals(startTimeComboBox.getValue())) {
            System.out.println("No changes to start date or time");
        } else {
            if (!currentStartTime.equals(startTimeComboBox.getValue())) {
                String start = startDatePicker.getValue().toString() + " " + startTimeComboBox.getValue();
                Timestamp newStart = Timestamp.valueOf(start);
                try {
                    DBAppointments.updateAppointmentStart(newStart, appointmentToModify.getAppointmentID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                String start = startDatePicker.getValue().toString() + " " + startTimeComboBox.getValue();
                Timestamp newStart = Timestamp.valueOf(start);
                try {
                    DBAppointments.updateAppointmentStart(newStart, appointmentToModify.getAppointmentID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * Checks end date and end time for changes, updates records in database if change
     */
    private void checkEndDateAndTimeFieldForChange() {
        if (currentEndDate.equals(endDatePicker.getValue().toString()) && currentEndTime.equals(endTimeComboBox.getValue())) {
            System.out.println("No changes to end date or time");
        } else {
            if (!currentEndTime.equals(endTimeComboBox.getValue())) {
                String end = endDatePicker.getValue().toString() + " " + endTimeComboBox.getValue();
                Timestamp newEnd = Timestamp.valueOf(end);
                try {
                    DBAppointments.updateAppointmentEnd(newEnd, appointmentToModify.getAppointmentID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                String end = endDatePicker.getValue().toString() + " " + endTimeComboBox.getValue();
                Timestamp newEnd = Timestamp.valueOf(end);
                try {
                    DBAppointments.updateAppointmentEnd(newEnd, appointmentToModify.getAppointmentID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Checks customer id field for change, updates record in database if change
     */
    private void checkCustomerIDFieldForChange() {
        if (appointmentToModify.getCustomerID().equals(customerComboBox.getValue())) {
            System.out.println("No changes to customer id field");
        } else {
            try {
                DBAppointments.updateAppointmentCustomerID(customerComboBox.getValue(), appointmentToModify.getAppointmentID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks user id field for change, updates record in database if change
     */
    private void checkUserIDFieldForChange() {
        if (appointmentToModify.getUserID().equals(userComboBox.getValue())) {
            System.out.println("No changes to user id field");
        } else {
            try {
                DBAppointments.updateAppointmentUserID(userComboBox.getValue(), appointmentToModify.getAppointmentID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks contact name field for change, updates record in database if change
     */
    private void checkContactNameFieldForChange() {
        if (appointmentToModify.getContactID().equals(DBContacts.getContactIDFromContactName(contactComboBox.getValue()))) {
            System.out.println("No changes to contact field");
        } else {
            try {
                DBAppointments.updateAppointmentContactID(DBContacts.getContactIDFromContactName(contactComboBox.getValue()), appointmentToModify.getAppointmentID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks other of the customer's appointments to see if times overlap
     *
     * @param customerID customer appointments to retrieve
     * @param newAppointmentStart start time of modified appointment
     * @param newAppointmentEnd end time of modified appointment
     * @return true if modified times overlap with another appointment
     */
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
                if (appointment.getAppointmentID().equals(appointmentToModify.getAppointmentID())) {
                    System.out.println("Same appointment");
                } else {
                    hasOverlappingAppointment = true;
                    overlappedAppointment = appointment;
                }

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

    /**
     * Checks modified start time to see if it is in the past
     * @param newStart start time of modified appointment
     * @return true if start time is in the past
     */
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

    private Timestamp createStartTimestamp(String startDate, String startTime) {
        String start = startDate + " " + startTime;
        return Timestamp.valueOf(start);
    }

    private Timestamp createEndTimeTimestamp(String endDate, String endTime) {
        String end = endDate + " " + endTime;
        return Timestamp.valueOf(end);
    }

    /**
     * Checks all form fields for input
     * @return true if any of the field are empty
     */
    private boolean checkForEmptyFields() {
        boolean hasEmptyField = false;

        if (titleTextField.getText().isEmpty()) {
            titleTextField.setId("empty-field");
            hasEmptyField = true;
        } else {
            titleTextField.setId("reset-border");
        }

        if (descriptionTextField.getText().isEmpty()) {
            descriptionTextField.setId("empty-field");
            hasEmptyField = true;
        } else {
            descriptionTextField.setId("reset-border");
        }

        if (locationTextField.getText().isEmpty()) {
            locationTextField.setId("empty-field");
            hasEmptyField = true;
        } else {
            locationTextField.setId("reset-border");
        }

        if (typeTextField.getText().isEmpty()) {
            typeTextField.setId("empty-field");
            hasEmptyField = true;
        } else {
            typeTextField.setId("reset-border");
        }

        if (startDatePicker.getValue() == null) {
            startDatePicker.setId("empty-field");
            hasEmptyField = true;
        } else {
            startDatePicker.setId("reset-border");
        }

        if (startTimeComboBox.getValue() == null) {
            startTimeComboBox.setId("empty-field");
            hasEmptyField = true;
        } else {
            startTimeComboBox.setId("reset-border");
        }

        if (endDatePicker.getValue() == null) {
            endDatePicker.setId("empty-field");
            hasEmptyField = true;
        } else {
            endDatePicker.setId("reset-border");
        }

        if (endTimeComboBox.getValue() == null) {
            endTimeComboBox.setId("empty-field");
            hasEmptyField = true;
        } else {
            endTimeComboBox.setId("reset-border");
        }

        if (customerComboBox.getValue() == null) {
            customerComboBox.setId("empty-field");
            hasEmptyField = true;
        } else {
            customerComboBox.setId("reset-border");
        }

        if (userComboBox.getValue() == null) {
            userComboBox.setId("empty-field");
            hasEmptyField = true;
        } else {
            userComboBox.setId("reset-border");
        }

        if (contactComboBox.getValue() == null) {
            contactComboBox.setId("empty-field");
            hasEmptyField = true;
        } else {
            contactComboBox.setId("reset-border");
        }

        if (hasEmptyField) {
            presentEmptyFieldMessage();
        }

        return hasEmptyField;
    }

    private void presentEmptyFieldMessage() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
        emptyFieldAlert.setTitle("Application Message");
        emptyFieldAlert.setHeaderText("Form Input");
        emptyFieldAlert.setContentText("All fields must be completed.");
        emptyFieldAlert.showAndWait();
    }

    /**
     * Checks for empty fields, overlapping appointments, and if start is past
     * @return true if save is successful
     */
    private boolean save() {

        boolean hasEmptyField;
        hasEmptyField = checkForEmptyFields();

        if (!hasEmptyField) {
            Timestamp start = createStartTimestamp(startDatePicker.getValue().toString(), startTimeComboBox.getValue());
            Timestamp end = createEndTimeTimestamp(endDatePicker.getValue().toString(), endTimeComboBox.getValue());
            boolean hasOverlappingAppointment = checkForOverlappingAppointments(appointmentToModify.getCustomerID(), start, end);
            boolean startIsInPast = checkIfAppointmentDateIsInPast(start);

            if (hasOverlappingAppointment || startIsInPast) {
                return false;
            } else {
                checkTitleFieldForChange();
                checkDescriptionFieldForChange();
                checkLocationFieldForChange();
                checkTypeFieldForChange();
                checkCustomerIDFieldForChange();
                checkUserIDFieldForChange();
                checkContactNameFieldForChange();
                checkStartDateAndTimeFieldForChange();
                checkEndDateAndTimeFieldForChange();
                return true;
            }
        } else {
            return false;
        }
    }

    public void cancelActionButton(ActionEvent event) {
        sceneController.setScene(event, "Main.fxml");
    }


    public void saveActionButton(ActionEvent event) {
        boolean saveSuccessful = save();

        if (saveSuccessful) {
            sceneController.setScene(event,"Main.fxml");
        } else {
            System.out.println("Unable to modify appointment");
        }


    }
}
