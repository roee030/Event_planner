package com.example.roeea.eventplanner.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.ObjectClasses.get;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

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