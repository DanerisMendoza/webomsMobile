package com.weboms.webomsmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;


public class Dashboard extends Activity {

    Button buttonlogout;
    Intent Callthis;
    TextView textViewName, textViewBalance, textViewUserName, textViewGender,  textViewAddress, textViewEmail;

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
    }

    public void init(){
        String user_id = getIntent().getStringExtra("user_id");
        postDataUsingVolley(user_id);
        textViewName = findViewById(R.id.textViewName);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewGender = findViewById(R.id.textViewGender);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewBalance = findViewById(R.id.textViewBalance);
        buttonlogout = findViewById(R.id.buttonlogout);
    }

    private void postDataUsingVolley(final String user_id) {
//        String url = "http://192.168.1.5/php/Web-based-ordering-management-system/mobile/getUserInfo.php";
        String url = "http://ucc-csd-bscs.com/WEBOMS/mobile/getUserInfo.php";
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
