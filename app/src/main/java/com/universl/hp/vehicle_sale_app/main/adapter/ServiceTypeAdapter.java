package com.universl.hp.vehicle_sale_app.main.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.main.VehicleDataActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceTypeAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> service;
    private ArrayList<String> arrayList;


    public ServiceTypeAdapter(Context context, List<String> service) {
        this.context = context;
        this.service = service;
        layoutInflater = LayoutInflater.from(context);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(service);
    }
    public class ViewHolder{
        Button button;
    }
    @Override
    public int getCount() {
        return service.size();
    }

    @Override
    public Object getItem(int position) {
        return service.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.service_button_new_ui_list, null);
            viewHolder.button = view.findViewById(R.id.button_list_id);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VehicleDataActivity.class);
                intent.putExtra("type","Category");
                intent.putExtra("category",service.get(position));
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        viewHolder.button.setText(service.get(position));
        return view;
    }
    public void filter_service(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        service.clear();
        if (charText.length() == 0) {
            service.addAll(arrayList);
        } else {
            for (String response : arrayList) {
                if (response.toLowerCase(Locale.getDefault()).contains(charText)) {
                    service.add(response);
                }
            }
        }
        notifyDataSetChanged();
    }
}
