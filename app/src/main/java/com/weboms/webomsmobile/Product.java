package com.weboms.webomsmobile;

public class Product {
    private int id;
    private String name;
    private Float price;
    private String image;
    private  int stock;
    private  int orderType;

    public Product(int id, String name, Float price,Integer stock, Integer orderType, String image) {
        super();
        this.id = id;
        this.name = name;
        this.price = price;
        this.orderType = orderType;
        this.stock = stock;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getStock() {
        return stock;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
