package com.universl.hp.vehicle_sale_app.service;

import com.universl.hp.vehicle_sale_app.response.CarResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("insert_all_vehicle.php")
    Call<CarResponse> uploadMultiple(
            @Part("description") RequestBody description,
            @Part("size") RequestBody size,
            @Part("status") RequestBody status,
            @Part("title") RequestBody title,
            @Part("district")RequestBody district,
            @Part("city")RequestBody city,
            @Part("vehicle_type")RequestBody vehicle_type,
            @Part("user_id")RequestBody user_id,
            @Part("brand")RequestBody brand,
            @Part("model")RequestBody model,
            @Part("trim")RequestBody trim,
            @Part("model_year")RequestBody model_year,
            @Part("conditions")RequestBody conditions,
            @Part("mileage")RequestBody mileage,
            @Part("body_type")RequestBody body_type,
            @Part("transmission")RequestBody transmission,
            @Part("fuel_type")RequestBody fuel_type,
            @Part("engine_capacity")RequestBody engine_capacity,
            @Part("description")RequestBody descriptions,
            @Part("price")RequestBody price,
            @Part("contact")RequestBody contact,
            @Part List<MultipartBody.Part> files);
}
