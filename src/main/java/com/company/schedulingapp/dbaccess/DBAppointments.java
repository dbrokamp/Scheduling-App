package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class DBAppointments {

    public static ObservableList<Appointment> getCustomerAppointments(Integer customerID) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM appointments where Customer_ID = ?";
        PreparedStatement customerAppointments = connection.prepareStatement(sql);
        customerAppointments.setString(1, customerID.toString());
        ResultSet appointmentsSet = customerAppointments.executeQuery();

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

            appointments.add(appointment);
        }

        return appointments;
    }

}
