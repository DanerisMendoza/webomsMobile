package com.weboms.webomsmobile;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ProgressBar loadingPB;
    private Button btnLogin;
    private EditText etUname, etPass;

    Intent Callthis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingPB = findViewById(R.id.idLoadingPB);

        etUname = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                postDataUsingVolley(etUname.getText().toString(), etPass.getText().toString());
            }
        });
    }

    private void postDataUsingVolley(final String username, final String password) {

        // url to post our data
//        String url = "http://192.168.1.4/php/Web-based-ordering-management-system/mobile/login.php";
        String url = "http://ucc-csd-bscs.com/WEBOMS/mobile/login.php";
        // loading pogress bar this is optional
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingPB.setVisibility(View.GONE);
                try {
                    JSONObject respObj = new JSONObject(response);
                    String result = respObj.getString("result");
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                    if (result.equals("valid")){
                        Callthis = new Intent(".Dashboard");
                        startActivity(Callthis);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                //this are what being post in php
                params.put("username", username);
                params.put("password", password);
                // put some code for verfication that the post came from your mobile app
                params.put("login", "webomsMobile");

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}