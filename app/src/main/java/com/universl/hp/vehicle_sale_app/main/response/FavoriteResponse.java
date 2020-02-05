package com.universl.hp.vehicle_sale_app.main.response;

import com.google.gson.annotations.SerializedName;

public class FavoriteResponse {
    @SerializedName("id")
    private Integer id;
    @SerializedName("f_id")
    private Integer f_id;
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

    public FavoriteResponse(Integer id, Integer f_id, String title, String district, String city, String vehicle_type, String brand, String model, String trim, String model_year, String condition, String mileage, String body_type, String transmission, String fuel_type, String engine_capacity, String description, String price, String contact, String image_path_1, String image_path_2, String image_path_3, String image_path_4, String image_path_5, String user_id) {
        this.id = id;
        this.f_id = f_id;
        this.title = title;
        this.district = district;
        this.city = city;
        this.vehicle_type = vehicle_type;
        this.brand = brand;
        this.model = model;
        this.trim = trim;
        this.model_year = model_year;
        this.condition = condition;
        this.mileage = mileage;
        this.body_type = body_type;
        this.transmission = transmission;
        this.fuel_type = fuel_type;
        this.engine_capacity = engine_capacity;
        this.description = description;
        this.price = price;
        this.contact = contact;
        this.image_path_1 = image_path_1;
        this.image_path_2 = image_path_2;
        this.image_path_3 = image_path_3;
        this.image_path_4 = image_path_4;
        this.image_path_5 = image_path_5;
        this.user_id = user_id;
    }

    public Integer getId() {
        return id;
    }
    public Integer getF_id() {
        return f_id;
    }
    public String getTitle() {
        return title;
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
}
