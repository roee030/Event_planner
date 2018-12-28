package com.example.roeea.eventplanner.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.service.autofill.UserData;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.roeea.eventplanner.DataHolders.UserDataHolder;
import com.example.roeea.eventplanner.DatePickerFragment;
import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.R;
import com.example.roeea.eventplanner.TimePickerFragment;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import com.example.roeea.eventplanner.R;

public class EventCreationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener {
    private EditText eventName;
    private EditText eventDetails;
    private EditText eventDate;
    private EditText eventLOC;

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        eventDate = (EditText)findViewById(R.id.eventDate);
        eventDate.setText(hourOfDay+":"+minute,TextView.BufferType.EDITABLE);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        eventLOC = (EditText) findViewById(R.id.eventLOC);
        eventLOC.setText(currentDateString,TextView.BufferType.EDITABLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);
        eventName = (EditText) findViewById(R.id.eventName);
        eventDetails = (EditText)findViewById(R.id.event_about_input);
        eventDate = (EditText)findViewById(R.id.eventDate);
        eventLOC = (EditText) findViewById(R.id.eventLOC);

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker =new TimePickerFragment();
                timePicker.showNow(getSupportFragmentManager(),"Time picker");

            }
        });
        eventLOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.showNow(getSupportFragmentManager(),"Date picker");
            }
        });


    }
}
