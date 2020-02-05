package com.universl.hp.vehicle_sale_app.main.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.items.GlideApp;
import com.universl.hp.vehicle_sale_app.main.DisplayAdvertisementActivity;
import com.universl.hp.vehicle_sale_app.main.MyAdsActivity;
import com.universl.hp.vehicle_sale_app.main.MyFavoriteActivity;
import com.universl.hp.vehicle_sale_app.main.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.main.response.VehicleResponse;
import com.universl.hp.vehicle_sale_app.main.service.FavoriteAPIService;
import com.universl.hp.vehicle_sale_app.main.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UrAdsAdapter extends BaseAdapter {

    private Boolean isSinhala;
    private Context context;
    private LayoutInflater layoutInflater;
    private List<VehicleResponse> vehicleResponses;


    @SuppressLint("HardwareIds")
    UrAdsAdapter(Context context, List<VehicleResponse> vehicleResponses, Boolean isSinhala) {
        this.context = context;
        this.vehicleResponses = vehicleResponses;
        layoutInflater = LayoutInflater.from(context);
        this.isSinhala = isSinhala;
    }
    public class ViewHolder{
        TextView message, price, title;
        ImageView image, del;
        RelativeLayout relativeLayout;
    }
    @Override
    public int getCount() {
        return vehicleResponses.size();
    }
    @Override
    public Object getItem(int position) {
        return vehicleResponses.get(position);
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
            view = layoutInflater.inflate(R.layout.my_list, null);
            viewHolder.message = view.findViewById(R.id.message);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.relativeLayout = view.findViewById(R.id.layout);
            viewHolder.image = view.findViewById(R.id.vehicle_image);
            viewHolder.del = view.findViewById(R.id.imageView_flag);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayAdvertisementActivity.class);
                intent.putExtra("id", vehicleResponses.get(position).getId());
                intent.putExtra("isSinhala",isSinhala);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        viewHolder.del.setImageResource(R.drawable.delete);
        viewHolder.title.setText(vehicleResponses.get(position).getTitle());
        viewHolder.message.setText("Disapproved");
        if (isSinhala){
            viewHolder.price.setText("රු "+ vehicleResponses.get(position).getPrice());
        }else {
            viewHolder.price.setText("Rs "+ vehicleResponses.get(position).getPrice());
        }
        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*fetchUrAdsData(getIntent().getStringExtra("android_id"));
                viewHolder.price.setVisibility(View.GONE);
                viewHolder.image.setVisibility(View.GONE);
                viewHolder.title.setVisibility(View.GONE);
                viewHolder.del.setVisibility(View.GONE);
                viewHolder.relativeLayout.setVisibility(View.GONE);*/
                //notify();
            }
        });
        GlideApp.with(context.getApplicationContext())
                .load(vehicleResponses.get(position).getImage_path_1())
                .fitCenter()
                .into(viewHolder.image);
        return view;
    }
}
