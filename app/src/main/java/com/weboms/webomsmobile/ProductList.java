package com.weboms.webomsmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class ProductList extends Activity {
    productAdapter adapter1 = null,normalAdapter1 = null;
    SimpleAdapter adapter2;
    GridView gridView,gridViewOrderList;
    ArrayList<Product> list = new ArrayList<Product>();
    ArrayList<Product> normalList = new ArrayList<Product>();
    List<Map<String,String>> orderList = new ArrayList<Map<String,String>>();
    String cursorGlobal = "";
    Button btnAddToCart;
    TextView orderTotalText;
    int totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        init();
        postDataUsingVolley();
        normalAdapter1();
        adapter1();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                cursorGlobal = String.valueOf(position);
                Toast.makeText(getApplicationContext(), list.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(cursorGlobal.contentEquals("")) {
                    Toast.makeText(getApplicationContext(), "Select a product!", Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter2(Integer.parseInt(cursorGlobal));
            }
        });
    }

    public void adapter2(int position) {
        Map<String, String> tab = new HashMap<String, String>();
        tab.put("name", list.get(position).getName());
        tab.put("price", list.get(position).getPrice());
        orderList.add(tab);
        String[] from = {"name","price"};
        int[] to = {R.id.nameText,R.id.priceText};
        adapter2 = new SimpleAdapter(this,orderList,R.layout.orderlist, from, to);
        gridViewOrderList.setAdapter(adapter2);
        totalPrice += Integer.parseInt(normalList.get(position).getPrice());
        orderTotalText.setText("Total Price: "+"₱"+String.valueOf(totalPrice));
    }

    public void normalAdapter1() {
//        Cursor cursor = MainActivity.DB.getData("SELECT * FROM image");
//        normalList.clear();
//        while(cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            String name = cursor.getString(1);
//            String price = cursor.getString(2);
//            byte[] image = cursor.getBlob(3);
//            normalList.add(new Product(id, name, price, image));
//        }
    }

    public void adapter1() {
//        Cursor cursor = MainActivity.DB.getData("SELECT * FROM image");
//        list.clear();
//        while(cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            String name = cursor.getString(1);
//            String price = "price: ₱"+cursor.getString(2);
//            byte[] image = cursor.getBlob(3);
//            list.add(new Product(id, name, price, image));
//        }
//        list.add(new Product(1  , "name", "price", null));
    }

    private void postDataUsingVolley() {
        String url = "http://192.168.1.3/php/Web-based-ordering-management-system/mobile/getMenu.php";
//        String url = "http://ucc-csd-bscs.com/WEBOMS/mobile/login.php";


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    String dishesArr = respObj.getString("dishesArr");
                    String priceArr = respObj.getString("priceArr");
                    String picNameArr = respObj.getString("picNameArr");
                    String[] dishesArr2 = dishesArr.split(",");
                    String[] priceArr2 = priceArr.split(",");
                    String[] picNameArr2 = picNameArr.split(",");


                    Toast.makeText(ProductList.this, picNameArr2[0], Toast.LENGTH_SHORT).show();

                    for(int i=0; i<dishesArr2.length; i++){
                        String picName = picNameArr2[i];
                        String picUrl = "http://192.168.1.3/php/Web-based-ordering-management-system/dishesPic/"+picName;

                        list.add(new Product(i , dishesArr2[i], "₱"+priceArr2[i], null));
                    }
                    adapter1.notifyDataSetChanged();
                    Toast.makeText(ProductList.this, dishesArr2[0], Toast.LENGTH_SHORT).show();
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

    private byte[] downloadUrl(URL toDownload) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = toDownload.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputStream.toByteArray();
    }


    public void init() {
        orderTotalText = (TextView) findViewById(R.id.orderTotalText);
        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);
        gridViewOrderList = (GridView) findViewById(R.id.gridViewOrderList);
        gridView = (GridView) findViewById(R.id.gridView);
        adapter1 = new productAdapter(this, R.layout.product_items, list);
        gridView.setAdapter(adapter1);

    }
}
