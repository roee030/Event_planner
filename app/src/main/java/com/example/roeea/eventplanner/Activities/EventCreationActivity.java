package com.example.roeea.eventplanner.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.roeea.eventplanner.DatePickerFragment;
import com.example.roeea.eventplanner.R;
import com.example.roeea.eventplanner.TimePickerFragment;
import com.example.roeea.eventplanner.dialog_of_product;

import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EventCreationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener ,dialog_of_product.DialogLisnnerforproducts {
    private EditText eventName;
    private EditText eventDetails;
    private EditText eventTimeEditText;
    private EditText eventDate;
    private EditText eventProduct;

    private Button addNewEventButton;

    private Time eventTime;

    ListView listOfProducts = null;
    private ArrayList<String>productsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);
        //layout
        eventName = (EditText) findViewById(R.id.eventName);
        eventDetails = (EditText)findViewById(R.id.event_about_input);
        eventTimeEditText = (EditText)findViewById(R.id.eventDate);
        eventDate = (EditText) findViewById(R.id.eventLOC);
        eventProduct = (EditText) findViewById((R.id.eventProduct));

        eventTime = (Time) eventTimeEditText.getText();


        //product array setting
    //    eventProductList = new ArrayList<Product>();
    //    productAdapter = new ArrayAdapter<User>(this,R.layout.activity_event_creation,R.id.);
        productsArrayList= new ArrayList<>();

        ///Dialog product list

        eventProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  openTimeDialog();
            }
        });


        eventTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker =new TimePickerFragment();
                timePicker.showNow(getSupportFragmentManager(),"Time picker");

            }
        });
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.showNow(getSupportFragmentManager(),"Date picker");
            }
        });


    }
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
        eventDate = (EditText) findViewById(R.id.eventLOC);
        eventDate.setText(currentDateString,TextView.BufferType.EDITABLE);
    }

    public void openTimeDialog()
    {
        dialog_of_product dialog = new dialog_of_product();
        dialog.show(getSupportFragmentManager(),"Example");
    }

    @Override
    public void applyText(String product) {
        productsArrayList.add(product);
        eventProduct.setText(productsArrayList.toString());
    }

    @Override
    public void clearList() {
        productsArrayList.clear();
    }
}
