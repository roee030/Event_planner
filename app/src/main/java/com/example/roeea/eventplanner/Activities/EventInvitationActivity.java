package com.example.roeea.eventplanner.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roeea.eventplanner.ObjectClasses.Event;
import com.example.roeea.eventplanner.ObjectClasses.Product;
import com.example.roeea.eventplanner.ObjectClasses.User;
import com.example.roeea.eventplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class EventInvitationActivity extends AppCompatActivity {
    private static final String TAG = "event invitation";

    String eventID;

    TextView invitedTo;
    TextView eventLocation;
    TextView eventTime;
    TextView eventDate;
    TextView eventDetails;
    TextView eventBudget;
    RecyclerView productsList;
    TextView eventProductCost;
    Button accept;
    Button decline;

    private FirebaseAuth fAuth;
    private FirebaseDatabase FBdb;
    DatabaseReference fEventRef;
    private String userID;
    private Event event;
    private List<Product> products = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_invitation);
        eventID = getIntent().getStringExtra("eventID");

        fAuth = FirebaseAuth.getInstance();
        FBdb = FirebaseDatabase.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        eventProductCost = findViewById(R.id.txtTotalProductCost);
        pullEventDetailsFromDB(eventID);

        accept = findViewById(R.id.btnAccept);
        decline = findViewById(R.id.btnDecline);
        productsList = findViewById(R.id.productsList);
        productsList.setLayoutManager(new LinearLayoutManager(this));
        productsList.setAdapter(new ProductsListAdapter(productsList));




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

    private void updateUiFromEvent() {
        invitedTo = findViewById(R.id.txtInvitedTo);
        eventLocation = findViewById(R.id.txtLoc);
        eventTime = findViewById(R.id.txtTime);
        eventDate = findViewById(R.id.txtDate);
        eventDetails = findViewById(R.id.txtDetails);
        eventBudget = findViewById(R.id.txtBudget);
        invitedTo.append(event.getName());
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
            quantity.setText(""+0);
            quantity.setTag(new Integer(0));

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

        private void calcTotalProductsCost(List<Product> products, int budget){
            int cost = 0;
            for( Product product : products){
                cost += product.getQuantity()*product.getPrice();
            }
            int budgetProximity = (int)(budget*(20.0f/100.0f));
            if(cost < budget-budgetProximity) eventProductCost.setTextColor(Color.RED);
            else if(cost > budget+budgetProximity) eventProductCost.setTextColor(Color.RED);
            else eventProductCost.setTextColor(Color.GREEN);
            eventProductCost.setText(cost+"");
        }
    }

    private void pullEventDetailsFromDB(final String eventID) {
        if(fAuth.getCurrentUser() == null)
        {
            Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);
            finish();
        }


        fEventRef = FBdb.getReference().child("Events").child(eventID);
        fEventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fEventRef.removeEventListener(this);
                event = dataSnapshot.getValue(Event.class);
                event.getInvited().add(userID);
                fEventRef.child("invited").setValue(event.getInvited());
                Log.d(TAG, "pulling details from event: " + event.getName());
                updateUiFromEvent();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
