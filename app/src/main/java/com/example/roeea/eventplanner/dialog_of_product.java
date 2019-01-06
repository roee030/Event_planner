package com.example.roeea.eventplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roeea.eventplanner.Activities.EventCreationActivity;

public class dialog_of_product extends AppCompatDialogFragment {
    private EditText ProductName;
    private EditText ProductQuantity;
    private EditText ProductPricePerItem;

    private ListView productListView;
    private DialogLisnnerforproducts lisnner;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.listofproducts,null);
        builder.setView(view).setTitle("Add product to Event").setNegativeButton("add!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String item = ProductName.getText().toString();
                String quantity =ProductQuantity.getText().toString();
                String price = ProductPricePerItem.getText().toString() ;
                if(validateForm()){                lisnner.applyText(item,quantity,price);
                }



            }
        }).setPositiveButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNeutralButton("clear list", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lisnner.clearList();
            }
        });
        ProductName =view.findViewById(R.id.productname);
        ProductQuantity = view.findViewById(R.id.productquantity);
        ProductPricePerItem = view.findViewById(R.id.productprice);
        return  builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            lisnner = (DialogLisnnerforproducts)context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString()+"must implement Dialog Lissner");
        }
    }

    public interface DialogLisnnerforproducts
    {
        void applyText(String item,String quantity,String price);
        void clearList();
    }
    private boolean validateForm() {
        boolean valid = true;

        String productName = ProductName.getText().toString();
        if (TextUtils.isEmpty(productName)) {
            ProductName.setError("Required.");
            valid = false;
        } else {
            ProductName.setError(null);
        }

        String password = ProductPricePerItem.getText().toString();
        if (TextUtils.isEmpty(password)) {
            ProductPricePerItem.setError("Required.");
            valid = false;
        } else {
            ProductPricePerItem.setError(null);
        }
        String quantity = ProductQuantity.getText().toString();
        if (TextUtils.isEmpty(quantity)) {
            ProductQuantity.setError("Required.");
            valid = false;
        } else {
            ProductQuantity.setError(null);
        }
        if(!valid){
        Toast.makeText(getContext(),"Enter All fields to complete",Toast.LENGTH_LONG).show();}
        return valid;
    }
}
