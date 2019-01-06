package com.example.roeea.eventplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.roeea.eventplanner.ObjectClasses.Product;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class ProductsDialog extends AppCompatDialogFragment {

    private ProductsDialogListener listener;
    private ArrayList<Product> products;

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_product,null);
        initializeProductsList((RecyclerView) view.findViewById(R.id.productsList));
        builder.setView(view).setTitle("Products for Event")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.listUpdated(products);
            }
        }).setNegativeButton("clear list", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.listUpdated(new ArrayList<Product>());
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            }
        });
        return dialog;
    }

    private void initializeProductsList(RecyclerView productsList) {
        productsList.setLayoutManager(new LinearLayoutManager(getContext()));
        productsList.setAdapter(new ProductsListAdapter(productsList));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ProductsDialogListener)context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString()+"must implement Dialog Lissner");
        }
    }

    public interface ProductsDialogListener
    {
        void listUpdated(ArrayList<Product> products);
    }

    private class ProductsListAdapter extends RecyclerView.Adapter {
        private final RecyclerView recyclerView;

        public ProductsListAdapter(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.add_product_item, recyclerView, false);
            ProdcutViewHolder viewHolder = new ProdcutViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            String name = null;
            String price = null;
            if (position < products.size()) {
                Product product = products.get(position);
                name = product.getName();
                price = NumberFormat.getCurrencyInstance().format(product.getPrice());
            }
            final EditText productName = holder.itemView.findViewById(R.id.productName);
            final EditText productPrice = holder.itemView.findViewById(R.id.productPrice);
            productName.setText(name);
            productPrice.setText(price);

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    updateProductInList(position,
                            productName.getText().toString(),
                            productPrice.getText().toString());
                }
            };
            productName.addTextChangedListener(textWatcher);
            productPrice.addTextChangedListener(textWatcher);

        }

        private void updateProductInList(int position, String name, String price) {
            if (name.isEmpty() || price.isEmpty()) {
                return;
            }
            Product product;
            if (position < products.size()) {
                product = products.get(position);
            } else {
                product = new Product();
                products.add(product);
                notifyItemInserted(products.size());
            }
            product.setName(name);
            int priceInt = 0;
            try {
                priceInt = Integer.parseInt(price);
            } catch (NumberFormatException e) {
                //ignore
            }
            try {
                priceInt = NumberFormat.getCurrencyInstance().parse(price).intValue();
            } catch (ParseException e) {
                // ignore
            }
            product.setPrice(priceInt);
        }

        @Override
        public int getItemCount() {
            return products.size()+1;
        }
    }

    private class ProdcutViewHolder extends RecyclerView.ViewHolder {

        public ProdcutViewHolder(View view) {
            super(view);
        }
    }
}
