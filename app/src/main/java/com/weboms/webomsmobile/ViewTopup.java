package com.weboms.webomsmobile;

import android.content.Intent;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup);
        buttonBack = findViewById(R.id.buttonBack);
        postDataUsingVolley();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent Callthis = new Intent(".Dashboard");
                startActivity(Callthis);
            }
        });
    }

    private void postDataUsingVolley() {
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
                    ArrayList<String> amountList = jsonArrayToList(amountArray);
                    ArrayList<String> statusList = jsonArrayToList(statusArray);
                    ArrayList<String> dateList = jsonArrayToList(dateArray);
                    ArrayList<String> proofOfPaymentList = jsonArrayToList(proofOfPaymentArray);

                    GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(), amountList, statusList, dateList, proofOfPaymentList);
                    GridView gridView = findViewById(R.id.gridView);
                    // Set the adapter to the GridView
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

    private ArrayList<String> jsonArrayToList(JSONArray jsonArray) throws JSONException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String value = jsonArray.getString(i);
            list.add(value);
        }
        return list;
    }

}

