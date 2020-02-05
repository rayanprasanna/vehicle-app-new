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
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universl.hp.vehicle_sale_app.adapter.SearchBrand;
import com.universl.hp.vehicle_sale_app.adapter.SpinnerAdapter;
import com.universl.hp.vehicle_sale_app.items.ItemData;
import com.universl.hp.vehicle_sale_app.items.VehicleAds;
import com.universl.hp.vehicle_sale_app.response.CarResponse;
import com.universl.hp.vehicle_sale_app.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.response.SparePartServiceResponse;
import com.universl.hp.vehicle_sale_app.utils.Constants;

/*
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
*/

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
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MainMenuActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 123;
    private Context context;
    private Activity activity;
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
    private SearchBrand searchBrand;
    private WifiManager wifiManager;
    AlertDialog.Builder alert;
    List<FavoriteResponse> favoriteResponses;
    List<String> image_path,favorite_image_path;
    String androidId;
    private AdView adView;
    GridView gridView;

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
        setContentView(R.layout.activity_main_menu);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        linearLayout = findViewById(R.id.main_linear_layout);
        context = getApplicationContext();
        activity = MainMenuActivity.this;

        /*ImageButton hit_ads = findViewById(R.id.ads_id);
        ImageButton car_ads = findViewById(R.id.car_id);
        ImageButton van_ads = findViewById(R.id.van_id);
        ImageButton service_ads = findViewById(R.id.service_id);*/
        brand = (SearchView) findViewById(R.id.search_view);
        ads = findViewById(R.id.ads_type);gridView = findViewById(R.id.gridView);
        FloatingTextButton floatingActionButton = findViewById(R.id.action_button);
        progress = new ProgressDialog(MainMenuActivity.this);
        ads_types = new ArrayList<>();
        getCarResponses = new ArrayList<>();
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final Boolean[] isClickOk = {false};
        if (!isConnect()){
            //Toast.makeText(MainMenuActivity.this,"Wifi Or Network is not connect!",Toast.LENGTH_SHORT).show();
            alert = new AlertDialog.Builder(MainMenuActivity.this);
            alert.setTitle("වාහන");
            alert.setMessage("ඔබට අන්තර්ජාල සම්බන්ධතාවයක් නොමැත");
            alert.setPositiveButton("පිටවීම", new DialogInterface.OnClickListener() {
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
        ads_types.add(new ItemData("දැන්විම් වර්ග",R.drawable.advertise));
        ads_types.add(new ItemData("ලුහුඬු දැන්විම්",R.drawable.v_ads));
        ads_types.add(new ItemData("කාර් දැන්විම්",R.drawable.v_car));
        ads_types.add(new ItemData("වෙනත් වාහන දැන්විම්",R.drawable.v_other_sale));
        ads_types.add(new ItemData("මෝටර් සයිකල් දැන්වීම්",R.drawable.motor_bike));
        ads_types.add(new ItemData("අමතර කොටස් හා සේවා",R.drawable.v_service));

        spinnerAdapter = new SpinnerAdapter(this,R.layout.spinner_layout,R.id.ads_type,ads_types);
        ads.setAdapter(spinnerAdapter);
        ads.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = parent.getSelectedItemPosition();
                if (position == 1){
                    Intent intent = new Intent(MainMenuActivity.this,LuhunduAdvertisementActivity.class);
                    startActivity(intent);
                    finish();
                }else if (position == 2){
                    Intent intent = new Intent(MainMenuActivity.this,CarSaleActivity.class);
                    startActivity(intent);
                    finish();
                }else if (position == 3){
                    Intent intent = new Intent(MainMenuActivity.this,OtherVehicleSaleActivity.class);
                    startActivity(intent);
                    finish();
                }else if (position == 4){
                    Intent intent = new Intent(MainMenuActivity.this,MotorBikeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 5){
                    Intent intent = new Intent(MainMenuActivity.this,SparePartSaleActivity.class);
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
                Intent intent = new Intent(MainMenuActivity.this,NewAdsPostActivity.class);
                startActivity(intent);
                finish();
            }
        });
        initAds();

    }
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                wifiManager.setWifiEnabled(true);
                try {
                    setMobileDataEnabled(MainMenuActivity.this,true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //finish();
            }
        });

        return builder;
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
            private ProgressDialog progress = new ProgressDialog(MainMenuActivity.this);
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
                        gridView = findViewById(R.id.gridView);
                        searchBrand = new SearchBrand(MainMenuActivity.this,getCarResponses,favoriteResponses,image_path,favorite_image_path,androidId);
                        gridView.setAdapter(searchBrand);
                    }

                }
                new Network_2().execute();
                brand.setOnQueryTextListener(this);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    private void getAllDetails(){
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> implements SearchView.OnQueryTextListener{
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
                    return client.execute(httpget,responseHandler);
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
                final GridView gridView = findViewById(R.id.gridView);
                carResponses = new ArrayList<>();
                title = new ArrayList<>();
                price = new ArrayList<>();
                vehicle_type = new ArrayList<>();
                vehicleAds = new ArrayList<>();
                final VehicleAds[] vehicleAds_1 = new VehicleAds[1];
                final VehicleAds[] vehicleAds_2 = new VehicleAds[1];
                carResponses = new Gson().fromJson(s,
                        new TypeToken<List<CarResponse>>(){
                        }.getType());
                for (int i = 0; i < carResponses.size(); i++){
                    if (carResponses.get(i).getStatus().equalsIgnoreCase("true")){
                        vehicle_type.add(carResponses.get(i).getVehicle_type());
                        title.add(carResponses.get(i).getBrand() + " " + carResponses.get(i).getModel_year());
                        price.add("රු " + carResponses.get(i).getPrice());
                        vehicleAds_1[0] = new VehicleAds(carResponses.get(i).getBrand(),carResponses.get(i).getModel_year(),carResponses.get(i).getPrice(),carResponses.get(i).getVehicle_type(),carResponses.get(i).getImage_path_1());
                        vehicleAds.add(vehicleAds_1[0]);
                    }
                }

                //searchBrand = new SearchBrand(MainMenuActivity.this,vehicleAds);
                gridView.setAdapter(searchBrand);
                brand.setOnQueryTextListener(this);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        brand.setQuery(vehicleAds.get(position).getBrand(),true);
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
    public class GetImageUrl extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;
        public GetImageUrl(ImageView imageView){
            this.imageView = imageView;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String displayUrl = url[0];
            bitmap = null;
            //Bitmap logo = null;
            try{
                InputStream inputStream = new URL(displayUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float scaleWidth = ((float) width) / width;
                float scaleHeight = ((float) height) / height;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth,scaleHeight);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @SuppressLint("InflateParams")
    private void open_Popup_window(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams") View customView = null;
        if (inflater != null) {
            customView = inflater.inflate(R.layout.google_sign_in,null);
        }

        popupWindow = new PopupWindow(
                customView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        if(Build.VERSION.SDK_INT>=21){
            popupWindow.setElevation(5.0f);
        }

        SignInButton sign = null;
        TextView close;
        if (customView != null) {
            sign = customView.findViewById(R.id.sign_in_button);
        }
        if (sign != null) {
            close = customView.findViewById(R.id.close_popup_window_id);
            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }

        findViewById(R.id.main_linear_layout).post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(linearLayout, Gravity.CENTER_HORIZONTAL,0,0);
            }
        });
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
            Intent intent = new Intent(MainMenuActivity.this,LanguageMenuActivity.class);
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
    private void insert_user(final String user_Uid, final String user_name, final String user_email){
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> {
            private ProgressDialog progress = new ProgressDialog(MainMenuActivity.this);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setTitle(getString(R.string.app_name));
                progress.setMessage("දත්ත උඩුගත කරමින් පවති !");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Constants.BASE_URL_insert_users);
                    List<NameValuePair> list = new ArrayList<>();
                    //httpPost.setEntity(new UrlEncodedFormEntity(list,"UTF-8"));
                    list.add(new BasicNameValuePair("user_id",user_Uid));
                    list.add(new BasicNameValuePair("user_name",user_name));
                    list.add(new BasicNameValuePair("user_email",user_email));

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
                progress.dismiss();
            }
        }
        new Network().execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            }else {
                Toast.makeText(MainMenuActivity.this,"Auth went wrong",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this.activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //user_email_address = account.getEmail();
                            // Sign in success, update UI with the signed-in user's information
                            user_Uid = account.getId();
                            user_email = account.getEmail();
                            user_name = account.getDisplayName();
                            popupWindow.dismiss();
                            insert_user(user_Uid,user_name,user_email);
                            Log.d(TAG, "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainMenuActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MainMenuActivity.this,LanguageMenuActivity.class);
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
