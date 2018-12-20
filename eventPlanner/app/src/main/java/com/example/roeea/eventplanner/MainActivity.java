package com.example.roeea.eventplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login;
    private Firebase mRRef;
    private FirebaseAuth mAuth;
    private EditText Email;
    private EditText Password;
    private FirebaseAuth mAuthlistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
        mRRef = new Firebase("https://event-planner-d32e9.firebaseio.com/");
        Email = (EditText) findViewById(R.id.EmailField);
        Password = (EditText) findViewById(R.id.PasswordField);
        login = (Button)findViewById(R.id.LoginBt);
        login.setOnClickListener(this);



    }

    private void startSignIn()
    {
        String EmailUser = Email.getText().toString();
        String PasswordUser = Password.getText().toString();
        if(TextUtils.isEmpty(EmailUser)||TextUtils.isEmpty(PasswordUser))
        {
            Toast.makeText(MainActivity.this,"Fields are empty",Toast.LENGTH_LONG).show();
        }

        else {
            mAuth.signInWithEmailAndPassword(EmailUser, PasswordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Sign in faild", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Wellcome again!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }



    }




    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.LoginBt:
                startSignIn();
                break;
            case R.id.RegisterBT:
                Intent register = new Intent(MainActivity.this,Register.class);
                startActivity(register);
                break;
                default:
                    break;
        }




    }
}
