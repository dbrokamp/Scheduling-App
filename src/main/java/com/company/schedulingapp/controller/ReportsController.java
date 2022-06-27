package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBAppointments;
import com.company.schedulingapp.dbaccess.DBContacts;
import com.company.schedulingapp.dbaccess.DBUsers;
import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.model.Contact;

import com.company.schedulingapp.util.SceneController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * Creates required reports plus one custom report
 */
public class ReportsController implements Initializable {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    Map<Month, Integer> totalsByMonth = new HashMap<>();
    Map<String, Integer> totalsByType = new HashMap<>();
    Map<String, Integer> totalsByUser = new HashMap<>();

    @FXML TableView<Map.Entry<Month, Integer>> appointmentsByMonth = new TableView<>();
    @FXML TableColumn<Map.Entry<Month, Integer>, Month> monthColumn = new TableColumn<>("Month");
    @FXML TableColumn<Map.Entry<Month, Integer>, Integer> monthCountColumn = new TableColumn<>("Count");

    @FXML TableView<Map.Entry<String, Integer>> appointmentsByType = new TableView<>();
    @FXML TableColumn<Map.Entry<String, Integer>, String> typeColumn = new TableColumn<>("Type");
    @FXML TableColumn<Map.Entry<String, Integer>, Integer> typeCountColumn = new TableColumn<>("Count");

    @FXML TableView<Map.Entry<String, Integer>> appointmentsByUser = new TableView<>();
    @FXML TableColumn<Map.Entry<String, Integer>, String> userColumn = new TableColumn<>("User");
    @FXML TableColumn<Map.Entry<String, Integer>, Integer> userCountColumn = new TableColumn<>("Count");

    @FXML ComboBox<String> contactComboBox;
    @FXML TableView<Appointment> contactAppointmentTableView = new TableView<>();
    @FXML TableColumn<Appointment, Integer> contactAppointmentIDColumn = new TableColumn<>("Appointment_ID");
    @FXML TableColumn<Appointment, String> contactAppointmentTitleColumn = new TableColumn<>("Title");
    @FXML TableColumn<Appointment, String> contactAppointmentDescriptionColumn = new TableColumn<>("Description");
    @FXML TableColumn<Appointment, String> contactAppointmentTypeColumn = new TableColumn<>("Type");
    @FXML TableColumn<Appointment, Date> contactAppointmentStartColumn = new TableColumn<>("Start");
    @FXML TableColumn<Appointment, Date> contactAppointmentEndColumn = new TableColumn<>("End");
    @FXML TableColumn<Appointment, Integer> contactAppointmentCustomerIDColumn = new TableColumn<>("Customer_ID");

    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAllAppointments();
        getAppointmentCountByMonth();
        getAppointmentCountByType();
        try {
            getAppointmentCountByUser();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        populateByMonthTable();
        populateByTypeTable();
        populateByUserTable();
        populateContactComboBox();
        setupAppointmentTable();
        addSelectionListenerToContactComboBox();

    }

    private void getAllAppointments() {
        try {
            appointments = DBAppointments.getAllAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets number of appointments by month
     */
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

    /**
     * Gets number of appointments by appointment type
     */
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

    /**
     * Customer report - gets number of appointments by user
     * @throws SQLException error for SQL query
     */
    private void getAppointmentCountByUser() throws SQLException {
        for (Appointment appointment : appointments) {
            if (totalsByUser.containsKey(DBUsers.getUserNameFromID(appointment.getUserID()))) {
                Integer count = totalsByUser.get(DBUsers.getUserNameFromID(appointment.getUserID()));
                count++;
                totalsByUser.put(DBUsers.getUserNameFromID(appointment.getUserID()), count);
            } else {
                totalsByUser.put(DBUsers.getUserNameFromID(appointment.getUserID()), 1);
            }
        }
    }

    private void populateByMonthTable() {
        monthColumn.setCellValueFactory(entryMonthCellDataFeatures -> new SimpleObjectProperty<>(entryMonthCellDataFeatures.getValue().getKey()));
        monthCountColumn.setCellValueFactory(entryIntegerCellDataFeatures -> new SimpleObjectProperty<>(entryIntegerCellDataFeatures.getValue().getValue()));
        ObservableList<Map.Entry<Month, Integer>> items = FXCollections.observableArrayList(totalsByMonth.entrySet());
        appointmentsByMonth.setItems(items);
    }

    private void populateByTypeTable() {
        typeColumn.setCellValueFactory(entryStringCellDataFeatures -> new SimpleObjectProperty<>(entryStringCellDataFeatures.getValue().getKey()));
        typeCountColumn.setCellValueFactory(entryIntegerCellDataFeatures -> new SimpleObjectProperty<>(entryIntegerCellDataFeatures.getValue().getValue()));
        ObservableList<Map.Entry<String, Integer>> items = FXCollections.observableArrayList(totalsByType.entrySet());
        appointmentsByType.setItems(items);
    }

    private void populateByUserTable() {
        userColumn.setCellValueFactory(entryStringCellDataFeatures -> new SimpleStringProperty(entryStringCellDataFeatures.getValue().getKey()));
        userCountColumn.setCellValueFactory(entryIntegerCellDataFeatures -> new SimpleObjectProperty<>(entryIntegerCellDataFeatures.getValue().getValue()));
        ObservableList<Map.Entry<String, Integer>> items = FXCollections.observableArrayList(totalsByUser.entrySet());
        appointmentsByUser.setItems(items);
    }

    private void setupAppointmentTable() {
        contactAppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        contactAppointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contactAppointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        contactAppointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactAppointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        contactAppointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        contactAppointmentCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    }

    private void populateContactComboBox() {
        ObservableList<String> contactNames = FXCollections.observableArrayList();

        for (Contact contact : DBContacts.getContacts()) {
            contactNames.add(contact.getContactName());
        }

        contactComboBox.setItems(contactNames);
    }

    /**
     * Adds selection listener for contact combo box and updates appointment table view with selection
     */
    private void addSelectionListenerToContactComboBox() {
        contactComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                getAppointmentsForContact(newValue);
            }
        });
    }

    private void getAppointmentsForContact(String contactName) {
        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : appointments) {
            if (appointment.getContactID().equals(DBContacts.getContactIDFromContactName(contactName))) {
                contactAppointments.add(appointment);
            }
        }

        contactAppointmentTableView.setItems(contactAppointments);

    }


    public void goToMain(ActionEvent event) {
        sceneController.setScene(event, "Main.fxml");
    }
}
