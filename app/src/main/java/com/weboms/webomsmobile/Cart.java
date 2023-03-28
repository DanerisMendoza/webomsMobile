package com.weboms.webomsmobile;

public class Cart {

    private String order;
    private int quantity;
    private float price;
    private int orderType;

    public Cart(String order, int quantity, float price, int orderType){
        super();
        this.order = order;
        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;
    }

    public String getOrder() {
        return order;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
