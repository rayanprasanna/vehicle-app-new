package com.universl.hp.vehicle_sale_app.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.main.help.Help;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity implements Help {
    String androidId;
    Boolean isSinhala = false;
    ImageButton sign, search, add;
    LinearLayout car_ads, motorbike_ads, other_ads, service_ads, brief_ads;
    TextView car_text_ads, motorbike_text_ads, other_text_ads, service_text_ads, brief_text_ads;
    @SuppressLint({"SetTextI18n", "HardwareIds"})
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Toolbar toolbar = findViewById(R.id.search_bar);
        sign = findViewById(R.id.sign); search = findViewById(R.id.search); add = findViewById(R.id.add);
        car_ads = findViewById(R.id.car_ads_new_UI);             car_text_ads = findViewById(R.id.car_text_new_UI);
        motorbike_ads = findViewById(R.id.motorbike_ads_new_UI); motorbike_text_ads = findViewById(R.id.motorbike_text_new_UI);
        other_ads = findViewById(R.id.other_ads_new_UI);         other_text_ads = findViewById(R.id.other_text_new_UI);
        service_ads = findViewById(R.id.service_ads_new_UI);     service_text_ads = findViewById(R.id.service_text_new_UI);
        brief_ads = findViewById(R.id.brief_ads_new_UI);         brief_text_ads = findViewById(R.id.brief_text_new_UI);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#ffffff'>වාහන</font>"));

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSinhala = !isSinhala;
                changeLanguage(isSinhala);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SearchActivity.class);
                intent.putExtra("isSinhala",isSinhala);
                intent.putExtra("android_id",androidId);
                intent.putExtra("activity", "menu");
                startActivity(intent);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, InsertAdvertisementActivity.class);
                intent.putExtra("isSinhala",isSinhala);
                intent.putExtra("android_id",androidId);
                intent.putExtra("activity", "menu");
                startActivity(intent);
                finish();
            }
        });
        car_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, VehicleDataActivity.class);
                intent.putExtra("type","Car");
                intent.putExtra("isSinhala",isSinhala);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }
        });
        motorbike_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, VehicleDataActivity.class);
                intent.putExtra("type","Motorbike");
                intent.putExtra("isSinhala",isSinhala);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }
        });
        other_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, VehicleDataActivity.class);
                intent.putExtra("type","Other");
                intent.putExtra("isSinhala",isSinhala);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }
        });
        brief_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, VehicleDataActivity.class);
                intent.putExtra("type","Brief");
                intent.putExtra("isSinhala",isSinhala);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }
        });
        service_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, VehicleDataActivity.class);
                intent.putExtra("type","Service");
                intent.putExtra("isSinhala",isSinhala);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }
        });

        if (getIntent().getBooleanExtra("pop", true)){
            changeLanguage(false);
            showCustomDialog();
        }else {
            changeLanguage(getIntent().getBooleanExtra("isSinhala", false));
            System.out.println(" Fuck You!");
        }
    }
    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity view_group
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.select_language, viewGroup, false);
        LinearLayout sinhala_layout = dialogView.findViewById(R.id.sinhala);
        LinearLayout english_layout = dialogView.findViewById(R.id.english);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        sinhala_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage(true);
                isSinhala = true;
                alertDialog.dismiss();
            }
        });
        english_layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                changeLanguage(false);
                isSinhala = false;
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.power) {
            logout();
            return true;
        }
        if (id == R.id.favorite_menu){
            Intent intent = new Intent(MenuActivity.this, MyFavoriteActivity.class);
            intent.putExtra("isSinhala",isSinhala);
            intent.putExtra("android_id",androidId);
            intent.putExtra("activity", "menu");
            startActivity(intent);
            finish();
        }
        if (id == R.id.user_menu){
            Intent intent = new Intent(MenuActivity.this, MyAdsActivity.class);
            intent.putExtra("isSinhala",isSinhala);
            intent.putExtra("android_id",androidId);
            intent.putExtra("activity", "menu");
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        System.exit(0);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void changeLanguage(Boolean isSinhala) {
        if (isSinhala){
            sign.setImageResource(R.drawable.si);
            car_text_ads.setText("කාර් දැන්වීම්");
            motorbike_text_ads.setText("මෝටර් සයිකල් දැන්වීම්");
            other_text_ads.setText("වෙනත් වාහන දැන්වීම්");
            service_text_ads.setText("අමතර කොටස් සහ සේවා");
            brief_text_ads.setText("ලුහුඬු දැන්වීම්");
        }else {
            sign.setImageResource(R.drawable.en);
            car_text_ads.setText("Car Advertisements");
            motorbike_text_ads.setText("Motor Bike Advertisements");
            other_text_ads.setText("Other Vehicle Advertisements");
            service_text_ads.setText("Spare Part and Service");
            brief_text_ads.setText("Brief Advertisements");
        }
    }
}
