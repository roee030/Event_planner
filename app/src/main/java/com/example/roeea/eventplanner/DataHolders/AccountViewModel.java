package com.example.roeea.eventplanner.DataHolders;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.roeea.eventplanner.Activities.AccountActivity;
import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.ObjectClasses.get;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.database.FirebaseDatabase.*;

public class AccountViewModel extends ViewModel {
    private MutableLiveData<User> user = new MutableLiveData<>();
    private DatabaseReference fb;
    private FirebaseAuth mAuth;

    public LiveData<User> getUser()
    {
        return user;
    }

    public void loadUseer()
    {
        if(user == null) {
            user = new MutableLiveData<>();
            fb = getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            String Uid = mAuth.getUid();
            User user = new User();
            user.getUserByUID(Uid, new get<User>() {
                @Override
                public void callBack(User user) {
                    AccountViewModel.this.user.setValue(user);
                }
            });
        }
    }
}