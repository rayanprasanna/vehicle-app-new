package com.universl.hp.vehicle_sale_app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.response.HitAdsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchHitAdsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<HitAdsResponse> hitAdsResponses;
    private ArrayList<HitAdsResponse> hitAdsResponseArrayList;


    public SearchHitAdsAdapter(Context context, List<HitAdsResponse> hitAdsResponses) {
        this.context = context;
        this.hitAdsResponses = hitAdsResponses;
        layoutInflater = LayoutInflater.from(context);
        hitAdsResponseArrayList = new ArrayList<>();
        hitAdsResponseArrayList.addAll(hitAdsResponses);
    }

    public class ViewHolder{
        TextView title,description,contact;
        LinearLayout linearLayout;
    }
    @Override
    public int getCount() {
        return hitAdsResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return hitAdsResponses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final SearchHitAdsAdapter.ViewHolder viewHolder;
        if (view == null){
            viewHolder = new SearchHitAdsAdapter.ViewHolder();
            view = layoutInflater.inflate(R.layout.hit_ads_list, null);
            // Locate the TextViews in listView_item.xml
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.description = view.findViewById(R.id.description);
            viewHolder.contact = view.findViewById(R.id.contact);
            viewHolder.linearLayout = view.findViewById(R.id.call_layout);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(hitAdsResponses.get(position).getTitle());
        viewHolder.description.setText(hitAdsResponses.get(position).getDescription());
        viewHolder.contact.setText(hitAdsResponses.get(position).getContact());
        viewHolder.contact.setPaintFlags(viewHolder.contact.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+hitAdsResponses.get(position).getContact()));
                ((Activity)context).startActivity(intent);
            }
        });
        return view;
    }

    public void filter_title(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        hitAdsResponses.clear();
        if (charText.length() == 0) {
            hitAdsResponses.addAll(hitAdsResponseArrayList);
        } else {
            for (HitAdsResponse response : hitAdsResponseArrayList) {
                if (response.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    hitAdsResponses.add(response);
                }
            }
        }
        notifyDataSetChanged();
    }
}
