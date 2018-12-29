package com.example.roeea.eventplanner.ObjectClasses;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.roeea.eventplanner.Activities.AccountActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Manager {
    public Manager() {
        events = new ArrayList<>();
    }

    public void addEventtoList(String eventID) {
        events.add(eventID);
    }

    private List<String> events;

    public List<String> getEvents() {
        return events;
    }
}