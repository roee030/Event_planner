package com.example.roeea.eventplanner.Activities;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.roeea.eventplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventInvitationActivity extends AppCompatActivity {
    private static final String TAG = "event invitation";

    String eventID;

    TextView eventName;
    TextView eventLocation;
    TextView eventTime;
    TextView eventDate;

    private FirebaseAuth fAuth;
    private FirebaseDatabase FBdb;
    private DatabaseReference fRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_invitation);

        eventID = getIntent().getStringExtra("eventID");


        eventName = findViewById(R.id.txtEventName);
        eventLocation = findViewById(R.id.txtLocation);
        eventTime = findViewById(R.id.txtTime);
        eventDate = findViewById(R.id.txtDate);

        pullEventDetailsFromDB(eventID);

    }

    private void pullEventDetailsFromDB(String eventID) {
        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() == null)
        {
            Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);
            finish();
        }

        FBdb = FirebaseDatabase.getInstance();
        fRef = FBdb.getReference().child("Events").child(eventID);
        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //test for getting information about the event from DB
                eventName.append(dataSnapshot.child("eventName").getValue(String.class));
                eventLocation.append(dataSnapshot.child("location").getValue(String.class));
                eventTime.append(dataSnapshot.child("time").getValue(String.class));
                eventDate.append(dataSnapshot.child("date").getValue(String.class));
                Log.d(TAG, "pulling details from event: " + eventName.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
