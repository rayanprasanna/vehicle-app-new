package com.universl.hp.vehicle_sale_app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universl.hp.vehicle_sale_app.adapter.EnglishSearchVehicleTypeAdapter;
import com.universl.hp.vehicle_sale_app.response.CarResponse;
import com.universl.hp.vehicle_sale_app.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.utils.Constants;

/*
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
*/
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnglishOtherVehicleSaleActivity extends AppCompatActivity {

    private List<CarResponse> carResponses;
    public List<CarResponse> getCarResponses;
    private EnglishSearchVehicleTypeAdapter searchAdapter;
    private ProgressDialog progress;
    List<FavoriteResponse> favoriteResponses;
    List<String> image_path,favorite_image_path;
    String androidId;
    private SearchView brand;
    private ListView listView;
    private AdView adView;

    @SuppressLint("HardwareIds")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_other_vehicle_sale);

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        brand = findViewById(R.id.other_vehicle);
        listView = findViewById(R.id.listView);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getCarResponses = new ArrayList<>();
        progress = new ProgressDialog(EnglishOtherVehicleSaleActivity.this);
        getDetails();
        initAds();
    }
    @Override
    public void onPause() {
        super.onPause();

        if ((progress != null) && progress.isShowing())
            progress.dismiss();
        progress = null;
    }
    private void getDetails(){
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> implements SearchView.OnQueryTextListener{
            private ProgressDialog progress = new ProgressDialog(EnglishOtherVehicleSaleActivity.this);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setTitle(getString(R.string.app_name));
                progress.setMessage("Data is downloading !");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();
            }
            @Override
            protected String doInBackground(String... strings) {
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet(Constants.BASE_URL_get_car);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    return client.execute(httpget, responseHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Data Download Successfully!";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                carResponses = new ArrayList<>();
                image_path = new ArrayList<>();
                favorite_image_path = new ArrayList<>();
                carResponses = new Gson().fromJson(s,
                        new TypeToken<List<CarResponse>>(){
                        }.getType());
                for (int i = 0; i < carResponses.size(); i++){
                    if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("Van") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("Bus") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("Lorry") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("1. Bulldozer") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("2. Crane") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("3. Digger") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("4. Excavation") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("5. Loader / lifter") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("6. Roller") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("7. Tractor") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("1. Three Wheelers") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("2. Push Cycles") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("3. Other Vehicle") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }
                }
                @SuppressLint("StaticFieldLeak")
                class Network_2 extends AsyncTask<String,Void,String>{

                    @SuppressLint("WrongThread")
                    @Override
                    protected String doInBackground(String... strings) {
                        try {
                            HttpClient client = new DefaultHttpClient();
                            HttpGet httpget = new HttpGet(Constants.BASE_URL_get_favorite);
                            ResponseHandler<String> responseHandler = new BasicResponseHandler();

                            return client.execute(httpget, responseHandler);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return "Data Download Successfully!";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if ((progress != null) && progress.isShowing()) {
                            progress.dismiss();
                        }
                        favoriteResponses = new ArrayList<>();
                        favoriteResponses = new Gson().fromJson(s,new TypeToken<List<FavoriteResponse>>(){
                        }.getType());
                        for (int i = 0; i < favoriteResponses.size(); i++){
                            image_path.add(favoriteResponses.get(i).getImage_path_1());
                            if (favoriteResponses.size() != 0){
                                if (androidId.equals(favoriteResponses.get(i).getUser_id())){
                                    favorite_image_path.add(favoriteResponses.get(i).getImage_path_1());
                                }
                            }
                        }
                        searchAdapter = new EnglishSearchVehicleTypeAdapter(EnglishOtherVehicleSaleActivity.this,getCarResponses,favoriteResponses,image_path,favorite_image_path,androidId);
                        listView.setAdapter(searchAdapter);
                    }
                }
                brand.setOnQueryTextListener(this);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        brand.setQuery(getCarResponses.get(i).getTitle(),true);
                        Toast.makeText(EnglishOtherVehicleSaleActivity.this,getCarResponses.get(i).getVehicle_type(),Toast.LENGTH_SHORT).show();
                    }
                });
                new Network_2().execute();
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchAdapter.filter_brand(s);
                return false;
            }
        }
        new Network().execute();
    }
    private void getCarDetails(){
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> implements SearchView.OnQueryTextListener {
            private ProgressDialog progress = new ProgressDialog(EnglishOtherVehicleSaleActivity.this);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setTitle(getString(R.string.app_name));
                progress.setMessage("Data is downloading !");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet(Constants.BASE_URL_get_car);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    return client.execute(httpget, responseHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Data Download Successfully!";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if ((progress != null) && progress.isShowing()) {
                    progress.dismiss();
                }
                carResponses = new ArrayList<>();
                carResponses = new Gson().fromJson(s,
                        new TypeToken<List<CarResponse>>(){
                        }.getType());
                for (int i = 0; i < carResponses.size(); i++){
                    if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("Van") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("Bus") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("Lorry") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("1. Bulldozer") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("2. Crane") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("3. Digger") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("4. Excavation") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("5. Loader / lifter") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("6. Roller") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("7. Tractor") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("1. Three Wheelers") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("2. Push Cycles") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("3. Other Vehicle") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("වෑන්") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("බස්") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("ලොරි") && carResponses.get(i).getVehicle_type().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("1. බුල්ඩෝසරය") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("2. දොඹකරය") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("3. කැණිය") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("4. හෑරීම් යන්ත්\u200D") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("5. ලෝඩර් / ලිෆ්ටරය") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("6. රෝලර්") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("7. ට්\u200Dරැක්ටරය") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("1. ත්\u200Dරිරෝද රථ") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("2. පාපැදි") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }else if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("3. වෙනත්") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }
                }
                final SearchView brand;
                ListView listView;
                brand = findViewById(R.id.other_vehicle);
                listView = findViewById(R.id.listView);
                //searchAdapter = new EnglishSearchVehicleTypeAdapter(EnglishOtherVehicleSaleActivity.this,getCarResponses);
                listView.setAdapter(searchAdapter);
                brand.setOnQueryTextListener(this);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        brand.setQuery(getCarResponses.get(i).getVehicle_type(),true);
                        Toast.makeText(EnglishOtherVehicleSaleActivity.this,getCarResponses.get(i).getVehicle_type(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchAdapter.filter_brand(s);
                return false;
            }
        }
        new Network().execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent = new Intent(EnglishOtherVehicleSaleActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EnglishOtherVehicleSaleActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    //Ads
    private void initAds() {
        adView = this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
