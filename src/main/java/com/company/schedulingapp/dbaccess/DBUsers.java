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
    private static Integer currentUserID;
    final private static ObservableList<User> users = FXCollections.observableArrayList();

    /**
     * Gets list of all users from database, does not retrieve passwords
     * @throws SQLException SQL error
     */
    private static void getAllUsersFromDataBase() throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM Users";
        PreparedStatement getAllUsersFromDatabaseStatement = connection.prepareStatement(sql);
        ResultSet allUsersSet = getAllUsersFromDatabaseStatement.executeQuery();

        while (allUsersSet.next()) {
            User user = new User(allUsersSet.getInt("User_ID"),
                                allUsersSet.getString("User_Name"));
            users.add(user);
        }
    }

    /**
     * Getter for users list
     * @return List of users
     */
    public static ObservableList<User> getUsers() {
        if (users.isEmpty()) {
            try {
                getAllUsersFromDataBase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }


    /**
     * Allows for searching of users table by userID
     * @param userID integer of userID to search the users table for
     * @return string of username
     * @throws SQLException SQL error
     */
    public static String getUserNameFromID(Integer userID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM users WHERE User_ID = ?";
        PreparedStatement getUserNameStatement = connection.prepareStatement(sql);
        getUserNameStatement.setInt(1,userID);
        ResultSet userNameByIDSet = getUserNameStatement.executeQuery();

        if (userNameByIDSet.next()) {
            return userNameByIDSet.getString("User_Name");
        } else {
            return null;
        }
    }


    /**
     * Verifies username is valid
     * @param username text from login textfield
     * @return true if username matches in list
     * @throws SQLException SQL error
     */
    public static boolean verifyUsername(String username) throws SQLException {


        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM users WHERE User_Name = ?";
        PreparedStatement userWithUsername = connection.prepareStatement(sql);
        userWithUsername.setString(1, username);
        ResultSet user = userWithUsername.executeQuery();

        if (user.next()) {
            currentUser = user.getString("User_Name");
            currentUserID = user.getInt("User_ID");
            return true;
        } else {
            return false;
        }

    }

    /**
     * Verifies password is valid
     * @param password password from password textfield
     * @return true if password is valid
     * @throws SQLException SQL error
     */
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

    public static Integer getCurrentUserID() { return currentUserID; }

}
