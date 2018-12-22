package com.example.roeea.eventplanner.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth Mauth;
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Mauth = FirebaseAuth.getInstance(); //Defines an instance
        db = FirebaseDatabase.getInstance().getReference(); //Getting root reference
        addUserToDB();

    }

    public void addUserToDB()
    {
        Mauth.createUserWithEmailAndPassword("dorlevu121@www.com","123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    User user = new User("Email@wal.com","Roee","Engel");
                    db.child("User's").child(user.getEmail().replace(".", "|")).setValue(user);

                    Toast.makeText(RegisterActivity.this, "Seccsess", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}
