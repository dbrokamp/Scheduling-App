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

    public static ObservableList<Country> getCountries() throws SQLException {
        ObservableList<Country> countries = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM countries";
        PreparedStatement allCountries = connection.prepareStatement(sql);
        ResultSet countriesSet = allCountries.executeQuery();

        while (countriesSet.next()) {

            Country country = new Country(countriesSet.getInt("Country_ID"),
                                            countriesSet.getString("Country"));
            countries.add(country);

        }


        return countries;
    }
}
