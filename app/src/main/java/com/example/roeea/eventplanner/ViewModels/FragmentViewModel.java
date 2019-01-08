package com.example.roeea.eventplanner.ViewModels;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.get;

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
        Event event = new Event();
        event.getListOfEventsByKeys(keys, new get<List<Event>>() {
            @Override
            public void callBack(List<Event> events) {
                Log.i("FragmentViewModel", events.toString());
                FragmentViewModel.this.events.setValue(events);
            }
        });
    }
}
