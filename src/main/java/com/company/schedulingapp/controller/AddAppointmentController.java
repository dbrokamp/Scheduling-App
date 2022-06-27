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

/**
 * Allows user to add a new appointment and save it to the database
 */
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

    /**
     * Initializes scene by populating combo boxes with necessary data
     *
     * From https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/Initializable.html:
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resourceBundle  The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createAppointmentTimes();
        populateStartTimeComboBox();
        populateEndTimeComboBox();
        populateCustomerIDComboBox();
        populateUserIDComboBox();
        populateContactNameComboBox();
    }

    /**
     * Creates list of appointment times for time combo boxes
     */
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

    /**
     * Adds all possible appointment start times to startTimeComboBox
     */
    private void populateStartTimeComboBox() {
        ObservableList<String> appointmentTimesStrings = FXCollections.observableArrayList();

        for (LocalTime time : appointmentTimes) {
            appointmentTimesStrings.add(time.toString());
        }

        startTimeComboBox.setItems(appointmentTimesStrings);
    }

    /**
     * Adds all possible appointment end times to endTimeComboBox
     */
    private void populateEndTimeComboBox() {
        ObservableList<String> appointmentTimesStrings = FXCollections.observableArrayList();

        for (LocalTime time : appointmentTimes) {
            appointmentTimesStrings.add(time.toString());
        }

        endTimeComboBox.setItems(appointmentTimesStrings);
    }

    /**
     * Gets all customer IDs from customers table in database
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
     * Adds all customer ids to customerComboBox
     */
    private void populateCustomerIDComboBox() {
        customerIDs.clear();
        getCustomerIDs();
        customerComboBox.setItems(customerIDs);
    }

    /**
     * Gets all user IDs from users table in database
     */
    private void getUserIDs() {
        for (User user : DBUsers.getUsers()) {
            userIDs.add(user.getUserID());
        }

    }

    /**
     * Adds all user ids to userComboBox
     */
    private void populateUserIDComboBox() {
        userIDs.clear();
        getUserIDs();
        userComboBox.setItems(userIDs);
    }

    /**
     * Gets all contact names from contacts table in database
     */
    private void getContactNames() {
        for (Contact contact : DBContacts.getContacts()) {
            contactNames.add(contact.getContactName());
        }
    }

    /**
     * Adds all contact names to combo box
     */
    private void populateContactNameComboBox() {
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

    /**
     * Checks all form fields for empty or null values
     * Highlights border of field if it is empty
     * @return true if any of the fields are empty or null
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

    /**
     * Presents an onscreen alert if there is an empty or null input field
     */
    private void presentEmptyFieldMessage() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
        emptyFieldAlert.setTitle("Application Message");
        emptyFieldAlert.setHeaderText("Form Input");
        emptyFieldAlert.setContentText("All fields must be completed.");
        emptyFieldAlert.showAndWait();
    }

    /**
     * Converts strings to timestamps
     * @param startDate Date as String
     * @param startTime Time as String
     * @return Strings as a Timestamp
     */
    private Timestamp createStartTimestamp(String startDate, String startTime) {
        String start = startDate + " " + startTime + ":00";
        return Timestamp.valueOf(start);
    }

    /**
     * Converts strings to timestamps
     * @param endDate Date as String
     * @param endTime Time as String
     * @return Strings as a Timestamp
     */
    private Timestamp createEndTimeTimestamp(String endDate, String endTime) {
        String end = endDate + " " + endTime + ":00";
        return Timestamp.valueOf(end);
    }

    /**
     * Checks new appointment for overlapping start or end times with another appointment
     * @param customerID to check for other appointments
     * @param newAppointmentStart start time of the new appointment being added
     * @param newAppointmentEnd end time of the new appointment being added
     * @return true if there is a time conflict with another appointment
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
                hasOverlappingAppointment = true;
                overlappedAppointment = appointment;
            }
        }

        if (hasOverlappingAppointment) {
            presentHasOverlappedAppointment(overlappedAppointment);
        }

        return hasOverlappingAppointment;
    }

    /**
     * Presents onscreen message if the appointment being added conflicts with another appointment
     * @param overlappedAppointment the appointment that the new appointment conflicts with
     */
    private void presentHasOverlappedAppointment(Appointment overlappedAppointment) {
        Alert hasOverlappingAppointmentAlert = new Alert(Alert.AlertType.ERROR);
        hasOverlappingAppointmentAlert.setTitle("Application Message");
        hasOverlappingAppointmentAlert.setHeaderText("Appointment conflict");
        hasOverlappingAppointmentAlert.setContentText("This appointment conflicts with Appointment_ID: " + overlappedAppointment.getAppointmentID().toString());
        hasOverlappingAppointmentAlert.showAndWait();
    }

    /**
     * Checks to see if the appointment being added is in the past
     * @param newStart start time of new appointment being added
     * @return true if appointment is being added before LocalDateTime.now()
     */
    private boolean checkIfAppointmentDateIsInPast(Timestamp newStart) {
        boolean isInPast = newStart.before(Timestamp.valueOf(LocalDateTime.now()));
        if (isInPast) {
            presentAppointmentStartIsInPast();
        }
        return isInPast;
    }

    /**
     * Presents onscreen message if appointment is in the past
     */
    private void presentAppointmentStartIsInPast() {
        Alert appointmentInPastAlert = new Alert(Alert.AlertType.ERROR);
        appointmentInPastAlert.setTitle("Application Message");
        appointmentInPastAlert.setHeaderText("Appointment error");
        appointmentInPastAlert.setContentText("Appointment start is in the past.");
        appointmentInPastAlert.showAndWait();
    }


    /**
     * Checks for empty fields, overlapping appointments, and possible past appointment
     * @return true if the save was successful
     */
    private boolean saveNewAppointment() {
        boolean saveSuccessful = false;
        boolean overlappingAppointment;
        boolean appointmentInPast;
        boolean hasEmptyField;
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

        hasEmptyField = checkForEmptyFields();

        if (!hasEmptyField) {
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
        }





        return saveSuccessful;
    }

    /**
     * Returns user to main screen
     * @param event Button clicked event
     */
    public void cancel(ActionEvent event) {
        sceneController.setScene(event, "Main.fxml");
    }

    /**
     * Attempts to save new appointment, returns user to main screen if save is successful
     * @param event Button clicked event
     */
    public void save(ActionEvent event) {

        if (saveNewAppointment()) {
            sceneController.setScene(event, "Main.fxml");
        } else {
            System.out.println("Save unsuccessful");
        }



    }

}
