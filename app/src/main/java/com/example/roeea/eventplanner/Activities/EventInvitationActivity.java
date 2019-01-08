package com.example.roeea.eventplanner.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.roeea.eventplanner.DatePickerFragment;
import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.Product;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.ProductsDialog;
import com.example.roeea.eventplanner.R;
import com.example.roeea.eventplanner.TimePickerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventInvitationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, ProductsDialog.ProductsDialogListener {
    private static final String TAG = "event invitation";

    String eventID;

    EditText editEventName;
    EditText editEventLoc;
    EditText editEventTime;
    EditText editEventDate;
    EditText editEventDetails;
    EditText editEventBudget;

    TextView eventName;
    TextView eventLocation;
    TextView eventTime;
    TextView eventDate;
    TextView eventDetails;
    TextView eventBudget;
    TextView eventProductCost;
    TextView txtUsers;
    RecyclerView productsList;
    RecyclerView usersList;
    Button accept;
    Button decline;
    Button editProducts;

    private FirebaseAuth fAuth;
    private FirebaseDatabase FBdb;
    DatabaseReference fEventRef;
    private String userID;
    private Event event;
    private List<Product> products = new ArrayList<>();
    private List<Product> tempProducts = new ArrayList<>();
    private List<User> guests = new ArrayList<>();

    int userStatus = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventID = getIntent().getStringExtra("eventID");

        fAuth = FirebaseAuth.getInstance();
        FBdb = FirebaseDatabase.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        //differentiating user permissions (0 - Invitee, 1 - Guest, 2 - Manager)
        checkUserEventStatus(eventID, userID);

    }

    private void runManagerLayout() {
        setContentView(R.layout.activity_event_management);
        eventProductCost = findViewById(R.id.txtTotalProductCost);

        pullEventDetailsFromDB(eventID, userStatus);

        accept = findViewById(R.id.btnAcceptChanges);
        editProducts = findViewById(R.id.btnEditProductList);
        editEventTime = findViewById(R.id.editEventTime);
        editEventDate = findViewById(R.id.editEventDate);
        productsList = findViewById(R.id.productsList);
        productsList.setLayoutManager(new LinearLayoutManager(this));
        productsList.setAdapter(new ProductsListAdapter(productsList));

        usersList = findViewById(R.id.usersList);
        usersList.setLayoutManager(new LinearLayoutManager(this));
        usersList.setAdapter(new UsersListAdapter(usersList));

        //not working...
//        editProducts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openProductListDialog();
//                productsList.getAdapter().notifyDataSetChanged();
//            }
//        });

        editEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.showNow(getSupportFragmentManager(), "Time picker");

            }
        });
        editEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.showNow(getSupportFragmentManager(), "Date picker");
            }
        });

        findViewById(R.id.btnInvite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventId = eventID;
                startActivity(
                        new Intent(Intent.ACTION_SEND)
                                .putExtra(Intent.EXTRA_TEXT, "Please join my event at https://sites.google.com/view/event-planner-ariel?eventId=" + eventId)
                                .setType("text/plain"));
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < products.size(); i++) {
                    event.getProducts().get(i).setQuantity(event.getProducts().get(i).getQuantity() + products.get(i).getQuantity());
                }
                event.updateTotalEventBudget();
                event.setName(editEventName.getText().toString());
                event.setLoc(editEventLoc.getText().toString());
                event.setDate(editEventDate.getText().toString());
                event.setTime(editEventTime.getText().toString());
                event.setDetails(editEventDetails.getText().toString());
                event.setBudget(editEventBudget.getText().toString());

                fEventRef.setValue(event);

                final DatabaseReference fUserRef = FBdb.getReference().child("Users").child(userID);
                fUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        fUserRef.removeEventListener(this);
                        User user = dataSnapshot.getValue(User.class);
                        user.getGuestIn().setProductsForEvent(eventID, products);
                        fUserRef.setValue(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

                Toast.makeText(EventInvitationActivity.this, "Saved changes successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), AccountActivity.class);
                startActivity(intent);

            }
        });


    }

    private void runGuestLayout() {
        setContentView(R.layout.activity_event_guest);
        eventProductCost = findViewById(R.id.txtTotalProductCost);

        pullEventDetailsFromDB(eventID, userStatus);

        accept = findViewById(R.id.btnAcceptChanges);
        productsList = findViewById(R.id.productsList);
        productsList.setLayoutManager(new LinearLayoutManager(this));
        productsList.setAdapter(new ProductsListAdapter(productsList));

        usersList = findViewById(R.id.usersList);
        usersList.setLayoutManager(new LinearLayoutManager(this));
        usersList.setAdapter(new UsersListAdapter(usersList));

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < products.size(); i++) {
                    event.getProducts().get(i).setQuantity(event.getProducts().get(i).getQuantity() + products.get(i).getQuantity());
                }
                event.updateTotalEventBudget();

                fEventRef.setValue(event);

                final DatabaseReference fUserRef = FBdb.getReference().child("Users").child(userID);
                fUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        fUserRef.removeEventListener(this);
                        User user = dataSnapshot.getValue(User.class);
                        user.getGuestIn().setProductsForEvent(eventID, products);
                        fUserRef.setValue(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

                Toast.makeText(EventInvitationActivity.this, "Saved changes successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), AccountActivity.class);
                startActivity(intent);

            }
        });


    }

    private void runInviteLayout() {
        Log.e(TAG, "inside runInviteLayout");
        setContentView(R.layout.activity_event_invitation);
        eventProductCost = findViewById(R.id.txtTotalProductCost);

        pullEventDetailsFromDB(eventID, userStatus);



        accept = findViewById(R.id.btnAccept);
        decline = findViewById(R.id.btnDecline);
        productsList = findViewById(R.id.productsList);
        productsList.setLayoutManager(new LinearLayoutManager(this));
        productsList.setAdapter(new ProductsListAdapter(productsList));

        usersList = findViewById(R.id.usersList);
        usersList.setLayoutManager(new LinearLayoutManager(this));
        usersList.setAdapter(new UsersListAdapter(usersList));

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.getInvited().remove(userID);
                event.getGuests().add(userID);
                for (int i = 0; i < products.size(); i++) {
                    event.getProducts().get(i).setQuantity(event.getProducts().get(i).getQuantity() + products.get(i).getQuantity());
                }
                event.updateTotalEventBudget();

                fEventRef.setValue(event);

                final DatabaseReference fUserRef = FBdb.getReference().child("Users").child(userID);
                fUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        fUserRef.removeEventListener(this);
                        User user = dataSnapshot.getValue(User.class);
                        user.getGuestIn().setProductsForEvent(eventID, products);
                        user.getInvitedTo().getInviteeEvent().remove(eventID);
                        user.getGuestIn().getEvents().put(eventID, event.getProducts());
                        fUserRef.setValue(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

                Toast.makeText(EventInvitationActivity.this, "Event Accepted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), AccountActivity.class);
                startActivity(intent);

            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.getInvited().remove(userID);
                fEventRef.setValue(event);
                Toast.makeText(EventInvitationActivity.this, "Event Declined!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), AccountActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkUserEventStatus(String eventID, final String userID) {
        fEventRef = FBdb.getReference().child("Events").child(eventID);
        fEventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fEventRef.removeEventListener(this);
                event = dataSnapshot.getValue(Event.class);
                if(event.getInvited().contains(userID)) userStatus = 0;
                if(event.getGuests().contains(userID)) userStatus = 1;
                if(event.getMannager().contains(userID)) userStatus = 2;
                if(userStatus == 0) runInviteLayout();
                else if(userStatus == 1) runGuestLayout();
                else if(userStatus == 2) runManagerLayout();
                else Toast.makeText(EventInvitationActivity.this, "Error: you don't appear in invited list", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "pulling details from event: " + event.getName());
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateUiFromEvent(int status) {

        switch (status) {
            case 0 : {
                eventName = findViewById(R.id.txtInvitedTo);
                eventLocation = findViewById(R.id.txtLoc);
                eventTime = findViewById(R.id.txtTime);
                eventDate = findViewById(R.id.txtDate);
                eventDetails = findViewById(R.id.txtDetails);
                eventBudget = findViewById(R.id.txtBudget);
                usersList = findViewById(R.id.usersList);
                txtUsers = findViewById(R.id.txtUsers);

                eventName.setText("You have been invited to " + event.getName() + "!");
                eventLocation.append(event.getLoc());
                eventTime.append(event.getTime());
                eventDate.append(event.getDate());
                eventDetails.append(event.getDetails());
                eventBudget.append(event.getBudget());
                products.clear();
                for (Product product : event.getProducts()) {
                    Product myProduct = new Product();
                    myProduct.setName(product.getName());
                    myProduct.setPrice(product.getPrice());
                    myProduct.setQuantity(0);
                    products.add(myProduct);
                }
                productsList.getAdapter().notifyDataSetChanged();
            }
            break;
            case 1 : {
                eventName = findViewById(R.id.txtEventName);
                eventLocation = findViewById(R.id.txtLoc);
                eventTime = findViewById(R.id.txtTime);
                eventDate = findViewById(R.id.txtDate);
                eventDetails = findViewById(R.id.txtDetails);
                eventBudget = findViewById(R.id.txtBudget);
                usersList = findViewById(R.id.usersList);
                txtUsers = findViewById(R.id.txtUsers);

                eventName.setText("Event's name: " + event.getName());
                eventLocation.append(event.getLoc());
                eventTime.append(event.getTime());
                eventDate.append(event.getDate());
                eventDetails.append(event.getDetails());
                eventBudget.append(event.getBudget());

                products.clear();

                final DatabaseReference fUserRef = FBdb.getReference().child("Users").child(userID);
                fUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        fUserRef.removeEventListener(this);
                        User user = dataSnapshot.getValue(User.class);
                        products = user.getGuestIn().getProductsForEvent(eventID);
                        productsList.getAdapter().notifyDataSetChanged();
                        Log.d(TAG, "pulling details from user: " + user.getUsername());
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
            break;
            case 2 : {
                eventName = findViewById(R.id.txtEventName);
                eventLocation = findViewById(R.id.txtLoc);
                eventTime = findViewById(R.id.txtTime);
                eventDate = findViewById(R.id.txtDate);
                eventDetails = findViewById(R.id.txtDetails);
                eventBudget = findViewById(R.id.txtBudget);
                usersList = findViewById(R.id.usersList);
                txtUsers = findViewById(R.id.txtUsers);
                editEventName = findViewById(R.id.editEventName);
                editEventLoc = findViewById(R.id.editEventLoc);
                editEventDetails = findViewById(R.id.editEventDetails);
                editEventBudget = findViewById(R.id.editEventBudget);


                eventName.setText("Event's name:");
                editEventName.setHint(event.getName());
                editEventLoc.setHint(event.getLoc());
                editEventDate.setHint(event.getDate());
                editEventTime.setHint(event.getTime());
                editEventDetails.setHint(event.getDetails());
                editEventBudget.setHint(event.getBudget());

                final DatabaseReference fUserRef = FBdb.getReference().child("Users").child(userID);
                fUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        fUserRef.removeEventListener(this);
                        User user = dataSnapshot.getValue(User.class);
                        products = user.getGuestIn().getProductsForEvent(eventID);
                        productsList.getAdapter().notifyDataSetChanged();
                        Log.d(TAG, "pulling details from user: " + user.getUsername());
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

            }
        }

    }


    private class UsersListAdapter extends RecyclerView.Adapter {
        private final RecyclerView recyclerView;

        public UsersListAdapter(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.users_list_item, recyclerView, false);
            return new RecyclerView.ViewHolder(view) {};        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            String userName = guests.get(position).getUsername();
            TextView name = holder.itemView.findViewById(R.id.txtUsername);
            name.setText(userName);
        }

        @Override
        public int getItemCount() {
            return guests.size();
        }
    }

    private class ProductsListAdapter extends RecyclerView.Adapter {
        private final RecyclerView recyclerView;

        public ProductsListAdapter(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.product_item, recyclerView, false);
            return new RecyclerView.ViewHolder(view) {};
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            final Product product = products.get(position);
            final Product productFromEvent = event.getProducts().get(position);
            TextView productName = holder.itemView.findViewById(R.id.productName);
            final TextView totalQuantity = holder.itemView.findViewById(R.id.totalQuantity);
            final TextView quantity = holder.itemView.findViewById(R.id.quantity);

            productName.setText(String.format("%s (%s)", product.getName(), NumberFormat.getCurrencyInstance().format(product.getPrice())));
            totalQuantity.setText("" + productFromEvent.getQuantity());
            quantity.setText(""+product.getQuantity());
            quantity.setTag(product.getQuantity());
            productFromEvent.setQuantity(productFromEvent.getQuantity() - product.getQuantity());
            calcTotalProductsCost(products, Integer.parseInt(event.getBudget()));


            holder.itemView.findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product.setQuantity(Math.max(0, (Integer) quantity.getTag() - 1));
                    quantity.setTag(product.getQuantity());
                    quantity.setText("" + product.getQuantity());
                    totalQuantity.setText("" + (productFromEvent.getQuantity() + product.getQuantity()));
                    calcTotalProductsCost(products, Integer.parseInt(event.getBudget()));
                }
            });

            holder.itemView.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product.setQuantity((Integer) quantity.getTag() + 1);
                    quantity.setTag(product.getQuantity());
                    quantity.setText("" + product.getQuantity());
                    totalQuantity.setText("" + (productFromEvent.getQuantity() + product.getQuantity()));
                    calcTotalProductsCost(products, Integer.parseInt(event.getBudget()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        //function to calculate if the user's products cost is in range of 20% above or 20% below the event's budget
        private void calcTotalProductsCost(List<Product> products, int budget){
            int cost = 0;
            accept.setEnabled(false);
            for( Product product : products){
                cost += product.getQuantity()*product.getPrice();
            }
            int budgetProximity = (int)(budget*(20.0f/100.0f));
            if(cost < budget-budgetProximity) {
                eventProductCost.setTextColor(Color.RED);
            }
            else if(cost > budget+budgetProximity) eventProductCost.setTextColor(Color.RED);
            else {
                eventProductCost.setTextColor(Color.GREEN);
                accept.setEnabled(true);
            }
            eventProductCost.setText(String.format("Your cost: %s", NumberFormat.getCurrencyInstance().format(cost)));
        }
    }

    private void pullEventDetailsFromDB(final String eventID, final int userStatus) {
        if(fAuth.getCurrentUser() == null)
        {
            Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);
            finish();
        }


        //adding the current user to the invitee list and updating UI with event's info
        fEventRef = FBdb.getReference().child("Events").child(eventID);
        fEventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fEventRef.removeEventListener(this);
                event = dataSnapshot.getValue(Event.class);
                event.getInvited().add(userID);///TODO: WHY?
                fEventRef.child("invited").setValue(event.getInvited());
                Log.d(TAG, "pulling details from event: " + event.getName());
                updateUiFromEvent(userStatus);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        //Adding users from the event to the Users list to show with recyclerview
        final DatabaseReference fUsersRef = FBdb.getReference().child("Users");
        fUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if(user.getGuestIn().getEvents().containsKey(eventID)){
                        guests.add(user);
                    }
                }
                txtUsers.append("(" + guests.size() + ")");
                usersList.getAdapter().notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String sHour="",sMin="";
        if(hourOfDay<10)sHour="0"+hourOfDay;
        else sHour = ""+hourOfDay;
        if(minute<10)sMin="0"+minute;
        else sMin=""+minute;
        editEventTime.setText(sHour + ":" + sMin, TextView.BufferType.EDITABLE);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        editEventDate.setText(currentDateString, TextView.BufferType.EDITABLE);
    }

    //copy products
    public void openProductListDialog() {
        ProductsDialog dialog = new ProductsDialog();
        dialog.setProducts(products);
        dialog.showNow(getSupportFragmentManager(), "Products List");
    }

    @Override
    public void listUpdated(List<Product> products) {
        this.products = products;
    }
}
