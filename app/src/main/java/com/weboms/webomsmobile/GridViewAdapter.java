package com.weboms.webomsmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> amountList;
    private ArrayList<String> statusList;
    private ArrayList<String> dateList;
    private ArrayList<String> proofOfPaymentList;

    public GridViewAdapter(Context context, ArrayList<String> amountList, ArrayList<String> statusList,
                           ArrayList<String> dateList, ArrayList<String> proofOfPaymentList) {
        this.context = context;
        this.amountList = amountList;
        this.statusList = statusList;
        this.dateList = dateList;
        this.proofOfPaymentList = proofOfPaymentList;
    }

    @Override
    public int getCount() {
        return amountList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false);
            holder = new ViewHolder();
            holder.amountTextView = convertView.findViewById(R.id.amountTextView);
            holder.statusTextView = convertView.findViewById(R.id.statusTextView);
            holder.dateTextView = convertView.findViewById(R.id.dateTextView);
            holder.proofOfPaymentTextView = convertView.findViewById(R.id.proofOfPaymentTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set the data to the views
        holder.amountTextView.setText(amountList.get(position));
        holder.statusTextView.setText(statusList.get(position));
        holder.dateTextView.setText(dateList.get(position));
        holder.proofOfPaymentTextView.setText(proofOfPaymentList.get(position));

        return convertView;
    }

    static class ViewHolder {
        TextView amountTextView;
        TextView statusTextView;
        TextView dateTextView;
        TextView proofOfPaymentTextView;
    }
}
