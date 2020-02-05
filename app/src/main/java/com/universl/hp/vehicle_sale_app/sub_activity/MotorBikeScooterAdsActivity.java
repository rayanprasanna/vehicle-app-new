package com.universl.hp.vehicle_sale_app.sub_activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.universl.hp.vehicle_sale_app.PostAdsActivity;
import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.adapter.UploadListAdapter;
import com.universl.hp.vehicle_sale_app.items.ItemData;
import com.universl.hp.vehicle_sale_app.response.DistrictResponse;
import com.universl.hp.vehicle_sale_app.utils.Constants;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MotorBikeScooterAdsActivity extends AppCompatActivity {

    private Context context;
    public Activity activity;
    private RelativeLayout relativeLayout;
    private ArrayList<DistrictResponse> districtResponses;
    ArrayList<String> districts,cities, models,brands;
    Spinner district,city,brand,model;
    EditText vehicle_type,year,mileage,capacity,description,price,trim;
    Button select_image,contact;
    int selected_district,selected_brand;
    ArrayList<ItemData> bodies;
    private UploadListAdapter uploadListAdapter;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseRef;
    public List<String> image_Urls;
    public ArrayList<String> multiple_imageUrl,single_imageUrl;
    RadioGroup radioGroup;
    RadioButton condition,new_radio,re_condition_radio,used_radio;
    Button save;
    Boolean isMultiple = false;
    Boolean isParseBoolean = false,isMotorbikeScooter = false;
    TextView contact_num,name;
    int count = 0;
    private PopupWindow popupWindow;
    ArrayAdapter<String> districtAdapter,cityAdapter,brandAdapter,modelAdapter;
    private String user_name,user_Uid;
    private ProgressDialog progress;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor_bike_scooter);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        relativeLayout = findViewById(R.id.motorbike_scooter_activity);
        context = getApplicationContext();
        activity = MotorBikeScooterAdsActivity.this;
        district = findViewById(R.id.select_location_district_3);
        city = findViewById(R.id.select_location_city_3);
        brand = findViewById(R.id.select_brand_3);
        model = findViewById(R.id.select_model_3);
        select_image = findViewById(R.id.image_select_btn_3);
        radioGroup = findViewById(R.id.radioCondition_3);
        contact = findViewById(R.id.contact_3);
        vehicle_type = findViewById(R.id.vehicle_type_2);
        year = findViewById(R.id.model_year_3);
        trim = findViewById(R.id.trim_2);
        mileage = findViewById(R.id.mileage_3);
        capacity = findViewById(R.id.capacity_3);
        description = findViewById(R.id.description_3);
        price = findViewById(R.id.price_3);
        save = findViewById(R.id.save_3);
        name = findViewById(R.id.name_text_3);
        contact_num = findViewById(R.id.telephone_num_3);
        new_radio = findViewById(R.id.new_radio_3);
        re_condition_radio = findViewById(R.id.recondition_radio_3);
        used_radio = findViewById(R.id.used_radio_3);
        mStorage = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        districts = new ArrayList<>();
        cities = new ArrayList<>();
        brands = new ArrayList<>();
        models = new ArrayList<>();
        bodies = new ArrayList<>();
        image_Urls = new ArrayList<>();
        multiple_imageUrl = new ArrayList<>();
        single_imageUrl = new ArrayList<>();
        progress = new ProgressDialog(MotorBikeScooterAdsActivity.this);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){
            user_name = account.getDisplayName();
            user_Uid = account.getId();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView mUploadList = findViewById(R.id.recycler_view_3);
        mUploadList.setLayoutManager(layoutManager);
        /*uploadListAdapter = new UploadListAdapter(this,image_Urls);
        mUploadList.setAdapter(uploadListAdapter);*/
        districts.add("ස්ථානය තෝරන්න");
        cities.add("නගරය තෝරන්න");
        brands.add("වෙළඳ නාමය");
        models.add("මාදිලිය");

        brands.add("Aprilia");brands.add("Bajaj");brands.add("BMW");brands.add("Chopper");brands.add("Demak");
        brands.add("Ducati");brands.add("Electra");brands.add("Falcon");brands.add("Harley Davidson");brands.add("Hero");
        brands.add("Honda");brands.add("Kawasaki");brands.add("Kinetic");brands.add("KTM");brands.add("Loncin");
        brands.add("Mahindra");brands.add("Minnelli");brands.add("Piaggio");brands.add("Ranomoto");brands.add("Royal Enfield");
        brands.add("Scooty");brands.add("Suzuki");brands.add("Triumph");brands.add("TVS");brands.add("Vespa");brands.add("Yamaha");
        brands.add("වෙනත් වෙළඳ නාම");

        load_districts();

        districtAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,districts);
        district.setAdapter(districtAdapter);
        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        cityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,cities);
        city.setAdapter(cityAdapter);
        brandAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, brands);
        brand.setAdapter(brandAdapter);
        brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_brand = parent.getSelectedItemPosition();
                models.clear();
                models.add("මාදිලිය");
                if (selected_brand == 1){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Dorsoduro");models.add("RS");models.add("SR");models.add("SXV");models.add("Shiver");
                    models.add("Storm");models.add("Tuono");
                    models.add("Other Model");
                }else if (selected_brand == 2){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Aspire");models.add("Avenger");models.add("Avenger Cruise");models.add("Avenger Street");models.add("Boxer");models.add("Byk");
                    models.add("CT100");models.add("Caliber");models.add("Discover");models.add("Discover 110");models.add("Discover 125");models.add("Dominar");
                    models.add("Kristal");models.add("Platina");models.add("Pulsar");models.add("Pulsar 135");models.add("Pulsar 150");models.add("Pulsar 180");models.add("Pulsar 220F");
                    models.add("Pulsar NS160");models.add("Pulsar NS200");models.add("Pulsar RS200");models.add("V12");models.add("V15");
                    models.add("XCD");
                    models.add("Other Model");
                }else if (selected_brand == 3){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("C600 Sport");models.add("C650 GT");models.add("F650");models.add("F700");models.add("F800");models.add("G450");models.add("G650");
                    models.add("K1200");models.add("K1300");models.add("K1600");models.add("R nineT");models.add("R1200");models.add("S1000");models.add("Other Model");
                }else if (selected_brand == 4){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Other Model");
                }else if (selected_brand == 5){
                    models.clear();
                    models.add("මාදිලිය");models.add("ATM");models.add("Civic");models.add("D7");models.add("DTM");
                    models.add("DXT Dart");models.add("DZM");models.add("Rio");models.add("Rino");models.add("Savage Supra");
                    models.add("Sky Born");models.add("Skyline");models.add("Transler");models.add("Transtar");models.add("Tropica");models.add("Warrior");models.add("Other Model");
                }else if (selected_brand == 6){
                    models.clear();
                    models.add("මාදිලිය");models.add("Diavel");models.add("Hypermotard");models.add("Monster");
                    models.add("Multistrada");models.add("ST");models.add("Scrambler");models.add("SportClassics");models.add("Superbike");models.add("SuperSport");models.add("Other Model");
                }else if (selected_brand == 7){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Alpha");models.add("Bravo");models.add("City");models.add("Classic");models.add("ERS 3000");models.add("KITO");models.add("Runner");
                    models.add("Traveller");models.add("VIZ");
                    models.add("Other Model");
                }else if (selected_brand == 8){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Other Model");
                }else if (selected_brand == 9){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Dyna Super Glide");models.add("FL");models.add("Softail");models.add("Sportster");models.add("Street");
                    models.add("VRSC");
                    models.add("Other Model");
                }else if (selected_brand == 10){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Achiever");models.add("CBZ");models.add("Dare");models.add("Dash");models.add("Dawn");models.add("Duet");
                    models.add("Glamour");models.add("HF Dawn");models.add("HF Deluxe");models.add("Hunk");models.add("Ignitor");models.add("Karizma");
                    models.add("Maestro Edge");models.add("Passion Plus");models.add("Passion Pro");models.add("Pleasure");models.add("Splender");
                    models.add("Splender Plus");models.add("Splender i smart");models.add("Super Splender");models.add("X Pulse");
                    models.add("XF3R");models.add("Xtream");models.add("Other Model");
                }else if (selected_brand == 11){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("AX1");models.add("Activa");
                    models.add("APE");models.add("Avitor");models.add("Benly");models.add("CB 125");models.add("CB Hornet");models.add("CB Shine");
                    models.add("CB Trigger");models.add("CB Unicorn");models.add("CB4");models.add("CBR");models.add("CD 110");
                    models.add("CD125");models.add("CD200");models.add("CD 70");models.add("CD Down");models.add("CD 90");models.add("CM Custom");models.add("CRF");models.add("Cliq");
                    models.add("Dio");models.add("DreamFTR");models.add("Gold Wing");models.add("Forza");models.add("Grazia");models.add("Hornet");models.add("Jade");
                    models.add("Livo");models.add("MD");models.add("Magna");models.add("NV400");models.add("Navi");models.add("PCX");models.add("Rebel");
                    models.add("Passion");models.add("Roadmaster");models.add("SL");models.add("Stunner");models.add("Super Club");models.add("Twister");
                    models.add("X-Blade");models.add("XLR");models.add("XR");models.add("VTR");models.add("Zoomer");models.add("Other Model");
                }else if (selected_brand == 12){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Balius");models.add("D Tracker");models.add("Eliminator");models.add("Estrella");models.add("GPZ");
                    models.add("KDX");models.add("KLX");models.add("KX");models.add("Ninja");models.add("Versys");
                    models.add("Vulcan S");models.add("Z");
                    models.add("Other Model");
                }else if (selected_brand == 13){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("4S");models.add("Blaze");models.add("Boxer");models.add("GF");models.add("Safari");models.add("Stryker");models.add("Other Model");
                }else if (selected_brand == 14){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("250");models.add("390");models.add("690");models.add("790");
                    models.add("Duke");models.add("Duke 200");models.add("RC");models.add("RC 200");models.add("Other Model");
                }else if (selected_brand == 15){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("90");models.add("CD");models.add("LD");models.add("LX");models.add("Super");models.add("Other Model");
                }else if (selected_brand == 16){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Centuro");models.add("Duro");models.add("Gusto");models.add("Mojo");models.add("Uzo 125");models.add("Other Model");
                }else if (selected_brand == 17){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Other Model");
                }else if (selected_brand == 18){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Other Model");
                }else if (selected_brand == 19){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Other Model");
                }else if (selected_brand == 20){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Bullet");models.add("Classic");models.add("Classic");models.add("Himalayan");
                    models.add("Interceptor");models.add("Machismo");models.add("Thunderbird");models.add("Other Model");
                }else if (selected_brand == 21){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Other Model");
                }else if (selected_brand == 22){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("AX100");models.add("Access");models.add("Bandit");models.add("DR");models.add("DRZ");models.add("Djebel");
                    models.add("GN 125");models.add("GN 250");models.add("GS 125");models.add("Gixxer");models.add("Grass Tracker");models.add("Intruder");
                    models.add("Lets");models.add("SX");models.add("Volty");models.add("");
                    models.add("Other Model");
                }else if (selected_brand == 23){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Other Model");
                }else if (selected_brand == 24){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Apache");models.add("Centra");models.add("Creon");models.add("Flame");models.add("Jupiter");models.add("Metro");models.add("Ntotq");
                    models.add("Scooty Pep");models.add("Scooty Pep+");models.add("Scooty Pept");models.add("Scooty Zest");models.add("Sport");models.add("Star City Plus");models.add("Star Sport");
                    models.add("Streak");models.add("Victor");models.add("Wego");models.add("XL 100");models.add("XL Super");models.add("Zest");models.add("Other Model");
                }else if (selected_brand == 25){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Hlegante");models.add("LX");models.add("SXL");models.add("VXL");models.add("Other Model");
                }else if (selected_brand == 26){
                    models.clear();
                    models.add("මාදිලිය");
                    models.add("Alpha");models.add("DT");models.add("Enticer");models.add("FZ");models.add("FZ S");models.add("FZ25");models.add("Facino");models.add("Fascino");models.add("Fazer");
                    models.add("Gladiator");models.add("JGR");models.add("Libero");models.add("Mate");models.add("MT 06");models.add("Mt 09");models.add("Majesty");models.add("N Max");models.add("R1");
                    models.add("R15");models.add("R6");models.add("Ray");models.add("Ray ZR");models.add("SZ-RR");models.add("Saluto");models.add("TTR");models.add("TW");models.add("TZR");
                    models.add("Virago");models.add("WR");models.add("WRF");models.add("WRZ");models.add("YZ");models.add("YZF");
                    models.add("Other Model");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        modelAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,models);
        model.setAdapter(modelAdapter);

        name.setText(user_name);

        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count <= 4){
                    count++;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), Constants.PICK_IMAGE_MULTIPLE);
                }else {
                    Toast.makeText(MotorBikeScooterAdsActivity.this,"More than 5 Images Selected",Toast.LENGTH_SHORT).show();
                }
            }
        });
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        condition = findViewById(selectedId);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMotorbikeScooter = true;
                int position;
                if (new_radio.isChecked()){
                    position = 0;
                }else if (re_condition_radio.isChecked()){
                    position = 2;
                }else {
                    position = 1;
                }
                Intent intent = new Intent(MotorBikeScooterAdsActivity.this,PhoneAuthActivity.class);
                intent.putExtra("district",district.getSelectedItem().toString());
                intent.putExtra("city",city.getSelectedItem().toString());
                if (isMultiple){
                    intent.putStringArrayListExtra("multiple_images",multiple_imageUrl);
                }else {
                    intent.putStringArrayListExtra("single_images",single_imageUrl);
                }
                intent.putExtra("brand",brand.getSelectedItem().toString());
                intent.putExtra("model",model.getSelectedItem().toString());
                intent.putExtra("vehicle_type",vehicle_type.getText().toString());
                intent.putExtra("year",year.getText().toString());
                intent.putExtra("mileage",mileage.getText().toString());
                intent.putExtra("capacity",capacity.getText().toString());
                intent.putExtra("description",description.getText().toString());
                intent.putExtra("price",price.getText().toString());
                intent.putExtra("trim",trim.getText().toString());
                intent.putExtra("condition",position);
                intent.putExtra("isMultiple",isMultiple);
                intent.putExtra("isMotorbikeScooterActivity",isMotorbikeScooter);
                startActivity(intent);
                finish();
            }
        });
        isParseBoolean = getIntent().getBooleanExtra("isParseBoolean",false);
        final ArrayList<String> multi_images = getIntent().getStringArrayListExtra("multiple_images");
        final ArrayList<String> sing_images = getIntent().getStringArrayListExtra("single_images");
        final Boolean isMultipleImage = getIntent().getBooleanExtra("isMultiple",false);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMultipleImage){
                    if (isParseBoolean){
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        //final int condition_sp = getIntent().getIntExtra("condition",0);
                        // find the radiobutton by returned id
                        condition = findViewById(selectedId);
                        upLoadMotorbikeScooter(district,city,multi_images,brand,model,year,
                                condition.getText().toString(),mileage,capacity,description,price,
                                contact_num,user_Uid,vehicle_type.getText().toString(),vehicle_type,trim);
                    }
                }else {
                    if (isParseBoolean){
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        //final int condition_sp = getIntent().getIntExtra("condition",0);
                        // find the radiobutton by returned id
                        condition = findViewById(selectedId);
                        upLoadMotorbikeScooter(district,city,sing_images,brand,model,year,
                                condition.getText().toString(),mileage,capacity,description,price,
                                contact_num,user_Uid,vehicle_type.getText().toString(),vehicle_type,trim);
                    }
                }
            }
        });


        if (isParseBoolean){
            System.out.println("////////////////////"+isParseBoolean);

            if (isMultipleImage){
                /*uploadListAdapter = new UploadListAdapter(this,multi_images);
                mUploadList.setAdapter(uploadListAdapter);*/
                open_Popup_window();
            }else {
                /*uploadListAdapter = new UploadListAdapter(this,sing_images);
                mUploadList.setAdapter(uploadListAdapter);*/
                open_Popup_window();
            }
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.new_radio_3:
                if(checked)
                    break;
            case R.id.used_radio_3:
                if(checked)
                    break;
            case R.id.recondition_radio_3:
                if(checked)
                    break;
        }
    }
    @SuppressLint("InflateParams")
    private void open_Popup_window() {
        final String district_sp = getIntent().getStringExtra("district");
        final String contact_number = getIntent().getStringExtra("contact");
        final String city_sp = getIntent().getStringExtra("city");
        final String brand_sp = getIntent().getStringExtra("brand");
        final String model_sp = getIntent().getStringExtra("model");
        final String year_sp = getIntent().getStringExtra("year");
        final String trim_sp = getIntent().getStringExtra("trim");
        final String description_sp = getIntent().getStringExtra("description");
        final String price_sp = getIntent().getStringExtra("price");
        final String capacity_sp = getIntent().getStringExtra("capacity");
        final String mileage_sp = getIntent().getStringExtra("mileage");
        final String vehicle_type_sp = getIntent().getStringExtra("vehicle_type");
        final int condition_sp = getIntent().getIntExtra("condition",0);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams") View customView = null;
        if (inflater != null) {
            customView = inflater.inflate(R.layout.load_previous_details,null);
        }

        popupWindow = new PopupWindow(
                customView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        if(Build.VERSION.SDK_INT>=21){
            popupWindow.setElevation(5.0f);
        }


        TextView exit,Yes;
        if (customView != null) {
            Yes = customView.findViewById(R.id.yes_btn);
            exit = customView.findViewById(R.id.close_id);
            Yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    district.setSelection(districtAdapter.getPosition(district_sp));
                    brand.setSelection(brandAdapter.getPosition(brand_sp));
                    vehicle_type.setText(vehicle_type_sp);
                    year.setText(year_sp);
                    mileage.setText(mileage_sp);
                    contact_num.setText(contact_number);
                    description.setText(description_sp);
                    trim.setText(trim_sp);
                    price.setText(price_sp);
                    capacity.setText(capacity_sp);
                    ((RadioButton)radioGroup.getChildAt(condition_sp)).setChecked(true);

                }
            });
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    city.setSelection(cityAdapter.getPosition(city_sp));
                    model.setSelection(modelAdapter.getPosition(model_sp));
                    popupWindow.dismiss();
                }
            });

        }

        findViewById(R.id.motorbike_scooter_activity).post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(relativeLayout, Gravity.CENTER_VERTICAL,0,0);
            }
        });
    }

    private void load_districts() {
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> {
            private ProgressDialog progress = new ProgressDialog(MotorBikeScooterAdsActivity.this);
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
    public void onPause() {
        super.onPause();

        if ((progress != null) && progress.isShowing())
            progress.dismiss();
        progress = null;
    }
    private void upLoadMotorbikeScooter(final Spinner district,final Spinner city,final ArrayList<String> image_Urls,
                           final Spinner brand,final Spinner model, final EditText year,final String condition,
                           final EditText mileage, final EditText capacity,final EditText description,final EditText price,
                           final TextView contact_num,final String user_Uid,final String vehicleType,final EditText vehicle_type,final TextView trim){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + image_Urls.size());
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String>{
            private ProgressDialog progress = new ProgressDialog(MotorBikeScooterAdsActivity.this);
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
                    HttpPost httpPost = new HttpPost(Constants.BASE_URL_insert_motorbike_scooter);
                    List<NameValuePair> list = new ArrayList<>();
                    //httpPost.setEntity(new UrlEncodedFormEntity(list,"UTF-8"));
                    list.add(new BasicNameValuePair("district",district.getSelectedItem().toString()));
                    list.add(new BasicNameValuePair("city", city.getSelectedItem().toString()));
                    list.add(new BasicNameValuePair("vehicle_type",vehicleType));
                    list.add(new BasicNameValuePair("description", description.getText().toString()));
                    list.add(new BasicNameValuePair("contact", contact_num.getText().toString()));
                    list.add(new BasicNameValuePair("user_id", user_Uid));
                    list.add(new BasicNameValuePair("mileage", mileage.getText().toString()));
                    list.add(new BasicNameValuePair("engine_capacity", capacity.getText().toString()));
                    list.add(new BasicNameValuePair("model", model.getSelectedItem().toString()));
                    list.add(new BasicNameValuePair("brand",brand.getSelectedItem().toString()));
                    list.add(new BasicNameValuePair("conditions", condition));
                    list.add(new BasicNameValuePair("model_year",year.getText().toString()));
                    list.add(new BasicNameValuePair("price",price.getText().toString()));
                    list.add(new BasicNameValuePair("trim",trim.getText().toString()));

                    for(int i=0;i<image_Urls.size();i++){
                        list.add(new BasicNameValuePair("image_path_"+(i+1),image_Urls.get(i)));
                    }
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
                }
                image_Urls.clear();
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                RecyclerView mUploadList = findViewById(R.id.recycler_view_3);
                mUploadList.setLayoutManager(layoutManager);
                /*uploadListAdapter = new UploadListAdapter(getApplicationContext(),image_Urls);
                mUploadList.setAdapter(uploadListAdapter);*/
                district.setAdapter(districtAdapter);
                district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                city.setAdapter(cityAdapter);
                brand.setAdapter(brandAdapter);
                brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selected_brand = parent.getSelectedItemPosition();
                        models.clear();
                        models.add("මාදිලිය");
                        if (selected_brand == 1){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Stelvio Quadrifoglio");models.add("Stelvio");models.add("Giulia Q4 Sedan (4WD)");models.add("Stelvio Q4 (4WD) ");models.add("Stelvio RWD ");
                            models.add("Other Model");
                        }else if (selected_brand == 2){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("VANQUISH S");models.add("VANQUISH");models.add("VANQUISH VOLANTE");models.add("VANQUISH ZAGATO");models.add("RAPIDE S");
                            models.add("DB9 GP");models.add("DB11");models.add("V12 VANTAGE S");models.add("V12 VANTAGE S ROADSTER");models.add("V8 VANTAGE S");
                            models.add("LAGONDA TARAF");models.add("VANTAGE GT12");models.add("VANTAGE GT8");models.add("ASTON MARTIN VULCAN");
                            models.add("Other Model");
                        }else if (selected_brand == 3){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("100");models.add("80");models.add("A1");models.add("A3");models.add("A4");models.add("A5");models.add("A6");
                            models.add("A7");models.add("A8");models.add("Q1");models.add("Q2");models.add("Q3");models.add("Q5");models.add("Q7");
                            models.add("R8");models.add("RS3");models.add("RS4");models.add("RS5");models.add("RS6");models.add("S1");models.add("S2");
                            models.add("S3");models.add("S4");models.add("S5");models.add("S6");models.add("S7");models.add("S8");models.add("SQ5");models.add("SQ7");
                            models.add("TT");models.add("TTS");models.add("V8");models.add("Other Model");
                        }else if (selected_brand == 4){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("7");models.add("Cambridge");models.add("Mini Cooper");models.add("Mini");models.add("Standard");models.add("Other Model");
                        }else if (selected_brand == 5){
                            models.clear();
                            models.add("මාදිලිය");models.add("116i");models.add("118d");models.add("118i");
                            models.add("120d");models.add("120i");models.add("123d");models.add("125i");
                            models.add("130i");models.add("135i");models.add("218d");models.add("218i");
                            models.add("220d");models.add("220i");models.add("225XE");models.add("225i");
                            models.add("228i");models.add("230i");models.add("252i");models.add("316i");
                            models.add("316ti");models.add("318is");models.add("318ti");models.add("320Ci");
                            models.add("320d");models.add("320i");models.add("323Ci");models.add("323i");
                            models.add("325Ci");models.add("325d");models.add("325e");models.add("325i");
                            models.add("328Ci");models.add("328d");models.add("328i");models.add("330 GT");
                            models.add("330Ci");models.add("330d");models.add("330e");models.add("330i");
                            models.add("335d");models.add("335i");models.add("340 GT");models.add("340i");
                            models.add("420d");models.add("420i");models.add("428 Gran Coupe");models.add("428i");
                            models.add("430 Gran Coupe");models.add("430i");models.add("435 Bran Coupe");
                            models.add("435i");models.add("440 Gran Coupe");models.add("440i");
                            models.add("520d");models.add("520i");models.add("523i");models.add("525e");
                            models.add("525i");models.add("528i");models.add("530d");models.add("530e");
                            models.add("530i");models.add("535 GT");models.add("535d");models.add("535i");
                            models.add("540d");models.add("540i");models.add("545i");models.add("550 GT");
                            models.add("550i");models.add("630i");models.add("633CSi");models.add("635CSi");
                            models.add("635d");models.add("640 GT");models.add("640 Gran Coupe");
                            models.add("640d");models.add("640i");models.add("645ci");models.add("650 Gran Coupe");
                            models.add("650i");models.add("730d");models.add("730iL");models.add("733i");
                            models.add("735Li");models.add("735i");models.add("735iL");models.add("740Le");
                            models.add("740Li");models.add("740d");models.add("740e");models.add("740i");
                            models.add("740iL");models.add("745Li");models.add("745i");models.add("750Li");
                            models.add("750i");models.add("750iL");models.add("760Li");models.add("840Ci");
                            models.add("850Ci");models.add("850i");models.add("ActivateHybrid 3");
                            models.add("ActivateHybrid 5");models.add("ActivateHybrid 37");models.add("ActivateHybrid 740");
                            models.add("ActivateHybrid 750");models.add("ActivateHybrid X6");models.add("Alpina B6");
                            models.add("Alpina B7");models.add("Coupe");models.add("E46");models.add("E60");
                            models.add("E90");models.add("GT");models.add("L7");models.add("M");models.add("M135i");
                            models.add("M140i");models.add("M2");models.add("M235");models.add("M235i");
                            models.add("M240");models.add("M240i");models.add("M3");models.add("M4");
                            models.add("M5");models.add("M535i");models.add("M550");models.add("M6");
                            models.add("M6 Gran Coupe");models.add("M635CSi");models.add("M760");models.add("M760Li");
                            models.add("Mini Cooper");models.add("Model");models.add("X1");models.add("X2");
                            models.add("X3");models.add("X4");models.add("X5");models.add("X5 M");models.add("X5 eDrive");
                            models.add("X6");models.add("X6 M");models.add("Z3");models.add("Z4");models.add("Z4 M");
                            models.add("Z8");models.add("i3");models.add("i5");models.add("i8");models.add("Other Model");
                        }else if (selected_brand == 6){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 7){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 8){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 9){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("A3");models.add("Arrizo");models.add("E3");models.add("E5");models.add("Fulwin");
                            models.add("QQ");models.add("QQ3");models.add("Tiggo");models.add("X1");models.add("eQ");
                            models.add("Other Model");
                        }else if (selected_brand == 10){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Aveo");models.add("Beat");models.add("Bolt");models.add("Camaro");models.add("Captiva");models.add("Colorado");
                            models.add("Corvette");models.add("Cruze");models.add("Malibu");models.add("Silverado");models.add("Sonic");models.add("Spark");models.add("Other Model");
                        }else if (selected_brand == 11){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("200");models.add("300");models.add("Other Model");
                        }else if (selected_brand == 12){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 13){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Espero");models.add("Lanos");models.add("Leganza");models.add("Magnus");models.add("Nubira");
                            models.add("Tacuma");models.add("Tico");models.add("Tosca");models.add("Other Model");
                        }else if (selected_brand == 14){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Altis");models.add("Atrai Wagon");models.add("Boon");models.add("Canbus");models.add("Cast Activa");models.add("Charade");
                            models.add("Charmant");models.add("Copen");models.add("Cuore");models.add("Esse");models.add("F50");models.add("Hijet");
                            models.add("Leeza");models.add("Mebius");models.add("Mira");models.add("Move");models.add("Redigo");models.add("Rocky");
                            models.add("Tanto");models.add("Terios");models.add("Thor");models.add("Wake");models.add("Other Model");
                        }else if (selected_brand == 15){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Cross");models.add("Go");models.add("Go Plus");models.add("Mi-Do");models.add("On-Do");
                            models.add("Redi Go");models.add("Tanto");models.add("Terios");models.add("Other Model");
                        }else if (selected_brand == 16){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Glory");models.add("V27");models.add("Other Model");
                        }else if (selected_brand == 17){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 18){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 19){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("1100");models.add("500");models.add("Bravo");models.add("Fullback");models.add("Linea");
                            models.add("Palio");models.add("Panda");models.add("Punto");models.add("Sedici");models.add("Tipo");
                            models.add("Uno");models.add("Other Model");
                        }else if (selected_brand == 20){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("C-Max");models.add("Ecosport");models.add("Edge");models.add("Escape");models.add("Everest");models.add("Expedition");
                            models.add("Explorer");models.add("F-150");models.add("Festiva");models.add("Fiesta");models.add("Flex");models.add("Focus");
                            models.add("Fusion");models.add("GT");models.add("Ka+");models.add("Kuga");models.add("Laser");models.add("Mondeo");
                            models.add("Mustang");models.add("Raptor Ranger");models.add("Super Duty");models.add("Taurus");models.add("Transit");models.add("Other Model");
                        }else if (selected_brand == 21){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 22){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Acadia");models.add("Canyon");models.add("Envoy");models.add("Sierra");models.add("Terrain");models.add("Yukon");
                            models.add("Other Model");
                        }else if (selected_brand == 23){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 24){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Accord");models.add("Airwave");models.add("Amaze");models.add("Ballade");models.add("Beat");models.add("CRV");models.add("CRZ");
                            models.add("City");models.add("Civic");models.add("Clarity");models.add("Concerto");models.add("FR-V");models.add("Fit");models.add("Fit Area");
                            models.add("Fit She's");models.add("Fit Shuttle");models.add("Freed");models.add("Grace");models.add("HR-V");models.add("Insight");models.add("Inspire");
                            models.add("Integra");models.add("Jade");models.add("Jazz");models.add("Legend");models.add("Logo");models.add("N-Box");models.add("N-One");models.add("N-WGN");
                            models.add("NSX");models.add("Odyssey");models.add("Pilot");models.add("Ridgeline");models.add("S2000");models.add("S660");models.add("Spike");models.add("Step Wagon");
                            models.add("Vezel");models.add("WR-V");models.add("Other Model");
                        }else if (selected_brand == 25){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("H1");models.add("H2");models.add("H3");models.add("Other Model");
                        }else if (selected_brand == 26){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Accent");models.add("Atos");models.add("Azera");models.add("Coupe");models.add("Elantra");models.add("Eon");models.add("Excel");models.add("Genesis");models.add("Getz");
                            models.add("Grand i10");models.add("loniq");models.add("Kona");models.add("Lantra");models.add("Matrix");models.add("Mistra");models.add("Nexo");models.add("Santa Fe");models.add("Santro");
                            models.add("Sonata");models.add("Stellar");models.add("Terracan");models.add("Trajet");models.add("Tucson");models.add("Veloster");models.add("i20");models.add("i30");models.add("i40");
                            models.add("Other Model");
                        }else if (selected_brand == 27){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Bighorn");models.add("D-Max");models.add("Gemini");models.add("MU-7");models.add("MU-X");models.add("Panther");models.add("Rodeo");
                            models.add("Smart cab");models.add("Trooper");models.add("Other Model");
                        }else if (selected_brand == 28){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("E-Pace");models.add("F-Pace");models.add("F-Type");models.add("I-Pace");models.add("S-Type");
                            models.add("X-Type");models.add("XE");models.add("XF");models.add("XJ");models.add("XK");
                            models.add("Other Model");
                        }else if (selected_brand == 29){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Cherokee");models.add("Compass");models.add("Grand Cherokee");models.add("Renegade");models.add("Wrangler");models.add("Other Model");
                        }else if (selected_brand == 30){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Cadenza");models.add("carens");models.add("Carnival");models.add("Ceed");models.add("Cerato");models.add("Clarus");models.add("Forte");
                            models.add("K7");models.add("K9");models.add("K900");models.add("Mentor");models.add("Niro");models.add("Optima");models.add("Picanto");
                            models.add("Rio");models.add("Rondo");models.add("Sedona");models.add("Sephia");models.add("Sorento");models.add("Spectra");models.add("Sportage");
                            models.add("Stinger");models.add("Stonic");models.add("saul");models.add("Other Model");
                        }else if (selected_brand == 31){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 32){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Defender");models.add("Discovery");models.add("Discovery Sport");models.add("Freelander");
                            models.add("Range Rover");models.add("Range Rover Evoque");models.add("Range Rover PHEV");models.add("Range Rover Sport");
                            models.add("Range Rover Velar");models.add("SV Coupe");models.add("Other Model");
                        }else if (selected_brand == 33){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("CT-200H");models.add("ES");models.add("HS250H");models.add("LS400");models.add("LX450");models.add("Land Cruiser");
                            models.add("NX");models.add("NX300H");models.add("RX350");models.add("RX400");models.add("UX");models.add("Other Model");
                        }else if (selected_brand == 34){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 35){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Bolero");models.add("KUV100");models.add("Legend");models.add("Nuvosport");models.add("Quanto");models.add("Scorpio");models.add("TUV300");models.add("Thar");
                            models.add("Verito");models.add("XUV500");models.add("Xylo");models.add("e2o");models.add("Other Model");
                        }else if (selected_brand == 36){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("800");models.add("Alto");models.add("Baleno");models.add("Esteem");models.add("Gypsy");models.add("Omni");
                            models.add("WagonR");models.add("Zen");models.add("Other Model");
                        }else if (selected_brand == 37){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 38){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("2");models.add("2 Skyactive");models.add("3");models.add("5");models.add("6");models.add("Astina");models.add("Axela");
                            models.add("BT-50");models.add("Butterfly");models.add("CX-3");models.add("CX-5");models.add("CX-6");models.add("CX-7");models.add("CX-8");
                            models.add("CX-9");models.add("Carol");models.add("Demio");models.add("Eunos");models.add("FA4TS");models.add("Familia");models.add("Flair");
                            models.add("MX-5");models.add("Millenia");models.add("RX");models.add("Roadster");models.add("Tribute");models.add("Verisa");models.add("Other Model");
                        }else if (selected_brand == 39){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("190D");models.add("A140");models.add("A150");models.add("A160");models.add("A170");models.add("A180");models.add("A190");
                            models.add("A200");models.add("A210");models.add("A220");models.add("A250");models.add("A45");models.add("B150");models.add("B160");
                            models.add("B170");models.add("B180");models.add("B200");models.add("B220");models.add("B250e");models.add("C160");models.add("C180");
                            models.add("C200");models.add("C220");models.add("C250");models.add("C300");models.add("C350");models.add("CLA 180");models.add("CLA 200");
                            models.add("CLA 250");models.add("CLS");models.add(" E200");models.add("E220");models.add("E240");models.add("E250");models.add("E300");
                            models.add("E350");models.add("E400");models.add("GLA 180");models.add("GLA 200");models.add("GLA 250");models.add("GLC 250");models.add("GLC 300");
                            models.add("GLE 320");models.add("GLE 4OO");models.add("GLE 500");models.add("GLS 400");models.add("GLS 500");models.add("ML250");models.add("ML270");
                            models.add("ML280");models.add("ML300");models.add("ML320");models.add("ML350");models.add("ML420");models.add("ML430");models.add("ML500");
                            models.add("ML55");models.add("ML63");models.add("S300");models.add("S320");models.add("S350");models.add("S500");models.add("S560");
                            models.add("SL 400");models.add("SL 500");models.add("SLC 180");models.add("SLC 200");models.add("SLC 300");models.add("SLK 200");models.add("Other Model");
                        }else if (selected_brand == 40){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("3");models.add("6");models.add("GS");models.add("ZS");models.add("Other Model");
                        }else if (selected_brand == 41){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Actyon");models.add("BAIC");models.add("D20 HACH");models.add("Emgrand");models.add("Geely");models.add("Glory");
                            models.add("Junior");models.add("Korondo");models.add("Kyron");models.add("Lifan");models.add("MX 7");models.add("Panda");
                            models.add("Panda Cross");models.add("Privilage");models.add("Rexton");models.add("Rhino");models.add("Tivoli");models.add("Trend");models.add("Voleex");models.add("Other Model");
                        }else if (selected_brand == 42){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Clubman");models.add("Cooper");models.add("Countryman");models.add("Other Model");
                        }else if (selected_brand == 43){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("4DR");models.add("ASX");models.add("Attrage");models.add("Cedia");models.add("Celeste");models.add("Chariot");
                            models.add("Colt");models.add("Delica");models.add("Eclipse Cross");models.add("FTO");models.add("Galant");models.add("J20");
                            models.add("J24");models.add("L200");models.add("Lancer");models.add("Libero");models.add("Mirage");models.add("Montero");
                            models.add("Outlander");models.add("Pajero");models.add("RVR");models.add("Raider");models.add("Shogun");models.add("Sportero");
                            models.add("Strada");models.add("Towny");models.add("Xpander");models.add("eK Space");models.add("eK Wagon");models.add("i-MiEV");
                            models.add("Other Model");
                        }else if (selected_brand == 44){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("8");models.add("Mini");models.add("Minor");models.add("Oxford");models.add("Other Model");
                        }else if (selected_brand == 45){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 46){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("370Z");models.add("AD Wagon");models.add("Almera");models.add("Aventure");models.add("Bluebird");models.add("Cefiro");
                            models.add("Dayz");models.add("Dualis");models.add("Dutsun");models.add("Fairlady Z");models.add("Fuga");models.add("GT-R");
                            models.add("Gloria");models.add("J10");models.add("Juke");models.add("Leaf");models.add("March");models.add("Micra");
                            models.add("Navara");models.add("Note");models.add("Pathfinder");models.add("Patrol");models.add("Presea");models.add("Primera");
                            models.add("Pulsar");models.add("Qashqai");models.add("Serena");models.add("Sima");models.add("Skyline");models.add("Sunny");models.add("Sylphy");models.add("Teana");
                            models.add("Terrano");models.add("Tiida");models.add("Wingroad");models.add("X-Trail");models.add("e-NV200");models.add("Other Model");
                        }else if (selected_brand == 47){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 48){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Astra");models.add("Combo");models.add("Cresent");models.add("Crossland");models.add("Frontera");
                            models.add("Grandland");models.add("Insignia");models.add("Karl");models.add("Mokka");models.add("Omega");
                            models.add("Vectra");models.add("Other Model");
                        }else if (selected_brand == 49){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Alza");models.add("Axia");models.add("Bezza");models.add("Kancil");models.add("Kelisa");
                            models.add("Kembara");models.add("Kenari");models.add("Myvi");models.add("Viva Elite");models.add("Other Model");
                        }else if (selected_brand == 50){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("104");models.add("108");models.add("2008");models.add("206");models.add("208");models.add("3008");models.add("305");
                            models.add("307");models.add("308");models.add("404");models.add("405");models.add("406");models.add("407");models.add("408");
                            models.add("5008");models.add("505");models.add("607");models.add("iOn");models.add("Other Model");
                        }else if (selected_brand == 51){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 52){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 53){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("718");models.add("718 Boxter");models.add("718 Cayman");models.add("718 GTS");models.add("911");models.add("911 Carrera");models.add("911 GT2");
                            models.add("911 GT3");models.add("911 GTS");models.add("911 Targa");models.add("911 Turbo");models.add("918 Spyder");models.add("Carrera GT");models.add("Cayenne");
                            models.add("Macan");models.add("Panamera");models.add("Other Model");
                        }else if (selected_brand == 54){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Exora");models.add("Gen-2");models.add("Perdana");models.add("Persona");models.add("Saga");models.add("Savvy");
                            models.add("waja");models.add("wira");models.add("Other Model");
                        }else if (selected_brand == 55){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Captur");models.add("Duster");models.add("Fluence");models.add("KWID");models.add("Other Model");
                        }else if (selected_brand == 56){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 57){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 58){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 59){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Other Model");
                        }else if (selected_brand == 60){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Arona");models.add("Ateca");models.add("Ibiza");models.add("Leon");models.add("Other Model");
                        }else if (selected_brand == 61){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Cities");models.add("Fabia");models.add("Karoq");models.add("Kodiaq");models.add("Laura");models.add("Octavia");models.add("Rapid");
                            models.add("Superb");models.add("Yeti");models.add("Other Model");
                        }else if (selected_brand == 62){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Elecric");models.add("Forfour");models.add("Fortwo");models.add("Roadster");models.add("Other Model");
                        }else if (selected_brand == 63){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Actyon");models.add("Chairman");models.add("Korando");models.add("Kyron");models.add("Musso");models.add("Rexton");models.add("Rodius");
                            models.add("Tivoli");models.add("XLV");models.add("Other Model");
                        }else if (selected_brand == 64){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Ascent");models.add("BRZ");models.add("Crosstrek");models.add("Forester");models.add("Impreza");models.add("Legacy");
                            models.add("Levorg");models.add("R2");models.add("STI");models.add("Stella");models.add("Trezia");models.add("WRX");models.add("XV");models.add("Other Model");
                        }else if (selected_brand == 65){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("A-Star");models.add("Alto");models.add("Baleno");models.add("Celerio");models.add("Ciaz");models.add("Cultus");
                            models.add("Dzire");models.add("Ertiga");models.add("Escudo");models.add("Esteem");models.add("Estilo");models.add("Grand Vitara");
                            models.add("Hustler");models.add("Ignis");models.add("Jimny");models.add("Kizashi");models.add("Liana");models.add("Maruti");
                            models.add("S-Cross");models.add("SX4");models.add("Solio");models.add("Solis");models.add("Spacia");models.add("Splash");models.add("Swift");
                            models.add("Twin");models.add("Vitara");models.add("Wagon R");models.add("Wagon R FX");models.add("Wagon R FZ");models.add("Wagon R Stingray");
                            models.add("XBee");models.add("Zen");models.add("Other Model");
                        }else if (selected_brand == 66){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Aria");models.add("Bolt");models.add("GenX Nano");models.add("Hexa");models.add("Indica");
                            models.add("Indigo");models.add("Nano");models.add("Nexon");models.add("Safari");models.add("Sumo");
                            models.add("Telcolin");models.add("Tiago");models.add("Tigor");models.add("Vista");models.add("Xenon");
                            models.add("Zest");models.add("Other Vehicle");
                        }else if (selected_brand == 67){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Model 3");models.add("Model S");models.add("Model X");models.add("Roadster");models.add("Other Model");
                        }else if (selected_brand == 68){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("4Runner");models.add("Allex");models.add("Allion");models.add("Alphard");models.add("Altezza");models.add("Aqua");models.add("Aristo");
                            models.add("Auris");models.add("Avalon");models.add("Avanza");models.add("Avensis");models.add("Axio");models.add("Aygo");models.add("BB");
                            models.add("Belta");models.add("Blizzard");models.add("Brebis");models.add("CH-R");models.add("Caldina");models.add("Cami");models.add("Camry");
                            models.add("Carid");models.add("Carina");models.add("Cast");models.add("Celica");models.add("Century");models.add("Ceres");models.add("Chaser");
                            models.add("Classic");models.add("Comfort");models.add("Corolla");models.add("Corona");models.add("Corsa");models.add("Crown");models.add("Cynos");models.add("Esquire");
                            models.add("Etios");models.add("FJ Cruiser");models.add("Fielder");models.add("Fortuner");models.add("GT86");models.add("Harrier");models.add("Hoghlander");models.add("Hilux");
                            models.add("IST");models.add("Land Cruiser Prado");models.add("Land Cruiser Sahara");models.add("MR-S");models.add("Mirai");models.add("Passo");models.add("Pixis");models.add("Platz");
                            models.add("Premio");models.add("Prius");models.add("RAV4");models.add("Roomy");models.add("Rush");models.add("Marino");models.add("Squoia");models.add("Sienta");
                            models.add("Soluna");models.add("Sprinter");models.add("Startlet");models.add("Supra");models.add("Tank");models.add("Tersel");
                            models.add("Vellfire");models.add("Vios");models.add("Vitz");models.add("Voxy");models.add("Welfare");models.add("wigo");
                            models.add("Wish");models.add("Yaris");models.add("Other Model");
                        }else if (selected_brand == 69){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Corsa");models.add("Crossland");models.add("Insignia");models.add("VXR8");models.add("Vectra");
                            models.add("Viva");models.add("Other Model");
                        }else if (selected_brand == 70){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Atlas");models.add("Beetle");models.add("Bora");models.add("Golf");models.add("Jetta");
                            models.add("Passat");models.add("Polo");models.add("T-Roc");models.add("Tiguan");models.add("UP");
                            models.add("VW1300");models.add("e-Golf");models.add("Other Model");
                        }else if (selected_brand == 71){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("850");models.add("940");models.add("S40");models.add("S60");models.add("S80");models.add("S90");models.add("V40");
                            models.add("V50");models.add("V60");models.add("V70");models.add("V90");models.add("XC40");
                            models.add("XC60");models.add("XC70");models.add("XC90");models.add("Other Model");
                        }else if (selected_brand == 72){
                            models.clear();
                            models.add("මාදිලිය");
                            models.add("Extreme");models.add("Nomad");models.add("Z100");models.add("Other Model");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                model.setAdapter(modelAdapter);

                trim.setText("");
                year.setText("");
                mileage.setText("");
                capacity.setText("");
                description.setText("");
                price.setText("");
                name.setText("");
                contact_num.setText("");
                vehicle_type.setText("");
                radioGroup.clearCheck();

            }
        }
        new Network().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent = new Intent(MotorBikeScooterAdsActivity.this, PostAdsActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MotorBikeScooterAdsActivity.this, PostAdsActivity.class);
        startActivity(intent);
        this.finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            // When an Image is picked
            if (requestCode == Constants.PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {

                if(data.getData()!=null){
                    isMultiple = false;
                    Uri fileUri = data.getData();
                    final String fileName = getFileName(fileUri);

                    final StorageReference fileToUpload = mStorage.child(System.currentTimeMillis()
                            +"." + getFileExtension(fileUri));

                    fileToUpload.putFile(fileUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @TargetApi(Build.VERSION_CODES.KITKAT)
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw Objects.requireNonNull(task.getException());
                            }
                            return fileToUpload.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @TargetApi(Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Upload upload = new Upload(fileName.trim(), downloadUri.toString());
                                mDatabaseRef.push().setValue(upload);
                                image_Urls.add(downloadUri.toString());
                                single_imageUrl.add(downloadUri.toString());
                                Log.d("Test_Url",String.valueOf(single_imageUrl.size()));
                                uploadListAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MotorBikeScooterAdsActivity.this, "upload failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        if (data.getClipData() != null) {
                            isMultiple = true;
                            int totalItemsSelected = data.getClipData().getItemCount();
                            if (totalItemsSelected <= 5){
                                for (int i = 0; i < totalItemsSelected; i++){
                                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                                    final String fileName = getFileName(fileUri);

                                    uploadListAdapter.notifyDataSetChanged();

                                    final StorageReference fileToUpload = mStorage.child(System.currentTimeMillis()
                                            +"." + getFileExtension(fileUri));

                                    fileToUpload.putFile(fileUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @TargetApi(Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()) {
                                                throw Objects.requireNonNull(task.getException());
                                            }
                                            return fileToUpload.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @TargetApi(Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downloadUri = task.getResult();
                                                Upload upload = new Upload(fileName.trim(), downloadUri.toString());
                                                mDatabaseRef.push().setValue(upload);
                                                image_Urls.add(downloadUri.toString());
                                                multiple_imageUrl.add(downloadUri.toString());
                                                Log.d("Test_Url",String.valueOf(multiple_imageUrl.size()));
                                                uploadListAdapter.notifyDataSetChanged();
                                            } else {
                                                Toast.makeText(MotorBikeScooterAdsActivity.this, "upload failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }else {
                                Toast.makeText(MotorBikeScooterAdsActivity.this,"More 5 Images Selected",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getFileName(Uri uri){
        String result = null;
        if (Objects.requireNonNull(uri.getScheme()).equals("content")){
            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
            try {
                if (cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }finally {
                Objects.requireNonNull(cursor).close();
            }
        }
        if (result == null){
            result = uri.getPath();
            int cut = Objects.requireNonNull(result).lastIndexOf('/');
            if (cut != -1){
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
