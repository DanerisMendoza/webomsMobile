package com.weboms.webomsmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> orderIds;
    private ArrayList<String> statuses;
    private ArrayList<String> dates;
    private ArrayList<String> totalOrders;

    public OrderAdapter(Context context, ArrayList<String> orderIds, ArrayList<String> statuses,
                        ArrayList<String> dates, ArrayList<String> totalOrders) {
        this.context = context;
        this.orderIds = orderIds;
        this.statuses = statuses;
        this.dates = dates;
        this.totalOrders = totalOrders;
    }

    @Override
    public int getCount() {
        return orderIds.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);
        }

        TextView orderIdTextView = convertView.findViewById(R.id.orderIdTextView);
        TextView statusTextView = convertView.findViewById(R.id.statusTextView);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView totalOrderTextView = convertView.findViewById(R.id.totalOrderTextView);

        orderIdTextView.setText(orderIds.get(position));
        statusTextView.setText(statuses.get(position));
        dateTextView.setText(dates.get(position));
        totalOrderTextView.setText(totalOrders.get(position));

        return convertView;
    }
}
