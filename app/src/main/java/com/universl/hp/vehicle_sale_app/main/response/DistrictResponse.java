package com.universl.hp.vehicle_sale_app.main.response;

import com.google.gson.annotations.SerializedName;

public class DistrictResponse {
    @SerializedName("id")
    public Integer id;
    @SerializedName("province_id")
    public Integer province_id;
    @SerializedName("name_si")
    public String name_si;
    @SerializedName("name_en")
    public String name_en;
    @SerializedName("name_ta")
    public String name_ta;
}
