package com.weboms.webomsmobile;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ViewTopup extends AppCompatActivity {
    Button buttonBack;
    GridView gridView;
    ArrayList<String> amountList = new ArrayList<>();
    ArrayList<String> statusList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> proofOfPaymentList = new ArrayList<>();
    TopupViewAdapter adapter = null;
    String checksum = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup);
        buttonBack = findViewById(R.id.buttonBack);
        gridView = findViewById(R.id.gridView);

        getTopup();
        firstChecksum();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent Callthis = new Intent(".Dashboard");
                startActivity(Callthis);
            }
        });
    }

    private void getTopup() {
        String url = GlobalVariables.url + "/mobile/getTopup.php";
        RequestQueue queue = Volley.newRequestQueue(ViewTopup.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    JSONArray amountArray = respObj.getJSONArray("amount");
                    JSONArray statusArray = respObj.getJSONArray("status");
                    JSONArray dateArray = respObj.getJSONArray("date");
                    JSONArray proofOfPaymentArray = respObj.getJSONArray("proofOfPayment");

                    // Convert JSON arrays to ArrayLists
                    amountList = jsonArrayToList(amountArray);
                    statusList = jsonArrayToList(statusArray);
                    dateList = jsonArrayToList(dateArray);
                    proofOfPaymentList = jsonArrayToList(proofOfPaymentArray);

                    adapter = new TopupViewAdapter(getApplicationContext(), amountList, statusList, dateList, proofOfPaymentList);
                    gridView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewTopup.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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

    private void checkDb(){
        new Thread(
                () -> {
                    while (true) {
                        String url = GlobalVariables.url + "/mobile/getTopupChecksum.php";
                        RequestQueue queue = Volley.newRequestQueue(ViewTopup.this);
                        @SuppressLint({"ResourceType", "SetTextI18n"}) StringRequest request = new StringRequest(Request.Method.POST, url,
                                response -> {
                                    try {
                                        JSONObject respObj = new JSONObject(response);
                                        String result =  respObj.getString("result");
                                        if(!checksum.equals(result)){
                                            checksum = result;
                                            getTopup();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }, error -> Toast.makeText(ViewTopup.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show()) {
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
        String url = GlobalVariables.url + "/mobile/getTopupChecksum.php";
        RequestQueue queue = Volley.newRequestQueue(ViewTopup.this);
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
                Toast.makeText(ViewTopup.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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

