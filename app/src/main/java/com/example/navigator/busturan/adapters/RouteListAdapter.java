package com.example.navigator.busturan.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.navigator.busturan.R;

import java.util.ArrayList;
import java.util.Map;

import static com.example.navigator.busturan.MainActivity.ROUTE_NAME;

public class RouteListAdapter extends BaseAdapter {


    ArrayList<Map<String, Object>> data;
    LayoutInflater inflater;

    Context context;

    RouteListAdapter.ViewHolder holder;

    public RouteListAdapter(Context context, ArrayList<Map<String, Object>> data) {
        this.data = data;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.indexOf(getItem(i));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_ma_listview, null);
            holder = new RouteListAdapter.ViewHolder();
            holder.item_btn = convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (RouteListAdapter.ViewHolder) convertView.getTag();
        }
        String route_name = data.get(position).get(ROUTE_NAME).toString();
        holder.item_btn.setText(route_name);
        return convertView;
    }

    class ViewHolder {
        TextView item_btn;
    }

}
