package com.example.roeea.eventplanner.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.ObjectClasses.getUser;
import com.example.roeea.eventplanner.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    private TextView Hellomsg;
    private Firebase mRRef;
    private FirebaseAuth mAuth;
    private FirebaseDatabase FBdb;
    private DatabaseReference firDatabaseUsers;
    private User userNew;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        userNew = new User();
        Hellomsg = (TextView) findViewById(R.id.Hellomsg);
        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail().replace('.','|');
        getDetailsUser();

        Hellomsg.append(" "+userNew.getUsername()+" "+userNew.getPassword());

    }

    private void getDetailsUser(){
        userNew.getUserByEmail(email, new getUser() {
            @Override
            public void callBack(User user) {
                userNew = user;
                Toast.makeText(AccountActivity.this,userNew.getUsername(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
