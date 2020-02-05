package com.universl.hp.vehicle_sale_app.main.response;

import com.google.gson.annotations.SerializedName;

public class CityResponse {
    @SerializedName("id")
    public Integer id;
    @SerializedName("district_id")
    public Integer district_id;
    @SerializedName("name_si")
    public String name_si;
    @SerializedName("name_en")
    public String name_en;
    @SerializedName("name_ta")
    public String name_ta;
    @SerializedName("sub_name_si")
    public String sub_name_si;
    @SerializedName("sub_name_en")
    public String sub_name_en;
    @SerializedName("sub_name_ta")
    public String sub_name_ta;
    @SerializedName("postcode")
    public String postcode;
    @SerializedName("latitude")
    public Double latitude;
    @SerializedName("longitude")
    public Double longitude;
}
