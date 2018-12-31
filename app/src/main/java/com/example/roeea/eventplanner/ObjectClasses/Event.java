package com.example.roeea.eventplanner.ObjectClasses;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String eventID;
    private String name;
    private String date;
    private String time;
    private String loc;
    private String details;
    private List<String> mannager;
    private List<String> guests;
    private List<String> invited;
    private ArrayList<Product> products;
    private DatabaseReference fireDatabaseT;
    private Event temp_event;
    final List<Event> m_events = new ArrayList<>();
    private static final String TAG = "EVENT CLASS";
    public Event() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<String> getMannager() {
        return mannager;
    }

    public void setMannager(List<String> mannager) {
        this.mannager = mannager;
    }

    public List<String> getGuests() {
        return guests;
    }

    public void setGuests(List<String> guests) {
        this.guests = guests;
    }

    public List<String> getInvited() {
        return invited;
    }

    public void setInvited(List<String> invited) {
        this.invited = invited;
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
        mannager = new ArrayList<>();
        guests = new ArrayList<>();
        invited = new ArrayList<>();
    }

    public void getListOfEventsByKeys(final List<String> keys, final get<List<Event>> events)
    {
        fireDatabaseT = FirebaseDatabase.getInstance().getReference();
        fireDatabaseT.child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot event: dataSnapshot.getChildren()) {
                    for(String key : keys)
                    {
                        if (key.equals(event.getKey())) {
                            Event.this.temp_event = event.getValue(Event.class);
                            m_events.add(Event.this.temp_event);
                        }
                    }
                    events.callBack(m_events);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
