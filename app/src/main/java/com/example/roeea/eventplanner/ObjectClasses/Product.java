package com.example.roeea.eventplanner.ObjectClasses;

public class Product {

    String productID;
    String name;
    double costPerItem;
    double quantity;
    String unitSize;

    public Product() {
    }

    public Product(String productID, String name, double costPerItem, double quantity, String unitSize) {
        this.productID = productID;
        this.name = name;
        this.costPerItem = costPerItem;
        this.quantity = quantity;
        this.unitSize = unitSize;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productID='" + productID + '\'' +
                ", name='" + name + '\'' +
                ", costPerItem=" + costPerItem +
                ", quantity=" + quantity +
                ", unitSize='" + unitSize + '\'' +
                '}';
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCostPerItem(double costPerItem) {
        this.costPerItem = costPerItem;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnitSize(String unitSize) {
        this.unitSize = unitSize;
    }

    public String getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public double getCostPerItem() {
        return costPerItem;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnitSize() {
        return unitSize;
    }
}
