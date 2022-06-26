package com.company.schedulingapp.model;

public class Country {

    private Integer countryID;
    private String country;


    public Country(Integer countryID, String country) {
        this.countryID = countryID;
        this.country = country;

    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
