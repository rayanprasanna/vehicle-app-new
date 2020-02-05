package com.universl.hp.vehicle_sale_app.items;

public class ItemData {

    private String text;
    private Integer image_id;

    public ItemData(String text, Integer image_id) {
        this.text = text;
        this.image_id = image_id;
    }

    public ItemData(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Integer getImage_id() {
        return image_id;
    }
}