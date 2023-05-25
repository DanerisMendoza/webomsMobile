package com.weboms.webomsmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;


public class Dashboard extends Activity {


    Button buttonlogout,buttonViewMenu,buttonViewTopup,buttonViewOrders;
    Intent Callthis;
    TextView textViewName, textViewBalance, textViewUserName, textViewGender,  textViewAddress, textViewEmail;
    ImageView imgView;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        init();


        buttonlogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                Callthis = new Intent(".Login");
                startActivity(Callthis);
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

        buttonViewTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Callthis = new Intent(".ViewTopup");
                startActivity(Callthis);
            }
        });

        buttonViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Callthis = new Intent(".ViewOrders");
                startActivity(Callthis);
            }
        });

    }

    public void init(){
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        user_id = globalClass.getUser_id();
        postDataUsingVolley(user_id);
        imgView = findViewById(R.id.imgView);
        textViewName = findViewById(R.id.textViewName);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewGender = findViewById(R.id.textViewGender);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewBalance = findViewById(R.id.textViewBalance);
        buttonlogout = findViewById(R.id.buttonlogout);
        buttonViewMenu = findViewById(R.id.buttonViewMenu);
        buttonViewTopup = findViewById(R.id.buttonViewTopup);
        buttonViewOrders = findViewById(R.id.buttonViewOrders);
    }

    private void postDataUsingVolley(final String user_id) {
        String url = GlobalVariables.url+"/mobile/getUserInfo.php";
        RequestQueue queue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    textViewName.setText( respObj.getString("name"));
                    textViewUserName.setText( respObj.getString("username"));
                    String gender = respObj.getString("gender").equals("m") ? "male":"female";
                    textViewGender.setText( gender);
                    textViewAddress.setText( respObj.getString("address"));
                    textViewEmail.setText( respObj.getString("email"));
                    textViewBalance.setText("₱"+ respObj.getString("balance"));
                    String picName = respObj.getString("picName");
                    if ((picName != "null")) {
                        String picUrl = GlobalVariables.url+"/profilePic/"+picName;
                        Glide.with(getApplicationContext()).load(picUrl).into(imgView);
                    }

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
}
