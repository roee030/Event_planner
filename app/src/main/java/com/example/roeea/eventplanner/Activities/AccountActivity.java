package com.example.roeea.eventplanner.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roeea.eventplanner.DataHolders.UserDataHolder;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.ObjectClasses.getUser;
import com.example.roeea.eventplanner.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "home";
    private TextView Hellomsg;
    private Firebase mRRef;
    private FirebaseUser fUser;
    private FirebaseAuth fAuth;
    private FirebaseDatabase FBdb;
    private DatabaseReference firDatabaseUsers;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private User user;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        String eventID = getIntent().getStringExtra("eventID");

        // Receiving invite
        if(eventID != null){
            startActivity(new Intent(this, EventInvitationActivity.class)
                    .putExtra("eventID", eventID));
        }

        Hellomsg = (TextView) findViewById(R.id.Hellomsg);
        fAuth = FirebaseAuth.getInstance();
//       fAuth.getInstance().signOut();
        if(fAuth.getCurrentUser() == null)
        {
            Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);
            finish();
        }
        FBdb = FirebaseDatabase.getInstance();

        String userUID = fAuth.getInstance().getCurrentUser().getUid();
        firDatabaseUsers = root.child("Users").child(userUID);


        firDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String userName = dataSnapshot.child("username").getValue(String.class);
                Hellomsg.append(" " + userName);
                Log.d(TAG, "Value is: " + userName);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void getDetailsUser(){
        user.getUserByUID(email, new getUser() {
            @Override
            public void callBack(User user) {
                AccountActivity.this.user = user;
                Toast.makeText(AccountActivity.this, AccountActivity.this.user.getUsername(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
