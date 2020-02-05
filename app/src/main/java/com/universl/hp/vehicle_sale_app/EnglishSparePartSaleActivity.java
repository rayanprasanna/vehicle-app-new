package com.universl.hp.vehicle_sale_app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universl.hp.vehicle_sale_app.adapter.EnglishSearchServiceAdapter;
import com.universl.hp.vehicle_sale_app.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.response.SparePartServiceResponse;
import com.universl.hp.vehicle_sale_app.utils.AppController;
import com.universl.hp.vehicle_sale_app.utils.Constants;

/*import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;*/

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnglishSparePartSaleActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String TAG = "";
    private List<SparePartServiceResponse> sparePartServiceResponses;
    public List<SparePartServiceResponse> getSparePartServiceResponses;
    private EnglishSearchServiceAdapter searchAdapter;
    private ProgressDialog progress;
    List<FavoriteResponse> favoriteResponses;
    List<String> image_path,favorite_image_path;
    String androidId;
    private SearchView brand;
    private ListView listView;
    private AdView adView;
    ArrayList<String> service_list;

    @SuppressLint("HardwareIds")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_spare_part_sale);

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        brand = findViewById(R.id.spare_part_service);
        listView = findViewById(R.id.listView);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSparePartServiceResponses = new ArrayList<>();
        progress = new ProgressDialog(EnglishSparePartSaleActivity.this);
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
        JsonArrayRequest request = new JsonArrayRequest(Constants.BASE_URL_get_service,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<SparePartServiceResponse> items = new Gson().fromJson(response.toString(), new TypeToken<List<SparePartServiceResponse>>() {
                        }.getType());

                        // adding contacts to contacts list
                        //sparePartServiceResponses.clear();
                        ArrayList<String> category = new ArrayList<>();
                        ArrayList<String> remove_duplicate_category;
                        service_list = new ArrayList<>();
                        for (int i = 0; i < items.size();i++){
                            category.add(items.get(i).category);
                        }
                        remove_duplicate_category = removeDuplicates(category);
                        service_list.addAll(remove_duplicate_category);
                        service_list.size();
                        searchAdapter = new EnglishSearchServiceAdapter(EnglishSparePartSaleActivity.this,remove_duplicate_category);
                        listView.setAdapter(searchAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                brand.setQuery(service_list.get(position),true);
                            }
                        });
                        brand.setOnQueryTextListener(EnglishSparePartSaleActivity.this);
                        // refreshing recycler view
                        searchAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().getRequestQueue().getCache().remove(Constants.BASE_URL_get_service);
        AppController.getInstance().addToRequestQueue(request);
    }
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

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
    /*private void getDetails(){
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> implements SearchView.OnQueryTextListener{
            private ProgressDialog progress = new ProgressDialog(EnglishSparePartSaleActivity.this);
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
                    HttpGet httpget = new HttpGet(Constants.BASE_URL_get_service);
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
                sparePartServiceResponses = new ArrayList<>();
                image_path = new ArrayList<>();
                favorite_image_path = new ArrayList<>();
                sparePartServiceResponses = new Gson().fromJson(s,
                        new TypeToken<List<SparePartServiceResponse>>(){
                        }.getType());
                for (int i = 0; i < sparePartServiceResponses.size(); i++){
                    if (sparePartServiceResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getSparePartServiceResponses.add(sparePartServiceResponses.get(i));
                    }
                }
                getSparePartServiceResponses.size();
                class Network_2 extends AsyncTask<String,Void,String>{

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
                        searchAdapter = new EnglishSearchServiceAdapter(EnglishSparePartSaleActivity.this,getSparePartServiceResponses,favoriteResponses,image_path,favorite_image_path,androidId);
                        listView.setAdapter(searchAdapter);
                    }
                }
                new Network_2().execute();

                brand.setOnQueryTextListener(this);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        brand.setQuery(getSparePartServiceResponses.get(i).getService_type(),true);
                        Toast.makeText(EnglishSparePartSaleActivity.this,getSparePartServiceResponses.get(i).getService_type(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchAdapter.filter_service(s);
                return false;
            }
        }
        new Network().execute();
    }
    private void getCarDetails(){
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> implements SearchView.OnQueryTextListener {
            private ProgressDialog progress = new ProgressDialog(EnglishSparePartSaleActivity.this);
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
                    HttpGet httpget = new HttpGet(Constants.BASE_URL_get_service);
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
                sparePartServiceResponses = new ArrayList<>();
                sparePartServiceResponses = new Gson().fromJson(s,
                        new TypeToken<List<SparePartServiceResponse>>(){
                        }.getType());
                for (int i = 0; i < sparePartServiceResponses.size(); i++){
                    if (sparePartServiceResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        getSparePartServiceResponses.add(sparePartServiceResponses.get(i));
                    }
                }
                final SearchView brand;
                ListView listView;
                brand = findViewById(R.id.spare_part_service);
                listView = findViewById(R.id.listView);
                searchAdapter = new EnglishSearchServiceAdapter(EnglishSparePartSaleActivity.this,getSparePartServiceResponses);
                listView.setAdapter(searchAdapter);
                brand.setOnQueryTextListener(this);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        brand.setQuery(getSparePartServiceResponses.get(i).getService_type(),true);
                        Toast.makeText(EnglishSparePartSaleActivity.this,getSparePartServiceResponses.get(i).getService_type(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchAdapter.filter_service(s);
                return false;
            }
        }
        new Network().execute();
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent = new Intent(EnglishSparePartSaleActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EnglishSparePartSaleActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    //Ads
    private void initAds() {
        adView = this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchAdapter.filter_service(newText);
        return false;
    }
}
