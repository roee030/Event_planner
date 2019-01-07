package com.example.roeea.eventplanner.ObjectClasses;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Event{
    private String eventID;
    private String name;
    private String date;
    private String time;
    private String loc;
    private String details;
    private List<String> mannager;
    private List<String> guests;
    private List<String> invited;
    private List<Product> products;
    private String budget;
    private String totalEventBudget;
    private Event temp_event;
    final List<Event> m_events = new ArrayList<>();
    private static final String TAG = "EVENT CLASS";
    private static DatabaseReference fireDatabaseT;

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
        if(mannager == null) mannager = new ArrayList<>();
        return mannager;
    }

    public void setMannager(List<String> mannager) {
        this.mannager = mannager;
    }

    public List<String> getGuests() {
        if(guests == null) guests = new ArrayList<>();
        return guests;
    }

    public void setGuests(List<String> guests) {
        this.guests = guests;
    }

    public List<String> getInvited() {
        if(invited == null) invited = new ArrayList<>();
        return invited;
    }

    public void setInvited(List<String> invited) {
        this.invited = invited;
    }

    public void setBudget(String budget){ this.budget = budget; }

    public String getBudget(){return budget; }

    public Event(String eventID, String eventName, String eventLoc, String eventDate,
                 String eventTime, String eventDetails, ArrayList<Product> productsArrayList, String budget) {

        this.eventID = eventID;
        this.budget = budget;
        name = eventName;
        date = eventDate;
        loc = eventLoc;
        time = eventTime;
        details = eventDetails;
        products = productsArrayList;
        mannager = new ArrayList<>();
        guests = new ArrayList<>();
        invited = new ArrayList<>();
        totalEventBudget = "";
    }

    public void getListOfEventsByKeys(final List<String> keys, final get<List<Event>> events)
    {
        fireDatabaseT = FirebaseDatabase.getInstance().getReference();
        fireDatabaseT.child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
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
                }
                events.callBack(m_events);
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
                ", guests = " + guests +
                ", invited = " + invited +
                ", products = " + products +
                ", budget = " + budget +
                ", totalEventBudget = " + totalEventBudget +
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

    public List<Product> getProducts() {
        if (products == null) products = new ArrayList<>();
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getTotalEventBudget(){ return totalEventBudget; }

    public void updateTotalEventBudget(){
        int sum = 0;
        for(int i = 0; i < products.size(); i++){
            sum += products.get(i).getQuantity() * products.get(i).getPrice();
        }
        totalEventBudget = sum +"";
    }
}
