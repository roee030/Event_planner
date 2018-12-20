package com.example.roeea.eventplanner;

import android.location.Location;

import java.util.Date;
import java.util.List;

public class Event {
    private int eventID;
    private String name;
    private Date date;
    private Location loc;
    private List<User>mannager;
    private List<User>guests;
    private List<User>Invated;
    private List<Product>Product;
    public Event()
    {

    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", loc=" + loc +
                ", mannager=" + mannager +
                ", guests=" + guests +
                ", Invated=" + Invated +
                ", Product=" + Product +
                '}';
    }

    public Event(int eID, String nm, Date dt, Location lc, List<User>mannager, List<User>gsts, List<User>invi, List<Product>pru)
    {
        this.eventID = eID;
        this.name = nm;
        this.loc = lc;
        this.date = dt;
        this.mannager = mannager;
        this.guests = gsts;
        this.Invated = invi;
        this.Product = pru;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void setMannager(List<User> mannager) {
        this.mannager = mannager;
    }

    public void setGuests(List<User> guests) {
        this.guests = guests;
    }

    public void setInvated(List<User> invated) {
        Invated = invated;
    }

    public void setPrudoct(List<Product> prudoct) {
        this.Product = prudoct;
    }

    public int getEventID() {
        return eventID;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Location getLoc() {
        return loc;
    }

    public List<User> getMannager() {
        return mannager;
    }

    public List<User> getGuests() {
        return guests;
    }

    public List<User> getInvated() {
        return Invated;
    }

    public List<Product> getPrudoct() {
        return Product;
    }
}
