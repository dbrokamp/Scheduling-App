package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.Customer;
import com.company.schedulingapp.util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        }
        return customers;
    }

    public static Integer createNewCustomerID() throws SQLException {
        if (customers.isEmpty()) {
            getCustomers();
        }

        Customer currentLastCustomer = customers.get(customers.size() - 1);

        return currentLastCustomer.getCustomerID() + 1;
    }
}
