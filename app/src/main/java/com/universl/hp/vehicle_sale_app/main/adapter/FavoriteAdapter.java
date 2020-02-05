package com.universl.hp.vehicle_sale_app.main.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.universl.hp.vehicle_sale_app.EnglishDisplayDetailsActivity;
import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.items.GlideApp;
import com.universl.hp.vehicle_sale_app.main.response.FavoriteResponse;

import java.util.List;

public class FavoriteAdapter  extends BaseAdapter {
    private Boolean isSinhala;
    private Context context;
    private LayoutInflater layoutInflater;
    private List<FavoriteResponse> favoriteResponses;


    @SuppressLint("HardwareIds")
    public FavoriteAdapter(Context context, List<FavoriteResponse> favoriteResponses, Boolean isSinhala) {
        this.context = context;
        this.favoriteResponses = favoriteResponses;
        layoutInflater = LayoutInflater.from(context);
        this.isSinhala = isSinhala;
    }
    public class ViewHolder{
        TextView title, price;
        ImageView image,fav;
        RelativeLayout relativeLayout;
    }
    @Override
    public int getCount() {
        return favoriteResponses.size();
    }
    @Override
    public Object getItem(int position) {
        return favoriteResponses.get(position);
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
            view = layoutInflater.inflate(R.layout.favorite_list, null);
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.relativeLayout = view.findViewById(R.id.image_details);
            viewHolder.image = view.findViewById(R.id.vehicle_image);
            viewHolder.fav = view.findViewById(R.id.imageView_flag);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EnglishDisplayDetailsActivity.class);
                intent.putExtra("activity","MyFavorite");
                intent.putExtra("image_path_1", favoriteResponses.get(position).getImage_path_1());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        viewHolder.fav.setImageResource(R.drawable.dis_fav);
        viewHolder.title.setText(favoriteResponses.get(position).getTitle());
        if (isSinhala){
            viewHolder.price.setText("රු "+ favoriteResponses.get(position).getPrice());
        }else {
            viewHolder.price.setText("Rs "+ favoriteResponses.get(position).getPrice());
        }
        viewHolder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteResponses.remove(position);
            }
        });
        GlideApp.with(context.getApplicationContext())
                .load(favoriteResponses.get(position).getImage_path_1())
                .fitCenter()
                .into(viewHolder.image);
        return view;
    }
}
