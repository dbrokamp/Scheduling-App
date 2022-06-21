package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBAppointments;
import com.company.schedulingapp.dbaccess.DBCustomers;
import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.model.Customer;
import com.company.schedulingapp.util.JDBC;
import com.company.schedulingapp.util.SceneController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
    @FXML ComboBox<String> monthComboBox;
    @FXML ComboBox<String> weekComboBox;
    @FXML ToggleGroup appointmentFilterRadioButtons = new ToggleGroup();


    private static Customer selectedCustomer;
    private static Appointment selectedAppointment;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCustomerTable();
        setupAppointmentTable();
        addSelectionListenerToCustomerTable();
        addSelectionListenerToAppointmentTable();
        addSelectionListenerToFilterByToggleGroup();

        monthComboBox.setVisible(false);
        weekComboBox.setVisible(false);

        filterByMonthRadioButton.setToggleGroup(appointmentFilterRadioButtons);
        filterByWeekRadioButton.setToggleGroup(appointmentFilterRadioButtons);


    }

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

    private void addSelectionListenerToFilterByToggleGroup() {
        appointmentFilterRadioButtons.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle old_toggle, Toggle new_toggle) {
                if (appointmentFilterRadioButtons.getSelectedToggle() != null) {
                    if (appointmentFilterRadioButtons.getSelectedToggle() == filterByMonthRadioButton) {
                        monthComboBox.setVisible(true);
                        weekComboBox.setVisible(false);

                    } else {
                        monthComboBox.setVisible(false);
                        weekComboBox.setVisible(true);
                    }
                }
            }
        });
    }

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

    private void addSelectionListenerToAppointmentTable() {
        appointmentTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedAppointment = newSelection;
                System.out.println(selectedAppointment);
            }
        });
    }

    public void deleteAppointmentAction() {
        if (selectedAppointment == null) {
            presentNoAppointmentSelectedAlert();
        } else {
            try {
                DBAppointments.deleteAppointmentAction(selectedAppointment.getAppointmentID());
                presentAppointmentDeletedAlert();
                appointmentTableView.setItems(DBAppointments.getCustomerAppointments(selectedCustomer.getCustomerID()));

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

    public void goToAddAppointment(ActionEvent event) { sceneController.setScene(event, "AddAppointment.fxml");}

    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public static Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

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
