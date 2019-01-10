package com.example.roeea.eventplanner.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.roeea.eventplanner.ObjectClasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.google.firebase.database.FirebaseDatabase.*;

public class UserViewModel extends ViewModel {
    private MutableLiveData<User> user = new MutableLiveData<>();
    private DatabaseReference fb;
    private FirebaseAuth mAuth;
    private static final String TAG = "UserViewModel";

    public LiveData<User> getUser()
    {
        return user;
    }

    public void loadUseer()
    {
            fb = getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            String Uid = mAuth.getUid();
            fb.child("Users").child(Uid).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            UserViewModel.this.user.setValue(user);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
    }
}