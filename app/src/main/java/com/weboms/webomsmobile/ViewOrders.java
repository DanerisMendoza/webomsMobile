package com.weboms.webomsmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    ArrayList<String> order_idList;
    ArrayList<String> statusList;
    ArrayList<String> dateList;
    ArrayList<String> totalOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup);
        buttonBack = findViewById(R.id.buttonBack);
        gridView = findViewById(R.id.gridView);
        postDataUsingVolley();

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
                getOrderDetails(order_idList.get(position).substring(1));
            }
        });
    }
    private void postDataUsingVolley() {
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
                    OrderAdapter adapter = new OrderAdapter(getApplicationContext(), order_idList, statusList, dateList, totalOrderList);
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

    private void getOrderDetails(String order_id) {
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


//                    StringBuffer buffer = new StringBuffer();
//                    for (int i = 0; i < dishesList.size(); i++) {
//                        String dish = dishesList.get(i);
//                        String quantity = dishesQuantityList.get(i);
//                        String price = priceList.get(i);
//
//                        String row = String.format("%-10s %-10s %-10s%n", dish, quantity, price);
//                        buffer.append(row);
//                    }
//
//                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(ViewOrders.this, R.style.AlertDialogCustom));
//                    builder.setCancelable(true);
//                    builder.setTitle("Order Details");
//                    builder.setMessage(buffer.toString());
//                    builder.show();



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
                params.put("order_id", order_id);
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

