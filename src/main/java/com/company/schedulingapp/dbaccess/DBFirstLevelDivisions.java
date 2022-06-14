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
}
