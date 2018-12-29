package com.example.roeea.eventplanner.ObjectClasses;

import java.util.ArrayList;
import java.util.List;

public class Invitee {
    private List<String>inviteeEvent;

    public Invitee() {
        inviteeEvent = new ArrayList<>();

    }

    public void accept()
    {

    }
    public void decline()
    {

    }
    public void declineAll()
    {

    }

    public List<String> getInviteeEvent() {
        return inviteeEvent;
    }
}
