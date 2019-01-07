package com.example.roeea.eventplanner.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.roeea.eventplanner.DatePickerFragment;
import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.Manager;
import com.example.roeea.eventplanner.ObjectClasses.Product;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.ObjectClasses.get;
import com.example.roeea.eventplanner.ProductsDialog;
import com.example.roeea.eventplanner.R;
import com.example.roeea.eventplanner.TimePickerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventCreationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, ProductsDialog.ProductsDialogListener {
    private EditText eventName;
    private EditText eventDetails;
    private EditText eventLoc;
    private EditText eventTimeEditText;
    private EditText eventDate;
    private TextView eventProduct;
    private EditText eventBudget;

    private FirebaseDatabase fbdatabase  = FirebaseDatabase.getInstance();
    private DatabaseReference fEventRef = fbdatabase.getReference().child("Events");
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    private Button addNewEventButton;

    private Time eventTime;

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
        eventProduct = findViewById(R.id.eventProduct);
        eventBudget = (EditText) findViewById(R.id.eventBudget);

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
                openProductListDialog();
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
                manager.add(fAuth.getCurrentUser().getUid());
                addEventIDToUser(eventID);
                Event event = addEventToFireBase(eventID, eventName.getText().toString(), eventLoc.getText().toString(),
                        eventDate.getText().toString(), eventTimeEditText.getText().toString(),
                        eventDetails.getText().toString(), productsArrayList, eventBudget.getText().toString(),
                        manager, new ArrayList<String>(), new ArrayList<String>());
                Intent intent = new Intent(getBaseContext(), SearchUserActivity.class);
                intent.putExtra("eventID",eventID);
                intent.putStringArrayListExtra("invited", (ArrayList<String>) event.getInvited());
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

    private Event addEventToFireBase(String eventID, String eventName, String eventLoc, String eventDate,
                                    String eventTime, String eventDetails, ArrayList<Product> productsArrayList, String budget,
                                    List<String> ManagerUids, List<String> GuestsUids, List<String> invited) {
        Event event = new Event(eventID,eventName,eventLoc,eventDate,eventTime,eventDetails,productsArrayList, budget);
        event.setMannager(ManagerUids);
        Log.e("addEventToFirebase", "invited = " + invited.toString());
        fbdatabase.getReference().child("Events").child(eventID).setValue(event);
        return event;
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

    public void openProductListDialog() {
        ProductsDialog dialog = new ProductsDialog();
        dialog.setProducts(productsArrayList);
        dialog.showNow(getSupportFragmentManager(), "Products List");
    }

    @Override
    public void listUpdated(List<Product> products) {
        productsArrayList = (ArrayList<Product>) products;
    }
}
