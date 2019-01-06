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
import com.example.roeea.eventplanner.ObjectClasses.get;
import com.example.roeea.eventplanner.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button RegisterButton;
    private EditText Email;
    private EditText Password;
    private FirebaseAuth mAuth;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


        Firebase.setAndroidContext(this);
        Email = (EditText) findViewById(R.id.EmailField);
        Password = (EditText) findViewById(R.id.PasswordField);
        RegisterButton = (Button) findViewById(R.id.MoveToRegisterationActivity);
        login = (Button) findViewById(R.id.LoginBt);
        if(mAuth.getUid() != null)
            user.getUserByUID(mAuth.getUid(), new get<User>() {
                @Override
                public void callBack(User user) {
                    MainActivity.this.user = user;
                }
            });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
            }
        });



        // Sending invite stuff, move to another place
        findViewById(R.id.btnInvite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventId = "-LVUftvXIO_ezSZlw3dP";
                startActivity(
                        new Intent(Intent.ACTION_SEND)
                                .putExtra(Intent.EXTRA_TEXT, "Please join my event at https://sites.google.com/view/event-planner-ariel?eventId=" + eventId)
                                .setType("text/plain"));
            }
        });


    }

    private String receiveInvite() {
        if (getIntent().getData() != null && getIntent().getData().getQueryParameter("eventId") != null) {
            String eventId = getIntent().getData().getQueryParameter("eventId");
            Toast.makeText(this, "Got " + eventId, Toast.LENGTH_LONG);
            return eventId;
            // Start event activity with this eventId as a string extra (putExtra)
        } else return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Main Activity", "On start");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.i("Main Activity/onStart", "Uid = " + mAuth.getUid());
            startAccountActivity();
        }
    }

    private void register(String email, String password) {
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        } else {

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        // [END create_user_with_email]
    }

    private void startSignIn() {
        String UserMail = Email.getText().toString();
        String UserPassword = Password.getText().toString();
//        if (TextUtils.isEmpty(UserMail) || TextUtils.isEmpty(UserPassword)) {
//            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();
//        }
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(UserMail, UserPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("MainActivity", "auth succedded");
                    user.getUserByUID(mAuth.getUid(), new get<User>() {
                        @Override
                        public void callBack(User user) {
                            MainActivity.this.user = user;
                            if (user == null)
                                Log.e("MainActivity/startSign","user is null + " + mAuth.getUid());
                        }
                    });
                    startAccountActivity();
                }
            }
        });
    }

    private void startAccountActivity() {
        startActivity(new Intent(MainActivity.this, AccountActivity.class)
                .putExtra("eventID", receiveInvite()));
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = Email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Email.setError("Required.");
            valid = false;
        } else {
            Email.setError(null);
        }

        String password = Password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Password.setError("Required.");
            valid = false;
        } else {
            Password.setError(null);
        }

        return valid;
    }

}


