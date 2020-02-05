package com.universl.hp.vehicle_sale_app.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hbb20.CountryCodePicker;
import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.main.help.Help;
import com.universl.hp.vehicle_sale_app.main.response.BrandResponse;
import com.universl.hp.vehicle_sale_app.main.response.CityResponse;
import com.universl.hp.vehicle_sale_app.main.response.DistrictResponse;
import com.universl.hp.vehicle_sale_app.main.response.ModelResponse;
import com.universl.hp.vehicle_sale_app.main.response.ServiceResponse;
import com.universl.hp.vehicle_sale_app.main.response.VehicleResponse;
import com.universl.hp.vehicle_sale_app.main.service.FavoriteAPIService;
import com.universl.hp.vehicle_sale_app.main.utils.Constants;
import com.universl.hp.vehicle_sale_app.main.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.universl.hp.vehicle_sale_app.main.utils.Constants.PICK_IMAGE_REQUEST_1;
import static com.universl.hp.vehicle_sale_app.main.utils.Constants.PICK_IMAGE_REQUEST_2;
import static com.universl.hp.vehicle_sale_app.main.utils.Constants.PICK_IMAGE_REQUEST_3;
import static com.universl.hp.vehicle_sale_app.main.utils.Constants.PICK_IMAGE_REQUEST_4;
import static com.universl.hp.vehicle_sale_app.main.utils.Constants.PICK_IMAGE_REQUEST_5;

public class InsertAdvertisementActivity extends AppCompatActivity implements Help {
    private static final String TAG = "";
    Boolean isSinhalaLanguage = false;
    ImageButton sign, search, add;
    CountryCodePicker countryCodePicker;
    RadioGroup radioGroup;
    Button post;
    LinearLayout vehicle_layout, service_layout;
    RadioButton con, new_radio,re_condition_radio,used_radio;
    CircleImageView carCircleImageView, vanCircleImageView, bikeCircleImageView, otherCircleImageView, serviceCircleImageView;
    ImageView image_1, image_2, image_3, image_4, image_5, close_1, close_2, close_3, close_4, close_5, arrow_car, arrow_van, arrow_bike, arrow_other, arrow_service;
    TextView title_text, ads_type, service_type_text, service_name_text, service_address_text, type_text, district_text, city_text, image_text, brand_text, model_text, trim_text, model_year_text, condition_text, mileage_text, body_text, transmission_text, fuel_text, engine_capacity_text, description_text, price_text, contact_text, phone_text;
    EditText title_edit_text, trim_edit_text, model_year_edit_text, mileage_edit_text, tele_edit_text, price_edit_text, description_edit_text, engine_capacity_edit_text, service_name_edit_text, service_address_edit_text;
    Spinner district_spinner, type_spinner, city_spinner, brand_spinner, model_spinner, body_type_spinner, transmission_spinner, fuel_spinner, service_spinner;
    ArrayList<String> districtArrayList = new ArrayList<>(), bodyArrayList = new ArrayList<>(), cityArrayList = new ArrayList<>(), brandArrayList = new ArrayList<>(), modelArrayList = new ArrayList<>(), type_v_ArrayList = new ArrayList<>(), type_b_ArrayList = new ArrayList<>(), type_o_ArrayList = new ArrayList<>(), transmissionArrayList = new ArrayList<>(), fuelArrayList = new ArrayList<>(), serviceArrayList = new ArrayList<>();
    ArrayList<Uri> uriArrayList = new ArrayList<>();
    AlertDialog.Builder alert;
    String district, city, brand, model, type, body, transmission, fuel,service, condition;
    String androidId;
    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_advertisement);sign = findViewById(R.id.sign); search = findViewById(R.id.search); add = findViewById(R.id.add);post = findViewById(R.id.post_button);type_text = findViewById(R.id.type_text);service_layout = findViewById(R.id.service_layout);vehicle_layout = findViewById(R.id.vehicle_layout);
        countryCodePicker = findViewById(R.id.country_code);radioGroup = findViewById(R.id.group);new_radio = findViewById(R.id.brand_new);re_condition_radio = findViewById(R.id.recondition);used_radio = findViewById(R.id.used);service_type_text = findViewById(R.id.service_type_text);
        carCircleImageView = findViewById(R.id.car);vanCircleImageView = findViewById(R.id.van);bikeCircleImageView = findViewById(R.id.motorBike);otherCircleImageView = findViewById(R.id.other);serviceCircleImageView = findViewById(R.id.service);service_name_text = findViewById(R.id.name_text);
        image_1 = findViewById(R.id.selected_image_1);image_2 = findViewById(R.id.selected_image_2);image_3 = findViewById(R.id.selected_image_3);image_4 = findViewById(R.id.selected_image_4);image_5 = findViewById(R.id.selected_image_5);service_name_edit_text = findViewById(R.id.name_edit_text);
        close_1 = findViewById(R.id.image_close_1);close_2 = findViewById(R.id.image_close_2);close_3 = findViewById(R.id.image_close_3);close_4 = findViewById(R.id.image_close_4);close_5 = findViewById(R.id.image_close_5);service_address_text = findViewById(R.id.address_text);service_address_edit_text = findViewById(R.id.address_edit_text);
        ads_type = findViewById(R.id.ad_type);district_text = findViewById(R.id.district_text);city_text = findViewById(R.id.city_text);image_text = findViewById(R.id.image_text);brand_text = findViewById(R.id.brand_text);model_text = findViewById(R.id.model_text);service_spinner = findViewById(R.id.service_type_spinner);
        trim_text = findViewById(R.id.trim_text);model_year_text = findViewById(R.id.model_year_text);condition_text = findViewById(R.id.condition_text);mileage_text = findViewById(R.id.mileage_text);body_text = findViewById(R.id.body_type_text);transmission_text = findViewById(R.id.transmission_text);fuel_text = findViewById(R.id.fuel_text);engine_capacity_text = findViewById(R.id.engine_capacity_text);
        description_text = findViewById(R.id.description_text);price_text = findViewById(R.id.price_text);contact_text = findViewById(R.id.contact_id);phone_text = findViewById(R.id.phone_number_text);arrow_car = findViewById(R.id.up_arrow_car);arrow_van = findViewById(R.id.up_arrow_van);arrow_bike = findViewById(R.id.up_arrow_motorBike);arrow_other = findViewById(R.id.up_arrow_other);arrow_service = findViewById(R.id.up_arrow_service);
        trim_edit_text = findViewById(R.id.trim_edit_text);model_year_edit_text = findViewById(R.id.year_text);mileage_edit_text = findViewById(R.id.mileage_edit_text);price_edit_text = findViewById(R.id.price_edit_text);description_edit_text = findViewById(R.id.description_edit_text);engine_capacity_edit_text = findViewById(R.id.engine_capacity_edit_text);tele_edit_text = findViewById(R.id.telephone_edit_text);
        title_edit_text = findViewById(R.id.title_edit_text);title_text = findViewById(R.id.title_text);district_spinner = findViewById(R.id.district_spinner);city_spinner = findViewById(R.id.city_spinner);brand_spinner = findViewById(R.id.brand_spinner);model_spinner = findViewById(R.id.model_spinner);body_type_spinner = findViewById(R.id.body_type_spinner);transmission_spinner = findViewById(R.id.transmission_spinner);fuel_spinner = findViewById(R.id.fuel_spinner);type_spinner = findViewById(R.id.type_spinner);
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#ffffff'>වාහන</font>"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                Intent intent = new Intent(InsertAdvertisementActivity.this, SearchActivity.class);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                intent.putExtra("activity", "insertAdvertisement");
                startActivity(intent);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(InsertAdvertisementActivity.this, InsertAdvertisementActivity.class);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();*/
            }
        });
        if (isSinhalaLanguage){
            type_v_ArrayList.clear();type_b_ArrayList.clear();type_o_ArrayList.clear();
            serviceArrayList.clear();bodyArrayList.clear();transmissionArrayList.clear();fuelArrayList.clear();
            type_v_ArrayList.add("වර්ගය");type_b_ArrayList.add("වර්ගය");type_o_ArrayList.add("වර්ගය");serviceArrayList.add("සේවා වර්ග");bodyArrayList.add("ඛඳ වර්ගය");transmissionArrayList.add("ගියර් පද්ධතිය වර්ගය");fuelArrayList.add("ඉන්ධන වර්ගය");
        }else {
            type_v_ArrayList.clear();type_b_ArrayList.clear();type_o_ArrayList.clear();
            serviceArrayList.clear();bodyArrayList.clear();transmissionArrayList.clear();fuelArrayList.clear();type_v_ArrayList.add("Type");type_b_ArrayList.add("Type");type_o_ArrayList.add("Type");serviceArrayList.add("Service Type");bodyArrayList.add("Body Type");transmissionArrayList.add("Transmission Type");fuelArrayList.add("Fuel Type");
        }
        serviceArrayList.add("Automobile A/C");serviceArrayList.add("Batteries - Storage - Retail");serviceArrayList.add("Battery Charging Equipment");serviceArrayList.add("Bicycles - Parts & Supplies Wholesale & Manufacturers");serviceArrayList.add("Bolts & Nuts");serviceArrayList.add("Motor Vehicle Breakdown Services");serviceArrayList.add("Car Park Management Systems");serviceArrayList.add("Engines - Diesel - Fuel Injection Services & Parts");serviceArrayList.add("Engines - Rebuilding & Regrinding");serviceArrayList.add("Helmets & Protective Head Gears");serviceArrayList.add("Lorry Body Builders");serviceArrayList.add("Motor Spares & Machinery");serviceArrayList.add("Motor Vehicle Distributors");serviceArrayList.add("Motorcar Air Conditioning Equipment");serviceArrayList.add("Motorcar Parts & Supplies - Rebuilt");serviceArrayList.add("Motorcar Parts & Supplies - Wholesale");serviceArrayList.add("Motorcar Radio & Stereophonic");serviceArrayList.add("Motorcar Repairing & Services - Equipment & Supplies");serviceArrayList.add("Motorcar Vehicle Dealers - New Cars");serviceArrayList.add("Motorcar Vehicle Emissions Testing");serviceArrayList.add("Motorcycles & Motor Scooters");serviceArrayList.add("Service Station Equipment");serviceArrayList.add("Ship Builders & Repairs");serviceArrayList.add("Towing - Motor Vehicles");serviceArrayList.add("Tractor Equipment & Parts");serviceArrayList.add("Trailers - Equipment & Parts");serviceArrayList.add("Tyre");serviceArrayList.add("Tyre Manufacturing Materials");serviceArrayList.add("Tyres & Tyre Services");serviceArrayList.add("Windscreens");serviceArrayList.add("Batteries Dry Cells Retail");serviceArrayList.add("Bicycles & Spare Parts");serviceArrayList.add("Brake Down & Wrecker Services");serviceArrayList.add("Brake Shoes Bonding & Exchange");serviceArrayList.add("Car Parking Systems");serviceArrayList.add("Electric Motors - Dealers & Repairing");serviceArrayList.add("Engines - Outboard");serviceArrayList.add("Engines - Supplies Equipment & Parts");serviceArrayList.add("Garages/Breakdown Services");serviceArrayList.add("Motorcar / Vehicle Repairing Servising & Testing Equipment Suppliers");serviceArrayList.add("Motorcar Alarms & Security Systems");serviceArrayList.add("Motorcar Parts & Supplies - Retail");serviceArrayList.add("Motorcar Polishing & Waxing");serviceArrayList.add("Motorcar Renting & Leasing");serviceArrayList.add("Motorcar Rust Proofing Service");serviceArrayList.add("Motorcar Vehicle Dealers - Re-Conditioned");serviceArrayList.add("Motorcycles & Motor Scooters - Wholesale & Manufacturers");serviceArrayList.add("Service Stations");serviceArrayList.add("Tractor Dealers");serviceArrayList.add("Traffic Signal Systems");serviceArrayList.add("Truck Body - Manufacturers");serviceArrayList.add("Tyre Changing & Service Equipment");serviceArrayList.add("Tyre Manufacturers, Distributors & Dealers");serviceArrayList.add("Upholsterers");serviceArrayList.add("Glass - Motorcar, Plate,Window etc");serviceArrayList.add("Batteries - Storage -Wholesale & Manufacturers");serviceArrayList.add("Bearings");
        final ArrayAdapter<String> service_adapter = new ArrayAdapter<>(this,R.layout.spinner_item, serviceArrayList);

        bodyArrayList.add("Saloon");bodyArrayList.add("Hatchback");bodyArrayList.add("Station wagon");bodyArrayList.add("Convertible");bodyArrayList.add("Couple/Sport");bodyArrayList.add("SUV / 4X4");bodyArrayList.add("MPV");
        transmissionArrayList.add("Manual");transmissionArrayList.add("Automatic");transmissionArrayList.add("Triptronic");transmissionArrayList.add("Other Transmission");
        fuelArrayList.add("ඩීසල්");fuelArrayList.add("පෙට්රල්");fuelArrayList.add("CNG ඉන්ධන");fuelArrayList.add("වෙනත් ඉන්ධන වර්ග");
        final ArrayAdapter<String> body_adapter = new ArrayAdapter<>(this,R.layout.spinner_item,bodyArrayList);
        final ArrayAdapter<String> transmission_adapter = new ArrayAdapter<>(this,R.layout.spinner_item,transmissionArrayList);
        final ArrayAdapter<String> fuel_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,fuelArrayList);

        type_v_ArrayList.add("Van");type_v_ArrayList.add("Bus");type_v_ArrayList.add("Lorry");
        final ArrayAdapter<String> type_v_adapter = new ArrayAdapter<>(this,R.layout.spinner_item,type_v_ArrayList);

        type_b_ArrayList.add("Scooter");type_b_ArrayList.add("Motor Bike");
        final ArrayAdapter<String> type_b_adapter = new ArrayAdapter<>(this,R.layout.spinner_item,type_b_ArrayList);

        type_o_ArrayList.add("Heavy vehicles");type_o_ArrayList.add("1. Bulldozer");type_o_ArrayList.add("2. Crane");type_o_ArrayList.add("3. Digger");type_o_ArrayList.add("4. Excavation");
        type_o_ArrayList.add("5. Loader / lifter");type_o_ArrayList.add("6. Roller");type_o_ArrayList.add("7. Tractor");type_o_ArrayList.add("Light vehicles");type_o_ArrayList.add("1. Three Wheelers");
        type_o_ArrayList.add("2. Push Cycles");type_o_ArrayList.add("3. Other Vehicle");
        final ArrayAdapter<String> type_o_adapter = new ArrayAdapter<>(this,R.layout.spinner_item,type_o_ArrayList);
        image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (askForPermission_1())
                    showChooser_1();
            }
        });
        image_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (askForPermission_2())
                    showChooser_2();
            }
        });
        image_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (askForPermission_3())
                    showChooser_3();
            }
        });
        image_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (askForPermission_4())
                    showChooser_4();
            }
        });
        image_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (askForPermission_5())
                    showChooser_5();
            }
        });
        int selectedId = radioGroup.getCheckedRadioButtonId();
        //final int condition_sp = getIntent().getIntExtra("condition",0);
        // find the radiobutton by returned id
        con = findViewById(selectedId);
        carCircleImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                fetchBrandData("car");
                carCircleImageView.setBorderColor(Color.parseColor("#1565c0"));
                vanCircleImageView.setBorderColor(Color.parseColor("#000000"));
                bikeCircleImageView.setBorderColor(Color.parseColor("#000000"));
                otherCircleImageView.setBorderColor(Color.parseColor("#000000"));
                serviceCircleImageView.setBorderColor(Color.parseColor("#000000"));
                arrow_car.setVisibility(View.VISIBLE);arrow_van.setVisibility(View.GONE);arrow_bike.setVisibility(View.GONE);arrow_other.setVisibility(View.GONE);arrow_service.setVisibility(View.GONE);
                if (getIntent().getBooleanExtra("isSinhala",false)){
                    title_text.setVisibility(View.GONE);title_edit_text.setVisibility(View.GONE);service_layout.setVisibility(View.GONE);vehicle_layout.setVisibility(View.VISIBLE);brand_text.setVisibility(View.VISIBLE);brand_spinner.setVisibility(View.VISIBLE);type_text.setVisibility(View.GONE);type_spinner.setVisibility(View.GONE);model_text.setVisibility(View.VISIBLE);model_spinner.setVisibility(View.VISIBLE);
                    trim_text.setVisibility(View.VISIBLE);trim_edit_text.setVisibility(View.VISIBLE);fuel_text.setVisibility(View.VISIBLE);fuel_spinner.setVisibility(View.VISIBLE);transmission_text.setVisibility(View.VISIBLE);transmission_spinner.setVisibility(View.VISIBLE);body_text.setVisibility(View.VISIBLE);body_type_spinner.setVisibility(View.VISIBLE);
                    fuel_text.setText("ඉන්ධන වර්ගය");new_radio.setText("අලුත්");re_condition_radio.setText("පාවිච්චි කරන ලද");used_radio.setText("පිළිසැකසුම්");ads_type.setText("කාර් රථ විකිණිමට");brand_text.setText("වෙළඳ නාමය");model_text.setText("මාදිලිය");trim_text.setText("උප මාදිලි නාමය / අංකය");model_year_text.setText("නිෂ්පාදිත වර්ෂය");condition_text.setText("අන්දම");mileage_text.setText("ධාවනය කර ඇති දුර");body_text.setText("ඛඳ වර්ගය");transmission_text.setText("ගියර් පද්ධතිය");engine_capacity_text.setText("එන්ජිමේ ධාරිතාව (cc)");description_text.setText("විස්තර");price_text.setText("මිල");contact_text.setText("ඇමතුම් විස්තර");phone_text.setText("දුරකථන අංකය");post.setText("ඔබේ දැන්වීම පළ කරන්න");
                    body_type_spinner.setAdapter(body_adapter);
                    transmission_spinner.setAdapter(transmission_adapter);
                    fuel_spinner.setAdapter(fuel_adapter);
                }else {
                    title_text.setVisibility(View.GONE);title_edit_text.setVisibility(View.GONE);service_layout.setVisibility(View.GONE);vehicle_layout.setVisibility(View.VISIBLE);brand_text.setVisibility(View.VISIBLE);brand_spinner.setVisibility(View.VISIBLE);type_text.setVisibility(View.GONE);type_spinner.setVisibility(View.GONE);model_text.setVisibility(View.VISIBLE);model_spinner.setVisibility(View.VISIBLE);
                    trim_text.setVisibility(View.VISIBLE);trim_edit_text.setVisibility(View.VISIBLE);fuel_text.setVisibility(View.VISIBLE);fuel_spinner.setVisibility(View.VISIBLE);transmission_text.setVisibility(View.VISIBLE);transmission_spinner.setVisibility(View.VISIBLE);body_text.setVisibility(View.VISIBLE);body_type_spinner.setVisibility(View.VISIBLE);
                    new_radio.setText("Brand New");re_condition_radio.setText("Recondition");used_radio.setText("Used");ads_type.setText("Selling Cars");brand_text.setText("Brand");model_text.setText("Model");trim_text.setText("Trim / Edition");model_year_text.setText("Model Year");condition_text.setText("Condition");mileage_text.setText("Mileage");body_text.setText("Body Type");transmission_text.setText("Transmission");engine_capacity_text.setText("Engine Capacity (cc)");description_text.setText("Description");price_text.setText("Price");contact_text.setText("Contact Details");phone_text.setText("Phone Number");post.setText("Post AD");
                    body_type_spinner.setAdapter(body_adapter);
                    transmission_spinner.setAdapter(transmission_adapter);
                    fuel_spinner.setAdapter(fuel_adapter);
                }
                body_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        body = parent.getSelectedItem().toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                transmission_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        transmission = parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                fuel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        fuel = parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertVehicle(
                                brand+ " "+ model + " " +model_year_edit_text.getText().toString(),
                                district,
                                city,
                                "Car",
                                androidId,
                                brand,
                                model,
                                trim_edit_text.getText().toString(),
                                model_year_edit_text.getText().toString(),
                                mileage_edit_text.getText().toString(),
                                body,
                                transmission,
                                fuel,
                                engine_capacity_edit_text.getText().toString(),
                                description_edit_text.getText().toString(),
                                price_edit_text.getText().toString(),
                                "+"+countryCodePicker.getSelectedCountryCode() +tele_edit_text.getText().toString());
                    }
                });
            }
        });
        vanCircleImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                fetchBrandData("long_vehicle");
                carCircleImageView.setBorderColor(Color.parseColor("#000000"));
                vanCircleImageView.setBorderColor(Color.parseColor("#1565c0"));
                bikeCircleImageView.setBorderColor(Color.parseColor("#000000"));
                otherCircleImageView.setBorderColor(Color.parseColor("#000000"));
                serviceCircleImageView.setBorderColor(Color.parseColor("#000000"));
                arrow_car.setVisibility(View.GONE);arrow_van.setVisibility(View.VISIBLE);arrow_bike.setVisibility(View.GONE);arrow_other.setVisibility(View.GONE);arrow_service.setVisibility(View.GONE);
                if (isSinhalaLanguage){
                    title_text.setVisibility(View.GONE);title_edit_text.setVisibility(View.GONE);service_layout.setVisibility(View.GONE);vehicle_layout.setVisibility(View.VISIBLE);type_text.setVisibility(View.VISIBLE);type_spinner.setVisibility(View.VISIBLE);
                    brand_text.setVisibility(View.VISIBLE);brand_spinner.setVisibility(View.VISIBLE);model_text.setVisibility(View.GONE);model_spinner.setVisibility(View.GONE);trim_text.setVisibility(View.GONE);trim_edit_text.setVisibility(View.GONE);fuel_text.setVisibility(View.GONE);fuel_spinner.setVisibility(View.GONE);transmission_text.setVisibility(View.GONE);transmission_spinner.setVisibility(View.GONE);body_text.setVisibility(View.GONE);body_type_spinner.setVisibility(View.GONE);
                    new_radio.setText("අලුත්");re_condition_radio.setText("පාවිච්චි කරන ලද");used_radio.setText("පිළිසැකසුම්");ads_type.setText("වෑන් රථ , බස් රථ සහ ලොරි රථ විකිණිමට");brand_text.setText("වෙළඳ නාමය");type_text.setText("වර්ගය");model_year_text.setText("නිෂ්පාදිත වර්ෂය");condition_text.setText("අන්දම");mileage_text.setText("ධාවනය කර ඇති දුර");engine_capacity_text.setText("එන්ජිමේ ධාරිතාව (cc)");description_text.setText("විස්තර");price_text.setText("මිල");contact_text.setText("ඇමතුම් විස්තර");phone_text.setText("දුරකථන අංකය");post.setText("ඔබේ දැන්වීම පළ කරන්න");
                    type_spinner.setAdapter(type_v_adapter);
                }else {
                    title_text.setVisibility(View.GONE);title_edit_text.setVisibility(View.GONE);service_layout.setVisibility(View.GONE);vehicle_layout.setVisibility(View.VISIBLE);type_text.setVisibility(View.VISIBLE);type_spinner.setVisibility(View.VISIBLE);
                    brand_text.setVisibility(View.VISIBLE);brand_spinner.setVisibility(View.VISIBLE);model_text.setVisibility(View.GONE);model_spinner.setVisibility(View.GONE);trim_text.setVisibility(View.GONE);trim_edit_text.setVisibility(View.GONE);fuel_text.setVisibility(View.GONE);fuel_spinner.setVisibility(View.GONE);transmission_text.setVisibility(View.GONE);transmission_spinner.setVisibility(View.GONE);body_text.setVisibility(View.GONE);body_type_spinner.setVisibility(View.GONE);
                    new_radio.setText("Brand New");re_condition_radio.setText("Recondition");used_radio.setText("Used");ads_type.setText("Selling Vans, Buses, & Lorries");brand_text.setText("Brand");type_text.setText("Type");model_year_text.setText("Model Year");condition_text.setText("Condition");mileage_text.setText("Mileage");engine_capacity_text.setText("Engine Capacity (cc)");description_text.setText("Description");price_text.setText("Price");contact_text.setText("Contact Details");phone_text.setText("Phone Number");post.setText("Post AD");
                    type_spinner.setAdapter(type_v_adapter);
                }
                type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        type = parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertVehicle(
                                brand+ " "+ model,
                                district,
                                city,
                                type,
                                androidId,
                                brand,
                                "NULL",
                                "NULL",
                                model_year_edit_text.getText().toString(),
                                mileage_edit_text.getText().toString(),
                                "NULL",
                                "NULL",
                                "NULL",
                                engine_capacity_edit_text.getText().toString(),
                                description_edit_text.getText().toString(),
                                price_edit_text.getText().toString(),
                                "+"+countryCodePicker.getSelectedCountryCode() +tele_edit_text.getText().toString());
                    }
                });
            }
        });
        bikeCircleImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                fetchBrandData("bike");
                carCircleImageView.setBorderColor(Color.parseColor("#000000"));
                vanCircleImageView.setBorderColor(Color.parseColor("#000000"));
                bikeCircleImageView.setBorderColor(Color.parseColor("#1565c0"));
                otherCircleImageView.setBorderColor(Color.parseColor("#000000"));
                serviceCircleImageView.setBorderColor(Color.parseColor("#000000"));

                arrow_car.setVisibility(View.GONE);arrow_van.setVisibility(View.GONE);arrow_bike.setVisibility(View.VISIBLE);arrow_other.setVisibility(View.GONE);arrow_service.setVisibility(View.GONE);
                if (isSinhalaLanguage){
                    title_text.setVisibility(View.GONE);title_edit_text.setVisibility(View.GONE);service_layout.setVisibility(View.GONE);vehicle_layout.setVisibility(View.VISIBLE);type_text.setVisibility(View.VISIBLE);type_spinner.setVisibility(View.VISIBLE);model_text.setVisibility(View.VISIBLE);model_spinner.setVisibility(View.VISIBLE);
                    model_text.setVisibility(View.VISIBLE);model_spinner.setVisibility(View.VISIBLE);brand_text.setVisibility(View.VISIBLE);brand_spinner.setVisibility(View.VISIBLE);trim_text.setVisibility(View.GONE);trim_edit_text.setVisibility(View.GONE);fuel_text.setVisibility(View.GONE);fuel_spinner.setVisibility(View.GONE);transmission_text.setVisibility(View.GONE);transmission_spinner.setVisibility(View.GONE);body_text.setVisibility(View.GONE);body_type_spinner.setVisibility(View.GONE);
                    new_radio.setText("අලුත්");re_condition_radio.setText("පාවිච්චි කරන ලද");used_radio.setText("පිළිසැකසුම්");ads_type.setText("යතුරු පැදි සහ ස්කූටර් විකිණිමට");brand_text.setText("වෙළඳ නාමය");model_text.setText("මාදිලිය");type_text.setText("වර්ගය");model_year_text.setText("නිෂ්පාදිත වර්ෂය");condition_text.setText("අන්දම");mileage_text.setText("ධාවනය කර ඇති දුර");engine_capacity_text.setText("එන්ජිමේ ධාරිතාව (cc)");description_text.setText("විස්තර");price_text.setText("මිල");contact_text.setText("ඇමතුම් විස්තර");phone_text.setText("දුරකථන අංකය");post.setText("ඔබේ දැන්වීම පළ කරන්න");
                    type_spinner.setAdapter(type_b_adapter);
                }else {
                    title_text.setVisibility(View.GONE);title_edit_text.setVisibility(View.GONE);service_layout.setVisibility(View.GONE);vehicle_layout.setVisibility(View.VISIBLE);type_text.setVisibility(View.VISIBLE);type_spinner.setVisibility(View.VISIBLE);model_text.setVisibility(View.VISIBLE);model_spinner.setVisibility(View.VISIBLE);
                    model_text.setVisibility(View.VISIBLE);model_spinner.setVisibility(View.VISIBLE);brand_text.setVisibility(View.VISIBLE);brand_spinner.setVisibility(View.VISIBLE);trim_text.setVisibility(View.GONE);trim_edit_text.setVisibility(View.GONE);fuel_text.setVisibility(View.GONE);fuel_spinner.setVisibility(View.GONE);transmission_text.setVisibility(View.GONE);transmission_spinner.setVisibility(View.GONE);body_text.setVisibility(View.GONE);body_type_spinner.setVisibility(View.GONE);
                    new_radio.setText("Brand New");re_condition_radio.setText("Recondition");used_radio.setText("Used");ads_type.setText("Selling Motor Bicycles and Scooters");brand_text.setText("Brand");model_text.setText("Model");type_text.setText("Type");model_year_text.setText("Model Year");condition_text.setText("Condition");mileage_text.setText("Mileage");engine_capacity_text.setText("Engine Capacity (cc)");description_text.setText("Description");price_text.setText("Price");contact_text.setText("Contact Details");phone_text.setText("Phone Number");post.setText("Post AD");
                    type_spinner.setAdapter(type_b_adapter);
                }
                type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        type = parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertVehicle(
                                brand+ " "+ model_year_edit_text.getText().toString(),
                                district,
                                city,
                                type,
                                androidId,
                                brand,
                                model,
                                "NULL",
                                model_year_edit_text.getText().toString(),
                                mileage_edit_text.getText().toString(),
                                "NULL",
                                "NULL",
                                "NULL",
                                engine_capacity_edit_text.getText().toString(),
                                description_edit_text.getText().toString(),
                                price_edit_text.getText().toString(),
                                "+"+countryCodePicker.getSelectedCountryCode() +tele_edit_text.getText().toString());
                    }
                });
            }
        });
        otherCircleImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                fetchBrandData("long_vehicle");
                carCircleImageView.setBorderColor(Color.parseColor("#000000"));
                vanCircleImageView.setBorderColor(Color.parseColor("#000000"));
                bikeCircleImageView.setBorderColor(Color.parseColor("#000000"));
                otherCircleImageView.setBorderColor(Color.parseColor("#1565c0"));
                serviceCircleImageView.setBorderColor(Color.parseColor("#000000"));

                arrow_car.setVisibility(View.GONE);arrow_van.setVisibility(View.GONE);arrow_bike.setVisibility(View.GONE);arrow_other.setVisibility(View.VISIBLE);arrow_service.setVisibility(View.GONE);
                if (isSinhalaLanguage){
                    title_text.setVisibility(View.VISIBLE);title_edit_text.setVisibility(View.VISIBLE);service_layout.setVisibility(View.GONE);vehicle_layout.setVisibility(View.VISIBLE);type_text.setVisibility(View.VISIBLE);type_spinner.setVisibility(View.VISIBLE);
                    brand_text.setVisibility(View.GONE);brand_spinner.setVisibility(View.GONE);model_text.setVisibility(View.GONE);model_spinner.setVisibility(View.GONE);trim_text.setVisibility(View.GONE);trim_edit_text.setVisibility(View.GONE);fuel_text.setVisibility(View.GONE);fuel_spinner.setVisibility(View.GONE);transmission_text.setVisibility(View.GONE);transmission_spinner.setVisibility(View.GONE);body_text.setVisibility(View.GONE);body_type_spinner.setVisibility(View.GONE);
                    title_text.setText("මාතෘකාව");new_radio.setText("අලුත්");re_condition_radio.setText("පාවිච්චි කරන ලද");used_radio.setText("පිළිසැකසුම්");ads_type.setText("වෙනත් වාහන විකිණිමට");type_text.setText("වර්ගය");model_year_text.setText("නිෂ්පාදිත වර්ෂය");condition_text.setText("අන්දම");mileage_text.setText("ධාවනය කර ඇති දුර");engine_capacity_text.setText("එන්ජිමේ ධාරිතාව (cc)");description_text.setText("විස්තර");price_text.setText("මිල");contact_text.setText("ඇමතුම් විස්තර");phone_text.setText("දුරකථන අංකය");post.setText("ඔබේ දැන්වීම පළ කරන්න");
                    type_spinner.setAdapter(type_o_adapter);
                }else {
                    title_text.setVisibility(View.VISIBLE);title_edit_text.setVisibility(View.VISIBLE);service_layout.setVisibility(View.GONE);vehicle_layout.setVisibility(View.VISIBLE);type_text.setVisibility(View.VISIBLE);type_spinner.setVisibility(View.VISIBLE);
                    brand_text.setVisibility(View.GONE);brand_spinner.setVisibility(View.GONE);model_text.setVisibility(View.GONE);model_spinner.setVisibility(View.GONE);trim_text.setVisibility(View.GONE);trim_edit_text.setVisibility(View.GONE);fuel_text.setVisibility(View.GONE);fuel_spinner.setVisibility(View.GONE);transmission_text.setVisibility(View.GONE);transmission_spinner.setVisibility(View.GONE);body_text.setVisibility(View.GONE);body_type_spinner.setVisibility(View.GONE);
                    title_text.setText("Title");new_radio.setText("Brand New");re_condition_radio.setText("Recondition");used_radio.setText("Used");ads_type.setText("Selling Other Vehicle");type_text.setText("Type");model_year_text.setText("Model Year");condition_text.setText("Condition");mileage_text.setText("Mileage");engine_capacity_text.setText("Engine Capacity (cc)");description_text.setText("Description");price_text.setText("Price");contact_text.setText("Contact Details");phone_text.setText("Phone Number");post.setText("Post AD");
                    type_spinner.setAdapter(type_o_adapter);
                }
                type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        type = parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertVehicle(
                                title_edit_text.getText().toString(),
                                district,
                                city,
                                type,
                                androidId,
                                "NULL",
                                "NULL",
                                "NULL",
                                model_year_edit_text.getText().toString(),
                                mileage_edit_text.getText().toString(),
                                "NULL",
                                "NULL",
                                "NULL",
                                engine_capacity_edit_text.getText().toString(),
                                description_edit_text.getText().toString(),
                                price_edit_text.getText().toString(),
                                "+"+countryCodePicker.getSelectedCountryCode() +tele_edit_text.getText().toString());
                    }
                });
            }
        });
        serviceCircleImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                carCircleImageView.setBorderColor(Color.parseColor("#000000"));
                vanCircleImageView.setBorderColor(Color.parseColor("#000000"));
                bikeCircleImageView.setBorderColor(Color.parseColor("#000000"));
                otherCircleImageView.setBorderColor(Color.parseColor("#000000"));
                serviceCircleImageView.setBorderColor(Color.parseColor("#1565c0"));
                arrow_car.setVisibility(View.GONE);arrow_van.setVisibility(View.GONE);arrow_bike.setVisibility(View.GONE);arrow_other.setVisibility(View.GONE);arrow_service.setVisibility(View.VISIBLE);
                if (isSinhalaLanguage){
                    service_layout.setVisibility(View.VISIBLE);vehicle_layout.setVisibility(View.GONE);
                    ads_type.setText("අමතර කොටස් සහ සේවා");service_type_text.setText("සේවා වර්ගය");service_name_text.setText("සේවා ස්ථානයේ නම");service_address_text.setText("සේවා ස්ථානයේ ලිපිනය");contact_text.setText("ඇමතුම් විස්තර");phone_text.setText("දුරකථන අංකය");post.setText("ඔබේ දැන්වීම පළ කරන්න");
                    service_spinner.setAdapter(service_adapter);
                }else {
                    service_layout.setVisibility(View.VISIBLE);vehicle_layout.setVisibility(View.GONE);
                    ads_type.setText("Spare Part & Service");service_type_text.setText("Service Type");service_name_text.setText("Name");service_address_text.setText("Address");contact_text.setText("Contact Details");phone_text.setText("Phone Number");post.setText("Post AD");
                    service_spinner.setAdapter(service_adapter);
                }
                service_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        service = parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertServiceData(
                                service,
                                service_name_edit_text.getText().toString(),
                                service_address_edit_text.getText().toString(),
                                "+"+countryCodePicker.getSelectedCountryCode() +tele_edit_text.getText().toString());
                    }
                });
            }

        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.brand_new:
            case R.id.recondition:
            case R.id.used:
                if (checked)
                    condition = ((RadioButton) view).getText().toString();
                    break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void changeLanguage(Boolean isSinhala) {
        fetchDistrictData();
        fetchBrandData("car");
        if (isSinhala){
            sign.setImageResource(R.drawable.si);
            isSinhalaLanguage = true;
            bodyArrayList.add("ඛඳ වර්ගය");
            transmissionArrayList.add("ගියර් පද්ධතිය වර්ගය");
            fuelArrayList.add("ඉන්ධන වර්ගය");
            final ArrayAdapter<String> body_adapter = new ArrayAdapter<>(this,R.layout.spinner_item,bodyArrayList);
            final ArrayAdapter<String> transmission_adapter = new ArrayAdapter<>(this,R.layout.spinner_item,transmissionArrayList);
            final ArrayAdapter<String> fuel_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,fuelArrayList);
            body_type_spinner.setAdapter(body_adapter);
            transmission_spinner.setAdapter(transmission_adapter);
            fuel_spinner.setAdapter(fuel_adapter);
            body_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    body = parent.getSelectedItem().toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            transmission_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    transmission = parent.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            fuel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fuel = parent.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            new_radio.setText("අලුත්");
            re_condition_radio.setText("පාවිච්චි කරන ලද");
            used_radio.setText("පිළිසැකසුම්");
            district_text.setText("දිස්ත්\u200Dරික්කය");
            city_text.setText("නගරය");
            image_text.setText("අවම වශයෙන් ඡායා රූප එකක් පමණක් ඇතුලත් කරන්න. (උපරිම 5ක් පමණයි)");
            title_text.setVisibility(View.GONE);title_edit_text.setVisibility(View.GONE);type_text.setVisibility(View.GONE);type_spinner.setVisibility(View.GONE);service_layout.setVisibility(View.GONE);vehicle_layout.setVisibility(View.VISIBLE);
            ads_type.setText("කාර් රථ විකිණිමට");brand_text.setText("වෙළඳ නාමය");model_text.setText("මාදිලිය");trim_text.setText("උප මාදිලි නාමය / අංකය");model_year_text.setText("නිෂ්පාදිත වර්ෂය");condition_text.setText("අන්දම");mileage_text.setText("ධාවනය කර ඇති දුර");body_text.setText("ඛඳ වර්ගය");transmission_text.setText("ගියර් පද්ධතිය");fuel_text.setText("ඉන්ධන වර්ගය");engine_capacity_text.setText("එන්ජිමේ ධාරිතාව (cc)");description_text.setText("විස්තර");price_text.setText("මිල");contact_text.setText("ඇමතුම් විස්තර");phone_text.setText("දුරකථන අංකය");post.setText("ඔබේ දැන්වීම පළ කරන්න");
        }else {
            sign.setImageResource(R.drawable.en);
            isSinhalaLanguage = false;
            bodyArrayList.add("Body Type");
            transmissionArrayList.add("Transmission Type");
            fuelArrayList.add("Fuel Type");
            final ArrayAdapter<String> body_adapter = new ArrayAdapter<>(this,R.layout.spinner_item,bodyArrayList);
            final ArrayAdapter<String> transmission_adapter = new ArrayAdapter<>(this,R.layout.spinner_item,transmissionArrayList);
            final ArrayAdapter<String> fuel_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,fuelArrayList);
            body_type_spinner.setAdapter(body_adapter);
            transmission_spinner.setAdapter(transmission_adapter);
            fuel_spinner.setAdapter(fuel_adapter);
            body_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    body = parent.getSelectedItem().toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            transmission_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    transmission = parent.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            fuel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fuel = parent.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            new_radio.setText("Brand New");
            re_condition_radio.setText("Recondition");
            used_radio.setText("Used");
            district_text.setText("District");
            city_text.setText("City");
            image_text.setText("Enter at least one photo (no more than 5)");
            title_text.setVisibility(View.GONE);title_edit_text.setVisibility(View.GONE);type_text.setVisibility(View.GONE);type_spinner.setVisibility(View.GONE);service_layout.setVisibility(View.GONE);vehicle_layout.setVisibility(View.VISIBLE);
            ads_type.setText("Selling Cars");brand_text.setText("Brand");model_text.setText("Model");trim_text.setText("Trim / Edition");model_year_text.setText("Model Year");condition_text.setText("Condition");mileage_text.setText("Mileage");body_text.setText("Body Type");transmission_text.setText("Transmission");engine_capacity_text.setText("Engine Capacity (cc)");description_text.setText("Description");price_text.setText("Price");contact_text.setText("Contact Details");phone_text.setText("Phone Number");post.setText("Post AD");
        }
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertVehicle(
                        brand+ " "+ model + " " +model_year_edit_text.getText().toString(),
                        district,
                        city,
                        "Car",
                        androidId,
                        brand,
                        model,
                        trim_edit_text.getText().toString(),
                        model_year_edit_text.getText().toString(),
                        mileage_edit_text.getText().toString(),
                        body,
                        transmission,
                        fuel,
                        engine_capacity_edit_text.getText().toString(),
                        description_edit_text.getText().toString(),
                        price_edit_text.getText().toString(),
                        "+"+countryCodePicker.getSelectedCountryCode() +tele_edit_text.getText().toString());
            }
        });
    }
    private void fetchDistrictData(){
        final ProgressDialog progress = new ProgressDialog(InsertAdvertisementActivity.this);
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
                    if(getIntent().getBooleanExtra("isSinhala", false)){
                        districtArrayList.add("දිස්ත්\u200Dරික්කය");
                    }else {
                        districtArrayList.add("District");
                    }
                    for (int i =0; i < response.body().size(); i++){
                        if (getIntent().getBooleanExtra("isSinhala", false)){
                            districtArrayList.add(response.body().get(i).name_si);
                        }else {
                            districtArrayList.add(response.body().get(i).name_en);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,districtArrayList);
                    district_spinner.setAdapter(adapter); // this will set list of values to spinner
                    district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            district = parent.getSelectedItem().toString();
                            fetchCityData(position);
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
    private void fetchCityData(final Integer id){
        final ProgressDialog progress = new ProgressDialog(InsertAdvertisementActivity.this);
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
        Call<ArrayList<CityResponse>> call = api.getCityData("cities", id);
        call.enqueue(new Callback<ArrayList<CityResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CityResponse>> call, @NonNull Response<ArrayList<CityResponse>> response) {
                if (response.body() != null) {
                    progress.dismiss();
                    cityArrayList.clear();
                    if(getIntent().getBooleanExtra("isSinhala", false)){
                        cityArrayList.add("නගරය");
                    }else {
                        cityArrayList.add("City");
                    }
                    for (int i =0; i < response.body().size(); i++){
                        if (getIntent().getBooleanExtra("isSinhala", false)){
                            cityArrayList.add(response.body().get(i).name_si);
                        }else {
                            cityArrayList.add(response.body().get(i).name_en);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,cityArrayList);
                    city_spinner.setAdapter(adapter); // this will set list of values to spinner
                    city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            city = parent.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<CityResponse>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void fetchBrandData(final String id){
        final ProgressDialog progress = new ProgressDialog(InsertAdvertisementActivity.this);
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
        Call<ArrayList<BrandResponse>> call = api.getBrandData("brand", id);
        call.enqueue(new Callback<ArrayList<BrandResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<BrandResponse>> call, @NonNull Response<ArrayList<BrandResponse>> response) {
                if (response.body() != null) {
                    progress.dismiss();
                    brandArrayList.clear();
                    modelArrayList.clear();
                    if(getIntent().getBooleanExtra("isSinhala", false)){
                        brandArrayList.add("වෙළඳ නාමය");
                        modelArrayList.add("මාදිලිය");
                    }else {
                        brandArrayList.add("Brand");
                        modelArrayList.add("Model");
                    }
                    for (int i =0; i < response.body().size(); i++){
                        brandArrayList.add(response.body().get(i).brand);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,brandArrayList);
                    brand_spinner.setAdapter(adapter); // this will set list of values to spinner
                    brand_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            fetchModelData(parent.getSelectedItem().toString());
                            brand = parent.getSelectedItem().toString();
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
        final ProgressDialog progress = new ProgressDialog(InsertAdvertisementActivity.this);
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
                    for (int i = 0; i < response.body().size(); i++){
                        modelArrayList.add(response.body().get(i).model);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item,modelArrayList);
                    model_spinner.setAdapter(adapter); // this will set list of values to spinner
                    model_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            model = parent.getSelectedItem().toString();
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
    private void insertServiceData(final String category, final String name, final String address, final String contact){

        final ProgressDialog progress = new ProgressDialog(InsertAdvertisementActivity.this);
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

        Call<ServiceResponse> call = api.insertService("FALSE", category, name, address, contact);
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServiceResponse> call, @NonNull Response<ServiceResponse> response) {
                progress.dismiss();
                alert = new AlertDialog.Builder(InsertAdvertisementActivity.this);
                alert.setIcon(R.mipmap.ic_icon);
                alert.setTitle(getString(R.string.app_name));
                alert.setMessage("Delivering a notice is a success. Once the Admin Board approved, your ad will be published in the app. Thank you !.");
                alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.create().show();
            }

            @Override
            public void onFailure(@NonNull Call<ServiceResponse> call, @NonNull Throwable t) {

            }
        });
    }
    private void insertVehicle(final String title, final String district, final String city, final String vehicle_type, final String user_id, final String brand, final String model, final String trim, final String model_year, final String mileage, final String body_type, final String transmission, final String fuel_type, final String engine_capacity, final String description, final String price, final String contact){
        final ProgressDialog progress = new ProgressDialog(InsertAdvertisementActivity.this);
        progress.setIcon(R.mipmap.ic_icon);
        progress.setTitle("වාහන");
        progress.setMessage("Data Uploading !");
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
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Creating object for our interface
        FavoriteAPIService api = retrofit.create(FavoriteAPIService.class);
        //Defining the method insertFavorite of our interface
        //Here a logging interceptor is created

        // create list of file parts (photo, video, ...)
        final List<MultipartBody.Part> parts = new ArrayList<>();

        if (uriArrayList != null) {
            // create part for file (photo, video, ...)
            for (int i = 0; i < uriArrayList.size(); i++) {
                parts.add(prepareFilePart("image"+i, uriArrayList.get(i)));
            }
        }
        RequestBody request_size = createPartFromString( "" + parts.size());
        RequestBody request_status = createPartFromString( "FALSE");
        RequestBody request_title = createPartFromString( title);
        RequestBody request_district = createPartFromString( district);
        RequestBody request_city = createPartFromString( city);
        RequestBody request_type = createPartFromString( vehicle_type);
        RequestBody request_userId = createPartFromString(user_id);
        RequestBody request_brand = createPartFromString(brand);
        RequestBody request_model = createPartFromString(model);
        RequestBody request_trim = createPartFromString(trim);
        RequestBody request_model_year = createPartFromString(model_year);
        RequestBody request_conditions = createPartFromString(con.getText().toString());
        RequestBody request_mileage = createPartFromString(mileage);
        RequestBody request_body_type = createPartFromString(body_type);
        RequestBody request_transmission = createPartFromString(transmission);
        RequestBody request_fuel_type = createPartFromString(fuel_type);
        RequestBody request_engine_capacity = createPartFromString(engine_capacity);
        RequestBody request_descriptions = createPartFromString(description);
        RequestBody request_price = createPartFromString(price);
        RequestBody request_contact = createPartFromString(contact);
        RequestBody request_description = createPartFromString("www.vahanamobileapp.com");
        Call<VehicleResponse> call = api.uploadMultiple(request_description,request_size, request_status, request_title, request_district, request_city, request_type, request_userId, request_brand, request_model, request_trim, request_model_year, request_conditions, request_mileage, request_body_type, request_transmission, request_fuel_type, request_engine_capacity, request_descriptions, request_price, request_contact, parts);
        call.enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(@NonNull Call<VehicleResponse> call, @NonNull Response<VehicleResponse> response) {
                progress.dismiss();
                alert = new AlertDialog.Builder(InsertAdvertisementActivity.this);
                alert.setIcon(R.mipmap.ic_icon);
                alert.setTitle(getString(R.string.app_name));
                alert.setMessage("Delivering a notice is a success. Once the Admin Board approved, your ad will be published in the app. Thank you !.");
                alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(InsertAdvertisementActivity.this,MyAdsActivity.class);
                        intent.putExtra("android_id",getIntent().getStringExtra("android_id"));
                        startActivity(intent);
                        finish();
                    }
                });
                alert.create().show();
            }

            @Override
            public void onFailure(@NonNull Call<VehicleResponse> call, @NonNull Throwable t) {
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.err.println("Request : " + requestCode);
        if (requestCode == PICK_IMAGE_REQUEST_1 && resultCode == RESULT_OK && data != null && data.getData() != null) {// If the file selection was successful
            final Uri uri = data.getData();
            System.err.println("Uri : " + uri);

            Log.i(TAG, "Uri = " + Objects.requireNonNull(uri).toString());
            try {
                //getting bitmap object from uri
                // Get the file path from the URI
                final String path = FileUtils.getPath(this, uri);
                Log.d("Single File Selected", path);

                uriArrayList.add(uri);
                Glide.with(InsertAdvertisementActivity.this)
                        .asBitmap()
                        .load(uri)
                        .into(image_1);
            } catch (Exception e) {
                Log.e(TAG, "File select error", e);
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST_2 && resultCode == RESULT_OK && data != null && data.getData() != null) {// If the file selection was successful
            //do something with the image (save it to some directory or whatever you need to do with it here)
            final Uri uri = data.getData();
            Log.i(TAG, "Uri = " + uri.toString());
            try {
                //getting bitmap object from uri
                // Get the file path from the URI
                final String path = FileUtils.getPath(this, uri);
                Log.d("Single File Selected", path);

                uriArrayList.add(uri);
                Glide.with(InsertAdvertisementActivity.this)
                        .asBitmap()
                        .load(uri)
                        .into(image_2);
            } catch (Exception e) {
                Log.e(TAG, "File select error", e);
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST_3 && resultCode == RESULT_OK && data != null && data.getData() != null) {// If the file selection was successful
            //do something with the image (save it to some directory or whatever you need to do with it here)
            final Uri uri = data.getData();
            Log.i(TAG, "Uri = " + uri.toString());
            try {
                //getting bitmap object from uri
                // Get the file path from the URI
                final String path = FileUtils.getPath(this, uri);
                Log.d("Single File Selected", path);

                uriArrayList.add(uri);
                Glide.with(InsertAdvertisementActivity.this)
                        .asBitmap()
                        .load(uri)
                        .into(image_3);
            } catch (Exception e) {
                Log.e(TAG, "File select error", e);
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST_4 && resultCode == RESULT_OK && data != null && data.getData() != null) {// If the file selection was successful
            //do something with the image (save it to some directory or whatever you need to do with it here)
            final Uri uri = data.getData();
            Log.i(TAG, "Uri = " + uri.toString());
            try {
                //getting bitmap object from uri
                // Get the file path from the URI
                final String path = FileUtils.getPath(this, uri);
                Log.d("Single File Selected", path);

                uriArrayList.add(uri);
                Glide.with(InsertAdvertisementActivity.this)
                        .asBitmap()
                        .load(uri)
                        .into(image_4);
            } catch (Exception e) {
                Log.e(TAG, "File select error", e);
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST_5 && resultCode == RESULT_OK && data != null && data.getData() != null) {// If the file selection was successful
            //do something with the image (save it to some directory or whatever you need to do with it here)
            final Uri uri = data.getData();
            Log.i(TAG, "Uri = " + uri.toString());
            try {
                //getting bitmap object from uri
                // Get the file path from the URI
                final String path = FileUtils.getPath(this, uri);
                Log.d("Single File Selected", path);

                uriArrayList.add(uri);
                Glide.with(InsertAdvertisementActivity.this)
                        .asBitmap()
                        .load(uri)
                        .into(image_5);
            } catch (Exception e) {
                Log.e(TAG, "File select error", e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private boolean askForPermission_1() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            int hasCallPermission = ContextCompat.checkSelfPermission(InsertAdvertisementActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                // Ask for permission
                // need to request permission
                if (ActivityCompat.shouldShowRequestPermissionRationale(InsertAdvertisementActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // explain
                    showMessageOKCancel(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(InsertAdvertisementActivity.this,
                                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                            Constants.REQUEST_CODE_ASK_PERMISSIONS_1);
                                }
                            });
                    // if denied then working here
                } else {
                    // Request for permission
                    ActivityCompat.requestPermissions(InsertAdvertisementActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.REQUEST_CODE_ASK_PERMISSIONS_1);
                }

                return false;
            } else {
                // permission granted and calling function working
                return true;
            }
        } else {
            return true;
        }
    }
    private boolean askForPermission_2() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            int hasCallPermission = ContextCompat.checkSelfPermission(InsertAdvertisementActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                // Ask for permission
                // need to request permission
                if (ActivityCompat.shouldShowRequestPermissionRationale(InsertAdvertisementActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // explain
                    showMessageOKCancel(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(InsertAdvertisementActivity.this,
                                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                            Constants.REQUEST_CODE_ASK_PERMISSIONS_2);
                                }
                            });
                    // if denied then working here
                } else {
                    // Request for permission
                    ActivityCompat.requestPermissions(InsertAdvertisementActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.REQUEST_CODE_ASK_PERMISSIONS_2);
                }

                return false;
            } else {
                // permission granted and calling function working
                return true;
            }
        } else {
            return true;
        }
    }
    private boolean askForPermission_3() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            int hasCallPermission = ContextCompat.checkSelfPermission(InsertAdvertisementActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                // Ask for permission
                // need to request permission
                if (ActivityCompat.shouldShowRequestPermissionRationale(InsertAdvertisementActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // explain
                    showMessageOKCancel(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(InsertAdvertisementActivity.this,
                                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                            Constants.REQUEST_CODE_ASK_PERMISSIONS_3);
                                }
                            });
                    // if denied then working here
                } else {
                    // Request for permission
                    ActivityCompat.requestPermissions(InsertAdvertisementActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.REQUEST_CODE_ASK_PERMISSIONS_3);
                }

                return false;
            } else {
                // permission granted and calling function working
                return true;
            }
        } else {
            return true;
        }
    }
    private boolean askForPermission_4() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            int hasCallPermission = ContextCompat.checkSelfPermission(InsertAdvertisementActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                // Ask for permission
                // need to request permission
                if (ActivityCompat.shouldShowRequestPermissionRationale(InsertAdvertisementActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // explain
                    showMessageOKCancel(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(InsertAdvertisementActivity.this,
                                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                            Constants.REQUEST_CODE_ASK_PERMISSIONS_4);
                                }
                            });
                    // if denied then working here
                } else {
                    // Request for permission
                    ActivityCompat.requestPermissions(InsertAdvertisementActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.REQUEST_CODE_ASK_PERMISSIONS_4);
                }

                return false;
            } else {
                // permission granted and calling function working
                return true;
            }
        } else {
            return true;
        }
    }
    private boolean askForPermission_5() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            int hasCallPermission = ContextCompat.checkSelfPermission(InsertAdvertisementActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                // Ask for permission
                // need to request permission
                if (ActivityCompat.shouldShowRequestPermissionRationale(InsertAdvertisementActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // explain
                    showMessageOKCancel(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(InsertAdvertisementActivity.this,
                                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                            Constants.REQUEST_CODE_ASK_PERMISSIONS_5);
                                }
                            });
                    // if denied then working here
                } else {
                    // Request for permission
                    ActivityCompat.requestPermissions(InsertAdvertisementActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.REQUEST_CODE_ASK_PERMISSIONS_5);
                }

                return false;
            } else {
                // permission granted and calling function working
                return true;
            }
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE_ASK_PERMISSIONS_1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                showChooser_1();
            } else {
                // Permission Denied
                Toast.makeText(InsertAdvertisementActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode == Constants.REQUEST_CODE_ASK_PERMISSIONS_2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                showChooser_2();
            } else {
                // Permission Denied
                Toast.makeText(InsertAdvertisementActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode == Constants.REQUEST_CODE_ASK_PERMISSIONS_3) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                showChooser_3();
            } else {
                // Permission Denied
                Toast.makeText(InsertAdvertisementActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode == Constants.REQUEST_CODE_ASK_PERMISSIONS_4) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                showChooser_4();
            } else {
                // Permission Denied
                Toast.makeText(InsertAdvertisementActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode == Constants.REQUEST_CODE_ASK_PERMISSIONS_5) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                showChooser_5();
            } else {
                // Permission Denied
                Toast.makeText(InsertAdvertisementActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void showChooser_1() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, PICK_IMAGE_REQUEST_1);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }
    private void showChooser_2() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, PICK_IMAGE_REQUEST_2);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }
    private void showChooser_3() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, PICK_IMAGE_REQUEST_3);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }
    private void showChooser_4() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, PICK_IMAGE_REQUEST_4);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }
    private void showChooser_5() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, PICK_IMAGE_REQUEST_5);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }
    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(InsertAdvertisementActivity.this);
        final android.support.v7.app.AlertDialog dialog = builder.setMessage("You need to grant access to Read External Storage")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(InsertAdvertisementActivity.this, android.R.color.holo_blue_light));
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(InsertAdvertisementActivity.this, android.R.color.holo_red_light));
            }
        });

        dialog.show();

    }
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(Objects.requireNonNull(getContentResolver().getType(fileUri))),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
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
                Intent intent = new Intent(InsertAdvertisementActivity.this, MenuActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("vehicleData")){
                Intent intent = new Intent(InsertAdvertisementActivity.this, VehicleDataActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("myFavorite")){
                Intent intent = new Intent(InsertAdvertisementActivity.this, MyFavoriteActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("myAds")){
                Intent intent = new Intent(InsertAdvertisementActivity.this, MyAdsActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("search")){
                Intent intent = new Intent(InsertAdvertisementActivity.this, SearchActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("displayAdvertisement")){
                Intent intent = new Intent(InsertAdvertisementActivity.this, DisplayAdvertisementActivity.class);
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
            Intent intent = new Intent(InsertAdvertisementActivity.this, MyFavoriteActivity.class);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            intent.putExtra("activity", "insertAdvertisement");
            startActivity(intent);
            finish();
        }
        if (id == R.id.user_menu){
            Intent intent = new Intent(InsertAdvertisementActivity.this, MyAdsActivity.class);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            intent.putExtra("activity", "insertAdvertisement");
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
            Intent intent = new Intent(InsertAdvertisementActivity.this, MenuActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("vehicleData")){
            Intent intent = new Intent(InsertAdvertisementActivity.this, VehicleDataActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            intent.putExtra("type", getIntent().getStringExtra("type"));
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("myFavorite")){
            Intent intent = new Intent(InsertAdvertisementActivity.this, MyFavoriteActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("myAds")){
            Intent intent = new Intent(InsertAdvertisementActivity.this, MyAdsActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("search")){
            Intent intent = new Intent(InsertAdvertisementActivity.this, SearchActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("displayAdvertisement")){
            Intent intent = new Intent(InsertAdvertisementActivity.this, DisplayAdvertisementActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            intent.putExtra("id", getIntent().getIntExtra("id",0));
            startActivity(intent);
            finish();
        }
    }
}
