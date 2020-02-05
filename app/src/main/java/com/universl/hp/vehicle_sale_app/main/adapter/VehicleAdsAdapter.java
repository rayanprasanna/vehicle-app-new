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

import com.universl.hp.vehicle_sale_app.EnglishDisplayDetailsActivity;
import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.items.GlideApp;
import com.universl.hp.vehicle_sale_app.main.DisplayAdvertisementActivity;
import com.universl.hp.vehicle_sale_app.main.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.main.response.VehicleResponse;
import com.universl.hp.vehicle_sale_app.main.service.FavoriteAPIService;
import com.universl.hp.vehicle_sale_app.main.utils.Constants;

import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VehicleAdsAdapter extends BaseAdapter {
    private String type, brand, model, year, district;
    private Boolean isSinhala;
    private Context context;
    private LayoutInflater layoutInflater;
    private List<VehicleResponse> vehicleResponses;
    private List<FavoriteResponse> favoriteResponses;
    private List<String> ads_id;
    private String user_id;


    @SuppressLint("HardwareIds")
    public VehicleAdsAdapter(Context context, List<VehicleResponse> vehicleResponses, List<FavoriteResponse> favoriteResponses, List<String> ads_id, String user_id, Boolean isSinhala, String type, String brand, String model, String year, String district) {
        this.context = context;
        this.vehicleResponses = vehicleResponses;
        this.favoriteResponses = favoriteResponses;
        this.ads_id = ads_id;
        layoutInflater = LayoutInflater.from(context);
        this.user_id = user_id;
        this.isSinhala = isSinhala;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.district = district;
        this.year = year;
    }
    public class ViewHolder{
        TextView title, price;
        ImageView image,fav;
        RelativeLayout relativeLayout;
        Boolean isClickFavoriteButton;
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
            view = layoutInflater.inflate(R.layout.vehicle_data_list, null);
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
                Intent intent = new Intent(context, DisplayAdvertisementActivity.class);
                intent.putExtra("id", vehicleResponses.get(position).getId());
                intent.putExtra("isSinhala",isSinhala);
                intent.putExtra("type", type);
                intent.putExtra("search_brand",brand);
                intent.putExtra("search_model",model);
                intent.putExtra("search_model_year",year);
                intent.putExtra("search_district",district);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        if (favoriteResponses.size() > 0){
            for (int j = 0; j < vehicleResponses.size(); j++){
                for (int i = 0; i < favoriteResponses.size(); i++){
                    if (vehicleResponses.get(j).getId().equals(favoriteResponses.get(i).getId())){
                        viewHolder.fav.setImageResource(R.drawable.ic_favorite_star_2);
                        System.out.println(favoriteResponses.get(i).getId() + " <---> "+ vehicleResponses.get(j).getId() + " <---> "+ ads_id.size());
                    }
                }
            }
        }
        viewHolder.title.setText(vehicleResponses.get(position).getTitle());
        if (isSinhala){
            viewHolder.price.setText("රු "+ vehicleResponses.get(position).getPrice());
        }else {
            viewHolder.price.setText("Rs "+ vehicleResponses.get(position).getPrice());
        }
        if (Collections.frequency(ads_id,vehicleResponses.get(position).getImage_path_1()) == 0){
            viewHolder.isClickFavoriteButton = false;
            viewHolder.fav.setImageResource(R.drawable.ic_favorite_star_1);
        }if (Collections.frequency(ads_id,vehicleResponses.get(position).getImage_path_1()) > 1){
            viewHolder.isClickFavoriteButton = true;
            viewHolder.fav.setImageResource(R.drawable.ic_favorite_star_2);
        }
        viewHolder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Collections.frequency(ads_id,vehicleResponses.get(position).getImage_path_1()) == 0){
                    insertData(vehicleResponses.get(position).getId(), user_id);
                    viewHolder.fav.setImageResource(R.drawable.ic_favorite_star_2);
                }if (Collections.frequency(ads_id,vehicleResponses.get(position).getImage_path_1()) > 1){
                    viewHolder.fav.setImageResource(R.drawable.ic_favorite_star_2);
                }
            }
        });
        GlideApp.with(context.getApplicationContext())
                .load(vehicleResponses.get(position).getImage_path_1())
                .fitCenter()
                .into(viewHolder.image);
        return view;
    }
    private void insertData(final Integer id, final String user_id){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //The logging interceptor will be added to the http client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        //The Retrofit builder will have the client attached, in order to get connection logs
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();
        //Creating object for our interface
        FavoriteAPIService api = retrofit.create(FavoriteAPIService.class);
        //Defining the method insertFavorite of our interface
        //Here a logging interceptor is created
        Call<FavoriteResponse> call = api.insertFavorite(id, user_id);
        final ViewHolder viewHolder = new ViewHolder();
        @SuppressLint("InflateParams") final View view = layoutInflater.inflate(R.layout.vehicle_data_list, null);
        call.enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(@NonNull Call<FavoriteResponse> call, @NonNull Response<FavoriteResponse> response) {
                viewHolder.fav = view.findViewById(R.id.imageView_flag);
                viewHolder.fav.setImageResource(R.drawable.ic_favorite_star_2);
                viewHolder.isClickFavoriteButton = true;
            }

            @Override
            public void onFailure(@NonNull Call<FavoriteResponse> call, @NonNull Throwable t) {
                viewHolder.fav = view.findViewById(R.id.imageView_flag);
                viewHolder.fav.setImageResource(R.drawable.ic_favorite_star_1);
                viewHolder.isClickFavoriteButton = false;
            }
        });
    }
}
