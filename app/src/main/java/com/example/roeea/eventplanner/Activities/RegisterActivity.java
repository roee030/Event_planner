package com.example.roeea.eventplanner.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roeea.eventplanner.DatabaseAPI.DatabaseHelper;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.R;
import com.example.roeea.eventplanner.Server.MiniServer;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth Mauth;
    private DatabaseHelper db;
    private Firebase mRRef;
    private FirebaseAuth mAuth;
    private EditText Email;
    private EditText Password;
    private EditText Fullname;
    private Button RegistrationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MiniServer.getServerInstance().setContext(getBaseContext());

        Password = (EditText) findViewById(R.id.passwordtextregister);
        Fullname = (EditText) findViewById(R.id.fullnametextregister);
        Email = (EditText) findViewById(R.id.emailtextregister);
        RegistrationButton = (Button) findViewById(R.id.registerbutton);

        MiniServer.getServerInstance().setContext(getBaseContext());

        RegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("RegisterActivity", "lalal");
                User user = new User(Fullname.toString(), Password.toString(), Email.toString());
                MiniServer server = MiniServer.getServerInstance();
                boolean register_flag = server.preformRegistration(user.getEmail(), RegisterActivity.this);
            }
        });
    }
}
