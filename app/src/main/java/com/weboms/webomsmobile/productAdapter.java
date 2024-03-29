package com.weboms.webomsmobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class productAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Product> productList;



    public productAdapter(Context context, int layout, ArrayList<Product> productList) {
        super();
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView nameText,priceText,stockText;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        View row = view;
        ViewHolder holder = new ViewHolder();
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.nameText = (TextView) row.findViewById(R.id.nameText);
            holder.priceText = (TextView) row.findViewById(R.id.priceText);
            holder.stockText = (TextView) row.findViewById(R.id.stockText);
            holder.imageView = (ImageView) row.findViewById(R.id.imgProduct);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }
        Product product = productList.get(position);
        holder.nameText.setText(product.getName());
        holder.priceText.setText("Price: ₱"+product.getPrice());
        if (product.getStock() <= 0){
            holder.stockText.setText("Out of Stock");
        }else {
            holder.stockText.setText("Stock: " + product.getStock());
        }
        Glide.with(context.getApplicationContext()).load(product.getImage()).into(holder.imageView);
        return row;
    }
}
