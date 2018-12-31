package com.example.roeea.eventplanner.DataHolders;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.roeea.eventplanner.Activities.AccountActivity;
import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.ObjectClasses.get;
import com.google.android.gms.dynamic.IFragmentWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This class track and reserve the information about the user
 * that is currently logged in our application in his device.
 */
public class UserDataHolder {
    private User user = null;
    private List<Event> ManagerOf;
    private List<Event> GuestIn;
    private List<Event> Invite_to;
    private List<String> ManagerEventsName=new ArrayList<>();;
    private List<String> GuestEventsName=new ArrayList<>();;
    private List<String> InvitedEventsName=new ArrayList<>();;
    private static Event tempEvent = new Event();

    private static final String TAG = "UserDataHolder";


    private static final UserDataHolder userDataHolderInstance = new UserDataHolder();

    public static UserDataHolder getUserDataHolderInstance() {
        return userDataHolderInstance;
    }

    /**
     * Default Constructor
     */
    private UserDataHolder()
    {
        user = new User();
        ManagerOf = new ArrayList<>();
        GuestIn = new ArrayList<>();
        Invite_to = new ArrayList<>();
    }

    /**
     * @return An authenticated user
     */
    public User getAuthenticatedUser() {
        return user;
    }

    /**
     * Sets the current logged user.
     * @param user - user one wants the UserDataHolder to keep track of
     */
    public void setAuthenticatedUser(User user)
    {
        Log.i(TAG, "Set User");
        if(user != null)
        {
            this.user = user;
            this.getGuestInEvents(user.getGuestIn().getEvents());
            this.getInviteToEvents(user.getInvitedTo().getInviteeEvent());
            this.getManagerOfEvents(user.getManagerOf().getEvents());
        }
        else {
            this.user = new User();
        }
    }

    public List<Event> getManagedEvents()
    {
        return this.ManagerOf;
    }
    public List<Event> getGuestEvents()
    {
        return this.GuestIn;
    }
    public List<Event> getInvitedEvents()
    {
        return this.Invite_to;
    }
    public List<String> getManagerEventsName()
    {
        return this.ManagerEventsName;
    }
    public List<String> getGuestEventsName()
    {
        return this.GuestEventsName;
    }
    public List<String> getInvitedEventsName()
    {
        return this.InvitedEventsName;
    }

    private void getDetailsUser(String uID) {
        user.getUserByUID(uID, new get<User>() {
            @Override
            public void callBack(User user) {
                UserDataHolder.this.user = user;
            }
        });
    }

    private void getGuestInEvents(List<String> keys) {
        for(int i = 1; i<= keys.size(); i++)
        {
            tempEvent.getEventByKey(keys.get(i-1), new get<Event>() {
                @Override
                public void callBack(Event event) {
                    if(event == null)
                        Log.e(TAG, "event is null, user is " + (user == null ? "" : "not ") + "null");
                    else
                        UserDataHolder.this.GuestEventsName.add(event.getName());
                    UserDataHolder.this.GuestIn.add(event);
                }
            });
        }
    }
    private void getManagerOfEvents(List<String> keys) {
        if(UserDataHolder.this.ManagerEventsName == null)
            UserDataHolder.this.ManagerEventsName = new ArrayList<>();
        for(int i = 1; i<= keys.size(); i++)
        {
            tempEvent.getEventByKey(keys.get(i-1), new get<Event>() {
                @Override
                public void callBack(Event event) {
                    if(event == null)
                        Log.e(TAG, "event is null, user is " + (user == null ? "" : "not ") + "null");
                    else
                        UserDataHolder.this.ManagerEventsName.add(event.getName());
                    UserDataHolder.this.ManagerOf.add(event);
                }
            });
        }
    }
    private void getInviteToEvents(List<String> keys) {
        for(int i = 1; i<= keys.size(); i++)
        {
            tempEvent.getEventByKey(keys.get(i-1), new get<Event>() {
                @Override
                public void callBack(Event event) {
                    if(event == null)
                        Log.e(TAG, "event is null, user is " + (user == null ? "" : "not ") + "null");
                    else
                        UserDataHolder.this.InvitedEventsName.add(event.getName());
                    UserDataHolder.this.Invite_to.add(event);
                }
            });
        }
    }

}