package com.universl.hp.vehicle_sale_app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.universl.hp.vehicle_sale_app.sub_activity.CarAdsActivity;
import com.universl.hp.vehicle_sale_app.sub_activity.MotorBikeScooterAdsActivity;
import com.universl.hp.vehicle_sale_app.sub_activity.OtherVehicleAdsActivity;
import com.universl.hp.vehicle_sale_app.sub_activity.SparePartServiceActivity;
import com.universl.hp.vehicle_sale_app.sub_activity.VanBusLorryAdsActivity;

import java.util.Objects;

public class PostAdsActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ads);

        Button car = findViewById(R.id.car_advertise);
        Button van_bus_lorry = findViewById(R.id.van_Bus_and_lorry_advertise);
        Button motorbike_scooter = findViewById(R.id.scooter_motorbike_advertise);
        Button other_vehicle = findViewById(R.id.other_vehicle_advertise);
        Button auto_service = findViewById(R.id.auto_part_and_service_advertise);

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostAdsActivity.this, CarAdsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        van_bus_lorry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostAdsActivity.this, VanBusLorryAdsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        motorbike_scooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostAdsActivity.this, MotorBikeScooterAdsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        other_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostAdsActivity.this, OtherVehicleAdsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        auto_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostAdsActivity.this, SparePartServiceActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent = new Intent(PostAdsActivity.this, MainMenuActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PostAdsActivity.this, MainMenuActivity.class);
        startActivity(intent);
        this.finish();
    }
}
