package com.weboms.webomsmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class CartPage extends Activity {

    String user_id;
    Button buttonViewMenu;
    Intent Callthis;
    GridView gridView2;
    CartAdapter adapter = null;

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
        gridView2 = (GridView) findViewById(R.id.gridView2);
        adapter = new CartAdapter(this, R.layout.cart_items, GlobalVariables.cartList);
        gridView2.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,GlobalVariables.cartList);
//        Toast.makeText(this, GlobalVariables.cartList.get(0).getOrder()+"\n"
//                +GlobalVariables.cartList.get(0).getPrice()+"\n"
//                , Toast.LENGTH_SHORT).show();
        Toast.makeText(this, user_id, Toast.LENGTH_SHORT).show();

    }
}
