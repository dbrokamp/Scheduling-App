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

    public ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM customers";
        PreparedStatement selectAllCustomers = connection.prepareStatement(sql);
        ResultSet allCustomers = selectAllCustomers.executeQuery();

        do {
            customers.add(new Customer(Integer.parseInt(allCustomers.getString("Customer_ID")),
                                allCustomers.getString("Customer_Name"),
                                allCustomers.getString("Address"),
                                allCustomers.getString("Postal_Code"),
                                allCustomers.getString("Phone"),
                                Integer.parseInt(allCustomers.getString("Division_ID"))));
        } while (allCustomers.next());
        return customers;
    }
}
