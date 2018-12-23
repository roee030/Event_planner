package com.example.roeea.eventplanner.DatabaseAPI;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.Product;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DatabaseHelper {

    private DatabaseReference db;
    private Firebase FB;
    private Event ev;
    private final String url = "https://event-planner-d32e9.firebaseio.com/";
    User user;
    public DatabaseHelper(Context context) {
        Firebase.setAndroidContext(context);
        FB = new Firebase(url);
        db = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * This method search user by email
     * @param email - a string represents the email one wants to search
     * @return User object in case the query is good, and null otherwise
     */
    public User getUserByEmail(final String email)
    {
        final User user = null;
        db.child("User's").orderByChild("Email").equalTo(User.ConvertEmailToFireBaseEmailField(email)).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                        User user = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
       return user;
    }

    public void setNewUser(User user) {
        db.child("Event's").child(user.getEmail()).setValue(user);
        //need to add method that check if event upload to firebase
    }


    /**
     * This function add event to firebase database
     *
     * @param ev - reference to the event
     * @return void
     */
    public void setNewEvent(Event ev) {
        db.child("Events").push().setValue(ev);
        //need to add method that check if event upload to firebase
    }


    /**
     * This function is an database query that goes
     * to the database and retrieves user object from it.
     * @param evid - event ID
     * @return User object in case the query is good, and null otherwise.
     */
    public Event getEventByEventID(final int evid) {
        FB = new Firebase("https://event-planner-d32e9.firebaseio.com/Event's");

        db = FirebaseDatabase.getInstance().getReference();
        FB.child("Event's").orderByChild("eventID").equalTo(evid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("evid")) {
                    ev = dataSnapshot.getValue(Event.class);
                } else {
                    ev = null;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return ev;
    }

    /**
     * Renmove ev from database
     * @param ev - reference to the event.
     */
    public void removeEvent(Event ev) {
        FB = new Firebase("https://event-planner-d32e9.firebaseio.com/Event's");

        db = FirebaseDatabase.getInstance().getReference();

        db.child("ev.getEventID()").removeValue();

    }

    /**
     * Remove Product From Event
     * @param ev - The event one wants to remove a product from
     * @param product_to_remove - The product one wants to remove from the event ev
     */
    public void removeProductFromEventProductsList(Event ev, Product product_to_remove) {
        int id = ev.getEventID();
        List<Product> temp = ev.getProducts();
        temp.remove(product_to_remove);
    }

}
