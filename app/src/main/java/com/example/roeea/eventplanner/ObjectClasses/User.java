package com.example.roeea.eventplanner.ObjectClasses;

import android.hardware.usb.UsbRequest;
import android.support.annotation.NonNull;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    private Firebase mRRef;
    private FirebaseAuth mAuth;
    private FirebaseDatabase FBdb;
    private DatabaseReference firDatabaseUsers;
    private String username;
    private String password;
    private String email;

    public User() {
        firDatabaseUsers = FirebaseDatabase.getInstance().getReference();
    }

    public User(String username,
                String password,
                String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public String usertoString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String ConvertEmailToFireBaseEmailField() {
        return User.ConvertEmailToFireBaseEmailField(this.email);
    }



    public User getUserByEmail(String email, final getUser u) {
        firDatabaseUsers.child("Users").child(email).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                User user = new User();
                user = dataSnapshot.getValue(User.class);
                u.callBack(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        return null;

    }

    public static String ConvertEmailToFireBaseEmailField(String email) {
        String st = new String(email);
        st.replace('.', '|');
        return st;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
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

}
