package com.example.roeea.eventplanner.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.roeea.eventplanner.ObjectClasses.Product;
import com.example.roeea.eventplanner.R;
import com.example.roeea.eventplanner.Fragments.fragment_ProductList;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Product> {
    private List<Product> list;
    private Context context;

    TextView currentFoodName,
            currentCost,
            quantityText,
            addMeal,
            subtractMeal,
            removeMeal;

    public OrderAdapter(Context context, List<Product> myOrders) {
        super(context, 0, myOrders);
        this.list = myOrders;
        this.context = context;
    }


    public View getView(final int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.product_item,parent,false
            );
        }

        final Product currentProduct = getItem(position);

        currentFoodName = (TextView)listItemView.findViewById(R.id.selected_food_name);
        currentCost = (TextView)listItemView.findViewById(R.id.selected_food_amount);
        subtractMeal = (TextView)listItemView.findViewById(R.id.minus_meal);
        quantityText = (TextView)listItemView.findViewById(R.id.quantity);
        addMeal = (TextView)listItemView.findViewById(R.id.plus_meal);
        removeMeal = (TextView)listItemView.findViewById(R.id.delete_item);

        //Set the text of the meal, amount and quantity
        currentFoodName.setText(currentProduct.getName());
        currentCost.setText("ש\"ח"+" "+ (currentProduct.getPrice() * currentProduct.getQuantity()));
        quantityText.setText("x "+ currentProduct.getQuantity());

        //OnClick listeners for all the buttons on the ListView Item
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentProduct.addToQuantity();
                quantityText.setText("x "+ currentProduct.getQuantity());
                currentCost.setText("ש\"ח"+" "+ (currentProduct.getPrice() * currentProduct.getQuantity()));
                notifyDataSetChanged();
            }
        });

        subtractMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentProduct.removeFromQuantity();
                quantityText.setText("x "+currentProduct.getQuantity());
                currentCost.setText("ש\"ח"+" "+ (currentProduct.getPrice() * currentProduct.getQuantity()));
                notifyDataSetChanged();
            }
        });

        removeMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        return listItemView;
    }

}