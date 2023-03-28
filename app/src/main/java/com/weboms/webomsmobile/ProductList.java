package com.weboms.webomsmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class ProductList extends Activity {
    productAdapter adapter = null;

    GridView gridView;
    ArrayList<Product> menuList = new ArrayList<Product>();

    String cursorGlobal = "";
    Button btnHome, btnViewCart;
    Intent Callthis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        init();
        postDataUsingVolley();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                String order = menuList.get(position).getName();
                Float price = menuList.get(position).getPrice();
                int orderType = menuList.get(position).getOrderType();
                int stock = menuList.get(position).getStock()-1;
                //check cart is empty
                if (!GlobalVariables.cartList.isEmpty()){
                    //for each order
                    boolean containOrder = false;
                    for (int i = 0; i < GlobalVariables.cartList.size(); i++) {
                        if (GlobalVariables.cartList.get(i).getOrder().equals(order)) {
                            int quantity = GlobalVariables.cartList.get(i).getQuantity() + 1;
                            GlobalVariables.cartList.get(i).setQuantity(quantity);
                            GlobalVariables.cartList.get(i).setPrice(price*quantity);
                            containOrder = true;
                        }
                    }
                    if (containOrder == false){
                        GlobalVariables.cartList.add(new Cart(order, 1, price, orderType));
                    }
                }
                //if empty then add order directly in the list
                else {
                    GlobalVariables.cartList.add(new Cart(order, 1, price, orderType));
                }
                Toast.makeText(ProductList.this, "Add to cart Success!", Toast.LENGTH_SHORT).show();
                //decrease stock
                menuList.get(position).setStock(stock);
                adapter.notifyDataSetChanged();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
                Callthis = new Intent(".Dashboard");
                startActivity(Callthis);
            }
        });

        btnViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
                Callthis = new Intent(".CartPage");
                startActivity(Callthis);
            }
        });

    }


    private void postDataUsingVolley() {
//        String url = "http://192.168.1.3/php/Web-based-ordering-management-system/mobile/getMenu.php";
//        String url = "http://ucc-csd-bscs.com/WEBOMS/mobile/getMenu.php";
        String url = GlobalVariables.url+"/mobile/getMenu.php";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    //init
                    String dishesArr = respObj.getString("dishesArr");
                    String priceArr = respObj.getString("priceArr");
                    String picNameArr = respObj.getString("picNameArr");
                    String stockArr = respObj.getString("stockArr");
                    String orderTypeArr = respObj.getString("orderTypeArr");
                    //converting to array
                    String[] dishesArr2 = dishesArr.split(",");
                    String[] priceArr2 = priceArr.split(",");
                    String[] picNameArr2 = picNameArr.split(",");
                    String[] stockArr2 = stockArr.split(",");
                    String[] orderTypeArr2 = orderTypeArr.split(",");
                    for(int i=0; i<dishesArr2.length; i++){
                        String picName = picNameArr2[i];
                        String picUrl = GlobalVariables.url+"/dishesPic/"+picName;
                        menuList.add(new Product(i , dishesArr2[i], Float.parseFloat(priceArr2[i]), Integer.parseInt(stockArr2[i]), Integer.parseInt(orderTypeArr2[i]), picUrl));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("post", "webomsMobile");
                return params;
            }
        };
        queue.add(request);
    }

    public void init() {
        btnHome = (Button) findViewById(R.id.btnHome);
        btnViewCart = (Button) findViewById(R.id.btnViewCart);
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new productAdapter(this, R.layout.product_items, menuList);
        gridView.setAdapter(adapter);
    }
}
