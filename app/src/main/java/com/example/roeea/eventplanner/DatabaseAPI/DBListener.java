package com.example.roeea.eventplanner.DatabaseAPI;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface DBListener {
    public void onStart();
    public void onSuccess();
    public void onFailed();
}
