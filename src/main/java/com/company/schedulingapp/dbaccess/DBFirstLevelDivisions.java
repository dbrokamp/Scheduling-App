package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.FirstLevelDivision;
import com.company.schedulingapp.util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Allows for database access for first_level_division table
 */
public class DBFirstLevelDivisions {


    /**
     * Queries database for first level divisions by countryID
     * @param countryID countryID to search for
     * @return list of first_level_divisions
     * @throws SQLException SQL error
     */
    public static ObservableList<FirstLevelDivision> getFirstLevelDivisionsForCountryID(Integer countryID) throws SQLException {
        ObservableList<FirstLevelDivision> firstLevelDivisions = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement firstLevelDivisionsStatement = connection.prepareStatement(sql);
        firstLevelDivisionsStatement.setInt(1, countryID);
        ResultSet firstLevelDivisionsSet = firstLevelDivisionsStatement.executeQuery();

        while (firstLevelDivisionsSet.next()) {

            FirstLevelDivision firstLevelDivision = new FirstLevelDivision(firstLevelDivisionsSet.getInt("Division_ID"),
                                                                            firstLevelDivisionsSet.getString("Division"),
                                                                            firstLevelDivisionsSet.getInt("Country_ID"));
            firstLevelDivisions.add(firstLevelDivision);

        }

        return firstLevelDivisions;
    }

    /**
     * Allows for searching of database by a string name of a first level division to get the division id
     * @param firstLevelDivisionName string of first level name to search database with
     * @return division id
     * @throws SQLException SQL error
     */
    public static Integer getDivisionID(String firstLevelDivisionName) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM first_level_divisions WHERE Division = ?";
        PreparedStatement firstLevelDivisionStatement = connection.prepareStatement(sql);
        firstLevelDivisionStatement.setString(1, firstLevelDivisionName);
        ResultSet firstLevelDivisionSet = firstLevelDivisionStatement.executeQuery();

        if (firstLevelDivisionSet.next()) {
            return firstLevelDivisionSet.getInt("Division_ID");
        } else {
            return null;
        }

    }

    /**
     * Allows for retrieval of a first level division object by searching by its ID
     * @param divisionID integer to search first_level_division table for
     * @return FirstLevelDivision full object
     * @throws SQLException SQL error
     */
    public static FirstLevelDivision getFirstLevelDivisionFromDivisionID(Integer divisionID) throws SQLException {

        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement firstLevelDivisionNameStatement = connection.prepareStatement(sql);
        firstLevelDivisionNameStatement.setInt(1, divisionID);
        ResultSet firstLevelNameDivisionSet = firstLevelDivisionNameStatement.executeQuery();

        if (firstLevelNameDivisionSet.next()) {
            return new FirstLevelDivision(firstLevelNameDivisionSet.getInt("Division_ID"),
                                                                            firstLevelNameDivisionSet.getString("Division"),
                                                                            firstLevelNameDivisionSet.getInt("Country_ID"));
        } else {
            return null;
        }

    }
}
