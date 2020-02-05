package com.universl.hp.vehicle_sale_app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.universl.hp.vehicle_sale_app.R;
import com.universl.hp.vehicle_sale_app.ServiceStationActivity;
import com.universl.hp.vehicle_sale_app.response.FavoriteResponse;
import com.universl.hp.vehicle_sale_app.response.SparePartServiceResponse;

/*import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;*/

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchServiceAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<SparePartServiceResponse> sparePartServiceResponses;
    private ArrayList<SparePartServiceResponse> sparePartServiceResponseArrayList ;
    private List<FavoriteResponse> favoriteResponses;
    private List<String> image_paths,my_favorite_image_path;
    private String user_id;
    private String androidId;
    private List<String> service;
    private ArrayList<String> arrayList;


    public SearchServiceAdapter(Context context, List<String> service) {
        this.context = context;
        this.service = service;
        layoutInflater = LayoutInflater.from(context);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(service);
    }

    @SuppressLint("HardwareIds")
    public SearchServiceAdapter(Context context, List<SparePartServiceResponse>sparePartServiceResponses, List<FavoriteResponse>favoriteResponses, List<String>image_paths, List<String>my_favorite_image_path, String user_id){
        this.context = context;
        this.sparePartServiceResponses = sparePartServiceResponses;
        layoutInflater = LayoutInflater.from(context);
        sparePartServiceResponseArrayList = new ArrayList<>();
        sparePartServiceResponseArrayList.addAll(sparePartServiceResponses);
        this.favoriteResponses = favoriteResponses;
        this.image_paths = image_paths;
        this.my_favorite_image_path = my_favorite_image_path;
        this.user_id = user_id;
        androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
    public class ViewHolder{
        TextView title,price,description,like_count;
        ImageView image;
        PhotoView photo;
        ImageButton favorite;
        RelativeLayout relativeLayout;
        Boolean isClickFavoriteButton;
        Button button;
    }
    @Override
    public int getCount() {
        return service.size();
    }

    @Override
    public Object getItem(int position) {
        return service.get(position);
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
            viewHolder = new SearchServiceAdapter.ViewHolder();
            view = layoutInflater.inflate(R.layout.service_button_list, null);

            // Locate the TextViews in listView_item.xml
            /*viewHolder.title = view.findViewById(R.id.brand);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.relativeLayout = view.findViewById(R.id.image_details);
            viewHolder.favorite = view.findViewById(R.id.title_favorite);
            viewHolder.like_count = view.findViewById(R.id.title_like);*/
            viewHolder.button = view.findViewById(R.id.button_list_id);
            //viewHolder.image = convertView.findViewById(R.id.selected_image);

            //viewHolder.photo = view.findViewById(R.id.photoView);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ServiceStationActivity.class);
                intent.putExtra("s_name",service.get(position));
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        viewHolder.button.setText(service.get(position));
        /*viewHolder.like_count.setText(String.valueOf(Collections.frequency(image_paths,sparePartServiceResponses.get(position).getImage_path_1())));
        if (!favoriteResponses.isEmpty()){
            for (int j = 0; j < sparePartServiceResponses.size(); j++){
                for (int k = 0; k < favoriteResponses.size(); k++ ){
                    if (sparePartServiceResponses.get(j).getImage_path_1().equals(favoriteResponses.get(k).getImage_path_1())){
                        viewHolder.favorite.setBackgroundResource(R.drawable.ic_favorite_y);
                        System.out.println(favoriteResponses.get(k).getImage_path_1() + " <---> "+ sparePartServiceResponses.get(j).getImage_path_1() + " <---> "+ image_paths.size());
                    }
                }
            }
        }
        if (Collections.frequency(my_favorite_image_path,sparePartServiceResponses.get(position).getImage_path_1()) == 0){
            viewHolder.isClickFavoriteButton = false;
            viewHolder.favorite.setBackgroundResource(R.drawable.ic_favorite_n);
        }if (Collections.frequency(my_favorite_image_path,sparePartServiceResponses.get(position).getImage_path_1()) > 0){
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
                    *//*Intent intent = ((Activity)context).getIntent();
                    ((Activity)context).finish();
                    context.startActivity(intent);*//*
                }else {
                    viewHolder.isClickFavoriteButton = false;
                    //isJobFavorite = "false";
                    viewHolder.favorite.setBackgroundResource(R.drawable.ic_favorite_n);
                    //search_titleActivity.delete_favorite();
                    delete_favorite(user_id,position);
                    *//*Intent intent = ((Activity)context).getIntent();
                    ((Activity)context).finish();
                    context.startActivity(intent);*//*
                }
            }
        });
        *//*if (position == 0 || position == 1 || position == 2){
            viewHolder.relativeLayout.setBackgroundResource(android.R.color.holo_orange_light);
        }else {
            viewHolder.relativeLayout.setBackgroundResource(android.R.color.transparent);
        }*//*
         *//*viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DisplayDetailsActivity.class);
                intent.putExtra("activity","service");
                intent.putExtra("image_path_1",sparePartServiceResponses.get(position).getImage_path_1());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });*//*
        if (sparePartServiceResponses.get(position).getStatus().equalsIgnoreCase("true")){
            viewHolder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,DisplayDetailsActivity.class);
                    intent.putExtra("activity","service");
                    intent.putExtra("image_path_1",sparePartServiceResponses.get(position).getImage_path_1());
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
            viewHolder.title.setText(sparePartServiceResponses.get(position).getTitle());
            viewHolder.price.setText("Rs " + sparePartServiceResponses.get(position).getPrice());
            GlideApp.with(context.getApplicationContext())
                    .load(sparePartServiceResponses.get(position).getImage_path_1())
                    .fitCenter()
                    .into(viewHolder.photo);
        }*/
        return view;
    }
    /*private void upload_favorite(final int position){
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
                    list.add(new BasicNameValuePair("title",sparePartServiceResponses.get(position).getTitle()));
                    list.add(new BasicNameValuePair("user_phone_id",user_id));
                    list.add(new BasicNameValuePair("district",sparePartServiceResponses.get(position).getDistrict()));
                    list.add(new BasicNameValuePair("city",sparePartServiceResponses.get(position).getCity()));
                    //list.add(new BasicNameValuePair("vehicle_type",carResponses.get(position).getVehicle_type()));
                    //list.add(new BasicNameValuePair("service_type",carResponses.get(position).getS));
                    //list.add(new BasicNameValuePair("contact",favorite_contact));
                    //list.add(new BasicNameValuePair("sector",favorite_sector));
                    //list.add(new BasicNameValuePair("appearance",favorite_appearance));
                    //list.add(new BasicNameValuePair("logo",favorite_job_logo));
                    //list.add(new BasicNameValuePair("publish_date",favorite_publish_date));
                    list.add(new BasicNameValuePair("image_path_1",sparePartServiceResponses.get(position).getImage_path_1()));
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
                *//*Intent intent = new Intent(context,context.getClass());
                context.getApplicationContext().startActivity(intent);
                ((Activity)context).finish();*//*
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
                    list.add(new BasicNameValuePair("image_path_1",sparePartServiceResponses.get(position).getImage_path_1()));
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
                *//*Intent intent = new Intent(context,context.getClass());
                context.getApplicationContext().startActivity(intent);
                ((Activity)context).finish();*//*
            }
        }
        new Network().execute();
    }*/
    public void filter_service(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        service.clear();
        if (charText.length() == 0) {
            service.addAll(arrayList);
        } else {
            for (String response : arrayList) {
                if (response.toLowerCase(Locale.getDefault()).contains(charText)) {
                    service.add(response);
                }
            }
        }
        notifyDataSetChanged();
    }
}
