package com.example.roeea.eventplanner.DataHolders;

import android.arch.lifecycle.LiveData;

import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.User;

import java.util.ArrayList;
import java.util.List;

public class ExtendedUser {
    private User user = new User();
    private List<Event> MangerOf = new ArrayList<>();
    private List<Event> GuestIn = new ArrayList<>();
    private List<Event> InvitedTo = new ArrayList<>();
    private List<String> ManagerEventsName = new ArrayList<>();
    private List<String> GuestEventsName = new ArrayList<>();
    private List<String> InvitedEventsName = new ArrayList<>();
    private static final String TAG = "ExtendedUser";

    public void setManagerEventsName(List<String> managerEventsName) {
        ManagerEventsName = managerEventsName;
    }

    public void setGuestEventsName(List<String> guestEventsName) {
        GuestEventsName = guestEventsName;
    }

    public void setInvitedEventsName(List<String> invitedEventsName) {
        InvitedEventsName = invitedEventsName;
    }

    public void setMangerOf(List<Event> mangerOf) {
        MangerOf = mangerOf;
    }

    public void setGuestIn(List<Event> guestIn) {
        GuestIn = guestIn;
    }

    public void setInvitedTo(List<Event> invitedTo) {
        InvitedTo = invitedTo;
    }

    public ExtendedUser(User user, List<String> managerEventsName, List<String> guestEventsName, List<String> invitedEventsName) {
        this.user = user;
        ManagerEventsName = managerEventsName;
        GuestEventsName = guestEventsName;
        InvitedEventsName = invitedEventsName;
    }
    public ExtendedUser(User user)
    {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getManagerEventsName() {
        return ManagerEventsName;
    }

    public List<String> getGuestEventsName() {
        return GuestEventsName;
    }

    public List<String> getInvitedEventsName() {
        return InvitedEventsName;
    }
}
