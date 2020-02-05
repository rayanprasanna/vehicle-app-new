package com.universl.hp.vehicle_sale_app.main.response;

import com.google.gson.annotations.SerializedName;

public class VehicleResponse {
    @SerializedName("id")
    private Integer id;
    @SerializedName("status")
    private String status;
    @SerializedName("title")
    private String title;
    @SerializedName("district")
    private String district;
    @SerializedName("city")
    private String city;
    @SerializedName("vehicle_type")
    private String vehicle_type;
    @SerializedName("brand")
    private String brand;
    @SerializedName("model")
    private String model;
    @SerializedName("trim")
    private String trim;
    @SerializedName("model_year")
    private String model_year;
    @SerializedName("conditions")
    private String condition;
    @SerializedName("mileage")
    private String mileage;
    @SerializedName("body_type")
    private String body_type;
    @SerializedName("transmission")
    private String transmission;
    @SerializedName("fuel_type")
    private String fuel_type;
    @SerializedName("engine_capacity")
    private String engine_capacity;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private String price;
    @SerializedName("contact")
    private String contact;
    @SerializedName("image_path_1")
    private String image_path_1;
    @SerializedName("image_path_2")
    private String image_path_2;
    @SerializedName("image_path_3")
    private String image_path_3;
    @SerializedName("image_path_4")
    private String image_path_4;
    @SerializedName("image_path_5")
    private String image_path_5;
    @SerializedName("user_id")
    private String user_id;

    public String getTitle() {
        return title;
    }
    public String getStatus() {
        return status;
    }
    public String getDistrict() {
        return district;
    }
    public String getCity() {
        return city;
    }
    public String getVehicle_type() {
        return vehicle_type;
    }
    public String getBrand() {
        return brand;
    }
    public String getModel() {
        return model;
    }
    public String getTrim() {
        return trim;
    }
    public String getModel_year() {
        return model_year;
    }
    public String getCondition() {
        return condition;
    }
    public String getMileage() {
        return mileage;
    }
    public String getBody_type() {
        return body_type;
    }
    public String getTransmission() {
        return transmission;
    }
    public String getFuel_type() {
        return fuel_type;
    }
    public String getEngine_capacity() {
        return engine_capacity;
    }
    public String getDescription() {
        return description;
    }
    public String getPrice() {
        return price;
    }
    public String getContact() {
        return contact;
    }
    public String getImage_path_1() {
        return image_path_1;
    }
    public String getImage_path_2() {
        return image_path_2;
    }
    public String getImage_path_3() {
        return image_path_3;
    }
    public String getImage_path_4() {
        return image_path_4;
    }
    public String getImage_path_5() {
        return image_path_5;
    }
    public String getUser_id() {
        return user_id;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
