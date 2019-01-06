package com.example.roeea.eventplanner.ObjectClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Guest {
    private Map<String, List<Product>> events;

    public Guest() {
        events = new HashMap<>();
    }

    public Guest(Map<String, List<Product>> events) {
        this.events = events;
    }

    public void leave()
    {
        //doto
    }

    public Map<String, List<Product>> getEvents() {
        return events;
    }

    public void setEvents(Map<String, List<Product>> events) {
        this.events = events;
    }

    public List<Product> getProductsForEvent(String eventId) {
        return events.get(eventId);
    }

    public void setProductsForEvent(String eventId, List<Product> products) {
        events.put(eventId, products);
    }
}
