package com.weboms.webomsmobile;

public class Product {
    private int id;
    private String name;
    private String price;
    private String image;
    private  String stock;

    public Product(int id, String name, String price,String stock, String image) {
        super();
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getStock() {
        return stock;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
