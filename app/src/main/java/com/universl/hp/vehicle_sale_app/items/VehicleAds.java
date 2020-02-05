package com.universl.hp.vehicle_sale_app.items;

public class VehicleAds {

    private String brand;
    private String year;
    private String price;
    private String vehicle_type;
    private String image_path;

    public VehicleAds() {
    }

    public VehicleAds(String brand, String year, String price, String vehicle_type,String image_path) {
        this.brand = brand;
        this.year = year;
        this.price = price;
        this.vehicle_type = vehicle_type;
        this.image_path = image_path;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
