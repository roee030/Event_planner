package com.example.roeea.eventplanner.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.roeea.eventplanner.DatePickerFragment;
import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.Manager;
import com.example.roeea.eventplanner.ObjectClasses.Product;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.ObjectClasses.get;
import com.example.roeea.eventplanner.R;
import com.example.roeea.eventplanner.TimePickerFragment;
import com.example.roeea.eventplanner.ViewModels.AccountViewModel;
import com.example.roeea.eventplanner.dialog_of_product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventCreationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, dialog_of_product.DialogLisnnerforproducts {
    private EditText eventName;
    private EditText eventDetails;
    private EditText eventLoc;
    private EditText eventTimeEditText;
    private EditText eventDate;
    private EditText eventProduct;

    private FirebaseDatabase fbdatabase  = FirebaseDatabase.getInstance();
    private DatabaseReference fEventRef = fbdatabase.getReference().child("Events");
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    private Button addNewEventButton;

    private Time eventTime;

    ListView listOfProducts = null;
    private ArrayList<Product> productsArrayList;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);
        //layout
        eventName = (EditText) findViewById(R.id.eventName);
        eventLoc = (EditText) findViewById(R.id.eventLoc);
        eventDetails = (EditText) findViewById(R.id.event_about_input);
        eventTimeEditText = (EditText) findViewById(R.id.eventTime);
        eventDate = (EditText) findViewById(R.id.eventDate);
        eventProduct = (EditText) findViewById((R.id.eventProduct));

        addNewEventButton = (Button)findViewById(R.id.submitbtn);

        //Firebase setting

        if(fAuth.getCurrentUser()==null)
        {
            Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);
            finish();
        }



        //product array setting
        //    eventProductList = new ArrayList<Product>();
        //    productAdapter = new ArrayAdapter<User>(this,R.layout.activity_event_creation,R.id.);
        productsArrayList = new ArrayList<>();

        ///Dialog product list

        eventProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog();
            }
        });
        /*
        //submit button to create a new event and add to DB
         */
        addNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String eventID = fEventRef.push().getKey();
                List<String> manager = new ArrayList<>();
                manager.add(fAuth.getUid());
                addEventIDToUser(eventID);
                addEventToFireBase(eventID, eventName.getText().toString(), eventLoc.getText().toString(),
                        eventDate.getText().toString(), eventTimeEditText.getText().toString(),
                        eventDetails.getText().toString(), productsArrayList, manager, new ArrayList<String>(), new ArrayList<String>());
                Intent intent = new Intent(getBaseContext(), AccountActivity.class);
                startActivity(intent);

            }
        });
        eventTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.showNow(getSupportFragmentManager(), "Time picker");

            }
        });
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.showNow(getSupportFragmentManager(), "Date picker");
            }
        });


    }

    private void addEventToFireBase(String eventID, String eventName, String eventLoc, String eventDate,
                                    String eventTime, String eventDetails, ArrayList<Product> productsArrayList,
                                    List<String> MangerUids, List<String> GuestsUids, List<String> invited) {
        Event event = new Event(eventID,eventName,eventLoc,eventDate,eventTime,eventDetails,productsArrayList);
        fbdatabase.getReference().child("Events").child(eventID).setValue(event);
    }

    private void addEventIDToUser(final String eventID) {
        String userID = fAuth.getCurrentUser().getUid();
        final DatabaseReference userRef = fbdatabase.getReference().child("Users").child(userID);
        user = new User();

        user.getUserByUID(userID, new get<User>() {
            @Override
            public void callBack(User user) {
                EventCreationActivity.this.user=user;
                Manager usermanagerof = user.getManagerOf();
                if(user.getManagerOf() == null) Log.w("EventCreation", "usermanagerof is null");
                usermanagerof.addEventtoList(eventID);
                userRef.child("managerOf").setValue(usermanagerof);
            }
        });
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//
//
////
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.e("EventCreationActivity ", "Failed to read value.", error.toException());
//            }
//        });




    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        eventTimeEditText = (EditText) findViewById(R.id.eventTime);
        String sHour="",sMin="";
        if(hourOfDay<10)sHour="0"+hourOfDay;
        else sHour = ""+hourOfDay;
        if(minute<10)sMin="0"+minute;
        else sMin=""+minute;
        eventTimeEditText.setText(sHour + ":" + sMin, TextView.BufferType.EDITABLE);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        eventDate = (EditText) findViewById(R.id.eventDate);
        eventDate.setText(currentDateString, TextView.BufferType.EDITABLE);
    }

    public void openTimeDialog() {
        dialog_of_product dialog = new dialog_of_product();
        dialog.show(getSupportFragmentManager(), "Example");

    }

    @Override
    public void applyText(String product) {
        Product p = new Product(product,1);
        productsArrayList.add(p);
        eventProduct.setText(productsArrayList.toString());
    }

    @Override
    public void clearList() {
        productsArrayList.clear();
    }

}
