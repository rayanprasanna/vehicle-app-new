package com.universl.hp.vehicle_sale_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.response.SparePartServiceResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<SparePartServiceResponse> sparePartServiceResponses;
    private ArrayList<SparePartServiceResponse> sparePartServiceResponseArrayList ;
    public ServiceAdapter(Context context, List<SparePartServiceResponse> sparePartServiceResponses) {
        this.context = context;
        this.sparePartServiceResponses = sparePartServiceResponses;
        layoutInflater = LayoutInflater.from(context);
        this.sparePartServiceResponseArrayList = new ArrayList<>();
        this.sparePartServiceResponseArrayList.addAll(sparePartServiceResponses);
    }
    public class ViewHolder{
        TextView name,address,contact;
        LinearLayout linearLayout;
    }
    @Override
    public int getCount() {
        return sparePartServiceResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return sparePartServiceResponses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.service_list, null);
            // Locate the TextViews in listView_item.xml
            viewHolder.name = convertView.findViewById(R.id.service_station_name);
            viewHolder.address = convertView.findViewById(R.id.service_station_address);
            viewHolder.contact = convertView.findViewById(R.id.service_station_contact);
            viewHolder.linearLayout = convertView.findViewById(R.id.call_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        // Set the results into TextViews
        viewHolder.name.setText(sparePartServiceResponses.get(position).name);
        viewHolder.address.setText(sparePartServiceResponses.get(position).address);
        viewHolder.contact.setText(sparePartServiceResponses.get(position).contact);

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+sparePartServiceResponses.get(position).contact));
                ((Activity)context).startActivity(intent);
            }
        });
        return convertView;
    }
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        sparePartServiceResponses.clear();
        if (charText.length() == 0) {
            sparePartServiceResponses.addAll(sparePartServiceResponseArrayList);
        } else {
            for (SparePartServiceResponse response : sparePartServiceResponseArrayList) {
                if (response.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    sparePartServiceResponses.add(response);
                }
            }
        }
        notifyDataSetChanged();
    }
}
