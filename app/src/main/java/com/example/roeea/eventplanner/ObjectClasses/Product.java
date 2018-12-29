package com.example.roeea.eventplanner.ObjectClasses;

public class Product {

    String name;
    double quantity;

    public Product() {
    }

    public Product(String name, double quantity) {
        this.name = name;
        this.quantity = quantity;
    }





    @Override
    public String toString() {
        return "Product{" +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }


    public void setName(String name) {
        this.name = name;
    }



    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }



    public String getName() {
        return name;
    }


    public double getQuantity() {
        return quantity;
    }

}
