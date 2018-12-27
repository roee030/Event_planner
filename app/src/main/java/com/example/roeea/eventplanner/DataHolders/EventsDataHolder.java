package com.example.roeea.eventplanner.DataHolders;
import com.example.roeea.eventplanner.ObjectClasses.Event;

import java.util.HashMap;


public class EventsDataHolder {
   // HashMap<Integer, String>

    private static final EventsDataHolder EventsDataHolderInstance = new EventsDataHolder();

    public static EventsDataHolder getEventsDataHolderInstance() {
        return EventsDataHolderInstance;
    }

    private EventsDataHolder() {
    }



}
