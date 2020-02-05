package com.universl.hp.vehicle_sale_app.main.response;

import com.google.gson.annotations.SerializedName;

public class BrandResponse {
    @SerializedName("id")
    public Integer id;
    @SerializedName("brand")
    public String brand;
    @SerializedName("type")
    public String type;
}
