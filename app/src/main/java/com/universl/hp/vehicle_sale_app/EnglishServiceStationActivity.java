package com.universl.hp.vehicle_sale_app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universl.hp.vehicle_sale_app.adapter.ServiceAdapter;
import com.universl.hp.vehicle_sale_app.response.SparePartServiceResponse;
import com.universl.hp.vehicle_sale_app.utils.AppController;
import com.universl.hp.vehicle_sale_app.utils.Constants;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnglishServiceStationActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private static final String TAG = "";
    SearchView brand;
    ListView listView;
    ServiceAdapter serviceAdapter;
    List<SparePartServiceResponse> sparePartServiceResponses;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_service_station);

        ActionBar toolbar = getSupportActionBar();
        assert toolbar != null;
        toolbar.setTitle(getIntent().getStringExtra("s_name"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        brand = findViewById(R.id.brand);
        brand.setQueryHint("Name");
        listView = findViewById(R.id.listView);
        getDetails();
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
                        sparePartServiceResponses = new ArrayList<>();
                        for (int i = 0; i < items.size();i++){
                            if (items.get(i).category.equalsIgnoreCase(getIntent().getStringExtra("s_name")) && items.get(i).status.equalsIgnoreCase("True")){
                                sparePartServiceResponses.add(items.get(i));
                            }
                        }
                        serviceAdapter = new ServiceAdapter(EnglishServiceStationActivity.this,sparePartServiceResponses);
                        listView.setAdapter(serviceAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                brand.setQuery(sparePartServiceResponses.get(position).name,true);
                            }
                        });
                        brand.setOnQueryTextListener(EnglishServiceStationActivity.this);
                        // refreshing recycler view
                        serviceAdapter.notifyDataSetChanged();
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent = new Intent(EnglishServiceStationActivity.this, EnglishSparePartSaleActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EnglishServiceStationActivity.this, EnglishSparePartSaleActivity.class);
        startActivity(intent);
        this.finish();
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        serviceAdapter.filter(newText);
        return false;
    }
}
