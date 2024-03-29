package com.weboms.webomsmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CartPage extends Activity {

    Button buttonViewMenu, btnClear, btnCheckout, ButtonDecrease, ButtonIncrease;
    Intent Callthis;
    GridView gridView;
    CartAdapter adapter = null;
    TextView textViewTotal,textViewBalance;
    float total = 0, balance = 0;
    String user_id;
    int currentlyClickedPosition = -1;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        init();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                String order = GlobalVariables.cartList.get(position).getOrder();
                int quantity = GlobalVariables.cartList.get(position).getQuantity();
                currentlyClickedPosition = position;

                // Iterate through all child views in the GridView
                for (int i = 0; i < arg0.getChildCount(); i++) {
                    View child = arg0.getChildAt(i);
                    if (i == position) {
                        // Set the background color of the clicked view to purple
                        child.setBackgroundColor(getResources().getColor(R.color.green));
                    } else {
                        // Set the background color of all other views to white
                        child.setBackgroundColor(getResources().getColor(R.color.beige));
                    }
                }
            }
        });

        ButtonDecrease.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(currentlyClickedPosition == -1){
                    Toast.makeText(CartPage.this, "Please Choose Order to Decrease", Toast.LENGTH_SHORT).show();
                    return;
                }

                int currentQty = GlobalVariables.cartList.get(currentlyClickedPosition).getQuantity();
                int result = currentQty - 1;
                GlobalVariables.cartList.get(currentlyClickedPosition).setQuantity(result);
                if (result <= 0){
                    GlobalVariables.cartList.remove(currentlyClickedPosition);
                }
                adapter.notifyDataSetChanged();
                computeTotal();
            }
        });

        ButtonIncrease.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(currentlyClickedPosition == -1){
                    Toast.makeText(CartPage.this, "Please Choose Order to Increase", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    int currentQty = GlobalVariables.cartList.get(currentlyClickedPosition).getQuantity();
                    int currentStock = GlobalVariables.menuList.get(currentlyClickedPosition).getStock();
                    if (currentStock - currentQty >= 1){
                        int result = currentQty + 1;
                        GlobalVariables.cartList.get(currentlyClickedPosition).setQuantity(result);
                        adapter.notifyDataSetChanged();
                        computeTotal();
                    }
                    else{
                        Toast.makeText(CartPage.this, "Not Enough Stock!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonViewMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                Callthis = new Intent(".ProductList");
                startActivity(Callthis);
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariables.cartList.clear();
                adapter.notifyDataSetChanged();
                total = 0;
                textViewTotal.setText("Total: ₱"+total);;
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( GlobalVariables.cartList.isEmpty()){
                    Toast.makeText(CartPage.this, "There is nothing in your Cart!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (balance < total){
                    Toast.makeText(CartPage.this, "Your Balance is insufficient", Toast.LENGTH_SHORT).show();
                }
                else{
                    String orderType = "",dishesQuantity = "", dishesArr = "", priceArr = "",totalString="";
                    for (int i=0; i<GlobalVariables.cartList.size(); i++){
                        if (i == GlobalVariables.cartList.size()-1){
                            orderType += GlobalVariables.cartList.get(i).getOrderType();
                            dishesQuantity += GlobalVariables.cartList.get(i).getQuantity();
                            dishesArr += GlobalVariables.cartList.get(i).getOrder();
                            priceArr += GlobalVariables.cartList.get(i).getPrice();
                        }
                        else {
                            orderType += GlobalVariables.cartList.get(i).getOrderType() + ",";
                            dishesQuantity += GlobalVariables.cartList.get(i).getQuantity() + ",";
                            dishesArr += GlobalVariables.cartList.get(i).getOrder() + ",";
                            priceArr += GlobalVariables.cartList.get(i).getPrice() + ",";
                        }
                    }
                    totalString = String.valueOf(total);

                    insertOrder(user_id,orderType,dishesQuantity,dishesArr,priceArr,totalString);
                    GlobalVariables.cartList.clear();
                    adapter.notifyDataSetChanged();
                    balance = balance-total;
                    textViewBalance.setText("Balance: ₱"+balance);
                    total = 0;
                    textViewTotal.setText("Total: ₱"+total);;
                    Toast.makeText(CartPage.this, "Order Success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void init(){
        ButtonDecrease = (Button) findViewById(R.id.ButtonDecrease);
        ButtonIncrease = (Button) findViewById(R.id.ButtonIncrease);
        textViewBalance = (TextView) findViewById(R.id.textViewBalance);
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        user_id = globalClass.getUser_id();
        getBalance(user_id);
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);
        buttonViewMenu = (Button) findViewById(R.id.buttonViewMenu);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnCheckout = (Button) findViewById(R.id.btnCheckout);
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new CartAdapter(this, R.layout.cart_items, GlobalVariables.cartList);
        gridView.setAdapter(adapter);
        computeTotal();
    }

    void computeTotal(){
        total = 0;
        for (int i = 0; i<GlobalVariables.cartList.size(); i++){
            int quantity = GlobalVariables.cartList.get(i).getQuantity();
            Float price = GlobalVariables.cartList.get(i).getPrice()*quantity;
            total += price;
        }
        textViewTotal.setText("Total: ₱"+total);
    }



    private void getBalance(final String user_id) {
        String url = GlobalVariables.url+"/mobile/getUserInfo.php";
        RequestQueue queue = Volley.newRequestQueue(CartPage.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    balance = Float.parseFloat(respObj.getString("balance"));
                    textViewBalance.setText( "Balance: ₱"+respObj.getString("balance"));
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
                params.put("user_id", user_id);
                return params;
            }
        };
        queue.add(request);
    }

    private void insertOrder(final String user_id, final String orderType, final String dishesQuantity, final String dishesArr, final String priceArr, final String total) {
        String url = GlobalVariables.url+"/mobile/insertOrder.php";
        RequestQueue queue = Volley.newRequestQueue(CartPage.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
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
                params.put("user_id", user_id);
                params.put("orderType", orderType);
                params.put("dishesQuantity", dishesQuantity);
                params.put("dishesArr", dishesArr);
                params.put("priceArr", priceArr);
                params.put("total", total);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

}
