package com.example.roeea.eventplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class dialog_of_product extends AppCompatDialogFragment {
    private EditText editText;
    private EditText productlist;

    private ListView productListView;
    private DialogLisnnerforproducts lisnner;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.product_item,null);
        builder.setView(view).setTitle("Add product to Event").setNegativeButton("add!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = editText.getText().toString();
                lisnner.applyText(item);

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNeutralButton("clear list", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lisnner.clearList();
            }
        });
        editText =view.findViewById(R.id.productField);

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
        void applyText(String product);
        void clearList();
    }
}
