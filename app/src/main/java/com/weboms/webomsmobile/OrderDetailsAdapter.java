package com.weboms.webomsmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private List<String> dishesList;
    private List<String> dishesQuantityList;
    private List<String> priceList;

    public OrderDetailsAdapter(List<String> dishesList, List<String> dishesQuantityList, List<String> priceList) {
        this.dishesList = dishesList;
        this.dishesQuantityList = dishesQuantityList;
        this.priceList = priceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String dish = dishesList.get(position);
        String quantity = dishesQuantityList.get(position);
        String price = priceList.get(position);

        holder.dishTextView.setText(dish);
        holder.quantityTextView.setText(quantity);
        holder.priceTextView.setText(price);
    }

    @Override
    public int getItemCount() {
        return dishesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishTextView;
        TextView quantityTextView;
        TextView priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishTextView = itemView.findViewById(R.id.dishTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }
    }
}
