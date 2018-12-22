package com.example.roeea.eventplanner.ObjectClasses;

import java.util.List;

public class Guest {
    private List<Event>events;
    private List<Product>Products;

    public Guest() {
    }

    public Guest(List<Event> events, List<Product> products) {
        this.events = events;
        Products = products;
    }
    public void leave()
    {
        //doto
    }
}
