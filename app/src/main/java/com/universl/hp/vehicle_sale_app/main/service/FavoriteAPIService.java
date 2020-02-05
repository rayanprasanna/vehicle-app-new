package com.universl.hp.vehicle_sale_app.main.service;

import com.universl.hp.vehicle_sale_app.main.response.BrandResponse;
import com.universl.hp.vehicle_sale_app.main.response.BriefResponse;
import com.universl.hp.vehicle_sale_app.main.response.CityResponse;
import com.universl.hp.vehicle_sale_app.main.response.DistrictResponse;
import com.universl.hp.vehicle_sale_app.main.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.main.response.ModelResponse;
import com.universl.hp.vehicle_sale_app.main.response.ServiceResponse;
import com.universl.hp.vehicle_sale_app.main.response.VehicleResponse;
import com.universl.hp.vehicle_sale_app.response.CarResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface FavoriteAPIService {
    @Multipart
    @POST("insert_favorite.php")
    Call<FavoriteResponse> insertFavorite(
            @Part("id") Integer id, @Part("user_id") String user_id
    );

    @GET("delete_favorite.php")
    Call<ArrayList<FavoriteResponse>> deleteFavorite(@Query("f_id") Integer id, @Query("user_id") String user_id, @Query("method") String method);

    @GET("get_favorite.php")
    Call<ArrayList<FavoriteResponse>> getFavorite(@Query("user_id") String user_id);

    @GET("get_vehicle_ads.php")
    Call<ArrayList<VehicleResponse>> getData(@Query("vehicle_type") String vehicle_type);

    @GET("get_vehicle_ads.php")
    Call<ArrayList<VehicleResponse>> getPointedData(@Query("vehicle_type") String vehicle_type, @Query("id") Integer id);

    @GET("search_ads.php")
    Call<ArrayList<VehicleResponse>> getSearchData(@Query("brand") String brand, @Query("model") String model, @Query("model_year") String model_year, @Query("district") String district);

    @GET("get_vehicle_ads.php")
    Call<ArrayList<BriefResponse>> getBriefData(@Query("vehicle_type") String vehicle_type);

    @GET("get_ur_ads.php")
    Call<ArrayList<VehicleResponse>> getUrAdsData(@Query("user_id") String user_id);

    @GET("delete_ur_ads.php")
    Call<ArrayList<VehicleResponse>> deleteUrAdsData(@Query("user_id") String user_id, @Query("id") Integer id);

    @GET("get_vehicle_ads.php")
    Call<ArrayList<ServiceResponse>> getServiceData(@Query("vehicle_type") String vehicle_type);

    @GET("get_all.php")
    Call<ArrayList<DistrictResponse>> getDistrictData(@Query("method") String method);

    @GET("get_all.php")
    Call<ArrayList<CityResponse>> getCityData(@Query("method") String method, @Query("id") Integer id);

    @GET("get_all.php")
    Call<ArrayList<BrandResponse>> getBrandData(@Query("method") String method, @Query("id") String id);

    @GET("get_all.php")
    Call<ArrayList<BrandResponse>> getAllBrandData(@Query("method") String method);

    @GET("get_all.php")
    Call<ArrayList<ModelResponse>> getModelData(@Query("method") String method, @Query("id") String id);

    @Multipart
    @POST("insert_vehicle.php")
    Call<VehicleResponse> uploadMultiple(
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

    @Multipart
    @POST("insert_vehicle.php")
    Call<ServiceResponse> insertService(
            @Part("status") String status,
            @Part("Category") String category,
            @Part("Name") String name,
            @Part("Address") String address,
            @Part("Contact_No")String contact
           );

}
