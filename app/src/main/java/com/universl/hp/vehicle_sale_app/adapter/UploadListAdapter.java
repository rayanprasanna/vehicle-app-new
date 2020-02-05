package com.universl.hp.vehicle_sale_app.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.universl.hp.vehicle_sale_app.R;

import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    private List<Uri> imageUrl;
    private Context mContext;

    public UploadListAdapter(Context mContext, List<Uri> imageUrl){

        this.mContext = mContext;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_list_item_single,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(mContext)
                .asBitmap()
                .load(imageUrl.get(i))
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return imageUrl.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        ImageView image;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            image = mView.findViewById(R.id.selected_image);
        }
    }
}
