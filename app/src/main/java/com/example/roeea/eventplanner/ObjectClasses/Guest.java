package com.example.roeea.eventplanner.ObjectClasses;

import java.util.ArrayList;
import java.util.List;

public class Guest {
    private List<String>events;
    private List<String>Products;

    public Guest() {
        events = new ArrayList<>();
        Products = new ArrayList<>();
    }

    public Guest(List<String> events, List<String> products) {
        this.events = events;
        Products = products;
    }
    public void leave()
    {
        //doto
    }

    public List<String> getEvents() {
        return events;
    }
}
