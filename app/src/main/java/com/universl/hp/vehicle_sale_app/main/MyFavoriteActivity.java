package com.universl.hp.vehicle_sale_app.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
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
import com.universl.hp.vehicle_sale_app.main.help.Help;
import com.universl.hp.vehicle_sale_app.main.response.FavoriteResponse;
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

public class MyFavoriteActivity extends AppCompatActivity implements Help {
    String androidId;
    Boolean isSinhalaLanguage = false;
    FavoriteAdapter favoriteAdapter;
    ArrayList<FavoriteResponse> favoriteResponseArrayList = new ArrayList<>();
    ListView dataList;
    ImageButton sign, search, add;
    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
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
                Intent intent = new Intent(MyFavoriteActivity.this, SearchActivity.class);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFavoriteActivity.this, InsertAdvertisementActivity.class);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }
        });
        changeLanguage(getIntent().getBooleanExtra("isSinhala", false));
        fetchFavoriteData(androidId);
    }
    private void fetchFavoriteData(final String user_id){
        final ProgressDialog progress = new ProgressDialog(MyFavoriteActivity.this);
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
        Call<ArrayList<FavoriteResponse>> call = api.getFavorite(user_id);
        call.enqueue(new Callback<ArrayList<FavoriteResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<FavoriteResponse>> call, @NonNull Response<ArrayList<FavoriteResponse>> response) {
                if (response.body() != null) {
                    progress.dismiss();
                    favoriteResponseArrayList.addAll(response.body());
                    favoriteAdapter = new FavoriteAdapter(MyFavoriteActivity.this, favoriteResponseArrayList, getIntent().getBooleanExtra("isSinhala",false));
                    dataList.setAdapter(favoriteAdapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<FavoriteResponse>> call, @NonNull Throwable t) {
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
                Intent intent = new Intent(MyFavoriteActivity.this, MenuActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("vehicleData")){
                Intent intent = new Intent(MyFavoriteActivity.this, VehicleDataActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("insertAdvertisement")){
                Intent intent = new Intent(MyFavoriteActivity.this, InsertAdvertisementActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("myAds")){
                Intent intent = new Intent(MyFavoriteActivity.this, MyAdsActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala",isSinhalaLanguage);
                intent.putExtra("android_id",androidId);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("search")){
                Intent intent = new Intent(MyFavoriteActivity.this, SearchActivity.class);
                intent.putExtra("pop", false);
                intent.putExtra("isSinhala", isSinhalaLanguage);
                startActivity(intent);
                finish();
            }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("displayAdvertisement")){
                Intent intent = new Intent(MyFavoriteActivity.this, DisplayAdvertisementActivity.class);
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
            /*Intent intent = new Intent(MyFavoriteActivity.this, MyFavoriteActivity.class);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            startActivity(intent);
            finish();*/
        }
        if (id == R.id.user_menu){
            Intent intent = new Intent(MyFavoriteActivity.this, MyAdsActivity.class);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        System.exit(0);
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
    public class FavoriteAdapter extends BaseAdapter{

        private Boolean isSinhala;
        private Context context;
        private LayoutInflater layoutInflater;
        private List<FavoriteResponse> favoriteResponses;


        @SuppressLint("HardwareIds")
        FavoriteAdapter(Context context, List<FavoriteResponse> favoriteResponses, Boolean isSinhala) {
            this.context = context;
            this.favoriteResponses = favoriteResponses;
            layoutInflater = LayoutInflater.from(context);
            this.isSinhala = isSinhala;
        }
        public class ViewHolder{
            TextView title, price;
            ImageView image,fav;
            RelativeLayout relativeLayout;
        }
        @Override
        public int getCount() {
            return favoriteResponses.size();
        }
        @Override
        public Object getItem(int position) {
            return favoriteResponses.get(position);
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
                view = layoutInflater.inflate(R.layout.favorite_list, null);
                viewHolder.title = view.findViewById(R.id.title);
                viewHolder.price = view.findViewById(R.id.price);
                viewHolder.relativeLayout = view.findViewById(R.id.layout);
                viewHolder.image = view.findViewById(R.id.vehicle_image);
                viewHolder.fav = view.findViewById(R.id.imageView_flag);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DisplayAdvertisementActivity.class);
                    intent.putExtra("id", favoriteResponses.get(position).getId());
                    intent.putExtra("isSinhala",isSinhala);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
            viewHolder.fav.setImageResource(R.drawable.dis_fav);
            viewHolder.title.setText(favoriteResponses.get(position).getTitle());
            if (isSinhala){
                viewHolder.price.setText("රු "+ favoriteResponses.get(position).getPrice());
            }else {
                viewHolder.price.setText("Rs "+ favoriteResponses.get(position).getPrice());
            }
            viewHolder.fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchFavoriteData(favoriteResponseArrayList.get(position).getF_id(), getIntent().getStringExtra("android_id"));
                    viewHolder.price.setVisibility(View.GONE);
                    viewHolder.image.setVisibility(View.GONE);
                    viewHolder.title.setVisibility(View.GONE);
                    viewHolder.fav.setVisibility(View.GONE);
                    viewHolder.relativeLayout.setVisibility(View.GONE);
                    //notify();
                }
            });
            GlideApp.with(context.getApplicationContext())
                    .load(favoriteResponses.get(position).getImage_path_1())
                    .fitCenter()
                    .into(viewHolder.image);
            return view;
        }
        private void fetchFavoriteData(final Integer id, final String user_id){
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
            Call<ArrayList<FavoriteResponse>> call = api.deleteFavorite(id, user_id, "del");
            call.enqueue(new Callback<ArrayList<FavoriteResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<FavoriteResponse>> call, @NonNull Response<ArrayList<FavoriteResponse>> response) {
                    if (response.body() != null) {
                        favoriteResponseArrayList.addAll(response.body());
                        favoriteAdapter = new FavoriteAdapter(MyFavoriteActivity.this, favoriteResponseArrayList, getIntent().getBooleanExtra("isSinhala",false));
                        dataList.setAdapter(favoriteAdapter);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ArrayList<FavoriteResponse>> call, @NonNull Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getStringExtra("activity").equalsIgnoreCase("menu")){
            Intent intent = new Intent(MyFavoriteActivity.this, MenuActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("vehicleData")){
            Intent intent = new Intent(MyFavoriteActivity.this, VehicleDataActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            intent.putExtra("type", getIntent().getStringExtra("type"));
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("insertAdvertisement")){
            Intent intent = new Intent(MyFavoriteActivity.this, InsertAdvertisementActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("myAds")){
            Intent intent = new Intent(MyFavoriteActivity.this, MyAdsActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala",isSinhalaLanguage);
            intent.putExtra("android_id",androidId);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("search")){
            Intent intent = new Intent(MyFavoriteActivity.this, SearchActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            startActivity(intent);
            finish();
        }else if (getIntent().getStringExtra("activity").equalsIgnoreCase("displayAdvertisement")){
            Intent intent = new Intent(MyFavoriteActivity.this, DisplayAdvertisementActivity.class);
            intent.putExtra("pop", false);
            intent.putExtra("isSinhala", isSinhalaLanguage);
            intent.putExtra("id", getIntent().getIntExtra("id",0));
            startActivity(intent);
            finish();
        }
    }
}
