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

import java.util.List;

public class User {
    private Firebase mRRef;
    private FirebaseAuth mAuth;
    private FirebaseDatabase FBdb;
    private DatabaseReference firDatabaseUsers;
    private String username;
    private String email;
    private String UID;
    private List<String> events;

    public User() {
        firDatabaseUsers = FirebaseDatabase.getInstance().getReference();
    }

    public User(String username,
                String email,
                String UID) {
        this.username = username;
        this.email = email;
        this.UID = UID;
    }


    public String usertoString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", events=' '\''" +
                '}';
    }

    public String ConvertEmailToFireBaseEmailField() {
        return User.ConvertEmailToFireBaseEmailField(this.email);
    }



    public String getUID(){
        return this.UID;
    }
    public User getUserByUID(String UID, final getUser u) {
        firDatabaseUsers.child("Users").child(UID).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

}
