package com.universl.hp.vehicle_sale_app.main.response;

import com.google.gson.annotations.SerializedName;

public class ModelResponse {
    @SerializedName("id")
    public Integer id;
    @SerializedName("model")
    public String model;
    @SerializedName("brand_name")
    public String brand_name;
}
