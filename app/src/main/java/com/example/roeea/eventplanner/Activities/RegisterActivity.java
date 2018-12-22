package com.example.roeea.eventplanner.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.R;
import com.example.roeea.eventplanner.Server.MiniServer;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth Mauth;
    private DatabaseReference db;
    private Firebase mRRef;
    private FirebaseAuth mAuth;
    private EditText Email;
    private EditText Password;
    private EditText Fullname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Password = (EditText)findViewById(R.id.passwordtextregister);
        Fullname = (EditText)findViewById(R.id.fullnametextregister);
        Email = (EditText)findViewById(R.id.emailtextregister);
        Mauth = FirebaseAuth.getInstance(); //Defines an instance
        db = FirebaseDatabase.getInstance().getReference(); //Getting root reference
        addUserToDB();

    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case(R.id.registerbutton):
                User user = new User(Fullname.toString(),Password.toString(),Email.toString());
                MiniServer server = MiniServer.getServerInstance();
                boolean register_flag = server.checkRegister(user);
                if(register_flag)
                {
                    db.child("User's").child(user.getEmail().replace(".", "|")).setValue(user);
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"Couldn't register",Toast.LENGTH_SHORT).show();
                }
            case(R.id.registerBackBT):
                Intent register = new Intent(RegisterActivity.this,MainActivity.class);
                default:
                    break;
        }
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
