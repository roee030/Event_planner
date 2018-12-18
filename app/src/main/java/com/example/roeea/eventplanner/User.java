package com.example.roeea.eventplanner;

public class User {
    private String email;
    private String firstName;
    private String lastNamel;

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastNamel() {
        return lastNamel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastNamel(String lastNamel) {
        this.lastNamel = lastNamel;
    }
    public User()
    {

    }
    public User(String eml, String fn,String ls)
    {
        this.email=eml;
        this.firstName=fn;
        this.lastNamel=ls;
    }
}
