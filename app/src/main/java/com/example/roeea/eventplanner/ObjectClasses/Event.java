package com.example.roeea.eventplanner.ObjectClasses;

import android.location.Location;

import java.util.Date;
import java.util.List;

public class Event {
    private int eventID;
    private String name;
    private Date date;
    private Location loc;
    private List<User> mannager;
    private List<User> guests;
    private List<User> Invated;
    private List<Product> Products;

    public Event() {

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
                ", Invated = " + Invated +
                ", Products = " + Products +
                '}';
    }

    public Event(int eID, String nm, Date dt, Location lc, List<User> mannager, List<User> gsts, List<User> invi, List<Product> pru) {
        this.eventID = eID;
        this.name = nm;
        this.loc = lc;
        this.date = dt;
        this.mannager = mannager;
        this.guests = gsts;
        this.Invated = invi;
        this.Products = pru;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
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

    public List<User> getInvated() {
        return Invated;
    }

    public void setInvated(List<User> invated) {
        Invated = invated;
    }

    public List<Product> getProducts() {
        return Products;
    }

    public void setProducts(List<Product> products) {
        Products = products;
    }
}
