package com.example.roeea.eventplanner;

/**
 * Actual class of user.
 */
public class User {
    private String username;
    private String password;
    private String name;
    private String lastName;
    private String email;

    public User(String username,
                String password,
                String name,
                String lastName,
                String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName(){
        return name;
    }

    public String getLastName(){
        return lastName;
    }

    // here we may add some new function such as changing details.

}
