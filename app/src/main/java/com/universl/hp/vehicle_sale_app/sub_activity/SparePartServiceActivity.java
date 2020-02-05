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

public class SparePartServiceActivity extends AppCompatActivity {

    private Context context;
    public Activity activity;
    private RelativeLayout relativeLayout;
    private PopupWindow popupWindow;
    private ArrayList<DistrictResponse> districtResponses;
    ArrayList<String> districts,cities,service_types;
    Spinner district,city,service_type;
    EditText title,description,price;
    Button select_image,contact;
    int selected_district;
    private UploadListAdapter uploadListAdapter;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseRef;
    public List<String> image_Urls;
    public ArrayList<String> multiple_imageUrl,single_imageUrl;
    Button save;
    Boolean isMultiple = false;
    Boolean isParseBoolean = false,isSpare_part_Service = false;
    TextView contact_num,name;
    int count = 0;
    ArrayAdapter<String> districtAdapter,cityAdapter,service_typeAdapter;
    private String user_name,user_Uid;
    private ProgressDialog progress;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spare_part_service);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        relativeLayout = findViewById(R.id.spare_part_activity);
        context = getApplicationContext();
        activity = SparePartServiceActivity.this;
        district = findViewById(R.id.select_location_district_5);
        city = findViewById(R.id.select_location_city_5);
        select_image = findViewById(R.id.image_select_btn_5);
        contact = findViewById(R.id.contact_5);
        service_type = findViewById(R.id.select_service_type_1);
        description = findViewById(R.id.description_5);
        price = findViewById(R.id.price_5);
        save = findViewById(R.id.save_5);
        name = findViewById(R.id.name_text_5);
        contact_num = findViewById(R.id.telephone_num_5);
        title = findViewById(R.id.title_2);
        mStorage = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        districts = new ArrayList<>();
        cities = new ArrayList<>();
        service_types = new ArrayList<>();
        image_Urls = new ArrayList<>();
        multiple_imageUrl = new ArrayList<>();
        single_imageUrl = new ArrayList<>();
        progress = new ProgressDialog(SparePartServiceActivity.this);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){
            user_name = account.getDisplayName();
            user_Uid = account.getId();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView mUploadList = findViewById(R.id.recycler_view_5);
        mUploadList.setLayoutManager(layoutManager);
        /*uploadListAdapter = new UploadListAdapter(this,image_Urls);
        mUploadList.setAdapter(uploadListAdapter);*/

        districts.add("ස්ථානය තෝරන්න");
        cities.add("නගරය තෝරන්න");
        service_types.add("භාණ්ඩ වර්ගය");
        service_types.add("ස්වයංක්\u200Dරිය කොටස්\n");service_types.add("කාර් ශ්\u200Dරව්\u200Dය /දෘෂ්\u200Dය");service_types.add("නඩත්තු / අලුත්වැඩියා");
        service_types.add("සුරක්ෂිතතාව / ආරක්ෂාව");service_types.add("ටයර් / රිම්");
        service_types.add("වෙනත් උපාංග");

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
        System.out.println("///////////////////////////////////////"+ districts.size());
        cityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,cities);
        city.setAdapter(cityAdapter);
        service_typeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,service_types);
        service_type.setAdapter(service_typeAdapter);

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
                    Toast.makeText(SparePartServiceActivity.this,"More than 5 Images Selected",Toast.LENGTH_SHORT).show();
                }
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSpare_part_Service = true;

                Intent intent = new Intent(SparePartServiceActivity.this,PhoneAuthActivity.class);
                intent.putExtra("district",district.getSelectedItem().toString());
                intent.putExtra("city",city.getSelectedItem().toString());
                if (isMultiple){
                    intent.putStringArrayListExtra("multiple_images",multiple_imageUrl);
                }else {
                    intent.putStringArrayListExtra("single_images",single_imageUrl);
                }
                intent.putExtra("service_type",service_type.getSelectedItem().toString());
                intent.putExtra("description",description.getText().toString());
                intent.putExtra("price",price.getText().toString());
                intent.putExtra("isMultiple",isMultiple);
                intent.putExtra("title",title.getText().toString());
                intent.putExtra("isSparePartService",isSpare_part_Service);
                startActivity(intent);
                finish();
            }
        });
        isParseBoolean = getIntent().getBooleanExtra("isParseBoolean",false);
        final ArrayList<String> multi_images = getIntent().getStringArrayListExtra("multiple_images");
        final ArrayList<String> sing_images = getIntent().getStringArrayListExtra("single_images");
        final Boolean isMultipleImage = getIntent().getBooleanExtra("isMultiple",false);


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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMultipleImage){
                    if (isParseBoolean){

                        uploadOtherVehicle(district,city,multi_images,service_type,description,price,contact_num,user_Uid,title);
                    }
                }else {
                    if (isParseBoolean){

                        uploadOtherVehicle(district,city,sing_images,service_type,description,price,contact_num,user_Uid,title);
                    }
                }
            }
        });
    }

    private void uploadOtherVehicle(final Spinner district, final Spinner city, final ArrayList<String> image_Urls , final Spinner service_type, final EditText description, final EditText price, final TextView contact_num, final String user_Uid, final EditText title) {
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> {
            private ProgressDialog progress = new ProgressDialog(SparePartServiceActivity.this);
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
                    list.add(new BasicNameValuePair("district",district.getSelectedItem().toString()));
                    list.add(new BasicNameValuePair("city", city.getSelectedItem().toString()));
                    list.add(new BasicNameValuePair("service_type",service_type.getSelectedItem().toString()));
                    list.add(new BasicNameValuePair("description", description.getText().toString()));
                    list.add(new BasicNameValuePair("contact", contact_num.getText().toString()));
                    list.add(new BasicNameValuePair("user_id", user_Uid));
                    list.add(new BasicNameValuePair("price",price.getText().toString()));
                    list.add(new BasicNameValuePair("title",title.getText().toString()));

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
                RecyclerView mUploadList = findViewById(R.id.recycler_view_5);
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
                service_type.setAdapter(service_typeAdapter);

                description.setText("");
                price.setText("");
                name.setText("");
                title.setText("");
                contact_num.setText("");

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

    @SuppressLint("InflateParams")
    private void open_Popup_window() {final String district_sp = getIntent().getStringExtra("district");
        final String contact_number = getIntent().getStringExtra("contact");
        final String city_sp = getIntent().getStringExtra("city");
        final String description_sp = getIntent().getStringExtra("description");
        final String price_sp = getIntent().getStringExtra("price");
        final String title_sp = getIntent().getStringExtra("title");
        final String service_type_sp = getIntent().getStringExtra("service_type");

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
                    //System.out.println("//////////////------------>"+dataAdapter.getPosition(district_sp));
                    service_type.setSelection(service_typeAdapter.getPosition(service_type_sp));
                    contact_num.setText(contact_number);
                    description.setText(description_sp);
                    price.setText(price_sp);
                    title.setText(title_sp);

                }
            });
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    city.setSelection(cityAdapter.getPosition(city_sp));
                    popupWindow.dismiss();
                }
            });

        }

        findViewById(R.id.spare_part_activity).post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(relativeLayout, Gravity.CENTER_VERTICAL,0,0);
            }
        });
    }

    private void load_districts() {
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> {
            private ProgressDialog progress = new ProgressDialog(SparePartServiceActivity.this);
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
            Intent intent = new Intent(SparePartServiceActivity.this, PostAdsActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SparePartServiceActivity.this, PostAdsActivity.class);
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
                                Toast.makeText(SparePartServiceActivity.this, "upload failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(SparePartServiceActivity.this, "upload failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }else {
                                Toast.makeText(SparePartServiceActivity.this,"More 5 Images Selected",Toast.LENGTH_SHORT).show();
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
