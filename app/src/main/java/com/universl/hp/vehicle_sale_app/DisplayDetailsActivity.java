package com.universl.hp.vehicle_sale_app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universl.hp.vehicle_sale_app.adapter.DetailsAdapter;
import com.universl.hp.vehicle_sale_app.response.CarResponse;
import com.universl.hp.vehicle_sale_app.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.response.SparePartServiceResponse;
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

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class DisplayDetailsActivity extends AppCompatActivity{

    private static final int RC_SIGN_IN = 123;
    private Context context;
    private Activity activity;
    private RelativeLayout relativeLayout;
    FirebaseAuth.AuthStateListener mAuthListener;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;
    private String TAG = "abc";
    private PopupWindow popupWindow;
    List<CarResponse> carResponses,getCarResponses;
    List<SparePartServiceResponse> sparePartServiceResponses,getSparePartServiceResponses;
    List<FavoriteResponse> favoriteResponses,getFavoriteResponses;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    ArrayList<String> image_path = new ArrayList<>();
    DetailsAdapter detailsAdapter;
    ListView listView;
    FloatingTextButton call_button;
    String type,district_up,city_up,vehicle_type_up,service_type_up,brand_up,model_up,
            model_year_up,conditions_up,mileage_up,capacity_up,user_id_up,
            image_path_1_up,image_path_2_up,image_path_3_up,image_path_4_up,image_path_5_up,
            body_type_up,trim_up,transmission_up
            ,fuel_type_up,description_up,price_up,title_up,contact_up,User_ID;
    private ProgressDialog progress;
    Boolean isClickFavorite = true;
    Menu menu;
    private String user_email;
    private AdView adView;

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_display_details);
        type = getIntent().getStringExtra("activity");

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progress = new ProgressDialog(DisplayDetailsActivity.this);

        relativeLayout = findViewById(R.id.coordinate_layout);
        context = getApplicationContext();
        activity = DisplayDetailsActivity.this;

        //initialize facebook
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        switch (type) {
            case "service":
                getService();
                break;
            default:
                getCarDetails();
                break;
        }
        initAds();
    }

    @SuppressLint("InflateParams")

    @Override
    public void onPause() {
        super.onPause();

        if ((progress != null) && progress.isShowing())
            progress.dismiss();
        progress = null;
    }
    private void getCarDetails(){
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setTitle(getString(R.string.app_name));
                progress.setMessage("දත්ත බාගත වෙමින් පවති !");
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
                getCarResponses = new ArrayList<>();
                listView = findViewById(R.id.list_view);
                call_button = findViewById(R.id.call_button);
                carResponses = new Gson().fromJson(s,
                        new TypeToken<List<CarResponse>>(){
                        }.getType());
                String image = getIntent().getStringExtra("image_path_1");
                for (int i = 0; i < carResponses.size(); i++){
                    if (carResponses.get(i).getImage_path_1().equals(image)){
                        image_path.add(carResponses.get(i).getImage_path_1());
                        if (!carResponses.get(i).getImage_path_2().equals("")){
                            image_path.add(carResponses.get(i).getImage_path_2());
                        } if (!carResponses.get(i).getImage_path_3().equals("")){
                            image_path.add(carResponses.get(i).getImage_path_3());
                        }
                         if (!carResponses.get(i).getImage_path_4().equals("")){
                            image_path.add(carResponses.get(i).getImage_path_4());
                        } if (!carResponses.get(i).getImage_path_5().equals("")){
                            image_path.add(carResponses.get(i).getImage_path_5());
                        }

                        getCarResponses.add(carResponses.get(i));
                    }
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(DisplayDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
                RecyclerView mUploadList = findViewById(R.id.recycler_view);
                mUploadList.setLayoutManager(layoutManager);
                detailsAdapter = new DetailsAdapter(DisplayDetailsActivity.this,image_path);
                mUploadList.setAdapter(detailsAdapter);
                System.out.println("}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}" + getCarResponses.size());

                BaseAdapter baseAdapter = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return getCarResponses.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return getCarResponses.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @SuppressLint({"ViewHolder", "SetTextI18n", "InflateParams"})
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        convertView = LayoutInflater.from(DisplayDetailsActivity.this).inflate(R.layout.full_vehicle_details, null);
                        TextView title = convertView.findViewById(R.id.title);
                        TextView price = convertView.findViewById(R.id.price);
                        TextView location = convertView.findViewById(R.id.location);
                        TextView brand = convertView.findViewById(R.id.brand);
                        TextView condition = convertView.findViewById(R.id.condition);
                        TextView model = convertView.findViewById(R.id.model);
                        TextView year = convertView.findViewById(R.id.year);
                        TextView transmission = convertView.findViewById(R.id.transmission);
                        TextView transmission_text = convertView.findViewById(R.id.transmission_text);
                        TextView body_text = convertView.findViewById(R.id.body_text);
                        TextView fuel_text = convertView.findViewById(R.id.fuel_text);
                        TextView body = convertView.findViewById(R.id.body_type);
                        TextView fuel = convertView.findViewById(R.id.fuel);
                        TextView engine = convertView.findViewById(R.id.capacity);
                        TextView mileage = convertView.findViewById(R.id.mileage);
                        LinearLayout call = convertView.findViewById(R.id.call_layout);
                        TextView model_text = convertView.findViewById(R.id.model_text);
                        TextView description_text = convertView.findViewById(R.id.description_text);
                        final TextView description = convertView.findViewById(R.id.description);
                        TextView brand_text = convertView.findViewById(R.id.brand_text);
                        TextView engine_text = convertView.findViewById(R.id.capacity_text);

                        location.setText(getCarResponses.get(position).getDistrict() + "," + getCarResponses.get(position).getCity());
                        brand.setText(getCarResponses.get(position).getBrand());
                        year.setText(getCarResponses.get(position).getModel_year());
                        engine.setText(getCarResponses.get(position).getEngine_capacity() +"cc");
                        mileage.setText(getCarResponses.get(position).getMileage()+ "km");
                        condition.setText(getCarResponses.get(position).getCondition());
                        title.setText(getCarResponses.get(position).getBrand() + "\t" + getCarResponses.get(position).getModel() + "\t" + getCarResponses.get(position).getModel_year());
                        price.setText("රු " + getCarResponses.get(position).getPrice() + "/=");

                        if (getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("Van")
                                || getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("Bus")
                                || getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("Lorry")){
                            model.setVisibility(View.GONE);
                            model_text.setVisibility(View.GONE);
                            transmission_text.setVisibility(View.GONE);
                            transmission.setVisibility(View.GONE);
                            body.setVisibility(View.GONE);
                            body_text.setVisibility(View.GONE);
                            fuel.setVisibility(View.GONE);
                            fuel_text.setVisibility(View.GONE);
                            description_text.setPaintFlags(description_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            description_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    description.setVisibility(View.VISIBLE);
                                    description.setText(getCarResponses.get(position).getDescription());
                                }
                            });
                        }else if (getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("Car")){
                            model.setText(getCarResponses.get(position).getModel());
                            transmission.setText(getCarResponses.get(position).getTransmission());
                            body.setText(getCarResponses.get(position).getBody_type());
                            fuel.setText(getCarResponses.get(position).getFuel_type());
                            description_text.setPaintFlags(description_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            description_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    description.setVisibility(View.VISIBLE);
                                    description.setText(getCarResponses.get(position).getDescription());
                                }
                            });
                        }else if (getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("Scooter")
                                || getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("Motor Bike")){
                            model.setText(getCarResponses.get(position).getModel());
                            transmission_text.setVisibility(View.GONE);
                            transmission.setVisibility(View.GONE);
                            body.setVisibility(View.GONE);
                            body_text.setVisibility(View.GONE);
                            fuel.setVisibility(View.GONE);
                            fuel_text.setVisibility(View.GONE);
                            description_text.setPaintFlags(description_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            description_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    description.setVisibility(View.VISIBLE);
                                    description.setText(getCarResponses.get(position).getDescription());
                                }
                            });
                        }else if (getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("1. Bulldozer")
                                || getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("2. Crane")
                                || getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("3. Digger")
                                || getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("4. Excavation")
                                || getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("5. Loader / lifter")
                                || getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("6. Roller")
                                || getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("7. Tractor")
                                || getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("1. Three Wheelers")
                                || getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("2. Push Cycle")
                                || getCarResponses.get(position).getVehicle_type().equalsIgnoreCase("3. Other Vehicle")){

                            model.setVisibility(View.GONE);
                            model_text.setVisibility(View.GONE);
                            transmission_text.setVisibility(View.GONE);
                            transmission.setVisibility(View.GONE);
                            body.setVisibility(View.GONE);
                            body_text.setVisibility(View.GONE);
                            fuel.setVisibility(View.GONE);
                            fuel_text.setVisibility(View.GONE);
                            brand.setVisibility(View.GONE);
                            brand_text.setVisibility(View.GONE);
                            body_text.setVisibility(View.GONE);
                            engine.setVisibility(View.GONE);
                            engine_text.setVisibility(View.GONE);
                            title.setText(getCarResponses.get(position).getTitle());
                            description.setVisibility(View.GONE);
                            description_text.setPaintFlags(description_text.getPaintFlags() | Paint.HINTING_ON);
                            description_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    description.setVisibility(View.VISIBLE);
                                    description.setText(getCarResponses.get(position).getDescription());
                                }
                            });
                        }

                        district_up = getCarResponses.get(position).getDistrict();
                        city_up = getCarResponses.get(position).getCity();
                        vehicle_type_up = getCarResponses.get(position).getVehicle_type();
                        title_up = getCarResponses.get(position).getTitle();
                        model_year_up = getCarResponses.get(position).getModel_year();
                        transmission_up = getCarResponses.get(position).getTransmission();
                        body_type_up = getCarResponses.get(position).getBody_type();
                        fuel_type_up = getCarResponses.get(position).getFuel_type();
                        mileage_up = getCarResponses.get(position).getMileage();
                        image_path_1_up = getCarResponses.get(position).getImage_path_1();
                        image_path_2_up = getCarResponses.get(position).getImage_path_2();
                        image_path_3_up = getCarResponses.get(position).getImage_path_3();
                        image_path_4_up = getCarResponses.get(position).getImage_path_4();
                        image_path_5_up = getCarResponses.get(position).getImage_path_5();
                        brand_up = getCarResponses.get(position).getBrand();
                        model_up = getCarResponses.get(position).getModel();
                        trim_up = getCarResponses.get(position).getTrim();
                        capacity_up = getCarResponses.get(position).getEngine_capacity();
                        price_up = getCarResponses.get(position).getPrice();
                        user_id_up = getCarResponses.get(position).getUser_id();
                        description_up = getCarResponses.get(position).getDescription();
                        contact_up = getCarResponses.get(position).getContact();
                        conditions_up = getCarResponses.get(position).getCondition();
                        User_ID = user_id_up;

                        call_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+getCarResponses.get(position).getContact()));
                                startActivity(intent);
                            }
                        });
                        return convertView;
                    }
                };
                listView.setAdapter(baseAdapter);
            }
        }
        new Network().execute();
    }

    private void getService(){
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setTitle(getString(R.string.app_name));
                progress.setMessage("දත්ත බාගත වෙමින් පවති !");
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
                //progress.dismiss();
                sparePartServiceResponses = new ArrayList<>();
                getSparePartServiceResponses = new ArrayList<>();
                listView = findViewById(R.id.list_view);
                call_button = findViewById(R.id.call_button);
                sparePartServiceResponses = new Gson().fromJson(s,
                        new TypeToken<List<SparePartServiceResponse>>(){
                        }.getType());
                final String image = getIntent().getStringExtra("image_path_1");
                /*for (int i = 0; i < sparePartServiceResponses.size(); i++){
                    if (sparePartServiceResponses.get(i).getImage_path_1().equals(image)){
                        image_path.add(sparePartServiceResponses.get(i).getImage_path_1());
                        //image_path.add(carResponses.get(i).getImage_path_1());
                        if (!sparePartServiceResponses.get(i).getImage_path_2().equals("")){
                            image_path.add(sparePartServiceResponses.get(i).getImage_path_2());
                        } if (!sparePartServiceResponses.get(i).getImage_path_3().equals("")){
                            image_path.add(sparePartServiceResponses.get(i).getImage_path_3());
                        }
                        if (!sparePartServiceResponses.get(i).getImage_path_4().equals("")){
                            image_path.add(sparePartServiceResponses.get(i).getImage_path_4());
                        } if (!sparePartServiceResponses.get(i).getImage_path_5().equals("")){
                            image_path.add(sparePartServiceResponses.get(i).getImage_path_5());
                        }

                        getSparePartServiceResponses.add(sparePartServiceResponses.get(i));
                    }
                }*/

                LinearLayoutManager layoutManager = new LinearLayoutManager(DisplayDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
                RecyclerView mUploadList = findViewById(R.id.recycler_view);
                mUploadList.setLayoutManager(layoutManager);
                detailsAdapter = new DetailsAdapter(DisplayDetailsActivity.this,image_path);
                mUploadList.setAdapter(detailsAdapter);
                System.out.println("}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}" + image_path.size());

                BaseAdapter baseAdapter = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return getSparePartServiceResponses.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return getSparePartServiceResponses.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @SuppressLint({"ViewHolder", "SetTextI18n", "InflateParams"})
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        convertView = LayoutInflater.from(DisplayDetailsActivity.this).inflate(R.layout.full_vehicle_details, null);
                        TextView title = convertView.findViewById(R.id.title);
                        TextView price = convertView.findViewById(R.id.price);
                        LinearLayout call = convertView.findViewById(R.id.call_layout);
                        TextView location = convertView.findViewById(R.id.location);
                        TextView brand = convertView.findViewById(R.id.brand);
                        TextView brand_text = convertView.findViewById(R.id.brand_text);
                        TextView model_text = convertView.findViewById(R.id.model_text);
                        TextView model = convertView.findViewById(R.id.model);
                        TextView year_text = convertView.findViewById(R.id.year_text);
                        TextView year = convertView.findViewById(R.id.year);
                        TextView condition_text = convertView.findViewById(R.id.condition_text);
                        TextView condition = convertView.findViewById(R.id.condition);
                        TextView transmission_text = convertView.findViewById(R.id.transmission_text);
                        TextView transmission = convertView.findViewById(R.id.transmission);
                        TextView body = convertView.findViewById(R.id.body_text);
                        TextView body_text = convertView.findViewById(R.id.body_type);
                        TextView fuel_text = convertView.findViewById(R.id.fuel_text);
                        TextView fuel = convertView.findViewById(R.id.fuel);
                        TextView engine_text = convertView.findViewById(R.id.capacity_text);
                        TextView engine = convertView.findViewById(R.id.capacity);
                        TextView mileage_text = convertView.findViewById(R.id.mileage_text);
                        TextView mileage = convertView.findViewById(R.id.mileage);
                        TextView description = convertView.findViewById(R.id.description);

                        model_text.setVisibility(View.GONE);
                        model.setVisibility(View.GONE);
                        year_text.setVisibility(View.GONE);
                        year.setVisibility(View.GONE);
                        condition_text.setVisibility(View.GONE);
                        condition.setVisibility(View.GONE);
                        transmission_text.setVisibility(View.GONE);
                        transmission.setVisibility(View.GONE);
                        body_text.setVisibility(View.GONE);
                        body.setVisibility(View.GONE);
                        fuel_text.setVisibility(View.GONE);
                        fuel.setVisibility(View.GONE);
                        engine_text.setVisibility(View.GONE);
                        engine.setVisibility(View.GONE);
                        mileage_text.setVisibility(View.GONE);
                        mileage.setVisibility(View.GONE);
                        brand.setVisibility(View.GONE);
                        brand_text.setVisibility(View.GONE);

                        /*district_up = getSparePartServiceResponses.get(position).getDistrict();
                        city_up = getSparePartServiceResponses.get(position).getCity();
                        title_up = getSparePartServiceResponses.get(position).getTitle();
                        image_path_1_up = getSparePartServiceResponses.get(position).getImage_path_1();
                        image_path_2_up = getSparePartServiceResponses.get(position).getImage_path_2();
                        image_path_3_up = getSparePartServiceResponses.get(position).getImage_path_3();
                        image_path_4_up = getSparePartServiceResponses.get(position).getImage_path_4();
                        image_path_5_up = getSparePartServiceResponses.get(position).getImage_path_5();
                        price_up = getSparePartServiceResponses.get(position).getPrice();
                        user_id_up = getSparePartServiceResponses.get(position).getUser_id();
                        description_up = getSparePartServiceResponses.get(position).getDescription();
                        contact_up = getSparePartServiceResponses.get(position).getContact();
                        service_type_up = getSparePartServiceResponses.get(position).getService_type();
                        User_ID = user_id_up;

                        title.setText(getSparePartServiceResponses.get(position).getTitle());
                        description.setText(getSparePartServiceResponses.get(position).getDescription());
                        price.setText("රු " + getSparePartServiceResponses.get(position).getPrice() + "/=");
                        location.setText(getSparePartServiceResponses.get(position).getDistrict() + "," + getSparePartServiceResponses.get(position).getCity());

                        user_id_up = getSparePartServiceResponses.get(position).getUser_id();
                        User_ID = user_id_up;
                        call_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+getSparePartServiceResponses.get(position).getContact()));
                                startActivity(intent);
                            }
                        });*/
                        return convertView;
                    }
                };
                listView.setAdapter(baseAdapter);
            }
        }
        new Network().execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            switch (type) {
                case "service": {
                    Intent intent = new Intent(DisplayDetailsActivity.this, SparePartSaleActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case "other": {
                    Intent intent = new Intent(DisplayDetailsActivity.this, OtherVehicleSaleActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case "favorites":{
                    Intent intent = new Intent(DisplayDetailsActivity.this, DisplayFavoriteActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case "main":{
                    Intent intent = new Intent(DisplayDetailsActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                default: {
                    Intent intent = new Intent(DisplayDetailsActivity.this, CarSaleActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
            }
        }
        if (id == R.id.share){
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(DisplayDetailsActivity.this,"Share successful!",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(DisplayDetailsActivity.this,"Share cancel!",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(DisplayDetailsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setQuote("# වාහන")
                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.universl.hp.suwa_hamuwa&hl=en"))
                    .build();
            if (ShareDialog.canShow(ShareLinkContent.class)){
                shareDialog.show(linkContent);
            }
        }

        if (id == R.id.go_home){
            Intent intent = new Intent(DisplayDetailsActivity.this, MainMenuActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        switch (type) {
            case "service": {
                Intent intent = new Intent(DisplayDetailsActivity.this, SparePartSaleActivity.class);
                startActivity(intent);
                this.finish();
                break;
            }
            case "other": {
                Intent intent = new Intent(DisplayDetailsActivity.this, OtherVehicleSaleActivity.class);
                startActivity(intent);
                this.finish();
                break;
            }
            case "favorites":{
                Intent intent = new Intent(DisplayDetailsActivity.this, DisplayFavoriteActivity.class);
                startActivity(intent);
                this.finish();
                break;
            }
            case "main":{
                Intent intent = new Intent(DisplayDetailsActivity.this, MainMenuActivity.class);
                startActivity(intent);
                this.finish();
                break;
            }
            default: {
                Intent intent = new Intent(DisplayDetailsActivity.this, CarSaleActivity.class);
                startActivity(intent);
                this.finish();
                break;
            }
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_like_menu,menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }
    //Ads
    private void initAds() {
        adView = this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
