package com.example.roeea.eventplanner.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Firebase mRRef;
    private FirebaseUser user;
    private FirebaseAuth fAuth;
    private FirebaseDatabase FBdb;
    private DatabaseReference firDatabaseUsers;
    private EditText EmailRegister;
    private EditText PasswordRegister;
    private EditText Fullname;
    private Button RegistrationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        //firDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        FBdb = FirebaseDatabase.getInstance();
        firDatabaseUsers = FBdb.getReference("Users");
        mRRef = new Firebase("https://event-planner-d32e9.firebaseio.com/");
        PasswordRegister = (EditText) findViewById(R.id.passwordtextregister);
        Fullname = (EditText) findViewById(R.id.fullnametextregister);
        EmailRegister = (EditText) findViewById(R.id.emailtextregister);
        RegistrationButton = (Button) findViewById(R.id.registerbutton);


        RegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                if (TextUtils.isEmpty(EmailRegister.getText())) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(PasswordRegister.getText())) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(Fullname.getText())) {
                    Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                    return;
                }



                if (PasswordRegister.getText().length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
*/

          //      user = FirebaseAuth.getInstance().getCurrentUser();

                Log.i("Register activity",Fullname.getText().toString());

                fAuth.createUserWithEmailAndPassword(EmailRegister.getText().toString(),PasswordRegister.getText().toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful()) {
                                String userUID = fAuth.getInstance().getCurrentUser().getUid();
                                User userRegisteration = new User(Fullname.getText().toString(), EmailRegister.getText().toString());
                                firDatabaseUsers.child(userUID).setValue(userRegisteration);
                                userRegisteration.getManagerOf();
                                Toast.makeText(RegisterActivity.this, "Register complete!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), AccountActivity.class));
                                finish();

                            } else {
                                Toast.makeText(RegisterActivity.this, "Register failed try again later!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                });

            }
        });
    }
}
