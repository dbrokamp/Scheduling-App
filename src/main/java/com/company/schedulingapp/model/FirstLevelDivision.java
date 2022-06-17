package com.company.schedulingapp.model;

import java.sql.Date;
import java.sql.Timestamp;

public class FirstLevelDivision {

    private Integer divisionID;
    private String division;
    private Integer countryID;

    public FirstLevelDivision(Integer divisionID, String division, Integer countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    public Integer getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(Integer divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }
}

