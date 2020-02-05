package com.universl.hp.vehicle_sale_app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.universl.hp.vehicle_sale_app.DisplayDetailsActivity;
import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.items.GlideApp;
import com.universl.hp.vehicle_sale_app.response.CarResponse;
import com.universl.hp.vehicle_sale_app.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.utils.Constants;

/*import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;*/

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SearchVehicleTypeAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<CarResponse> carResponses;
    private ArrayList<CarResponse> typeArrayList;
    private List<FavoriteResponse> favoriteResponses;
    private List<String> image_paths,my_favorite_image_path;
    private String user_id;
    private String androidId;

    @SuppressLint("HardwareIds")
    public SearchVehicleTypeAdapter(Context context, List<CarResponse> carResponses,
                                    List<FavoriteResponse> favoriteResponses,
                                    List<String> image_paths, List<String> my_favorite_image_path, String user_id) {
        this.context = context;
        this.carResponses = carResponses;
        layoutInflater = LayoutInflater.from(context);
        typeArrayList = new ArrayList<>();
        typeArrayList.addAll(carResponses);
        this.favoriteResponses = favoriteResponses;
        this.image_paths = image_paths;
        this.my_favorite_image_path = my_favorite_image_path;
        this.user_id = user_id;
        androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public class ViewHolder{
        TextView brand,price,model,year,like_count;
        ImageView image;
        PhotoView photo;
        ImageButton favorite;
        RelativeLayout relativeLayout;
        Boolean isClickFavoriteButton;
    }
    @Override
    public int getCount() {
        return carResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return carResponses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final SearchVehicleTypeAdapter.ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.vahana_ad_details, null);

            // Locate the TextViews in listView_item.xml
            viewHolder.brand = view.findViewById(R.id.brand);
            viewHolder.model = view.findViewById(R.id.model);
            viewHolder.year = view.findViewById(R.id.year);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.relativeLayout = view.findViewById(R.id.image_details);
            viewHolder.favorite = view.findViewById(R.id.title_favorite);
            viewHolder.like_count = view.findViewById(R.id.title_like);
            //viewHolder.image = convertView.findViewById(R.id.selected_image);

            viewHolder.photo = view.findViewById(R.id.photoView);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.like_count.setText(String.valueOf(Collections.frequency(image_paths,carResponses.get(position).getImage_path_1())));
        if (!favoriteResponses.isEmpty()){
            for (int j = 0; j < carResponses.size(); j++){
                for (int k = 0; k < favoriteResponses.size(); k++ ){
                    if (carResponses.get(j).getImage_path_1().equals(favoriteResponses.get(k).getImage_path_1())){
                        viewHolder.favorite.setBackgroundResource(R.drawable.ic_favorite_y);
                        System.out.println(favoriteResponses.get(k).getImage_path_1() + " <---> "+ carResponses.get(j).getImage_path_1() + " <---> "+ image_paths.size());
                    }
                }
            }
        }
        if (Collections.frequency(my_favorite_image_path,carResponses.get(position).getImage_path_1()) == 0){
            viewHolder.isClickFavoriteButton = false;
            viewHolder.favorite.setBackgroundResource(R.drawable.ic_favorite_n);
        }if (Collections.frequency(my_favorite_image_path,carResponses.get(position).getImage_path_1()) > 0){
            viewHolder.isClickFavoriteButton = true;
            viewHolder.favorite.setBackgroundResource(R.drawable.ic_favorite_y);
        }
        viewHolder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewHolder.isClickFavoriteButton){
                    viewHolder.isClickFavoriteButton = true;

                    viewHolder.favorite.setBackgroundResource(R.drawable.ic_favorite_y);
                    //search_titleActivity.upload_favorite(user_email_address);
                    upload_favorite(position);
                    /*Intent intent = ((Activity)context).getIntent();
                    ((Activity)context).finish();
                    context.startActivity(intent);*/
                }else {
                    viewHolder.isClickFavoriteButton = false;
                    //isJobFavorite = "false";
                    viewHolder.favorite.setBackgroundResource(R.drawable.ic_favorite_n);
                    //search_titleActivity.delete_favorite();
                    delete_favorite(user_id,position);
                    /*Intent intent = ((Activity)context).getIntent();
                    ((Activity)context).finish();
                    context.startActivity(intent);*/
                }
            }
        });
        /*if (position == 0 || position == 1 || position == 2){
            viewHolder.relativeLayout.setBackgroundResource(android.R.color.holo_orange_light);
        }else {
            viewHolder.relativeLayout.setBackgroundResource(android.R.color.transparent);
        }*/
        /*viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DisplayDetailsActivity.class);
                intent.putExtra("activity","other");
                intent.putExtra("image_path_1",carResponses.get(position).getImage_path_1());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });*/
        if (carResponses.get(position).getStatus().equalsIgnoreCase("true")){
            viewHolder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,DisplayDetailsActivity.class);
                    intent.putExtra("activity","other");
                    intent.putExtra("image_path_1",carResponses.get(position).getImage_path_1());
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
            viewHolder.price.setText("රු "+ carResponses.get(position).getPrice() +"/=");
            GlideApp.with(context.getApplicationContext())
                    .load(carResponses.get(position).getImage_path_1())
                    .fitCenter()
                    .into(viewHolder.photo);
            /*if (carResponses.get(position).getVehicle_type().equalsIgnoreCase("යතුරැ පැදි")
                    || carResponses.get(position).getVehicle_type().equalsIgnoreCase("ස්කූටර්")){
                viewHolder.brand.setText(carResponses.get(position).getBrand());
                viewHolder.model.setText(carResponses.get(position).getModel());
                viewHolder.year.setText(carResponses.get(position).getModel_year());
            }*/
            if (carResponses.get(position).getVehicle_type().equalsIgnoreCase("Van")
                    || carResponses.get(position).getVehicle_type().equalsIgnoreCase("Bus")
                    || carResponses.get(position).getVehicle_type().equalsIgnoreCase("Lorry")){
                viewHolder.brand.setText(carResponses.get(position).getTitle());
                /*viewHolder.model.setText("");
                viewHolder.year.setText(carResponses.get(position).getModel_year());*/
            }
            if (carResponses.get(position).getVehicle_type().equalsIgnoreCase("1. Bulldozer")
                    || carResponses.get(position).getVehicle_type().equalsIgnoreCase("2. Crane")
                    || carResponses.get(position).getVehicle_type().equalsIgnoreCase("3. Digger")
                    || carResponses.get(position).getVehicle_type().equalsIgnoreCase("4. Excavation")
                    || carResponses.get(position).getVehicle_type().equalsIgnoreCase("5. Loader / Lifter")
                    || carResponses.get(position).getVehicle_type().equalsIgnoreCase("6. Roller")
                    || carResponses.get(position).getVehicle_type().equalsIgnoreCase("7. Tractor")
                    || carResponses.get(position).getVehicle_type().equalsIgnoreCase("1. Three Wheelers")
                    || carResponses.get(position).getVehicle_type().equalsIgnoreCase("2. Push Cycles")
                    || carResponses.get(position).getVehicle_type().equalsIgnoreCase("3. Other Vehicle")){
                viewHolder.brand.setText(carResponses.get(position).getTitle());
                viewHolder.model.setVisibility(View.GONE);
                viewHolder.year.setVisibility(View.GONE);
            }
        }
        return view;
    }
    private void upload_favorite(final int position){
        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String> {
            private ProgressDialog progress = new ProgressDialog(context);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setTitle(context.getString(R.string.app_name));
                progress.setMessage("Data is uploading!");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Constants.BASE_URL_insert_favorite);
                    List<NameValuePair> list = new ArrayList<>();
                    //list.add(new BasicNameValuePair("user_email",i));
                    list.add(new BasicNameValuePair("title",carResponses.get(position).getTitle()));
                    list.add(new BasicNameValuePair("user_phone_id",user_id));
                    list.add(new BasicNameValuePair("district",carResponses.get(position).getDistrict()));
                    list.add(new BasicNameValuePair("city",carResponses.get(position).getCity()));
                    //list.add(new BasicNameValuePair("vehicle_type",carResponses.get(position).getVehicle_type()));
                    //list.add(new BasicNameValuePair("service_type",carResponses.get(position).getS));
                    //list.add(new BasicNameValuePair("contact",favorite_contact));
                    //list.add(new BasicNameValuePair("sector",favorite_sector));
                    //list.add(new BasicNameValuePair("appearance",favorite_appearance));
                    //list.add(new BasicNameValuePair("logo",favorite_job_logo));
                    //list.add(new BasicNameValuePair("publish_date",favorite_publish_date));
                    list.add(new BasicNameValuePair("image_path_1",carResponses.get(position).getImage_path_1()));
                    list.add(new BasicNameValuePair("user_id",androidId));
                    httpPost.setEntity(new UrlEncodedFormEntity(list));
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
                Toast.makeText(context,"added to favorites ",Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent(context,context.getClass());
                context.getApplicationContext().startActivity(intent);
                ((Activity)context).finish();*/
            }
        }
        new Network().execute();
    }

    private void delete_favorite(final String user_id,final int position){

        @SuppressLint("StaticFieldLeak")
        class Network extends AsyncTask<String,Void,String>{
            private ProgressDialog progress = new ProgressDialog(context);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setTitle(context.getString(R.string.app_name));
                progress.setMessage("Deleting Favorite!");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Constants.BASE_URL_delete_favorite);
                    List<NameValuePair> list = new ArrayList<>();
                    list.add(new BasicNameValuePair("image_path_1",carResponses.get(position).getImage_path_1()));
                    list.add(new BasicNameValuePair("user_id",user_id));
                    httpPost.setEntity(new UrlEncodedFormEntity(list));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    return httpClient.execute(httpPost, responseHandler);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Delete successfully!";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progress.dismiss();
                Toast.makeText(context,"delete to favorites ",Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent(context,context.getClass());
                context.getApplicationContext().startActivity(intent);
                ((Activity)context).finish();*/
            }
        }
        new Network().execute();
    }
    public void filter_brand(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        carResponses.clear();
        if (charText.length() == 0) {
            carResponses.addAll(typeArrayList);
        } else {
            for (CarResponse response : typeArrayList) {
                if (response.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    carResponses.add(response);
                }
            }
        }
        notifyDataSetChanged();
    }
}
