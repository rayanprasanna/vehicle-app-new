package com.universl.hp.vehicle_sale_app.main.help;

import android.support.annotation.NonNull;

import com.universl.hp.vehicle_sale_app.main.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.main.service.FavoriteAPIService;
import com.universl.hp.vehicle_sale_app.main.utils.Constants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchFavorite {
    private String user_id;
    private ArrayList<FavoriteResponse> favoriteResponses;

    public FetchFavorite(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public ArrayList<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }

    public void setFavoriteResponses(ArrayList<FavoriteResponse> favoriteResponses) {
        this.favoriteResponses = favoriteResponses;
    }

    public ArrayList<FavoriteResponse> fetchData(final String user_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL) //Setting the Root URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Creating object for our interface
        FavoriteAPIService api = retrofit.create(FavoriteAPIService.class);
        //Defining the method insertFavorite of our interface
        api.getFavorite(user_id).enqueue(new Callback<ArrayList<FavoriteResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<FavoriteResponse>> call, @NonNull Response<ArrayList<FavoriteResponse>> response) {
                setFavoriteResponses(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<FavoriteResponse>> call, @NonNull Throwable t) {
                setFavoriteResponses(null);
            }
        });
        return getFavoriteResponses();
    }
}
