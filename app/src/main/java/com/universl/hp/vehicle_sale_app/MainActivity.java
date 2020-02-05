package com.universl.hp.vehicle_sale_app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universl.hp.vehicle_sale_app.adapter.EnglishSearchAdapter;
import com.universl.hp.vehicle_sale_app.adapter.EnglishSearchBrand;
import com.universl.hp.vehicle_sale_app.adapter.SpinnerAdapter;
import com.universl.hp.vehicle_sale_app.items.ItemData;
import com.universl.hp.vehicle_sale_app.items.VehicleAds;
import com.universl.hp.vehicle_sale_app.response.CarResponse;
import com.universl.hp.vehicle_sale_app.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.response.SparePartServiceResponse;
import com.universl.hp.vehicle_sale_app.utils.Constants;

/*import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;*/

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 123;
    private Context context;
    private Activity activity;
    private String getUser_name;
    private LinearLayout linearLayout;
    FirebaseAuth.AuthStateListener mAuthListener;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;
    private String TAG = "abc";
    private PopupWindow popupWindow;
    private String user_name,user_Uid,user_email;
    private ProgressDialog progress;
    private List<CarResponse> carResponses,getCarResponses;
    private List<SparePartServiceResponse> sparePartServiceResponses;
    private ArrayList<String> price,title,vehicle_type;
    private ArrayList<ItemData> ads_types;
    private Spinner ads;
    private ArrayList<VehicleAds> vehicleAds;
    private ImageView image;
    private Bitmap bitmap;
    private SpinnerAdapter spinnerAdapter;
    private SearchView brand;
    private EnglishSearchBrand searchBrand;
    private EnglishSearchAdapter searchAdapter;
    private WifiManager wifiManager;
    AlertDialog.Builder alert;
    List<FavoriteResponse> favoriteResponses;
    List<String> image_path,favorite_image_path;
    String androidId;
    private AdView adView;
    ListView listView;

    @Override
    public void onStart() {
        super.onStart();
        // mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onPause() {
        super.onPause();
        if ((progress != null) && progress.isShowing())
            progress.dismiss();
        progress = null;
        //mGoogleApiClient.stopAutoManage(this);
        // mGoogleApiClient.disconnect();
    }

    @SuppressLint("HardwareIds")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.main_linear_layout);
        context = getApplicationContext();
        activity = MainActivity.this;

        /*ImageButton hit_ads = findViewById(R.id.ads_id);
        ImageButton car_ads = findViewById(R.id.car_id);
        ImageButton van_ads = findViewById(R.id.van_id);
        ImageButton service_ads = findViewById(R.id.service_id);*/

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        brand = (SearchView) findViewById(R.id.search_view);
        ads = findViewById(R.id.ads_type);
        listView = findViewById(R.id.listView);
        FloatingTextButton floatingActionButton = findViewById(R.id.action_button);
        progress = new ProgressDialog(MainActivity.this);
        ads_types = new ArrayList<>();
        getCarResponses = new ArrayList<>();
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final Boolean[] isClickOk = {false};
        if (!isConnect()){
            //Toast.makeText(MainMenuActivity.this,"Wifi Or Network is not connect!",Toast.LENGTH_SHORT).show();
            alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("වාහන");
            alert.setMessage("You do not have an Internet connection");
            alert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.create().show();
        }else {
            getDetails();
        }
        //getAllDetails();
        ads_types.add(new ItemData("Type of Advertisements",R.drawable.advertise));
        ads_types.add(new ItemData("Brief Advertisement",R.drawable.v_ads));
        ads_types.add(new ItemData("Car Advertisement",R.drawable.v_car));
        ads_types.add(new ItemData("Other Vehicle Advertisement",R.drawable.v_other_sale));
        ads_types.add(new ItemData("Motor Bike Advertisement",R.drawable.motor_bike));
        ads_types.add(new ItemData("Spare Part and Service",R.drawable.v_service));

        spinnerAdapter = new SpinnerAdapter(this,R.layout.spinner_layout,R.id.ads_type,ads_types);
        ads.setAdapter(spinnerAdapter);
        ads.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = parent.getSelectedItemPosition();
                if (position == 1){
                    Intent intent = new Intent(MainActivity.this,EnglishLuhunduAdvertisementActivity.class);
                    startActivity(intent);
                    finish();
                }else if (position == 2){
                    Intent intent = new Intent(MainActivity.this,EnglishCarSaleActivity.class);
                    startActivity(intent);
                    finish();
                }else if (position == 3){
                    Intent intent = new Intent(MainActivity.this,EnglishOtherVehicleSaleActivity.class);
                    startActivity(intent);
                    finish();
                }else if (position == 4){
                    Intent intent = new Intent(MainActivity.this,EnglishMotorBikeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 5){
                    Intent intent = new Intent(MainActivity.this,EnglishSparePartSaleActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EnglishNewPostAdsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        initAds();
    }
    private void setMobileDataEnabled(Context context, boolean enabled) throws Exception{
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class conmanClass = null;
        try {
            conmanClass = Class.forName(conman.getClass().getName());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
        iConnectivityManagerField.setAccessible(true);
        final Object iConnectivityManager = iConnectivityManagerField.get(conman);
        final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);
        setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
    }
    private void getDetails(){
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> implements SearchView.OnQueryTextListener{
            private ProgressDialog progress = new ProgressDialog(MainActivity.this);
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
                    if (carResponses.get(i).getStatus().equalsIgnoreCase("true")){
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
                        searchBrand = new EnglishSearchBrand(MainActivity.this,getCarResponses,favoriteResponses,image_path,favorite_image_path,androidId);
                        listView.setAdapter(searchBrand);
                    }

                }
                new Network_2().execute();
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
                searchBrand.filter_brand(s);
                return false;
            }
        }
        new Network().execute();
    }
    private boolean isConnect(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.power) {
            logout();
            return true;
        }
        if (id == android.R.id.home){
            Intent intent = new Intent(MainActivity.this,LanguageMenuActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        System.exit(0);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MainActivity.this,LanguageMenuActivity.class);
        startActivity(intent);
        finish();
    }

    //Ads
    private void initAds() {
        adView = this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
