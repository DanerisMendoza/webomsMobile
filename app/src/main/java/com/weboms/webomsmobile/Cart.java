package com.weboms.webomsmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

public class Cart extends Activity {

    String user_id;
    Button buttonViewMenu;
    Intent Callthis;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        init();
        buttonViewMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                Callthis = new Intent(".ProductList");
                Callthis.putExtra("user_id", user_id);
                startActivity(Callthis);
            }
        });

    }

    public void init(){
        user_id = getIntent().getStringExtra("user_id");
        buttonViewMenu = (Button) findViewById(R.id.buttonViewMenu);
    }
}
