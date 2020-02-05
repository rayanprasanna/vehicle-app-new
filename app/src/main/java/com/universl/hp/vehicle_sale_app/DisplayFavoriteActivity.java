package com.universl.hp.vehicle_sale_app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universl.hp.vehicle_sale_app.items.GlideApp;
import com.universl.hp.vehicle_sale_app.response.FavoriteResponse;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class DisplayFavoriteActivity extends AppCompatActivity {

    private List<FavoriteResponse> favoriteResponses,getFavoriteResponses;
    private ProgressDialog progress;
    private String user_email;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_favorite);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progress = new ProgressDialog(DisplayFavoriteActivity.this);
        getFavoriteDetails();
    }

    private void getFavoriteDetails() {
        @SuppressLint("StaticFieldLeak")
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(DisplayFavoriteActivity.this);
        if (account != null){
            user_email = account.getEmail();
        }
        class Network extends AsyncTask<String,Void,String>{
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
                getFavoriteResponses = new ArrayList<>();
                ListView listView;
                final ArrayList<String> brandArrayList = new ArrayList<>();
                final ArrayList<String> modelArrayList = new ArrayList<>();
                final ArrayList<String> yearArrayList = new ArrayList<>();
                final ArrayList<String> priceArrayList = new ArrayList<>();
                favoriteResponses = new Gson().fromJson(s,
                        new TypeToken<List<FavoriteResponse>>(){
                        }.getType());

                listView = findViewById(R.id.listView_2);
                for (int i = 0; i < favoriteResponses.size(); i++){
                    /*if (favoriteResponses.get(i).getUser_email().equalsIgnoreCase(user_email)){
                        getFavoriteResponses.add(favoriteResponses.get(i));
                    }*/
                }

                for (int j = 0; j < getFavoriteResponses.size(); j++){
                    if (getFavoriteResponses.get(j).getBrand().equalsIgnoreCase("")){
                        brandArrayList.add(getFavoriteResponses.get(j).getTitle());
                        removedDuplicates(brandArrayList);
                    }else {
                        brandArrayList.add(getFavoriteResponses.get(j).getBrand());
                        removedDuplicates(brandArrayList);
                        modelArrayList.add(getFavoriteResponses.get(j).getModel());
                        removedDuplicates(modelArrayList);
                        yearArrayList.add(getFavoriteResponses.get(j).getModel_year());
                        removedDuplicates(yearArrayList);
                    }
                    priceArrayList.add(getFavoriteResponses.get(j).getPrice());
                    removedDuplicates(priceArrayList);
                }
                BaseAdapter baseAdapter = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return brandArrayList.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return brandArrayList.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        convertView = LayoutInflater.from(DisplayFavoriteActivity.this).inflate(R.layout.image_detail_single_list, null);
                        TextView brand = convertView.findViewById(R.id.brand);
                        TextView model = convertView.findViewById(R.id.model);
                        TextView year = convertView.findViewById(R.id.year);
                        TextView price = convertView.findViewById(R.id.price);
                        ImageView ads = convertView.findViewById(R.id.selected_image);
                        PhotoView photo = convertView.findViewById(R.id.photoView);
                        /*if (getFavoriteResponses.get(position).getBrand().equalsIgnoreCase("")){
                            brand.setText(getFavoriteResponses.get(position).getTitle());
                        }else {
                            brand.setText(getFavoriteResponses.get(position).getBrand());
                            model.setText(getFavoriteResponses.get(position).getModel());
                            year.setText(getFavoriteResponses.get(position).getModel_year());
                        }*/
                        brand.setText(brandArrayList.get(position));
                        model.setText(modelArrayList.get(position));
                        year.setText(yearArrayList.get(position));
                        price.setText("Rs "+ getFavoriteResponses.get(position).getPrice());
                        GlideApp
                                .with(DisplayFavoriteActivity.this)
                                .load(getFavoriteResponses.get(position).getImage_path_1())
                                .fitCenter()
                                .override(500,500)
                                .into(photo);

                        photo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DisplayFavoriteActivity.this,DisplayDetailsActivity.class);
                                intent.putExtra("activity","favorites");
                                intent.putExtra("image_path_1",getFavoriteResponses.get(position).getImage_path_1());
                                startActivity(intent);
                                finish();
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

    List<String> removedDuplicates(List<String> list){
        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }
    @Override
    public void onPause() {
        super.onPause();

        if ((progress != null) && progress.isShowing())
            progress.dismiss();
        progress = null;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent = new Intent(DisplayFavoriteActivity.this, MainMenuActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DisplayFavoriteActivity.this, MainMenuActivity.class);
        startActivity(intent);
        this.finish();
    }
}
