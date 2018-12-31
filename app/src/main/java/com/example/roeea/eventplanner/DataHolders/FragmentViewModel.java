package com.example.roeea.eventplanner.DataHolders;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.ObjectClasses.get;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewModel extends ViewModel
{
    MutableLiveData<List<Event>> events = new MutableLiveData<>();

    public LiveData<List<Event>> getEvent()
    {
        return events;
    }

    public void loadEvents(List<String> keys)
    {
        events = new MutableLiveData<>();
        Event event = new Event();
        event.getListOfEventsByKeys(keys, new get<List<Event>>() {
            @Override
            public void callBack(List<Event> events) {
                FragmentViewModel.this.events.setValue(events);
            }
        });
    }
}
