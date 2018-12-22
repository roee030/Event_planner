package com.example.roeea.eventplanner.Server;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.roeea.eventplanner.Activities.MainActivity;
import com.example.roeea.eventplanner.Activities.RegisterActivity;
import com.example.roeea.eventplanner.DatabaseAPI.DatabaseHelper;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MiniServer {
    private static final MiniServer miniServerInstance = new MiniServer();
    private DatabaseHelper myDb = null;
    FirebaseAuth Mauth=FirebaseAuth.getInstance();
    private boolean flag;
    private MiniServer() {
    }

    /**
     * @return The instance of the local server.
     */
    public static MiniServer getServerInstance() {
        return MiniServer.miniServerInstance;
    }


    public boolean checkRegister(User user)
    {
        String pass = user.getPassword();
        String email = user.getEmail();
        flag = false;
        Mauth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                {
                   flag=true;

                }
            }
        });
        return false;
    }
}
