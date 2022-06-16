package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.model.Customer;
import com.company.schedulingapp.util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;


import java.sql.*;
import java.time.LocalDateTime;

public class DBCustomers {

    private static ObservableList<Customer> customers = FXCollections.observableArrayList();

    private static void getCustomers() throws SQLException {


        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM customers";
        PreparedStatement selectAllCustomers = connection.prepareStatement(sql);
        ResultSet allCustomers = selectAllCustomers.executeQuery();

        while (allCustomers.next()) {
            Customer customer = new Customer(allCustomers.getInt("Customer_ID"),
                                                allCustomers.getString("Customer_Name"),
                                                allCustomers.getString("Address"),
                                                allCustomers.getString("Postal_Code"),
                                                allCustomers.getString("Phone"),
                                                allCustomers.getInt("Division_ID"));
            customers.add(customer);
        }

    }

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        if (customers.isEmpty()) {
            getCustomers();
        } else {
            customers.clear();
            getCustomers();
        }
        return customers;
    }

    private static Integer createNewCustomerID() throws SQLException {
        if (customers.isEmpty()) {
            getCustomers();
        }

        Customer currentLastCustomer = customers.get(customers.size() - 1);

        return currentLastCustomer.getCustomerID() + 1;
    }

    public static void addNewCustomer(String newCustomerName, String newCustomerAddress, String newCustomerPostalCode, String newCustomerPhone, String firstLevelDivisionName) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "INSERT INTO Customers VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement newCustomerStatement = connection.prepareStatement(sql);
        newCustomerStatement.setInt(1, createNewCustomerID());
        newCustomerStatement.setString(2, newCustomerName);
        newCustomerStatement.setString(3, newCustomerAddress);
        newCustomerStatement.setString(4, newCustomerPostalCode);
        newCustomerStatement.setString(5, newCustomerPhone);
        newCustomerStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now())); // Create Date
        newCustomerStatement.setString(7, DBUsers.getCurrentUserName()); // Created_By
        newCustomerStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now())); // Last updated
        newCustomerStatement.setString(9, DBUsers.getCurrentUserName()); // Last updated by
        newCustomerStatement.setInt(10, DBFirstLevelDivisions.getDivisionID(firstLevelDivisionName));
        newCustomerStatement.executeUpdate();
    }

    public static void deleteCustomerAction(Integer customerID) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            appointments = DBAppointments.getCustomerAppointments(customerID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (appointments.isEmpty()) {
            deleteCustomerFromDatabase(customerID);
        } else {
            presentUnableToDeleteCustomerAlert();
        }

    }

    private static void deleteCustomerFromDatabase(Integer customerID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement deleteCustomerStatement = connection.prepareStatement(sql);
        deleteCustomerStatement.setInt(1, customerID);
        deleteCustomerStatement.executeUpdate();
    }

    private static void presentUnableToDeleteCustomerAlert() {
        Alert unableToDeleteCustomerAlert = new Alert(Alert.AlertType.ERROR);
        unableToDeleteCustomerAlert.setTitle("Database Message");
        unableToDeleteCustomerAlert.setHeaderText("Failed");
        unableToDeleteCustomerAlert.setContentText("All customer appointments must be deleted before the customer can be deleted.");
        unableToDeleteCustomerAlert.showAndWait();
    }


}
