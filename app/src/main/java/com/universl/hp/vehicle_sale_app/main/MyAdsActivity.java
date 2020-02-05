package com.universl.hp.vehicle_sale_app.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.items.GlideApp;
import com.universl.hp.vehicle_sale_app.main.adapter.UrAdsAdapter;
import com.universl.hp.vehicle_sale_app.main.help.Help;
import com.universl.hp.vehicle_sale_app.main.response.VehicleResponse;
import com.universl.hp.vehicle_sale_app.main.service.FavoriteAPIService;
import com.universl.hp.vehicle_sale_app.main.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyAdsActivity extends AppCompatActivity implements Help {
    String androidId;
    Boolean isSinhalaLanguage = false;
    UrAdsAdapter urAdsAdapter;
    ArrayList<VehicleResponse> vehicleResponseArrayList = new ArrayList<>();
    ListView dataList;
    ImageButton sign, search, add;
    AlertDialog.Builder alert;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);
        Toolbar toolbar = findViewById(R.id.search_bar);
        sign = findViewById(R.id.sign); search = findViewById(R.id.search); add = findViewById(R.id.add);
        dataList = findViewById(R.id.vehicle_data_listView);

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#ffffff'>වාහන</font>"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage(!isSinhalaLanguage);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAdsActivity.this, SearchActivity.class);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAdsActivity.this, InsertAdvertisementActivity.class);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }
        });
        changeLanguage(getIntent().getBooleanExtra("isSinhala", false));
        fetchUrAdsData(getIntent().getStringExtra("android_id"));
    }

    @Override
    public void changeLanguage(Boolean isSinhala) {
        if (isSinhala){
            sign.setImageResource(R.drawable.si);
            isSinhalaLanguage = true;
        }else {
            sign.setImageResource(R.drawable.en);
            isSinhalaLanguage = false;
        }
    }
    private void deleteUrAdsData(final String user_id, final Integer id){
        final ProgressDialog progress = new ProgressDialog(MyAdsActivity.this);
        progress.setIcon(R.mipmap.ic_icon);
        progress.setTitle("වාහන");
        progress.setMessage("Deleting!");
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
        Call<ArrayList<VehicleResponse>> call = api.deleteUrAdsData(user_id, id);
        call.enqueue(new Callback<ArrayList<VehicleResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VehicleResponse>> call, @NonNull Response<ArrayList<VehicleResponse>> response) {
                if (response.body() != null) {
                    progress.dismiss();
                    alert = new AlertDialog.Builder(MyAdsActivity.this);
                    alert.setIcon(R.mipmap.ic_icon);
                    alert.setTitle(getString(R.string.app_name));
                    alert.setMessage("Delete is a success. Thank you !.");
                    alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MyAdsActivity.this,MyAdsActivity.class);
                            intent.putExtra("android_id",getIntent().getStringExtra("android_id"));
                            startActivity(intent);
                            finish();
                        }
                    });
                    alert.create().show();
                    vehicleResponseArrayList.addAll(response.body());
                    urAdsAdapter = new UrAdsAdapter(MyAdsActivity.this, vehicleResponseArrayList, getIntent().getBooleanExtra("isSinhala",false));
                    dataList.setAdapter(urAdsAdapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<VehicleResponse>> call, @NonNull Throwable t) {
                progress.dismiss();
                System.out.println(t.getMessage());
            }
        });
    }
    private void fetchUrAdsData(final String user_id){
        final ProgressDialog progress = new ProgressDialog(MyAdsActivity.this);
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
        Call<ArrayList<VehicleResponse>> call = api.getUrAdsData(user_id);
        call.enqueue(new Callback<ArrayList<VehicleResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<VehicleResponse>> call, @NonNull Response<ArrayList<VehicleResponse>> response) {
                if (response.body() != null) {
                    progress.dismiss();
                    vehicleResponseArrayList.addAll(response.body());
                    urAdsAdapter = new UrAdsAdapter(MyAdsActivity.this, vehicleResponseArrayList, getIntent().getBooleanExtra("isSinhala",false));
                    dataList.setAdapter(urAdsAdapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<VehicleResponse>> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
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
                Intent intent = new Intent(MyAdsActivity.this, MenuActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("vehicleData")){
                Intent intent = new Intent(MyAdsActivity.this, VehicleDataActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("myFavorite")){
                Intent intent = new Intent(MyAdsActivity.this, MyFavoriteActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("search")){
                Intent intent = new Intent(MyAdsActivity.this, SearchActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("insertAdvertisement")){
                Intent intent = new Intent(MyAdsActivity.this, InsertAdvertisementActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("displayAdvertisement")){
                Intent intent = new Intent(MyAdsActivity.this, DisplayAdvertisementActivity.class);
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
            Intent intent = new Intent(MyAdsActivity.this, MyFavoriteActivity.class);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            startActivity(intent);
            finish();
        }
        if (id == R.id.user_menu){
            /*Intent intent = new Intent(MyAdsActivity.this, MyAdsActivity.class);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            startActivity(intent);
            finish();*/
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        System.exit(0);
    }

    public class UrAdsAdapter extends BaseAdapter {

        private Boolean isSinhala;
        private Context context;
        private LayoutInflater layoutInflater;
        private List<VehicleResponse> vehicleResponses;


        @SuppressLint("HardwareIds")
        UrAdsAdapter(Context context, List<VehicleResponse> vehicleResponses, Boolean isSinhala) {
            this.context = context;
            this.vehicleResponses = vehicleResponses;
            layoutInflater = LayoutInflater.from(context);
            this.isSinhala = isSinhala;
        }
        public class ViewHolder{
            TextView message, price, title;
            ImageView image, del;
            RelativeLayout relativeLayout;
        }
        @Override
        public int getCount() {
            return vehicleResponses.size();
        }
        @Override
        public Object getItem(int position) {
            return vehicleResponses.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @SuppressLint({"InflateParams", "SetTextI18n"})
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (view == null){
                viewHolder = new ViewHolder();
                view = layoutInflater.inflate(R.layout.my_list, null);
                viewHolder.message = view.findViewById(R.id.message);
                viewHolder.price = view.findViewById(R.id.price);
                viewHolder.title = view.findViewById(R.id.title);
                viewHolder.relativeLayout = view.findViewById(R.id.layout);
                viewHolder.image = view.findViewById(R.id.vehicle_image);
                viewHolder.del = view.findViewById(R.id.imageView_flag);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DisplayAdvertisementActivity.class);
                    intent.putExtra("id", vehicleResponses.get(position).getId());
                    intent.putExtra("isSinhala",isSinhala);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
            viewHolder.del.setImageResource(R.drawable.delete);
            viewHolder.title.setText(vehicleResponses.get(position).getTitle());
            viewHolder.message.setText("Disapproved");
            if (isSinhala){
                viewHolder.price.setText("රු "+ vehicleResponses.get(position).getPrice());
            }else {
                viewHolder.price.setText("Rs "+ vehicleResponses.get(position).getPrice());
            }
            viewHolder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteUrAdsData(getIntent().getStringExtra("android_id"), vehicleResponses.get(position).getId() );
                    viewHolder.price.setVisibility(View.GONE);
                    viewHolder.image.setVisibility(View.GONE);
                    viewHolder.title.setVisibility(View.GONE);
                    viewHolder.del.setVisibility(View.GONE);
                    viewHolder.relativeLayout.setVisibility(View.GONE);
                    //notify();
                }
            });
            GlideApp.with(context.getApplicationContext())
                    .load(vehicleResponses.get(position).getImage_path_1())
                    .fitCenter()
                    .into(viewHolder.image);
            return view;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getStringExtra("activity").equalsIgnoreCase("menu")){
            Intent intent = new Intent(MyAdsActivity.this, MenuActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("vehicleData")){
            Intent intent = new Intent(MyAdsActivity.this, VehicleDataActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("myFavorite")){
            Intent intent = new Intent(MyAdsActivity.this, MyFavoriteActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("search")){
            Intent intent = new Intent(MyAdsActivity.this, SearchActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("insertAdvertisement")){
            Intent intent = new Intent(MyAdsActivity.this, InsertAdvertisementActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("displayAdvertisement")){
            Intent intent = new Intent(MyAdsActivity.this, DisplayAdvertisementActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }
    }
}
