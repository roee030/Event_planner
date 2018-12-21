package com.example.roeea.eventplanner;

public class User {
    private String email;
    private String userName;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return userName;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.userName = firstName;
    }


    public User()
    {

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public User(String eml, String fn, String pass)
    {
        this.email=eml;
        this.userName=fn;

        this.password = pass;
    }
}
