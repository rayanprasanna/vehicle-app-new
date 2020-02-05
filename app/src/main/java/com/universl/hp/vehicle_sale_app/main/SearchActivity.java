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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.main.help.Help;
import com.universl.hp.vehicle_sale_app.main.response.BrandResponse;
import com.universl.hp.vehicle_sale_app.main.response.DistrictResponse;
import com.universl.hp.vehicle_sale_app.main.response.ModelResponse;
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

public class SearchActivity extends AppCompatActivity implements Help {
    TextView district, brand, model, model_year;
    Button post;
    EditText model_year_edit_text;
    Spinner district_spinner, brand_spinner, model_spinner;
    String androidId, brand_text, model_text, model_year_text, district_text;
    Boolean isSinhalaLanguage = false, isSinhalaSearch;
    ImageButton sign, search, add;
    ArrayList<DistrictResponse> districtResponseArrayList = new ArrayList<>();
    ArrayList<BrandResponse> brandResponseArrayList = new ArrayList<>();
    ArrayList<ModelResponse> modelResponseArrayList = new ArrayList<>();
    ArrayList<String> districtArrayList = new ArrayList<>();
    ArrayList<String> brandArrayList = new ArrayList<>(125);
    ArrayList<String> modelArrayList = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint({"HardwareIds", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.search_bar);
        sign = findViewById(R.id.sign); search = findViewById(R.id.search); add = findViewById(R.id.add);
        district = findViewById(R.id.district_text);brand = findViewById(R.id.brand_text);
        model = findViewById(R.id.model_text);model_year = findViewById(R.id.model_year_text);
        post = findViewById(R.id.post_button);model_year_edit_text = findViewById(R.id.year_text);
        district_spinner = findViewById(R.id.district_spinner);brand_spinner = findViewById(R.id.brand_spinner);
        model_spinner = findViewById(R.id.model_spinner);

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#ffffff'>වාහන</font>"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model_year_text = model_year_edit_text.getText().toString();
                Intent intent = new Intent(SearchActivity.this, VehicleDataActivity.class);
                intent.putExtra("type","Search");
                intent.putExtra("search_brand",brand_text);
                intent.putExtra("search_model",model_text);
                intent.putExtra("search_model_year",model_year_text);
                intent.putExtra("search_district",district_text);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }
        });
        changeLanguage(getIntent().getBooleanExtra("isSinhala", false));
        if(getIntent().getBooleanExtra("isSinhala", false)){
            post.setText("සොයන්න");
        }else {
            post.setText("Search");
        }
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage(!isSinhalaLanguage);
                isSinhalaSearch = !getIntent().getBooleanExtra("isSinhala", false);
                if(isSinhalaSearch){
                    post.setText("සොයන්න");
                }else {
                    post.setText("Search");
                }
                fetchDistrictData();
                fetchBrandData();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();*/
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, InsertAdvertisementActivity.class);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                intent.putExtra("activity", "search");
                startActivity(intent);
                finish();
            }
        });
        model_year_text = model_year_edit_text.getText().toString();
    }
    private void fetchDistrictData(){
        final ProgressDialog progress = new ProgressDialog(SearchActivity.this);
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
        Call<ArrayList<DistrictResponse>> call = api.getDistrictData("districts");
        call.enqueue(new Callback<ArrayList<DistrictResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<DistrictResponse>> call, @NonNull Response<ArrayList<DistrictResponse>> response) {
                if (response.body() != null) {
                    progress.dismiss();
                    districtArrayList.clear();
                    if(isSinhalaSearch){
                        districtArrayList.add("දිස්ත්\u200Dරික්කය");
                    }else {
                        districtArrayList.add("District");
                    }
                    districtResponseArrayList.addAll(response.body());
                    for (int i =0; i < districtResponseArrayList.size(); i++){
                        if (isSinhalaSearch){
                            districtArrayList.add(districtResponseArrayList.get(i).name_si);
                        }else {
                            districtArrayList.add(districtResponseArrayList.get(i).name_en);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,districtArrayList);
                    district_spinner.setAdapter(adapter); // this will set list of values to spinner
                    district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            district_text = parent.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<DistrictResponse>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void fetchBrandData(){
        final ProgressDialog progress = new ProgressDialog(SearchActivity.this);
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
        Call<ArrayList<BrandResponse>> call = api.getAllBrandData("all_brand");
        call.enqueue(new Callback<ArrayList<BrandResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<BrandResponse>> call, @NonNull Response<ArrayList<BrandResponse>> response) {
                if (response.body() != null) {
                    progress.dismiss();
                    brandArrayList.clear();
                    modelArrayList.clear();
                    brandResponseArrayList.clear();
                    if(isSinhalaSearch){
                        brandArrayList.add("වෙළඳ නාමය");
                        modelArrayList.add("මාදිලිය");
                    }else {
                        brandArrayList.add("Brand");
                        modelArrayList.add("Model");
                    }
                    brandResponseArrayList.addAll(response.body());
                    for (int i =0; i < brandResponseArrayList.size(); i++){
                        brandArrayList.add(response.body().get(i).brand);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,brandArrayList);
                    brand_spinner.setAdapter(adapter); // this will set list of values to spinner
                    brand_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            fetchModelData(parent.getSelectedItem().toString());
                            brand_text = parent.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<BrandResponse>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void fetchModelData(final String id){
        final ProgressDialog progress = new ProgressDialog(SearchActivity.this);
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
        Call<ArrayList<ModelResponse>> call = api.getModelData("model", id);
        call.enqueue(new Callback<ArrayList<ModelResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ModelResponse>> call, @NonNull Response<ArrayList<ModelResponse>> response) {
                if (response.body() != null) {
                    progress.dismiss();
                    modelResponseArrayList.clear();
                    modelResponseArrayList.addAll(response.body());
                    for (int i = 0; i < modelResponseArrayList.size(); i++){
                        modelArrayList.add(modelResponseArrayList.get(i).model);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item,modelArrayList);
                    model_spinner.setAdapter(adapter); // this will set list of values to spinner
                    model_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            model_text = parent.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<ModelResponse>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    @Override
    public void changeLanguage(Boolean isSinhala) {
        if (isSinhala){
            sign.setImageResource(R.drawable.si);
            isSinhalaLanguage = true;
            isSinhalaSearch = true;
            fetchDistrictData();
            fetchBrandData();
        }else {
            sign.setImageResource(R.drawable.en);
            isSinhalaLanguage = false;
            isSinhalaSearch = false;
            fetchDistrictData();
            fetchBrandData();
        }
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
            if (getIntent().getStringExtra("activity").equalsIgnoreCase("menu")){
                Intent intent = new Intent(SearchActivity.this, MenuActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("vehicleData")){
                Intent intent = new Intent(SearchActivity.this, VehicleDataActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("myFavorite")){
                Intent intent = new Intent(SearchActivity.this, MyFavoriteActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("myAds")){
                Intent intent = new Intent(SearchActivity.this, MyAdsActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("insertAdvertisement")){
                Intent intent = new Intent(SearchActivity.this, InsertAdvertisementActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("displayAdvertisement")){
                Intent intent = new Intent(SearchActivity.this, DisplayAdvertisementActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                intent.putExtra("id", getIntent().getIntExtra("id",0));
                startActivity(intent);
                finish();
            }
        }
        if (id == R.id.power) {
            logout();
            return true;
        }
        if (id == R.id.favorite_menu){
            Intent intent = new Intent(SearchActivity.this, MyFavoriteActivity.class);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            intent.putExtra("activity", "search");
            startActivity(intent);
            finish();
        }
        if (id == R.id.user_menu){
            Intent intent = new Intent(SearchActivity.this, MyAdsActivity.class);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            intent.putExtra("activity", "search");
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        System.exit(0);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getStringExtra("activity").equalsIgnoreCase("menu")){
            Intent intent = new Intent(SearchActivity.this, MenuActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("vehicleData")){
            Intent intent = new Intent(SearchActivity.this, VehicleDataActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            intent.putExtra("type", getIntent().getStringExtra("type"));
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("myFavorite")){
            Intent intent = new Intent(SearchActivity.this, MyFavoriteActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("myAds")){
            Intent intent = new Intent(SearchActivity.this, MyAdsActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("insertAdvertisement")){
            Intent intent = new Intent(SearchActivity.this, InsertAdvertisementActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("displayAdvertisement")){
            Intent intent = new Intent(SearchActivity.this, DisplayAdvertisementActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            intent.putExtra("id", getIntent().getIntExtra("id",0));
            startActivity(intent);
            finish();
        }
    }
}
