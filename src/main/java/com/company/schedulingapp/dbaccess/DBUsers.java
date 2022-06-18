package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.User;
import com.company.schedulingapp.util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsers {

    private static String currentUser;
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();

    private static void getAllUsersFromDataBase() throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM Users";
        PreparedStatement getAllUsersFromDatabaseStatement = connection.prepareStatement(sql);
        ResultSet allUsersSet = getAllUsersFromDatabaseStatement.executeQuery();

        while (allUsersSet.next()) {
            User user = new User(allUsersSet.getInt("User_ID"),
                                allUsersSet.getString("User_Name"));
            allUsers.add(user);
        }
    }

    public static ObservableList<User> getAllUsers() {
        if (allUsers.isEmpty()) {
            try {
                getAllUsersFromDataBase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return allUsers;
    }




    public static boolean verifyUsername(String username) throws SQLException {


        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM users WHERE User_Name = ?";
        PreparedStatement userWithUsername = connection.prepareStatement(sql);
        userWithUsername.setString(1, username);
        ResultSet user = userWithUsername.executeQuery();

        if (user.next()) {
            currentUser = user.getString("User_Name");
            return true;
        } else {
            return false;
        }

    }
    public static boolean verifyPassword(String password) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM users WHERE Password = ?";
        PreparedStatement userWithPassword = connection.prepareStatement(sql);
        userWithPassword.setString(1, password);
        ResultSet user = userWithPassword.executeQuery();
        return user.next();
    }

    public static String getCurrentUserName() {
        return currentUser;
    }

}
