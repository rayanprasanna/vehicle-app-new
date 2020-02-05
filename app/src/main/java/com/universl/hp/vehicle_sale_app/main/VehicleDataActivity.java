package com.universl.hp.vehicle_sale_app.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.main.adapter.BriefAdapter;
import com.universl.hp.vehicle_sale_app.main.adapter.ServiceAdapter;
import com.universl.hp.vehicle_sale_app.main.adapter.ServiceTypeAdapter;
import com.universl.hp.vehicle_sale_app.main.adapter.VehicleAdsAdapter;
import com.universl.hp.vehicle_sale_app.main.help.Help;
import com.universl.hp.vehicle_sale_app.main.response.BriefResponse;
import com.universl.hp.vehicle_sale_app.main.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.main.response.ServiceResponse;
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

public class VehicleDataActivity extends AppCompatActivity implements Help {
    String androidId;
    Boolean isSinhalaLanguage = false;
    VehicleAdsAdapter vehicleAdsAdapter;
    BriefAdapter briefAdapter;
    ServiceTypeAdapter serviceTypeAdapter;
    ServiceAdapter serviceAdapter;
    ImageButton sign, search, add;
    ArrayList<ServiceResponse> serviceResponseArrayList = new ArrayList<>();
    ArrayList<VehicleResponse> vehicleResponseArrayList = new ArrayList<>();
    ArrayList<BriefResponse> briefResponseArrayList = new ArrayList<>();
    ArrayList<FavoriteResponse> favoriteResponseArrayList = new ArrayList<>();
    ArrayList<String> image_path = new ArrayList<>();
    ArrayList<String> category = new ArrayList<>();
    ListView dataList;
    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_data);
        Toolbar toolbar = findViewById(R.id.search_bar);
        sign = findViewById(R.id.sign); search = findViewById(R.id.search); add = findViewById(R.id.add);
        dataList = findViewById(R.id.vehicle_data_listView);

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#ffffff'>වාහන</font>"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getStringExtra("type").equalsIgnoreCase("Car")){
            fetchData(getIntent().getStringExtra("type"));
        }else if (getIntent().getStringExtra("type").equalsIgnoreCase("Motorbike")){
            fetchData(getIntent().getStringExtra("type"));
        }else if (getIntent().getStringExtra("type").equalsIgnoreCase("Other")){
            fetchData(getIntent().getStringExtra("type"));
        }else if (getIntent().getStringExtra("type").equalsIgnoreCase("Brief")){
            fetchBriefData(getIntent().getStringExtra("type"));
        }else if (getIntent().getStringExtra("type").equalsIgnoreCase("Service")){
            fetchServiceData(getIntent().getStringExtra("type"));
        }else if (getIntent().getStringExtra("type").equalsIgnoreCase("Category")){
            fetchStationData();
        }else if (getIntent().getStringExtra("type").equalsIgnoreCase("Search")){
            fetchSearchData(
                    getIntent().getStringExtra("search_brand"),
                    getIntent().getStringExtra("search_model"),
                    getIntent().getStringExtra("search_model_year"),
                    getIntent().getStringExtra("search_district")
            );
        }
        changeLanguage(getIntent().getBooleanExtra("isSinhala", false));
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage(!isSinhalaLanguage);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleDataActivity.this, SearchActivity.class);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                intent.putExtra("activity", "vehicleData");
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivity(intent);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleDataActivity.this, InsertAdvertisementActivity.class);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                intent.putExtra("activity", "vehicleData");
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivity(intent);
                finish();
            }
        });
    }
    private void fetchData(final String vehicle_type){
        final ProgressDialog progress = new ProgressDialog(VehicleDataActivity.this);
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
        Call<ArrayList<VehicleResponse>> call = api.getData(vehicle_type);
        call.enqueue(new Callback<ArrayList<VehicleResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VehicleResponse>> call, @NonNull Response<ArrayList<VehicleResponse>> response) {
                if (response.body() != null) {
                    fetchFavoriteData(getIntent().getStringExtra("android_id"));
                    progress.dismiss();
                    vehicleResponseArrayList.addAll(response.body());
                    vehicleAdsAdapter = new VehicleAdsAdapter(VehicleDataActivity.this,vehicleResponseArrayList,favoriteResponseArrayList, image_path,getIntent().getStringExtra("android_id"), getIntent().getBooleanExtra("isSinhala", false), getIntent().getStringExtra("type"), getIntent().getStringExtra("search_brand"), getIntent().getStringExtra("search_model"), getIntent().getStringExtra("search_model_year"), getIntent().getStringExtra("search_district"));
                    dataList.setAdapter(vehicleAdsAdapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<VehicleResponse>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void fetchFavoriteData(final String user_id){
        final ProgressDialog progress = new ProgressDialog(VehicleDataActivity.this);
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
        Call<ArrayList<FavoriteResponse>> call = api.getFavorite(user_id);
        call.enqueue(new Callback<ArrayList<FavoriteResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<FavoriteResponse>> call, @NonNull Response<ArrayList<FavoriteResponse>> response) {
                progress.dismiss();
                if (response.body() != null) {
                    favoriteResponseArrayList.addAll(response.body());
                    for (int i = 0; i < favoriteResponseArrayList.size(); i++){
                        image_path.add(favoriteResponseArrayList.get(i).getImage_path_1());
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<FavoriteResponse>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void fetchBriefData(final String brief){
        final ProgressDialog progress = new ProgressDialog(VehicleDataActivity.this);
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
        Call<ArrayList<BriefResponse>> call = api.getBriefData(brief);
        call.enqueue(new Callback<ArrayList<BriefResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<BriefResponse>> call, @NonNull Response<ArrayList<BriefResponse>> response) {
                if (response.body() != null) {
                    fetchFavoriteData(getIntent().getStringExtra("android_id"));
                    progress.dismiss();
                    briefResponseArrayList.addAll(response.body());
                    briefAdapter = new BriefAdapter(VehicleDataActivity.this, briefResponseArrayList);
                    dataList.setAdapter(briefAdapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<BriefResponse>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void fetchServiceData(final String service){
        final ProgressDialog progress = new ProgressDialog(VehicleDataActivity.this);
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
        Call<ArrayList<ServiceResponse>> call = api.getServiceData(service);
        call.enqueue(new Callback<ArrayList<ServiceResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ServiceResponse>> call, @NonNull Response<ArrayList<ServiceResponse>> response) {
                if (response.body() != null) {
                    fetchFavoriteData(getIntent().getStringExtra("android_id"));
                    progress.dismiss();
                    serviceResponseArrayList.addAll(response.body());
                    for (int i = 0; i < serviceResponseArrayList.size(); i++){
                        category.add(serviceResponseArrayList.get(i).category);
                    }
                    serviceTypeAdapter = new ServiceTypeAdapter(VehicleDataActivity.this, removeDuplicates(category));
                    dataList.setAdapter(serviceTypeAdapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<ServiceResponse>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void fetchStationData(){
        final ProgressDialog progress = new ProgressDialog(VehicleDataActivity.this);
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
        Call<ArrayList<ServiceResponse>> call = api.getServiceData("Service");
        call.enqueue(new Callback<ArrayList<ServiceResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ServiceResponse>> call, @NonNull Response<ArrayList<ServiceResponse>> response) {
                if (response.body() != null) {
                    fetchFavoriteData(getIntent().getStringExtra("android_id"));
                    progress.dismiss();
                    for (int i = 0; i < response.body().size(); i++){
                        if (response.body().get(i).category.equalsIgnoreCase(getIntent().getStringExtra("category"))){
                            serviceResponseArrayList.addAll(response.body());
                        }
                    }
                    serviceAdapter = new ServiceAdapter(VehicleDataActivity.this, serviceResponseArrayList);
                    dataList.setAdapter(serviceAdapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<ServiceResponse>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void fetchSearchData(final String brand, final String model, final String mode_year, final String district){
        final ProgressDialog progress = new ProgressDialog(VehicleDataActivity.this);
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
        Call<ArrayList<VehicleResponse>> call = api.getSearchData(brand, model, mode_year, district);
        call.enqueue(new Callback<ArrayList<VehicleResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VehicleResponse>> call, @NonNull Response<ArrayList<VehicleResponse>> response) {
                if (response.body() != null) {
                    fetchFavoriteData(getIntent().getStringExtra("android_id"));
                    progress.dismiss();
                    vehicleResponseArrayList.addAll(response.body());
                    vehicleAdsAdapter = new VehicleAdsAdapter(VehicleDataActivity.this,vehicleResponseArrayList,favoriteResponseArrayList, image_path,getIntent().getStringExtra("android_id"), getIntent().getBooleanExtra("isSinhala", false), getIntent().getStringExtra("type"), getIntent().getStringExtra("search_brand"), getIntent().getStringExtra("search_model"), getIntent().getStringExtra("search_model_year"), getIntent().getStringExtra("search_district"));
                    dataList.setAdapter(vehicleAdsAdapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<VehicleResponse>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
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
            Intent intent = new Intent(VehicleDataActivity.this, MenuActivity.class);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            intent.putExtra("pop", false);
            startActivity(intent);
            finish();
        }
        if (id == R.id.power) {
            logout();
            return true;
        }
        if (id == R.id.favorite_menu){
            Intent intent = new Intent(VehicleDataActivity.this, MyFavoriteActivity.class);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            intent.putExtra("activity", "vehicleData");
            intent.putExtra("type", getIntent().getStringExtra("type"));
            startActivity(intent);
            finish();
        }
        if (id == R.id.user_menu){
            Intent intent = new Intent(VehicleDataActivity.this, MyAdsActivity.class);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            intent.putExtra("activity", "vehicleData");
            intent.putExtra("type", getIntent().getStringExtra("type"));
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
        }else {
            sign.setImageResource(R.drawable.en);
            isSinhalaLanguage = false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VehicleDataActivity.this, MenuActivity.class);
        intent.putExtra("isSinhala", isSinhalaLanguage);
        intent.putExtra("pop", false);
        startActivity(intent);
        finish();
    }
}
