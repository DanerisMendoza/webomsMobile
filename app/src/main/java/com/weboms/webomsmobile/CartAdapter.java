package com.weboms.webomsmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Cart> cartList;

    public CartAdapter(Context context, int layout, ArrayList<Cart> cartList) {
        super();
        this.context = context;
        this.layout = layout;
        this.cartList = cartList;
    }


    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView textName,textQuantity,textPrice;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        View row = view;
        ViewHolder holder = new ViewHolder();
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.textName = (TextView) row.findViewById(R.id.textName);
            holder.textQuantity = (TextView) row.findViewById(R.id.textQuantity);
            holder.textPrice = (TextView) row.findViewById(R.id.textPrice);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }
        Cart cart = cartList.get(position);
        holder.textName.setText(cart.getOrder());
        holder.textQuantity.setText(String.valueOf(cart.getQuantity()));
        holder.textPrice.setText("₱"+String.valueOf(cart.getPrice()));
        return row;
    }

}
