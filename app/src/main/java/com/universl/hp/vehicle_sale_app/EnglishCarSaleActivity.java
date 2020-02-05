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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universl.hp.vehicle_sale_app.adapter.EnglishSearchAdapter;
import com.universl.hp.vehicle_sale_app.response.CarResponse;
import com.universl.hp.vehicle_sale_app.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.utils.Constants;

/*import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;*/

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnglishCarSaleActivity extends AppCompatActivity {

    private List<CarResponse> carResponses;
    public List<CarResponse> getCarResponses;
    private EnglishSearchAdapter searchAdapter;
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
        setContentView(R.layout.activity_english_car_sale);

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        brand = findViewById(R.id.brand);
        listView = findViewById(R.id.listView);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getCarResponses = new ArrayList<>();
        progress = new ProgressDialog(EnglishCarSaleActivity.this);
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
            private ProgressDialog progress = new ProgressDialog(EnglishCarSaleActivity.this);
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
                    if ((carResponses.get(i).getVehicle_type().equals("කාර්")|| carResponses.get(i).getVehicle_type().equalsIgnoreCase("car")) && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
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

                        brand = findViewById(R.id.brand);
                        listView = findViewById(R.id.listView);
                        searchAdapter = new EnglishSearchAdapter(EnglishCarSaleActivity.this,getCarResponses,favoriteResponses,image_path,favorite_image_path,androidId);
                        listView.setAdapter(searchAdapter);
                    }

                }
                new Network_2().execute();
                brand.setOnQueryTextListener(this);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        brand.setQuery(getCarResponses.get(i).getBrand(),true);
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
    private void getCarDetails(){
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> implements SearchView.OnQueryTextListener {

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
                    if (carResponses.get(i).getVehicle_type().equalsIgnoreCase("car") && carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getCarResponses.add(carResponses.get(i));
                    }
                }
                final SearchView brand;
                ListView listView;
                brand = findViewById(R.id.brand);
                listView = findViewById(R.id.listView);
                //searchAdapter = new EnglishSearchAdapter(EnglishCarSaleActivity.this,getCarResponses);
                listView.setAdapter(searchAdapter);
                brand.setOnQueryTextListener(this);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        brand.setQuery(getCarResponses.get(i).getTitle(),true);
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
            Intent intent = new Intent(EnglishCarSaleActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EnglishCarSaleActivity.this, MainActivity.class);
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
