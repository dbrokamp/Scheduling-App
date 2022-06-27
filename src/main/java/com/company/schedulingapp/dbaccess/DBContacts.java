package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.Contact;
import com.company.schedulingapp.util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Allows access to contact list in databse
 */
public class DBContacts {

    final private static ObservableList<Contact> contacts = FXCollections.observableArrayList();

    /**
     * Retrieves all contacts from databse
     * @throws SQLException SQL error
     */
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

    /**
     * Getter for contacts
     * @return list of all contacts
     */
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

    /**
     * Gets contact id from a contact name
     * @param name contact name to search
     * @return contact id of contact name
     */
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
            if (contact.getContactName().equals(name)) {
                contactID = contact.getContactID();
            }
        }

        return contactID;
    }

    /**
     * Gets a contact name by contact id
     * @param contactID id of contact to search for
     * @return name of contact for id
     */
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
            if (contact.getContactID().equals(contactID)) {
                contactName = contact.getContactName();
            }
        }

        return  contactName;
    }
}
