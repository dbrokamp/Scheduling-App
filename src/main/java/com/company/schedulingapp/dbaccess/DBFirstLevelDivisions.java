package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.FirstLevelDivision;
import com.company.schedulingapp.util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBFirstLevelDivisions {


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
