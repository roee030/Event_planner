package com.example.roeea.eventplanner.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roeea.eventplanner.DataHolders.UserDataHolder;
import com.example.roeea.eventplanner.DatabaseAPI.DatabaseHelper;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.R;
import com.example.roeea.eventplanner.Server.MiniServer;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper myDBH;
    private MiniServer server;
    private Button login;
    private Button RegisterButton;
    private EditText Email;
    private EditText Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MiniServer.getServerInstance().setContext(getBaseContext());

        Firebase.setAndroidContext(this);
        Email = (EditText) findViewById(R.id.EmailField);
        Password = (EditText) findViewById(R.id.PasswordField);
        RegisterButton = (Button) findViewById(R.id.MoveToRegisterationActivity);
        login = (Button) findViewById(R.id.LoginBt);

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


    }


    private void startSignIn()
    {
        String UserMail = Email.getText().toString();
        String UserPassword = Password.getText().toString();
        if(TextUtils.isEmpty(UserMail)||TextUtils.isEmpty(UserPassword))
        {
            Toast.makeText(MainActivity.this,"Fields are empty",Toast.LENGTH_LONG).show();
        }
        else {
            User user = MiniServer
                    .getServerInstance()
                    .login(UserMail, UserPassword, MainActivity.this);
            if (user != null) {
                UserDataHolder.getUserDataHolderInstance().setAuthenticatedUser(user);
                startActivity(new Intent(this, AccountActivity.class));
            }
        }
    }
}
