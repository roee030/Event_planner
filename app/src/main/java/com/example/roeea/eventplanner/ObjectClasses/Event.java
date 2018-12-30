package com.example.roeea.eventplanner.ObjectClasses;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String eventID;
    private String name;
    private String date;
    private String time;
    private String loc;
    private String details;
    private List<User> mannager;
    private List<User> guests;
    private List<User> invited;
    private ArrayList<Product> products;
    private DatabaseReference fireDatabaseT;
    private Event temp_event;
    private static final String TAG = "EVENT CLASS";
    public Event() {
    }

    public Event(String eventID, String eventName, String eventLoc, String eventDate,
                 String eventTime, String eventDetails, ArrayList<Product> productsArrayList) {

        this.eventID = eventID;
        name = eventName;
        date = eventDate;
        loc = eventLoc;
        time = eventTime;
        details = eventDetails;
        products = productsArrayList;
    }


    public void getEventByKey(String key, final get<Event> element)
    {
        fireDatabaseT = FirebaseDatabase.getInstance().getReference();
        fireDatabaseT.child("Events").child(key).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                temp_event = dataSnapshot.getValue(Event.class);
                element.callBack(temp_event);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", name = '" + name + '\'' +
                ", date = " + date +
                ", loc = " + loc +
                ", managers = " + mannager +
                ", guests=" + guests +
                ", invited = " + invited +
                ", products = " + products +
                '}';
    }

    public String  getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public List<User> getMannager() {
        return mannager;
    }

    public void setMannager(List<User> mannager) {
        this.mannager = mannager;
    }

    public List<User> getGuests() {
        return guests;
    }

    public void setGuests(List<User> guests) {
        this.guests = guests;
    }

    public List<User> getInvited() {
        return invited;
    }

    public void setInvited(List<User> invited) {
        this.invited = invited;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
