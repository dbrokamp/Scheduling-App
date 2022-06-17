package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.Country;
import com.company.schedulingapp.util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCountries {

    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();

    private static void getAllCountries() throws SQLException {

        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM countries";
        PreparedStatement allCountriesStatement = connection.prepareStatement(sql);
        ResultSet countriesSet = allCountriesStatement.executeQuery();

        while (countriesSet.next()) {

            Country country = new Country(countriesSet.getInt("Country_ID"),
                                            countriesSet.getString("Country"));
            allCountries.add(country);

        }
    }

    public static ObservableList<Country> getCountries() throws SQLException {
        if (allCountries.isEmpty()) {
           getAllCountries();
        }
        return allCountries;
    }

    public static Country getCountryByName(String countryName) throws SQLException {
        Country country = null;
        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM countries WHERE Country = ?";
        PreparedStatement getCountryByNameStatement = connection.prepareStatement(sql);
        getCountryByNameStatement.setString(1, countryName);
        ResultSet countrySet = getCountryByNameStatement.executeQuery();

        while (countrySet.next()) {
            country = new Country(countrySet.getInt("Country_ID"),
                                            countrySet.getString("Country"));

        }
        return country;
    }

    public static Country getCountryByID(Integer countryID) throws SQLException {
        Country country = null;
        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM countries WHERE Country_ID = ?";
        PreparedStatement getCountryByNameStatement = connection.prepareStatement(sql);
        getCountryByNameStatement.setInt(1, countryID);
        ResultSet countrySet = getCountryByNameStatement.executeQuery();

        while (countrySet.next()) {
            country = new Country(countrySet.getInt("Country_ID"),
                    countrySet.getString("Country"));

        }
        return country;
    }
}
