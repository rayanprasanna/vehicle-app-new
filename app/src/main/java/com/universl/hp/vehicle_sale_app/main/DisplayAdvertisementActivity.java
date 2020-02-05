package com.universl.hp.vehicle_sale_app.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.items.GlideApp;
import com.universl.hp.vehicle_sale_app.main.help.Help;
import com.universl.hp.vehicle_sale_app.main.response.VehicleResponse;
import com.universl.hp.vehicle_sale_app.main.service.FavoriteAPIService;
import com.universl.hp.vehicle_sale_app.main.utils.Constants;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayAdvertisementActivity extends AppCompatActivity implements Help {
    String androidId;
    Boolean isSinhalaLanguage = false, isSinhalaSearch;
    ImageButton sign, search, add;
    ImageView image_1, image_2, image_3, image_4, image_5, call_image;
    TextView title, price, condition, mileage, transmission, fuel, location, location_text, brand, brand_text, model, model_text, model_year, model_year_text, body_type, body_type_text, engine_capacity, engine_capacity_text, description, description_text;

    ArrayList<VehicleResponse> vehicleResponseArrayList = new ArrayList<>();
    ListView dataList;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_advertisement);

        Toolbar toolbar = findViewById(R.id.search_bar);
        sign = findViewById(R.id.sign); search = findViewById(R.id.search); add = findViewById(R.id.add);
        image_1 = findViewById(R.id.selected_image_1);image_2 = findViewById(R.id.selected_image_2);
        image_3 = findViewById(R.id.selected_image_3);image_4 = findViewById(R.id.selected_image_4);
        image_5 = findViewById(R.id.selected_image_5);call_image = findViewById(R.id.call);
        title = findViewById(R.id.details_title);price = findViewById(R.id.details_price);
        condition = findViewById(R.id.condition_text);mileage = findViewById(R.id.mileage_text);
        transmission = findViewById(R.id.transmission_text);fuel = findViewById(R.id.fuel_text);
        location = findViewById(R.id.location);location_text = findViewById(R.id.location_text);
        brand = findViewById(R.id.brand);brand_text = findViewById(R.id.brand_text);
        model = findViewById(R.id.model);model_text = findViewById(R.id.model_text);
        model_year = findViewById(R.id.model_year);model_year_text = findViewById(R.id.model_year_text);
        body_type = findViewById(R.id.body_type);body_type_text = findViewById(R.id.body_type_text);
        engine_capacity = findViewById(R.id.engine_capacity);engine_capacity_text = findViewById(R.id.engine_capacity_text);
        description = findViewById(R.id.description);description_text = findViewById(R.id.description_text);

        dataList = findViewById(R.id.vehicle_data_listView);

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#ffffff'>වාහන</font>"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage(!isSinhalaLanguage);
                isSinhalaSearch = !isSinhalaLanguage;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayAdvertisementActivity.this, SearchActivity.class);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                intent.putExtra("activity", "displayAdvertisement");
                intent.putExtra("id", getIntent().getIntExtra("id",0));
                startActivity(intent);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayAdvertisementActivity.this, InsertAdvertisementActivity.class);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                intent.putExtra("activity", "displayAdvertisement");
                intent.putExtra("id", getIntent().getIntExtra("id",0));
                startActivity(intent);
                finish();
            }
        });
        fetchData(getIntent().getIntExtra("id",0));
        changeLanguage(getIntent().getBooleanExtra("isSinhala", false));
    }
    private void fetchData(final Integer id){
        final ProgressDialog progress = new ProgressDialog(DisplayAdvertisementActivity.this);
        progress.setIcon(R.mipmap.ic_icon);
        progress.setTitle("වාහන");
        progress.setMessage("Data downloading !");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //The logging interceptor will be added to the http client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        //The Retrofit builder will have the client attached, in order to get connection logs
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();
        //Creating object for our interface
        FavoriteAPIService api = retrofit.create(FavoriteAPIService.class);
        //Defining the method insertFavorite of our interface
        //Here a logging interceptor is created
        Call<ArrayList<VehicleResponse>> call = api.getPointedData("Point", id);
        call.enqueue(new Callback<ArrayList<VehicleResponse>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ArrayList<VehicleResponse>> call, @NonNull Response<ArrayList<VehicleResponse>> response) {
                if (response.body() != null) {
                    progress.dismiss();
                    vehicleResponseArrayList.addAll(response.body());
                    call_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+vehicleResponseArrayList.get(0).getContact()));
                            (DisplayAdvertisementActivity.this).startActivity(intent);
                        }
                    });
                    GlideApp.with(DisplayAdvertisementActivity.this)
                            .load(vehicleResponseArrayList.get(0).getImage_path_1())
                            .fitCenter()
                            .into(image_1);
                    if (!vehicleResponseArrayList.get(0).getImage_path_2().equals("")){
                        GlideApp.with(DisplayAdvertisementActivity.this)
                                .load(vehicleResponseArrayList.get(0).getImage_path_2())
                                .fitCenter()
                                .into(image_2);
                    }
                    if (!vehicleResponseArrayList.get(0).getImage_path_3().equals("")){
                        GlideApp.with(DisplayAdvertisementActivity.this)
                                .load(vehicleResponseArrayList.get(0).getImage_path_3())
                                .fitCenter()
                                .into(image_3);
                    }
                    if (!vehicleResponseArrayList.get(0).getImage_path_4().equals("")){
                        GlideApp.with(DisplayAdvertisementActivity.this)
                                .load(vehicleResponseArrayList.get(0).getImage_path_4())
                                .fitCenter()
                                .into(image_4);
                    }
                    if (!vehicleResponseArrayList.get(0).getImage_path_5().equals("")){
                        GlideApp.with(DisplayAdvertisementActivity.this)
                                .load(vehicleResponseArrayList.get(0).getImage_path_5())
                                .fitCenter()
                                .into(image_5);
                    }
                    if (!vehicleResponseArrayList.get(0).getTitle().equalsIgnoreCase("NULL")){
                        title.setText(vehicleResponseArrayList.get(0).getTitle());
                    }
                    if (!vehicleResponseArrayList.get(0).getPrice().equalsIgnoreCase("NULL")){
                        price.setText("Rs " + vehicleResponseArrayList.get(0).getPrice() + "/=");
                    }
                    if (!vehicleResponseArrayList.get(0).getCondition().equalsIgnoreCase("NULL")){
                        condition.setText(vehicleResponseArrayList.get(0).getCondition());
                    }
                    if (!vehicleResponseArrayList.get(0).getMileage().equalsIgnoreCase("NULL")){
                        mileage.setText(vehicleResponseArrayList.get(0).getMileage() + "km");
                    }
                    if (!vehicleResponseArrayList.get(0).getTransmission().equalsIgnoreCase("NULL")){
                        transmission.setText(vehicleResponseArrayList.get(0).getTransmission());
                    }
                    if (!vehicleResponseArrayList.get(0).getFuel_type().equalsIgnoreCase("NULL")){
                        fuel.setText(vehicleResponseArrayList.get(0).getFuel_type());
                    }
                    if (!vehicleResponseArrayList.get(0).getBrand().equalsIgnoreCase("NULL")){
                        brand_text.setText(vehicleResponseArrayList.get(0).getBrand());
                    }
                    if (!vehicleResponseArrayList.get(0).getModel().equalsIgnoreCase("NULL")){
                        model_text.setText(vehicleResponseArrayList.get(0).getModel());
                    }
                    if (!vehicleResponseArrayList.get(0).getModel_year().equalsIgnoreCase("NULL")){
                        model_year_text.setText(vehicleResponseArrayList.get(0).getModel_year());
                    }
                    if (!vehicleResponseArrayList.get(0).getBody_type().equalsIgnoreCase("NULL")){
                        body_type_text.setText(vehicleResponseArrayList.get(0).getBody_type());
                    }
                    if (!vehicleResponseArrayList.get(0).getEngine_capacity().equalsIgnoreCase("NULL")){
                        engine_capacity_text.setText(vehicleResponseArrayList.get(0).getEngine_capacity() + "cc");
                    }if (!vehicleResponseArrayList.get(0).getDescription().equalsIgnoreCase("NULL")){
                        description_text.setText(vehicleResponseArrayList.get(0).getDescription());
                    }
                    //title.setText(vehicleResponseArrayList.get(0).getTitle());
                    //.setText("Rs " + vehicleResponseArrayList.get(0).getPrice() + "/=");
                    //condition.setText(vehicleResponseArrayList.get(0).getCondition());
                    //mileage.setText(vehicleResponseArrayList.get(0).getMileage() + "km");
                    //transmission.setText(vehicleResponseArrayList.get(0).getTransmission());
                    //fuel.setText(vehicleResponseArrayList.get(0).getFuel_type());
                    location_text.setText(vehicleResponseArrayList.get(0).getDistrict() + "," + vehicleResponseArrayList.get(0).getCity());
                    //brand_text.setText(vehicleResponseArrayList.get(0).getBrand());
                    //model_text.setText(vehicleResponseArrayList.get(0).getModel());
                    //model_year_text.setText(vehicleResponseArrayList.get(0).getModel_year());
                    //body_type_text.setText(vehicleResponseArrayList.get(0).getBody_type());
                    //engine_capacity_text.setText(vehicleResponseArrayList.get(0).getEngine_capacity() + "cc");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<VehicleResponse>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            String type;
            type = getIntent().getStringExtra("type");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> " + type + " <<<<<<<<<<<<<<<<<<<<<<");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> " + type + " <<<<<<<<<<<<<<<<<<<<<<");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> " + type + " <<<<<<<<<<<<<<<<<<<<<<");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> " + type + " <<<<<<<<<<<<<<<<<<<<<<");
            Intent intent = new Intent(DisplayAdvertisementActivity.this, VehicleDataActivity.class);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            intent.putExtra("type", type);
            intent.putExtra("pop", false);
            intent.putExtra("search_brand",getIntent().getStringExtra("search_brand"));
            intent.putExtra("search_model",getIntent().getStringExtra("search_model"));
            intent.putExtra("search_model_year",getIntent().getStringExtra("search_model_year"));
            intent.putExtra("search_district",getIntent().getStringExtra("search_district"));
            startActivity(intent);
            finish();
        }
        if (id == R.id.power) {
            logout();
            return true;
        }
        if (id == R.id.favorite_menu){
            Intent intent = new Intent(DisplayAdvertisementActivity.this, MyFavoriteActivity.class);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            intent.putExtra("activity", "displayAdvertisement");
            intent.putExtra("id", getIntent().getIntExtra("id",0));
            startActivity(intent);
            finish();
        }
        if (id == R.id.user_menu){
            Intent intent = new Intent(DisplayAdvertisementActivity.this, MyAdsActivity.class);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            intent.putExtra("activity", "displayAdvertisement");
            intent.putExtra("id", getIntent().getIntExtra("id",0));
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
            isSinhalaLanguage = true;
            isSinhalaSearch = true;
            location.setText("ස්ථානය :");
            brand.setText("වෙළඳ නාමය :");
            model.setText("මාදිලිය :");
            model_year.setText("නිෂ්පාදිත වර්ෂය :");
            body_type.setText("ඛඳ වර්ගය :");
            engine_capacity.setText("ධාරිතාව :");
            description.setText("විස්තර :");
        }else {
            sign.setImageResource(R.drawable.en);
            isSinhalaLanguage = false;
            isSinhalaSearch = false;
            location.setText("Location :");
            brand.setText("Brand :");
            model.setText("Model :");
            model_year.setText("Model Year :");
            body_type.setText("Body Type :");
            engine_capacity.setText("Engine Capacity :");
            description.setText("Description :");
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DisplayAdvertisementActivity.this, VehicleDataActivity.class);
        intent.putExtra("isSinhala", isSinhalaLanguage);
        intent.putExtra("type", getIntent().getStringExtra("type"));
        intent.putExtra("pop", false);
        intent.putExtra("search_brand",vehicleResponseArrayList.get(0).getBrand());
        intent.putExtra("search_model",vehicleResponseArrayList.get(0).getModel());
        intent.putExtra("search_model_year",vehicleResponseArrayList.get(0).getModel_year());
        intent.putExtra("search_district",vehicleResponseArrayList.get(0).getDistrict());
        startActivity(intent);
        finish();
    }
}
