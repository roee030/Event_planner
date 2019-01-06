package com.example.roeea.eventplanner.ObjectClasses;

public class Product {

    String name;
    String quantity;
    String pricePerItem;
    public Product() {
    }

    public Product(String name, String quantity ,String pricePerItem) {
        this.name = name;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
    }





    @Override
    public String toString() {
        return "Name=" +
                 name  +
                ", quantity=" + quantity +", price(PI)=" + pricePerItem +"|";
    }

    public String getPricePerItem() {
        return pricePerItem;
    }

    public void setPricePerItem(String pricePerItem) {
        this.pricePerItem = pricePerItem;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }



    public String getName() {
        return name;
    }


    public String getQuantity() {
        return quantity;
    }

}
