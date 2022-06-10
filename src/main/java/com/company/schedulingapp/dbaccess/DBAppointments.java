package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.ZonedDateTime;

public class DBAppointments {

    public static ObservableList<Appointment> getCustomerAppointments(Integer customerID) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM appointments where Customer_ID = ?";
        PreparedStatement customerAppointments = connection.prepareStatement(sql);
        customerAppointments.setString(1, customerID.toString());
        ResultSet appointmentsSet = customerAppointments.executeQuery();

        while (appointmentsSet.next()) {

            Appointment appointment = new Appointment(Integer.parseInt(appointmentsSet.getObject(1).toString()),
                                                       appointmentsSet.getObject(2).toString(),
                                                        appointmentsSet.getObject(3).toString(),
                                                        appointmentsSet.getObject(4).toString(),
                                                        appointmentsSet.getObject(5).toString(),
                                                        Date.valueOf(appointmentsSet.getObject(6).toString()),
                                                        Date.valueOf(appointmentsSet.getObject(7).toString()),
                                                        Integer.parseInt(appointmentsSet.getObject(12).toString()),
                                                        Integer.parseInt(appointmentsSet.getObject(13).toString()),
                                                        Integer.parseInt(appointmentsSet.getObject(14).toString()));

            appointments.add(appointment);
        }

        return appointments;
    }

}
