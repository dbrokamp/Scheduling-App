package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.util.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsers {

    public static boolean verifyUsername(String username) throws SQLException {


        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM users WHERE User_Name = ?";
        PreparedStatement userWithUsername = connection.prepareStatement(sql);
        userWithUsername.setString(1, username);
        ResultSet user = userWithUsername.executeQuery();
        return user.next();

    }
    public static boolean verifyPassword(String password) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM users WHERE Password = ?";
        PreparedStatement userWithPassword = connection.prepareStatement(sql);
        userWithPassword.setString(1, password);
        ResultSet user = userWithPassword.executeQuery();
        return user.next();
    }


}
