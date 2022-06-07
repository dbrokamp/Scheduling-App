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

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM customers";
        PreparedStatement selectAllCustomers = connection.prepareStatement(sql);
        ResultSet allCustomers = selectAllCustomers.executeQuery();

        while (allCustomers.next()) {
            Customer customer = new Customer(Integer.parseInt(allCustomers.getObject(1).toString()),
                                                allCustomers.getObject(2).toString(),
                                                allCustomers.getObject(3).toString(),
                                                allCustomers.getObject(4).toString(),
                                                allCustomers.getObject(5).toString(),
                                                Integer.parseInt(allCustomers.getObject(10).toString()));
            customers.add(customer);
        }

        return customers;
    }
}
