package com.universl.hp.vehicle_sale_app.main.response;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

public class ServiceResponse {
    @SerializedName("ID")
    public Integer id;
    @SerializedName("Category")
    public String category;
    @SerializedName("Name")
    public String name;
    @SerializedName("Address")
    public String address;
    @SerializedName("Contact_No")
    public String contact;
    @SerializedName("status")
    public String status;
}
