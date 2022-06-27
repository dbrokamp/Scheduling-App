package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.model.Customer;
import com.company.schedulingapp.util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;


import java.sql.*;
import java.time.LocalDateTime;

/**
 * Allows for access and search of customers table in database
 */
public class DBCustomers {

    final private static ObservableList<Customer> customers = FXCollections.observableArrayList();

    /**
     * Gets all customers from database
     * @throws SQLException SQL error
     */
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

    /** Public getter for customers list
     * @return list of all customers
     * @throws SQLException SQL error
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        if (customers.isEmpty()) {
            getCustomers();
        } else {
            customers.clear();
            getCustomers();
        }
        return customers;
    }

    /**
     * Creates a new customer id based on last customer in list
     * @return new integer for customer id
     * @throws SQLException SQL error
     */
    private static Integer createNewCustomerID() throws SQLException {
        if (customers.isEmpty()) {
            getCustomers();
        }

        Customer currentLastCustomer = customers.get(customers.size() - 1);

        return currentLastCustomer.getCustomerID() + 1;
    }

    /**
     * Adds a new customer to the database
     *
     * @param newCustomerName String for new name
     * @param newCustomerAddress String for new address
     * @param newCustomerPostalCode String for new postal code
     * @param newCustomerPhone String for new phone
     * @param firstLevelDivisionName String for new first level division
     * @throws SQLException SQL error
     */
    public static void addNewCustomer(String newCustomerName, String newCustomerAddress, String newCustomerPostalCode, String newCustomerPhone, String firstLevelDivisionName) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "INSERT INTO customers VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

    /**
     * Public method that deletes a customer from the database
     *
     * @param customerID customer id of customer to delete
     * @throws SQLException SQL error
     */
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

    /**
     * Deletes directly from database
     * @param customerID customer id of customer to delete
     * @throws SQLException SQL Error
     */
    private static void deleteCustomerFromDatabase(Integer customerID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement deleteCustomerStatement = connection.prepareStatement(sql);
        deleteCustomerStatement.setInt(1, customerID);
        deleteCustomerStatement.executeUpdate();
        presentCustomerDeletedAlert();
    }

    private static void presentCustomerDeletedAlert() {
        Alert customerDeletedAlert = new Alert(Alert.AlertType.INFORMATION);
        customerDeletedAlert.setTitle("Database Message");
        customerDeletedAlert.setHeaderText("Success");
        customerDeletedAlert.setContentText("Customer successfully deleted.");
        customerDeletedAlert.showAndWait();
    }

    private static void presentUnableToDeleteCustomerAlert() {
        Alert unableToDeleteCustomerAlert = new Alert(Alert.AlertType.ERROR);
        unableToDeleteCustomerAlert.setTitle("Database Message");
        unableToDeleteCustomerAlert.setHeaderText("Failed");
        unableToDeleteCustomerAlert.setContentText("All customer appointments must be deleted before the customer can be deleted.");
        unableToDeleteCustomerAlert.showAndWait();
    }

    /**
     * Allows for name field to be updated in database
     * @param newCustomerName new name
     * @param customerID customer to be updated
     * @throws SQLException SQL error
     */
    public static void updateCustomerName(String newCustomerName, Integer customerID) throws  SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE customers SET Customer_Name = ? WHERE Customer_ID = ?";
        PreparedStatement updateCustomerStatement = connection.prepareStatement(sql);
        updateCustomerStatement.setString(1, newCustomerName);
        updateCustomerStatement.setInt(2, customerID);
        updateCustomerStatement.executeUpdate();
        updateLastUpdatedTime(customerID);
        updateLastUpdatedByUser(customerID);
    }

    /**
     * Allows for address field to be updated in database
     * @param newCustomerAddress new address
     * @param customerID customer to be updated
     * @throws SQLException SQL error
     */
    public static void updateCustomerAddress(String newCustomerAddress, Integer customerID) throws  SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE customers SET Address = ? WHERE Customer_ID = ?";
        PreparedStatement updateCustomerStatement = connection.prepareStatement(sql);
        updateCustomerStatement.setString(1, newCustomerAddress);
        updateCustomerStatement.setInt(2, customerID);
        updateCustomerStatement.executeUpdate();
        updateLastUpdatedTime(customerID);
        updateLastUpdatedByUser(customerID);
    }

    /**
     * Allows postal code to be updated in database
     * @param newCustomerPostalCode new postal code
     * @param customerID customer to be updated
     * @throws SQLException SQL error
     */
    public static void updateCustomerPostalCode(String newCustomerPostalCode, Integer customerID) throws  SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE customers SET Postal_Code = ? WHERE Customer_ID = ?";
        PreparedStatement updateCustomerStatement = connection.prepareStatement(sql);
        updateCustomerStatement.setString(1, newCustomerPostalCode);
        updateCustomerStatement.setInt(2, customerID);
        updateCustomerStatement.executeUpdate();
        updateLastUpdatedTime(customerID);
        updateLastUpdatedByUser(customerID);
    }

    /**
     * Allows phone number to be updated in database
     * @param newCustomerPhone new phone number
     * @param customerID customer to be updated
     * @throws SQLException SQL error
     */
    public static void updateCustomerPhone(String newCustomerPhone, Integer customerID) throws  SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE customers SET Phone = ? WHERE Customer_ID = ?";
        PreparedStatement updateCustomerStatement = connection.prepareStatement(sql);
        updateCustomerStatement.setString(1, newCustomerPhone);
        updateCustomerStatement.setInt(2, customerID);
        updateCustomerStatement.executeUpdate();
        updateLastUpdatedTime(customerID);
        updateLastUpdatedByUser(customerID);
    }

    /**
     * Allows for the divisionID to be updated in the database
     *
     * @param newDivisionID new division id
     * @param customerID customer to be updated
     * @throws SQLException SQL error
     */
    public static void updateCustomerDivisionID(Integer newDivisionID, Integer customerID) throws  SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE customers SET Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement updateCustomerStatement = connection.prepareStatement(sql);
        updateCustomerStatement.setInt(1, newDivisionID);
        updateCustomerStatement.setInt(2, customerID);
        updateCustomerStatement.executeUpdate();
        updateLastUpdatedTime(customerID);
        updateLastUpdatedByUser(customerID);
    }

    /**
     * Updates time of last_updated in database
     * @param customerID customer that was updated
     * @throws SQLException SQL error
     */
    private static void updateLastUpdatedTime(Integer customerID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE customers SET Last_Update = ? WHERE Customer_ID = ?";
        PreparedStatement updateCustomerStatement = connection.prepareStatement(sql);
        updateCustomerStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        updateCustomerStatement.setInt(2, customerID);
        updateCustomerStatement.executeUpdate();
    }

    /**
     * Updated last_updated_by in database
     * @param customerID customer that was updated
     * @throws SQLException SQL error
     */
    private static void updateLastUpdatedByUser(Integer customerID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE customers SET Last_Updated_By = ? WHERE Customer_ID = ?";
        PreparedStatement updateCustomerStatement = connection.prepareStatement(sql);
        updateCustomerStatement.setString(1, DBUsers.getCurrentUserName());
        updateCustomerStatement.setInt(2, customerID);
        updateCustomerStatement.executeUpdate();
    }

}
