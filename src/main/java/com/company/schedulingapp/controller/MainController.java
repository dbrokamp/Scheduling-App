package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBAppointments;
import com.company.schedulingapp.dbaccess.DBCustomers;

import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.model.Customer;
import com.company.schedulingapp.util.JDBC;
import com.company.schedulingapp.util.SceneController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;

import java.time.LocalDate;

import java.util.ResourceBundle;


/**
 * Sets up tables for customers and appointments. Allows users to move to scene to add or update customers or appointments
 * Allows for deletion of customers or appointments
 */
public class MainController implements Initializable {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    @FXML TableView<Customer> customerTableView = new TableView<>();
    @FXML TableColumn<Customer, Integer> customerIDColumn = new TableColumn<>("ID");
    @FXML TableColumn<Customer, String> customerNameColumn = new TableColumn<>("Name");
    @FXML TableColumn<Customer, String> customerAddressColumn = new TableColumn<>("Address");
    @FXML TableColumn<Customer, String> customerPostalCodeColumn = new TableColumn<>("Postal Code");
    @FXML TableColumn<Customer, String> customerPhoneColumn = new TableColumn<>("Phone");
    @FXML TableColumn<Customer, Integer> customerDivisionIDColumn = new TableColumn<>("Division ID");

    @FXML TableView<Appointment> appointmentTableView = new TableView<>();
    @FXML TableColumn<Appointment, Integer> appointmentIDColumn = new TableColumn<>("Appointment_ID");
    @FXML TableColumn<Appointment, String> appointmentTitleColumn = new TableColumn<>("Title");
    @FXML TableColumn<Appointment, String> appointmentDescriptionColumn = new TableColumn<>("Description");
    @FXML TableColumn<Appointment, String> appointmentLocationColumn = new TableColumn<>("Location");
    @FXML TableColumn<Appointment, String> appointmentTypeColumn = new TableColumn<>("Type");
    @FXML TableColumn<Appointment, Date> appointmentStartColumn = new TableColumn<>("Start");
    @FXML TableColumn<Appointment, Date> appointmentEndColumn = new TableColumn<>("End");
    @FXML TableColumn<Appointment, Integer> appointmentCustomerIDColumn = new TableColumn<>("Customer_ID");
    @FXML TableColumn<Appointment, Integer> appointmentUserIDColumn = new TableColumn<>("User_ID");
    @FXML TableColumn<Appointment, Integer> appointmentContactIDColumn = new TableColumn<>("Contact_ID");
    @FXML RadioButton filterByMonthRadioButton;
    @FXML RadioButton filterByWeekRadioButton;
    @FXML ToggleGroup appointmentFilterRadioButtons = new ToggleGroup();

    private static Customer selectedCustomer;
    private static Appointment selectedAppointment;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCustomerTable();
        setupAppointmentTable();
        addSelectionListenerToCustomerTable();
        addSelectionListenerToAppointmentTable();
        addSelectionListenerToFilterByToggleGroup();

        loadAllAppointmentsIntoAppointmentTable();

        filterByMonthRadioButton.setToggleGroup(appointmentFilterRadioButtons);
        filterByWeekRadioButton.setToggleGroup(appointmentFilterRadioButtons);


    }

    /**
     * Sets up columns for customer table
     */
    private void setupCustomerTable() {
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerDivisionIDColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

        try {
            customerTableView.setItems(DBCustomers.getAllCustomers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds listener to customer table for selection and if one is selected, updates the appointment table with that customer's
     * appointments.
     */
    private void addSelectionListenerToCustomerTable() {
        customerTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    selectedCustomer = newSelection;
                    appointmentTableView.setItems(DBAppointments.getCustomerAppointments(selectedCustomer.getCustomerID()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Adds listener to toggle group in order to present appointments from current month or week
     */
    private void addSelectionListenerToFilterByToggleGroup() {
        appointmentFilterRadioButtons.selectedToggleProperty().addListener((observableValue, old_toggle, new_toggle) -> {
            if (appointmentFilterRadioButtons.getSelectedToggle() != null) {
                if (appointmentFilterRadioButtons.getSelectedToggle() == filterByMonthRadioButton) {
                    filterByMonth();
                } else if (appointmentFilterRadioButtons.getSelectedToggle() == filterByWeekRadioButton) {
                    filterByWeek();
                }
            }
        });
    }

    /**
     * Filters appointment table view to show only appointments from current month
     */
    private void filterByMonth() {
        LocalDate localDate = LocalDate.now();
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        try {
            allAppointments = DBAppointments.getAllAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FilteredList<Appointment> filteredList = new FilteredList<>(allAppointments);
        filteredList.setPredicate(row -> {

            LocalDate rowDate = LocalDate.from(row.getStart().toLocalDateTime());

            return rowDate.isEqual(localDate.withDayOfMonth(1)) || rowDate.isAfter(localDate.withDayOfMonth(1)) && rowDate.isBefore(localDate.withDayOfMonth(localDate.getMonth().length(localDate.isLeapYear()))) || rowDate.isEqual(localDate.withDayOfMonth(localDate.getMonth().length(localDate.isLeapYear())));

        });

        appointmentTableView.setItems(filteredList);
    }

    /**
     * Filters appointment table view to show only appointments from current week
     * Lambda expression used to filter list and table based on date of appointment
     */
    private void filterByWeek() {
        LocalDate localDate = LocalDate.now();
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        LocalDate week = localDate.plusWeeks(1);

        try {
            allAppointments = DBAppointments.getAllAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FilteredList<Appointment> filteredList = new FilteredList<>(allAppointments);
        filteredList.setPredicate(row -> {

            LocalDate rowDate = LocalDate.from(row.getStart().toLocalDateTime());

            return rowDate.isAfter(localDate.minusDays(1)) && rowDate.isBefore(week);
        });

        appointmentTableView.setItems(filteredList);
    }


    /**
     * Sets up columns for appointment table
     */
    private void setupAppointmentTable() {
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appointmentUserIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        appointmentContactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
    }

    /**
     * Allows for all appointments from all customers to be loaded into the appointment table
     */
    private void loadAllAppointmentsIntoAppointmentTable() {
        try {
            appointmentTableView.setItems(DBAppointments.getAllAppointments());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add listener for selection in appointment table
     * Lambda expression used to efficiently detect selections in appointment table
     */
    private void addSelectionListenerToAppointmentTable() {
        appointmentTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedAppointment = newSelection;
            }
        });
    }

    /**
     * Checks that an appointment has been selected and deletes it from the database
     */
    public void deleteAppointmentAction() {
        if (selectedAppointment == null) {
            presentNoAppointmentSelectedAlert();
        } else {
            try {
                DBAppointments.deleteAppointmentAction(selectedAppointment.getAppointmentID());
                presentAppointmentDeletedAlert();
                loadAllAppointmentsIntoAppointmentTable();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void presentNoAppointmentSelectedAlert() {
        Alert noAppointmentSelectedAlert = new Alert(Alert.AlertType.ERROR);
        noAppointmentSelectedAlert.setTitle("Application Message");
        noAppointmentSelectedAlert.setHeaderText("Failed - No selection");
        noAppointmentSelectedAlert.setContentText("Please select an appointment");
        noAppointmentSelectedAlert.showAndWait();
    }

    private void presentAppointmentDeletedAlert() {
        Alert appointmentDeletedAlert = new Alert(Alert.AlertType.INFORMATION);
        appointmentDeletedAlert.setTitle("Application Message");
        appointmentDeletedAlert.setHeaderText(selectedAppointment.getAppointmentID() + " - " + selectedAppointment.getType());
        appointmentDeletedAlert.setContentText("Successfully deleted");
        appointmentDeletedAlert.showAndWait();
    }


    private void presentNoCustomerSelectedAlert() {
        Alert noCustomerSelectedAlert = new Alert(Alert.AlertType.ERROR);
        noCustomerSelectedAlert.setTitle("Application Message");
        noCustomerSelectedAlert.setHeaderText("Failed - No Selection");
        noCustomerSelectedAlert.setContentText("Please select a customer");
        noCustomerSelectedAlert.showAndWait();
    }

    private void setSelectedCustomer() {
        selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
    }

    public void goToModifyCustomer(ActionEvent event) {
        setSelectedCustomer();
        if (selectedCustomer == null) {
            presentNoCustomerSelectedAlert();
        } else {
            sceneController.setScene(event, "ModifyCustomer.fxml");
        }

    }

    /**
     * Deletes a customer from the database
     */
    public void deleteCustomerActionButton() {
        setSelectedCustomer();

        if (selectedCustomer == null) {
            presentNoCustomerSelectedAlert();
        } else {
            try {
                DBCustomers.deleteCustomerAction(selectedCustomer.getCustomerID());
                setupCustomerTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void goToAddCustomer(ActionEvent event) { sceneController.setScene(event, "AddCustomer.fxml"); }

    public void clearAppointmentFilter() { loadAllAppointmentsIntoAppointmentTable(); }

    public void goToAddAppointment(ActionEvent event) { sceneController.setScene(event, "AddAppointment.fxml");}

    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public static Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    public void goToReports(ActionEvent event) { sceneController.setScene(event,"Reports.fxml");}


    public void goToModifyAppointment(ActionEvent event) {
        if (selectedAppointment == null) {
            presentNoAppointmentSelectedAlert();
        } else {
            sceneController.setScene(event, "ModifyAppointment.fxml");
        }
    }

    public void exitApplicationButtonAction() {
        JDBC.closeConnection();
        Platform.exit();
    }


}
