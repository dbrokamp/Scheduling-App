package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.Contact;
import com.company.schedulingapp.util.JDBC;
import com.mysql.cj.jdbc.JdbcConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBContacts {

    private static ObservableList<Contact> contacts = FXCollections.observableArrayList();

    private static void getAllContactsFromDatabase() throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM contacts";
        PreparedStatement getAllContactsFromDatabaseStatement = connection.prepareStatement(sql);
        ResultSet allContactsSet = getAllContactsFromDatabaseStatement.executeQuery();

        while (allContactsSet.next()) {
            Contact contact = new Contact(allContactsSet.getInt("Contact_ID"),
                                            allContactsSet.getString("Contact_Name"),
                                            allContactsSet.getString("Email"));
            contacts.add(contact);
        }
    }

    public static ObservableList<Contact> getContacts() {
        if (contacts.isEmpty()) {
            try {
                getAllContactsFromDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return contacts;
    }

    public static Integer getContactIDFromContactName(String name) {
        if (contacts.isEmpty()) {
            try {
                getAllContactsFromDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Integer contactID = null;
        for (Contact contact : contacts) {
            if (contact.getContactName() == name) {
                contactID = contact.getContactID();
            }
        }

        return contactID;
    }

    public static String getContactNameFromContactID(Integer contactID) {
        if (contacts.isEmpty()) {
            try {
                getAllContactsFromDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String contactName = null;
        for (Contact contact : contacts) {
            if (contact.getContactID() == contactID) {
                contactName = contact.getContactName();
            }
        }

        return  contactName;
    }
}
