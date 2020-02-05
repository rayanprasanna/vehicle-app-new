package com.universl.hp.vehicle_sale_app.response;

import com.google.gson.annotations.SerializedName;

public class SparePartServiceResponse {

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
