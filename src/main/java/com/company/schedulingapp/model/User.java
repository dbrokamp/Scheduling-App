package com.company.schedulingapp.model;

/**
 * Model for objects created from the users table; does not get or store passwords
 */
public class User {

    private Integer userID;
    private String userName;



    public User(Integer userID, String userName) {
        this.userID = userID;
        this.userName = userName;

    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}

