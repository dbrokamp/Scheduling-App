package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDateTime;


/**
 * Allows for access to appointments table in database
 */
public class DBAppointments {

    final private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    /**
     * Gets appointments for specified user id
     * @param userID user to get appointments for
     * @return list of appointments by user
     * @throws SQLException error with sql query
     */
    public static ObservableList<Appointment> getUserAppointments(Integer userID) throws SQLException {
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM appointments where User_ID = ?";
        PreparedStatement customerAppointmentsStatement = connection.prepareStatement(sql);
        customerAppointmentsStatement.setInt(1, userID);
        ResultSet appointmentsSet = customerAppointmentsStatement.executeQuery();

        while (appointmentsSet.next()) {

            Appointment appointment = new Appointment(appointmentsSet.getInt("Appointment_ID"),
                    appointmentsSet.getString("Title"),
                    appointmentsSet.getString("Description"),
                    appointmentsSet.getString("Location"),
                    appointmentsSet.getString("Type"),
                    appointmentsSet.getTimestamp("Start"),
                    appointmentsSet.getTimestamp("End"),
                    appointmentsSet.getInt("Customer_ID"),
                    appointmentsSet.getInt("User_ID"),
                    appointmentsSet.getInt("Contact_ID"));

            userAppointments.add(appointment);
        }

        return userAppointments;


    }

    /**
     * Gets appointment list for specified customer
     * @param customerID customer id to get appointments for
     * @return list of customer's appointments
     * @throws SQLException sql query error
     */
    public static ObservableList<Appointment> getCustomerAppointments(Integer customerID) throws SQLException {
        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM appointments where Customer_ID = ?";
        PreparedStatement customerAppointmentsStatement = connection.prepareStatement(sql);
        customerAppointmentsStatement.setString(1, customerID.toString());
        ResultSet appointmentsSet = customerAppointmentsStatement.executeQuery();

        while (appointmentsSet.next()) {

            Appointment appointment = new Appointment(appointmentsSet.getInt("Appointment_ID"),
                                                       appointmentsSet.getString("Title"),
                                                        appointmentsSet.getString("Description"),
                                                        appointmentsSet.getString("Location"),
                                                        appointmentsSet.getString("Type"),
                                                        appointmentsSet.getTimestamp("Start"),
                                                        appointmentsSet.getTimestamp("End"),
                                                        appointmentsSet.getInt("Customer_ID"),
                                                        appointmentsSet.getInt("User_ID"),
                                                        appointmentsSet.getInt("Contact_ID"));

            customerAppointments.add(appointment);
        }

        return customerAppointments;
    }

    /**
     * Gets all appointments from database
     *
     * @throws SQLException sql query error
     */
    private static void getAllAppointmentsFromDatabase() throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM appointments";
        PreparedStatement allAppointmentsStatement = connection.prepareStatement(sql);
        ResultSet allAppointmentsSet = allAppointmentsStatement.executeQuery();

        while (allAppointmentsSet.next()) {
            Appointment appointment = new Appointment(allAppointmentsSet.getInt("Appointment_ID"),
                    allAppointmentsSet.getString("Title"),
                    allAppointmentsSet.getString("Description"),
                    allAppointmentsSet.getString("Location"),
                    allAppointmentsSet.getString("Type"),
                    allAppointmentsSet.getTimestamp("Start"),
                    allAppointmentsSet.getTimestamp("End"),
                    allAppointmentsSet.getInt("Customer_ID"),
                    allAppointmentsSet.getInt("User_ID"),
                    allAppointmentsSet.getInt("Contact_ID"));

            allAppointments.add(appointment);
        }
    }

    /**
     * Gets last appointmentID Integer and increases it by one
     * @return Integer of new appointment id
     * @throws SQLException error in sql query
     */
    private static Integer createNewAppointmentID() throws SQLException {
        if (allAppointments.isEmpty()) {
            getAllAppointmentsFromDatabase();
        }

        Appointment currentLastAppointment = allAppointments.get(allAppointments.size() - 1);

        return currentLastAppointment.getAppointmentID() + 1;
    }

    /**
     * Getter for allAppointments
     * @return list of all appointments
     * @throws SQLException sql query error
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        if (allAppointments.isEmpty()) {
            getAllAppointmentsFromDatabase();
        } else {
            allAppointments.clear();
            getAllAppointmentsFromDatabase();
        }

        return allAppointments;
    }

    /**
     * Adds a new appointment to the database
     *
     * @param title for new appointment
     * @param description for new appointment
     * @param location for new appointment
     * @param type for new appointment
     * @param start for new appointment
     * @param end for new appointment
     * @param customerID for new appointment
     * @param userID for new appointment
     * @param contactName for new appointment
     * @throws SQLException sql query error
     */
    public static void addNewAppointment(String title, String description, String location, String type, Timestamp start, Timestamp end, Integer customerID, Integer userID, String contactName) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "INSERT INTO appointments VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement newAppointmentStatement = connection.prepareStatement(sql);
        newAppointmentStatement.setInt(1, createNewAppointmentID()); // Appointment_ID
        newAppointmentStatement.setString(2, title);// Title (String)
        newAppointmentStatement.setString(3, description);// Description (String)
        newAppointmentStatement.setString(4, location);// Location (String)
        newAppointmentStatement.setString(5, type);// Type (String)
        newAppointmentStatement.setTimestamp(6, start);// Start (DateTime) 6
        newAppointmentStatement.setTimestamp(7, end);// End (DateTime) 7
        newAppointmentStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));// Create_Date (DateTime) 8
        newAppointmentStatement.setString(9, DBUsers.getCurrentUserName());// Created_By (String - UserName) 9
        newAppointmentStatement.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));// Last_Update (Timestamp) 10
        newAppointmentStatement.setString(11, DBUsers.getCurrentUserName());// Last_Update_By (String - Username) 11
        newAppointmentStatement.setInt(12, customerID);// Customer ID (Integer) 12
        newAppointmentStatement.setInt(13, userID);// User ID (Integer) 13
        newAppointmentStatement.setInt(14, DBContacts.getContactIDFromContactName(contactName)); // Contact_ID (Integer) 14
        newAppointmentStatement.executeUpdate();

    }

    /**
     * Public access to delete from database action
     * @param appointmentID appointment to delete
     * @throws SQLException SQL query error
     */
    public static void deleteAppointmentAction(Integer appointmentID) throws SQLException {
        deleteAppointmentFromDatabase(appointmentID);
    }

    /**
     * Deletes from database
     * @param appointmentID appointment to delete
     * @throws SQLException SQL query error
     */
    private static void deleteAppointmentFromDatabase(Integer appointmentID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement deleteAppointmentStatement = connection.prepareStatement(sql);
        deleteAppointmentStatement.setInt(1, appointmentID);
        deleteAppointmentStatement.executeUpdate();
    }

    /**
     * Update the last updated record for an appointment
     * @param appointmentID appointment to update
     * @throws SQLException SQL error
     */
    private static void updateLastUpdateTimeField(Integer appointmentID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE appointments SET Last_Update = ? WHERE Appointment_ID = ?";
        PreparedStatement updateAppointmentStatement = connection.prepareStatement(sql);
        updateAppointmentStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        updateAppointmentStatement.setInt(2, appointmentID);
        updateAppointmentStatement.executeUpdate();
    }

    /**
     * Update the last updated by field for an appointment
     * @param appointmentID appointment to update
     * @throws SQLException SQL error
     */
    private static void updateLastUpdatedByField(Integer appointmentID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE appointments SET Last_Updated_By = ? WHERE Appointment_ID = ?";
        PreparedStatement updateAppointmentStatement = connection.prepareStatement(sql);
        updateAppointmentStatement.setString(1, DBUsers.getCurrentUserName());
        updateAppointmentStatement.setInt(2, appointmentID);
        updateAppointmentStatement.executeUpdate();
    }

    /**
     * Updates title field in database record
     * @param newAppointmentTitle title to update
     * @param appointmentID appointment to update
     * @throws SQLException SQL error
     */
    public static void updateAppointmentTitle(String newAppointmentTitle, Integer appointmentID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE appointments SET Title = ? WHERE Appointment_ID = ?";
        PreparedStatement updateAppointmentStatement = connection.prepareStatement(sql);
        updateAppointmentStatement.setString(1, newAppointmentTitle);
        updateAppointmentStatement.setInt(2, appointmentID);
        updateAppointmentStatement.executeUpdate();
        updateLastUpdateTimeField(appointmentID);
        updateLastUpdatedByField(appointmentID);
    }

    /**
     * Updates description field in database record
     * @param newAppointmentDescription description to update
     * @param appointmentID - appointment to update
     * @throws SQLException SQL error
     */
    public static void updateAppointmentDescription(String newAppointmentDescription, Integer appointmentID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE appointments SET Description = ? WHERE Appointment_ID = ?";
        PreparedStatement updateAppointmentStatement = connection.prepareStatement(sql);
        updateAppointmentStatement.setString(1, newAppointmentDescription);
        updateAppointmentStatement.setInt(2, appointmentID);
        updateAppointmentStatement.executeUpdate();
        updateLastUpdateTimeField(appointmentID);
        updateLastUpdatedByField(appointmentID);
    }

    /**
     * Updates record of appointment location
     * @param newAppointmentLocation location to update
     * @param appointmentID - appointment to update
     * @throws SQLException SQL error
     */
    public static void updateAppointmentLocation(String newAppointmentLocation, Integer appointmentID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE appointments SET Location = ? WHERE Appointment_ID = ?";
        PreparedStatement updateAppointmentStatement = connection.prepareStatement(sql);
        updateAppointmentStatement.setString(1, newAppointmentLocation);
        updateAppointmentStatement.setInt(2, appointmentID);
        updateAppointmentStatement.executeUpdate();
        updateLastUpdateTimeField(appointmentID);
        updateLastUpdatedByField(appointmentID);
    }

    /**
     * Updates record of appointment type
     * @param newAppointmentType type to update
     * @param appointmentID appointment to update
     * @throws SQLException SQL error
     */
    public static void updateAppointmentType(String newAppointmentType, Integer appointmentID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE appointments SET Type = ? WHERE Appointment_ID = ?";
        PreparedStatement updateAppointmentStatement = connection.prepareStatement(sql);
        updateAppointmentStatement.setString(1, newAppointmentType);
        updateAppointmentStatement.setInt(2, appointmentID);
        updateAppointmentStatement.executeUpdate();
        updateLastUpdateTimeField(appointmentID);
        updateLastUpdatedByField(appointmentID);
    }

    /**
     * Updates record of start time in database
     * @param newAppointmentStart new start time
     * @param appointmentID appointment to update
     * @throws SQLException SQL error
     */
    public static void updateAppointmentStart(Timestamp newAppointmentStart, Integer appointmentID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE appointments SET Start = ? WHERE Appointment_ID = ?";
        PreparedStatement updateAppointmentStatement = connection.prepareStatement(sql);
        updateAppointmentStatement.setTimestamp(1, newAppointmentStart);
        updateAppointmentStatement.setInt(2, appointmentID);
        updateAppointmentStatement.executeUpdate();
        updateLastUpdateTimeField(appointmentID);
        updateLastUpdatedByField(appointmentID);
    }

    /**
     * Updates record of end time in database
     * @param newAppointmentEnd new end time
     * @param appointmentID appointment to update
     * @throws SQLException SQL Error
     */
    public static void updateAppointmentEnd(Timestamp newAppointmentEnd, Integer appointmentID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE appointments SET End = ? WHERE Appointment_ID = ?";
        PreparedStatement updateAppointmentStatement = connection.prepareStatement(sql);
        updateAppointmentStatement.setTimestamp(1, newAppointmentEnd);
        updateAppointmentStatement.setInt(2, appointmentID);
        updateAppointmentStatement.executeUpdate();
        updateLastUpdateTimeField(appointmentID);
        updateLastUpdatedByField(appointmentID);
    }

    /**
     * Updates customer id record in database
     * @param newAppointmentCustomerID new customer id
     * @param appointmentID appointment to update
     * @throws SQLException SQL error
     */
    public static void updateAppointmentCustomerID(Integer newAppointmentCustomerID, Integer appointmentID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE appointments SET Customer_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement updateAppointmentStatement = connection.prepareStatement(sql);
        updateAppointmentStatement.setInt(1, newAppointmentCustomerID);
        updateAppointmentStatement.setInt(2, appointmentID);
        updateAppointmentStatement.executeUpdate();
        updateLastUpdateTimeField(appointmentID);
        updateLastUpdatedByField(appointmentID);
    }

    /**
     * Updates record of user id in database
     * @param newAppointmentUserID new user id
     * @param appointmentID appointment to update
     * @throws SQLException SQL error
     */
    public static void updateAppointmentUserID(Integer newAppointmentUserID, Integer appointmentID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE appointments SET Customer_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement updateAppointmentStatement = connection.prepareStatement(sql);
        updateAppointmentStatement.setInt(1, newAppointmentUserID);
        updateAppointmentStatement.setInt(2, appointmentID);
        updateAppointmentStatement.executeUpdate();
        updateLastUpdateTimeField(appointmentID);
        updateLastUpdatedByField(appointmentID);
    }

    /**
     * Updates record of contact id in database
     * @param newAppointmentContactID new contact id
     * @param appointmentID appointment to update
     * @throws SQLException SQL error
     */
    public static void updateAppointmentContactID(Integer newAppointmentContactID, Integer appointmentID) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "UPDATE appointments SET Customer_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement updateAppointmentStatement = connection.prepareStatement(sql);
        updateAppointmentStatement.setInt(1, newAppointmentContactID);
        updateAppointmentStatement.setInt(2, appointmentID);
        updateAppointmentStatement.executeUpdate();
        updateLastUpdateTimeField(appointmentID);
        updateLastUpdatedByField(appointmentID);
    }










}
