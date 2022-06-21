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
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ModifyAppointmentController implements Initializable {

    private SceneController sceneController = SceneController.getSceneControllerInstance();
    private Appointment appointmentToModify = MainController.getSelectedAppointment();
    private ObservableList<String> appointmentTimes = FXCollections.observableArrayList();
    private ObservableList<Integer> customerIDs = FXCollections.observableArrayList();
    private ObservableList<Integer> userIDs = FXCollections.observableArrayList();
    private ObservableList<String> contactNames = FXCollections.observableArrayList();

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

    private void createAppointmentTimes() {
        LocalTime businessHoursStart = LocalTime.of(8,00);
        LocalTime businessHoursEnd = LocalTime.of(22,00);
        Duration quarterHour = Duration.ofMinutes(15);

        LocalTime appointment = businessHoursStart;

        appointmentTimes.clear();
        while (appointment.isBefore(businessHoursEnd)) {
            appointmentTimes.add(appointment.toString());
            appointment = appointment.plus(quarterHour);
        }

        appointmentTimes.add(businessHoursEnd.toString());
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

    private void getUserIDs() {
        for (User user : DBUsers.getUsers()) {
            userIDs.add(user.getUserID());
        }

    }

    private void getContactNames() {
        for (Contact contact : DBContacts.getContacts()) {
            contactNames.add(contact.getContactName());
        }
    }

    private void getAndSetStartDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String startDateFormatted = dateFormat.format(appointmentToModify.getStart());
        String[] startString = startDateFormatted.split("\\s+");
        LocalDate startDate = LocalDate.parse(startString[0]);
        startDatePicker.setValue(startDate);
        startTimeComboBox.setValue(startString[1]);
    }

    private void getAndSetEndDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String endDateFormatted = dateFormat.format(appointmentToModify.getEnd());
        String[] endString = endDateFormatted.split("\\s+");
        LocalDate endDate = LocalDate.parse(endString[0]);
        endDatePicker.setValue(endDate);
        endTimeComboBox.setValue(endString[1])  ;
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

    private Timestamp createTimeStampFromDateAndTimeStrings(String date, String time) {
        String timestampString = date + " " + time + ":00";
        return Timestamp.valueOf(timestampString);
    }

    private void checkStartDateAndTimeFieldForChange() {
        Timestamp start = createTimeStampFromDateAndTimeStrings(startDatePicker.getValue().toString(), startTimeComboBox.getValue());
        if (appointmentToModify.getStart().equals(start)) {
            System.out.println("No changes to start fields");
        } else {
            try {
                DBAppointments.updateAppointmentStart(start, appointmentToModify.getAppointmentID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkEndDateAndTimeFieldForChange() {
        Timestamp end = createTimeStampFromDateAndTimeStrings(endDatePicker.getValue().toString(), endTimeComboBox.getValue());
        if (appointmentToModify.getEnd().equals(end)) {
            System.out.println("No changes to end fields");
        } else {
            try {
                DBAppointments.updateAppointmentEnd(end, appointmentToModify.getAppointmentID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

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



    public void cancelActionButton(ActionEvent event) {
        sceneController.setScene(event, "Main.fxml");
    }

    public void saveActionButton(ActionEvent event) {
        checkTitleFieldForChange();
        checkDescriptionFieldForChange();
        checkLocationFieldForChange();
        checkTypeFieldForChange();
        checkStartDateAndTimeFieldForChange();
        checkEndDateAndTimeFieldForChange();
        checkCustomerIDFieldForChange();
        checkUserIDFieldForChange();

        sceneController.setScene(event,"Main.fxml");
    }
}
