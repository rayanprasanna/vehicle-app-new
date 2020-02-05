package com.universl.hp.vehicle_sale_app.main.response;

import com.google.gson.annotations.SerializedName;

public class BriefResponse {
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("contact")
    private String contact;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContact() {
        return contact;
    }
}
