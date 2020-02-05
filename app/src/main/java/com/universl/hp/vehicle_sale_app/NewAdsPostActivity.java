package com.universl.hp.vehicle_sale_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;
import com.universl.hp.vehicle_sale_app.adapter.SpinnerAdapter;
import com.universl.hp.vehicle_sale_app.adapter.UploadListAdapter;
import com.universl.hp.vehicle_sale_app.helper.DocumentHelper;
import com.universl.hp.vehicle_sale_app.helper.NotificationHelper;
import com.universl.hp.vehicle_sale_app.items.ItemData;
import com.universl.hp.vehicle_sale_app.response.CarResponse;
import com.universl.hp.vehicle_sale_app.response.DistrictResponse;
import com.universl.hp.vehicle_sale_app.response.ImageResponse;
import com.universl.hp.vehicle_sale_app.service.ApiService;
import com.universl.hp.vehicle_sale_app.service.ImgurService;
import com.universl.hp.vehicle_sale_app.utils.Constants;
import com.universl.hp.vehicle_sale_app.utils.FileUtils;
import com.universl.hp.vehicle_sale_app.utils.InternetConnection;
import com.universl.hp.vehicle_sale_app.utils.Upload;

/*import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;*/

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.universl.hp.vehicle_sale_app.utils.Constants.PICK_IMAGE_REQUEST_1;
import static com.universl.hp.vehicle_sale_app.utils.Constants.PICK_IMAGE_REQUEST_2;
import static com.universl.hp.vehicle_sale_app.utils.Constants.PICK_IMAGE_REQUEST_3;
import static com.universl.hp.vehicle_sale_app.utils.Constants.PICK_IMAGE_REQUEST_4;
import static com.universl.hp.vehicle_sale_app.utils.Constants.PICK_IMAGE_REQUEST_5;
import static com.universl.hp.vehicle_sale_app.utils.Constants.READ_WRITE_EXTERNAL;

public class NewAdsPostActivity extends AppCompatActivity {

    private View parentView;
    CountryCodePicker countryCodePicker;
    ArrayList<String> image_urls;
    File chosenFile_1,chosenFile_2,chosenFile_3,chosenFile_4,chosenFile_5;
    private Uri returnUri_1,returnUri_2,returnUri_3,returnUri_4,returnUri_5;
    private ArrayList<Uri> arrayList;
    private LinearLayout your_details_layout,district_layout,city_layout,image_layout,
            brand_layout,model_layout,goods_layout,vehicle_type_1_layout,vehicle_type_2_layout
            ,title_layout,trim_layout,model_year_layout,condition_layout,mileage_layout,body_type_layout,
            transmission_layout,fuel_layout,capacity_layout,description_layout,price_layout,service_layout,image_button_layout;
    LinearLayout getService_layout,service_station_name_layout,getService_station_address_layout;
    private Spinner ad_title,location,city,brand,model,body,fuel,vehicle_type,motor_bike_type,transmission,goods_type,service_type,service_spinner;
    /*private RecyclerView images;*/
    /*ImageView select_image_1,select_image_2,select_image_3,select_image_4,select_image_5;
    Button upload_1,upload_2,upload_3,upload_4,upload_5,,select_upload_1,select_upload_2,select_upload_3,select_upload_4,select_upload_5;
    HorizontalScrollView scrollView;*/
    Button select_image;
    private Button enter_contact,save_button;
    private EditText title,trim,model_year,capacity,description,price,mileage,com_name,com_address,contact;
    private RadioGroup radioGroup;
    private RadioButton condition,new_radio,re_condition_radio,used_radio;
    private PopupWindow popupWindow;
    private String user_name,user_Uid,body_type_string,string;
    private ProgressDialog progress;
    private ArrayList<ItemData> bodies;
    private ArrayList<String> districts;
    private ArrayList<String> cities;
    private ArrayList<String> brand_1,brand_2,brand_3;
    private ArrayList<String> model_1, model_2;
    private ArrayList<String> vehicle_types;
    private ArrayList<String> fuels;
    private ArrayList<String> transmissions;
    private ArrayList<String> service_types;
    private ArrayList<String> services,motor_bikes,van_bus_lorry;
    RecyclerView images;
    private UploadListAdapter uploadListAdapter;
    private SpinnerAdapter body_adapter;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseRef;
    private List<String> image_Urls;
    private ArrayList<String> multiple_imageUrl,single_imageUrl;
    private int selected_district,selected_brand;
    private Context context;
    public Activity activity;
    private RelativeLayout relativeLayout;
    private ArrayList<DistrictResponse> districtResponses;

    private ArrayAdapter<String> ads_adapter,district_adapter,cities_adapter,model_2_adapter,brands_adapter,brand_2_adapter,brand_3_adapter,models_adapter
            ,transmission_adapter,fuel_adapter,vehicle_type_adapter,service_type_adapter,service_adapter,bike_adapter,van_adapter;
    private TextView ad_title_text,location_text,city_text,image_description,image_error,enter_tile,
            goods_type_text,brand_text,model_text,vehicle_type_text_1,vehicle_type_text_2,title_text,
            trim_text,model_year_text,condition_text,mileage_text,body_type_text,transmission_text,
            fuel_text,capacity_text,description_text,price_text,your_description_text,name_text,
            name,contact_text,service_text;
    int count = 0;
    String android_id;
    Boolean isMultiple = false;
    AlertDialog.Builder alert;
    private static final String TAG = MainActivity.class.getSimpleName();
    Boolean isParseBoolean = true,isVanBusLorryActivity = false,isMotorbikeScooter = false,isOtherVehicleActivity = false,isSpare_part_Service = false;
    Boolean isSelectCar = false,isSelectVanBusLorry = false,isSelectOtherVehicle = false,isSelectMotorBike = false,isSelectService = false;
    @SuppressLint("HardwareIds")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ads_post);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        relativeLayout = findViewById(R.id.new_ads_activity);
        context = getApplicationContext();
        activity = NewAdsPostActivity.this;

        image_urls = new ArrayList<>();

        parentView = findViewById(R.id.new_ads_activity);
        countryCodePicker = findViewById(R.id.country_code);
        service_spinner = findViewById(R.id.service_spinner);com_name = findViewById(R.id.service_station);com_address = findViewById(R.id.service_station_address);
        getService_layout = findViewById(R.id.service_type_layout);service_station_name_layout = findViewById(R.id.com_name_layout);
        getService_station_address_layout = findViewById(R.id.com_address_layout);
        your_details_layout = findViewById(R.id.your_details_layout);your_details_layout.setVisibility(View.GONE);
        district_layout = findViewById(R.id.district_layout);district_layout.setVisibility(View.GONE);
        city_layout = findViewById(R.id.city_layout);city_layout.setVisibility(View.GONE);
        image_layout = findViewById(R.id.select_image_layout);image_layout.setVisibility(View.GONE);
        image_button_layout = findViewById(R.id.select_image_button_layout);image_button_layout.setVisibility(View.GONE);
        brand_layout = findViewById(R.id.brand_layout);brand_layout.setVisibility(View.GONE);
        model_layout = findViewById(R.id.model_layout);model_layout.setVisibility(View.GONE);
        goods_layout = findViewById(R.id.goods_type_layout);goods_layout.setVisibility(View.GONE);
        vehicle_type_1_layout = findViewById(R.id.vehicle_type_1_layout);vehicle_type_1_layout.setVisibility(View.GONE);
        vehicle_type_2_layout = findViewById(R.id.vehicle_type_2_layout);vehicle_type_2_layout.setVisibility(View.GONE);
        title_layout = findViewById(R.id.title_layout);title_layout.setVisibility(View.GONE);
        trim_layout = findViewById(R.id.trim_layout);trim_layout.setVisibility(View.GONE);
        model_year_layout = findViewById(R.id.model_year_layout);model_year_layout.setVisibility(View.GONE);
        condition_layout = findViewById(R.id.condition_layout);condition_layout.setVisibility(View.GONE);
        mileage_layout = findViewById(R.id.mileage_layout);mileage_layout.setVisibility(View.GONE);
        body_type_layout = findViewById(R.id.body_type_layout);body_type_layout.setVisibility(View.GONE);
        transmission_layout = findViewById(R.id.transmission_layout);transmission_layout.setVisibility(View.GONE);
        fuel_layout = findViewById(R.id.fuel_layout);fuel_layout.setVisibility(View.GONE);
        capacity_layout = findViewById(R.id.capacity_layout);capacity_layout.setVisibility(View.GONE);
        description_layout = findViewById(R.id.description_layout);description_layout.setVisibility(View.GONE);
        price_layout = findViewById(R.id.price_layout);price_layout.setVisibility(View.GONE);
        service_layout = findViewById(R.id.service_layout);service_layout.setVisibility(View.GONE);
        //scrollView = findViewById(R.id.image_scroll);

        //upload_1 = findViewById(R.id.upload_btn_1);upload_2 = findViewById(R.id.upload_btn_2);upload_3 = findViewById(R.id.upload_btn_3);upload_4 = findViewById(R.id.upload_btn_4);upload_5 = findViewById(R.id.upload_btn_5);
        ad_title_text = findViewById(R.id.v_ad_title);
        location_text = findViewById(R.id.v_ad_district_text);location_text.setVisibility(View.GONE);
        city_text = findViewById(R.id.v_ad_city_text);city_text.setVisibility(View.GONE);
        image_description = findViewById(R.id.v_ad_images_description);image_description.setVisibility(View.GONE);
        image_error = findViewById(R.id.v_ad_image_error);image_error.setVisibility(View.GONE);
        enter_tile = findViewById(R.id.v_enter_title);enter_tile.setVisibility(View.GONE);
        goods_type_text = findViewById(R.id.v_ad_goods_type_text);goods_type_text.setVisibility(View.GONE);
        brand_text = findViewById(R.id.v_ad_brand_text);brand_text.setVisibility(View.GONE);
        model_text = findViewById(R.id.v_ad_model_name);model_text.setVisibility(View.GONE);
        vehicle_type_text_1 = findViewById(R.id.v_ad_vehicle_type_text_1);vehicle_type_text_1.setVisibility(View.GONE);
        vehicle_type_text_2 = findViewById(R.id.v_ad_vehicle_type_text2);vehicle_type_text_2.setVisibility(View.GONE);
        title_text = findViewById(R.id.v_ad_title_name);title_text.setVisibility(View.GONE);
        trim_text = findViewById(R.id.v_ad_trim_name);trim_text.setVisibility(View.GONE);
        model_year_text = findViewById(R.id.v_ad_model_year_text);model_year_text.setVisibility(View.GONE);
        condition_text = findViewById(R.id.v_ad_condition_text);condition_text.setVisibility(View.GONE);
        mileage_text = findViewById(R.id.v_ad_mileage_text);mileage_text.setVisibility(View.GONE);
        body_type_text = findViewById(R.id.v_ad_body_type_text);body_type_text.setVisibility(View.GONE);
        transmission_text = findViewById(R.id.v_ad_transmission_text);transmission_text.setVisibility(View.GONE);
        fuel_text = findViewById(R.id.v_ad_fuel_text);fuel_text.setVisibility(View.GONE);
        capacity_text = findViewById(R.id.v_ad_capacity_text);capacity_text.setVisibility(View.GONE);
        description_text = findViewById(R.id.v_ad_description_text);description_text.setVisibility(View.GONE);
        price_text = findViewById(R.id.v_ad_price_text);price_text.setVisibility(View.GONE);
        your_description_text = findViewById(R.id.v_ad_your_details);your_description_text.setVisibility(View.GONE);
        name_text = findViewById(R.id.v_ad_your_name);name_text.setVisibility(View.GONE);
        name = findViewById(R.id.v_ad_name_text);name.setVisibility(View.GONE);
        contact_text = findViewById(R.id.v_ad_your_telephone);contact_text.setVisibility(View.GONE);
        contact = findViewById(R.id.v_ad_telephone_num);contact.setVisibility(View.GONE);
        service_text = findViewById(R.id.v_ad_service_text);service_text.setVisibility(View.GONE);

        /*select_upload_1 = findViewById(R.id.select_upload_btn_1); select_image_1 = findViewById(R.id.selected_image_1);
        select_upload_2 = findViewById(R.id.select_upload_btn_2); select_image_2 = findViewById(R.id.selected_image_2);
        select_upload_3 = findViewById(R.id.select_upload_btn_3); select_image_3 = findViewById(R.id.selected_image_3);
        select_upload_4 = findViewById(R.id.select_upload_btn_4); select_image_4 = findViewById(R.id.selected_image_4);
        select_upload_5 = findViewById(R.id.select_upload_btn_5); select_image_5 = findViewById(R.id.selected_image_5);*/
        images = findViewById(R.id.v_ad_recycler_view);images.setVisibility(View.GONE);

        ad_title = findViewById(R.id.v_ad_types);
        location = findViewById(R.id.v_ad_district);location.setVisibility(View.GONE);
        city = findViewById(R.id.v_ad_city);city.setVisibility(View.GONE);
        brand = findViewById(R.id.v_ad_brand);brand.setVisibility(View.GONE);
        model = findViewById(R.id.v_ad_model);model.setVisibility(View.GONE);
        transmission = findViewById(R.id.v_ad_transmission);transmission.setVisibility(View.GONE);
        fuel = findViewById(R.id.v_ad_fuel);fuel.setVisibility(View.GONE);
        vehicle_type = findViewById(R.id.v_ad_vehicle_type_1);vehicle_type.setVisibility(View.GONE);
        body = findViewById(R.id.v_ad_body_type);body.setVisibility(View.GONE);
        goods_type = findViewById(R.id.v_ad_goods_type);goods_type.setVisibility(View.GONE);
        service_type = findViewById(R.id.v_ad_service);service_type.setVisibility(View.GONE);

        enter_contact = findViewById(R.id.v_ad_contact);enter_contact.setVisibility(View.GONE);
        select_image = findViewById(R.id.v_ad_image_btn);select_image.setVisibility(View.GONE);
        save_button = findViewById(R.id.v_ad_save);save_button.setVisibility(View.GONE);

        motor_bike_type = findViewById(R.id.v_ad_vehicle_type_2);motor_bike_type.setVisibility(View.GONE);
        title = findViewById(R.id.v_ad_vehicle_title);title.setVisibility(View.GONE);
        trim = findViewById(R.id.v_ad_trim);trim.setVisibility(View.GONE);
        model_year = findViewById(R.id.v_ad_model_year);model_year.setVisibility(View.GONE);
        capacity = findViewById(R.id.v_ad_capacity);capacity.setVisibility(View.GONE);
        description = findViewById(R.id.v_ad_description);description.setVisibility(View.GONE);
        price = findViewById(R.id.v_ad_price);price.setVisibility(View.GONE);
        mileage = findViewById(R.id.v_ad_mileage);mileage.setVisibility(View.GONE);

        radioGroup = findViewById(R.id.v_ad_radio_group);radioGroup.setVisibility(View.GONE);
        new_radio = findViewById(R.id.v_ad_new_radio);new_radio.setVisibility(View.GONE);
        used_radio = findViewById(R.id.v_ad_used_radio);used_radio.setVisibility(View.GONE);
        re_condition_radio = findViewById(R.id.v_ad_recondition_radio);re_condition_radio.setVisibility(View.GONE);

        mStorage = FirebaseStorage.getInstance().getReference("uploads_2");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads_2");

        districts = new ArrayList<>();
        cities = new ArrayList<>();
        model_1 = new ArrayList<>();
        model_2 = new ArrayList<>();
        brand_1 = new ArrayList<>();brand_2 = new ArrayList<>();brand_3 = new ArrayList<>();
        vehicle_types = new ArrayList<>();
        bodies = new ArrayList<>();
        fuels = new ArrayList<>();
        transmissions = new ArrayList<>();
        image_Urls = new ArrayList<>();
        multiple_imageUrl = new ArrayList<>();
        single_imageUrl = new ArrayList<>();
        vehicle_types = new ArrayList<>();
        service_types = new ArrayList<>();

        ArrayList<String> ads_types = new ArrayList<>();
        ads_types.add("දැන්විම් වර්ග");
        ads_types.add("කාර් රථ විකිණිමට");
        ads_types.add("වෑන් රථ,බස් රථ & ලොරි රථ විකිණිමට");
        ads_types.add("යතුරැ පැදි & ස්කූටර් විකිණිමට");
        ads_types.add("වෙනත් වාහන විකිණිමට");
        ads_types.add("අමතර කොටස් හා සේවා");

        ads_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,ads_types);
        ad_title.setAdapter(ads_adapter);

        districts.add("ස්ථානය තෝරන්න");
        cities.add("නගරය තෝරන්න");
        transmissions.add("ගියර පද්ධති");
        fuels.add("ඉන්ධන වර්ගය");
        brand_1.add("වෙළඳ නාමය");
        model_1.add("මාදිලිය");
        model_2.add("මාදිලිය");
        vehicle_types.add("වාහන වර්ගය");

        load_districts();

        services = new ArrayList<>();services.add("සේවා වර්ග");
        services.add("Automobile A/C");services.add("Batteries - Storage - Retail");services.add("Battery Charging Equipment");services.add("Bicycles - Parts & Supplies Wholesale & Manufacturers");services.add("Bolts & Nuts");services.add("Motor Vehicle Breakdown Services");services.add("Car Park Management Systems");services.add("Engines - Diesel - Fuel Injection Services & Parts");services.add("Engines - Rebuilding & Regrinding");services.add("Helmets & Protective Head Gears");services.add("Lorry Body Builders");services.add("Motor Spares & Machinery");services.add("Motor Vehicle Distributors");services.add("Motorcar Air Conditioning Equipment");services.add("Motorcar Parts & Supplies - Rebuilt");services.add("Motorcar Parts & Supplies - Wholesale");services.add("Motorcar Radio & Stereophonic");services.add("Motorcar Repairing & Services - Equipment & Supplies");services.add("Motorcar Vehicle Dealers - New Cars");services.add("Motorcar Vehicle Emissions Testing");services.add("Motorcycles & Motor Scooters");services.add("Service Station Equipment");services.add("Ship Builders & Repairs");services.add("Towing - Motor Vehicles");services.add("Tractor Equipment & Parts");
        services.add("Trailers - Equipment & Parts");services.add("Tyre");services.add("Tyre Manufacturing Materials");services.add("Tyres & Tyre Services");services.add("Windscreens");services.add("Batteries Dry Cells Retail");services.add("Bicycles & Spare Parts");services.add("Brake Down & Wrecker Services");services.add("Brake Shoes Bonding & Exchange");services.add("Car Parking Systems");services.add("Electric Motors - Dealers & Repairing");services.add("Engines - Outboard");services.add("Engines - Supplies Equipment & Parts");services.add("Garages/Breakdown Services");services.add("Motorcar / Vehicle Repairing Servising & Testing Equipment Suppliers");services.add("Motorcar Alarms & Security Systems");services.add("Motorcar Parts & Supplies - Retail");services.add("Motorcar Polishing & Waxing");services.add("Motorcar Renting & Leasing");services.add("Motorcar Rust Proofing Service");services.add("Motorcar Vehicle Dealers - Re-Conditioned");services.add("Motorcycles & Motor Scooters - Wholesale & Manufacturers");services.add("Service Stations");services.add("Tractor Dealers");services.add("Traffic Signal Systems");services.add("Truck Body - Manufacturers");
        services.add("Tyre Changing & Service Equipment");services.add("Tyre Manufacturers, Distributors & Dealers");services.add("Upholsterers");services.add("Glass - Motorcar, Plate,Window etc");services.add("Batteries - Storage -Wholesale & Manufacturers");services.add("Bearings");
        service_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,services);
        service_spinner.setAdapter(service_adapter);


        motor_bikes = new ArrayList<>();
        motor_bikes.add("වර්ගය");
        motor_bikes.add("Scooter");
        motor_bikes.add("Motor Bike");
        bike_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,motor_bikes);
        //motor_bike_type.setAdapter(bike_adapter);

        van_bus_lorry = new ArrayList<>();
        van_bus_lorry.add("වර්ගය");
        van_bus_lorry.add("Van");van_bus_lorry.add("Bus");van_bus_lorry.add("Lorry");
        van_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,van_bus_lorry);

        alert = new AlertDialog.Builder(NewAdsPostActivity.this);
        alert.setTitle("වාහන");
        alert.setMessage("දැන්වීම භාර දීම සාර්තකයි. ඇඩ්මින් මඩුල්ල අනුමත කළ පසු ඔබගේ දැන්වීම ඇප් එකෙහි පළ වනු ඇත. ස්තූතියි!.");
        alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        district_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,districts);
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_district = parent.getSelectedItemPosition();
                cities.clear();
                cities.add("නගරය තෝරන්න");
                if (selected_district > 0){
                    if (selected_district == 1){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("අක්කරපත්තුව");cities.add("කල්මුනේ");cities.add("අම්පාර");cities.add("සායින්දමරුදු");
                    }else if (selected_district == 2){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("අනුරාධපුර");cities.add("කැකිරාව");cities.add("මැදවච්චිය");cities.add("තඹුත්තේගම");cities.add("එප්පාවල");
                        cities.add("මිහින්තලේ");cities.add("නොච්චියාගම");cities.add("ගල්නෑව");cities.add("ගලේන්බින්දුනුවැව");cities.add("තලාව");
                        cities.add("හබරණ");
                    }else if (selected_district == 3){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("බදුල්ල");cities.add("බණ්ඩාරවෙල");cities.add("වැලිමඩ");cities.add("මහියංගනය");cities.add("ඇල්ල");
                        cities.add("හාලි ඇළ");cities.add("දියතලාව");cities.add("පස්සර");cities.add("හපුතලේ");
                    }else if (selected_district == 4){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("මඩකලපුව");

                    }else if (selected_district == 5){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("නුගේගොඩ");cities.add("දෙහිවල");cities.add("කොළඹ 6");cities.add("පිළියන්දල");
                        cities.add("කොට්ටාව");cities.add("රාජගිරිය");cities.add("මහරගම");cities.add("හෝමාගම");cities.add("මොරටුව");
                        cities.add("බොරලැස්ගමුව");cities.add("මාලඹේ");cities.add("කොළඹ 4");cities.add("බත්තරමුල්ල");cities.add("අතුරුගිරිය");cities.add("කොළඹ 10");
                        cities.add("කඩුවෙල");cities.add("කොළඹ 5");cities.add("කොළඹ 3");cities.add("කොහුවල");
                        cities.add("ගල්කිස්ස");cities.add("කොළඹ 8");cities.add("කොළඹ 11");cities.add("රත්මලාන");cities.add("කොළඹ 9");
                        cities.add("පන්නිපිටිය");cities.add("කොළඹ 2");cities.add("කෝට්ටෙ");cities.add("තලවතුගොඩ");cities.add("වැල්ලම්පිටිය");
                        cities.add("නාවල");cities.add("කොළඹ 15");cities.add("අංගොඩ");cities.add("පාදුක්ක");cities.add("කොලොන්නාව");
                        cities.add("කොළඹ 13");cities.add("කොළඹ 14");cities.add("කොළඹ 12");cities.add("කොළඹ 7");cities.add("හංවැල්ල");
                        cities.add("කැස්බෑව");cities.add("අවිස්සාවේල්ල");cities.add("කොළඹ 1");
                    }else if (selected_district == 6){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("අම්බලන්ගොඩ");cities.add("ඇල්පිටිය");cities.add("හික්කඩුව");cities.add("බද්දේගම");
                        cities.add("කරාපිටිය");cities.add("බෙන්තොට");cities.add("අහංගම");cities.add("බටපොළ");
                        cities.add("ගාල්ල");
                    }else if (selected_district == 7){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("කැළණිය ");cities.add("කඩවත");cities.add("කිරිබත්ගොඩ");cities.add("ජා-ඇල");cities.add("වත්තල");
                        cities.add("නිට්ටඔුව");cities.add("මිනුවන්ගොඩ");cities.add("කටුනායක");
                        cities.add("කඳාන");cities.add("රාගම");cities.add("දෙල්ගොඩ");cities.add("දිවුලපිටිය");cities.add("මීරිගම");
                        cities.add("වේයන්ගොඩ");cities.add("ගනේමුල්ල");
                    }else if (selected_district == 8){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("තංගල්ල");cities.add("බෙලිඅත්ත");cities.add("අම්බලන්තොට");cities.add("තිස්සමහාරාම");
                        cities.add("හම්බන්තොට");
                    }else if (selected_district == 9){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("යාපනය");cities.add("නල්ලුර්");cities.add("චාවකච්චේරි");
                    }else if (selected_district == 10){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("හොරණ");cities.add("කළුතර");cities.add("පානදුර");cities.add("බණ්ඩාරගම");
                        cities.add("මතුගම");cities.add("වාද්දුව");cities.add("අලුත්ගම");cities.add("බේරුවල ");
                        cities.add("ඉංගිරිය");
                    }else if (selected_district == 11){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("නුවර");cities.add("ගම්පොල");cities.add("පුඡාපිටිය");
                        cities.add("තුම්පනේ");cities.add("අකුරණ");cities.add("පාතදුම්බර");
                        cities.add("පන්විල");cities.add("උඩදුම්බර");cities.add("මිනිපෙ");
                        cities.add("කුන්ඩසාලෙ");cities.add("හාරිස්පත්තුව");

                    }else if (selected_district == 12){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("කෑගල්ල");cities.add("මාවනැල්ල");cities.add("වරකාපොළ ");cities.add("රඔුක්කන");cities.add("රැවන්වැල්ල");cities.add("ගලිගමුව");
                        cities.add("දෙහිඕවිට");cities.add("යටියන්තොට");cities.add("දෙරණියගල");cities.add("කිතුල්ගල");
                    }else if (selected_district == 13){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("කිළිනොච්චිය");
                    }else if (selected_district == 14){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("කුරෑණෑගල");cities.add("කුලියාපිටිය");cities.add("පන්නල");cities.add("නාරම්මල");
                        cities.add("වාරියපොළ");cities.add("මාවතගම");cities.add("පොල්ගහවෙල");cities.add("ඉබ්බාගමුව");
                        cities.add("අලව්ව");cities.add("ගිරිඋල්ල");cities.add("හෙට්ටිපොළ");cities.add("නිකවැරටිය");
                        cities.add("බිංගිරිය");cities.add("ගල්ගමුව");
                    }else if (selected_district == 15){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("මන්නාරම");
                    }else if (selected_district == 16){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("මාතලේ");cities.add("දඔුල්ල");cities.add("ගලේවෙල");cities.add("උකුවෙල");
                        cities.add("සීගිරිය");cities.add("රත්තොට");cities.add("පලපත්වෙල");cities.add("යටවත්ත");

                    }else if (selected_district == 17){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("මාතර");cities.add("අකුරැස්ස");cities.add("වැලිගම");
                        cities.add("හක්මන");cities.add("දික්වැල්ල");cities.add("කඔුරැපිටිය");
                        cities.add("දෙනියාය");cities.add("කඔුරැගමුව");cities.add("කැකනදුර");

                    }else if (selected_district == 18){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("මොණරාගල");cities.add("බිබිල");cities.add("වැල්ලවාය");cities.add("බුත්තල");cities.add("කතරගම");

                    }else if (selected_district == 19){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("මුලතිව්");

                    }else if (selected_district == 20){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("නුවර එළිය");cities.add("හැටන්");cities.add("ගිනිගත්හේන");cities.add("මඬුල්ල");

                    }else if (selected_district == 21){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("පොළොන්නරුව");cities.add("කඳුරැවෙල");cities.add("හිඟුරක්ගොඩ");cities.add("මැදිරිගිරිය");

                    }else if (selected_district == 22){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("හලාවත");cities.add("වෙන්නප්පුව");cities.add("පුත්තලම");
                        cities.add("නාත්තන්ඩිය");cities.add("මාරවිල");cities.add("දංකොටුව");

                    }else if (selected_district == 23){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("රත්නපුරය");cities.add("ඇඹිලිපිටිය");cities.add("බළන්ගොඩ ");
                        cities.add("පැල්මඩුල්ල");cities.add("ඇහැළියගොඩ");cities.add("කුරුවිට");

                    }else if (selected_district == 24){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("ත්\u200Dරිකුණාමලය");
                        cities.add("කින්නියා");

                    }else if (selected_district == 25){
                        cities.clear();
                        cities.add("නගරය තෝරන්න");
                        cities.add("වව්නියාව");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cities_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,cities);

        brand_1.add("Alfa Romeo");brand_1.add("Aston Martin");brand_1.add("Audi");brand_1.add("Austin");brand_1.add("BMW");
        brand_1.add("Buick");brand_1.add("Cadillac");brand_1.add("Changan");brand_1.add("Chery");brand_1.add("Chevrolet");
        brand_1.add("Chrysler");brand_1.add("Citroen");brand_1.add("Daewoo");brand_1.add("Daihatsu");brand_1.add("Datsun");
        brand_1.add("DFSK");brand_1.add("Dodge");brand_1.add("Ferrari");brand_1.add("Fait");brand_1.add("Ford");
        brand_1.add("Geely");brand_1.add("GMC");brand_1.add("Hino");brand_1.add("Honda");brand_1.add("Hummer");brand_1.add("Hyundai");
        brand_1.add("Isuzu");brand_1.add("Jaguar");brand_1.add("Jeep");brand_1.add("Kia");brand_1.add("Lamborghini");brand_1.add("Land Rover");
        brand_1.add("Lexus");brand_1.add("Lincoln");brand_1.add("Mahindra");brand_1.add("Maruti Suzuki");brand_1.add("Maruti");
        brand_1.add("Mazda");brand_1.add("Mercedes Benz");brand_1.add("MG");brand_1.add("Micro");brand_1.add("Mini");brand_1.add("Mitsubishi");
        brand_1.add("Morris");brand_1.add("Moto Guzzi");brand_1.add("Nissan");brand_1.add("Oldsmobile");brand_1.add("Opel");
        brand_1.add("Perodua");brand_1.add("Peugeot");brand_1.add("Plymoth");brand_1.add("Pontiac");brand_1.add("Porsche");brand_1.add("Proton");
        brand_1.add("Renault");brand_1.add("Rover");brand_1.add("Royal Enfield");brand_1.add("Saab");brand_1.add("Scion");brand_1.add("SEAT");
        brand_1.add("Skoda");brand_1.add("Smart");brand_1.add("Ssang Yong");brand_1.add("Subaru");brand_1.add("Suzuki");brand_1.add("Tata");
        brand_1.add("Tesla");brand_1.add("Toyota");brand_1.add("Vauxhall");brand_1.add("Volkswagen");brand_1.add("Volvo");brand_1.add("Zotye");

        brand_2.add("වෙළඳ නාමය");
        brand_2.add("Ashok Leyland");brand_2.add("Chanda");brand_2.add("Changan");brand_2.add("Daihatsu");brand_2.add("Dimo බට්ටා");
        brand_2.add("Eicher");brand_2.add("Eicher Kender");brand_2.add("Ford");brand_2.add("Foton");brand_2.add("Hino");
        brand_2.add("Honda");brand_2.add("Hyundai");brand_2.add("Isuzu");brand_2.add("JAC");brand_2.add("Mahindra");
        brand_2.add("Mazda");brand_2.add("Mercedes-Benz");brand_2.add("Mitsubishi");brand_2.add("Nissan");brand_2.add("Scania");
        brand_2.add("Subaru");brand_2.add("Suzuki");brand_2.add("Tata");brand_2.add("Toyota");brand_2.add("Volvo");brand_2.add("වෙනත් වෙළඳ නාම");
        brand_2_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,brand_2);

        brand_3.add("වෙළඳ නාමය");
        brand_3.add("Aprilia");brand_3.add("Bajaj");brand_3.add("BMW");brand_3.add("Chopper");brand_3.add("Demak");
        brand_3.add("Ducati");brand_3.add("Electra");brand_3.add("Falcon");brand_3.add("Harley Davidson");brand_3.add("Hero");
        brand_3.add("Honda");brand_3.add("Kawasaki");brand_3.add("Kinetic");brand_3.add("KTM");brand_3.add("Loncin");
        brand_3.add("Mahindra");brand_3.add("Minnelli");brand_3.add("Piaggio");brand_3.add("Ranomoto");brand_3.add("Royal Enfield");
        brand_3.add("Scooty");brand_3.add("Suzuki");brand_3.add("Triumph");brand_3.add("TVS");brand_3.add("Vespa");brand_3.add("Yamaha");
        brand_3.add("වෙනත් වෙළඳ නාම");

        brand_3_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,brand_3);

        brands_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,brand_1);

        model_2_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,model_2);
        models_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line, model_1);
        bodies.add(new ItemData("Body Type",R.drawable.vehicle));
        bodies.add(new ItemData("Saloon",R.drawable.super_saloon));
        bodies.add(new ItemData("Hatchback",R.drawable.hatchback));
        bodies.add(new ItemData("Station wagon",R.drawable.station_wagon));bodies.add(new ItemData("Convertible",R.drawable.convertible));
        bodies.add(new ItemData("Couple/Sport",R.drawable.sport));
        bodies.add(new ItemData("SUV / 4X4",R.drawable.suv));bodies.add(new ItemData("MPV",R.drawable.mpv));

        body_adapter = new SpinnerAdapter(this,R.layout.spinner_layout,R.id.body_type_id,bodies);

        transmissions.add("Manual");transmissions.add("Automatic");
        transmissions.add("Triptronic");transmissions.add("Other Transmission");

        transmission_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,transmissions);

        fuels.add("ඩීසල්");fuels.add("පෙට්රල්");
        fuels.add("CNG ඉන්ධන");fuels.add("වෙනත් ඉන්ධන වර්ග");

        fuel_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,fuels);

        vehicle_types.add("Heavy vehicles");vehicle_types.add("1. Bulldozer");vehicle_types.add("2. Crane");vehicle_types.add("3. Digger");vehicle_types.add("4. Excavation");
        vehicle_types.add("5. Loader / lifter");vehicle_types.add("6. Roller");vehicle_types.add("7. Tractor");vehicle_types.add("Light vehicles");vehicle_types.add("1. Three Wheelers");
        vehicle_types.add("2. Push Cycles");vehicle_types.add("3. Other Vehicle");
        vehicle_type_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, vehicle_types);

        service_types.add("භාණ්ඩ වර්ගය");
        service_types.add("ස්වයංක්\u200Dරිය කොටස්\n");service_types.add("කාර් ශ්\u200Dරව්\u200Dය /දෘෂ්\u200Dය");service_types.add("නඩත්තු / අලුත්වැඩියා");
        service_types.add("සුරක්ෂිතතාව / ආරක්ෂාව");service_types.add("ටයර් / රිම්");
        service_types.add("වෙනත් උපාංග");

        service_type_adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,service_types);


        name.setText(user_name);
        ad_title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = parent.getSelectedItemPosition();
                if (position == 1){

                    isSelectCar = true;
                    location.setAdapter(district_adapter);
                    city.setAdapter(cities_adapter);
                    brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selected_brand = parent.getSelectedItemPosition();
                            model_1.clear();
                            model_1.add("මාදිලිය");
                            if (selected_brand == 1){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Stelvio Quadrifoglio");
                                model_1.add("Stelvio");
                                model_1.add("Giulia Q4 Sedan (4WD)");
                                model_1.add("Stelvio Q4 (4WD) ");
                                model_1.add("Stelvio RWD ");
                                model_1.add("Other Model");
                            }else if (selected_brand == 2){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("VANQUISH S");
                                model_1.add("VANQUISH");
                                model_1.add("VANQUISH VOLANTE");
                                model_1.add("VANQUISH ZAGATO");
                                model_1.add("RAPIDE S");
                                model_1.add("DB9 GP");
                                model_1.add("DB11");
                                model_1.add("V12 VANTAGE S");
                                model_1.add("V12 VANTAGE S ROADSTER");
                                model_1.add("V8 VANTAGE S");
                                model_1.add("LAGONDA TARAF");
                                model_1.add("VANTAGE GT12");
                                model_1.add("VANTAGE GT8");
                                model_1.add("ASTON MARTIN VULCAN");
                                model_1.add("Other Model");
                            }else if (selected_brand == 3){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("100");
                                model_1.add("80");
                                model_1.add("A1");
                                model_1.add("A3");
                                model_1.add("A4");
                                model_1.add("A5");
                                model_1.add("A6");
                                model_1.add("A7");
                                model_1.add("A8");
                                model_1.add("Q1");
                                model_1.add("Q2");
                                model_1.add("Q3");
                                model_1.add("Q5");
                                model_1.add("Q7");
                                model_1.add("R8");
                                model_1.add("RS3");
                                model_1.add("RS4");
                                model_1.add("RS5");
                                model_1.add("RS6");
                                model_1.add("S1");
                                model_1.add("S2");
                                model_1.add("S3");
                                model_1.add("S4");
                                model_1.add("S5");
                                model_1.add("S6");
                                model_1.add("S7");
                                model_1.add("S8");
                                model_1.add("SQ5");
                                model_1.add("SQ7");
                                model_1.add("TT");
                                model_1.add("TTS");
                                model_1.add("V8");
                                model_1.add("Other Model");
                            }else if (selected_brand == 4){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("7");
                                model_1.add("Cambridge");
                                model_1.add("Mini Cooper");
                                model_1.add("Mini");
                                model_1.add("Standard");
                                model_1.add("Other Model");
                            }else if (selected_brand == 5){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("116i");
                                model_1.add("118d");
                                model_1.add("118i");
                                model_1.add("120d");
                                model_1.add("120i");
                                model_1.add("123d");
                                model_1.add("125i");
                                model_1.add("130i");
                                model_1.add("135i");
                                model_1.add("218d");
                                model_1.add("218i");
                                model_1.add("220d");
                                model_1.add("220i");
                                model_1.add("225XE");
                                model_1.add("225i");
                                model_1.add("228i");
                                model_1.add("230i");
                                model_1.add("252i");
                                model_1.add("316i");
                                model_1.add("316ti");
                                model_1.add("318is");
                                model_1.add("318ti");
                                model_1.add("320Ci");
                                model_1.add("320d");
                                model_1.add("320i");
                                model_1.add("323Ci");
                                model_1.add("323i");
                                model_1.add("325Ci");
                                model_1.add("325d");
                                model_1.add("325e");
                                model_1.add("325i");
                                model_1.add("328Ci");
                                model_1.add("328d");
                                model_1.add("328i");
                                model_1.add("330 GT");
                                model_1.add("330Ci");
                                model_1.add("330d");
                                model_1.add("330e");
                                model_1.add("330i");
                                model_1.add("335d");
                                model_1.add("335i");
                                model_1.add("340 GT");
                                model_1.add("340i");
                                model_1.add("420d");
                                model_1.add("420i");
                                model_1.add("428 Gran Coupe");
                                model_1.add("428i");
                                model_1.add("430 Gran Coupe");
                                model_1.add("430i");
                                model_1.add("435 Bran Coupe");
                                model_1.add("435i");
                                model_1.add("440 Gran Coupe");
                                model_1.add("440i");
                                model_1.add("520d");
                                model_1.add("520i");
                                model_1.add("523i");
                                model_1.add("525e");
                                model_1.add("525i");
                                model_1.add("528i");
                                model_1.add("530d");
                                model_1.add("530e");
                                model_1.add("530i");
                                model_1.add("535 GT");
                                model_1.add("535d");
                                model_1.add("535i");
                                model_1.add("540d");
                                model_1.add("540i");
                                model_1.add("545i");
                                model_1.add("550 GT");
                                model_1.add("550i");
                                model_1.add("630i");
                                model_1.add("633CSi");
                                model_1.add("635CSi");
                                model_1.add("635d");
                                model_1.add("640 GT");
                                model_1.add("640 Gran Coupe");
                                model_1.add("640d");
                                model_1.add("640i");
                                model_1.add("645ci");
                                model_1.add("650 Gran Coupe");
                                model_1.add("650i");
                                model_1.add("730d");
                                model_1.add("730iL");
                                model_1.add("733i");
                                model_1.add("735Li");
                                model_1.add("735i");
                                model_1.add("735iL");
                                model_1.add("740Le");
                                model_1.add("740Li");
                                model_1.add("740d");
                                model_1.add("740e");
                                model_1.add("740i");
                                model_1.add("740iL");
                                model_1.add("745Li");
                                model_1.add("745i");
                                model_1.add("750Li");
                                model_1.add("750i");
                                model_1.add("750iL");
                                model_1.add("760Li");
                                model_1.add("840Ci");
                                model_1.add("850Ci");
                                model_1.add("850i");
                                model_1.add("ActivateHybrid 3");
                                model_1.add("ActivateHybrid 5");
                                model_1.add("ActivateHybrid 37");
                                model_1.add("ActivateHybrid 740");
                                model_1.add("ActivateHybrid 750");
                                model_1.add("ActivateHybrid X6");
                                model_1.add("Alpina B6");
                                model_1.add("Alpina B7");
                                model_1.add("Coupe");
                                model_1.add("E46");
                                model_1.add("E60");
                                model_1.add("E90");
                                model_1.add("GT");
                                model_1.add("L7");
                                model_1.add("M");
                                model_1.add("M135i");
                                model_1.add("M140i");
                                model_1.add("M2");
                                model_1.add("M235");
                                model_1.add("M235i");
                                model_1.add("M240");
                                model_1.add("M240i");
                                model_1.add("M3");
                                model_1.add("M4");
                                model_1.add("M5");
                                model_1.add("M535i");
                                model_1.add("M550");
                                model_1.add("M6");
                                model_1.add("M6 Gran Coupe");
                                model_1.add("M635CSi");
                                model_1.add("M760");
                                model_1.add("M760Li");
                                model_1.add("Mini Cooper");
                                model_1.add("Model");
                                model_1.add("X1");
                                model_1.add("X2");
                                model_1.add("X3");
                                model_1.add("X4");
                                model_1.add("X5");
                                model_1.add("X5 M");
                                model_1.add("X5 eDrive");
                                model_1.add("X6");
                                model_1.add("X6 M");
                                model_1.add("Z3");
                                model_1.add("Z4");
                                model_1.add("Z4 M");
                                model_1.add("Z8");
                                model_1.add("i3");
                                model_1.add("i5");
                                model_1.add("i8");
                                model_1.add("Other Model");
                            }else if (selected_brand == 6){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 7){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 8){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 9){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("A3");
                                model_1.add("Arrizo");
                                model_1.add("E3");
                                model_1.add("E5");
                                model_1.add("Fulwin");
                                model_1.add("QQ");
                                model_1.add("QQ3");
                                model_1.add("Tiggo");
                                model_1.add("X1");
                                model_1.add("eQ");
                                model_1.add("Other Model");
                            }else if (selected_brand == 10){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Aveo");
                                model_1.add("Beat");
                                model_1.add("Bolt");
                                model_1.add("Camaro");
                                model_1.add("Captiva");
                                model_1.add("Colorado");
                                model_1.add("Corvette");
                                model_1.add("Cruze");
                                model_1.add("Malibu");
                                model_1.add("Silverado");
                                model_1.add("Sonic");
                                model_1.add("Spark");
                                model_1.add("Other Model");
                            }else if (selected_brand == 11){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("200");
                                model_1.add("300");
                                model_1.add("Other Model");
                            }else if (selected_brand == 12){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 13){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Espero");
                                model_1.add("Lanos");
                                model_1.add("Leganza");
                                model_1.add("Magnus");
                                model_1.add("Nubira");
                                model_1.add("Tacuma");
                                model_1.add("Tico");
                                model_1.add("Tosca");
                                model_1.add("Other Model");
                            }else if (selected_brand == 14){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Altis");
                                model_1.add("Atrai Wagon");
                                model_1.add("Boon");
                                model_1.add("Canbus");
                                model_1.add("Cast Activa");
                                model_1.add("Charade");
                                model_1.add("Charmant");
                                model_1.add("Copen");
                                model_1.add("Cuore");
                                model_1.add("Esse");
                                model_1.add("F50");
                                model_1.add("Hijet");
                                model_1.add("Leeza");
                                model_1.add("Mebius");
                                model_1.add("Mira");
                                model_1.add("Move");
                                model_1.add("Redigo");
                                model_1.add("Rocky");
                                model_1.add("Tanto");
                                model_1.add("Terios");
                                model_1.add("Thor");
                                model_1.add("Wake");
                                model_1.add("Other Model");
                            }else if (selected_brand == 15){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Cross");
                                model_1.add("Go");
                                model_1.add("Go Plus");
                                model_1.add("Mi-Do");
                                model_1.add("On-Do");
                                model_1.add("Redi Go");
                                model_1.add("Tanto");
                                model_1.add("Terios");
                                model_1.add("Other Model");
                            }else if (selected_brand == 16){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Glory");
                                model_1.add("V27");
                                model_1.add("Other Model");
                            }else if (selected_brand == 17){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 18){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 19){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("1100");
                                model_1.add("500");
                                model_1.add("Bravo");
                                model_1.add("Fullback");
                                model_1.add("Linea");
                                model_1.add("Palio");
                                model_1.add("Panda");
                                model_1.add("Punto");
                                model_1.add("Sedici");
                                model_1.add("Tipo");
                                model_1.add("Uno");
                                model_1.add("Other Model");
                            }else if (selected_brand == 20){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("C-Max");
                                model_1.add("Ecosport");
                                model_1.add("Edge");
                                model_1.add("Escape");
                                model_1.add("Everest");
                                model_1.add("Expedition");
                                model_1.add("Explorer");
                                model_1.add("F-150");
                                model_1.add("Festiva");
                                model_1.add("Fiesta");
                                model_1.add("Flex");
                                model_1.add("Focus");
                                model_1.add("Fusion");
                                model_1.add("GT");
                                model_1.add("Ka+");
                                model_1.add("Kuga");
                                model_1.add("Laser");
                                model_1.add("Mondeo");
                                model_1.add("Mustang");
                                model_1.add("Raptor Ranger");
                                model_1.add("Super Duty");
                                model_1.add("Taurus");
                                model_1.add("Transit");
                                model_1.add("Other Model");
                            }else if (selected_brand == 21){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 22){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Acadia");
                                model_1.add("Canyon");
                                model_1.add("Envoy");
                                model_1.add("Sierra");
                                model_1.add("Terrain");
                                model_1.add("Yukon");
                                model_1.add("Other Model");
                            }else if (selected_brand == 23){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 24){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Accord");
                                model_1.add("Airwave");
                                model_1.add("Amaze");
                                model_1.add("Ballade");
                                model_1.add("Beat");
                                model_1.add("CRV");
                                model_1.add("CRZ");
                                model_1.add("City");
                                model_1.add("Civic");
                                model_1.add("Clarity");
                                model_1.add("Concerto");
                                model_1.add("FR-V");
                                model_1.add("Fit");
                                model_1.add("Fit Area");
                                model_1.add("Fit She's");
                                model_1.add("Fit Shuttle");
                                model_1.add("Freed");
                                model_1.add("Grace");
                                model_1.add("HR-V");
                                model_1.add("Insight");
                                model_1.add("Inspire");
                                model_1.add("Integra");
                                model_1.add("Jade");
                                model_1.add("Jazz");
                                model_1.add("Legend");
                                model_1.add("Logo");
                                model_1.add("N-Box");
                                model_1.add("N-One");
                                model_1.add("N-WGN");
                                model_1.add("NSX");
                                model_1.add("Odyssey");
                                model_1.add("Pilot");
                                model_1.add("Ridgeline");
                                model_1.add("S2000");
                                model_1.add("S660");
                                model_1.add("Spike");
                                model_1.add("Step Wagon");
                                model_1.add("Vezel");
                                model_1.add("WR-V");
                                model_1.add("Other Model");
                            }else if (selected_brand == 25){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("H1");
                                model_1.add("H2");
                                model_1.add("H3");
                                model_1.add("Other Model");
                            }else if (selected_brand == 26){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Accent");
                                model_1.add("Atos");
                                model_1.add("Azera");
                                model_1.add("Coupe");
                                model_1.add("Elantra");
                                model_1.add("Eon");
                                model_1.add("Excel");
                                model_1.add("Genesis");
                                model_1.add("Getz");
                                model_1.add("Grand i10");
                                model_1.add("loniq");
                                model_1.add("Kona");
                                model_1.add("Lantra");
                                model_1.add("Matrix");
                                model_1.add("Mistra");
                                model_1.add("Nexo");
                                model_1.add("Santa Fe");
                                model_1.add("Santro");
                                model_1.add("Sonata");
                                model_1.add("Stellar");
                                model_1.add("Terracan");
                                model_1.add("Trajet");
                                model_1.add("Tucson");
                                model_1.add("Veloster");
                                model_1.add("i20");
                                model_1.add("i30");
                                model_1.add("i40");
                                model_1.add("Other Model");
                            }else if (selected_brand == 27){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Bighorn");
                                model_1.add("D-Max");
                                model_1.add("Gemini");
                                model_1.add("MU-7");
                                model_1.add("MU-X");
                                model_1.add("Panther");
                                model_1.add("Rodeo");
                                model_1.add("Smart cab");
                                model_1.add("Trooper");
                                model_1.add("Other Model");
                            }else if (selected_brand == 28){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("E-Pace");
                                model_1.add("F-Pace");
                                model_1.add("F-Type");
                                model_1.add("I-Pace");
                                model_1.add("S-Type");
                                model_1.add("X-Type");
                                model_1.add("XE");
                                model_1.add("XF");
                                model_1.add("XJ");
                                model_1.add("XK");
                                model_1.add("Other Model");
                            }else if (selected_brand == 29){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Cherokee");
                                model_1.add("Compass");
                                model_1.add("Grand Cherokee");
                                model_1.add("Renegade");
                                model_1.add("Wrangler");
                                model_1.add("Other Model");
                            }else if (selected_brand == 30){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Cadenza");
                                model_1.add("carens");
                                model_1.add("Carnival");
                                model_1.add("Ceed");
                                model_1.add("Cerato");
                                model_1.add("Clarus");
                                model_1.add("Forte");
                                model_1.add("K7");
                                model_1.add("K9");
                                model_1.add("K900");
                                model_1.add("Mentor");
                                model_1.add("Niro");
                                model_1.add("Optima");
                                model_1.add("Picanto");
                                model_1.add("Rio");
                                model_1.add("Rondo");
                                model_1.add("Sedona");
                                model_1.add("Sephia");
                                model_1.add("Sorento");
                                model_1.add("Spectra");
                                model_1.add("Sportage");
                                model_1.add("Stinger");
                                model_1.add("Stonic");
                                model_1.add("saul");
                                model_1.add("Other Model");
                            }else if (selected_brand == 31){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 32){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Defender");
                                model_1.add("Discovery");
                                model_1.add("Discovery Sport");
                                model_1.add("Freelander");
                                model_1.add("Range Rover");
                                model_1.add("Range Rover Evoque");
                                model_1.add("Range Rover PHEV");
                                model_1.add("Range Rover Sport");
                                model_1.add("Range Rover Velar");
                                model_1.add("SV Coupe");
                                model_1.add("Other Model");
                            }else if (selected_brand == 33){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("CT-200H");
                                model_1.add("ES");
                                model_1.add("HS250H");
                                model_1.add("LS400");
                                model_1.add("LX450");
                                model_1.add("Land Cruiser");
                                model_1.add("NX");
                                model_1.add("NX300H");
                                model_1.add("RX350");
                                model_1.add("RX400");
                                model_1.add("UX");
                                model_1.add("Other Model");
                            }else if (selected_brand == 34){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 35){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Bolero");
                                model_1.add("KUV100");
                                model_1.add("Legend");
                                model_1.add("Nuvosport");
                                model_1.add("Quanto");
                                model_1.add("Scorpio");
                                model_1.add("TUV300");
                                model_1.add("Thar");
                                model_1.add("Verito");
                                model_1.add("XUV500");
                                model_1.add("Xylo");
                                model_1.add("e2o");
                                model_1.add("Other Model");
                            }else if (selected_brand == 36){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("800");
                                model_1.add("Alto");
                                model_1.add("Baleno");
                                model_1.add("Esteem");
                                model_1.add("Gypsy");
                                model_1.add("Omni");
                                model_1.add("WagonR");
                                model_1.add("Zen");
                                model_1.add("Other Model");
                            }else if (selected_brand == 37){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 38){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("2");
                                model_1.add("2 Skyactive");
                                model_1.add("3");
                                model_1.add("5");
                                model_1.add("6");
                                model_1.add("Astina");
                                model_1.add("Axela");
                                model_1.add("BT-50");
                                model_1.add("Butterfly");
                                model_1.add("CX-3");
                                model_1.add("CX-5");
                                model_1.add("CX-6");
                                model_1.add("CX-7");
                                model_1.add("CX-8");
                                model_1.add("CX-9");
                                model_1.add("Carol");
                                model_1.add("Demio");
                                model_1.add("Eunos");
                                model_1.add("FA4TS");
                                model_1.add("Familia");
                                model_1.add("Flair");
                                model_1.add("MX-5");
                                model_1.add("Millenia");
                                model_1.add("RX");
                                model_1.add("Roadster");
                                model_1.add("Tribute");
                                model_1.add("Verisa");
                                model_1.add("Other Model");
                            }else if (selected_brand == 39){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("190D");
                                model_1.add("A140");
                                model_1.add("A150");
                                model_1.add("A160");
                                model_1.add("A170");
                                model_1.add("A180");
                                model_1.add("A190");
                                model_1.add("A200");
                                model_1.add("A210");
                                model_1.add("A220");
                                model_1.add("A250");
                                model_1.add("A45");
                                model_1.add("B150");
                                model_1.add("B160");
                                model_1.add("B170");
                                model_1.add("B180");
                                model_1.add("B200");
                                model_1.add("B220");
                                model_1.add("B250e");
                                model_1.add("C160");
                                model_1.add("C180");
                                model_1.add("C200");
                                model_1.add("C220");
                                model_1.add("C250");
                                model_1.add("C300");
                                model_1.add("C350");
                                model_1.add("CLA 180");
                                model_1.add("CLA 200");
                                model_1.add("CLA 250");
                                model_1.add("CLS");
                                model_1.add(" E200");
                                model_1.add("E220");
                                model_1.add("E240");
                                model_1.add("E250");
                                model_1.add("E300");
                                model_1.add("E350");
                                model_1.add("E400");
                                model_1.add("GLA 180");
                                model_1.add("GLA 200");
                                model_1.add("GLA 250");
                                model_1.add("GLC 250");
                                model_1.add("GLC 300");
                                model_1.add("GLE 320");
                                model_1.add("GLE 4OO");
                                model_1.add("GLE 500");
                                model_1.add("GLS 400");
                                model_1.add("GLS 500");
                                model_1.add("ML250");
                                model_1.add("ML270");
                                model_1.add("ML280");
                                model_1.add("ML300");
                                model_1.add("ML320");
                                model_1.add("ML350");
                                model_1.add("ML420");
                                model_1.add("ML430");
                                model_1.add("ML500");
                                model_1.add("ML55");
                                model_1.add("ML63");
                                model_1.add("S300");
                                model_1.add("S320");
                                model_1.add("S350");
                                model_1.add("S500");
                                model_1.add("S560");
                                model_1.add("SL 400");
                                model_1.add("SL 500");
                                model_1.add("SLC 180");
                                model_1.add("SLC 200");
                                model_1.add("SLC 300");
                                model_1.add("SLK 200");
                                model_1.add("Other Model");
                            }else if (selected_brand == 40){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("3");
                                model_1.add("6");
                                model_1.add("GS");
                                model_1.add("ZS");
                                model_1.add("Other Model");
                            }else if (selected_brand == 41){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Actyon");
                                model_1.add("BAIC");
                                model_1.add("D20 HACH");
                                model_1.add("Emgrand");
                                model_1.add("Geely");
                                model_1.add("Glory");
                                model_1.add("Junior");
                                model_1.add("Korondo");
                                model_1.add("Kyron");
                                model_1.add("Lifan");
                                model_1.add("MX 7");
                                model_1.add("Panda");
                                model_1.add("Panda Cross");
                                model_1.add("Privilage");
                                model_1.add("Rexton");
                                model_1.add("Rhino");
                                model_1.add("Tivoli");
                                model_1.add("Trend");
                                model_1.add("Voleex");
                                model_1.add("Other Model");
                            }else if (selected_brand == 42){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Clubman");
                                model_1.add("Cooper");
                                model_1.add("Countryman");
                                model_1.add("Other Model");
                            }else if (selected_brand == 43){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("4DR");
                                model_1.add("ASX");
                                model_1.add("Attrage");
                                model_1.add("Cedia");
                                model_1.add("Celeste");
                                model_1.add("Chariot");
                                model_1.add("Colt");
                                model_1.add("Delica");
                                model_1.add("Eclipse Cross");
                                model_1.add("FTO");
                                model_1.add("Galant");
                                model_1.add("J20");
                                model_1.add("J24");
                                model_1.add("L200");
                                model_1.add("Lancer");
                                model_1.add("Libero");
                                model_1.add("Mirage");
                                model_1.add("Montero");
                                model_1.add("Outlander");
                                model_1.add("Pajero");
                                model_1.add("RVR");
                                model_1.add("Raider");
                                model_1.add("Shogun");
                                model_1.add("Sportero");
                                model_1.add("Strada");
                                model_1.add("Towny");
                                model_1.add("Xpander");
                                model_1.add("eK Space");
                                model_1.add("eK Wagon");
                                model_1.add("i-MiEV");
                                model_1.add("Other Model");
                            }else if (selected_brand == 44){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("8");
                                model_1.add("Mini");
                                model_1.add("Minor");
                                model_1.add("Oxford");
                                model_1.add("Other Model");
                            }else if (selected_brand == 45){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 46){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("370Z");
                                model_1.add("AD Wagon");
                                model_1.add("Almera");
                                model_1.add("Aventure");
                                model_1.add("Bluebird");
                                model_1.add("Cefiro");
                                model_1.add("Dayz");
                                model_1.add("Dualis");
                                model_1.add("Dutsun");
                                model_1.add("Fairlady Z");
                                model_1.add("Fuga");
                                model_1.add("GT-R");
                                model_1.add("Gloria");
                                model_1.add("J10");
                                model_1.add("Juke");
                                model_1.add("Leaf");
                                model_1.add("March");
                                model_1.add("Micra");
                                model_1.add("Navara");
                                model_1.add("Note");
                                model_1.add("Pathfinder");
                                model_1.add("Patrol");
                                model_1.add("Presea");
                                model_1.add("Primera");
                                model_1.add("Pulsar");
                                model_1.add("Qashqai");
                                model_1.add("Serena");
                                model_1.add("Sima");
                                model_1.add("Skyline");
                                model_1.add("Sunny");
                                model_1.add("Sylphy");
                                model_1.add("Teana");
                                model_1.add("Terrano");
                                model_1.add("Tiida");
                                model_1.add("Wingroad");
                                model_1.add("X-Trail");
                                model_1.add("e-NV200");
                                model_1.add("Other Model");
                            }else if (selected_brand == 47){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 48){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Astra");
                                model_1.add("Combo");
                                model_1.add("Cresent");
                                model_1.add("Crossland");
                                model_1.add("Frontera");
                                model_1.add("Grandland");
                                model_1.add("Insignia");
                                model_1.add("Karl");
                                model_1.add("Mokka");
                                model_1.add("Omega");
                                model_1.add("Vectra");
                                model_1.add("Other Model");
                            }else if (selected_brand == 49){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Alza");
                                model_1.add("Axia");
                                model_1.add("Bezza");
                                model_1.add("Kancil");
                                model_1.add("Kelisa");
                                model_1.add("Kembara");
                                model_1.add("Kenari");
                                model_1.add("Myvi");
                                model_1.add("Viva Elite");
                                model_1.add("Other Model");
                            }else if (selected_brand == 50){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("104");
                                model_1.add("108");
                                model_1.add("2008");
                                model_1.add("206");
                                model_1.add("208");
                                model_1.add("3008");
                                model_1.add("305");
                                model_1.add("307");
                                model_1.add("308");
                                model_1.add("404");
                                model_1.add("405");
                                model_1.add("406");
                                model_1.add("407");
                                model_1.add("408");
                                model_1.add("5008");
                                model_1.add("505");
                                model_1.add("607");
                                model_1.add("iOn");
                                model_1.add("Other Model");
                            }else if (selected_brand == 51){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 52){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 53){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("718");
                                model_1.add("718 Boxter");
                                model_1.add("718 Cayman");
                                model_1.add("718 GTS");
                                model_1.add("911");
                                model_1.add("911 Carrera");
                                model_1.add("911 GT2");
                                model_1.add("911 GT3");
                                model_1.add("911 GTS");
                                model_1.add("911 Targa");
                                model_1.add("911 Turbo");
                                model_1.add("918 Spyder");
                                model_1.add("Carrera GT");
                                model_1.add("Cayenne");
                                model_1.add("Macan");
                                model_1.add("Panamera");
                                model_1.add("Other Model");
                            }else if (selected_brand == 54){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Exora");
                                model_1.add("Gen-2");
                                model_1.add("Perdana");
                                model_1.add("Persona");
                                model_1.add("Saga");
                                model_1.add("Savvy");
                                model_1.add("waja");
                                model_1.add("wira");
                                model_1.add("Other Model");
                            }else if (selected_brand == 55){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Captur");
                                model_1.add("Duster");
                                model_1.add("Fluence");
                                model_1.add("KWID");
                                model_1.add("Other Model");
                            }else if (selected_brand == 56){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 57){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 58){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 59){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Other Model");
                            }else if (selected_brand == 60){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Arona");
                                model_1.add("Ateca");
                                model_1.add("Ibiza");
                                model_1.add("Leon");
                                model_1.add("Other Model");
                            }else if (selected_brand == 61){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Cities");
                                model_1.add("Fabia");
                                model_1.add("Karoq");
                                model_1.add("Kodiaq");
                                model_1.add("Laura");
                                model_1.add("Octavia");
                                model_1.add("Rapid");
                                model_1.add("Superb");
                                model_1.add("Yeti");
                                model_1.add("Other Model");
                            }else if (selected_brand == 62){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Elecric");
                                model_1.add("Forfour");
                                model_1.add("Fortwo");
                                model_1.add("Roadster");
                                model_1.add("Other Model");
                            }else if (selected_brand == 63){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Actyon");
                                model_1.add("Chairman");
                                model_1.add("Korando");
                                model_1.add("Kyron");
                                model_1.add("Musso");
                                model_1.add("Rexton");
                                model_1.add("Rodius");
                                model_1.add("Tivoli");
                                model_1.add("XLV");
                                model_1.add("Other Model");
                            }else if (selected_brand == 64){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Ascent");
                                model_1.add("BRZ");
                                model_1.add("Crosstrek");
                                model_1.add("Forester");
                                model_1.add("Impreza");
                                model_1.add("Legacy");
                                model_1.add("Levorg");
                                model_1.add("R2");
                                model_1.add("STI");
                                model_1.add("Stella");
                                model_1.add("Trezia");
                                model_1.add("WRX");
                                model_1.add("XV");
                                model_1.add("Other Model");
                            }else if (selected_brand == 65){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("A-Star");
                                model_1.add("Alto");
                                model_1.add("Baleno");
                                model_1.add("Celerio");
                                model_1.add("Ciaz");
                                model_1.add("Cultus");
                                model_1.add("Dzire");
                                model_1.add("Ertiga");
                                model_1.add("Escudo");
                                model_1.add("Esteem");
                                model_1.add("Estilo");
                                model_1.add("Grand Vitara");
                                model_1.add("Hustler");
                                model_1.add("Ignis");
                                model_1.add("Jimny");
                                model_1.add("Kizashi");
                                model_1.add("Liana");
                                model_1.add("Maruti");
                                model_1.add("S-Cross");
                                model_1.add("SX4");
                                model_1.add("Solio");
                                model_1.add("Solis");
                                model_1.add("Spacia");
                                model_1.add("Splash");
                                model_1.add("Swift");
                                model_1.add("Twin");
                                model_1.add("Vitara");
                                model_1.add("Wagon R");
                                model_1.add("Wagon R FX");
                                model_1.add("Wagon R FZ");
                                model_1.add("Wagon R Stingray");
                                model_1.add("XBee");
                                model_1.add("Zen");
                                model_1.add("Other Model");
                            }else if (selected_brand == 66){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Aria");
                                model_1.add("Bolt");
                                model_1.add("GenX Nano");
                                model_1.add("Hexa");
                                model_1.add("Indica");
                                model_1.add("Indigo");
                                model_1.add("Nano");
                                model_1.add("Nexon");
                                model_1.add("Safari");
                                model_1.add("Sumo");
                                model_1.add("Telcolin");
                                model_1.add("Tiago");
                                model_1.add("Tigor");
                                model_1.add("Vista");
                                model_1.add("Xenon");
                                model_1.add("Zest");
                                model_1.add("Other Vehicle");
                            }else if (selected_brand == 67){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Model 3");
                                model_1.add("Model S");
                                model_1.add("Model X");
                                model_1.add("Roadster");
                                model_1.add("Other Model");
                            }else if (selected_brand == 68){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("4Runner");
                                model_1.add("Allex");
                                model_1.add("Allion");
                                model_1.add("Alphard");
                                model_1.add("Altezza");
                                model_1.add("Aqua");
                                model_1.add("Aristo");
                                model_1.add("Auris");
                                model_1.add("Avalon");
                                model_1.add("Avanza");
                                model_1.add("Avensis");
                                model_1.add("Axio");
                                model_1.add("Aygo");
                                model_1.add("BB");
                                model_1.add("Belta");
                                model_1.add("Blizzard");
                                model_1.add("Brebis");
                                model_1.add("CH-R");
                                model_1.add("Caldina");
                                model_1.add("Cami");
                                model_1.add("Camry");
                                model_1.add("Carid");
                                model_1.add("Carina");
                                model_1.add("Cast");
                                model_1.add("Celica");
                                model_1.add("Century");
                                model_1.add("Ceres");
                                model_1.add("Chaser");
                                model_1.add("Classic");
                                model_1.add("Comfort");
                                model_1.add("Corolla");
                                model_1.add("Corona");
                                model_1.add("Corsa");
                                model_1.add("Crown");
                                model_1.add("Cynos");
                                model_1.add("Esquire");
                                model_1.add("Etios");
                                model_1.add("FJ Cruiser");
                                model_1.add("Fielder");
                                model_1.add("Fortuner");
                                model_1.add("GT86");
                                model_1.add("Harrier");
                                model_1.add("Hoghlander");
                                model_1.add("Hilux");
                                model_1.add("IST");
                                model_1.add("Land Cruiser Prado");
                                model_1.add("Land Cruiser Sahara");
                                model_1.add("MR-S");
                                model_1.add("Mirai");
                                model_1.add("Passo");
                                model_1.add("Pixis");
                                model_1.add("Platz");
                                model_1.add("Premio");
                                model_1.add("Prius");
                                model_1.add("RAV4");
                                model_1.add("Roomy");
                                model_1.add("Rush");
                                model_1.add("Marino");
                                model_1.add("Squoia");
                                model_1.add("Sienta");
                                model_1.add("Soluna");
                                model_1.add("Sprinter");
                                model_1.add("Startlet");
                                model_1.add("Supra");
                                model_1.add("Tank");
                                model_1.add("Tersel");
                                model_1.add("Vellfire");
                                model_1.add("Vios");
                                model_1.add("Vitz");
                                model_1.add("Voxy");
                                model_1.add("Welfare");
                                model_1.add("wigo");
                                model_1.add("Wish");
                                model_1.add("Yaris");
                                model_1.add("Other Model");
                            }else if (selected_brand == 69){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Corsa");
                                model_1.add("Crossland");
                                model_1.add("Insignia");
                                model_1.add("VXR8");
                                model_1.add("Vectra");
                                model_1.add("Viva");
                                model_1.add("Other Model");
                            }else if (selected_brand == 70){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Atlas");
                                model_1.add("Beetle");
                                model_1.add("Bora");
                                model_1.add("Golf");
                                model_1.add("Jetta");
                                model_1.add("Passat");
                                model_1.add("Polo");
                                model_1.add("T-Roc");
                                model_1.add("Tiguan");
                                model_1.add("UP");
                                model_1.add("VW1300");
                                model_1.add("e-Golf");
                                model_1.add("Other Model");
                            }else if (selected_brand == 71){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("850");
                                model_1.add("940");
                                model_1.add("S40");
                                model_1.add("S60");
                                model_1.add("S80");
                                model_1.add("S90");
                                model_1.add("V40");
                                model_1.add("V50");
                                model_1.add("V60");
                                model_1.add("V70");
                                model_1.add("V90");
                                model_1.add("XC40");
                                model_1.add("XC60");
                                model_1.add("XC70");
                                model_1.add("XC90");
                                model_1.add("Other Model");
                            }else if (selected_brand == 72){
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                model_1.add("Extreme");
                                model_1.add("Nomad");
                                model_1.add("Z100");
                                model_1.add("Other Model");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    brand.setAdapter(brands_adapter);
                    model.setAdapter(models_adapter);
                    body.setAdapter(body_adapter);
                    transmission.setAdapter(transmission_adapter);
                    fuel.setAdapter(fuel_adapter);

                    progress = new ProgressDialog(NewAdsPostActivity.this);

                    district_layout.setVisibility(View.VISIBLE);
                    city_layout.setVisibility(View.VISIBLE);
                    image_layout.setVisibility(View.VISIBLE);
                    //scrollView.setVisibility(View.VISIBLE);
                    image_button_layout.setVisibility(View.VISIBLE);
                    brand_layout.setVisibility(View.VISIBLE);
                    model_layout.setVisibility(View.VISIBLE);
                    trim_layout.setVisibility(View.VISIBLE);
                    model_year_layout.setVisibility(View.VISIBLE);
                    condition_layout.setVisibility(View.VISIBLE);
                    mileage_layout.setVisibility(View.VISIBLE);
                    body_type_layout.setVisibility(View.VISIBLE);
                    transmission_layout.setVisibility(View.VISIBLE);
                    fuel_layout.setVisibility(View.VISIBLE);
                    capacity_layout.setVisibility(View.VISIBLE);
                    description_layout.setVisibility(View.VISIBLE);
                    price_layout.setVisibility(View.VISIBLE);

                    enter_tile.setVisibility(View.VISIBLE);
                    ad_title_text.setVisibility(View.GONE);
                    ad_title.setVisibility(View.GONE);
                    location_text.setVisibility(View.VISIBLE);
                    location.setVisibility(View.VISIBLE);
                    city_text.setVisibility(View.VISIBLE);
                    city.setVisibility(View.VISIBLE);
                    image_description.setVisibility(View.VISIBLE);
                    images.setVisibility(View.VISIBLE);
                    select_image.setVisibility(View.VISIBLE);
                    brand_text.setVisibility(View.VISIBLE);
                    brand.setVisibility(View.VISIBLE);
                    model_text.setVisibility(View.VISIBLE);
                    model.setVisibility(View.VISIBLE);
                    trim_text.setVisibility(View.VISIBLE);
                    trim.setVisibility(View.VISIBLE);
                    model_year_text.setVisibility(View.VISIBLE);
                    model_year.setVisibility(View.VISIBLE);
                    condition_text.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.VISIBLE);
                    new_radio.setVisibility(View.VISIBLE);
                    used_radio.setVisibility(View.VISIBLE);
                    re_condition_radio.setVisibility(View.VISIBLE);
                    mileage_text.setVisibility(View.VISIBLE);
                    mileage.setVisibility(View.VISIBLE);
                    body_type_text.setVisibility(View.VISIBLE);
                    body.setVisibility(View.VISIBLE);
                    transmission_text.setVisibility(View.VISIBLE);
                    transmission.setVisibility(View.VISIBLE);
                    fuel_text.setVisibility(View.VISIBLE);
                    fuel.setVisibility(View.VISIBLE);
                    capacity_text.setVisibility(View.VISIBLE);
                    capacity.setVisibility(View.VISIBLE);
                    description_text.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    price_text.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);
                    your_details_layout.setVisibility(View.VISIBLE);
                    your_description_text.setVisibility(View.VISIBLE);
                    //name_text.setVisibility(View.VISIBLE);
                    //name.setVisibility(View.VISIBLE);
                    contact_text.setVisibility(View.VISIBLE);
                    contact.setVisibility(View.VISIBLE);
                    //enter_contact.setVisibility(View.VISIBLE);
                    save_button.setVisibility(View.VISIBLE);

                }else if (position == 2){
                    isSelectVanBusLorry = true;
                    isVanBusLorryActivity = true;
                    location.setAdapter(district_adapter);
                    city.setAdapter(cities_adapter);
                    brand.setAdapter(brand_2_adapter);

                    //scrollView.setVisibility(View.VISIBLE);
                    district_layout.setVisibility(View.VISIBLE);
                    city_layout.setVisibility(View.VISIBLE);
                    image_layout.setVisibility(View.VISIBLE);
                    image_button_layout.setVisibility(View.VISIBLE);
                    brand_layout.setVisibility(View.VISIBLE);
                    vehicle_type_2_layout.setVisibility(View.VISIBLE);
                    model_year_layout.setVisibility(View.VISIBLE);
                    condition_layout.setVisibility(View.VISIBLE);
                    mileage_layout.setVisibility(View.VISIBLE);
                    capacity_layout.setVisibility(View.VISIBLE);
                    description_layout.setVisibility(View.VISIBLE);
                    price_layout.setVisibility(View.VISIBLE);

                    enter_tile.setVisibility(View.VISIBLE);
                    ad_title_text.setVisibility(View.GONE);
                    ad_title.setVisibility(View.GONE);
                    location_text.setVisibility(View.VISIBLE);
                    location.setVisibility(View.VISIBLE);
                    city_text.setVisibility(View.VISIBLE);
                    city.setVisibility(View.VISIBLE);
                    image_description.setVisibility(View.VISIBLE);
                    images.setVisibility(View.VISIBLE);
                    select_image.setVisibility(View.VISIBLE);
                    brand_text.setVisibility(View.VISIBLE);
                    brand.setVisibility(View.VISIBLE);
                    vehicle_type_text_2.setVisibility(View.VISIBLE);
                    motor_bike_type.setAdapter(van_adapter);
                    motor_bike_type.setVisibility(View.VISIBLE);
                    /*vehicle_type_2.setHint("උදා: වෑන් , බස් හෝ ලොරි");*/
                    model_year_text.setVisibility(View.VISIBLE);
                    model_year.setVisibility(View.VISIBLE);
                    condition_text.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.VISIBLE);
                    new_radio.setVisibility(View.VISIBLE);
                    used_radio.setVisibility(View.VISIBLE);
                    re_condition_radio.setVisibility(View.VISIBLE);
                    mileage_text.setVisibility(View.VISIBLE);
                    mileage.setVisibility(View.VISIBLE);
                    capacity_text.setVisibility(View.VISIBLE);
                    capacity.setVisibility(View.VISIBLE);
                    description_text.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    price_text.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);
                    your_details_layout.setVisibility(View.VISIBLE);
                    your_description_text.setVisibility(View.VISIBLE);
                    //name_text.setVisibility(View.VISIBLE);
                    //name.setVisibility(View.VISIBLE);
                    contact_text.setVisibility(View.VISIBLE);
                    contact.setVisibility(View.VISIBLE);
                    //enter_contact.setVisibility(View.VISIBLE);
                    save_button.setVisibility(View.VISIBLE);

                }else if (position == 3){
                    isSelectMotorBike = true;
                    isMotorbikeScooter = true;
                    location.setAdapter(district_adapter);
                    city.setAdapter(cities_adapter);
                    brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selected_brand = parent.getSelectedItemPosition();
                            model_2.clear();
                            model_2.add("මාදිලිය");
                            if (selected_brand == 1){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Dorsoduro");
                                model_2.add("RS");
                                model_2.add("SR");
                                model_2.add("SXV");
                                model_2.add("Shiver");
                                model_2.add("Storm");
                                model_2.add("Tuono");
                                model_2.add("Other Model");
                            }else if (selected_brand == 2){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Aspire");
                                model_2.add("Avenger");
                                model_2.add("Avenger Cruise");
                                model_2.add("Avenger Street");
                                model_2.add("Boxer");
                                model_2.add("Byk");
                                model_2.add("CT100");
                                model_2.add("Caliber");
                                model_2.add("Discover");
                                model_2.add("Discover 110");
                                model_2.add("Discover 125");
                                model_2.add("Dominar");
                                model_2.add("Kristal");
                                model_2.add("Platina");
                                model_2.add("Pulsar");
                                model_2.add("Pulsar 135");
                                model_2.add("Pulsar 150");
                                model_2.add("Pulsar 180");
                                model_2.add("Pulsar 220F");
                                model_2.add("Pulsar NS160");
                                model_2.add("Pulsar NS200");
                                model_2.add("Pulsar RS200");
                                model_2.add("V12");
                                model_2.add("V15");
                                model_2.add("XCD");
                                model_2.add("Other Model");
                            }else if (selected_brand == 3){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("C600 Sport");
                                model_2.add("C650 GT");
                                model_2.add("F650");
                                model_2.add("F700");
                                model_2.add("F800");
                                model_2.add("G450");
                                model_2.add("G650");
                                model_2.add("K1200");
                                model_2.add("K1300");
                                model_2.add("K1600");
                                model_2.add("R nineT");
                                model_2.add("R1200");
                                model_2.add("S1000");
                                model_2.add("Other Model");
                            }else if (selected_brand == 4){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Other Model");
                            }else if (selected_brand == 5){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("ATM");
                                model_2.add("Civic");
                                model_2.add("D7");
                                model_2.add("DTM");
                                model_2.add("DXT Dart");
                                model_2.add("DZM");
                                model_2.add("Rio");
                                model_2.add("Rino");
                                model_2.add("Savage Supra");
                                model_2.add("Sky Born");
                                model_2.add("Skyline");
                                model_2.add("Transler");
                                model_2.add("Transtar");
                                model_2.add("Tropica");
                                model_2.add("Warrior");
                                model_2.add("Other Model");
                            }else if (selected_brand == 6){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Diavel");
                                model_2.add("Hypermotard");
                                model_2.add("Monster");
                                model_2.add("Multistrada");
                                model_2.add("ST");
                                model_2.add("Scrambler");
                                model_2.add("SportClassics");
                                model_2.add("Superbike");
                                model_2.add("SuperSport");
                                model_2.add("Other Model");
                            }else if (selected_brand == 7){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Alpha");
                                model_2.add("Bravo");
                                model_2.add("City");
                                model_2.add("Classic");
                                model_2.add("ERS 3000");
                                model_2.add("KITO");
                                model_2.add("Runner");
                                model_2.add("Traveller");
                                model_2.add("VIZ");
                                model_2.add("Other Model");
                            }else if (selected_brand == 8){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Other Model");
                            }else if (selected_brand == 9){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Dyna Super Glide");
                                model_2.add("FL");
                                model_2.add("Softail");
                                model_2.add("Sportster");
                                model_2.add("Street");
                                model_2.add("VRSC");
                                model_2.add("Other Model");
                            }else if (selected_brand == 10){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Achiever");
                                model_2.add("CBZ");
                                model_2.add("Dare");
                                model_2.add("Dash");
                                model_2.add("Dawn");
                                model_2.add("Duet");
                                model_2.add("Glamour");
                                model_2.add("HF Dawn");
                                model_2.add("HF Deluxe");
                                model_2.add("Hunk");
                                model_2.add("Ignitor");
                                model_2.add("Karizma");
                                model_2.add("Maestro Edge");
                                model_2.add("Passion Plus");
                                model_2.add("Passion Pro");
                                model_2.add("Pleasure");
                                model_2.add("Splender");
                                model_2.add("Splender Plus");
                                model_2.add("Splender i smart");
                                model_2.add("Super Splender");
                                model_2.add("X Pulse");
                                model_2.add("XF3R");
                                model_2.add("Xtream");
                                model_2.add("Other Model");
                            }else if (selected_brand == 11){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("AX1");
                                model_2.add("Activa");
                                model_2.add("APE");
                                model_2.add("Avitor");
                                model_2.add("Benly");
                                model_2.add("CB 125");
                                model_2.add("CB Hornet");
                                model_2.add("CB Shine");
                                model_2.add("CB Trigger");
                                model_2.add("CB Unicorn");
                                model_2.add("CB4");
                                model_2.add("CBR");
                                model_2.add("CD 110");
                                model_2.add("CD125");
                                model_2.add("CD200");
                                model_2.add("CD 70");
                                model_2.add("CD Down");
                                model_2.add("CD 90");
                                model_2.add("CM Custom");
                                model_2.add("CRF");
                                model_2.add("Cliq");
                                model_2.add("Dio");
                                model_2.add("DreamFTR");
                                model_2.add("Gold Wing");
                                model_2.add("Forza");
                                model_2.add("Grazia");
                                model_2.add("Hornet");
                                model_2.add("Jade");
                                model_2.add("Livo");
                                model_2.add("MD");
                                model_2.add("Magna");
                                model_2.add("NV400");
                                model_2.add("Navi");
                                model_2.add("PCX");
                                model_2.add("Rebel");
                                model_2.add("Passion");
                                model_2.add("Roadmaster");
                                model_2.add("SL");
                                model_2.add("Stunner");
                                model_2.add("Super Club");
                                model_2.add("Twister");
                                model_2.add("X-Blade");
                                model_2.add("XLR");
                                model_2.add("XR");
                                model_2.add("VTR");
                                model_2.add("Zoomer");
                                model_2.add("Other Model");
                            }else if (selected_brand == 12){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Balius");
                                model_2.add("D Tracker");
                                model_2.add("Eliminator");
                                model_2.add("Estrella");
                                model_2.add("GPZ");
                                model_2.add("KDX");
                                model_2.add("KLX");
                                model_2.add("KX");
                                model_2.add("Ninja");
                                model_2.add("Versys");
                                model_2.add("Vulcan S");
                                model_2.add("Z");
                                model_2.add("Other Model");
                            }else if (selected_brand == 13){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("4S");
                                model_2.add("Blaze");
                                model_2.add("Boxer");
                                model_2.add("GF");
                                model_2.add("Safari");
                                model_2.add("Stryker");
                                model_2.add("Other Model");
                            }else if (selected_brand == 14){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("250");
                                model_2.add("390");
                                model_2.add("690");
                                model_2.add("790");
                                model_2.add("Duke");
                                model_2.add("Duke 200");
                                model_2.add("RC");
                                model_2.add("RC 200");
                                model_2.add("Other Model");
                            }else if (selected_brand == 15){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("90");
                                model_2.add("CD");
                                model_2.add("LD");
                                model_2.add("LX");
                                model_2.add("Super");
                                model_2.add("Other Model");
                            }else if (selected_brand == 16){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Centuro");
                                model_2.add("Duro");
                                model_2.add("Gusto");
                                model_2.add("Mojo");
                                model_2.add("Uzo 125");
                                model_2.add("Other Model");
                            }else if (selected_brand == 17){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Other Model");
                            }else if (selected_brand == 18){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Other Model");
                            }else if (selected_brand == 19){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Other Model");
                            }else if (selected_brand == 20){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Bullet");
                                model_2.add("Classic");
                                model_2.add("Classic");
                                model_2.add("Himalayan");
                                model_2.add("Interceptor");
                                model_2.add("Machismo");
                                model_2.add("Thunderbird");
                                model_2.add("Other Model");
                            }else if (selected_brand == 21){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Other Model");
                            }else if (selected_brand == 22){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("AX100");
                                model_2.add("Access");
                                model_2.add("Bandit");
                                model_2.add("DR");
                                model_2.add("DRZ");
                                model_2.add("Djebel");
                                model_2.add("GN 125");
                                model_2.add("GN 250");
                                model_2.add("GS 125");
                                model_2.add("Gixxer");
                                model_2.add("Grass Tracker");
                                model_2.add("Intruder");
                                model_2.add("Lets");
                                model_2.add("SX");
                                model_2.add("Volty");
                                model_2.add("");
                                model_2.add("Other Model");
                            }else if (selected_brand == 23){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Other Model");
                            }else if (selected_brand == 24){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Apache");
                                model_2.add("Centra");
                                model_2.add("Creon");
                                model_2.add("Flame");
                                model_2.add("Jupiter");
                                model_2.add("Metro");
                                model_2.add("Ntotq");
                                model_2.add("Scooty Pep");
                                model_2.add("Scooty Pep+");
                                model_2.add("Scooty Pept");
                                model_2.add("Scooty Zest");
                                model_2.add("Sport");
                                model_2.add("Star City Plus");
                                model_2.add("Star Sport");
                                model_2.add("Streak");
                                model_2.add("Victor");
                                model_2.add("Wego");
                                model_2.add("XL 100");
                                model_2.add("XL Super");
                                model_2.add("Zest");
                                model_2.add("Other Model");
                            }else if (selected_brand == 25){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Hlegante");
                                model_2.add("LX");
                                model_2.add("SXL");
                                model_2.add("VXL");
                                model_2.add("Other Model");
                            }else if (selected_brand == 26){
                                model_2.clear();
                                model_2.add("මාදිලිය");
                                model_2.add("Alpha");
                                model_2.add("DT");
                                model_2.add("Enticer");
                                model_2.add("FZ");
                                model_2.add("FZ S");
                                model_2.add("FZ25");
                                model_2.add("Facino");
                                model_2.add("Fascino");
                                model_2.add("Fazer");
                                model_2.add("Gladiator");
                                model_2.add("JGR");
                                model_2.add("Libero");
                                model_2.add("Mate");
                                model_2.add("MT 06");
                                model_2.add("Mt 09");
                                model_2.add("Majesty");
                                model_2.add("N Max");
                                model_2.add("R1");
                                model_2.add("R15");
                                model_2.add("R6");
                                model_2.add("Ray");
                                model_2.add("Ray ZR");
                                model_2.add("SZ-RR");
                                model_2.add("Saluto");
                                model_2.add("TTR");
                                model_2.add("TW");
                                model_2.add("TZR");
                                model_2.add("Virago");
                                model_2.add("WR");
                                model_2.add("WRF");
                                model_2.add("WRZ");
                                model_2.add("YZ");
                                model_2.add("YZF");
                                model_2.add("Other Model");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    brand.setAdapter(brand_3_adapter);
                    model.setAdapter(model_2_adapter);

                    //scrollView.setVisibility(View.VISIBLE);
                    vehicle_type_2_layout.setVisibility(View.VISIBLE);
                    district_layout.setVisibility(View.VISIBLE);
                    city_layout.setVisibility(View.VISIBLE);
                    image_layout.setVisibility(View.VISIBLE);
                    image_button_layout.setVisibility(View.VISIBLE);
                    brand_layout.setVisibility(View.VISIBLE);
                    model_layout.setVisibility(View.VISIBLE);
                    trim_layout.setVisibility(View.VISIBLE);
                    model_year_layout.setVisibility(View.VISIBLE);
                    condition_layout.setVisibility(View.VISIBLE);
                    mileage_layout.setVisibility(View.VISIBLE);
                    capacity_layout.setVisibility(View.VISIBLE);
                    description_layout.setVisibility(View.VISIBLE);
                    price_layout.setVisibility(View.VISIBLE);

                    enter_tile.setVisibility(View.VISIBLE);
                    ad_title_text.setVisibility(View.GONE);
                    ad_title.setVisibility(View.GONE);
                    location_text.setVisibility(View.VISIBLE);
                    location.setVisibility(View.VISIBLE);
                    city_text.setVisibility(View.VISIBLE);
                    city.setVisibility(View.VISIBLE);
                    image_description.setVisibility(View.VISIBLE);
                    images.setVisibility(View.VISIBLE);
                    select_image.setVisibility(View.VISIBLE);
                    vehicle_type_text_2.setVisibility(View.VISIBLE);
                    motor_bike_type.setAdapter(bike_adapter);
                    motor_bike_type.setVisibility(View.VISIBLE);
                    brand_text.setVisibility(View.VISIBLE);
                    brand.setVisibility(View.VISIBLE);
                    model_text.setVisibility(View.VISIBLE);
                    model.setVisibility(View.VISIBLE);
                    trim_text.setVisibility(View.VISIBLE);
                    trim.setVisibility(View.VISIBLE);
                    model_year_text.setVisibility(View.VISIBLE);
                    model_year.setVisibility(View.VISIBLE);
                    condition_text.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.VISIBLE);
                    new_radio.setVisibility(View.VISIBLE);
                    used_radio.setVisibility(View.VISIBLE);
                    re_condition_radio.setVisibility(View.VISIBLE);
                    mileage_text.setVisibility(View.VISIBLE);
                    mileage.setVisibility(View.VISIBLE);
                    capacity_text.setVisibility(View.VISIBLE);
                    capacity.setVisibility(View.VISIBLE);
                    description_text.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    price_text.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);
                    your_details_layout.setVisibility(View.VISIBLE);
                    your_description_text.setVisibility(View.VISIBLE);
                    //name_text.setVisibility(View.VISIBLE);
                    //name.setVisibility(View.VISIBLE);
                    contact_text.setVisibility(View.VISIBLE);
                    contact.setVisibility(View.VISIBLE);
                    //enter_contact.setVisibility(View.VISIBLE);
                    save_button.setVisibility(View.VISIBLE);

                }else if (position == 4){
                    isSelectOtherVehicle = true;
                    isOtherVehicleActivity = true;
                    location.setAdapter(district_adapter);
                    city.setAdapter(cities_adapter);
                    vehicle_type.setAdapter(vehicle_type_adapter);

                    //scrollView.setVisibility(View.VISIBLE);
                    district_layout.setVisibility(View.VISIBLE);
                    city_layout.setVisibility(View.VISIBLE);
                    image_layout.setVisibility(View.VISIBLE);
                    image_button_layout.setVisibility(View.VISIBLE);
                    title_layout.setVisibility(View.VISIBLE);
                    brand_layout.setVisibility(View.VISIBLE);
                    vehicle_type_1_layout.setVisibility(View.VISIBLE);
                    model_year_layout.setVisibility(View.VISIBLE);
                    condition_layout.setVisibility(View.VISIBLE);
                    mileage_layout.setVisibility(View.VISIBLE);
                    capacity_layout.setVisibility(View.VISIBLE);
                    description_layout.setVisibility(View.VISIBLE);
                    price_layout.setVisibility(View.VISIBLE);

                    enter_tile.setVisibility(View.VISIBLE);
                    ad_title_text.setVisibility(View.GONE);
                    ad_title.setVisibility(View.GONE);
                    location_text.setVisibility(View.VISIBLE);
                    location.setVisibility(View.VISIBLE);
                    city_text.setVisibility(View.VISIBLE);
                    city.setVisibility(View.VISIBLE);
                    image_description.setVisibility(View.VISIBLE);
                    images.setVisibility(View.VISIBLE);
                    select_image.setVisibility(View.VISIBLE);
                    vehicle_type_text_1.setVisibility(View.VISIBLE);
                    vehicle_type.setVisibility(View.VISIBLE);
                    title_text.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);
                    model_year_text.setVisibility(View.VISIBLE);
                    model_year.setVisibility(View.VISIBLE);
                    condition_text.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.VISIBLE);
                    new_radio.setVisibility(View.VISIBLE);
                    used_radio.setVisibility(View.VISIBLE);
                    re_condition_radio.setVisibility(View.VISIBLE);
                    mileage_text.setVisibility(View.VISIBLE);
                    mileage.setVisibility(View.VISIBLE);
                    description_text.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    price_text.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);
                    your_details_layout.setVisibility(View.VISIBLE);
                    your_description_text.setVisibility(View.VISIBLE);
                    //name_text.setVisibility(View.GONE);
                    //name.setVisibility(View.GONE);
                    contact_text.setVisibility(View.VISIBLE);
                    contact.setVisibility(View.VISIBLE);
                    //enter_contact.setVisibility(View.VISIBLE);
                    save_button.setVisibility(View.VISIBLE);

                }else if (position == 5){
                    isSelectService = true;
                    isSpare_part_Service = true;

                    ad_title_text.setVisibility(View.GONE);
                    service_spinner.setAdapter(service_adapter);
                    getService_layout.setVisibility(View.VISIBLE);
                    service_station_name_layout.setVisibility(View.VISIBLE);
                    getService_station_address_layout.setVisibility(View.VISIBLE);
                    ad_title.setVisibility(View.GONE);

                    your_details_layout.setVisibility(View.VISIBLE);
                    your_description_text.setVisibility(View.VISIBLE);
                    //name_text.setVisibility(View.GONE);
                    //name.setVisibility(View.GONE);
                    contact_text.setVisibility(View.VISIBLE);
                    contact.setVisibility(View.VISIBLE);
                    //enter_contact.setVisibility(View.VISIBLE);
                    save_button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){
            user_name = account.getDisplayName();
            user_Uid = account.getId();
        }
        /*LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        images.setLayoutManager(layoutManager);
        uploadListAdapter = new UploadListAdapter(this,image_Urls);
        images.setAdapter(uploadListAdapter);*/

        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        condition = findViewById(selectedId);
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (askForPermission())
                    showChooser();
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectCar){
                    if (!model_year.getText().toString().isEmpty()
                            && !mileage.getText().toString().isEmpty()
                            && !price.getText().toString().isEmpty()
                            && !contact.getText().toString().isEmpty()
                            && !capacity.getText().toString().isEmpty()){
                        if (Integer.parseInt(model_year.getText().toString()) <= Calendar.getInstance().get(Calendar.YEAR)
                                && Integer.parseInt(capacity.getText().toString()) > 0
                                && Integer.parseInt(capacity.getText().toString()) <= 5000){
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            //final int condition_sp = getIntent().getIntExtra("condition",0);
                            // find the radiobutton by returned id
                            condition = findViewById(selectedId);
                            if (!arrayList.isEmpty()){
                                /*upLoadCar(location,city,image_urls,brand,model,trim,model_year,condition.getText().toString(),mileage,body_adapter.getList(body.getSelectedItemPosition()).getText(),
                                        transmission,fuel,capacity,description,price,contact,android_id,"Car",body);*/
                                uploadImagesToServer();
                            }else {
                                final AlertDialog.Builder alert = new AlertDialog.Builder(NewAdsPostActivity.this);
                                alert.setTitle(R.string.app_name);
                                alert.setIcon(R.mipmap.ic_icon);
                                alert.setMessage("කරුණාකර ඔබ තෝරා ගත් ඡායා රෑප උඩුගත කරන්න !");
                                alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                alert.create().show();
                            }
                        }else {
                            if (Integer.parseInt(capacity.getText().toString()) < 0
                                    || Integer.parseInt(capacity.getText().toString()) >= 5000){
                                capacity.setError("කරුණාකර වලංගු අගයක් ඇතුලත් කරන්න");
                            }else if (Integer.parseInt(model_year.getText().toString()) >= Calendar.getInstance().get(Calendar.YEAR)){
                                model_year.setError("කරුණාකර වලංගු අවුරුද්දක් ඇතුළත් කරන්න");
                            }
                        }
                    }else {
                        if (model_year.getText().toString().isEmpty()){
                            model_year.setError("වසර හිස් විය නොහැකියි !");
                        }else if(capacity.getText().toString().isEmpty()){
                            capacity.setError("ධාරිතාවය හිස් විය නොහැක!");
                        }else if (contact.getText().toString().isEmpty()){
                            contact.setError("දුරකථන අංකය හිස් විය නොහැක!");
                        }else if (price.getText().toString().isEmpty()){
                            price.setError("මිල හිස් විය නොහැක!");
                        }else {
                            mileage.setError("ධාවනය කර ඇති දුර හිස් විය නොහැක!");
                        }
                    }
                }if (isSelectVanBusLorry){
                    if (!model_year.getText().toString().isEmpty()
                            && !mileage.getText().toString().isEmpty()
                            && !price.getText().toString().isEmpty()
                            && !contact.getText().toString().isEmpty()
                            && !capacity.getText().toString().isEmpty()){
                        if(Integer.parseInt(model_year.getText().toString()) <= Calendar.getInstance().get(Calendar.YEAR)
                                && Integer.parseInt(capacity.getText().toString()) > 0
                                && Integer.parseInt(capacity.getText().toString()) <= 5000){
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            //final int condition_sp = getIntent().getIntExtra("condition",0);
                            // find the radiobutton by returned id
                            condition = findViewById(selectedId);
                            if (!arrayList.isEmpty()){
                                /*upLoadVanBusLorry(location,city,image_urls,brand,model_year,condition.getText().toString(),
                                        mileage,capacity,description,price,contact,android_id,motor_bike_type);*/
                                uploadImagesToServer();
                            }else {
                                final AlertDialog.Builder alert = new AlertDialog.Builder(NewAdsPostActivity.this);
                                alert.setTitle(R.string.app_name);
                                alert.setIcon(R.mipmap.ic_icon);
                                alert.setMessage("කරුණාකර ඔබ තෝරා ගත් ඡායා රෑප උඩුගත කරන්න !");
                                alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                alert.create().show();
                            }
                        }else {
                            if (Integer.parseInt(capacity.getText().toString()) < 0
                                    || Integer.parseInt(capacity.getText().toString()) >= 5000){
                                capacity.setError("කරුණාකර වලංගු අගයක් ඇතුලත් කරන්න");
                            }else if (Integer.parseInt(model_year.getText().toString()) >= Calendar.getInstance().get(Calendar.YEAR)){
                                model_year.setError("කරුණාකර වලංගු අවුරුද්දක් ඇතුළත් කරන්න");
                            }
                        }
                    }else {
                        if (model_year.getText().toString().isEmpty()){
                            model_year.setError("වසර හිස් විය නොහැකියි !");
                        }else if(capacity.getText().toString().isEmpty()){
                            capacity.setError("ධාරිතාවය හිස් විය නොහැක!");
                        }else if (contact.getText().toString().isEmpty()){
                            contact.setError("දුරකථන අංකය හිස් විය නොහැක!");
                        }else if (price.getText().toString().isEmpty()){
                            price.setError("මිල හිස් විය නොහැක!");
                        }else {
                            mileage.setError("ධාවනය කර ඇති දුර හිස් විය නොහැක!");
                        }
                    }
                }if (isSelectMotorBike){
                    if (!model_year.getText().toString().isEmpty()
                            && !mileage.getText().toString().isEmpty()
                            && !price.getText().toString().isEmpty()
                            && !contact.getText().toString().isEmpty()
                            && !capacity.getText().toString().isEmpty()){
                        if (Integer.parseInt(model_year.getText().toString()) <= Calendar.getInstance().get(Calendar.YEAR)
                                && Integer.parseInt(capacity.getText().toString()) > 0
                                && Integer.parseInt(capacity.getText().toString()) <= 5000){
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            //final int condition_sp = getIntent().getIntExtra("condition",0);
                            // find the radiobutton by returned id
                            condition = findViewById(selectedId);
                            if (!arrayList.isEmpty()){
                                /*upLoadMotorbikeScooter(location,city,image_urls,brand,model,model_year,
                                        condition.getText().toString(),mileage,capacity,description,price,
                                        contact,android_id,motor_bike_type,trim);*/
                                uploadImagesToServer();
                            }else {
                                final AlertDialog.Builder alert = new AlertDialog.Builder(NewAdsPostActivity.this);
                                alert.setTitle(R.string.app_name);
                                alert.setIcon(R.mipmap.ic_icon);
                                alert.setMessage("කරුණාකර ඔබ තෝරා ගත් ඡායා රෑප උඩුගත කරන්න !");
                                alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                alert.create().show();
                            }
                        }else {
                            if (Integer.parseInt(capacity.getText().toString()) < 0
                                    || Integer.parseInt(capacity.getText().toString()) >= 5000){
                                capacity.setError("කරුණාකර වලංගු අගයක් ඇතුලත් කරන්න");
                            }else if (Integer.parseInt(model_year.getText().toString()) >= Calendar.getInstance().get(Calendar.YEAR)){
                                model_year.setError("කරුණාකර වලංගු අවුරුද්දක් ඇතුළත් කරන්න");
                            }
                        }
                    }else {
                        if (model_year.getText().toString().isEmpty()){
                            model_year.setError("වසර හිස් විය නොහැකියි !");
                        }else if(capacity.getText().toString().isEmpty()){
                            capacity.setError("ධාරිතාවය හිස් විය නොහැක!");
                        }else if (contact.getText().toString().isEmpty()){
                            contact.setError("දුරකථන අංකය හිස් විය නොහැක!");
                        }else if (price.getText().toString().isEmpty()){
                            price.setError("මිල හිස් විය නොහැක!");
                        }else {
                            mileage.setError("ධාවනය කර ඇති දුර හිස් විය නොහැක!");
                        }
                    }
                }if (isSelectOtherVehicle){
                    if (!model_year.getText().toString().isEmpty()
                            && !mileage.getText().toString().isEmpty()
                            && !price.getText().toString().isEmpty()
                            && !contact.getText().toString().isEmpty()
                            && !title.getText().toString().isEmpty()){
                        if (Integer.parseInt(model_year.getText().toString()) <= Calendar.getInstance().get(Calendar.YEAR)){
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            condition = findViewById(selectedId);
                            //final int condition_sp = getIntent().getIntExtra("condition",0);
                            // find the radiobutton by returned id
                            if (!arrayList.isEmpty()){
                                /*uploadOtherVehicle(location,city,image_urls,vehicle_type,model_year
                                        ,condition.getText().toString(),mileage,description,price,contact,android_id,title);*/
                                uploadImagesToServer();
                            }else {
                                final AlertDialog.Builder alert = new AlertDialog.Builder(NewAdsPostActivity.this);
                                alert.setTitle(R.string.app_name);
                                alert.setIcon(R.mipmap.ic_icon);
                                alert.setMessage("කරුණාකර ඔබ තෝරා ගත් ඡායා රෑප උඩුගත කරන්න !");
                                alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                alert.create().show();
                            }
                        }else {
                            if (Integer.parseInt(capacity.getText().toString()) < 0
                                    || Integer.parseInt(capacity.getText().toString()) >= 5000){
                                capacity.setError("කරුණාකර වලංගු අගයක් ඇතුලත් කරන්න");
                            }
                        }
                    }else {
                        if (model_year.getText().toString().isEmpty()){
                            model_year.setError("වසර හිස් විය නොහැකියි !");
                        }else if(title.getText().toString().isEmpty()){
                            title.setError("මාතෘකාව හිස් විය නොහැකියි !");
                        }else if (contact.getText().toString().isEmpty()){
                            contact.setError("දුරකථන අංකය හිස් විය නොහැක!");
                        }else if (price.getText().toString().isEmpty()){
                            price.setError("මිල හිස් විය නොහැක!");
                        }else {
                            mileage.setError("ධාවනය කර ඇති දුර හිස් විය නොහැක!");
                        }
                    }
                }if (isSelectService){
                    if (!com_name.getText().toString().isEmpty()
                            && !com_address.getText().toString().isEmpty()
                            && !contact.getText().toString().isEmpty()){
                        if (isMultiple){
                        /*if (isParseBoolean){

                            uploadVehicleService(service_spinner,com_address,com_name,contact);
                        }*/
                            uploadVehicleService(service_spinner,com_address,com_name,contact);
                        }else {
                        /*if (isParseBoolean){

                            uploadVehicleService(service_spinner,com_address,com_name,contact);
                        }*/
                            uploadVehicleService(service_spinner,com_address,com_name,contact);
                        }
                    }else {
                        if (com_name.getText().toString().isEmpty()){
                            com_name.setError("සේවා ස්ථානයේ නම හිස් විය නොහැකියි!");
                        }else if(com_address.getText().toString().isEmpty()){
                            com_address.setError("සේවා ස්ථානයේ ලිපිනය හිස් විය නොහැක!");
                        }else if (contact.getText().toString().isEmpty()){
                            contact.setError("දුරකථන අංකය හිස් විය නොහැක!");
                        }
                    }
                }
            }
        });
        arrayList = new ArrayList<>();
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.new_radio_1:
                if(checked)

                    break;
            case R.id.used_radio_1:
                if(checked)
                    break;
            case R.id.recondition_radio_1:
                if(checked)
                    break;
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        if ((progress != null) && progress.isShowing())
            progress.dismiss();
        progress = null;
    }

    private void uploadVehicleService(final Spinner service_type, final EditText address, final EditText name, final EditText contact_num) {
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> {
            private ProgressDialog progress = new ProgressDialog(NewAdsPostActivity.this);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setTitle(getString(R.string.app_name));
                progress.setMessage("දත්ත උඩුගත කරමින් පවති !");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();
            }

            @SuppressLint("WrongThread")
            @Override
            protected String doInBackground(String... strings) {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Constants.BASE_URL_insert_spare_part_service);
                    List<NameValuePair> list = new ArrayList<>();
                    //httpPost.setEntity(new UrlEncodedFormEntity(list,"UTF-8"));
                    list.add(new BasicNameValuePair("service",service_type.getSelectedItem().toString()));
                    list.add(new BasicNameValuePair("contact", "+"+countryCodePicker.getSelectedCountryCode() + contact_num.getText().toString()));
                    list.add(new BasicNameValuePair("address",address.getText().toString()));
                    list.add(new BasicNameValuePair("name",name.getText().toString()));
                    list.add(new BasicNameValuePair("status","FALSE"));
                    httpPost.setEntity(new UrlEncodedFormEntity(list,"UTF-8"));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    return httpClient.execute(httpPost, responseHandler);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Data add successfully!";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if ((progress != null) && progress.isShowing()) {
                    progress.dismiss();
                    alert.create().show();
                }

                service_spinner.setAdapter(service_adapter);
                com_address.setText("");
                com_name.setText("");
                title.setText("");
                contact_num.setText("");

            }
        }
        new Network().execute();
    }
    private void load_districts(){
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> {
            private ProgressDialog progress = new ProgressDialog(NewAdsPostActivity.this);
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
                    HttpGet httpget = new HttpGet(Constants.BASE_URL_get_district);
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
                districtResponses = new ArrayList<>();
                districtResponses = new Gson().fromJson(s,
                        new TypeToken<List<DistrictResponse>>(){
                        }.getType());
                for (int i = 0; i < districtResponses.size(); i++){
                    districts.add(districtResponses.get(i).getName_si());
                }
            }
        }
        new Network().execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent = new Intent(NewAdsPostActivity.this, MainMenuActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NewAdsPostActivity.this, MainMenuActivity.class);
        startActivity(intent);
        this.finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if(data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        int currentItem = 0;
                        while(currentItem < count) {
                            Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
                            //do something with the image (save it to some directory or whatever you need to do with it here)
                            currentItem = currentItem + 1;
                            Log.d("Uri Selected", imageUri.toString());
                            try {
                                // Get the file path from the URI
                                String path = FileUtils.getPath(this, imageUri);
                                Log.d("Multiple File Selected", path);

                                arrayList.add(imageUri);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                                images = findViewById(R.id.v_ad_recycler_view);
                                images.setLayoutManager(layoutManager);
                                uploadListAdapter = new UploadListAdapter(getApplicationContext(),arrayList);
                                images.setAdapter(uploadListAdapter);

                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }
                        }
                    } else if(data.getData() != null) {
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            Log.d("Single File Selected", path);

                            arrayList.add(uri);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                            images = findViewById(R.id.v_ad_recycler_view);
                            images.setLayoutManager(layoutManager);
                            uploadListAdapter = new UploadListAdapter(getApplicationContext(),arrayList);
                            images.setAdapter(uploadListAdapter);

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private boolean askForPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            int hasCallPermission = ContextCompat.checkSelfPermission(NewAdsPostActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                // Ask for permission
                // need to request permission
                if (ActivityCompat.shouldShowRequestPermissionRationale(NewAdsPostActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // explain
                    showMessageOKCancel(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(NewAdsPostActivity.this,
                                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                            Constants.REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    // if denied then working here
                } else {
                    // Request for permission
                    ActivityCompat.requestPermissions(NewAdsPostActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.REQUEST_CODE_ASK_PERMISSIONS);
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
    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, Constants.REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    showChooser();
                } else {
                    // Permission Denied
                    Toast.makeText(NewAdsPostActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NewAdsPostActivity.this);
        final android.support.v7.app.AlertDialog dialog = builder.setMessage("You need to grant access to Read External Storage")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(NewAdsPostActivity.this, android.R.color.holo_blue_light));
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(NewAdsPostActivity.this, android.R.color.holo_red_light));
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
    private void uploadImagesToServer() {
        if (InternetConnection.checkConnection(NewAdsPostActivity.this)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://vahanamobileapp.com/Rayan/Vehicle/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //showProgress();

            // create list of file parts (photo, video, ...)
            final List<MultipartBody.Part> parts = new ArrayList<>();

            // create upload service client
            ApiService service = retrofit.create(ApiService.class);

            if (arrayList != null) {
                // create part for file (photo, video, ...)
                for (int i = 0; i < arrayList.size(); i++) {
                    parts.add(prepareFilePart("image"+i, arrayList.get(i)));
                }
            }

            // create a map of data to pass along
            RequestBody descriptions = createPartFromString("www.vahanamobileapp.com");
            RequestBody size = createPartFromString(""+parts.size());
            RequestBody status = createPartFromString("TRUE");
            RequestBody districts = createPartFromString(location.getSelectedItem().toString());
            RequestBody city_s = createPartFromString(city.getSelectedItem().toString());
            RequestBody vehicle_types  = null;
            RequestBody UID = createPartFromString(android_id);
            RequestBody brands = null;
            RequestBody models = null;
            RequestBody trims = null;
            RequestBody model_years = createPartFromString(model_year.getText().toString());
            RequestBody conditions = createPartFromString(condition.getText().toString());
            RequestBody mileages = createPartFromString(mileage.getText().toString());
            RequestBody bodys = null;
            RequestBody transmissions = null;
            RequestBody fuel_types = null;
            RequestBody capacities = null;
            RequestBody descript = createPartFromString(description.getText().toString());
            RequestBody prices = createPartFromString(price.getText().toString());
            RequestBody contacts = createPartFromString("+"+countryCodePicker.getSelectedCountryCode() + contact.getText().toString());
            RequestBody titles = null;
            if (isSelectCar){
                vehicle_types = createPartFromString("Car");
                brands = createPartFromString(brand.getSelectedItem().toString());
                models = createPartFromString(model.getSelectedItem().toString());
                trims = createPartFromString(trim.getText().toString());
                bodys = createPartFromString(body_adapter.getList(body.getSelectedItemPosition()).getText());
                transmissions = createPartFromString(transmission.getSelectedItem().toString());
                fuel_types = createPartFromString(fuel.getSelectedItem().toString());
                capacities = createPartFromString(capacity.getText().toString());
                titles = createPartFromString(brand.getSelectedItem().toString() + " " + model.getSelectedItem().toString() + " " + model_year.getText().toString());
            }
            if (isSelectVanBusLorry){
                vehicle_types = createPartFromString(vehicle_type.getSelectedItem().toString());
                brands = createPartFromString(brand.getSelectedItem().toString());
                models = createPartFromString("NULL");
                trims = createPartFromString("NULL");
                bodys = createPartFromString("NULL");
                transmissions = createPartFromString("NULL");
                fuel_types = createPartFromString("NULL");
                capacities = createPartFromString(capacity.getText().toString());
                titles = createPartFromString(brand.getSelectedItem().toString() + " " + model_year.getText().toString());
            }
            if (isSelectMotorBike){
                vehicle_types = createPartFromString(motor_bike_type.getSelectedItem().toString());
                brands = createPartFromString(brand.getSelectedItem().toString());
                models = createPartFromString(model.getSelectedItem().toString());
                trims = createPartFromString(trim.getText().toString());
                bodys = createPartFromString("NULL");
                transmissions = createPartFromString("NULL");
                fuel_types = createPartFromString("NULL");
                capacities = createPartFromString(capacity.getText().toString());
                titles = createPartFromString(brand.getSelectedItem().toString() + " " + model.getSelectedItem().toString() + " " + model_year.getText().toString());
            }
            if (isSelectOtherVehicle){
                vehicle_types = createPartFromString(vehicle_type.getSelectedItem().toString());
                brands = createPartFromString("NULL");
                models = createPartFromString("NULL");
                trims = createPartFromString("NULL");
                bodys = createPartFromString("NULL");
                transmissions = createPartFromString("NULL");
                fuel_types = createPartFromString("NULL");
                capacities = createPartFromString("NULL");
                titles = createPartFromString(title.getText().toString());
            }
            // finally, execute the request
            Call<CarResponse> call = service.uploadMultiple(descriptions, size,status,titles,districts,city_s,vehicle_types,UID,brands,
                    models,trims,model_years,conditions
                    ,mileages,bodys,
                    transmissions,fuel_types,capacities,descript,prices,contacts, parts);
            final ProgressDialog progress = new ProgressDialog(NewAdsPostActivity.this);
            progress.setTitle(getString(R.string.app_name));
            progress.setMessage("Data is Uploading !");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
            call.enqueue(new Callback<CarResponse>() {
                @Override
                public void onResponse(@NonNull Call<CarResponse> call, @NonNull Response<CarResponse> response) {
                    //hideProgress();
                    if(response.isSuccessful()) {
                        /*Toast.makeText(EnglishNewPostAdsActivity.this,
                                "Images successfully uploaded!", Toast.LENGTH_SHORT).show();*/
                        progress.dismiss();
                        final AlertDialog.Builder alert = new AlertDialog.Builder(NewAdsPostActivity.this);
                        alert.setTitle("වාහන");
                        alert.setMessage("දැන්වීම භාර දීම සාර්තකයි. ඇඩ්මින් මඩුල්ල අනුමත කළ පසු ඔබගේ දැන්වීම ඇප් එකෙහි පළ වනු ඇත. ස්තූතියි!.");
                        alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.create().show();
                        arrayList.clear();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                        images = findViewById(R.id.v_ad_recycler_view);
                        images.setLayoutManager(layoutManager);
                        uploadListAdapter = new UploadListAdapter(getApplicationContext(),arrayList);
                        images.setAdapter(uploadListAdapter);

                        location.setAdapter(district_adapter);
                        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selected_district = parent.getSelectedItemPosition();
                                cities.clear();
                                cities.add("නගරය තෝරන්න");
                                if (selected_district > 0){
                                    if (selected_district == 1){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("අක්කරපත්තුව");cities.add("කල්මුනේ");cities.add("අම්පාර");cities.add("සායින්දමරුදු");
                                    }else if (selected_district == 2){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("අනුරාධපුර");cities.add("කැකිරාව");cities.add("මැදවච්චිය");cities.add("තඹුත්තේගම");cities.add("එප්පාවල");
                                        cities.add("මිහින්තලේ");cities.add("නොච්චියාගම");cities.add("ගල්නෑව");cities.add("ගලේන්බින්දුනුවැව");cities.add("තලාව");
                                        cities.add("හබරණ");
                                    }else if (selected_district == 3){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("බදුල්ල");cities.add("බණ්ඩාරවෙල");cities.add("වැලිමඩ");cities.add("මහියංගනය");cities.add("ඇල්ල");
                                        cities.add("හාලි ඇළ");cities.add("දියතලාව");cities.add("පස්සර");cities.add("හපුතලේ");
                                    }else if (selected_district == 4){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("මඩකලපුව");

                                    }else if (selected_district == 5){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("නුගේගොඩ");cities.add("දෙහිවල");cities.add("කොළඹ 6");cities.add("පිළියන්දල");
                                        cities.add("කොට්ටාව");cities.add("රාජගිරිය");cities.add("මහරගම");cities.add("හෝමාගම");cities.add("මොරටුව");
                                        cities.add("බොරලැස්ගමුව");cities.add("මාලඹේ");cities.add("කොළඹ 4");cities.add("බත්තරමුල්ල");cities.add("අතුරුගිරිය");cities.add("කොළඹ 10");
                                        cities.add("කඩුවෙල");cities.add("කොළඹ 5");cities.add("කොළඹ 3");cities.add("කොහුවල");
                                        cities.add("ගල්කිස්ස");cities.add("කොළඹ 8");cities.add("කොළඹ 11");cities.add("රත්මලාන");cities.add("කොළඹ 9");
                                        cities.add("පන්නිපිටිය");cities.add("කොළඹ 2");cities.add("කෝට්ටෙ");cities.add("තලවතුගොඩ");cities.add("වැල්ලම්පිටිය");
                                        cities.add("නාවල");cities.add("කොළඹ 15");cities.add("අංගොඩ");cities.add("පාදුක්ක");cities.add("කොලොන්නාව");
                                        cities.add("කොළඹ 13");cities.add("කොළඹ 14");cities.add("කොළඹ 12");cities.add("කොළඹ 7");cities.add("හංවැල්ල");
                                        cities.add("කැස්බෑව");cities.add("අවිස්සාවේල්ල");cities.add("කොළඹ 1");
                                    }else if (selected_district == 6){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("අම්බලන්ගොඩ");cities.add("ඇල්පිටිය");cities.add("හික්කඩුව");cities.add("බද්දේගම");
                                        cities.add("කරාපිටිය");cities.add("බෙන්තොට");cities.add("අහංගම");cities.add("බටපොළ");
                                        cities.add("ගාල්ල");
                                    }else if (selected_district == 7){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("කැළණිය ");cities.add("කඩවත");cities.add("කිරිබත්ගොඩ");cities.add("ජා-ඇල");cities.add("වත්තල");
                                        cities.add("නිට්ටඔුව");cities.add("මිනුවන්ගොඩ");cities.add("කටුනායක");
                                        cities.add("කඳාන");cities.add("රාගම");cities.add("දෙල්ගොඩ");cities.add("දිවුලපිටිය");cities.add("මීරිගම");
                                        cities.add("වේයන්ගොඩ");cities.add("ගනේමුල්ල");
                                    }else if (selected_district == 8){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("තංගල්ල");cities.add("බෙලිඅත්ත");cities.add("අම්බලන්තොට");cities.add("තිස්සමහාරාම");
                                        cities.add("හම්බන්තොට");
                                    }else if (selected_district == 9){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("යාපනය");cities.add("නල්ලුර්");cities.add("චාවකච්චේරි");
                                    }else if (selected_district == 10){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("හොරණ");cities.add("කළුතර");cities.add("පානදුර");cities.add("බණ්ඩාරගම");
                                        cities.add("මතුගම");cities.add("වාද්දුව");cities.add("අලුත්ගම");cities.add("බේරුවල ");
                                        cities.add("ඉංගිරිය");
                                    }else if (selected_district == 11){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("නුවර");cities.add("ගම්පොල");cities.add("පුඡාපිටිය");
                                        cities.add("තුම්පනේ");cities.add("අකුරණ");cities.add("පාතදුම්බර");
                                        cities.add("පන්විල");cities.add("උඩදුම්බර");cities.add("මිනිපෙ");
                                        cities.add("කුන්ඩසාලෙ");cities.add("හාරිස්පත්තුව");

                                    }else if (selected_district == 12){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("කෑගල්ල");cities.add("මාවනැල්ල");cities.add("වරකාපොළ ");cities.add("රඔුක්කන");cities.add("රැවන්වැල්ල");cities.add("ගලිගමුව");
                                        cities.add("දෙහිඕවිට");cities.add("යටියන්තොට");cities.add("දෙරණියගල");cities.add("කිතුල්ගල");
                                    }else if (selected_district == 13){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("කිළිනොච්චිය");
                                    }else if (selected_district == 14){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("කුරෑණෑගල");cities.add("කුලියාපිටිය");cities.add("පන්නල");cities.add("නාරම්මල");
                                        cities.add("වාරියපොළ");cities.add("මාවතගම");cities.add("පොල්ගහවෙල");cities.add("ඉබ්බාගමුව");
                                        cities.add("අලව්ව");cities.add("ගිරිඋල්ල");cities.add("හෙට්ටිපොළ");cities.add("නිකවැරටිය");
                                        cities.add("බිංගිරිය");cities.add("ගල්ගමුව");
                                    }else if (selected_district == 15){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("මන්නාරම");
                                    }else if (selected_district == 16){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("මාතලේ");cities.add("දඔුල්ල");cities.add("ගලේවෙල");cities.add("උකුවෙල");
                                        cities.add("සීගිරිය");cities.add("රත්තොට");cities.add("පලපත්වෙල");cities.add("යටවත්ත");

                                    }else if (selected_district == 17){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("මාතර");cities.add("අකුරැස්ස");cities.add("වැලිගම");
                                        cities.add("හක්මන");cities.add("දික්වැල්ල");cities.add("කඔුරැපිටිය");
                                        cities.add("දෙනියාය");cities.add("කඔුරැගමුව");cities.add("කැකනදුර");

                                    }else if (selected_district == 18){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("මොණරාගල");cities.add("බිබිල");cities.add("වැල්ලවාය");cities.add("බුත්තල");cities.add("කතරගම");

                                    }else if (selected_district == 19){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("මුලතිව්");

                                    }else if (selected_district == 20){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("නුවර එළිය");cities.add("හැටන්");cities.add("ගිනිගත්හේන");cities.add("මඬුල්ල");

                                    }else if (selected_district == 21){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("පොළොන්නරුව");cities.add("කඳුරැවෙල");cities.add("හිඟුරක්ගොඩ");cities.add("මැදිරිගිරිය");

                                    }else if (selected_district == 22){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("හලාවත");cities.add("වෙන්නප්පුව");cities.add("පුත්තලම");
                                        cities.add("නාත්තන්ඩිය");cities.add("මාරවිල");cities.add("දංකොටුව");

                                    }else if (selected_district == 23){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("රත්නපුරය");cities.add("ඇඹිලිපිටිය");cities.add("බළන්ගොඩ ");
                                        cities.add("පැල්මඩුල්ල");cities.add("ඇහැළියගොඩ");cities.add("කුරුවිට");

                                    }else if (selected_district == 24){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("ත්\u200Dරිකුණාමලය");
                                        cities.add("කින්නියා");

                                    }else if (selected_district == 25){
                                        cities.clear();
                                        cities.add("නගරය තෝරන්න");
                                        cities.add("වව්නියාව");
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        city.setAdapter(cities_adapter);
                        brand.setAdapter(brands_adapter);
                        brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selected_brand = parent.getSelectedItemPosition();
                                model_1.clear();
                                model_1.add("මාදිලිය");
                                if (selected_brand == 1){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Stelvio Quadrifoglio");
                                    model_1.add("Stelvio");
                                    model_1.add("Giulia Q4 Sedan (4WD)");
                                    model_1.add("Stelvio Q4 (4WD) ");
                                    model_1.add("Stelvio RWD ");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 2){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("VANQUISH S");
                                    model_1.add("VANQUISH");
                                    model_1.add("VANQUISH VOLANTE");
                                    model_1.add("VANQUISH ZAGATO");
                                    model_1.add("RAPIDE S");
                                    model_1.add("DB9 GP");
                                    model_1.add("DB11");
                                    model_1.add("V12 VANTAGE S");
                                    model_1.add("V12 VANTAGE S ROADSTER");
                                    model_1.add("V8 VANTAGE S");
                                    model_1.add("LAGONDA TARAF");
                                    model_1.add("VANTAGE GT12");
                                    model_1.add("VANTAGE GT8");
                                    model_1.add("ASTON MARTIN VULCAN");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 3){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("100");
                                    model_1.add("80");
                                    model_1.add("A1");
                                    model_1.add("A3");
                                    model_1.add("A4");
                                    model_1.add("A5");
                                    model_1.add("A6");
                                    model_1.add("A7");
                                    model_1.add("A8");
                                    model_1.add("Q1");
                                    model_1.add("Q2");
                                    model_1.add("Q3");
                                    model_1.add("Q5");
                                    model_1.add("Q7");
                                    model_1.add("R8");
                                    model_1.add("RS3");
                                    model_1.add("RS4");
                                    model_1.add("RS5");
                                    model_1.add("RS6");
                                    model_1.add("S1");
                                    model_1.add("S2");
                                    model_1.add("S3");
                                    model_1.add("S4");
                                    model_1.add("S5");
                                    model_1.add("S6");
                                    model_1.add("S7");
                                    model_1.add("S8");
                                    model_1.add("SQ5");
                                    model_1.add("SQ7");
                                    model_1.add("TT");
                                    model_1.add("TTS");
                                    model_1.add("V8");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 4){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("7");
                                    model_1.add("Cambridge");
                                    model_1.add("Mini Cooper");
                                    model_1.add("Mini");
                                    model_1.add("Standard");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 5){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("116i");
                                    model_1.add("118d");
                                    model_1.add("118i");
                                    model_1.add("120d");
                                    model_1.add("120i");
                                    model_1.add("123d");
                                    model_1.add("125i");
                                    model_1.add("130i");
                                    model_1.add("135i");
                                    model_1.add("218d");
                                    model_1.add("218i");
                                    model_1.add("220d");
                                    model_1.add("220i");
                                    model_1.add("225XE");
                                    model_1.add("225i");
                                    model_1.add("228i");
                                    model_1.add("230i");
                                    model_1.add("252i");
                                    model_1.add("316i");
                                    model_1.add("316ti");
                                    model_1.add("318is");
                                    model_1.add("318ti");
                                    model_1.add("320Ci");
                                    model_1.add("320d");
                                    model_1.add("320i");
                                    model_1.add("323Ci");
                                    model_1.add("323i");
                                    model_1.add("325Ci");
                                    model_1.add("325d");
                                    model_1.add("325e");
                                    model_1.add("325i");
                                    model_1.add("328Ci");
                                    model_1.add("328d");
                                    model_1.add("328i");
                                    model_1.add("330 GT");
                                    model_1.add("330Ci");
                                    model_1.add("330d");
                                    model_1.add("330e");
                                    model_1.add("330i");
                                    model_1.add("335d");
                                    model_1.add("335i");
                                    model_1.add("340 GT");
                                    model_1.add("340i");
                                    model_1.add("420d");
                                    model_1.add("420i");
                                    model_1.add("428 Gran Coupe");
                                    model_1.add("428i");
                                    model_1.add("430 Gran Coupe");
                                    model_1.add("430i");
                                    model_1.add("435 Bran Coupe");
                                    model_1.add("435i");
                                    model_1.add("440 Gran Coupe");
                                    model_1.add("440i");
                                    model_1.add("520d");
                                    model_1.add("520i");
                                    model_1.add("523i");
                                    model_1.add("525e");
                                    model_1.add("525i");
                                    model_1.add("528i");
                                    model_1.add("530d");
                                    model_1.add("530e");
                                    model_1.add("530i");
                                    model_1.add("535 GT");
                                    model_1.add("535d");
                                    model_1.add("535i");
                                    model_1.add("540d");
                                    model_1.add("540i");
                                    model_1.add("545i");
                                    model_1.add("550 GT");
                                    model_1.add("550i");
                                    model_1.add("630i");
                                    model_1.add("633CSi");
                                    model_1.add("635CSi");
                                    model_1.add("635d");
                                    model_1.add("640 GT");
                                    model_1.add("640 Gran Coupe");
                                    model_1.add("640d");
                                    model_1.add("640i");
                                    model_1.add("645ci");
                                    model_1.add("650 Gran Coupe");
                                    model_1.add("650i");
                                    model_1.add("730d");
                                    model_1.add("730iL");
                                    model_1.add("733i");
                                    model_1.add("735Li");
                                    model_1.add("735i");
                                    model_1.add("735iL");
                                    model_1.add("740Le");
                                    model_1.add("740Li");
                                    model_1.add("740d");
                                    model_1.add("740e");
                                    model_1.add("740i");
                                    model_1.add("740iL");
                                    model_1.add("745Li");
                                    model_1.add("745i");
                                    model_1.add("750Li");
                                    model_1.add("750i");
                                    model_1.add("750iL");
                                    model_1.add("760Li");
                                    model_1.add("840Ci");
                                    model_1.add("850Ci");
                                    model_1.add("850i");
                                    model_1.add("ActivateHybrid 3");
                                    model_1.add("ActivateHybrid 5");
                                    model_1.add("ActivateHybrid 37");
                                    model_1.add("ActivateHybrid 740");
                                    model_1.add("ActivateHybrid 750");
                                    model_1.add("ActivateHybrid X6");
                                    model_1.add("Alpina B6");
                                    model_1.add("Alpina B7");
                                    model_1.add("Coupe");
                                    model_1.add("E46");
                                    model_1.add("E60");
                                    model_1.add("E90");
                                    model_1.add("GT");
                                    model_1.add("L7");
                                    model_1.add("M");
                                    model_1.add("M135i");
                                    model_1.add("M140i");
                                    model_1.add("M2");
                                    model_1.add("M235");
                                    model_1.add("M235i");
                                    model_1.add("M240");
                                    model_1.add("M240i");
                                    model_1.add("M3");
                                    model_1.add("M4");
                                    model_1.add("M5");
                                    model_1.add("M535i");
                                    model_1.add("M550");
                                    model_1.add("M6");
                                    model_1.add("M6 Gran Coupe");
                                    model_1.add("M635CSi");
                                    model_1.add("M760");
                                    model_1.add("M760Li");
                                    model_1.add("Mini Cooper");
                                    model_1.add("Model");
                                    model_1.add("X1");
                                    model_1.add("X2");
                                    model_1.add("X3");
                                    model_1.add("X4");
                                    model_1.add("X5");
                                    model_1.add("X5 M");
                                    model_1.add("X5 eDrive");
                                    model_1.add("X6");
                                    model_1.add("X6 M");
                                    model_1.add("Z3");
                                    model_1.add("Z4");
                                    model_1.add("Z4 M");
                                    model_1.add("Z8");
                                    model_1.add("i3");
                                    model_1.add("i5");
                                    model_1.add("i8");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 6){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 7){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 8){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 9){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("A3");
                                    model_1.add("Arrizo");
                                    model_1.add("E3");
                                    model_1.add("E5");
                                    model_1.add("Fulwin");
                                    model_1.add("QQ");
                                    model_1.add("QQ3");
                                    model_1.add("Tiggo");
                                    model_1.add("X1");
                                    model_1.add("eQ");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 10){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Aveo");
                                    model_1.add("Beat");
                                    model_1.add("Bolt");
                                    model_1.add("Camaro");
                                    model_1.add("Captiva");
                                    model_1.add("Colorado");
                                    model_1.add("Corvette");
                                    model_1.add("Cruze");
                                    model_1.add("Malibu");
                                    model_1.add("Silverado");
                                    model_1.add("Sonic");
                                    model_1.add("Spark");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 11){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("200");
                                    model_1.add("300");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 12){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 13){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Espero");
                                    model_1.add("Lanos");
                                    model_1.add("Leganza");
                                    model_1.add("Magnus");
                                    model_1.add("Nubira");
                                    model_1.add("Tacuma");
                                    model_1.add("Tico");
                                    model_1.add("Tosca");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 14){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Altis");
                                    model_1.add("Atrai Wagon");
                                    model_1.add("Boon");
                                    model_1.add("Canbus");
                                    model_1.add("Cast Activa");
                                    model_1.add("Charade");
                                    model_1.add("Charmant");
                                    model_1.add("Copen");
                                    model_1.add("Cuore");
                                    model_1.add("Esse");
                                    model_1.add("F50");
                                    model_1.add("Hijet");
                                    model_1.add("Leeza");
                                    model_1.add("Mebius");
                                    model_1.add("Mira");
                                    model_1.add("Move");
                                    model_1.add("Redigo");
                                    model_1.add("Rocky");
                                    model_1.add("Tanto");
                                    model_1.add("Terios");
                                    model_1.add("Thor");
                                    model_1.add("Wake");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 15){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Cross");
                                    model_1.add("Go");
                                    model_1.add("Go Plus");
                                    model_1.add("Mi-Do");
                                    model_1.add("On-Do");
                                    model_1.add("Redi Go");
                                    model_1.add("Tanto");
                                    model_1.add("Terios");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 16){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Glory");
                                    model_1.add("V27");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 17){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 18){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 19){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("1100");
                                    model_1.add("500");
                                    model_1.add("Bravo");
                                    model_1.add("Fullback");
                                    model_1.add("Linea");
                                    model_1.add("Palio");
                                    model_1.add("Panda");
                                    model_1.add("Punto");
                                    model_1.add("Sedici");
                                    model_1.add("Tipo");
                                    model_1.add("Uno");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 20){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("C-Max");
                                    model_1.add("Ecosport");
                                    model_1.add("Edge");
                                    model_1.add("Escape");
                                    model_1.add("Everest");
                                    model_1.add("Expedition");
                                    model_1.add("Explorer");
                                    model_1.add("F-150");
                                    model_1.add("Festiva");
                                    model_1.add("Fiesta");
                                    model_1.add("Flex");
                                    model_1.add("Focus");
                                    model_1.add("Fusion");
                                    model_1.add("GT");
                                    model_1.add("Ka+");
                                    model_1.add("Kuga");
                                    model_1.add("Laser");
                                    model_1.add("Mondeo");
                                    model_1.add("Mustang");
                                    model_1.add("Raptor Ranger");
                                    model_1.add("Super Duty");
                                    model_1.add("Taurus");
                                    model_1.add("Transit");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 21){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 22){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Acadia");
                                    model_1.add("Canyon");
                                    model_1.add("Envoy");
                                    model_1.add("Sierra");
                                    model_1.add("Terrain");
                                    model_1.add("Yukon");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 23){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 24){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Accord");
                                    model_1.add("Airwave");
                                    model_1.add("Amaze");
                                    model_1.add("Ballade");
                                    model_1.add("Beat");
                                    model_1.add("CRV");
                                    model_1.add("CRZ");
                                    model_1.add("City");
                                    model_1.add("Civic");
                                    model_1.add("Clarity");
                                    model_1.add("Concerto");
                                    model_1.add("FR-V");
                                    model_1.add("Fit");
                                    model_1.add("Fit Area");
                                    model_1.add("Fit She's");
                                    model_1.add("Fit Shuttle");
                                    model_1.add("Freed");
                                    model_1.add("Grace");
                                    model_1.add("HR-V");
                                    model_1.add("Insight");
                                    model_1.add("Inspire");
                                    model_1.add("Integra");
                                    model_1.add("Jade");
                                    model_1.add("Jazz");
                                    model_1.add("Legend");
                                    model_1.add("Logo");
                                    model_1.add("N-Box");
                                    model_1.add("N-One");
                                    model_1.add("N-WGN");
                                    model_1.add("NSX");
                                    model_1.add("Odyssey");
                                    model_1.add("Pilot");
                                    model_1.add("Ridgeline");
                                    model_1.add("S2000");
                                    model_1.add("S660");
                                    model_1.add("Spike");
                                    model_1.add("Step Wagon");
                                    model_1.add("Vezel");
                                    model_1.add("WR-V");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 25){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("H1");
                                    model_1.add("H2");
                                    model_1.add("H3");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 26){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Accent");
                                    model_1.add("Atos");
                                    model_1.add("Azera");
                                    model_1.add("Coupe");
                                    model_1.add("Elantra");
                                    model_1.add("Eon");
                                    model_1.add("Excel");
                                    model_1.add("Genesis");
                                    model_1.add("Getz");
                                    model_1.add("Grand i10");
                                    model_1.add("loniq");
                                    model_1.add("Kona");
                                    model_1.add("Lantra");
                                    model_1.add("Matrix");
                                    model_1.add("Mistra");
                                    model_1.add("Nexo");
                                    model_1.add("Santa Fe");
                                    model_1.add("Santro");
                                    model_1.add("Sonata");
                                    model_1.add("Stellar");
                                    model_1.add("Terracan");
                                    model_1.add("Trajet");
                                    model_1.add("Tucson");
                                    model_1.add("Veloster");
                                    model_1.add("i20");
                                    model_1.add("i30");
                                    model_1.add("i40");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 27){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Bighorn");
                                    model_1.add("D-Max");
                                    model_1.add("Gemini");
                                    model_1.add("MU-7");
                                    model_1.add("MU-X");
                                    model_1.add("Panther");
                                    model_1.add("Rodeo");
                                    model_1.add("Smart cab");
                                    model_1.add("Trooper");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 28){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("E-Pace");
                                    model_1.add("F-Pace");
                                    model_1.add("F-Type");
                                    model_1.add("I-Pace");
                                    model_1.add("S-Type");
                                    model_1.add("X-Type");
                                    model_1.add("XE");
                                    model_1.add("XF");
                                    model_1.add("XJ");
                                    model_1.add("XK");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 29){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Cherokee");
                                    model_1.add("Compass");
                                    model_1.add("Grand Cherokee");
                                    model_1.add("Renegade");
                                    model_1.add("Wrangler");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 30){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Cadenza");
                                    model_1.add("carens");
                                    model_1.add("Carnival");
                                    model_1.add("Ceed");
                                    model_1.add("Cerato");
                                    model_1.add("Clarus");
                                    model_1.add("Forte");
                                    model_1.add("K7");
                                    model_1.add("K9");
                                    model_1.add("K900");
                                    model_1.add("Mentor");
                                    model_1.add("Niro");
                                    model_1.add("Optima");
                                    model_1.add("Picanto");
                                    model_1.add("Rio");
                                    model_1.add("Rondo");
                                    model_1.add("Sedona");
                                    model_1.add("Sephia");
                                    model_1.add("Sorento");
                                    model_1.add("Spectra");
                                    model_1.add("Sportage");
                                    model_1.add("Stinger");
                                    model_1.add("Stonic");
                                    model_1.add("saul");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 31){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 32){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Defender");
                                    model_1.add("Discovery");
                                    model_1.add("Discovery Sport");
                                    model_1.add("Freelander");
                                    model_1.add("Range Rover");
                                    model_1.add("Range Rover Evoque");
                                    model_1.add("Range Rover PHEV");
                                    model_1.add("Range Rover Sport");
                                    model_1.add("Range Rover Velar");
                                    model_1.add("SV Coupe");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 33){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("CT-200H");
                                    model_1.add("ES");
                                    model_1.add("HS250H");
                                    model_1.add("LS400");
                                    model_1.add("LX450");
                                    model_1.add("Land Cruiser");
                                    model_1.add("NX");
                                    model_1.add("NX300H");
                                    model_1.add("RX350");
                                    model_1.add("RX400");
                                    model_1.add("UX");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 34){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 35){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Bolero");
                                    model_1.add("KUV100");
                                    model_1.add("Legend");
                                    model_1.add("Nuvosport");
                                    model_1.add("Quanto");
                                    model_1.add("Scorpio");
                                    model_1.add("TUV300");
                                    model_1.add("Thar");
                                    model_1.add("Verito");
                                    model_1.add("XUV500");
                                    model_1.add("Xylo");
                                    model_1.add("e2o");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 36){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("800");
                                    model_1.add("Alto");
                                    model_1.add("Baleno");
                                    model_1.add("Esteem");
                                    model_1.add("Gypsy");
                                    model_1.add("Omni");
                                    model_1.add("WagonR");
                                    model_1.add("Zen");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 37){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 38){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("2");
                                    model_1.add("2 Skyactive");
                                    model_1.add("3");
                                    model_1.add("5");
                                    model_1.add("6");
                                    model_1.add("Astina");
                                    model_1.add("Axela");
                                    model_1.add("BT-50");
                                    model_1.add("Butterfly");
                                    model_1.add("CX-3");
                                    model_1.add("CX-5");
                                    model_1.add("CX-6");
                                    model_1.add("CX-7");
                                    model_1.add("CX-8");
                                    model_1.add("CX-9");
                                    model_1.add("Carol");
                                    model_1.add("Demio");
                                    model_1.add("Eunos");
                                    model_1.add("FA4TS");
                                    model_1.add("Familia");
                                    model_1.add("Flair");
                                    model_1.add("MX-5");
                                    model_1.add("Millenia");
                                    model_1.add("RX");
                                    model_1.add("Roadster");
                                    model_1.add("Tribute");
                                    model_1.add("Verisa");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 39){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("190D");
                                    model_1.add("A140");
                                    model_1.add("A150");
                                    model_1.add("A160");
                                    model_1.add("A170");
                                    model_1.add("A180");
                                    model_1.add("A190");
                                    model_1.add("A200");
                                    model_1.add("A210");
                                    model_1.add("A220");
                                    model_1.add("A250");
                                    model_1.add("A45");
                                    model_1.add("B150");
                                    model_1.add("B160");
                                    model_1.add("B170");
                                    model_1.add("B180");
                                    model_1.add("B200");
                                    model_1.add("B220");
                                    model_1.add("B250e");
                                    model_1.add("C160");
                                    model_1.add("C180");
                                    model_1.add("C200");
                                    model_1.add("C220");
                                    model_1.add("C250");
                                    model_1.add("C300");
                                    model_1.add("C350");
                                    model_1.add("CLA 180");
                                    model_1.add("CLA 200");
                                    model_1.add("CLA 250");
                                    model_1.add("CLS");
                                    model_1.add(" E200");
                                    model_1.add("E220");
                                    model_1.add("E240");
                                    model_1.add("E250");
                                    model_1.add("E300");
                                    model_1.add("E350");
                                    model_1.add("E400");
                                    model_1.add("GLA 180");
                                    model_1.add("GLA 200");
                                    model_1.add("GLA 250");
                                    model_1.add("GLC 250");
                                    model_1.add("GLC 300");
                                    model_1.add("GLE 320");
                                    model_1.add("GLE 4OO");
                                    model_1.add("GLE 500");
                                    model_1.add("GLS 400");
                                    model_1.add("GLS 500");
                                    model_1.add("ML250");
                                    model_1.add("ML270");
                                    model_1.add("ML280");
                                    model_1.add("ML300");
                                    model_1.add("ML320");
                                    model_1.add("ML350");
                                    model_1.add("ML420");
                                    model_1.add("ML430");
                                    model_1.add("ML500");
                                    model_1.add("ML55");
                                    model_1.add("ML63");
                                    model_1.add("S300");
                                    model_1.add("S320");
                                    model_1.add("S350");
                                    model_1.add("S500");
                                    model_1.add("S560");
                                    model_1.add("SL 400");
                                    model_1.add("SL 500");
                                    model_1.add("SLC 180");
                                    model_1.add("SLC 200");
                                    model_1.add("SLC 300");
                                    model_1.add("SLK 200");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 40){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("3");
                                    model_1.add("6");
                                    model_1.add("GS");
                                    model_1.add("ZS");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 41){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Actyon");
                                    model_1.add("BAIC");
                                    model_1.add("D20 HACH");
                                    model_1.add("Emgrand");
                                    model_1.add("Geely");
                                    model_1.add("Glory");
                                    model_1.add("Junior");
                                    model_1.add("Korondo");
                                    model_1.add("Kyron");
                                    model_1.add("Lifan");
                                    model_1.add("MX 7");
                                    model_1.add("Panda");
                                    model_1.add("Panda Cross");
                                    model_1.add("Privilage");
                                    model_1.add("Rexton");
                                    model_1.add("Rhino");
                                    model_1.add("Tivoli");
                                    model_1.add("Trend");
                                    model_1.add("Voleex");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 42){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Clubman");
                                    model_1.add("Cooper");
                                    model_1.add("Countryman");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 43){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("4DR");
                                    model_1.add("ASX");
                                    model_1.add("Attrage");
                                    model_1.add("Cedia");
                                    model_1.add("Celeste");
                                    model_1.add("Chariot");
                                    model_1.add("Colt");
                                    model_1.add("Delica");
                                    model_1.add("Eclipse Cross");
                                    model_1.add("FTO");
                                    model_1.add("Galant");
                                    model_1.add("J20");
                                    model_1.add("J24");
                                    model_1.add("L200");
                                    model_1.add("Lancer");
                                    model_1.add("Libero");
                                    model_1.add("Mirage");
                                    model_1.add("Montero");
                                    model_1.add("Outlander");
                                    model_1.add("Pajero");
                                    model_1.add("RVR");
                                    model_1.add("Raider");
                                    model_1.add("Shogun");
                                    model_1.add("Sportero");
                                    model_1.add("Strada");
                                    model_1.add("Towny");
                                    model_1.add("Xpander");
                                    model_1.add("eK Space");
                                    model_1.add("eK Wagon");
                                    model_1.add("i-MiEV");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 44){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("8");
                                    model_1.add("Mini");
                                    model_1.add("Minor");
                                    model_1.add("Oxford");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 45){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 46){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("370Z");
                                    model_1.add("AD Wagon");
                                    model_1.add("Almera");
                                    model_1.add("Aventure");
                                    model_1.add("Bluebird");
                                    model_1.add("Cefiro");
                                    model_1.add("Dayz");
                                    model_1.add("Dualis");
                                    model_1.add("Dutsun");
                                    model_1.add("Fairlady Z");
                                    model_1.add("Fuga");
                                    model_1.add("GT-R");
                                    model_1.add("Gloria");
                                    model_1.add("J10");
                                    model_1.add("Juke");
                                    model_1.add("Leaf");
                                    model_1.add("March");
                                    model_1.add("Micra");
                                    model_1.add("Navara");
                                    model_1.add("Note");
                                    model_1.add("Pathfinder");
                                    model_1.add("Patrol");
                                    model_1.add("Presea");
                                    model_1.add("Primera");
                                    model_1.add("Pulsar");
                                    model_1.add("Qashqai");
                                    model_1.add("Serena");
                                    model_1.add("Sima");
                                    model_1.add("Skyline");
                                    model_1.add("Sunny");
                                    model_1.add("Sylphy");
                                    model_1.add("Teana");
                                    model_1.add("Terrano");
                                    model_1.add("Tiida");
                                    model_1.add("Wingroad");
                                    model_1.add("X-Trail");
                                    model_1.add("e-NV200");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 47){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 48){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Astra");
                                    model_1.add("Combo");
                                    model_1.add("Cresent");
                                    model_1.add("Crossland");
                                    model_1.add("Frontera");
                                    model_1.add("Grandland");
                                    model_1.add("Insignia");
                                    model_1.add("Karl");
                                    model_1.add("Mokka");
                                    model_1.add("Omega");
                                    model_1.add("Vectra");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 49){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Alza");
                                    model_1.add("Axia");
                                    model_1.add("Bezza");
                                    model_1.add("Kancil");
                                    model_1.add("Kelisa");
                                    model_1.add("Kembara");
                                    model_1.add("Kenari");
                                    model_1.add("Myvi");
                                    model_1.add("Viva Elite");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 50){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("104");
                                    model_1.add("108");
                                    model_1.add("2008");
                                    model_1.add("206");
                                    model_1.add("208");
                                    model_1.add("3008");
                                    model_1.add("305");
                                    model_1.add("307");
                                    model_1.add("308");
                                    model_1.add("404");
                                    model_1.add("405");
                                    model_1.add("406");
                                    model_1.add("407");
                                    model_1.add("408");
                                    model_1.add("5008");
                                    model_1.add("505");
                                    model_1.add("607");
                                    model_1.add("iOn");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 51){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 52){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 53){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("718");
                                    model_1.add("718 Boxter");
                                    model_1.add("718 Cayman");
                                    model_1.add("718 GTS");
                                    model_1.add("911");
                                    model_1.add("911 Carrera");
                                    model_1.add("911 GT2");
                                    model_1.add("911 GT3");
                                    model_1.add("911 GTS");
                                    model_1.add("911 Targa");
                                    model_1.add("911 Turbo");
                                    model_1.add("918 Spyder");
                                    model_1.add("Carrera GT");
                                    model_1.add("Cayenne");
                                    model_1.add("Macan");
                                    model_1.add("Panamera");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 54){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Exora");
                                    model_1.add("Gen-2");
                                    model_1.add("Perdana");
                                    model_1.add("Persona");
                                    model_1.add("Saga");
                                    model_1.add("Savvy");
                                    model_1.add("waja");
                                    model_1.add("wira");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 55){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Captur");
                                    model_1.add("Duster");
                                    model_1.add("Fluence");
                                    model_1.add("KWID");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 56){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 57){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 58){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 59){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 60){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Arona");
                                    model_1.add("Ateca");
                                    model_1.add("Ibiza");
                                    model_1.add("Leon");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 61){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Cities");
                                    model_1.add("Fabia");
                                    model_1.add("Karoq");
                                    model_1.add("Kodiaq");
                                    model_1.add("Laura");
                                    model_1.add("Octavia");
                                    model_1.add("Rapid");
                                    model_1.add("Superb");
                                    model_1.add("Yeti");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 62){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Elecric");
                                    model_1.add("Forfour");
                                    model_1.add("Fortwo");
                                    model_1.add("Roadster");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 63){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Actyon");
                                    model_1.add("Chairman");
                                    model_1.add("Korando");
                                    model_1.add("Kyron");
                                    model_1.add("Musso");
                                    model_1.add("Rexton");
                                    model_1.add("Rodius");
                                    model_1.add("Tivoli");
                                    model_1.add("XLV");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 64){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Ascent");
                                    model_1.add("BRZ");
                                    model_1.add("Crosstrek");
                                    model_1.add("Forester");
                                    model_1.add("Impreza");
                                    model_1.add("Legacy");
                                    model_1.add("Levorg");
                                    model_1.add("R2");
                                    model_1.add("STI");
                                    model_1.add("Stella");
                                    model_1.add("Trezia");
                                    model_1.add("WRX");
                                    model_1.add("XV");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 65){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("A-Star");
                                    model_1.add("Alto");
                                    model_1.add("Baleno");
                                    model_1.add("Celerio");
                                    model_1.add("Ciaz");
                                    model_1.add("Cultus");
                                    model_1.add("Dzire");
                                    model_1.add("Ertiga");
                                    model_1.add("Escudo");
                                    model_1.add("Esteem");
                                    model_1.add("Estilo");
                                    model_1.add("Grand Vitara");
                                    model_1.add("Hustler");
                                    model_1.add("Ignis");
                                    model_1.add("Jimny");
                                    model_1.add("Kizashi");
                                    model_1.add("Liana");
                                    model_1.add("Maruti");
                                    model_1.add("S-Cross");
                                    model_1.add("SX4");
                                    model_1.add("Solio");
                                    model_1.add("Solis");
                                    model_1.add("Spacia");
                                    model_1.add("Splash");
                                    model_1.add("Swift");
                                    model_1.add("Twin");
                                    model_1.add("Vitara");
                                    model_1.add("Wagon R");
                                    model_1.add("Wagon R FX");
                                    model_1.add("Wagon R FZ");
                                    model_1.add("Wagon R Stingray");
                                    model_1.add("XBee");
                                    model_1.add("Zen");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 66){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Aria");
                                    model_1.add("Bolt");
                                    model_1.add("GenX Nano");
                                    model_1.add("Hexa");
                                    model_1.add("Indica");
                                    model_1.add("Indigo");
                                    model_1.add("Nano");
                                    model_1.add("Nexon");
                                    model_1.add("Safari");
                                    model_1.add("Sumo");
                                    model_1.add("Telcolin");
                                    model_1.add("Tiago");
                                    model_1.add("Tigor");
                                    model_1.add("Vista");
                                    model_1.add("Xenon");
                                    model_1.add("Zest");
                                    model_1.add("Other Vehicle");
                                }else if (selected_brand == 67){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Model 3");
                                    model_1.add("Model S");
                                    model_1.add("Model X");
                                    model_1.add("Roadster");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 68){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("4Runner");
                                    model_1.add("Allex");
                                    model_1.add("Allion");
                                    model_1.add("Alphard");
                                    model_1.add("Altezza");
                                    model_1.add("Aqua");
                                    model_1.add("Aristo");
                                    model_1.add("Auris");
                                    model_1.add("Avalon");
                                    model_1.add("Avanza");
                                    model_1.add("Avensis");
                                    model_1.add("Axio");
                                    model_1.add("Aygo");
                                    model_1.add("BB");
                                    model_1.add("Belta");
                                    model_1.add("Blizzard");
                                    model_1.add("Brebis");
                                    model_1.add("CH-R");
                                    model_1.add("Caldina");
                                    model_1.add("Cami");
                                    model_1.add("Camry");
                                    model_1.add("Carid");
                                    model_1.add("Carina");
                                    model_1.add("Cast");
                                    model_1.add("Celica");
                                    model_1.add("Century");
                                    model_1.add("Ceres");
                                    model_1.add("Chaser");
                                    model_1.add("Classic");
                                    model_1.add("Comfort");
                                    model_1.add("Corolla");
                                    model_1.add("Corona");
                                    model_1.add("Corsa");
                                    model_1.add("Crown");
                                    model_1.add("Cynos");
                                    model_1.add("Esquire");
                                    model_1.add("Etios");
                                    model_1.add("FJ Cruiser");
                                    model_1.add("Fielder");
                                    model_1.add("Fortuner");
                                    model_1.add("GT86");
                                    model_1.add("Harrier");
                                    model_1.add("Hoghlander");
                                    model_1.add("Hilux");
                                    model_1.add("IST");
                                    model_1.add("Land Cruiser Prado");
                                    model_1.add("Land Cruiser Sahara");
                                    model_1.add("MR-S");
                                    model_1.add("Mirai");
                                    model_1.add("Passo");
                                    model_1.add("Pixis");
                                    model_1.add("Platz");
                                    model_1.add("Premio");
                                    model_1.add("Prius");
                                    model_1.add("RAV4");
                                    model_1.add("Roomy");
                                    model_1.add("Rush");
                                    model_1.add("Marino");
                                    model_1.add("Squoia");
                                    model_1.add("Sienta");
                                    model_1.add("Soluna");
                                    model_1.add("Sprinter");
                                    model_1.add("Startlet");
                                    model_1.add("Supra");
                                    model_1.add("Tank");
                                    model_1.add("Tersel");
                                    model_1.add("Vellfire");
                                    model_1.add("Vios");
                                    model_1.add("Vitz");
                                    model_1.add("Voxy");
                                    model_1.add("Welfare");
                                    model_1.add("wigo");
                                    model_1.add("Wish");
                                    model_1.add("Yaris");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 69){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Corsa");
                                    model_1.add("Crossland");
                                    model_1.add("Insignia");
                                    model_1.add("VXR8");
                                    model_1.add("Vectra");
                                    model_1.add("Viva");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 70){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Atlas");
                                    model_1.add("Beetle");
                                    model_1.add("Bora");
                                    model_1.add("Golf");
                                    model_1.add("Jetta");
                                    model_1.add("Passat");
                                    model_1.add("Polo");
                                    model_1.add("T-Roc");
                                    model_1.add("Tiguan");
                                    model_1.add("UP");
                                    model_1.add("VW1300");
                                    model_1.add("e-Golf");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 71){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("850");
                                    model_1.add("940");
                                    model_1.add("S40");
                                    model_1.add("S60");
                                    model_1.add("S80");
                                    model_1.add("S90");
                                    model_1.add("V40");
                                    model_1.add("V50");
                                    model_1.add("V60");
                                    model_1.add("V70");
                                    model_1.add("V90");
                                    model_1.add("XC40");
                                    model_1.add("XC60");
                                    model_1.add("XC70");
                                    model_1.add("XC90");
                                    model_1.add("Other Model");
                                }else if (selected_brand == 72){
                                    model_1.clear();
                                    model_1.add("මාදිලිය");
                                    model_1.add("Extreme");
                                    model_1.add("Nomad");
                                    model_1.add("Z100");
                                    model_1.add("Other Model");
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        model.setAdapter(models_adapter);
                        body.setAdapter(body_adapter);
                        transmission.setAdapter(transmission_adapter);
                        trim.setText("");
                        model_year.setText("");
                        mileage.setText("");
                        capacity.setText("");
                        description.setText("");
                        price.setText("");
                        contact.setText("");
                        radioGroup.clearCheck();
                        fuel.setAdapter(fuel_adapter);
                        title.setText("");
                    } else {
                        Snackbar.make(parentView, R.string.string_some_thing_wrong, Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CarResponse> call, @NonNull Throwable t) {
                    //hideProgress();
                    Snackbar.make(parentView, t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            //hideProgress();
            Toast.makeText(NewAdsPostActivity.this,
                    R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }
}
