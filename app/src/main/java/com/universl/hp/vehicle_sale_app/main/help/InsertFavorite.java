package com.universl.hp.vehicle_sale_app.main.help;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.main.adapter.VehicleAdsAdapter;
import com.universl.hp.vehicle_sale_app.main.service.FavoriteAPIService;
import com.universl.hp.vehicle_sale_app.main.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InsertFavorite {
    private Integer id;
    private Boolean isUpload;
    public InsertFavorite(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public Boolean getUpload() {
        return isUpload;
    }

    public void setUpload(Boolean upload) {
        isUpload = upload;
    }

    /*public Boolean upload_favorite(final Integer id, final String user_id){
        //Here we will handle the http request to insert user to mysql db
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL) //Setting the Root URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Creating object for our interface
        FavoriteAPIService api = retrofit.create(FavoriteAPIService.class);
        //Defining the method insertFavorite of our interface
        api.insertFavorite(id, user_id, new Callback<Response>() {
            @Override
            public void onResponse(@NonNull Call<Response> call, @NonNull Response<Response> response) {
               setUpload(true);
            }
            @Override
            public void onFailure(@NonNull Call<Response> call, @NonNull Throwable t) {
               setUpload(false);
            }
        });
        return getUpload();
    }*/
}
