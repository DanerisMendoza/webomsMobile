package com.weboms.webomsmobile;

import java.util.ArrayList;

public interface GlobalVariables {
    //hosting
    public static final String url = "http://ucc-csd-bscs.com/WEBOMS";
    //localHost
//    public static final  String url = "http://192.168.1.6/Web-based-ordering-management-system";

    public ArrayList<Cart> cartList = new ArrayList<Cart>();
    public ArrayList<Product> menuList = new ArrayList<Product>();

}
