package com.example.roeea.eventplanner.Fragments;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.roeea.eventplanner.Adapters.OrderAdapter;
import com.example.roeea.eventplanner.ObjectClasses.Product;
import com.example.roeea.eventplanner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductListFragment extends Fragment {

    private FirebaseDatabase fbdatabase  = FirebaseDatabase.getInstance();
    private DatabaseReference fEventRef = fbdatabase.getReference().child("Events");
    private String eventID;

    TextView mealTotalText;
    ArrayList<Product> products;

    public ProductListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ListView storedOrders = (ListView) view.findViewById(R.id.selected_food_list);

        Bundle bundle = this.getArguments();
        eventID = bundle.getString("eventID");
        if (eventID != null) {
            fEventRef = fEventRef.child(eventID);
        }




        getListItemData(fEventRef);
        mealTotalText = (TextView) view.findViewById(R.id.meal_total);
        OrderAdapter adapter = new OrderAdapter(view.getContext(), products);

        storedOrders.setAdapter(adapter);
        adapter.registerDataSetObserver(observer);
        return view;
    }

    public int calculateMealTotal(){
        int mealTotal = 0;
        for(Product order : products){
            mealTotal += order.getPrice() * order.getQuantity();
        }
        return mealTotal;
    }

    DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            setMealTotal();
        }
    };

    private void getListItemData(DatabaseReference fEventRef){
        fEventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Product>> t = new GenericTypeIndicator<ArrayList<Product>>() {};
                products.clear();
                products = dataSnapshot.child("products").getValue(t);


                // eventDate.append(dataSnapshot.child("date").getValue(String.class));
                Log.d("ProductListFragment", "pulling product list from event");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ProductListFragment", "Failed to read value.", error.toException());
            }
        });
    }

    public void setMealTotal(){
        mealTotalText.setText("ש\"ח"+" "+ calculateMealTotal());
    }
}



