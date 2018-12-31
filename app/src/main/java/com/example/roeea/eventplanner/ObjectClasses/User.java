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
    private Manager managerOf;
    private Invitee invitedTo;
    private Guest guestIn;
    private User userForCallBack;
    public User(String username, String email, Manager managerOf, Invitee invitedTo, Guest guestIn) {
        this.username = username;
        this.email = email;
        this.managerOf = managerOf;
        this.invitedTo = invitedTo;
        this.guestIn = guestIn;
    }

    public User() {
        firDatabaseUsers = FirebaseDatabase.getInstance().getReference();
        managerOf = new Manager();
        invitedTo = new Invitee();
        guestIn = new Guest();
    }


    public User(String username,
                String email) {
        this.username = username;
        this.email = email;
        this.managerOf = new Manager();
        this.invitedTo = new Invitee();
        this.guestIn = new Guest();
    }

    public User getUserByUID(String UID, final get<User> u) {
        firDatabaseUsers.child("Users").child(UID).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
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

    public void setManagerOf(Manager managerOf) {
        this.managerOf = managerOf;
    }

    public void setInvitedTo(Invitee invitedTo) {
        this.invitedTo = invitedTo;
    }

    public void setGuestIn(Guest guestIn) {
        this.guestIn = guestIn;
    }

    public void setUsername(final String username) {
//
//        mAuth = FirebaseAuth.getInstance();
//
//        firDatabaseUsers.child("Users").child(mAuth.getUid());
//
//        mAuth = FirebaseAuth.getInstance();
//        String userID = mAuth.getCurrentUser().getUid();
//        FBdb = FirebaseDatabase.getInstance();
//        User userForChangeNameInDB = getUserByUID(userID, new get<User>() {
//            @Override
//            public void callBack(User user) {
//                String userID = mAuth.getCurrentUser().getUid();
//                userForCallBack = user;
//                FBdb.getReference().child("Users").child(userID).child("username").setValue("lala");
//            }
//        });
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
    public Manager getManagerOf() {
        return managerOf;
    }

    public Invitee getInvitedTo() {
        return invitedTo;
    }

    public Guest getGuestIn() {
        return guestIn;
    }
}
