package com.example.roeea.eventplanner;

import android.location.Location;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class DatabaseHelper {
    DatabaseReference db;
    Firebase mRRef;
    Event ev;







    /**
     * This function add event to firebase database
     * @param Event ev
     * @return void
     */

    public void setNewEvent(Event ev)
    {

        mRRef = new Firebase("https://event-planner-d32e9.firebaseio.com/");
        db=FirebaseDatabase.getInstance().getReference();
        db.child("Event's").child("ev.getEventID()").setValue(ev);
        //need to add method that check if event upload to firebase

    }


    /**
     * This function is an database query that goes
     * to the database and retrieves user object from it.
     * @param username
     * @return User object in case the query is good, and null otherwise.
     */

    public Event getEventByEventID(final int evid) {
        mRRef = new Firebase("https://event-planner-d32e9.firebaseio.com/Event's");

        db=FirebaseDatabase.getInstance().getReference();
        mRRef.child("Event's").orderByChild("eventID").equalTo(evid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("evid"))
                {
                    ev = dataSnapshot.getValue(Event.class);


                }
                else
                {
                    // the case where the data does not yet exist


                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

                    }
        });



        return ev;

    }

    public void removeEvent(Event ev)
    {
        mRRef = new Firebase("https://event-planner-d32e9.firebaseio.com/Event's");

        db=FirebaseDatabase.getInstance().getReference();

        db.child("ev.getEventID()").removeValue();

    }





    }
