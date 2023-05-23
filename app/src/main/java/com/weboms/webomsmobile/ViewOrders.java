package com.weboms.webomsmobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewOrders extends AppCompatActivity {
    Button buttonBack;
    GridView gridView;
    ArrayList<String> order_idList = new ArrayList<>();
    ArrayList<String> statusList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> totalOrderList = new ArrayList<>();
    OrderAdapter adapter = null;
    String checksum = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);
        buttonBack = findViewById(R.id.buttonBack);
        gridView = findViewById(R.id.gridView);

        getOrders();
        firstChecksum();


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent Callthis = new Intent(".Dashboard");
                startActivity(Callthis);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                getOrderDetails(order_idList.get(position).substring(1),totalOrderList.get(position),order_idList.get(position),dateList.get(position));
            }
        });
    }
    private void getOrders() {
        String url = GlobalVariables.url + "/mobile/getOrder.php";
        RequestQueue queue = Volley.newRequestQueue(ViewOrders.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    JSONArray order_idArray = respObj.getJSONArray("order_id");
                    JSONArray statusArray = respObj.getJSONArray("status");
                    JSONArray dateArray = respObj.getJSONArray("date");
                    JSONArray totalOrderArray = respObj.getJSONArray("totalOrder");

                    // Convert JSON arrays to ArrayLists
                    order_idList = jsonArrayToList(order_idArray);
                    statusList = jsonArrayToList(statusArray);
                    dateList = jsonArrayToList(dateArray);
                    totalOrderList = jsonArrayToList(totalOrderArray);

                    // After retrieving data using Volley and converting to ArrayLists
                    adapter = new OrderAdapter(getApplicationContext(), order_idList, statusList, dateList, totalOrderList);
                    gridView.setAdapter(adapter);

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
                GlobalClass globalClass = (GlobalClass) getApplicationContext();
                params.put("post", "webomsMobile");
                params.put("user_id", globalClass.getUser_id());
                return params;
            }
        };
        queue.add(request);
    }

    private void getOrderDetails(String order_id, String total, String orderId, String date) {
        String url = GlobalVariables.url + "/mobile/getOrderDetails.php";
        RequestQueue queue = Volley.newRequestQueue(ViewOrders.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    JSONArray dishesArr= respObj.getJSONArray("dishesArr");
                    JSONArray priceArr = respObj.getJSONArray("priceArr");
                    JSONArray dishesQuantity = respObj.getJSONArray("dishesQuantity");


                    // Convert JSON arrays to ArrayLists
                    ArrayList<String> dishesList = jsonArrayToList(dishesArr);
                    ArrayList<String> priceList = jsonArrayToList(priceArr);
                    ArrayList<String> dishesQuantityList = jsonArrayToList(dishesQuantity);


                    // Inflate the custom layout for the dialog
                    View dialogView = LayoutInflater.from(ViewOrders.this).inflate(R.layout.dialog_order_details, null);

                    // Find views in the custom layout
                    TextView titleTextView = dialogView.findViewById(R.id.titleTextView);
                    TextView dateTextView = dialogView.findViewById(R.id.dateTextView);
                    TextView totalTextView = dialogView.findViewById(R.id.totalTextView);
                    RecyclerView orderDetailsRecyclerView = dialogView.findViewById(R.id.orderDetailsRecyclerView);

                    // Set the title for the dialog
                    titleTextView.setText("Order Details"+orderId);
                    dateTextView.setText(date);
                    totalTextView.setText(total);


                    // Create a custom adapter for the RecyclerView
                    OrderDetailsAdapter adapter = new OrderDetailsAdapter(dishesList, dishesQuantityList, priceList);
                    orderDetailsRecyclerView.setAdapter(adapter);
                    orderDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(ViewOrders.this));

                    // Build and show the dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewOrders.this);
                    builder.setView(dialogView);
                    builder.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewOrders.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("post", "webomsMobile");
                params.put("order_id", order_id);
                return params;
            }
        };
        queue.add(request);
    }

    private void checkDb(){
        new Thread(
                () -> {
                    while (true) {
                        String url = GlobalVariables.url + "/mobile/getOrderChecksum.php";
                        RequestQueue queue = Volley.newRequestQueue(ViewOrders.this);
                        @SuppressLint({"ResourceType", "SetTextI18n"}) StringRequest request = new StringRequest(Request.Method.POST, url,
                                response -> {
                                    try {
                                        JSONObject respObj = new JSONObject(response);
                                        String result =  respObj.getString("result");
                                        if(!checksum.equals(result)){
                                            checksum = result;
                                            getOrders();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }, error -> Toast.makeText(ViewOrders.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show()) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("post", "webomsMobile");
                                return params;
                            }
                        };
                        queue.add(request);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    private void firstChecksum(){
        String url = GlobalVariables.url + "/mobile/getOrderChecksum.php";
        RequestQueue queue = Volley.newRequestQueue(ViewOrders.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    String result =  respObj.getString("result");
                    checksum = result;
                    checkDb();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewOrders.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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

    private ArrayList<String> jsonArrayToList(JSONArray jsonArray) throws JSONException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String value = jsonArray.getString(i);
            list.add(value);
        }
        return list;
    }
}

