package com.example.roeea.eventplanner.ObjectClasses;

public class Product {

    private String name;
    private int quantity;
    private int price;

    public Product() {
    }

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
        this.quantity = 1;
    }

    @Override
    public String toString() {
        return "Product{" +
                ", name = '" + name + '\'' +
                ", price = " + price +
                ", quantity = " + quantity +
                '}';
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setPrice(int price) {this.price = price; }

    public int getPrice(){return this.price; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addToQuantity(){
        quantity += 1;
    }

    public void removeFromQuantity(){
        if(quantity > 0){
            quantity -= 1;
        }
    }

}
